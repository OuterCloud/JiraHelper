package com.quality.protocol.plugin.filter;

import com.quality.protocol.plugin.IPlugin;

public interface IFilterPlugin extends IPlugin {
	public String handleFilterPlugin(String request, String bodyContent);
}