/**
 * creator:bjlantianyou
 */
$(function() {
	$('ul li').click(function() {
		// 把之前已有的active去掉
		$('.active').removeClass('active');
		// 当前点击的li加上class
		$(this).addClass("active");
	});
})

/**
 * 去除字符串左右两端指定字符 使用方法：http://www.jb51.net/article/101338.htm
 */
String.prototype.trim = function(char, type) {
	if (char) {
		if (type == 'left') {
			return this.replace(new RegExp('^\\' + char + '+', 'g'), '');
		} else if (type == 'right') {
			return this.replace(new RegExp('\\' + char + '+$', 'g'), '');
		}
		return this.replace(
				new RegExp('^\\' + char + '+|\\' + char + '+$', 'g'), '');
	}
	return this.replace(/^\s+|\s+$/g, '');
};

function dealNull(str) {
	var target_jira_url = "http://jira.ms.netease.com/browse/" + $("#jiraNum").val();
	if (str == null || str == "") {
		return '<a href=' + target_jira_url + ' target="_blank"><font color="red">现在去完善</font></a>';
	} else {
		return str;
	}
}

function dealRelateStaff(str,docUrl) {
	var target_jira_url = "http://jira.ms.netease.com/browse/" + $("#jiraNum").val();
	if (docUrl != null && docUrl != "" && docUrl.indexOf("http") == -1) {
		return "本期没有相关人员";
	}
	else if (str == null || str == "") {
		return '<a href=' + target_jira_url + ' target="_blank"><font color="red">现在去完善</font></a>';
	} else {
		return str;
	}
}

function dealDocUrl(str,name) {
	var target_jira_url = "http://jira.ms.netease.com/browse/"
		+ $("#jiraNum").val();
	if (str == null || str == "") {
		return '<a href=' + target_jira_url + ' target="_blank" class="apopdoc" title="注意" data-toggle="popover" data-trigger="hover"><font color="red">现在去完善</font></a>';
	}
	else if (str.indexOf("http") == -1) {
		return '本期没有相关文档';
	}
	else {
		return "<a href='"+str+"' target='_blank'>"+name+"</a>";
	}
}

function sendPopoTo(emailAddress,message) {
	var target_jira_url = "http://jira.ms.netease.com/browse/"
		+ $("#jiraNum").val();
	var message = "您有一个JIRA缺少"+message+",请在\"备注\"中按\"["+message+"|url]\"的格式补充信息，JIRA地址："+target_jira_url;
	$.ajax({
		type: "POST",
		url: "http://app.qa.ms.netease.com/popo/sendPopoMsg.html",
		data: {"to":emailAddress, "content":message},
		dataType: "json",
		success: function(data){
			alert("发送成功");
		}
	});
}

function minusTime(endTime, startTime) {
	if (endTime == null || startTime == null) {
		return 0;
	}
	var time = ((new Date(endTime)) - (new Date(startTime)) + 24*3600000) / 3600000;
	return time;
}

function countSubstr(str, substr, isIgnore) {
	var count;
	var reg = "";
	if (isIgnore == true) {
		reg = "/" + substr + "/gi";
	} else {
		reg = "/" + substr + "/g";
	}
	reg = eval(reg);
	if (str.match(reg) == null) {
		count = 0;
	} else {
		count = str.match(reg).length + 1;
	}
	return count;
}

function spinnerInit() {
    var opts = {
        lines: 13, // The number of lines to draw
        length: 7, // The length of each line
        width: 4, // The line thickness
        radius: 10, // The radius of the inner circle
        corners: 1, // Corner roundness (0..1)
        rotate: 0, // The rotation offset
        color: '#000', // #rgb or #rrggbb
        speed: 1, // Rounds per second
        trail: 60, // Afterglow percentage
        shadow: false, // Whether to render a shadow
        hwaccel: false, // Whether to use hardware acceleration
        className: 'spinner', // The CSS class to assign to the spinner
        zIndex: 2e9, // The z-index (defaults to 2000000000)
        top: 'auto', // Top position relative to parent in px
        left: 'auto', // Left position relative to parent in px
        visibility: true
    };

    var target = document.getElementById('spinnerContainer');
    //target.style.display = "block";
    var spinner = new Spinner(opts).spin(target);
}

function searchJiraInfo(event) {
	var data = {};
	data.issueNum = $("#jiraNum").val();
	var target_jira_url = "http://jira.ms.netease.com/browse/" + $("#jiraNum").val();
	var theobj = event.srcElement ? event.srcElement : event.target;
	var choice = theobj.id;
	if (data.issueNum == "" || data.userName == "" || data.passWord == "") {
		alert("请先输入JIRA号！");
	} else {
		bootbox.dialog({
		    message:'<div style="text-align:center;height:150px;padding-top:65px;" id="spinMessage">关联JIRA信息统计中...</div>' +
		        '<div id="saving"></div>',
		    buttons:{
		        ok:{
		            label: '确定',
		            className: 'hidden savingBtn'
		        }
		    }
		});
		var spinner = new Spinner({radius: 30, length: 0, width: 10, color: '#286090', trail: 40}).spin(document.getElementById('saving'));
		$.ajax({
			url : "/quality/jiraInfo/jiraSearch.html",
			type : "POST",
			data : data,
			async : true,
			dataType : "json",
			success : function(json) {
				if (json.retCode == 200) {
					var title = "<h1 style='font-size:25px'>"
							+ json.summary + "<small>&nbsp&nbsp<a target=\"_blank\" href=\""+target_jira_url+"\">"
							+ json.issueNum + "</a>"+"</small></h1>";
					$('#title').html(title);
					if (choice == "jira_doc") {
						var table_content = '<table class="table table-hover">'
								+ '<thead>'
								+ '<tr>'
								+ '<th>文档名称</th>'
								+ '<th>链接地址</th>'
								+ '<th>负责人员</th>'
								+ '</tr>'
								+ '</thead>'
								+ '<tbody>'
								+ '<tr>'
								+ '<td width="200">产品需求文档</td>'
								+ '<td width="200" id="productDocUrl">'
								+ dealDocUrl(json.docInfo.value.productDocUrl,"产品需求文档")
								+ '</td>'
								+ '<td>'
								+ dealRelateStaff(json.jiraInfo.product_a,json.docInfo.value.productDocUrl)
								+ '</td>'
								+ '</tr>'
								+ '<td width="200">前端配置文档</td>'
								+ '<td width="200" id="webDocUrl">'
								+ dealDocUrl(json.docInfo.value.webDocUrl,"前端配置文档")
								+ '</td>'
								+ '<td>'
								+ dealRelateStaff(json.jiraInfo.qianduans_a,json.docInfo.value.webDocUrl)
								+ '</td>'
								+ '</tr>'
								+ '<tr>'
								+ '<td width="200">后台接口文档</td>'
								+ '<td width="200" id="devDocUrl">'
								+ dealDocUrl(json.docInfo.value.interDocUrl,"后台接口文档")
								+ '</td>'
								+ '<td>'
								+ dealRelateStaff(json.jiraInfo.developers_a,json.docInfo.value.interDocUrl)
								+ '</td>'
								+ '</tr>'
								+ '<tr>'
								+ '<td width="200">测试说明文档</td>'
								+ '<td width="200" id="testDocUrl">'
								+ dealDocUrl(json.docInfo.value.testDocUrl,"测试说明文档")
								+ '</td>'
								+ '<td>'
								+ dealRelateStaff(json.jiraInfo.assignees_a,json.docInfo.value.testDocUrl)
								+ '</td>'
								+ '</tr>'
								+ '</tbody>'
								+ '</table>';
						$('#table_content').html(table_content);
						// 生成图表
						document.getElementById('myChart').innerHTML = "";
						spinner.spin();
						$("#spinMessage").html('<font style="font-size:20px" color="green"><strong>统计完成!</strong></font>');
						var savingBtn = document.getElementsByClassName('savingBtn')[0];
						setTimeout(function(){
						    savingBtn.click();
						}, 1000);
					} else if (choice == "jira_staff"
							|| choice == "jira_search") {
						$('.active').removeClass('active');
						$('#index_li').addClass("active");
						var table_content = '<table class="table table-hover">'
								+ '<thead>'
								+ '<tr>'
								+ '<th>职责</th>'
								+ '<th>负责人员</th>'
								+ '</tr>'
								+ '</thead>'
								+ '<tbody>'
								+ '<tr>'
								+ '<td width="200">前端人员</td>'
								+ '<td id="qianduans">'
								+ dealNull(json.jiraInfo.qianduans)
								+ '</td>'
								+ '</tr>'
								+ '<tr>'
								+ '<td width="200">开发人员</td>'
								+ '<td id="developers">'
								+ dealNull(json.jiraInfo.developers)
								+ '</td>'
								+ '<tr>'
								+ '<td width="200">QA</td>'
								+ '<td id="assignees">'
								+ dealNull(json.jiraInfo.assignees)
								+ '</td>'
								+ '</tr>'
								+ '<tr>'
								+ '<td width="200">产品需求人员</td>'
								+ '<td id="product">'
								+ dealNull(json.jiraInfo.product)
								+ '</td>'
								+ '</tr>'
								+ '</tbody>'
								+ '</table>';
						$('#table_content').html(table_content);
						// 生成图表
						var myChart = echarts.init(document
								.getElementById('myChart'));
						var qianduans_sig = 0;
						var developers_sig = 0;
						var assignees_sig = 0;
						var product_sig = 0;
						($('#qianduans').html() == "" || $('#qianduans')
								.html().indexOf("现在去完善") > 0) ? qianduans_sig = 0
								: qianduans_sig = 1;
						($('#developers').html() == "" || $(
								'#developers').html().indexOf("现在去完善") > 0) ? developers_sig = 0
								: developers_sig = 1;
						($('#assignees').html() == "" || $('#assignees')
								.html().indexOf("现在去完善") > 0) ? assignees_sig = 0
								: assignees_sig = 1;
						($('#product').html() == "" || $('#product')
								.html().indexOf("现在去完善") > 0) ? product_sig = 0
								: product_sig = 1;
						var qianduans = 0;
						var developers = 0;
						var assignees = 0;
						var product = 0;
						if (qianduans_sig == 1) {
							countSubstr($('#qianduans').html(), ',',
									true) == 0 ? qianduans = 1
									: qianduans = countSubstr($(
											'#qianduans').html(), ',',
											true);
						}
						if (developers_sig == 1) {
							countSubstr($('#developers').html(), ',',
									true) == 0 ? developers = 1
									: developers = countSubstr($(
											'#developers').html(), ',',
											true);
						}
						if (assignees_sig == 1) {
							countSubstr($('#assignees').html(), ',',
									true) == 0 ? assignees = 1
									: assignees = countSubstr($(
											'#assignees').html(), ',',
											true);
						}
						if (product_sig == 1) {
							countSubstr($('#product').html(), ',', true) == 0 ? product = 1
									: product = countSubstr($(
											'#product').html(), ',',
											true);
						}
						var option = {
							title : {
								text : 'JIRA人员信息',
								subtext : '完善度显示',
								x : 'center'
							},
							tooltip : {
								trigger : 'item',
								formatter : "{a} <br/>{b} : {c} ({d}%)"
							},
							legend : {
								orient : 'vertical',
								left : 'left',
								data : [ "前端人员信息", "开发人员信息", "测试人员信息",
										"产品人员信息" ]
							},
							series : [ {
								name : '人员信息',
								type : 'pie',
								radius : '55%',
								center : [ '50%', '60%' ],
								data : [ {
									value : qianduans,
									name : '前端人员信息'
								}, {
									value : developers,
									name : '开发人员信息'
								}, {
									value : assignees,
									name : '测试人员信息'
								}, {
									value : product,
									name : '产品人员信息'
								} ],
								itemStyle : {
									emphasis : {
										shadowBlur : 10,
										shadowOffsetX : 0,
										shadowColor : 'rgba(0, 0, 0, 0.5)'
									}
								}
							} ],
							color : [ '#7FFF00', '#2f4554', '#61a0a8',
									'#d48265', '#91c7ae', '#749f83',
									'#ca8622', '#bda29a', '#6e7074',
									'#546570', '#c4ccd3' ],
						};
						myChart.setOption(option);
						spinner.spin();
						$("#spinMessage").html('<font style="font-size:20px" color="green"><strong>统计完成!</strong></font>');
						var savingBtn = document.getElementsByClassName('savingBtn')[0];
						setTimeout(function(){
						    savingBtn.click();
						}, 1000);
					} else {
						var table_content = '<table class="table table-hover">'
								+ '<thead>'
								+ '<tr>'
								+ '<th>阶段</th>'
								+ '<th>用时</th>'
								+ '</tr>'
								+ '</thead>'
								+ '<tbody>'
								+ '<tr>'
								+ '<td width="200">需求设计阶段</td>'
								+ '<td>'
								+ dealNull(json.jiraInfo.ue_start_time)
								+ ' 到 '
								+ dealNull(json.jiraInfo.ue_end_time)
								+ '</td>'
								+ '</tr>'
								+ '<tr>'
								+ '<td width="200">视觉设计阶段</td>'
								+ '<td>'
								+ dealNull(json.jiraInfo.ui_start_time)
								+ ' 到 '
								+ dealNull(json.jiraInfo.ui_end_time)
								+ '</td>'
								+ '</tr>'
								+ '<tr>'
								+ '<td width="200">客户端开发阶段</td>'
								+ '<td>'
								+ dealNull(json.jiraInfo.app_start_time)
								+ ' 到 '
								+ dealNull(json.jiraInfo.app_end_time)
								+ '</td>'
								+ '</tr>'
								+ '<tr>'
								+ '<td width="200">前端开发阶段</td>'
								+ '<td>'
								+ dealNull(json.jiraInfo.qianduan_start_time)
								+ ' 到 '
								+ dealNull(json.jiraInfo.qianduan_end_time)
								+ '</td>'
								+ '</tr>'
								+ '<tr>'
								+ '<td width="200">后台开发阶段</td>'
								+ '<td>'
								+ dealNull(json.jiraInfo.develop_start_time)
								+ ' 到 '
								+ dealNull(json.jiraInfo.develop_end_time)
								+ '</td>'
								+ '</tr>'
								+ '<tr>'
								+ '<td width="200">联调阶段</td>'
								+ '<td>'
								+ dealNull(json.jiraInfo.liantiao_start_time)
								+ ' 到 '
								+ dealNull(json.jiraInfo.liantiao_end_time)
								+ '</td>'
								+ '</tr>'
								+ '<tr>'
								+ '<td width="200">测试阶段</td>'
								+ '<td>'
								+ dealNull(json.jiraInfo.test_start_time)
								+ ' 到 '
								+ dealNull(json.jiraInfo.test_end_time)
								+ '</td>'
								+ '</tr>'
								+ '<tr>'
								+ '<td width="200">上线阶段</td>'
								+ '<td>'
								+ dealNull(json.jiraInfo.test_end_time)
								+ ' 到 现在</td>'
								+ '</tr>'
								+ '</tbody>'
								+ '</table>';
						$('#table_content').html(table_content);
						// 生成图表
						var myChart = echarts.init(document
								.getElementById('myChart'));
						var UETime = minusTime(
								json.jiraInfo.ue_end_time,
								json.jiraInfo.ue_start_time);
						var UITime = minusTime(
								json.jiraInfo.ui_end_time,
								json.jiraInfo.ui_start_time);
						var AppTime = minusTime(
								json.jiraInfo.app_end_time,
								json.jiraInfo.app_start_time);
						var QianduanTime = minusTime(
								json.jiraInfo.qianduan_end_time,
								json.jiraInfo.qianduan_start_time);
						var DevTime = minusTime(
								json.jiraInfo.develop_end_time,
								json.jiraInfo.develop_start_time);
						var LiantiaoTime = minusTime(
								json.jiraInfo.liantiao_end_time,
								json.jiraInfo.liantiao_start_time);
						var TestTime = minusTime(
								json.jiraInfo.test_end_time,
								json.jiraInfo.test_start_time);
						var option = {
							title : {
								text : '各阶段耗时信息柱状显示'
							},
							tooltip : {},
							legend : {
								data : [ '耗时（单位：h）' ]
							},
							xAxis : {
								data : [ "需求设计阶段", "视觉设计阶段", "前端开发阶段",
										"后台开发阶段", "客户端开发阶段", "联调阶段",
										"测试阶段" ]
							},
							yAxis : {
								min : 0,
								max : 500,
								splitNumber : 1
							},
							series : [ {
								name : '耗时（单位：h）',
								type : 'bar',
								data : [ UETime, UITime, QianduanTime,
										DevTime, AppTime, LiantiaoTime,
										TestTime ]
							} ],
							color : [ '#7FFF00', '#2f4554', '#61a0a8',
									'#d48265', '#91c7ae', '#749f83',
									'#ca8622', '#bda29a', '#6e7074',
									'#546570', '#c4ccd3' ],
						};
						myChart.setOption(option);
						spinner.spin();
						$("#spinMessage").html('<font style="font-size:20px" color="green"><strong>统计完成!</strong></font>');
						var savingBtn = document.getElementsByClassName('savingBtn')[0];
						setTimeout(function(){
						    savingBtn.click();
						}, 1000);
					}
				} else if (json.retCode == -1) {
					spinner.spin();
					$("#spinMessage").html('<font style="font-size:20px" color="green"><strong>统计完成!</strong></font>');
					var savingBtn = document.getElementsByClassName('savingBtn')[0];
					setTimeout(function(){
					    savingBtn.click();
					}, 1000);
					alert(json.tip);
				}
				$('.apopover').popover({content:'点击我给他发送popo消息提醒！'});
				$('.apopdoc').popover({content:'在JIRA备注中注明文档地址，规范示例如下（在JIRA中编写带链接的文字格式为：[文字|链接]）。“文字”规范为：产品需求文档、前端配置文档、后台接口文档、测试说明文档，“链接”规范为相应的文档所在的online url。示例详见首页“规范介绍”。'});
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert(XMLHttpRequest.status);
				alert(XMLHttpRequest.readyState);
				alert(textStatus);
			}
		});
	}
}

function searchJiraRelateInfo(event) {
	$('.apopover').popover({content:'点我给他发popo消息提醒！'})
	var data = {};
	data.issueNum = $("#jiraNum").val();
	var target_jira_url = "http://jira.ms.netease.com/browse/" + $("#jiraNum").val();
	var theobj = event.srcElement ? event.srcElement : event.target;
	var choice = theobj.id;
	if (data.issueNum == "" || data.userName == "" || data.passWord == "") {
		alert("信息输入不完整，无法进行查询。");
	} else {
		bootbox.dialog({
			message:'<div style="text-align:center;height:150px;padding-top:65px;" id="spinMessage">关联JIRA信息统计中...</div>' +
				'<div id="saving"></div>',
			buttons:{
				ok:{
					label: '确定',
					className: 'hidden savingBtn'
				}
			}
		});
		var spinner = new Spinner({radius: 30, length: 0, width: 10, color: '#286090', trail: 40}).spin(document.getElementById('saving'));
		$.ajax({
			url : "/quality/jiraInfo/jiraRelateInfo.html",
			type : "POST",
			data : data,
			async : true,
			dataType : "json",
			success : function(json) {
				if (json.retCode == 200) {
					var title = "<h1 style='font-size:25px'>"
						+ json.summary + "<small>&nbsp&nbsp<a target=\"_blank\" href=\""+target_jira_url+"\">"
						+ json.issueNum + "</a>"+"</small></h1>";
					$('#title').html(title);
					var table_content = '<table class="table table-striped" style="word-break:break-all; word-wrap:break-all;">'
							+ '<thead>'
							+ '<tr>'
							+ '<th>信息名称</th>'
							+ '<th>内容</th>'
							+ '</tr>'
							+ '</thead>'
							+ '<tbody>'
							+ '<tr>'
							+ '<td width="200">关联的JIRA</td>'
							+ '<td id="issueLinks">'
							+ dealNull(json.jiraInfo.issue_links)
							+ '</td>' + '</tr>'
							'</tbody>' + '</table>';
					$('#table_content').html(table_content);
					// 生成图表
					document.getElementById('myChart').innerHTML = "";
				} else if (json.retCode == -1) {
					alert(json.tip);
				}
				spinner.spin();
				$("#spinMessage").html('<font style="font-size:20px" color="green"><strong>统计完成!</strong></font>');
				var savingBtn = document.getElementsByClassName('savingBtn')[0];
				setTimeout(function(){
					savingBtn.click();
				}, 1000);
				$('.apopover').popover({content:'点击我给他发送popo消息提醒！'});
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert(XMLHttpRequest.status);
				alert(XMLHttpRequest.readyState);
				alert(textStatus);
				spinner.spin();
				$("#spinMessage").html('<font style="font-size:20px" color="green"><strong>统计完成!</strong></font>');
				var savingBtn = document.getElementsByClassName('savingBtn')[0];
				setTimeout(function(){
					savingBtn.click();
				}, 1000);
			}
		});
	}
}