package org.xsnake.cloud.xflow3.core.activity;

import java.util.Date;
import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.dao.DaoUtil;
import org.xsnake.cloud.xflow3.api.exception.XflowDefinitionException;
import org.xsnake.cloud.xflow3.core.AutomaticActivity;
import org.xsnake.cloud.xflow3.core.Transition;
import org.xsnake.cloud.xflow3.core.context.ApplicationContext;
import org.xsnake.cloud.xflow3.core.context.ProcessInstanceContext;

/**
 * 结束环节
 * 结束环节为流程最后一个环节，结束环节可以设置回调
 * @author Jerry.Zhao
 *
 */
public final class EndActivity extends AutomaticActivity {

//	List<Callback> callbackList;
	
	public EndActivity(ApplicationContext context , Element activityElement) {
		super(context,activityElement);
	}

	
	
	private static final long serialVersionUID = 1L;

	public List<Transition> doWork(ProcessInstanceContext context){
		DaoUtil daoUtil = context.getApplicationContext().getDaoUtil();
		
		daoUtil.$update("PROCESS_INSTANCE_STATUS.sql",
				daoUtil.createMap("PROCESS_INSTANCE_ID", context.getProcessInstance().getProcessInstanceId()).put("STATUS", "END"));
		
		//添加流程结束后回调事件标示
		daoUtil.$update("PROCESS_INSTANCE_CALLBACK_ADD.sql",daoUtil.createMap()
			.put("PROCESS_INSTANCE_ID", context.getProcessInstance().getProcessInstanceId())
			.put("BUSINESS_KEY", context.getProcessInstance().getBusinessKey())
			.put("CREATE_DATE", new Date())
			.put("CALLBACK_TYPE", "PROCESS_INSTANCE_END")
			.put("STATUS", "0")
			.put("ACTIVITY_ID", id)
			.put("ACTIVITY_NAME", name)
			.put("ACTIVITY_TYPE", type)
		);
		
		//清除流程的所有错误
		
		return null;
	}
	
	@Override
	public void definitionValidate(ApplicationContext context) {
		if(toTransitionList !=null && toTransitionList.size() > 0){
			throw new XflowDefinitionException("结束节点不能包含任何流转路径");
		}
	}
	
}
