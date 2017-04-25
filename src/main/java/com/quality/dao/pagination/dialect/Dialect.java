package com.quality.dao.pagination.dialect;

/**
 * dialect接口，提供生成分页和总和的sql
 * 
 * @author elton_liu
 */
public interface Dialect {
	
	/**
	 * 是否支持物理分页
	 * 
	 * @return
	 */
	public boolean supportsLimit();
	
	/**
	 * 是否支持物理分页偏移量
	 * 
	 * @return
	 */
	public boolean supportsLimitOffset();
	
	/**
	 * 分页查询
	 * 
	 * @param sql
	 * @param hasOffset
	 * @return
	 */
	public String getLimitString(String sql, boolean hasOffset);
	
	/**
	 * 分页查询
	 * 
	 * @param sql
	 * @param offset
	 * @param limit
	 * @return
	 */
	public String getLimitString(String sql, int offset, int limit);
	
	/**
	 * 生成查询总和的sql
	 * 
	 * @param sql
	 * @return
	 */
	public String getCountString(String sql);
	
}
