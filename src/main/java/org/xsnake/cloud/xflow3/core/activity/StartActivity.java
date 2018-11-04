package org.xsnake.cloud.xflow3.core.activity;

import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.xflow3.api.exception.XflowDefinitionException;
import org.xsnake.cloud.xflow3.core.AutomaticActivity;
import org.xsnake.cloud.xflow3.core.Transition;
import org.xsnake.cloud.xflow3.core.context.ApplicationContext;
import org.xsnake.cloud.xflow3.core.context.ProcessInstanceContext;

public final class StartActivity extends AutomaticActivity {

	public StartActivity(ApplicationContext context , Element activityElement) {
		super(context,activityElement);
	}

	private static final long serialVersionUID = 1L;

	public List<Transition> doWork(ProcessInstanceContext context){
		return toTransitionList;
	}

	@Override
	public void definitionValidate(ApplicationContext context) {
		if(toTransitionList.size() != 1){
			throw new XflowDefinitionException("开始活动有且只能有一个出口路径");
		}
	}
}
