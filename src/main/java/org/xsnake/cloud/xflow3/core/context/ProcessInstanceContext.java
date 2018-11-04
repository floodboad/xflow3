package org.xsnake.cloud.xflow3.core.context;

import org.xsnake.cloud.xflow3.api.ProcessInstance;
import org.xsnake.cloud.xflow3.core.ProcessDefinition;

public class ProcessInstanceContext extends Context {

	protected ApplicationContext applicationContext;
	
	protected ProcessInstance processInstance;
	
	protected ProcessDefinition processDefinition;

	public ProcessInstanceContext(ApplicationContext applicationContext , ProcessInstance processInstance) {
		this.applicationContext = applicationContext;
		this.processInstance = processInstance;
	}
	
	public ProcessInstanceContext(ApplicationContext applicationContext , String processInstanceId) {
		this.applicationContext = applicationContext;
		processInstance = applicationContext.getDaoUtil().$queryObject("PROCESS_INSTANCE_GET_BY_ID.sql",
				applicationContext.getDaoUtil().createMap("processInstanceId", processInstanceId),ProcessInstance.class);
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}
	
	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

}
