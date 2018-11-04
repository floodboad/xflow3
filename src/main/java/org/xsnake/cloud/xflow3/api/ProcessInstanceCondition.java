package org.xsnake.cloud.xflow3.api;

import java.util.Date;

import org.xsnake.cloud.common.search.BaseCondition;
/**
 * 
 * @author Jerry.Zhao
 *
 */
public class ProcessInstanceCondition extends BaseCondition{

	private static final long serialVersionUID = 1L;

	String id;
	
	String definitionCode;
	
	int definitionVersion;
	
	String status;
	
	String parentId;
	
	Date startTime;
	
	String creatorId;
	
	Date endTime;
	
	String participantId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDefinitionCode() {
		return definitionCode;
	}

	public void setDefinitionCode(String definitionCode) {
		this.definitionCode = definitionCode;
	}

	public int getDefinitionVersion() {
		return definitionVersion;
	}

	public void setDefinitionVersion(int definitionVersion) {
		this.definitionVersion = definitionVersion;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
}
