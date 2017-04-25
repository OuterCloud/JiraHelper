package com.quality.web;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.quality.constant.LogConstant;


/**
 * 所有Controller都需要的操作
 * 
 */
@Controller
public class BaseController {
	
	@ExceptionHandler(Throwable.class)
	public String handleException(Throwable e) {

		LogConstant.debugLog.error("", e);
		return "/fail";
	}
	

	
	public boolean verifyPara(String... params) {

		for (String param : params) {
			if (StringUtils.isBlank(param)) {
				return false;
			}
		}
		return true;
	}
	
	
	
}
