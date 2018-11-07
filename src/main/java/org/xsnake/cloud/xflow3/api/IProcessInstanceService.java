package org.xsnake.cloud.xflow3.api;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xsnake.cloud.common.search.Page;

/**
 * 
 * @author Jerry.Zhao
 *
 */
public interface IProcessInstanceService {

	public static final String STATUS_RUNNING = "RUNNING";

	public static final String STATUS_END = "END";

	public static final String STATUS_CLOSE = "CLOSE";

	public static final String STATUS_WAITING = "WAITING";

	/*
	 * 开启流程实例。一个流程定义只能同时运行一个业务流程， 可以先通过缓存设置占位，然后再做业务流程开启，开启流程前都先去验证是否在缓存中是否有占位。
	 */
	@RequestMapping(value = "/process/start", method = RequestMethod.POST)
	ProcessInstance start(@RequestBody ApplyForm applyForm);

	/*
	 * 获取该业务的全部流程实例
	 */
	List<ProcessInstance> getAllByBusinessKey(String businessKey);

	/*
	 * 获取该业务对应的运行中的流程实例，如果没有则为null
	 */
	ProcessInstance getRunningByBusinessKey(String definitionCode, String businessKey);

	/*
	 * 获取流程实例
	 */
	ProcessInstance getProcessInstance(String processInstanceId);

	/*
	 * 关闭流程实例
	 */
	void close(String processInstanceId, Participant participant, String comment);

	/*
	 * 关闭流程实例
	 */
	void closeByBusinessKey(String definitionCode, String businessKey, Participant participant, String comment);

	/*
	 * 查询符合条件的流程实例
	 */
	Page<ProcessInstance> query(ProcessInstanceCondition processInstanceCondition);

	/*
	 * 参与过的流程
	 */
	Page<ProcessInstance> queryJoin(ProcessInstanceCondition processInstanceCondition);

	/*
	 * 列出流程实例的历史
	 */
	List<HistoryRecord> listHistory(String processInstanceId);

	public static class ApplyForm implements Serializable {

		private static final long serialVersionUID = 1L;
		
		String processCode;
		
		String businessKey;
		
		String businessType;
		
		String bussinessForm;
		
		Participant creator;

		public String getProcessCode() {
			return processCode;
		}

		public void setProcessCode(String processCode) {
			this.processCode = processCode;
		}
		
		public String getBusinessType() {
			return businessType;
		}

		public void setBusinessType(String businessType) {
			this.businessType = businessType;
		}

		public String getBusinessKey() {
			return businessKey;
		}

		public void setBusinessKey(String businessKey) {
			this.businessKey = businessKey;
		}

		public String getBussinessForm() {
			return bussinessForm;
		}

		public void setBussinessForm(String bussinessForm) {
			this.bussinessForm = bussinessForm;
		}

		public Participant getCreator() {
			return creator;
		}

		public void setCreator(Participant creator) {
			this.creator = creator;
		}
	}

}
