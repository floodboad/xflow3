package org.xsnake.cloud.xflow3.core.context;

import java.util.List;

import org.xsnake.cloud.xflow3.api.Participant;
import org.xsnake.cloud.xflow3.api.Task;

public class OperateContext extends TaskContext{

	Participant operator;
	
	String suggestion;
	
	List<Participant> participantList;
	
	OperateType operateType;
	
	String toTransitionId; 
	
	public static enum OperateType {
		
		start("START"),complete("COMPLETE"),reject("REJECT"),assign("ASSIGN"),support("SUPPORT"),transfer("TRANSFER");
		
		private String type;
		
		OperateType(String type){
			this.type = type;
		}
		
		@Override
		public String toString() {
			return type;
		}
	}
	
	public OperateContext(ApplicationContext applicationContext,Task task,OperateType operateType,Participant operator,String suggestion) {
		super(applicationContext,task);
		this.operateType = operateType;
		this.operator = operator;
		this.suggestion = suggestion;
	}

	public String getToTransitionId() {
		return toTransitionId;
	}

	public void setToTransitionId(String toTransitionId) {
		this.toTransitionId = toTransitionId;
	}

	public Participant getOperator() {
		return operator;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public OperateType getOperateType() {
		return operateType;
	}
	
}
