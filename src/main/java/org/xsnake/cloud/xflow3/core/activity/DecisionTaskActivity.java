package org.xsnake.cloud.xflow3.core.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.xsnake.cloud.xflow3.api.exception.XflowBusinessException;
import org.xsnake.cloud.xflow3.api.exception.XflowDefinitionException;
import org.xsnake.cloud.xflow3.core.ParticipantActivity;
import org.xsnake.cloud.xflow3.core.Transition;
import org.xsnake.cloud.xflow3.core.context.ApplicationContext;
import org.xsnake.cloud.xflow3.core.context.OperateContext;

//可以有多个流转，人为选择走哪一个分支，把多个流转都存在任务信息中，前台读取
public class DecisionTaskActivity extends ParticipantActivity{

	private static final long serialVersionUID = 1L;

	public DecisionTaskActivity(ApplicationContext context , Element activityElement) {
		super(context,activityElement);
	}

	@Override
	protected List<Transition> doTask(OperateContext context) {
		String toId = context.getToTransitionId();
		if(StringUtils.isEmpty(toId)){
			throw new XflowBusinessException("人工决策节点请选择要流转的路径");
		}
		List<Transition> resultList = new ArrayList<Transition>();
		for(Transition transition : toTransitionList){
			if(toId.equals(transition.getId())){
				resultList.add(transition);
			}
		}
		if(resultList.size() == 0){
			throw new XflowBusinessException("异常：传入的流转不存在定义中");
		}
		return resultList;
	}
	
	@Override
	public void definitionValidate(ApplicationContext context) {
		if(toTransitionList.size() < 2){
			throw new XflowDefinitionException("人工决策任务需要包含多个出口路径");
		}
	}
}
