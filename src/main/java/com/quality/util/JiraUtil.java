package com.quality.util;

import java.awt.print.Printable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.springframework.scheduling.commonj.ScheduledTimerListener;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.domain.BasicComponent;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.Comment;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.IssueLink;
import com.atlassian.jira.rest.client.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClientFactory;
import com.quality.constant.InfoConstant;
import com.quality.constant.LogConstant;
import com.quality.model.JiraInfoModel;

import mjson.Json;

public class JiraUtil {
	/**
	 * 登录JIRA并返回指定的JiraRestClient对象
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws URISyntaxException
	 */
	public static JiraRestClient login_jira(String username, String password) throws URISyntaxException {
		try {
			final JerseyJiraRestClientFactory factory = new JerseyJiraRestClientFactory();
			final URI jiraServerUri = new URI("http://jira.ms.netease.com");
			final JiraRestClient restClient = factory.createWithBasicHttpAuthentication(jiraServerUri, username,
					password);
			return restClient;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取并返回指定的Issue对象
	 * 
	 * @param issueNum
	 * @param username
	 * @param password
	 * @return
	 * @throws URISyntaxException
	 */
	public static Issue get_issue(String issueNum, String username, String password) throws URISyntaxException {
		try {
			final JiraRestClient restClient = login_jira(username, password);
			final NullProgressMonitor pm = new NullProgressMonitor();
			final Issue issue = restClient.getIssueClient().getIssue(issueNum, pm);
			return issue;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA备注部分的内容
	 * 
	 * @param issue
	 * @param username
	 * @param password
	 * @return
	 * @throws URISyntaxException
	 */
	public static List<String> get_comments_body(Issue issue) throws URISyntaxException {
		try {
			List<String> comments = new ArrayList<String>();
			for (Comment comment : issue.getComments()) {
				comments.add(comment.getBody().toString());
			}
			return comments;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的创建时间
	 * 
	 * @param issueNum
	 * @param username
	 * @param password
	 * @return
	 * @throws URISyntaxException
	 */
	public static DateTime get_create_time(Issue issue) throws URISyntaxException {
		try {
			return issue.getCreationDate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的描述部分
	 * 
	 * @param issueNum
	 * @param username
	 * @param password
	 * @return
	 * @throws URISyntaxException
	 */
	public static String get_description(Issue issue) throws URISyntaxException {
		try {
			return issue.getDescription();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的标题
	 * 
	 * @param issueNum
	 * @param username
	 * @param password
	 * @return
	 * @throws URISyntaxException
	 */
	public static String get_summary(Issue issue) throws URISyntaxException {
		try {
			return issue.getSummary();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的报告人的名字
	 * 
	 * @param issueNum
	 * @param username
	 * @param password
	 * @return
	 * @throws URISyntaxException
	 */
	public static String get_reporter(Issue issue) throws URISyntaxException {
		try {
			return issue.getReporter().getDisplayName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的状态
	 * 
	 * @param issue
	 * @param username
	 * @param password
	 * @return
	 * @throws URISyntaxException
	 */
	public static String get_status(Issue issue) throws URISyntaxException {
		try {
			return issue.getStatus().getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的类型
	 * 
	 * @param issue
	 * @param username
	 * @param password
	 * @return
	 * @throws URISyntaxException
	 */
	public static String get_issue_type(Issue issue) throws URISyntaxException {
		try {
			return issue.getIssueType().getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的模块
	 * 
	 * @param issue
	 * @param username
	 * @param password
	 * @return
	 * @throws URISyntaxException
	 */
	public static ArrayList<String> get_modules(Issue issue) throws URISyntaxException {
		try {
			ArrayList<String> arrayList = new ArrayList<String>();
			Iterator<BasicComponent> basicComponents = issue.getComponents().iterator();
			while (basicComponents.hasNext()) {
				String moduleName = basicComponents.next().getName();
				arrayList.add(moduleName);
			}
			return arrayList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的分派人的姓名列表
	 * 
	 * @param issue
	 * @param username
	 * @param password
	 * @return
	 * @throws URISyntaxException
	 */
	public static ArrayList<String> get_assignees(Issue issue) throws URISyntaxException {
		try {
			Json json = Json.read(issue.getFieldByName("分派给").getValue().toString());
			Iterator<Json> assignees = json.asJsonList().iterator();
			ArrayList<String> assigneesNames = new ArrayList<String>();
			while (assignees.hasNext()) {
				Json assignee = Json.read(assignees.next().toString());
				String assigneeName = assignee.at("displayName").toString();
				assigneesNames.add(assigneeName);
			}
			return assigneesNames;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的分派人的姓名和Email列表
	 * 
	 * @param issue
	 * @param username
	 * @param password
	 * @return
	 * @throws URISyntaxException
	 */
	public static ArrayList<String> get_assignees_a(Issue issue) throws URISyntaxException {
		try {
			Json json = Json.read(issue.getFieldByName("分派给").getValue().toString());
			Iterator<Json> assignees = json.asJsonList().iterator();
			ArrayList<String> assigneesNames = new ArrayList<String>();
			while (assignees.hasNext()) {
				Json assignee = Json.read(assignees.next().toString());
				String assigneeName = assignee.at("displayName").toString();
				assigneeName = "<a class='apopover' title=\"注意\" data-toggle=\"popover\" data-trigger=\"hover\" email="
						+ assignee.at("emailAddress") + " onclick='sendPopoTo(" + assignee.at("emailAddress")
						+ ",\"测试说明文档\")'>" + assigneeName + "</a>";
				assigneesNames.add(assigneeName);
			}
			return assigneesNames;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的前端人员的姓名列表
	 * 
	 * @param issue
	 * @param username
	 * @param password
	 * @return
	 * @throws URISyntaxException
	 */
	public static ArrayList<String> get_qianduans(Issue issue) throws URISyntaxException {
		try {
			ArrayList<String> qianduanList = new ArrayList<String>();
			Json json = Json.read(issue.getFieldByName("前端").getValue().toString());
			Iterator<Json> qianduans = json.asJsonList().iterator();
			while (qianduans.hasNext()) {
				Json qianduan = Json.read(qianduans.next().toString());
				// System.out.println(qianduan);
				String qianduanName = qianduan.at("displayName").toString();
				qianduanList.add(qianduanName);
			}
			return qianduanList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的前端人员的姓名和Email列表
	 * 
	 * @param issue
	 * @param username
	 * @param password
	 * @return
	 * @throws URISyntaxException
	 */
	public static ArrayList<String> get_qianduans_a(Issue issue) throws URISyntaxException {
		try {
			ArrayList<String> qianduanList = new ArrayList<String>();
			Json json = Json.read(issue.getFieldByName("前端").getValue().toString());
			Iterator<Json> qianduans = json.asJsonList().iterator();
			while (qianduans.hasNext()) {
				Json qianduan = Json.read(qianduans.next().toString());
				// System.out.println(qianduan);
				String qianduanName = qianduan.at("displayName").toString();
				qianduanName = "<a class='apopover' title=\"注意\" data-toggle=\"popover\" data-trigger=\"hover\" email="
						+ qianduan.at("emailAddress") + " onclick='sendPopoTo(" + qianduan.at("emailAddress")
						+ ",\"前端配置文档\")'>" + qianduanName + "</a>";
				qianduanList.add(qianduanName);
			}
			return qianduanList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的开发的姓名和Email列表
	 * 
	 * @param issue
	 * @param username
	 * @param password
	 * @return
	 * @throws URISyntaxException
	 */
	public static ArrayList<String> get_developers_a(Issue issue) throws URISyntaxException {
		try {
			ArrayList<String> developersList = new ArrayList<String>();
			Json json = Json.read(issue.getFieldByName("开发").getValue().toString());
			Iterator<Json> developers = json.asJsonList().iterator();
			while (developers.hasNext()) {
				Json developer = Json.read(developers.next().toString());
				String developerName = developer.at("displayName").toString();
				developerName = "<a class='apopover' title=\"注意\" data-toggle=\"popover\" data-trigger=\"hover\" email="
						+ developer.at("emailAddress") + " onclick='sendPopoTo(" + developer.at("emailAddress")
						+ ",\"后台接口文档\")'>" + developerName + "</a>";
				developersList.add(developerName);
			}
			return developersList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的开发的姓名列表
	 * 
	 * @param issue
	 * @param username
	 * @param password
	 * @return
	 * @throws URISyntaxException
	 */
	public static ArrayList<String> get_developers(Issue issue) throws URISyntaxException {
		try {
			ArrayList<String> developersList = new ArrayList<String>();
			Json json = Json.read(issue.getFieldByName("开发").getValue().toString());
			Iterator<Json> developers = json.asJsonList().iterator();
			while (developers.hasNext()) {
				Json developer = Json.read(developers.next().toString());
				String developerName = developer.at("displayName").toString();
				developersList.add(developerName);
			}
			return developersList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的产品人员的姓名列表
	 * 
	 * @param issue
	 * @param username
	 * @param password
	 * @return
	 * @throws URISyntaxException
	 */
	public static String get_product(Issue issue) throws URISyntaxException {
		try {
			String product_field = issue.getFieldByName("产品人员").getValue().toString();
			String productName = Json.read(product_field).at("displayName").toString();
			return productName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的产品人员的姓名和Email列表
	 * 
	 * @param issue
	 * @param username
	 * @param password
	 * @return
	 * @throws URISyntaxException
	 */
	public static String get_product_a(Issue issue) throws URISyntaxException {
		try {
			String product_field = issue.getFieldByName("产品人员").getValue().toString();
			String productName = Json.read(product_field).at("displayName").toString();
			String productEmail = Json.read(product_field).at("emailAddress").toString();
			productName = "<a class='apopover' data-toggle=\"popover\" data-trigger=\"hover\" title=\"注意\" email="
					+ productEmail + " onclick='sendPopoTo(" + productEmail + ",\"产品需求文档\")'>" + productName + "</a>";
			return productName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的UE开始时间
	 * 
	 * @param issue
	 * @return
	 * @throws URISyntaxException
	 */
	public static String get_UE_start_time(Issue issue) throws URISyntaxException {
		try {
			String UE_start_time = issue.getFieldByName("UE开始时间").getValue().toString();
			// System.out.println("UE_start_time:"+UE_start_time);
			return UE_start_time;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的UE结束时间
	 * 
	 * @param issue
	 * @return
	 * @throws URISyntaxException
	 */
	public static String get_UE_end_time(Issue issue) throws URISyntaxException {
		try {
			String UE_end_time = issue.getFieldByName("UE结束时间").getValue().toString();
			// System.out.println("UE_end_time:"+UE_end_time);
			return UE_end_time;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的UI开始时间
	 * 
	 * @param issue
	 * @return
	 * @throws URISyntaxException
	 */
	public static String get_UI_start_time(Issue issue) throws URISyntaxException {
		try {
			String UI_start_time = issue.getFieldByName("UI开始时间").getValue().toString();
			return UI_start_time;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的UI结束时间
	 * 
	 * @param issue
	 * @return
	 * @throws URISyntaxException
	 */
	public static String get_UI_end_time(Issue issue) throws URISyntaxException {
		try {
			String UI_end_time = issue.getFieldByName("UI结束时间").getValue().toString();
			return UI_end_time;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的客户端开始时间
	 * 
	 * @param issue
	 * @return
	 * @throws URISyntaxException
	 */
	public static String get_app_start_time(Issue issue) throws URISyntaxException {
		try {
			String app_start_time = issue.getFieldByName("客户端开始时间").getValue().toString();
			return app_start_time;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的客户端结束时间
	 * 
	 * @param issue
	 * @return
	 * @throws URISyntaxException
	 */
	public static String get_app_end_time(Issue issue) throws URISyntaxException {
		try {
			String app_end_time = issue.getFieldByName("客户端结束时间").getValue().toString();
			return app_end_time;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的前端开始时间
	 * 
	 * @param issue
	 * @return
	 * @throws URISyntaxException
	 */
	public static String get_qianduan_start_time(Issue issue) throws URISyntaxException {
		try {
			String qianduan_start_time = issue.getFieldByName("前端开始时间").getValue().toString();
			return qianduan_start_time;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的前端结束时间
	 * 
	 * @param issue
	 * @return
	 * @throws URISyntaxException
	 */
	public static String get_qianduan_end_time(Issue issue) throws URISyntaxException {
		try {
			String qianduan_end_time = issue.getFieldByName("前端结束时间").getValue().toString();
			return qianduan_end_time;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的开发开始时间
	 * 
	 * @param issue
	 * @return
	 * @throws URISyntaxException
	 */
	public static String get_develop_start_time(Issue issue) throws URISyntaxException {
		try {
			String develop_start_time = issue.getFieldByName("开发开始时间").getValue().toString();
			return develop_start_time;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的开发结束时间
	 * 
	 * @param issue
	 * @return
	 * @throws URISyntaxException
	 */
	public static String get_develop_end_time(Issue issue) throws URISyntaxException {
		try {
			String develop_end_time = issue.getFieldByName("开发结束时间").getValue().toString();
			return develop_end_time;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的联调开始时间
	 * 
	 * @param issue
	 * @return
	 * @throws URISyntaxException
	 */
	public static String get_liantiao_start_time(Issue issue) throws URISyntaxException {
		try {
			String liantiao_start_time = issue.getFieldByName("联调开始时间").getValue().toString();
			return liantiao_start_time;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的联调结束时间
	 * 
	 * @param issue
	 * @return
	 * @throws URISyntaxException
	 */
	public static String get_liantiao_end_time(Issue issue) throws URISyntaxException {
		try {
			String liantiao_end_time = issue.getFieldByName("联调结束时间").getValue().toString();
			return liantiao_end_time;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的测试开始时间
	 * 
	 * @param issue
	 * @return
	 * @throws URISyntaxException
	 */
	public static String get_test_start_time(Issue issue) throws URISyntaxException {
		try {
			String test_start_time = issue.getFieldByName("测试开始时间").getValue().toString();
			return test_start_time;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的测试结束时间
	 * 
	 * @param issue
	 * @return
	 * @throws URISyntaxException
	 */
	public static String get_test_end_time(Issue issue) throws URISyntaxException {
		try {
			String test_end_time = issue.getFieldByName("测试结束时间").getValue().toString();
			return test_end_time;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定JIRA的关联JIRA
	 * 
	 * @param issue
	 * @return
	 * @throws URISyntaxException
	 */
	private static String get_issue_links(Issue issue, String username, String password) throws URISyntaxException {
		ArrayList<String> issueLinksArray = new ArrayList<String>();
		Iterator<IssueLink> issueLinks = issue.getIssueLinks().iterator();
		while (issueLinks.hasNext()) {
			String issueKey = issueLinks.next().getTargetIssueKey();
			Issue relate_issue = JiraUtil.get_issue(issueKey, username, password);
			String relate_issue_type = JiraUtil.get_issue_type(relate_issue);
			String relate_issue_title = relate_issue.getSummary();
			issueLinksArray.add("<a target='_blank' href='http://jira.ms.netease.com/browse/" + issueKey + "'>["
					+ relate_issue_type + "]" + relate_issue_title + "</a><br>");
		}
		return StringUtils.join(issueLinksArray, "");
	}

	/**
	 * 获取所有可以收集到的JIRA信息 （不包括关联JIRA信息）并返回JiraInfoModel类型对象
	 * 
	 * @param issue
	 * @return
	 * @throws URISyntaxException
	 */
	public static JiraInfoModel get_jira_basic_info(Issue issue, String username, String password)
			throws URISyntaxException {
		String jiraCommentsBody = StringUtils.join(get_comments_body(issue), ",");
		String jiraCreateTime = get_create_time(issue).toString();
		String description = get_description(issue);
		String summary = get_summary(issue);
		String reporter = get_reporter(issue);
		String assignees = StringUtils.join(get_assignees(issue), ",");
		String status = get_status(issue);
		String issueType = get_issue_type(issue);
		String modules = StringUtils.join(get_modules(issue), ",");
		String qianduans = StringUtils.join(get_qianduans(issue), ",");
		String developers = StringUtils.join(get_developers(issue), ",");
		String assignees_a = StringUtils.join(get_assignees_a(issue), ",");
		String qianduans_a = StringUtils.join(get_qianduans_a(issue), ",");
		String developers_a = StringUtils.join(get_developers_a(issue), ",");
		String product_a = get_product_a(issue);
		String product = get_product(issue);
		String UE_start_time = get_UE_start_time(issue);
		String UE_end_time = get_UE_end_time(issue);
		String UI_start_time = get_UI_start_time(issue);
		String UI_end_time = get_UI_end_time(issue);
		String app_start_time = get_app_start_time(issue);
		String app_end_time = get_app_end_time(issue);
		String qianduan_start_time = get_qianduan_start_time(issue);
		String qianduan_end_time = get_qianduan_end_time(issue);
		String develop_start_time = get_develop_start_time(issue);
		String develop_end_time = get_develop_end_time(issue);
		String liantiao_start_time = get_liantiao_start_time(issue);
		String liantiao_end_time = get_liantiao_end_time(issue);
		String test_start_time = get_test_start_time(issue);
		String test_end_time = get_test_end_time(issue);
		JiraInfoModel jiraInfoModel = new JiraInfoModel();
		jiraInfoModel.setJiraCommentsBody(jiraCommentsBody);
		jiraInfoModel.setJiraCreateTime(jiraCreateTime);
		jiraInfoModel.setDescription(description);
		jiraInfoModel.setSummary(summary);
		jiraInfoModel.setReporter(reporter);
		jiraInfoModel.setAssignees(assignees);
		jiraInfoModel.setStatus(status);
		jiraInfoModel.setIssueType(issueType);
		jiraInfoModel.setModules(modules);
		jiraInfoModel.setQianduans(qianduans);
		jiraInfoModel.setDevelopers(developers);
		jiraInfoModel.setProduct(product);
		jiraInfoModel.setAssignees_a(assignees_a);
		jiraInfoModel.setQianduans_a(qianduans_a);
		jiraInfoModel.setDevelopers_a(developers_a);
		jiraInfoModel.setProduct_a(product_a);
		jiraInfoModel.setUE_start_time(UE_start_time);
		jiraInfoModel.setUE_end_time(UE_end_time);
		jiraInfoModel.setUI_start_time(UI_start_time);
		jiraInfoModel.setUI_end_time(UI_end_time);
		jiraInfoModel.setApp_start_time(app_start_time);
		jiraInfoModel.setApp_end_time(app_end_time);
		jiraInfoModel.setQianduan_start_time(qianduan_start_time);
		jiraInfoModel.setQianduan_end_time(qianduan_end_time);
		jiraInfoModel.setDevelop_start_time(develop_start_time);
		jiraInfoModel.setDevelop_end_time(develop_end_time);
		jiraInfoModel.setLiantiao_start_time(liantiao_start_time);
		jiraInfoModel.setLiantiao_end_time(liantiao_end_time);
		jiraInfoModel.setTest_start_time(test_start_time);
		jiraInfoModel.setTest_end_time(test_end_time);
		return jiraInfoModel;
	}

	/**
	 * 获取所有可以收集到的JIRA信息 （包括关联JIRA信息）并返回JiraInfoModel类型对象
	 * 
	 * @param issue
	 * @return
	 * @throws URISyntaxException
	 */
	public static JiraInfoModel get_jira_all_info(Issue issue, String username, String password)
			throws URISyntaxException {
		String jiraCommentsBody = StringUtils.join(get_comments_body(issue), ",");
		String jiraCreateTime = get_create_time(issue).toString();
		String description = get_description(issue);
		String summary = get_summary(issue);
		String reporter = get_reporter(issue);
		String status = get_status(issue);
		String issueType = get_issue_type(issue);
		String modules = StringUtils.join(get_modules(issue), ",");
		String assignees = StringUtils.join(get_assignees(issue), ",");
		String qianduans = StringUtils.join(get_qianduans(issue), ",");
		String developers = StringUtils.join(get_developers(issue), ",");
		String product = get_product(issue);
		String assignees_a = StringUtils.join(get_assignees_a(issue), ",");
		String qianduans_a = StringUtils.join(get_qianduans_a(issue), ",");
		String developers_a = StringUtils.join(get_developers_a(issue), ",");
		String product_a = get_product_a(issue);
		String UE_start_time = get_UE_start_time(issue);
		String UE_end_time = get_UE_end_time(issue);
		String UI_start_time = get_UI_start_time(issue);
		String UI_end_time = get_UI_end_time(issue);
		String app_start_time = get_app_start_time(issue);
		String app_end_time = get_app_end_time(issue);
		String qianduan_start_time = get_qianduan_start_time(issue);
		String qianduan_end_time = get_qianduan_end_time(issue);
		String develop_start_time = get_develop_start_time(issue);
		String develop_end_time = get_develop_end_time(issue);
		String liantiao_start_time = get_liantiao_start_time(issue);
		String liantiao_end_time = get_liantiao_end_time(issue);
		String test_start_time = get_test_start_time(issue);
		String test_end_time = get_test_end_time(issue);
		String issue_links = get_issue_links(issue, username, password);
		JiraInfoModel jiraInfoModel = new JiraInfoModel();
		jiraInfoModel.setJiraCommentsBody(jiraCommentsBody);
		jiraInfoModel.setJiraCreateTime(jiraCreateTime);
		jiraInfoModel.setDescription(description);
		jiraInfoModel.setSummary(summary);
		jiraInfoModel.setReporter(reporter);
		jiraInfoModel.setStatus(status);
		jiraInfoModel.setIssueType(issueType);
		jiraInfoModel.setModules(modules);
		jiraInfoModel.setAssignees(assignees);
		jiraInfoModel.setQianduans(qianduans);
		jiraInfoModel.setDevelopers(developers);
		jiraInfoModel.setProduct(product);
		jiraInfoModel.setAssignees_a(assignees_a);
		jiraInfoModel.setQianduans_a(qianduans_a);
		jiraInfoModel.setDevelopers_a(developers_a);
		jiraInfoModel.setProduct_a(product_a);
		jiraInfoModel.setUE_start_time(UE_start_time);
		jiraInfoModel.setUE_end_time(UE_end_time);
		jiraInfoModel.setUI_start_time(UI_start_time);
		jiraInfoModel.setUI_end_time(UI_end_time);
		jiraInfoModel.setApp_start_time(app_start_time);
		jiraInfoModel.setApp_end_time(app_end_time);
		jiraInfoModel.setQianduan_start_time(qianduan_start_time);
		jiraInfoModel.setQianduan_end_time(qianduan_end_time);
		jiraInfoModel.setDevelop_start_time(develop_start_time);
		jiraInfoModel.setDevelop_end_time(develop_end_time);
		jiraInfoModel.setLiantiao_start_time(liantiao_start_time);
		jiraInfoModel.setLiantiao_end_time(liantiao_end_time);
		jiraInfoModel.setTest_start_time(test_start_time);
		jiraInfoModel.setTest_end_time(test_end_time);
		jiraInfoModel.setIssue_links(issue_links);
		return jiraInfoModel;
	}

	/**
	 * 获取JIRA的文档信息
	 * 
	 * @param issue
	 * @param userName
	 * @param passWord
	 * @return
	 * @throws URISyntaxException
	 */
	public static Json get_jira_doc_info(Issue issue) throws URISyntaxException {
		List<String> comments = JiraUtil.get_comments_body(issue);
		String productDocRegex = "\\[产品需求文档\\|.*\\]";
		Pattern productDocPattern = Pattern.compile(productDocRegex);
		String webDocRegex = "\\[前端配置文档\\|.*\\]";
		Pattern webDocPattern = Pattern.compile(webDocRegex);
		String interDocRegex = "\\[后台接口文档\\|.*\\]";
		Pattern interDocPattern = Pattern.compile(interDocRegex);
		String testDocRegex = "\\[测试说明文档\\|.*\\]";
		Pattern testDocPattern = Pattern.compile(testDocRegex);
		Matcher productDocMatcher, webDocMatcher, interDocMatcher, testDocMatcher;
		String productDocUrl = "";
		String webDocUrl = "";
		String interDocUrl = "";
		String testDocUrl = "";
		Json docInfos = Json.object();
		for (String comment : comments) {
			productDocMatcher = productDocPattern.matcher(comment);
			webDocMatcher = webDocPattern.matcher(comment);
			interDocMatcher = interDocPattern.matcher(comment);
			testDocMatcher = testDocPattern.matcher(comment);
			if (productDocMatcher.find()) {
				for (int i = 0; i <= productDocMatcher.groupCount(); i++) {
					productDocUrl = productDocMatcher.group(i).split("\\|")[1];
					productDocUrl = productDocUrl.substring(0, productDocUrl.length() - 1);
					LogConstant.runLog.info("productDocUrl:" + productDocUrl);
					docInfos.set("productDocUrl", productDocUrl);
				}
			}
			if (webDocMatcher.find()) {
				for (int i = 0; i <= webDocMatcher.groupCount(); i++) {
					webDocUrl = webDocMatcher.group(i).split("\\|")[1];
					webDocUrl = webDocUrl.substring(0, webDocUrl.length() - 1);
					docInfos.set("webDocUrl", webDocUrl);
				}
			}
			if (interDocMatcher.find()) {
				for (int i = 0; i <= interDocMatcher.groupCount(); i++) {
					interDocUrl = interDocMatcher.group(i).split("\\|")[1];
					interDocUrl = interDocUrl.substring(0, interDocUrl.length() - 1);
					docInfos.set("interDocUrl", interDocUrl);
				}
			}
			if (testDocMatcher.find()) {
				for (int i = 0; i <= testDocMatcher.groupCount(); i++) {
					testDocUrl = testDocMatcher.group(i).split("\\|")[1];
					testDocUrl = testDocUrl.substring(0, testDocUrl.length() - 1);
					docInfos.set("testDocUrl", testDocUrl);
				}
			}
		}
		return docInfos;
	}

	/**
	 * 通过jql语句进行查询并返回一个包含issue的key的数组
	 * 
	 * @param username
	 *            登录JIRA的用户名
	 * @param password
	 *            登录JIRA的用户密码
	 * @param current_user_name
	 *            要查询的用户名
	 * @param jql
	 * @return
	 * @throws URISyntaxException
	 */
	public static ArrayList<String> search_jql(String username, String password, String current_user_name, String jql)
			throws URISyntaxException {
		final JiraRestClient restClient = login_jira(username, password);
		final NullProgressMonitor pm = new NullProgressMonitor();
		SearchResult searchResult = restClient.getSearchClient().searchJql(jql, pm);
		Iterator<BasicIssue> basicIssues = searchResult.getIssues().iterator();
		ArrayList<String> issueKeys = new ArrayList<>();
		while (basicIssues.hasNext()) {
			String issueKey = basicIssues.next().getKey();
			LogConstant.runLog.info("issueKey:"+issueKey);
			issueKeys.add(issueKey);
		}
		return issueKeys;
	}

	/**
	 * 测试函数
	 * 
	 * @param args
	 * @throws URISyntaxException
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) throws URISyntaxException {
		String username = InfoConstant.userName;
		String password = InfoConstant.passWord;
		String issueNum = "SILVERCRM-42";
		// Issue issue = JiraUtil.get_issue(issueNum, username, password);
		String currentUserName = "bjlantianyou";
		String jql = "status in (Open, \"In Progress\", \"To Do\", 需求评审中, UI设计中, 排期中, 开发中, 测试中, 待上线, UE设计中, 建模中, \"Code Review\") AND resolution = Unresolved AND 分派给 in ("
				+ currentUserName + ") ORDER BY created DESC";
		ArrayList<String> searchResult = search_jql(username, password, currentUserName, jql);
		// System.out.println(JiraUtil.get_assignees_a(issue));
		// JSONArray docInfosJsonArray = get_jira_doc_info(issue);
	}

}
