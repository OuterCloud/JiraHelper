package com.quality.protocol.plugin;

import java.util.HashMap;
import java.util.Map;

import com.quality.protocol.plugin.filter.IFilterPlugin;
import com.quality.protocol.plugin.filter.IFormateContentFilterPlugin;

public class PluginFactory {

	public static final String PLUGIN_START_PLACEHOLDER = "<%@";
	public static final String PLUGIN_END_PLACEHOLDER = "@%>";

	private static Map<String, IPlugin> pluginMap = new HashMap<String, IPlugin>();

	/**
	 * 
	 * 响应body含有标签则交由用户处理，否则不作任何处理直接返回
	 * 
	 * @param request
	 * @param bodyContent
	 * @return
	 */
	public static String renderFilterPlugin(String request, String bodyContent) {
		PluginContentItem item = getPlugin(bodyContent);
		StringBuffer resultContent = new StringBuffer();
		resultContent.append(item.getContentList().get(0));
		for (int i = 1; i < item.getContentList().size(); i++) {
			IPlugin plugin = item.getPluginList().get(i - 1);
			if (plugin instanceof IFilterPlugin) {
				bodyContent = bodyContent.substring(bodyContent.indexOf(PLUGIN_END_PLACEHOLDER)
						+ PLUGIN_END_PLACEHOLDER.length());

				resultContent.append(((IFilterPlugin) plugin).handleFilterPlugin(request, bodyContent));
			}
			if (!(plugin instanceof IFormateContentFilterPlugin)) {
				resultContent.append(item.getContentList().get(i));
			}
		}

		return resultContent.toString();
	}

	private static PluginContentItem getPlugin(String bodyContent) {
		PluginContentItem item = new PluginContentItem();
		String[] segs = bodyContent.split(PLUGIN_START_PLACEHOLDER);
		item.addContent(segs[0]);
		for (int i = 1; i < segs.length; i++) {
			int endPlace = segs[i].indexOf(PLUGIN_END_PLACEHOLDER);
			if (endPlace >= 0) {
				String className = segs[i].substring(0, endPlace).trim();
				IPlugin plugin = pluginMap.get(className);
				if (plugin == null) {
					try {
						Class<?> clazz = getPluginClass(className);
						plugin = (IPlugin) clazz.newInstance();
						pluginMap.put(className, plugin);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
				item.addPlugin(plugin);
				item.addContent(segs[i].substring(endPlace + PLUGIN_END_PLACEHOLDER.length()));
			}
		}
		return item;
	}

	private static Class<?> getPluginClass(String className) throws ClassNotFoundException {
		return PluginClassLoader.getInstance().loadClass(className);
	}
}