package com.quality.model;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class JiraInfoModel {
	String jiraCommentsBody;
	String jiraCreateTime;
	String description;
	String summary;
	String reporter;
	String status;
	String issueType;
	String modules;
	String assignees;
	String qianduans;
	String developers;
	String product;
	String assignees_a;
	String qianduans_a;
	String developers_a;
	String product_a;
	String start_develop_time;
	String UE_start_time;
	String UE_end_time;
	String UI_start_time;
	String UI_end_time;
	String app_start_time;
	String app_end_time;
	String qianduan_start_time;
	String qianduan_end_time;
	String develop_start_time;
	String develop_end_time;
	String liantiao_start_time;
	String liantiao_end_time;
	String test_start_time;
	String test_end_time;
	String issue_links;

	public String getIssue_links() {
		return issue_links;
	}

	public void setIssue_links(String issue_links) {
		this.issue_links = issue_links;
	}

	public String getJiraCommentsBody() {
		return jiraCommentsBody;
	}

	public void setJiraCommentsBody(String jiraCommentsBody) {
		this.jiraCommentsBody = jiraCommentsBody;
	}

	public String getJiraCreateTime() {
		return jiraCreateTime;
	}

	public void setJiraCreateTime(String jiraCreateTime) {
		this.jiraCreateTime = jiraCreateTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getReporter() {
		return reporter;
	}

	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	public String getAssignees() {
		return assignees;
	}

	public void setAssignees(String assignees) {
		this.assignees = assignees;
	}

	public String getAssignees_a() {
		return assignees_a;
	}

	public void setAssignees_a(String assignees_a) {
		this.assignees_a = assignees_a;
	}

	public String getQianduans_a() {
		return qianduans_a;
	}

	public void setQianduans_a(String qianduans_a) {
		this.qianduans_a = qianduans_a;
	}

	public String getDevelopers_a() {
		return developers_a;
	}

	public void setDevelopers_a(String developers_a) {
		this.developers_a = developers_a;
	}

	public String getProduct_a() {
		return product_a;
	}

	public void setProduct_a(String product_a) {
		this.product_a = product_a;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public String getModules() {
		return modules;
	}

	public void setModules(String modules) {
		this.modules = modules;
	}

	public String getQianduans() {
		return qianduans;
	}

	public void setQianduans(String qianduans) {
		this.qianduans = qianduans;
	}

	public String getDevelopers() {
		return developers;
	}

	public void setDevelopers(String developers) {
		this.developers = developers;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getStart_develop_time() {
		return start_develop_time;
	}

	public void setStart_develop_time(String start_develop_time) {
		this.start_develop_time = start_develop_time;
	}

	public String getUE_start_time() {
		return UE_start_time;
	}

	public void setUE_start_time(String uE_start_time) {
		UE_start_time = uE_start_time;
	}

	public String getUE_end_time() {
		return UE_end_time;
	}

	public void setUE_end_time(String uE_end_time) {
		UE_end_time = uE_end_time;
	}

	public String getUI_start_time() {
		return UI_start_time;
	}

	public void setUI_start_time(String uI_start_time) {
		UI_start_time = uI_start_time;
	}

	public String getUI_end_time() {
		return UI_end_time;
	}

	public void setUI_end_time(String uI_end_time) {
		UI_end_time = uI_end_time;
	}

	public String getApp_start_time() {
		return app_start_time;
	}

	public void setApp_start_time(String app_start_time) {
		this.app_start_time = app_start_time;
	}

	public String getApp_end_time() {
		return app_end_time;
	}

	public void setApp_end_time(String app_end_time) {
		this.app_end_time = app_end_time;
	}

	public String getQianduan_start_time() {
		return qianduan_start_time;
	}

	public void setQianduan_start_time(String qianduan_start_time) {
		this.qianduan_start_time = qianduan_start_time;
	}

	public String getQianduan_end_time() {
		return qianduan_end_time;
	}

	public void setQianduan_end_time(String qianduan_end_time) {
		this.qianduan_end_time = qianduan_end_time;
	}

	public String getDevelop_start_time() {
		return develop_start_time;
	}

	public void setDevelop_start_time(String develop_start_time) {
		this.develop_start_time = develop_start_time;
	}

	public String getDevelop_end_time() {
		return develop_end_time;
	}

	public void setDevelop_end_time(String develop_end_time) {
		this.develop_end_time = develop_end_time;
	}

	public String getLiantiao_start_time() {
		return liantiao_start_time;
	}

	public void setLiantiao_start_time(String liantiao_start_time) {
		this.liantiao_start_time = liantiao_start_time;
	}

	public String getLiantiao_end_time() {
		return liantiao_end_time;
	}

	public void setLiantiao_end_time(String liantiao_end_time) {
		this.liantiao_end_time = liantiao_end_time;
	}

	public String getTest_start_time() {
		return test_start_time;
	}

	public void setTest_start_time(String test_start_time) {
		this.test_start_time = test_start_time;
	}

	public String getTest_end_time() {
		return test_end_time;
	}

	public void setTest_end_time(String test_end_time) {
		this.test_end_time = test_end_time;
	}
}
