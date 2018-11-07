package org.xsnake.cloud.xflow3.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xsnake.cloud.common.search.Page;
import org.xsnake.cloud.dao.DaoTemplate;
import org.xsnake.cloud.dao.DaoUtil;
import org.xsnake.cloud.xflow3.api.ITaskService;
import org.xsnake.cloud.xflow3.api.Participant;
import org.xsnake.cloud.xflow3.api.Task;
import org.xsnake.cloud.xflow3.api.TaskCondition;
import org.xsnake.cloud.xflow3.api.exception.XflowBusinessException;
import org.xsnake.cloud.xflow3.core.Activity;
import org.xsnake.cloud.xflow3.core.DefinitionConstant;
import org.xsnake.cloud.xflow3.core.ProcessDefinition;
import org.xsnake.cloud.xflow3.core.Transition;
import org.xsnake.cloud.xflow3.core.context.ApplicationContext;
import org.xsnake.cloud.xflow3.core.context.OperateContext;
import org.xsnake.cloud.xflow3.core.context.OperateContext.OperateType;

@Service
@RestController
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class TaskServiceImpl implements ITaskService{

	@Autowired
	DaoTemplate daoTemplate;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	DaoUtil daoUtil;
	
	@Override
	public boolean complete(@RequestBody CompleteTaskForm completeTaskForm) {
		Assert.notNull(completeTaskForm);
		//锁模式查询任务
		Task task = daoUtil.$queryObject("PROCESS_INSTANCE_TASK_LOCK.sql", daoUtil.createMap("TASK_ID", completeTaskForm.getTaskId()),Task.class);
		return complete(task, completeTaskForm.getOperator(), completeTaskForm.getComment() , null , completeTaskForm.getMultiTaskResult());
	}

	private void validate(CompleteTaskForm completeTaskForm) {
		Assert.notNull(completeTaskForm.getTaskId(), "任务不能为空");
		Assert.notNull(completeTaskForm.getOperator(), "操作者不能为空");
		Assert.notNull(completeTaskForm.getOperator().getId(), "操作者ID不能为空");
		Assert.notNull(completeTaskForm.getOperator().getName(), "操作者Name不能为空");
		Assert.notNull(completeTaskForm.getOperator().getType(), "操作者Type不能为空");
	}
	
	private boolean complete(Task task, Participant participant, String comment , String toTransitionId,String multiTaskResult) {
		if(task == null){
			throw new XflowBusinessException("任务不存在或者已经被处理！");
		}
		//如果是虚拟任务，非流程定义中的
		if(DefinitionConstant.TASK_TYPE_VIRTUAL.equals(task.getTaskType())){
			daoUtil.$update("PROCESS_INSTANCE_TASK_DELETE.sql", task);
			daoUtil.$update("PROCESS_INSTANCE_HISTORY_ADD.sql", daoUtil.createMap()
					.put("HISTORY_TASK_ID", task.getTaskId())
					.put("RECORD_ID", task.getRecordId())
					.put("OPERATOR_ID", participant.getId())
					.put("OPERATOR_NAME", participant.getName())
					.put("OPERATOR_TYPE", participant.getType())
					.put("SUGGESTION", comment)
					.put("OPERATE_DATE", new Date())
					.put("OPERATE_TYPE", OperateType.complete.toString())
					.put("PARTICIPANT_ID", task.getParticipantId())
					.put("PARTICIPANT_NAME", task.getParticipantName())
					.put("PARTICIPANT_TYPE", task.getParticipantType())
					.put("TASK_DATE", task.getTaskDate())
					.put("TASK_TYPE", task.getTaskType())
			);
	
			//如果该记录上的虚拟任务都已经完成，则将父任务唤起
			Long count = daoUtil.$queryObject("PROCESS_INSTANCE_TASK_VIRTUAL_IS_END.sql", task,Long.class);
			if(count == 0){
				daoUtil.$update("PROCESS_INSTANCE_TASK_UPDATE_STATUS.sql", 
					daoUtil.createMap("taskId",task.getTaskParentId()).put("status", "RUNNING")
				);
			}
			return false;
		}
		
		if(DefinitionConstant.TASK_TYPE_REJECT.equals(task.getTaskType())){
			daoUtil.$update("PROCESS_INSTANCE_TASK_DELETE.sql", task);
			daoUtil.$update("PROCESS_INSTANCE_HISTORY_ADD.sql", daoUtil.createMap()
					.put("HISTORY_TASK_ID", task.getTaskId())
					.put("RECORD_ID", task.getRecordId())
					.put("OPERATOR_ID", participant.getId())
					.put("OPERATOR_NAME", participant.getName())
					.put("OPERATOR_TYPE", participant.getType())
					.put("SUGGESTION", comment)
					.put("OPERATE_DATE", new Date())
					.put("OPERATE_TYPE", OperateType.complete.toString())
					.put("PARTICIPANT_ID", task.getParticipantId())
					.put("PARTICIPANT_NAME", task.getParticipantName())
					.put("PARTICIPANT_TYPE", task.getParticipantType())
					.put("TASK_DATE", task.getTaskDate())
					.put("TASK_TYPE", task.getTaskType())
			);
			//驳回操作后直接跳转回到父任务，因为任务已经不存在，所以从历史中查找并恢复
			daoUtil.$update("PROCESS_INSTANCE_TASK_ADD_BY_HISTORY.sql", daoUtil.createMap("CREATE_DATE",new Date()).put("TASK_ID", task.getTaskParentId()));
			return false;
		}
		
		//创建上下文
		OperateContext operateContext = new OperateContext(applicationContext, task, OperateType.complete, participant, comment);

		//通过任务中的流程定义信息，找到对应的定义XML TODO 优化项，缓存流程定义
		String xml = daoUtil.$queryObject("DEFINITION_INSTANCE_GET_XML.sql", task,String.class);
		ProcessDefinition processDefinition = null;
		try {
			processDefinition = ProcessDefinition.parse(applicationContext, xml);
		} catch (DocumentException e) {
			//忽略错误
		}
		
		//查找定义的活动节点
		Activity activity = processDefinition.getActivity(task.getActivityId());
		
		//设置流程的流向,如果没有指定流向则使用第一个，TODO 后续优化项
		if(toTransitionId == null){
			List<Transition> toTransitionList = activity.getToTransitionList();
			toTransitionId = toTransitionList.get(0).getId();
		}
		operateContext.setToTransitionId(toTransitionId);
		
		//工作流引擎执行
		boolean isEnd = activity.process(operateContext);
		
		//如果没有结束判断流程后续任务是否与当前操作人一致，如果一样则自动处理
		if(!isEnd){
			List<Task> taskList = daoUtil.$queryList("PROCESS_INSTANCE_TASK_BY_INSTANCE_OPERATOR.sql", daoUtil.createMap()
						.put("processInstanceId", task.getProcessInstanceId())
						.put("participantId", participant.getId())
						.put("participantType", participant.getType())
					,Task.class);
			for(Task _task : taskList){
				isEnd = complete(_task, participant, comment , null,"1");
			}
		}
		return isEnd;
	}

	@Override
	public boolean transfer(@RequestBody TransferTaskForm transferTaskForm) {
		validate(transferTaskForm);
		Assert.notNull(transferTaskForm.getAssignee(),"任务转交人不能为空");
		Assert.notNull(transferTaskForm.getAssignee().getId(),"任务转交人ID不能为空");
		Assert.notNull(transferTaskForm.getAssignee().getName(),"任务转交人NAME不能为空");
		Assert.notNull(transferTaskForm.getAssignee().getType(),"任务转交人TYPE不能为空");
		//锁定数据
		Task task = daoUtil.$queryObject("PROCESS_INSTANCE_TASK_LOCK.sql", daoUtil.createMap("TASK_ID", transferTaskForm.getTaskId()),Task.class);

		if(task == null){
			throw new XflowBusinessException("任务不存在或者已经被处理");
		}
		
		//更新任务办理人
		daoUtil.$update("PROCESS_INSTANCE_TASK_TRANSFER.sql", daoUtil.createMap()
				.put("participantId", transferTaskForm.getAssignee().getId())
				.put("participantName", transferTaskForm.getAssignee().getName())
				.put("participantType", transferTaskForm.getAssignee().getType())
				.put("taskId", transferTaskForm.getTaskId())
		);
		//记录历史
		daoUtil.$update("PROCESS_INSTANCE_HISTORY_ADD.sql", daoUtil.createMap()
				.put("HISTORY_TASK_ID", task.getTaskId())
				.put("RECORD_ID", task.getRecordId())
				.put("OPERATOR_ID", transferTaskForm.getOperator().getId())
				.put("OPERATOR_NAME", transferTaskForm.getOperator().getName())
				.put("OPERATOR_TYPE", transferTaskForm.getOperator().getType())
				.put("SUGGESTION", transferTaskForm.getComment())
				.put("OPERATE_DATE", new Date())
				.put("OPERATE_TYPE", OperateType.transfer.toString())
				.put("PARTICIPANT_ID", task.getParticipantId())
				.put("PARTICIPANT_NAME", task.getParticipantName())
				.put("PARTICIPANT_TYPE", task.getParticipantType())
				.put("TASK_DATE", task.getTaskDate())
				.put("TASK_TYPE", task.getTaskType())
		);
		return true;
	}
	

	@Override
	public boolean support(@RequestBody SupportTaskForm supportTaskForm) {
		validate(supportTaskForm);
		Assert.notEmpty(supportTaskForm.getParticipantList(),"支持人列表不能为空");
		for(Participant participant : supportTaskForm.getParticipantList()){
			Assert.notNull(participant.getId(), "支持人ID不能为空");
			Assert.notNull(participant.getName(), "支持人Name不能为空");
			Assert.notNull(participant.getType(), "支持人Type不能为空");
		}
		
		Task task = daoUtil.$queryObject("PROCESS_INSTANCE_TASK_LOCK.sql", daoUtil.createMap("TASK_ID", supportTaskForm.getTaskId()),Task.class);
		
		if(task == null){
			throw new XflowBusinessException("任务不存在或者已经被处理");
		}
		
		//记录历史
		daoUtil.$update("PROCESS_INSTANCE_HISTORY_ADD.sql", daoUtil.createMap()
				.put("HISTORY_TASK_ID", task.getTaskId())
				.put("RECORD_ID", task.getRecordId())
				.put("OPERATOR_ID", supportTaskForm.getOperator().getId())
				.put("OPERATOR_NAME", supportTaskForm.getOperator().getName())
				.put("OPERATOR_TYPE", supportTaskForm.getOperator().getType())
				.put("SUGGESTION", supportTaskForm.getComment())
				.put("OPERATE_DATE", new Date())
				.put("OPERATE_TYPE", OperateType.support.toString())
				.put("PARTICIPANT_ID", task.getParticipantId())
				.put("PARTICIPANT_NAME", task.getParticipantName())
				.put("PARTICIPANT_TYPE", task.getParticipantType())
				.put("TASK_DATE", task.getTaskDate())
				.put("TASK_TYPE", task.getTaskType())
		);
		for(Participant participant : supportTaskForm.getParticipantList()){
			daoUtil.$update("PROCESS_INSTANCE_TASK_ADD.sql", daoUtil.createMap()
					.put("TASK_ID", UUID.randomUUID().toString())
					.put("RECORD_ID", task.getRecordId())
					.put("PARTICIPANT_ID", participant.getId())
					.put("PARTICIPANT_NAME", participant.getName())
					.put("PARTICIPANT_TYPE", participant.getType())
					.put("CREATE_DATE", new Date())
					.put("PARENT_ID", task.getTaskId())
					.put("STATUS", "RUNNING")
					.put("TASK_TYPE", DefinitionConstant.TASK_TYPE_VIRTUAL)
			);
		}
		
		//更新原任务的状态，等待支持任务完成
		daoUtil.$update("PROCESS_INSTANCE_TASK_UPDATE_STATUS.sql", 
			daoUtil.createMap("taskId",task.getTaskId()).put("status", "WAITING")
		);
		return true;
	}

	@Override
	public boolean reject(@RequestBody RejectTaskForm rejectTaskForm) {
		validate(rejectTaskForm);
		Task task = daoUtil.$queryObject("PROCESS_INSTANCE_TASK_LOCK.sql", daoUtil.createMap("TASK_ID", rejectTaskForm.getTaskId()),Task.class);
		if(task == null){
			throw new XflowBusinessException("任务不存在或者已经被处理");
		}
		//查找当前流程实例下是否存在其他任务，只有一个任务存在时，才可以拒绝操作
		List<Task> taskList = daoUtil.$queryList("PROCESS_INSTANCE_TASK_BY_INSTANCE_NOT_VIRTUAL.sql", 
				daoUtil.createMap("processInstanceId", task.getProcessInstanceId()),Task.class);
		if(taskList.size() > 1){
			throw new XflowBusinessException("存在多个任务，当前无法拒绝");
		}
		//记录操作
		daoUtil.$update("PROCESS_INSTANCE_HISTORY_ADD.sql", daoUtil.createMap()
				.put("HISTORY_TASK_ID", task.getTaskId())
				.put("RECORD_ID", task.getRecordId())
				.put("OPERATOR_ID", rejectTaskForm.getOperator().getId())
				.put("OPERATOR_NAME", rejectTaskForm.getOperator().getName())
				.put("OPERATOR_TYPE", rejectTaskForm.getOperator().getType())
				.put("SUGGESTION", rejectTaskForm.getComment())
				.put("OPERATE_DATE", new Date())
				.put("OPERATE_TYPE", OperateType.reject.toString())
				.put("PARTICIPANT_ID", task.getParticipantId())
				.put("PARTICIPANT_NAME", task.getParticipantName())
				.put("PARTICIPANT_TYPE", task.getParticipantType())
				.put("TASK_DATE", task.getTaskDate())
				.put("TASK_TYPE", task.getTaskType())
		);
		String recordId = rejectTaskForm.getRecordId();
		RejectPath rejectPath = null;
		List<RejectPath> rejectPathList = null;
		if(StringUtils.isEmpty(recordId)){
			rejectPathList = daoUtil.$queryList("PROCESS_INSTANCE_REJECT_PATH.sql", 
					daoUtil.createMap("processInstanceId",task.getProcessInstanceId()),RejectPath.class);
		}else{
			rejectPathList = daoUtil.$queryList("PROCESS_INSTANCE_REJECT_PATH_BY_RECORD.sql", 
					daoUtil.createMap("processInstanceId",task.getProcessInstanceId()).put("recordId", recordId),RejectPath.class);
		}

		if(rejectPathList.size() == 0){
			throw new XflowBusinessException("没有找到可以驳回的节点");
		}
		
		rejectPath = rejectPathList.get(0);
		
		//设置历史路径
		daoUtil.$update("PATH_HISTORY_ADD.sql", daoUtil.createMap()
				.put("recordId", task.getRecordId())
				.put("toPathId", "reject")
				.put("toPathName", "驳回")
				.put("toActivityId", rejectPath.getActivityId())
				.put("toActivityName", rejectPath.getActivityName())
				.put("toActivityType", rejectPath.getActivityType())
		);

		daoUtil.$update("PROCESS_INSTANCE_TASK_DELETE_BY_INSTANCE.sql", task);
		//创建驳回任务,如果任务类型是驳回，则会走唤起原任务跳过中间过程,如果是普通任务，则会走流程引擎重新按定义流转
		if(RejectTaskForm.REJECT_JUMP.equals(rejectTaskForm.getRejectType())){
			//删除所有该流程实例中的任务
			daoUtil.$update("PROCESS_INSTANCE_TASK_ADD.sql", daoUtil.createMap()
					.put("TASK_ID", UUID.randomUUID().toString())
					.put("RECORD_ID", rejectPath.getRecordId())
					.put("PARTICIPANT_ID", rejectPath.getOperatorId())
					.put("PARTICIPANT_NAME", rejectPath.getOperatorName())
					.put("PARTICIPANT_TYPE", rejectPath.getOperatorType())
					.put("CREATE_DATE", new Date())
					.put("PARENT_ID", task.getTaskId())
					.put("STATUS", "RUNNING")
					.put("TASK_TYPE", DefinitionConstant.TASK_TYPE_REJECT)
			);
		}else{
			daoUtil.$update("PROCESS_INSTANCE_TASK_ADD.sql", daoUtil.createMap()
					.put("TASK_ID", UUID.randomUUID().toString())
					.put("RECORD_ID", rejectPath.getRecordId())
					.put("PARTICIPANT_ID", rejectPath.getOperatorId())
					.put("PARTICIPANT_NAME", rejectPath.getOperatorName())
					.put("PARTICIPANT_TYPE", rejectPath.getOperatorType())
					.put("CREATE_DATE", new Date())
					.put("PARENT_ID", null)
					.put("STATUS", "RUNNING")
					.put("TASK_TYPE", rejectPath.getTaskType())
			);
		}
		
		return true;
	}

	@Override
	public Task get(@RequestParam(value="taskId") String taskId) {
		Task task = daoUtil.$queryObject("PROCESS_INSTANCE_TASK_LOCK.sql", daoUtil.createMap("TASK_ID", taskId),Task.class);
		if(task == null){
			return null;
		}
		
		//活动流程定义信息
		String xml = daoUtil.$queryObject("DEFINITION_INSTANCE_GET_XML.sql", task,String.class);
		ProcessDefinition processDefinition = null;
		try {
			processDefinition = ProcessDefinition.parse(applicationContext, xml);
		} catch (DocumentException e) {
			//忽略错误
		}
		
		Activity taskActivity = processDefinition.getActivity(task.getActivityId());
		task.setActivityAttributes(taskActivity.getAttributes());
		
		//查找当前任务可以驳回的节点
		if(DefinitionConstant.TYPE_ACTIVITY_TASK_NORMAL.equals(task.getTaskType())){
			List<RejectPath> rejectPathList = daoUtil.$queryList("PROCESS_INSTANCE_REJECT_PATH.sql", 
					daoUtil.createMap("processInstanceId",task.getProcessInstanceId()),RejectPath.class);
			task.setRejectPathList(rejectPathList);
		}
		return task;
	}

	@Override
	public Page<Task> taskList(@RequestBody TaskCondition taskCondition) {
		List<Participant> participantList = taskCondition.getParticipantList();
		if(participantList == null || participantList.size() ==0){
			return new Page<>(new ArrayList<>(), 1, 20, 0);
		}
		
		//组装需要的FreeMarker参数
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("participantList", participantList);
		for(int i=0; i<participantList.size(); i++){
			Participant participant = participantList.get(i);
			map.put("participantId_"+i, participant.getId());
			map.put("participantType_"+i, participant.getType());
		}
		Page<Task> taskList = daoUtil.$queryPage("PROCESS_INSTANCE_TASK_LIST.sql", map,taskCondition.getPage(),taskCondition.getRows(),Task.class);
		return taskList;
	}

	@Override
	public boolean receive(@RequestBody TaskForm taskForm) {
		//PROCESS_INSTANCE_TASK_BY_INSTANCE_RECORD_LOCK.sql
		return false;
	}

}
