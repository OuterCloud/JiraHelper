package com.quality.util;

import javax.servlet.http.HttpServletRequest;

import com.quality.constant.ThreadLocalConstant;

public class RequestUtil {
	
	public static Object getAttribute(String name) {

		return ThreadLocalConstant.requestTL.get().getAttribute(name);
	}
	
	public static void setAttribute(String name, Object value) {

		ThreadLocalConstant.requestTL.get().setAttribute(name, value);
	}
	
	public static void removeAttribute(String name) {

		ThreadLocalConstant.requestTL.get().removeAttribute(name);
	}
	
	public static boolean containsKey(String name) {

		Object value = getAttribute(name);
		if (value != null) {
			return true;
		}
		return false;
	}
	
	public static boolean notContainsKey(String name) {

		Object value = getAttribute(name);
		if (value == null) {
			return true;
		}
		return false;
	}
	
	public static HttpServletRequest getRequest() {

		return ThreadLocalConstant.requestTL.get();
	}
	
	public static String getParameter(String name) {

		return ThreadLocalConstant.requestTL.get().getParameter(name);
	}
}
