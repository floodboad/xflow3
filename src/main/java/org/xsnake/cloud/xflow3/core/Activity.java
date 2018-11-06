package org.xsnake.cloud.xflow3.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dom4j.Element;
import org.springframework.util.StringUtils;
import org.xsnake.cloud.dao.DaoUtil;
import org.xsnake.cloud.xflow3.api.exception.XflowDefinitionException;
import org.xsnake.cloud.xflow3.core.activity.EndActivity;
import org.xsnake.cloud.xflow3.core.activity.StartActivity;
import org.xsnake.cloud.xflow3.core.context.ApplicationContext;
import org.xsnake.cloud.xflow3.core.context.IContext;
import org.xsnake.cloud.xflow3.core.context.OperateContext;
import org.xsnake.cloud.xflow3.core.context.ProcessInstanceContext;

public abstract class Activity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	protected String id;
	
	protected String name;
	
	protected String type;
	
	protected String callback;
	
	protected List<Transition> fromTransitionList = new ArrayList<Transition>();
	
	protected List<Transition> toTransitionList = new ArrayList<Transition>();
	
	protected Map<String,String> attributes = new HashMap<String,String>();
	
	public Activity(){}
	
	public Activity(ApplicationContext context, final Element activityElement){
		id = activityElement.attributeValue(DefinitionConstant.ELEMENT_ACTIVITY_ATTRIBUTE_ID);
		name = activityElement.attributeValue(DefinitionConstant.ELEMENT_ACTIVITY_ATTRIBUTE_NAME);
		type = activityElement.attributeValue(DefinitionConstant.ELEMENT_ACTIVITY_ATTRIBUTE_TYPE);
		if(StringUtils.isEmpty(id)){
			throw new XflowDefinitionException("活动定义错误： id不能为空");
		}
		if(StringUtils.isEmpty(name)){
			throw new XflowDefinitionException("活动定义错误： name不能为空 ");
		}
		if(StringUtils.isEmpty(type)){
			throw new XflowDefinitionException("活动定义错误： type不能为空 ");
		}
		parseAttributes(activityElement);
		parseCallback(activityElement);
	}
	
	private void parseCallback(final Element activityElement){
		Element callbackElement = activityElement.element(DefinitionConstant.ELEMENT_ACTIVITY_CALLBACK);
		if(callbackElement == null){
			return;
		}
		callback = callbackElement.attributeValue(DefinitionConstant.ELEMENT_ACTIVITY_CALLBACK_TYPE);
	}

	private void parseAttributes(final Element activityElement) {
		Element propsElement = activityElement.element(DefinitionConstant.ELEMENT_ATTRIBUTES);
		if(propsElement == null){
			return;
		}
		@SuppressWarnings("unchecked")
		List<Element> propertyList = propsElement.elements(DefinitionConstant.ELEMENT_ATTRIBUTES_ATTRIBUTE);
		if(propertyList == null){
			return;
		}
		for(Element propertyElement : propertyList){
			attributes.put(propertyElement.attributeValue(DefinitionConstant.ELEMENT_ATTRIBUTES_ATTRIBUTE_KEY), propertyElement.getText());
		}
	}
	
	public String getAttribute(final String id) {
		return attributes.get(id);
	}
	
	public final void validate(ApplicationContext context){
		//只有开始环节是没有来源流转的，其他都必须包含来源流转，否则系统无法到达该环节
		if(!(this instanceof StartActivity)){ //如果不是开始环节
			if(fromTransitionList.size() == 0){ //并且该环节的来源为空
				throw new XflowDefinitionException("活动定义错误：[" + name + "] 没有任何一个来源路径");
			}
		}
		
		if(!(this instanceof VirtualNode || this instanceof EndActivity)){
			if(toTransitionList.size() == 0 ){
				throw new XflowDefinitionException("活动定义错误：[" + name + "] 没有任何一个出口路径");
			}
		}
		definitionValidate(context);
	}
	
	//验证流程定义是否有错误
	public abstract void definitionValidate(ApplicationContext context);
	
	protected abstract List<Transition> doAutomaticWork(ProcessInstanceContext context);
	
	protected abstract List<Transition> doParticipantTask(OperateContext context);
	
	private List<Transition> _doWork(ProcessInstanceContext context){
		List<Transition> toPathList = null;
		String recordId = null;
		if(this instanceof ParticipantActivity){
			recordId = ((OperateContext)context).getTask().getRecordId();
			toPathList = doParticipantTask((OperateContext)context);
		}else{
			recordId = activityRecord(context,this);
			toPathList = doAutomaticWork(context);
		}
		
		//节点完成后添加回调事件标示
		DaoUtil daoUtil =  context.getApplicationContext().getDaoUtil();
		daoUtil.$update("PROCESS_INSTANCE_CALLBACK_ADD.sql",daoUtil.createMap()
				.put("PROCESS_INSTANCE_ID", context.getProcessInstance().getProcessInstanceId())
				.put("BUSINESS_KEY", context.getProcessInstance().getBusinessKey())
				.put("CREATE_DATE", new Date())
				.put("CALLBACK_TYPE", callback)
				.put("STATUS", "0")
				.put("ACTIVITY_ID", id)
				.put("ACTIVITY_NAME", name)
				.put("ACTIVITY_TYPE", type)
				.put("SN", context.getApplicationContext().getNextNumberService().nextNumber(context.getProcessInstance().getProcessInstanceId()))
		);
		
		//记录转出路径
		if(toPathList != null){
			for(Transition toTransition : toPathList){
				context.getApplicationContext().getDaoUtil().$update("PATH_HISTORY_ADD.sql", 
					context.getApplicationContext().getDaoUtil().createMap()
					.put("recordId", recordId)
					.put("toPathId", toTransition.getId())
					.put("toPathName", toTransition.getName())
					.put("toActivityType",toTransition.targetActivity.getType())
					.put("toActivityName",toTransition.targetActivity.getName())
					.put("toActivityId",toTransition.targetActivity.getId())
				);
			}
		}
		return toPathList;
	}

	private String activityRecord(ProcessInstanceContext context,Activity activity) {
		String recordId = UUID.randomUUID().toString();
		context.setAttribute(IContext.RECORD_ID,recordId);
		Transition formTransition = (Transition)context.getAttribute(IContext.FROM_TRANSITION);
		DaoUtil daoUtil = context.getApplicationContext().getDaoUtil();
		daoUtil.$update("PROCESS_INSTANCE_RECORD_ADD.sql", daoUtil.createMap()
				.put("RECORD_ID", recordId)
				.put("PROCESS_INSTANCE_ID", context.getProcessInstance().getProcessInstanceId())
				.put("ACTIVITY_TYPE", activity.type)
				.put("ACTIVITY_NAME", activity.name)
				.put("ACTIVITY_ID", activity.id)
				.put("CREATE_DATE", new Date())
				.put("FROM_PATH", formTransition != null ? formTransition.getId():null)
				.put("SN", context.getApplicationContext().getNextNumberService()
						.nextNumber(context.getProcessInstance().getProcessInstanceId()))
		);
		return recordId;
	}

	//自动完成所有自动节点，直到结束或者遇到人工参与的节点等待人工完成
	public final boolean process(ProcessInstanceContext context){
		//做节点需要做的事情
		List<Transition> toPathList = _doWork(context);
		
		//如果是任务类型的实现，则广播完成的消息
		if(this instanceof ParticipantActivity){
			//TODO 发送任务完成的消息广播
		}
		
		//做完后如果是结束环节，这说明整个业务流程实例已经完全结束
		if(this instanceof EndActivity){
			return true;
		}
		
		//如果是waitAble接口实现，则有可能返回一个空的流转，这个时候不做任何处理
		//在一些情况可能会出现没有流转的情况，比如JOIN，它会等待所有的任务都到达后再往下走
		if (((this instanceof Waitable) || (this instanceof VirtualNode)) && 
			(toPathList == null || toPathList.isEmpty())){
			return false;
		}
		
		//这里记录最后流程实例是否结束
		boolean endFlag = false;
		
		//循环去做本节点产出的流转，找到流转所要去的节点，然后再做他们应该做的事情
		for(Transition transition : toPathList){
			context.setAttribute(IContext.FROM_TRANSITION, transition);
			//如果不是自动环节则是人工环节，自动环节会递归调用本方法。要么流程直到结束为止，要么遇到人工参与环节停止，等待人工处理
			Activity targetActivity = transition.targetActivity;
			if(targetActivity instanceof AutomaticActivity){
				endFlag = ((AutomaticActivity)targetActivity).process(context);
			}else{
				activityRecord(context,targetActivity);
				((ParticipantActivity)targetActivity).createTask(context);
			}
		}
		
		return endFlag;
	}


	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public List<Transition> getToTransitionList() {
		return Collections.unmodifiableList(toTransitionList);
	}

	public Map<String, String> getAttributes() {
		return Collections.unmodifiableMap(attributes);
	}
	
}
