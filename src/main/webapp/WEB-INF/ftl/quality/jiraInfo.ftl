<!--首页 -->
<#include "../inc/core.ftl">
<@htmHead title="质量保障系统" 
otherJs=["/js/quality/jiraInfo.js", "/js/quality/myJiraInfo.js", "/js/common/spin.js", "/js/common/bootbox.js", "/js/common/echarts.js", "/js/common/intro.js"] 
otherCss=["/css/common/introjs.css","/css/quality/jiraInfo.css"]
/>
<@htmlNavHead activeName="jiraInfo"/> 
<div id="page-wrapper">
	<div class="container-fluid">
		<div class="row-fluid">
			<div id="title" class="span6 page-header">
			    <h1 style="font-size:25px">JIRA标题
			        <small>JIRA号</small>
			    </h1>
			</div>
		</div>
	</div><!-- /.row -->
	<!--分页导航-->
	<ul class="nav nav-tabs nav-justified">
		<li id="index_li"><a id="jira_staff" onClick="searchJiraInfo(event)">人员</a></li>
		<li><a id="jira_schedule" onClick="searchJiraInfo(event)">进度</a></li>
		<li data-intro='点这里对JIRA信息进行分类统计!'><a id="jira_doc" onClick="searchJiraInfo(event)">文档</a></li>
		<li><a id="jira_info" onClick="searchJiraRelateInfo(event)">信息</a></li>
 	</ul>
 	<!--表格-->
 	<div id="table_content"></div>
 	<!--为ECharts准备一个具备大小（宽高）的Dom-->
	<div id="myChart" style="width:1000px;height:400px;"></div>
</div>
<script>
	$("#search").css('display','block'); 
	$(document).ready(function(){
		//判断是不是从别的页面过来的
		if(window.location.search != ""){
			$("#jira_search").click();
		} else {
			introJs().start();
		}
	});
</script>
<@htmlNavFoot />
<@htmlFoot/>

