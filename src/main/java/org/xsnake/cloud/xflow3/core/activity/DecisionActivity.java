package org.xsnake.cloud.xflow3.core.activity;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.xsnake.cloud.dao.DaoUtil;
import org.xsnake.cloud.xflow3.api.exception.XflowDefinitionException;
import org.xsnake.cloud.xflow3.core.AutomaticActivity;
import org.xsnake.cloud.xflow3.core.Transition;
import org.xsnake.cloud.xflow3.core.context.ApplicationContext;
import org.xsnake.cloud.xflow3.core.context.ProcessInstanceContext;

import com.alibaba.fastjson.JSONObject;
/**
 * 2018/1/15
 * 决策任务
 * @author Jerry.Zhao
 *
 */
public class DecisionActivity extends AutomaticActivity {

	private static final long serialVersionUID = 1L;

	String expression;
	
	static ScriptEngineManager sfm = new ScriptEngineManager(); 
	
	static ScriptEngine jsEngine = sfm.getEngineByName("JavaScript"); 
	
	public DecisionActivity(ApplicationContext context , Element activityElement) {
		super(context,activityElement);
		expression = activityElement.elementText("expression");
		if(StringUtils.isEmpty(expression)){
			throw new XflowDefinitionException("判断节点定义必须包含表达式");
		}
		if(jsEngine == null){
			throw new RuntimeException("找不到 JavaScript 引擎。");
		}
	}


	@Override
	public List<Transition> doWork(ProcessInstanceContext context) {
		DaoUtil daoUtil = context.getApplicationContext().getDaoUtil();
		String newExpression = null;
		try {
			newExpression = daoUtil.processTemplate(expression, JSONObject.parseObject(context.getProcessInstance().getBusinessForm()));
			String to = String.valueOf(jsEngine.eval("function test(businessForm) {" + newExpression + "} test("+context.getProcessInstance().getBusinessForm()+")"));
			if (StringUtils.isEmpty(to)) {
				throw new XflowDefinitionException("表达式错误，没有返回一个正确的流转名称");
			}
			List<Transition> resultList = new ArrayList<Transition>();
			for(Transition toTransition : toTransitionList){
				if(to.equals(toTransition.getId()) || to.equals(toTransition.getName())){
					resultList.add(toTransition);
				}
			}
			if(resultList.size() == 0){
				throw new XflowDefinitionException("表达式所返回了未知的流转");
			}
			return resultList;
		} catch (Exception e) {
			throw new XflowDefinitionException("表达式解析异常：" + e.getMessage());
		}
	}
	
}
