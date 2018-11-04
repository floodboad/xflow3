package org.xsnake.cloud.xflow3.core.activity;

import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.dao.DaoUtil;
import org.xsnake.cloud.xflow3.api.Task;
import org.xsnake.cloud.xflow3.core.AutomaticActivity;
import org.xsnake.cloud.xflow3.core.Transition;
import org.xsnake.cloud.xflow3.core.Waitable;
import org.xsnake.cloud.xflow3.core.context.ApplicationContext;
import org.xsnake.cloud.xflow3.core.context.ProcessInstanceContext;

public class JoinActivity extends AutomaticActivity implements Waitable{

	public JoinActivity(ApplicationContext context , Element activityElement) {
		super(context,activityElement);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public List<Transition> doWork(ProcessInstanceContext context){
		DaoUtil daoUtil = context.getApplicationContext().getDaoUtil();
		//存在任何能驱动流程流转的任务时，什么也不返回，一直等待，不合理的流程设计会导致流程一直等待
		List<Task> taskList = daoUtil.$queryList("PROCESS_INSTANCE_TASK_BY_INSTANCE_NOT_VIRTUAL.sql", daoUtil.createMap("processInstanceId",context.getProcessInstance().getProcessInstanceId()),Task.class);
		if(taskList.size() > 0){
			return null;
		}
		daoUtil.$update("PROCESS_INSTANCE_FORK_STATUS.sql", daoUtil.createMap()
				.put("PROCESS_INSTANCE_ID", context.getProcessInstance().getProcessInstanceId())
				.put("FORK_STATUS", ForkActivity.NORMAL));
		
		return toTransitionList;
	}

	@Override
	public void definitionValidate(ApplicationContext context) {
		
	}
	
}
