package com.quality.dao;

import java.util.List;

import com.netease.cron.CronModel;

public interface CronDao {
	
	public List<CronModel> getAllCron(String group);
	
	public void addCron(CronModel cron);
	
	public void updateCron(CronModel cron);
	
	public CronModel selectCron(String id);
	
	public void deleteCron(String id);
	
	public List<CronModel> getAllCronWithOutGroup();
}
