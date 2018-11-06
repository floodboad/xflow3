package org.xsnake.cloud.xflow3.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface ITaskService {
	
	
	/*
	 * 领取任务 
	 */
	@RequestMapping(value = "/task/receive", method = RequestMethod.POST)
	boolean receive(@RequestBody TaskForm taskForm);
	
	/*
	 * 获取多个参与者的任务列表 
	 */
	@RequestMapping(value = "/task/taskList", method = RequestMethod.POST)
	List<Task> taskList(@RequestBody ArrayList<Participant> participantList);
	
	/*
	 * 完成任务，如果整个流程结束返回true反之为false。区分虚拟任务与定义任务，如果是虚拟任务不会推动流程运行
	 * @param completeTaskForm
	 * @return 流程是否结束标志
	 */
	@RequestMapping(value = "/task/complete", method = RequestMethod.POST)
	boolean complete(@RequestBody CompleteTaskForm completeTaskForm);
	
	/*
	 * 获取指定任务
	 */
	@RequestMapping(value = "/task/get", method = RequestMethod.GET)
	Task get(String id);
	
	
	/*
	 * 转办，将任务交给另一个参与者 
	 */
	@RequestMapping(value = "/task/transfer", method = RequestMethod.POST)
	boolean transfer(@RequestBody TransferTaskForm transferTaskForm);
	
	/*
	 * 支持类型任务，不会影响流程的流转，当操作人发起支持操作时，原任务进入挂起等待状态，等待所有支持的虚拟任务完成后会唤起父任务
	 */
	@RequestMapping(value = "/task/support", method = RequestMethod.POST)
	boolean support(@RequestBody SupportTaskForm supportTaskForm);
	
	/*
	 * 驳回,支持两种驳回方式。1JUMP驳回后跳过中间环节直接回到驳回发起的节点，2FLOW驳回后按照流程定义重新流转
	 */
	@RequestMapping(value = "/task/reject", method = RequestMethod.POST)
	boolean reject(@RequestBody RejectTaskForm rejectTaskForm);
	
	public static class RejectPath {
		String recordId;
		String processInstanceId;
		String activityType;
		String activityName;
		String activityId;
		String createDate;
		String fromPath;
		String sn;
		String operatorId;
		String operatorName;
		String operatorType;
		String suggestion;
		String operateDate;
		String taskType;
		public String getRecordId() {
			return recordId;
		}
		public void setRecordId(String recordId) {
			this.recordId = recordId;
		}
		public String getProcessInstanceId() {
			return processInstanceId;
		}
		public void setProcessInstanceId(String processInstanceId) {
			this.processInstanceId = processInstanceId;
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
		public String getCreateDate() {
			return createDate;
		}
		public void setCreateDate(String createDate) {
			this.createDate = createDate;
		}
		public String getFromPath() {
			return fromPath;
		}
		public void setFromPath(String fromPath) {
			this.fromPath = fromPath;
		}
		public String getSn() {
			return sn;
		}
		public void setSn(String sn) {
			this.sn = sn;
		}
		public String getOperatorId() {
			return operatorId;
		}
		public void setOperatorId(String operatorId) {
			this.operatorId = operatorId;
		}
		public String getOperatorName() {
			return operatorName;
		}
		public void setOperatorName(String operatorName) {
			this.operatorName = operatorName;
		}
		public String getOperatorType() {
			return operatorType;
		}
		public void setOperatorType(String operatorType) {
			this.operatorType = operatorType;
		}
		public String getSuggestion() {
			return suggestion;
		}
		public void setSuggestion(String suggestion) {
			this.suggestion = suggestion;
		}
		public String getOperateDate() {
			return operateDate;
		}
		public void setOperateDate(String operateDate) {
			this.operateDate = operateDate;
		}
		public String getTaskType() {
			return taskType;
		}
		public void setTaskType(String taskType) {
			this.taskType = taskType;
		}
		
	}
	
	public static class RejectTaskForm extends CompleteTaskForm{

		public static final String REJECT_FLOW = "FLOW"; //重新逐层审批
		
		public static final String REJECT_JUMP = "JUMP"; //跳过其他直接回到驳回的节点
		
		String recordId;

		String rejectType;
		
		public String getRejectType() {
			return rejectType;
		}

		public void setRejectType(String rejectType) {
			this.rejectType = rejectType;
		}

		public String getRecordId() {
			return recordId;
		}

		public void setRecordId(String recordId) {
			this.recordId = recordId;
		}
	}
	
	public static class SupportTaskForm extends CompleteTaskForm{
		
		List<Participant> participantList;

		public List<Participant> getParticipantList() {
			return participantList;
		}

		public void setParticipantList(List<Participant> participantList) {
			this.participantList = participantList;
		}
		
	}
	
	public static class TransferTaskForm extends CompleteTaskForm{
		
		Participant assignee;

		public Participant getAssignee() {
			return assignee;
		}

		public void setAssignee(Participant assignee) {
			this.assignee = assignee;
		}
	}
	
	public static class TaskForm {
	
		String taskId;
		
		Participant operator;
		
		public String getTaskId() {
			return taskId;
		}

		public void setTaskId(String taskId) {
			this.taskId = taskId;
		}

		public Participant getOperator() {
			return operator;
		}

		public void setOperator(Participant operator) {
			this.operator = operator;
		}
	}
	
	public static class CompleteTaskForm extends TaskForm{
		
		String comment;

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}
	}
}
