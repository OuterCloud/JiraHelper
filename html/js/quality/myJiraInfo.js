/**
 * creator:bjlantianyou
 */
function searchIssue(issueKey) {
	window.location.href="http://quality.qa.ms.netease.com/quality/jiraInfo.html?issueKey="+issueKey;
}

/**
 * 用ajax的方式请求后台
 * 
function myJiraInfo() {
	$("#search").css('display','none'); 
	bootbox
			.dialog({
				message : '<div style="text-align:center;height:150px;padding-top:65px;" id="spinMessage">分派给我未完成的JIRA任务统计中...</div>'
						+ '<div id="saving"></div>',
				buttons : {
					ok : {
						label : '确定',
						className : 'hidden savingBtn'
					}
				}
			});
	var spinner = new Spinner({
		radius : 30,
		length : 0,
		width : 10,
		color : '#286090',
		trail : 40
	}).spin(document.getElementById('saving'));
	$
			.ajax({
				url : "/quality/myJiraInfo.html",
				type : "GET",
				async : true,
				dataType : "json",
				success : function(json) {
					if (json.retCode == 200) {
						var results = json.searchResult;
						var html = "<div class=\"container-fluid\"><div class=\"row-fluid\"><div id=\"title\" class=\"span6 page-header\"><h1 style=\"font-size:25px\">分派给我未完成的<small> JIRA任务统计</small></h1></div></div></div><table class=\"table table-hover\"><thead><tr><th>JIRA标题</th></tr></thead><tbody id=\"\">";
						for (var i = 0; i < results.length; i++) {
							var atag = "<tr><td><a href=\"#\" onclick=\"searchIssue(\'"+results[i].issueKey+"\')\">"
									+ results[i].issueSummary + "</a></td></tr>";
							html += atag;
						}
						html += "</tbody></table>";
						$("#page-wrapper").html(html);
					} else if (json.retCode == -1) {
						alert(json.tip);
					}
					spinner.spin();
					$("#spinMessage")
							.html(
									'<font style="font-size:20px" color="green"><strong>统计完成!</strong></font>');
					var savingBtn = document
							.getElementsByClassName('savingBtn')[0];
					setTimeout(function() {
						savingBtn.click();
					}, 1000);
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					alert(XMLHttpRequest.status);
					alert(XMLHttpRequest.readyState);
					alert(textStatus);
					spinner.spin();
					$("#spinMessage")
							.html(
									'<font style="font-size:20px" color="green"><strong>统计完成!</strong></font>');
					var savingBtn = document
							.getElementsByClassName('savingBtn')[0];
					setTimeout(function() {
						savingBtn.click();
					}, 1000);
				}
			});
}
**/
function myJiraInfo() {
	bootbox
	.dialog({
		message : '<div style="text-align:center;height:150px;padding-top:65px;" id="spinMessage">分派给我未完成的JIRA任务统计中...</div>'
				+ '<div id="saving"></div>',
		buttons : {
			ok : {
				label : '确定',
				className : 'hidden savingBtn'
			}
		}
	});
	var spinner = new Spinner({
	radius : 30,
	length : 0,
	width : 10,
	color : '#286090',
	trail : 40
	}).spin(document.getElementById('saving'));
}