package org.xsnake.cloud.xflow3.core.participant;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.dao.DaoUtil;
import org.xsnake.cloud.xflow3.api.Participant;
import org.xsnake.cloud.xflow3.api.exception.XflowBusinessException;
import org.xsnake.cloud.xflow3.core.ParticipantHandle;
import org.xsnake.cloud.xflow3.core.context.ProcessInstanceContext;

public class SQLParticipant extends ParticipantHandle implements Serializable{

	private static final long serialVersionUID = 1L;

	String sql;
	
	public SQLParticipant(Element participantElement) {
		super(participantElement);
		sql = participantElement.elementText("participant");
	}
	
	@Override
	protected List<Participant> findParticipantList(ProcessInstanceContext context) {
		DaoUtil daoUtil = context.getApplicationContext().getDaoUtil();
		String finalSql;
		List<Participant> participantList;
		try{
			finalSql = daoUtil.processTemplate(sql, context.getProcessInstance());
			participantList = daoUtil.queryList(finalSql, 
				context.getProcessInstance(),Participant.class);
		}catch (Exception e) {
			daoUtil.$update("PROCESS_INSTANCE_ERROR.sql", daoUtil.createMap()
					.put("PROCESS_INSTANCE_ID", context.getProcessInstance().getProcessInstanceId())
					.put("EXCEPTION_TYPE", "SQLParticipant")
					.put("MESSAGE", e.getMessage())
					.put("CREATE_DATE", new Date())
					.put("ACTIVITY_ID", activity.getId())
					.put("ACTIVITY_NAME", activity.getName())
					.put("ACTIVITY_TYPE", activity.getType())
			);
			throw new XflowBusinessException(e.getMessage());
		}
		return participantList;
	}


}
