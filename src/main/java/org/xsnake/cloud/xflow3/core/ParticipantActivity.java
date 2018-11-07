package org.xsnake.cloud.xflow3.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.dom4j.Element;
import org.xsnake.cloud.dao.DaoUtil;
import org.xsnake.cloud.xflow3.api.Participant;
import org.xsnake.cloud.xflow3.api.Task;
import org.xsnake.cloud.xflow3.api.exception.XflowBusinessException;
import org.xsnake.cloud.xflow3.api.exception.XflowDefinitionException;
import org.xsnake.cloud.xflow3.core.context.ApplicationContext;
import org.xsnake.cloud.xflow3.core.context.IContext;
import org.xsnake.cloud.xflow3.core.context.OperateContext;
import org.xsnake.cloud.xflow3.core.context.ProcessInstanceContext;

public abstract class ParticipantActivity extends Activity{

	private static final long serialVersionUID = 1L;
	
	//该属性是设置如果参与者为空则自动完成任务
	public final static String NONE_PARTICIPANT_AUTOCOMPLETE = "NONE_PARTICIPANT_AUTOCOMPLETE";
	
	ParticipantHandle participantHandle;
	
	public ParticipantActivity(){}
	
	public ParticipantActivity(ApplicationContext context,Element activityElement) {
		super(context,activityElement);
		Element participantElement = activityElement.element(DefinitionConstant.ELEMENT_ACTIVITY_PARTICIPANT);
		if(participantElement==null){
			throw new XflowDefinitionException("任务类型活动必须指定参与者");
		}
		//初始化参与者处理器
		participantHandle = parseParticipantHandle(context,participantElement);
	}
	
	private ParticipantHandle parseParticipantHandle(ApplicationContext context, Element participantElement) {
		String type = participantElement.attributeValue(DefinitionConstant.ELEMENT_ACTIVITY_PARTICIPANT_TYPE);
		Constructor<? extends ParticipantHandle> constructor = null;
		ParticipantHandle participantHandle = null;
		Class<? extends ParticipantHandle> participantHandleCls = context.getParticipantHandleRegister().getParticipantHandle(type);
		try {
			constructor = participantHandleCls.getDeclaredConstructor(Element.class);
		} catch (NoSuchMethodException e) {
			throw new XflowDefinitionException("自定义的参与者类没有找到构造函数 ："+e.getMessage());
		} catch(SecurityException e){
			throw new XflowDefinitionException("自定义的参与者类的构造函数不能访问 ："+e.getMessage());
		}
		
		try {
			participantHandle = constructor.newInstance(participantElement);
			participantHandle.activity = this;
		} catch (InstantiationException 
				| IllegalAccessException 
				| IllegalArgumentException e) {
			throw new XflowDefinitionException("自定义的参与者类实例化出错："+e.getMessage());
		} catch (InvocationTargetException e) {
			throw new XflowDefinitionException("自定义的参与者类实例化出错："+e.getCause().getMessage());
		}

		return participantHandle;
	}
	
	public final void createTask(ProcessInstanceContext context){
		List<Participant> participantList = participantHandle._findParticipantList(context);
		String recordId = (String)context.getAttribute(IContext.RECORD_ID);
		for(Participant participant : participantList){
			String taskId = UUID.randomUUID().toString();
			DaoUtil daoUtil = context.getApplicationContext().getDaoUtil();
			daoUtil.$update("PROCESS_INSTANCE_TASK_ADD.sql", daoUtil.createMap()
					.put("TASK_ID", taskId)
					.put("RECORD_ID", recordId)
					.put("PARTICIPANT_ID", participant.getId())
					.put("PARTICIPANT_NAME", participant.getName())
					.put("PARTICIPANT_TYPE", participant.getType())
					.put("CREATE_DATE", new Date())
					.put("PARENT_ID", null)
					.put("STATUS", "RUNNING")
					.put("TASK_TYPE", this.type)
			);
		}
	}
	
	protected abstract List<Transition> doTask(OperateContext context);
	
	@Override
	public void definitionValidate(ApplicationContext context) {
		if(toTransitionList.size() != 1){
			throw new XflowDefinitionException("任务有且只能有一个出口路径");
		}
	}
	
	//从人工参与环节进来的请求都为人为操作的，其上线文必为OperateContext
	@Override
	public List<Transition> doAutomaticWork(ProcessInstanceContext context){
		throw new XflowBusinessException("程序内部错误");
	}
	
	public List<Transition> doParticipantTask(OperateContext context){
		Task task = context.getTask();
		DaoUtil daoUtil = context.getApplicationContext().getDaoUtil();
		//删除任务
		daoUtil.$update("PROCESS_INSTANCE_TASK_DELETE.sql", daoUtil.createMap("taskId",task.getTaskId()));
		//记录任务操作历史。
		daoUtil.$update("PROCESS_INSTANCE_HISTORY_ADD.sql",daoUtil.createMap()
				.put("HISTORY_TASK_ID", task.getTaskId())
				.put("RECORD_ID", task.getRecordId())
				.put("OPERATOR_ID", context.getOperator().getId())
				.put("OPERATOR_NAME", context.getOperator().getName())
				.put("OPERATOR_TYPE", context.getOperator().getType())
				.put("SUGGESTION", context.getSuggestion())
				.put("OPERATE_DATE", new Date())
				.put("OPERATE_TYPE", context.getOperateType().toString())
				.put("PARTICIPANT_ID", task.getParticipantId())
				.put("PARTICIPANT_NAME", task.getParticipantName())
				.put("PARTICIPANT_TYPE", task.getParticipantType())
				.put("TASK_DATE", task.getTaskDate())
				.put("TASK_TYPE", task.getTaskType())
				.put("MULTI_TASK_RESULT", context.getMultiTaskResult())
		);
		List<Transition> resultList = doTask(context);
		return resultList;
	}
}