package com.quality.web.quality;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atlassian.jira.rest.client.domain.Issue;
import com.quality.constant.InfoConstant;
import com.quality.util.JiraUtil;
import com.quality.web.BaseController;

@Controller
@RequestMapping("quality")
public class JIRAInfoController extends BaseController {
	@RequestMapping("jiraInfo")
	public String basicInfo(HttpServletRequest request, ModelMap modelMap, String issueKey) {
		// issueKey的值从前端url上行参数传过来，不传的话issueKey的值就是空
		// 在前端ftl页面上对isssueKey的值进行判断，并添加相应逻辑处理
		modelMap.addAttribute("issueKey", issueKey);
		return "quality/jiraInfo";
	}

	@RequestMapping("myJiraInfo")
	public String myJiraInfo(HttpServletRequest request, ModelMap modelMap) throws URISyntaxException {
		try {
			String current_user_name = request.getSession().getAttribute("englishName").toString();
			String userName = InfoConstant.userName;
			String passWord = InfoConstant.passWord;
			String jql = "status in (Open, \"In Progress\", \"To Do\", 需求评审中, UI设计中, 排期中, 开发中, 测试中, 待上线, UE设计中, 建模中, \"Code Review\") AND resolution = Unresolved AND 分派给 in ("
					+ current_user_name + ") ORDER BY created DESC";
			ArrayList<String> searchResult = JiraUtil.search_jql(userName, passWord, current_user_name, jql);
			ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
			String issueSummary = "";
			for (String issueKey : searchResult) {
				issueSummary = JiraUtil.get_issue(issueKey, userName, passWord).getSummary();
				HashMap<String, String> result = new HashMap<String, String>();
				result.put("issueSummary", issueSummary);
				result.put("issueKey", issueKey);
				results.add(result);
			}
			modelMap.put("retCode", 200);
			modelMap.put("searchResult", results);
		} catch (Exception e) {
			modelMap.put("retCode", -1);
			modelMap.put("tip", "出异常了！");
		}
		return "quality/myJiraInfo";
	}

	@RequestMapping("jiraInfo/jiraSearch")
	@ResponseBody
	public Map<String, Object> jiraSearch(HttpServletRequest request, ModelMap modelMap, String issueNum)
			throws URISyntaxException {
		Map<String, Object> retMap = new HashMap<>();
		try {
			String userName = InfoConstant.userName;
			String passWord = InfoConstant.passWord;
			Issue issue = JiraUtil.get_issue(issueNum, userName, passWord);
			retMap.put("retCode", 200);
			retMap.put("issueNum", issueNum);
			retMap.put("summary", issue.getSummary().toString());
			retMap.put("jiraInfo", JiraUtil.get_jira_basic_info(issue, userName, passWord));
			retMap.put("docInfo", JiraUtil.get_jira_doc_info(issue));
			System.out.println(JiraUtil.get_jira_doc_info(issue));
			return retMap;
		} catch (Exception e) {
			retMap.put("retCode", -1);
			retMap.put("tip", "您输入的查询信息有误！");
			return retMap;
		}
	}

	@RequestMapping("jiraInfo/jiraInfo")
	@ResponseBody
	public Map<String, Object> jiraInfo(HttpServletRequest request, ModelMap modelMap, String issueNum)
			throws URISyntaxException {
		Map<String, Object> retMap = new HashMap<>();
		try {
			String userName = InfoConstant.userName;
			String passWord = InfoConstant.passWord;
			Issue issue = JiraUtil.get_issue(issueNum, userName, passWord);
			retMap.put("retCode", 200);
			retMap.put("issueNum", issueNum);
			retMap.put("summary", issue.getSummary().toString());
			retMap.put("jiraInfo", JiraUtil.get_jira_basic_info(issue, userName, passWord));
			return retMap;
		} catch (Exception e) {
			retMap.put("retCode", -1);
			retMap.put("tip", "您输入的查询信息有误！");
			return retMap;
		}
	}

	@RequestMapping("jiraInfo/jiraRelateInfo")
	@ResponseBody
	public Map<String, Object> jiraRelateInfo(HttpServletRequest request, ModelMap modelMap, String issueNum)
			throws URISyntaxException {
		Map<String, Object> retMap = new HashMap<>();
		try {
			String userName = InfoConstant.userName;
			String passWord = InfoConstant.passWord;
			Issue issue = JiraUtil.get_issue(issueNum, userName, passWord);
			retMap.put("retCode", 200);
			retMap.put("issueNum", issueNum);
			retMap.put("summary", issue.getSummary().toString());
			retMap.put("jiraInfo", JiraUtil.get_jira_all_info(issue, userName, passWord));
			return retMap;
		} catch (Exception e) {
			retMap.put("retCode", -1);
			retMap.put("tip", "您输入的查询信息有误！");
			return retMap;
		}
	}
}
