package org.xsnake.cloud.xflow3.api;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class ProcessInstance implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String processInstanceId;
	private String processCode;
	private String version;
	private String businessKey;
	private String name;
	private String status;
	private String parentId;
	private Date startDate;
	private String creatorId;
	private String creatorName;
	private String creatorType;
	private String parentActivityId;
	private String businessForm;
	private String forkStatus;
	
	
	public String getForkStatus() {
		return forkStatus;
	}

	public void setForkStatus(String forkStatus) {
		this.forkStatus = forkStatus;
	}

	public String getBusinessForm() {
		return businessForm;
	}

	public void setBusinessForm(String businessForm) {
		this.businessForm = businessForm;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessCode() {
		return processCode;
	}

	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCreatorType() {
		return creatorType;
	}

	public void setCreatorType(String creatorType) {
		this.creatorType = creatorType;
	}

	public String getParentActivityId() {
		return parentActivityId;
	}

	public void setParentActivityId(String parentActivityId) {
		this.parentActivityId = parentActivityId;
	}

	public boolean isEnd(){
		return IProcessInstanceService.STATUS_CLOSE.equals(status);
	}
	
	public boolean isSub(){
		return !StringUtils.isEmpty(parentId);
	}
	
}
