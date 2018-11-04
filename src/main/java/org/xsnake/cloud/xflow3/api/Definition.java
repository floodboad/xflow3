package org.xsnake.cloud.xflow3.api;

import java.util.Date;

public class Definition {
	
	private String code;
	
	private Long currentVersion;
	
	private String name;
	
	private String remark;
	
	private String status;

	private String versionCount;
	
	private String businessCount;
	
	private Date createTime;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(Long currentVersion) {
		this.currentVersion = currentVersion;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVersionCount() {
		return versionCount;
	}

	public void setVersionCount(String versionCount) {
		this.versionCount = versionCount;
	}

	public String getBusinessCount() {
		return businessCount;
	}

	public void setBusinessCount(String businessCount) {
		this.businessCount = businessCount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
