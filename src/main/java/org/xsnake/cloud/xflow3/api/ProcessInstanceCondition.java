package org.xsnake.cloud.xflow3.api;

import org.xsnake.cloud.common.search.BaseCondition;
/**
 * 
 * @author Jerry.Zhao
 *
 */
public class ProcessInstanceCondition extends BaseCondition{

	private static final long serialVersionUID = 1L;
	
	String creatorId;
	
	String creatorType;
	
	String operatorId;
	
	String operatorType;
	
	String status;

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorType() {
		return creatorType;
	}

	public void setCreatorType(String creatorType) {
		this.creatorType = creatorType;
	}
	
}
