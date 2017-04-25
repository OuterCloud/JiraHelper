package com.quality.dao.pagination;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @param <T>
 */
public class Page<T> implements Serializable {
	
	private static final long serialVersionUID = 6554969807626701309L;
	
	public static final int DEFAULT_PAGESIZE = 10;
	
	protected List<T> result = new ArrayList<T>();
	
	protected long totalCount = 0;
	
	protected int pageNo = 0;
	
	protected int totalPage = 0;
	
	protected int pageSize;
	
	public Page() {

		super();
		this.pageSize = Page.DEFAULT_PAGESIZE;
	}
	
	public Page(int pageSize) {

		this.pageSize = pageSize;
	}
	
	public Page(int pageNo, int pageSize) {

		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}
	
	public int getPageSize() {

		return this.pageSize;
	}
	
	public List<T> getResult() {

		return this.result;
	}
	
	public void setResult(List<T> result) {

		this.result = result;
	}
	
	public long getTotalCount() {

		return this.totalCount;
	}
	
	public int getPageNo() {

		if (this.pageNo < 0) {
			this.pageNo = 0;
		}
		return this.pageNo;
	}
	
	public void setPageNo(int pageNo) {

		this.pageNo = pageNo;
	}
	
	public int getTotalPage() {

		return this.totalPage;
	}
	
	public void setTotalPage(int totalPage) {

		this.totalPage = totalPage;
	}
	
	public void setTotalCount(long totalCount) {

		this.totalCount = totalCount;
	}
	
	public void setPageSize(int pageSize) {

		this.pageSize = pageSize;
	}
	
}
