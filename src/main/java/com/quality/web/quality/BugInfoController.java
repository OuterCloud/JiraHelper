package com.quality.web.quality;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.quality.web.BaseController;

@Controller
@RequestMapping("quality")
public class BugInfoController extends BaseController{
	@RequestMapping("bugInfo")
	public String index() {
		return "quality/bugInfo";
	}
}
