package org.xsnake.cloud.xflow3.core.activity;

import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.xflow3.core.ParticipantActivity;
import org.xsnake.cloud.xflow3.core.Transition;
import org.xsnake.cloud.xflow3.core.context.ApplicationContext;
import org.xsnake.cloud.xflow3.core.context.OperateContext;

/**
 * 2018/1/15
 * 普通任务节点
 * @author Jerry.Zhao
 *
 */
public class TaskActivity extends ParticipantActivity{
	
	public TaskActivity(ApplicationContext context , Element activityElement) {
		super(context,activityElement);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public List<Transition> doTask(OperateContext context) {
		return toTransitionList;
	}

}
