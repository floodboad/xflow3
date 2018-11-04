package org.xsnake.cloud.xflow3.core.activity;

import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.dao.DaoUtil;
import org.xsnake.cloud.xflow3.api.exception.XflowDefinitionException;
import org.xsnake.cloud.xflow3.core.AutomaticActivity;
import org.xsnake.cloud.xflow3.core.Transition;
import org.xsnake.cloud.xflow3.core.context.ApplicationContext;
import org.xsnake.cloud.xflow3.core.context.ProcessInstanceContext;

public class ForkActivity extends AutomaticActivity {

	public final static String FORK = "1";
	
	public final static String NORMAL = "0";
	
	public ForkActivity(ApplicationContext context , Element activityElement) {
		super(context,activityElement);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public List<Transition> doWork(ProcessInstanceContext context){
		if(FORK.equals(context.getProcessInstance().getForkStatus())){
			throw new XflowDefinitionException("流程异常：已经在分支状态下，无法再分支");
		}
		DaoUtil daoUtil = context.getApplicationContext().getDaoUtil();
		daoUtil.$update("PROCESS_INSTANCE_FORK_STATUS.sql", daoUtil.createMap()
				.put("PROCESS_INSTANCE_ID", context.getProcessInstance().getProcessInstanceId())
				.put("FORK_STATUS", FORK));
		return toTransitionList;
	}

	@Override
	public void definitionValidate(ApplicationContext context) {
		if(toTransitionList.size() < 2){
			throw new XflowDefinitionException("分支节点需要包含多个出口路径");
		}
	}
}
