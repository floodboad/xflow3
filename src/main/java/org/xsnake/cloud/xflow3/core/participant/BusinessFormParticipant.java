package org.xsnake.cloud.xflow3.core.participant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.xflow3.api.Participant;
import org.xsnake.cloud.xflow3.core.ParticipantHandle;
import org.xsnake.cloud.xflow3.core.context.ProcessInstanceContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class BusinessFormParticipant extends ParticipantHandle implements Serializable{

	private static final long serialVersionUID = 1L;

	private static final String PARTICIPANT_ID = "participantId";
	private static final String PARTICIPANT_NAME = "participantName";
	private static final String PARTICIPANT_TYPE = "participantType";
	
	public BusinessFormParticipant(Element participantElement) {
		super(participantElement);
	}

	@Override
	protected List<Participant> findParticipantList(ProcessInstanceContext context) {
		String participantId = attributes.get(PARTICIPANT_ID);
		String participantName = attributes.get(PARTICIPANT_NAME);
		String participantType = attributes.get(PARTICIPANT_TYPE);
		
		String businessForm = context.getProcessInstance().getBusinessForm();
		JSONObject businessFormJSON = JSON.parseObject(businessForm);
		
		Participant participant = new Participant ((String)businessFormJSON.get(participantId) ,(String)businessFormJSON.get(participantName) ,(String)businessFormJSON.get(participantType));
		List<Participant> participantList = new ArrayList<Participant>();
		participantList.add(participant);
		return participantList;
	}

}
