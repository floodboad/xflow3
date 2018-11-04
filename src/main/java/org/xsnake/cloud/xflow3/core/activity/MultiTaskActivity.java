package org.xsnake.cloud.xflow3.core.activity;

import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.dao.DaoUtil;
import org.xsnake.cloud.xflow3.api.Task;
import org.xsnake.cloud.xflow3.core.ParticipantActivity;
import org.xsnake.cloud.xflow3.core.Transition;
import org.xsnake.cloud.xflow3.core.Waitable;
import org.xsnake.cloud.xflow3.core.context.ApplicationContext;
import org.xsnake.cloud.xflow3.core.context.OperateContext;
/**
 * 2018/1/15
 * 多人任务/会签任务
 * @author Jerry.Zhao
 *
 */
public class MultiTaskActivity extends ParticipantActivity implements Waitable {

	public MultiTaskActivity(ApplicationContext context , Element activityElement) {
		super(context,activityElement);
	}

	private static final long serialVersionUID = 1L;
	
	
	@Override
	public List<Transition> doTask(OperateContext context) {
		DaoUtil daoUtil = context.getApplicationContext().getDaoUtil();
		List<Task> taskList = daoUtil.$queryList("PROCESS_INSTANCE_TASK_BY_INSTANCE_RECORD.sql", context.getTask(),Task.class);
		if(taskList.size() > 0){
			return null;
		}
		return toTransitionList;
	}
	
}
