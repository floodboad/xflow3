package org.xsnake.cloud.xflow3.api;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.xsnake.cloud.xflow3.api.ITaskService.RejectPath;

public class Task implements Serializable {

	private static final long serialVersionUID = 1L;

	String taskId;
	String participantId;
	String participantName;
	String participantType;
	Date taskDate;
	String taskParentId;
	String status;
	String recordId;
	String activityType;
	String activityName;
	String activityId;
	Date startDate;
	Date endDate;
	String fromPath;
	Long sn;
	String processInstanceId;
	String processCode;
	String version;
	String businessKey;
	String processInstanceName;
	String processInstanceStatus;
	String parentId;
	Date processInstanceStartDate;
	String creatorId;
	String creatorName;
	String creatorType;
	String parentActivityId;
	String taskType;
	
	List<RejectPath> rejectPathList;
	
	Map<String,String> activityAttributes;
	
	public Map<String, String> getActivityAttributes() {
		return activityAttributes;
	}

	public void setActivityAttributes(Map<String, String> activityAttributes) {
		this.activityAttributes = activityAttributes;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	public String getParticipantType() {
		return participantType;
	}

	public void setParticipantType(String participantType) {
		this.participantType = participantType;
	}

	public Date getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}

	public String getTaskParentId() {
		return taskParentId;
	}

	public void setTaskParentId(String taskParentId) {
		this.taskParentId = taskParentId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getFromPath() {
		return fromPath;
	}

	public void setFromPath(String fromPath) {
		this.fromPath = fromPath;
	}

	public Long getSn() {
		return sn;
	}

	public void setSn(Long sn) {
		this.sn = sn;
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

	public String getProcessInstanceName() {
		return processInstanceName;
	}

	public void setProcessInstanceName(String processInstanceName) {
		this.processInstanceName = processInstanceName;
	}

	public String getProcessInstanceStatus() {
		return processInstanceStatus;
	}

	public void setProcessInstanceStatus(String processInstanceStatus) {
		this.processInstanceStatus = processInstanceStatus;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Date getProcessInstanceStartDate() {
		return processInstanceStartDate;
	}

	public void setProcessInstanceStartDate(Date processInstanceStartDate) {
		this.processInstanceStartDate = processInstanceStartDate;
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

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public List<RejectPath> getRejectPathList() {
		return rejectPathList;
	}

	public void setRejectPathList(List<RejectPath> rejectPathList) {
		this.rejectPathList = rejectPathList;
	}
	
}
