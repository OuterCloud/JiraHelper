<#compress>
<#--
	简化登录判断
	仅仅用于服务器端ftl输出页面使用
	简化cdn版本控制
-->
<#assign cdnFileVersion = versionId!"0"/>
<#--欲定义-->
<#setting locale="zh_CN">
<#setting url_escaping_charset="UTF-8">
<#--
输出js文件 / css文件，含版本号
-->
<#macro jsFile file=[] file2=[]>
<#list file2 as x><script src="${cdnBaseUrl}${x}"></script>
</#list>
<#--<#list file as x><script src="${cdnBaseUrl}${x}?v=${cdnFileVersion}"></script>
</#list>-->
</#macro>
<#macro cssFile file=[] file2=[]>
<#list file2 as x><link rel="stylesheet" href="${cdnBaseUrl}${x}"/>
</#list>
<#--<#list file as x><link rel="stylesheet" href="${cdnBaseUrl}${x}?v=${cdnFileVersion}"/>
</#list>-->
</#macro>

<#--
文档声明/head
支持对head内容项进行修改
-->
<#macro htmHead title="" css=[] js=[] 
otherJs=["/js/quality/jiraInfo.js","/js/quality/myJiraInfo.js", "/js/common/spin.js", "/js/common/bootbox.js", "/js/common/echarts.js", "/js/common/intro.js","/js/chuncai/jquery.js","/js/chuncai/chuncai.js"] 
otherCss=["/css/common/introjs.css","/css/quality/jiraInfo.css"]
>
<!DOCTYPE HTML>
<html lang="zh-CN">
<head>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
	<title>${title}</title>
	
	<link rel="icon" type="image/x-icon" href="${cdnBaseUrl}/img/favicon.ico">
	<!-- Bootstrap Core CSS -->
    <link href="${cdnBaseUrl}/css/common/bootstrap.css" rel="stylesheet">
    <!-- MetisMenu CSS -->
    <link href="${cdnBaseUrl}/css/common/metisMenu.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${cdnBaseUrl}/css/common/sb-admin-meteor.css" rel="stylesheet">
    <!-- Custom Fonts -->
    <link href="${cdnBaseUrl}/css/common/font-awesome.css" rel="stylesheet">
    
    <!-- jQuery -->
    <script src="${cdnBaseUrl}/js/common/jquery-1.11.3.js"></script>
    <!-- Bootstrap Core JavaScript -->
    <script src="${cdnBaseUrl}/js/common/bootstrap.js"></script>
    <!-- Metis Menu Plugin JavaScript -->
    <script src="${cdnBaseUrl}/js/common/metisMenu.js"></script>
    <!-- Custom Theme JavaScript -->
    <script src="${cdnBaseUrl}/js/common/sb-admin-2.js"></script>
	<@cssFile file=css file2=otherCss/>
	<@jsFile file=js file2=otherJs/>
<#nested>
</head>
<body>
    <!-- navbar -->
    <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
        <div class="navbar-header">
            <a class="navbar-brand"  id="logo">
         	<img style="display:inline;" src="/img/logo.png">
        		质量保障系统
      	</a>
        </div>
        <!-- /.navbar-header -->
        <ul class="nav navbar-top-links navbar-right">
            <#if userName?exists>
	        	<input class="text hidden" id="userEngName" value="${userCorpMail!''}">
	            <li class="dropdown">
	                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
	                    <i class="fa fa-user fa-fw"></i>  <i class="fa fa-caret-down"></i>
	                </a>
	                <ul class="dropdown-menu dropdown-user">
	                    <li><a href="#"><i class="fa fa-user fa-fw"></i>${userName}</a>
	                    </li>
	                    <li class="divider"></li>
	                    <li><a href="${cdnBaseUrl}/logout.html"><i class="fa fa-sign-out fa-fw"></i>注销</a>
	                    </li>
	                </ul>
	                <!-- /.dropdown-user -->
	            </li>
	            <!-- /.dropdown -->
		     <#else>
            	<li><a href="${cdnBaseUrl}/login/signIn.html">登录</a></li>
		     </#if>
        </ul>
        <!-- /.navbar-top-links -->
        <!--搜索区域-->
        <div id="search" style="display:none">
			<form data-intro='输入JIRA号，开始统计吧!' class="navbar-form navbar-right" role="search">
				<div class="form-group">
					<#if issueKey?exists>
					<input style="width:300px" value="${issueKey}" id="jiraNum" type="text" class="form-control" placeholder="请输入JIRA号，例如：LOTTERY-5744">
					<#else>
					<input style="width:300px" value="" id="jiraNum" type="text" class="form-control" placeholder="请输入JIRA号，例如：LOTTERY-5744">
					</#if>
				</div>
				<button class="btn btn-primary" type="button" id="jira_search" onClick="searchJiraInfo(event)">进行统计</button>
			</form>
		</div>
    </nav>
    <!-- /.navbar-static-side -->
</#macro>

<#macro htmlNavHead isShow=1 activeName="1">
	<#if isShow == 1>
		<!-- /.navbar-static-side -->
	    <div class="navbar-default sidebar" role="navigation">
	        <div class="sidebar-nav navbar-collapse">
	            <ul class="nav" id="side-menu">
	                <#if activeName == "index">
	            	<li class="active">
	            	<#else>
	                <li>
	                </#if>
	                	<a href="${cdnBaseUrl}/index.html">首页</a>
	                </li> 
	                <li> 
	                	<a href="#"><i class="fa fa-wrench fa-fw"></i>JIRA任务信息</a>
	                	<ul class="nav nav-second-level">
		        			<#if activeName == "jiraInfo">
	            			<li class="active">
	            			<#else>
		               		<li>
		                	</#if>
	                            <a href="${cdnBaseUrl}/quality/jiraInfo.html"><i class="fa fa-space-shuttle"></i>指定任务统计</a>
	                        </li>
	                        <#if activeName == "myJiraInfo">
	            			<li class="active">
	            			<#else>
		               		<li>
		                	</#if>
	                            <a href="${cdnBaseUrl}/quality/myJiraInfo.html" onclick="myJiraInfo()"><i class="fa fa-tags"></i>分派给我未完成的任务统计</a>
	                        </li>
	                    </ul>
	                    <!-- /.nav-second-level -->
	                </li>
	                <#if activeName == "bugInfo">
	            	<li class="active">
	            	<#else>
	                <li>
	                </#if>
	                	<a href="${cdnBaseUrl}/quality/bugInfo.html"><i class="fa fa-table fa-fw"></i>BUG统计信息</a>
	                </li>
	            </ul>
	        </div>
	        <!-- /.sidebar-collapse -->
	    </div>
	</#if>
</#macro>

<#macro htmlNavFoot isShow=1>
	<#if isShow == 1>
        <footer class="footer">
			<div class="container">
				<p class="text-muted">Powered by Netease QA© 2015- Netease QA 如有问题请及时联系我们<br>
				</p>
			</div>
	    </footer>
    </#if>
</#macro>

<#macro htmlFoot>
    </body>
</html>
</#macro>
</#compress>