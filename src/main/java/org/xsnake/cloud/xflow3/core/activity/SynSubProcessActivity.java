package org.xsnake.cloud.xflow3.core.activity;

import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.xflow3.api.ProcessInstance;
import org.xsnake.cloud.xflow3.core.Transition;
import org.xsnake.cloud.xflow3.core.Waitable;
import org.xsnake.cloud.xflow3.core.context.ApplicationContext;
import org.xsnake.cloud.xflow3.core.context.ProcessInstanceContext;

public class SynSubProcessActivity extends SubProcessActivity implements Waitable {

	private static final long serialVersionUID = 1L;
	
	public SynSubProcessActivity(ApplicationContext context , Element activityElement) {
		super(context,activityElement);
	}

	@Override
	public List<Transition> doWork(List<ProcessInstance> subProcessInstanceList , ProcessInstanceContext context) {
		//所有的子流程都结束了则开启主流程
		for(ProcessInstance processInstance :subProcessInstanceList){
			if(!processInstance.isEnd()){
				return null;
			}
		}
		return toTransitionList;
	}

}
