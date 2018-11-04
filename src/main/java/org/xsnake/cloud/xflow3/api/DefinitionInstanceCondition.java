package org.xsnake.cloud.xflow3.api;

import org.xsnake.cloud.common.search.BaseCondition;

public class DefinitionInstanceCondition extends BaseCondition {

	private static final long serialVersionUID = 1L;
	
	private String status;

	private String code;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
