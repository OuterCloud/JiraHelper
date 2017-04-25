<!--定时任务 -->
<#include "../inc/core.ftl">
<@htmHead title="Mock公共平台" otherJs=["/js/common/jquery.disable.1.0.js","/js/common/jquery.requestForm.js","/js/common/jquery.form.js","/js/common/jquery.validate.js"]/>
<@htmlNavHead />


	<div id="page-wrapper">
    	<div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">定时任务</h1>
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <!-- /.row -->
        
        <div class="row" style="margin-bottom:10px;">
            <div class="col-lg-12">
                <button class="btn btn-success btn-sm" id="create">新增</button>
                <a class="btn btn-info btn-sm" href="${cdnBaseUrl}/bg/cron/refresh.html" target="_blank">刷新</a>
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <!-- /.row -->
        
        <div class="row">
            <div class="col-lg-12">
            	<div class="panel panel-default">
	            	<div class="table-responsive table-bordered">
	                    <table class="table table-hover">
	                        <thead>
	                            <tr>
	                                <th>名称</th>
	                                <th>执行方法</th>
	                                <th>执行策略</th>
	                                <th>IP限制</th>
	                                <th>描述</th>
	                                <th>分组</th>
	                                <th class="col-sm-1">启动执行</th>
	                                <th>操作</th>
	                            </tr>
	                        </thead>
	                        <tbody>
	                        	<#list cronList as cron>
		                            <tr>
		                                <td>${cron.cronName!}</td>
		                                <td>${cron.serviceName!}</td>
		                                <td>${cron.cronExpression!}</td>
		                                <td>${cron.limitIp!}</td>
		                                <td>${cron.cronDesc!}</td>
		                                <td>${cron.group!}</td>
		                                <td>${cron.fireOnStartUp!}</td>
		                                <td>
		                                	<a href="${cdnBaseUrl}/bg/cron/call/${cron.cronName!}.html?uncheckTime=true&uncheckIp=true" class="btn btn-success btn-sm" target="_blank">执行</a>
		                                	<button class="btn btn-info btn-sm edit" data-id="${cron.id!}">修改</button>
		                                	<a href="${cdnBaseUrl}/cron/delete/${cron.id}.html" class="btn btn-warning btn-sm delete-item">删除</a>
		                                </td>
		                            </tr>
	                        	</#list>
	                        </tbody>
	                    </table>
	                </div>
                </div>
            </div>
            <!-- /.col-lg-12 -->
        </div>
        <!-- /.row -->
    </div>
    <!-- #page-wrapper -->
	<script type="text/javascript">
		$('#create').click(function(){
    		$("#page-wrapper").simpleRequestForm({
    			ajaxUrl:"${cdnBaseUrl}/cron/create.html"
    		});
    		
    	});
		
		$('.edit').click(function(){
	   		$("#page-wrapper").simpleRequestForm({
	   			ajaxUrl:"${cdnBaseUrl}/cron/edit.html?id=" + $(this).attr("data-id") +"&ajax=true"
	   		});
	   	});
	</script>
<@htmlNavFoot />
<@htmlFoot/>

