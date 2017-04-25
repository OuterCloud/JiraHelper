package com.quality.dao.pagination;

/**
 * 
 * <p>Title: 分页信息</p> 
 * 
 * <p>Copyright: Copyright (c) 2011</p> 
 * 
 * <p>Company: www.netease.com</p>
 * 
 * @author 	Barney Woo
 * @date 	2011-11-22
 * @version 1.0
 */

public class PaginationInfo
{
	//default value
	private static final Integer defaultCurrentPage = 1;
	private static final Integer defaultRecordPerPage = 20;
	
	/**
	 * 当前页，1基址
	 */
	private Integer currentPage = null;
	/**
	 * 每页记录数
	 */
	private Integer recordPerPage = null;
	/**
	 * 总页数
	 */
	private Integer totalPage = null;
	/**
	 * 总记录数
	 */
	private Integer totalRecord = null;
	
	private void initialize()
	{
		this.currentPage = defaultCurrentPage;
		this.recordPerPage = defaultRecordPerPage;
	}
	
	public PaginationInfo()
	{
		this.initialize();
	}
	
	public PaginationInfo(Integer currentPage, Integer recordPerPage)
	{
		this.setCurrentPage(currentPage);
		this.setRecordPerPage(recordPerPage);
	}
	
	public int getOffset()
	{
		return this.getRecordPerPage() * (this.getCurrentPage() - 1);
	}
	
	public int getLimit()
	{
		return this.getRecordPerPage();
	}
	
	public static PaginationInfo getDefault()
	{
		return new PaginationInfo(defaultCurrentPage, defaultRecordPerPage);
	}
	
	public Integer getCurrentPage()
	{
		return currentPage == null ? defaultCurrentPage : currentPage;
	}
	public void setCurrentPage(Integer currentPage)
	{
		this.currentPage = (currentPage == null || currentPage <= 0) ? defaultCurrentPage : currentPage;
	}
	public Integer getRecordPerPage()
	{
		return recordPerPage == null ? defaultRecordPerPage : recordPerPage;
	}
	public void setRecordPerPage(Integer recordPerPage)
	{
		this.recordPerPage = (recordPerPage == null || recordPerPage <= 0) ? defaultRecordPerPage : recordPerPage;
	}
	public Integer getTotalPage()
	{
		return totalPage;
	}
	public void setTotalPage(Integer totalPage)
	{
		this.totalPage = totalPage;
	}
	public Integer getTotalRecord()
	{
		return totalRecord;
	}
	public void setTotalRecord(Integer totalRecord)
	{
		this.totalRecord = totalRecord;
		if ((this.getCurrentPage() - 1) * this.getRecordPerPage() >= this.totalRecord)
		{
			this.initialize();
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[currentPage=");
		builder.append(currentPage);
		builder.append(", recordPerPage=");
		builder.append(recordPerPage);
		builder.append(", totalPage=");
		builder.append(totalPage);
		builder.append(", totalRecord=");
		builder.append(totalRecord);
		builder.append("]");
		return builder.toString();
	}
}
