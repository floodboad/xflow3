package org.xsnake.cloud.xflow3.core.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xsnake.cloud.dao.DaoUtil;
import org.xsnake.cloud.xflow3.api.ITaskService;
import org.xsnake.cloud.xflow3.api.Participant;
import org.xsnake.cloud.xflow3.common.ProcessInstanceNextNumberService;
import org.xsnake.cloud.xflow3.core.register.ActivityRegister;
import org.xsnake.cloud.xflow3.core.register.ParticipantHandleRegister;

@Component
public class ApplicationContext extends Context{
	
	@Autowired
	Participant emptyParticipant;
	
	@Autowired
	private ActivityRegister activityRegister;
	
	@Autowired
	private ParticipantHandleRegister participantHandleRegister;
	
	@Autowired
	private DaoUtil daoUtil;
	
	@Autowired
	ProcessInstanceNextNumberService processInstanceNextNumberService;
	
	@Autowired
	ITaskService taskService;
	
	public ActivityRegister getActivityRegister() {
		return activityRegister;
	}

	public ParticipantHandleRegister getParticipantHandleRegister() {
		return participantHandleRegister;
	}

	public DaoUtil getDaoUtil() {
		return daoUtil;
	}

	public ProcessInstanceNextNumberService getNextNumberService() {
		return processInstanceNextNumberService;
	}

	public ITaskService getTaskService() {
		return taskService;
	}

	public Participant getEmptyParticipant() {
		return emptyParticipant;
	}
	
}
