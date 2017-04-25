<!--首页 -->
<#include "../inc/core.ftl">
<@htmHead title="质量保障系统" 
otherJs=["/js/quality/jiraInfo.js", "/js/quality/myJiraInfo.js", "/js/common/spin.js", "/js/common/bootbox.js", "/js/common/echarts.js", "/js/common/intro.js"] 
otherCss=["/css/common/introjs.css","/css/quality/jiraInfo.css"]
/>
<@htmlNavHead activeName="myJiraInfo"/> 
<div id="page-wrapper">
	<div class="container-fluid">
		<div class="row-fluid">
			<div id="title" class="span6 page-header">
				<h1 style="font-size:25px">分派给我未完成的<small> JIRA任务统计</small></h1>
			</div>
		</div>
	</div>
	<table class="table table-hover">
		<thead>
			<tr><th>JIRA标题</th></tr>
		</thead>
		<tbody id="">
			<#list searchResult as result>
				<tr><td><a href="#" onclick="searchIssue('${result.issueKey}')"> ${result.issueSummary} </a></td></tr>
			</#list>
		</tbody>
	</table>
</div>
<script>
	$("#search").css('display','none');
</script>
<@htmlNavFoot />
<@htmlFoot/>

