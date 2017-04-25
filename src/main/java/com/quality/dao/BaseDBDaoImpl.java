package com.quality.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository("baseDBDao")
public class BaseDBDaoImpl extends SqlSessionDaoSupport{
	
	
	@Autowired
	public void setMySqlSessionFactory(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory){
		this.setSqlSessionFactory(sqlSessionFactory);
	}
	
	public int delete(String sqlId,Object obj){
		return getSqlSession().delete(sqlId,obj);
	}
	

	public int insertOrUpdate(String sqlId, Object obj){
		return this.getSqlSession().insert(sqlId,obj);
	}
	/**
	 * 
	 * @return
	 */
	public List<?> select(){
		//this.getSqlSession().selectOne(statement, parameter)
		return this.getSqlSession().selectList("", "");
	}
}
