package com.quality.dao;

import java.text.DecimalFormat;
import java.util.Date;

import org.springframework.stereotype.Repository;

import com.netease.common.util.DateUtil;
import com.netease.common.util.RandomUtil;

@Repository("commonDaoImpl")
public class CommonDaoImpl extends BaseDBDaoImpl {
	
	public long getSeqIdFromDB() {
		this.getSqlSession().clearCache();
		return (Long) this.getSqlSession().selectOne("getSeq");
	}
	
	public String querySeqId(String prefix) {

		if (prefix == null) {
			prefix = "";
		}
		long seqId = getSeqIdFromDB();
		String timePrefix = DateUtil.format(new Date(), "yyyyMMddHH");
		return timePrefix + prefix + formatSequence(seqId) + RandomUtil.generateNumberString(3);
	}
	
	public String generateFormatedIdFromSeq() {

		long seqId = getSeqIdFromDB();
		return formatSequence(seqId);
		
	}
	
	private String formatSequence(long numberToFormat) {

		DecimalFormat format = new DecimalFormat("00000");
		return format.format(numberToFormat % 100000L);
	}

	public String querySeqIdNoRadom(String prefix) {
	

		if (prefix == null) {
			prefix = "";
		}
		long seqId = getSeqIdFromDB();
		String timePrefix = DateUtil.format(new Date(), "yyyyMMddHH");
		return timePrefix + prefix + formatSequence(seqId);
	}
	
}
