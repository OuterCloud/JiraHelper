<!--首页 -->
<#include "inc/core.ftl">
<@htmHead title="质量保障系统" />
<@htmlNavHead activeName="index"/> 
<div id="page-wrapper" style="min-height: 800px;">
	<div class="row">
		<div class="page-header">
		    <h1>质量保障系统
		        <small>首页介绍</small>
		    </h1>
		</div>
	</div><!-- /.row -->
	<div class="col-lg-12" style="font-size:16px;">
		<div class="jumbotron">
			<div class="container" style="margin-left:20px;">
				<ul class="list-unstyled">
					<li>质量保障系统是网易电商QA团队开发的基于jira的规范使用体系</li>
					<br>
					<li>目的是为了:
						<ul>
							<li>完善jira的信息</li>
							<li>规范jira的使用</li>
						</ul>
					</li>
					<br>
					<li>有问题请联系：
						<ul>
							<li>天外归云</li>
						</ul>
					</li>
					<br>
					<li>友情链接：
						<ul>
							<li><a href="http://jira.ms.netease.com" target="_blank">JIRA</a></li>
						</ul>
					</li>
					<br>
					<li>规范介绍：
						<ul>
							<li>在JIRA<font color="red">备注</font>中注明文档地址，规范示例如下（在JIRA中编写带链接的文字格式为：[文字|链接]）：</li>
							<ul>
								<li>[产品需求文档|http://www.163.com]</li>
								<li>[前端配置文档|http://www.163.com]</li>
								<li>[后台接口文档|http://www.163.com]</li>
								<li>[测试说明文档|http://www.163.com]</li>
								<li>*如果本期需求不包含相关文档，可以备注例如：[测试说明文档|无]</li>
							</ul>
						</ul>
						<ul>
							<li>在JIRA<font color="red">问题链接</font>中关联相关需求和缺陷。</li>
						</ul>
					</li>
				</ul>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
    $.chuncai();
</script>
<@htmlNavFoot />
<@htmlFoot/>

