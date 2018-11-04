package org.xsnake.cloud.xflow3.core;

import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.xflow3.api.exception.XflowBusinessException;
import org.xsnake.cloud.xflow3.core.context.ApplicationContext;
import org.xsnake.cloud.xflow3.core.context.OperateContext;
import org.xsnake.cloud.xflow3.core.context.ProcessInstanceContext;

public abstract class AutomaticActivity extends Activity{

	public AutomaticActivity(ApplicationContext context , Element activityElement) {
		super(context,activityElement);
	}

	private static final long serialVersionUID = 1L;
	
	protected abstract List<Transition> doWork(ProcessInstanceContext context);
	
	protected List<Transition> doParticipantTask(OperateContext context){
		throw new XflowBusinessException("程序内部错误");
	}
	
	protected List<Transition> doAutomaticWork(ProcessInstanceContext context){
		 List<Transition> transitionList = doWork(context);
		return transitionList;
	}
	
	@Override
	public void definitionValidate(ApplicationContext context) {
		
	}
}
