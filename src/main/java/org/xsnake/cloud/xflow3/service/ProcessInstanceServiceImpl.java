package org.xsnake.cloud.xflow3.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.xsnake.cloud.common.search.Page;
import org.xsnake.cloud.dao.DaoUtil;
import org.xsnake.cloud.xflow3.api.HistoryRecord;
import org.xsnake.cloud.xflow3.api.IProcessInstanceService;
import org.xsnake.cloud.xflow3.api.Participant;
import org.xsnake.cloud.xflow3.api.ProcessInstance;
import org.xsnake.cloud.xflow3.api.ProcessInstanceCondition;
import org.xsnake.cloud.xflow3.api.Task;
import org.xsnake.cloud.xflow3.api.ITaskService.CompleteTaskForm;
import org.xsnake.cloud.xflow3.api.exception.XflowBusinessException;
import org.xsnake.cloud.xflow3.core.ProcessDefinition;
import org.xsnake.cloud.xflow3.core.context.ApplicationContext;
import org.xsnake.cloud.xflow3.core.context.ProcessInstanceContext;

@Service
@RestController
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class ProcessInstanceServiceImpl implements IProcessInstanceService{
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	DaoUtil daoUtil;
	
	public ProcessInstance start(@RequestBody ApplyForm applyForm) {
		
		String processCode = applyForm.getProcessCode(); 
		String businessKey = applyForm.getBusinessKey(); 
		String businessForm = applyForm.getBussinessForm();
		Participant creator = applyForm.getCreator();
		
		//这里要检查是否已经存在同一业务，同一流程正在创建，可以通过缓存来实现
		try {
			Map<String,Object> businessProcessMap = daoUtil.createMap("processCode", processCode).put("businessKey", businessKey);
			Long existCount = daoUtil.$queryObject("PROCESS_INSTANCE_BUSINESS_IS_RUNNING.sql", businessProcessMap,Long.class);
			if(existCount > 0){
				throw new XflowBusinessException("该业务已经正在运行中");
			}
			Map<String,Object> releaseProcessDefinition = daoUtil.$queryObject("DEFINITION_INSTANCE_GET_RELEASE_XML.sql", businessProcessMap);
			String version = (String)releaseProcessDefinition.get("VERSION");
			String xml = (String)releaseProcessDefinition.get("XML");
			ProcessDefinition processDefinition = null;
			try {
				processDefinition = ProcessDefinition.parse(applicationContext, xml);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			ProcessInstance processInstance = new ProcessInstance();
			processInstance.setBusinessKey(businessKey);
			processInstance.setCreatorId(creator.getId());
			processInstance.setCreatorName(creator.getName());
			processInstance.setCreatorType(creator.getType());
			processInstance.setProcessCode(processCode);
			processInstance.setVersion(version);
			processInstance.setParentId(null);
			processInstance.setParentActivityId(null);
			String processInstanceId = UUID.randomUUID().toString();
			processInstance.setProcessInstanceId(processInstanceId);
			//TODO 需要重构优化，可以让用户自定义主键生成策略，和标题生成策略，默认使用UUID，生成ID后要检查是否已经存在
			processInstance.setName("临时名称,这里通过定义的规则替换");
			processInstance.setStatus(IProcessInstanceService.STATUS_RUNNING);
			processInstance.setStartDate(new Date());
			processInstance.setParentId(null);
			processInstance.setBusinessForm(businessForm);
			
			daoUtil.$update("PROCESS_INSTANCE_ADD.sql", processInstance);
			ProcessInstanceContext context = new ProcessInstanceContext(applicationContext,processInstance);
			boolean end = processDefinition.startProcess(context);
			if(end){
				processInstance.setStatus(IProcessInstanceService.STATUS_END);
			}else{
				
				//流程开启后的第一个活动节点如果是任务节点并且参与者是操作人，则自动完成
				DaoUtil daoUtil = context.getApplicationContext().getDaoUtil();
				Task task = daoUtil.$queryObject("PROCESS_INSTANCE_TASK_BY_INSTANCE_OPERATOR.sql",daoUtil.createMap()
						.put("processInstanceId", context.getProcessInstance().getProcessInstanceId())
						.put("participantId", context.getProcessInstance().getCreatorId())
						.put("participantType", context.getProcessInstance().getCreatorType())
				,Task.class);
				
				CompleteTaskForm completeTaskForm = new CompleteTaskForm(); 
				completeTaskForm.setTaskId(task.getTaskId());
				completeTaskForm.setOperator(creator);
				completeTaskForm.setComment("流程开启");
				context.getApplicationContext().getTaskService().complete(completeTaskForm);
			}
			return processInstance;
		} catch (Exception e){
			throw new XflowBusinessException("异常: "+e.getMessage());
		}
		
	}
	
	@Override
	public List<ProcessInstance> getAllByBusinessKey(String businessKey) {
		return daoUtil.$queryList("PROCESS_INSTANCE_GET_ALL.sql", 
				daoUtil.createMap("businessKey",businessKey) , ProcessInstance.class);
	}

	@Override
	public ProcessInstance getRunningByBusinessKey(String processCode,String businessKey) {
		return daoUtil.$queryObject("PROCESS_INSTANCE_GET_RUNNING_BY_KEY.sql", 
				daoUtil.createMap("businessKey",businessKey).put("processCode", processCode) , ProcessInstance.class);
	}

	@Override
	public ProcessInstance getProcessInstance(String processInstanceId) {
		return daoUtil.$queryObject("PROCESS_INSTANCE_GET_BY_ID.sql", 
				daoUtil.createMap("processInstanceId",processInstanceId), ProcessInstance.class);
	}

	@Override
	public void close(String processInstanceId, Participant participant, String comment) {
		//TODO 删除所有相关的任务
//		processInstanceRepository.updateStatus(processInstanceId,IProcessInstanceService.STATUS_CLOSE);
	}

	@Override
	public void closeByBusinessKey(String definitionCode, String businessKey,Participant participant, String comment) {
//		ProcessInstance processInstance = processInstanceRepository.getRunningByBusinessKey(definitionCode, businessKey);
//		taskRepository.removeAllTask(processInstance.getId());
//		processInstanceRepository.updateStatus(processInstance.getId(),IProcessInstanceService.STATUS_CLOSE);
	}

	@Override
	public Page<ProcessInstance> query(ProcessInstanceCondition processInstanceCondition) {
//		return processInstanceRepository.query(processInstanceCondition);
		return null;
	}

	@Override
	public Page<ProcessInstance> queryJoin(ProcessInstanceCondition processInstanceCondition) {
//		return processInstanceRepository.queryJoin(processInstanceCondition);
		return null;
	}

	@Override
	public List<HistoryRecord> listHistory(String processInstanceId) {
		return null;
	}

}
