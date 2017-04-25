package com.quality.util;

import java.util.ArrayList;
import java.util.List;

import com.quality.constant.ThreadLocalConstant;

public class ErrorMessageUtil {
	
	public static void put(String errorMessage) {

		List<String> emList = ThreadLocalConstant.ERROR_MESSAGE_TL.get();
		if (emList == null) {
			emList = new ArrayList<String>();
			ThreadLocalConstant.ERROR_MESSAGE_TL.set(emList);
		}
		emList.add(errorMessage);
	}
	
	public static List<String> get() {

		List<String> emList = ThreadLocalConstant.ERROR_MESSAGE_TL.get();
		if (emList == null || emList.size() == 0) {
			return null;
		}
		return emList;
	}
	
	public static void clear() {

		List<String> emList = ThreadLocalConstant.ERROR_MESSAGE_TL.get();
		if (emList != null) {
			emList.clear();
		}
		
	}
	
	public static String getErrorMessages(){
		
		List<String> emList = ThreadLocalConstant.ERROR_MESSAGE_TL.get();
		if (emList == null || emList.size() == 0) {
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder();
		for (String error : emList) {
			stringBuilder.append(error);
		}
		return stringBuilder.toString();
	}
}
