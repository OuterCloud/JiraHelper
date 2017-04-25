package com.quality.dao.pagination;

import java.util.ArrayList;


/**
 * 
 * <p>Title: 带分页信息的List实现类，作为分页结果返回</p> 
 * 
 * <p>Copyright: Copyright (c) 2011</p> 
 * 
 * <p>Company: www.netease.com</p>
 * 
 * @author 	Barney Woo
 * @date 	2011-11-22
 * @version 1.0
 */

public class PaginationList<T> extends ArrayList<T>
{
	private static final long serialVersionUID = -6059628280162549106L;

	private PaginationInfo paginationInfo = null;

	public PaginationInfo getPaginationInfo()
	{
		return paginationInfo;
	}
	public void setPaginationInfo(PaginationInfo paginationInfo)
	{
		this.paginationInfo = paginationInfo;
	}

	public Integer getCurrentPage()
	{
		return this.paginationInfo.getCurrentPage();
	}
	public Integer getRecordPerPage()
	{
		return this.paginationInfo.getRecordPerPage();
	}
	public Integer getTotalPage()
	{
		return this.paginationInfo.getTotalPage();
	}
	public Integer getTotalRecord()
	{
		return this.paginationInfo.getTotalRecord();
	}
}
