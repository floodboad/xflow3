package org.xsnake.cloud.xflow3.core.participant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.xflow3.api.Participant;
import org.xsnake.cloud.xflow3.core.ParticipantHandle;
import org.xsnake.cloud.xflow3.core.context.ProcessInstanceContext;

public class CreatorParticipant extends ParticipantHandle implements Serializable{

	private static final long serialVersionUID = 1L;

	public CreatorParticipant(Element participantElement) {
		super(participantElement);
	}

	@Override
	protected List<Participant> findParticipantList(ProcessInstanceContext context) {
		List<Participant> list = new ArrayList<Participant>();
		list.add(new Participant(context.getProcessInstance().getCreatorId(),context.getProcessInstance().getCreatorName(),context.getProcessInstance().getCreatorType()));
		return list;
	}


}
