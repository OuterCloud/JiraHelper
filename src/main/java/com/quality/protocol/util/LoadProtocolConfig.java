package com.quality.protocol.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.quality.constant.LogConstant;

/**
 * 配置文件中的数据
 * 
 * @author li_zhe
 * 
 */

public class LoadProtocolConfig {
	static Properties config = null;
	static Properties promoter_config = null;
	static Map<String, Properties> mapProperties = new HashMap<String, Properties>();

	@PostConstruct
	public void autoLoadConfig() {
		if (null == mapProperties) {
			LogConstant.debugLog.info("加载properties文件时，创建Map出错！");
			return;
		}
		Set<String> keySet = mapProperties.keySet();
		Iterator<String> itr = keySet.iterator();
		while (itr.hasNext()) {
			String fileName = itr.next();
			try {
				getConfig(fileName, "test");
				LogConstant.runLog.info("加载properties配置文件成功！");
			} catch (IOException e) {
				LogConstant.debugLog.info("加载properties文件时异常：", e);
			}
		}
	}

	public static String getProtocolConfig(String propertyName) throws IOException {
		return getConfig("protocol.properties", propertyName);
	}

	public static String getConfig(String fileName, String propertyName)
			throws IOException {
		Properties config = mapProperties.get(fileName);
		if (null == config || config.isEmpty()) {
			InputStream in = null;

			in = LoadProtocolConfig.class.getResourceAsStream("/"+fileName);
			config = new Properties();

			config.load(in);
			if (null != in) {
				in.close();
			}
			mapProperties.put(fileName, config);
			LogConstant.runLog.info("config path:" + LoadProtocolConfig.class.getResource("/"+fileName) + " load success!");
		}

		String property = config.getProperty(propertyName);
		if (null == property && !"test".equals(propertyName)) {
			LogConstant.runLog.info("config path:" + LoadProtocolConfig.class.getResource("/"+fileName)+ "|"+propertyName + " load failed!");
			return null;
		}

		return property;
	}

	public Map<String, Properties> getMapProperties() {
		return mapProperties;
	}

	@SuppressWarnings("static-access")
	public void setMapProperties(Map<String, Properties> mapProperties) {
		this.mapProperties = mapProperties;
	}
}