package com.quality.protocol.plugin;

import java.util.ArrayList;
import java.util.List;

public class PluginContentItem {

	private List<IPlugin> pluginList;
	private List<String> contentList;

	public PluginContentItem() {
		super();
		this.pluginList = new ArrayList<IPlugin>();
		this.contentList = new ArrayList<String>();
	}

	public List<IPlugin> getPluginList() {
		return pluginList;
	}

	public void setPluginList(List<IPlugin> pluginList) {
		this.pluginList = pluginList;
	}

	public List<String> getContentList() {
		return contentList;
	}

	public void setContentList(List<String> contentList) {
		this.contentList = contentList;
	}

	public void addContent(String content) {
		contentList.add(content);
	}

	public void addPlugin(IPlugin plugin) {
		pluginList.add(plugin);
	}

}