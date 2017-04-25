package com.quality.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import com.quality.util.OpenId;

public class SessionFilter extends OncePerRequestFilter {

	@Autowired
	private OpenId openId;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		/**
		 * 需要使用OpenId时将下面的注释去掉，并且修改OpenId.java中相应的配置
		 */
		String queryString = request.getQueryString();

		if (request.getSession().getAttribute("userCorpMail") == null) {
			if (queryString != null && queryString.contains("openid.assoc_handle")) {
				if (openId.check_authentication(queryString)) {
					String corpMail = request.getParameter("openid.sreg.email");
					String fullName = "";
					if (System.getProperty("os.name").startsWith("Windows")) {
						fullName = new String(request.getParameter("openid.sreg.fullname").getBytes("ISO-8859-1"),
								"UTF-8");
					} else {
						fullName = new String(request.getParameter("openid.sreg.fullname"));
					}
					String nickName = request.getParameter("openid.sreg.nickname");
					request.getSession().setAttribute("userName", fullName);
					request.getSession().setAttribute("englishName", nickName);
					request.getSession().setAttribute("userCorpMail", corpMail);
				} else {
					response.sendRedirect(openId.generateAuthUrl(request.getRequestURI()));
				}
			} else {
				// 解决带有参数的请求重定向后的问题
				String requestType = request.getHeader("X-Requested-With");
				if (requestType != null) { // ajax请求
					response.setIntHeader("REQUIRES_AUTH", 1);
					response.setHeader("REQUIRES_AUTH_URL", openId.generateAuthUrl("/index.html"));
				} else {
					response.sendRedirect(
							openId.generateAuthUrl(request.getRequestURI() + "?" + request.getQueryString()));
				}
				return;

			}
		}

		chain.doFilter(request, response);

	}

}
