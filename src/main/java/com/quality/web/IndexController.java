package com.quality.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.quality.util.OpenId;

@Controller
public class IndexController {
	@Autowired
	private OpenId openId;
	
	@RequestMapping("index")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/login/signIn")
	public String signIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.sendRedirect(openId.generateAuthUrl("/index.html"));
		return "index";
	}

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute("email");
		request.getSession().removeAttribute("userName");
		request.getSession().removeAttribute("englishName");
		request.getSession().removeAttribute("userCorpMail");
		request.getSession().removeAttribute("isAdmin");
		return "redirect:/index.html";
	}
}
