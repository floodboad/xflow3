package org.xsnake.cloud.xflow3.api;

import java.util.List;

import org.xsnake.cloud.common.search.BaseCondition;

public class TaskCondition extends BaseCondition{

	private static final long serialVersionUID = 1L;
	
	List<Participant> participantList;

	public List<Participant> getParticipantList() {
		return participantList;
	}

	public void setParticipantList(List<Participant> participantList) {
		this.participantList = participantList;
	}
	
}
