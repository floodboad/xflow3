package org.xsnake.cloud.xflow3.core.context;

import org.xsnake.cloud.xflow3.api.Task;

public class TaskContext extends ProcessInstanceContext {
	
	private Task task;
	
	public TaskContext(ApplicationContext applicationContext ,Task task){
		super(applicationContext, task.getProcessInstanceId());
		this.task = task;
	}

	public Task getTask() {
		return task;
	}
	
}
