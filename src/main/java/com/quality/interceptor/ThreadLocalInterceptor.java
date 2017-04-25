package com.quality.interceptor;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.quality.constant.LogConstant;
import com.quality.constant.ThreadLocalConstant;
import com.quality.util.ErrorMessageUtil;
import com.quality.util.RequestUtil;

/**
 * 在本地threadLocal中保存request
 */
public class ThreadLocalInterceptor implements HandlerInterceptor {
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {

		ErrorMessageUtil.clear();
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {

		List<String> emList = ErrorMessageUtil.get();
		if (emList != null) {
			RequestUtil.setAttribute(ThreadLocalConstant.ERROR_MESSAGE_REQUEST_KEY, emList);
		}
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		//根据实际需求修改cdnBaseUrl的地址
		request.setAttribute("cdnBaseUrl", "http://quality.qa.ms.netease.com");
		request.setAttribute("cdnFileVersion", "20170327");
		
		ThreadLocalConstant.requestTL.set(request);
		ThreadLocalConstant.responseTL.set(response);
		ThreadLocalConstant.sessionTL.set(request.getSession());
		
		return true;
	}
	
	/**
	 * 获取字符串的真实长度
	 * 
	 * @param string
	 * @return
	 */
	public int getStringLen(String string) {

		int length = 0;
		if (string != null) {
			try {
				length = string.getBytes("utf-8").length;
			} catch (UnsupportedEncodingException e) {
				LogConstant.debugLog.error("[UserInfoSource]Error occuer when getStringLen [string = " + string + "].", e);
			}
		}
		return length;
	}
}
