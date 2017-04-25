package com.quality.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.netease.cron.CronModel;
import com.quality.dao.CommonDaoImpl;
import com.quality.dao.CronDao;

@Repository
public class CronDaoImpl extends SqlSessionDaoSupport implements CronDao {
	
	@Resource(name = "commonDaoImpl")
	private CommonDaoImpl commonDaoImpl;
	
	@Override
	public void addCron(CronModel cron) {
		String id = commonDaoImpl.querySeqId("TK");
		cron.setId(id);
		
		this.getSqlSession().insert("addCron", cron);
	}
	
	@Override
	public void updateCron(CronModel cron) {

		this.getSqlSession().update("updateCron", cron);
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CronModel> getAllCron(String group) {

		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("systemCronId", Constant.SYSTEM_CRON_NAME);
		map.put("group", group);
		map.put("withOutAllGroup", false);
		List<CronModel> result = this.getSqlSession().selectList("getAllCron", map);
		return result;
	}
	
	@Override
	public CronModel selectCron(String id) {

		return (CronModel) this.getSqlSession().selectOne("selectCron", id);
	}
	
	@Override
	public void deleteCron(String id) {

		this.getSqlSession().delete("deleteCron", id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CronModel> getAllCronWithOutGroup(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("withOutAllGroup", true);
		List<CronModel> result = this.getSqlSession().selectList("getAllCron", map);
		return result;
	}
}
