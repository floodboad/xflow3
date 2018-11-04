package org.xsnake.cloud.common.search;

import java.io.Serializable;

/**
 * 
 * @author Jerry.Zhao
 * 查询的基础类,继承自BaseForm,一般分页查询条件，已经模糊查询搜索
 */
public class BaseCondition implements Serializable{

	private static final long serialVersionUID = 1L;
	
	protected String searchKey;
	
	protected String orderBy;

	protected int page = 1;
	
	private int rows = 20;
	
	public String getOrderBy() {
		return orderBy;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public int getPage() {
		return page;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	
}
