<div class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">
                	<strong>
                		<#if action="create">
                			创建
                		<#else>
                			编辑
                		</#if>
				    	定时任务
                	</strong>
                </h4>
            </div>
            <form id="inputForm" action="${cdnBaseUrl}/cron/save.html?ajax=true" class="form-horizontal" role="form">
            	<input type="hidden" name="id" value="${cron.id!}"/>
	            <div class="modal-body">
	                <div class="row">
	                    <div class="col-lg-12">
		                    <div class="form-group">
		                        <label class="col-sm-2 control-label">Cron名称</label>
		                        <div class="col-sm-8">
		                            <input name="cronName" class="form-control required" placeholder="Cron名称" value="${cron.cronName!}" />
		                        </div>
		                    </div>
		                    <div class="form-group">
		                        <label class="col-sm-2 control-label">执行方法</label>
		                        <div class="col-sm-8">
		                            <input name="serviceName" class="form-control required" placeholder="执行方法" value="${cron.serviceName!}"/>
		                        </div>
		                    </div>
		                    <div class="form-group">
		                        <label class="col-sm-2 control-label">执行策略</label>
		                        <div class="col-sm-8">
		                            <input name="cronExpression" class="form-control required" placeholder="执行策略" value="${cron.cronExpression!}"/>
		                        </div>
		                    </div>
		                    <div class="form-group">
		                        <label class="col-sm-2 control-label">ip限制</label>
		                        <div class="col-sm-8">
		                            <input name="limitIp" class="form-control required" placeholder="ip限制" value="${cron.limitIp!}"/>
		                        </div>
		                    </div>
		                    <div class="form-group">
		                        <label class="col-sm-2 control-label">描述</label>
		                        <div class="col-sm-8">
		                            <input name="cronDesc" class="form-control" placeholder="描述" value="${cron.cronDesc!}"/>
		                        </div>
		                    </div>
		                    <div class="form-group">
		                        <label class="col-sm-2 control-label">分组</label>
		                        <div class="col-sm-8">
		                            <input name="group" class="form-control required" placeholder="分组" value="${cron.group!}"/>
		                        </div>
		                    </div>
		                    <div class="form-group">
		                        <label class="col-sm-2 control-label">启动执行</label>
		                        <div class="col-sm-8">
		                            <input name="fireOnStartUp" class="form-control" placeholder="启动执行" value="${cron.fireOnStartUp!}"/>
		                        </div>
		                    </div>
		            	</div>
	                </div>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-warning" data-dismiss="modal">关闭</button>
	                <button type="submit" class="btn btn-success">保存</button>
	            </div>
            </form>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog --> 
</div>