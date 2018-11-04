package org.xsnake.cloud.xflow3.core.activity;

import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.xflow3.api.ProcessInstance;
import org.xsnake.cloud.xflow3.core.Transition;
import org.xsnake.cloud.xflow3.core.context.ApplicationContext;
import org.xsnake.cloud.xflow3.core.context.ProcessInstanceContext;

/**
 * 2018/1/15
 * 异步子流程
 * TODO 等待实现
 * @author Jerry.Zhao
 *
 */
public class AsynSubProcessActivity extends SubProcessActivity {

	private static final long serialVersionUID = 1L;
	
	public AsynSubProcessActivity(ApplicationContext context , Element activityElement) {
		super(context,activityElement);
	}

	@Override
	public List<Transition> doWork(List<ProcessInstance> subProcessInstanceList, ProcessInstanceContext context) {
		return toTransitionList;
	}
	
}
