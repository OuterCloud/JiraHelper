/*
 * 动态插入flash组件
 * 2012-04-27 马超 创建
 * 2012-05-04 马超 增加Flash插件探测接口
 *
 * [接口说明]
 * $.easyFlash.insert(dom, option) 将Flash插入到制定的Dom节点中（innerHTML方法）
 * $.easyFlash.write(dom, option) 将Flash插入到页面中（document.write方法）
 * $.easyFlash.support	浏览器是否安装了Flash插件
 * $.easyFlash.version	Flash插件的版本（无插件就是0）
 *
 * [option参数说明]
 * path		: flash路径
 * id		: flash的ID
 * width	: flash的宽度
 * height	: flash的高度
 * vars	: flash初始化传递的参数[可选]
 * bgcolor : flash默认的背景色[可选，默认白色]
 * fullScreen	: 是否允许全屏[可选，默认否]
 * loop	: flahs是否循环播放[可选，默认否]
 * lowVersion : flash最低版本要求，仅对IE有效[可选，默认9.0]
 */
(function(window, $) {
    /*
     * 探测浏览器是否支持Flash插件
     */
    var support = false,
        version = 0,
        detectFlash = function() {
            //navigator.mimeTypes是MIME类型，包含插件信息
            var nav = window.navigator,
                mime = nav.mimeTypes,
                flash, plugin, activeX, regVer = /^[\D]+([^ ]+)/;
            if (mime.length) {
                flash = mime["application/x-shockwave-flash"];
                if (flash) {
                    plugin = flash.enabledPlugin;
                    if (plugin) {
                        support = true;
                        version = +String(nav.plugins["Shockwave Flash"].description).match(regVer)[1];
                    }
                }
                //如果是ie则尝试创建ActiveXObject
            } else if (self.ActiveXObject) {
                try {
                    activeX = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
                    support = true;
                    version = +String(activeX.GetVariable("$version")).match(regVer)[1].replace(/,(\d+).*$/, ".$1");
                } catch (e) {
                    support = false;
                    version = 0;
                }
            }
        },
        /*
         * 创建Flash的html代码
         */
        getHTML = function(path, id, width, height, vars, bgcolor, fullScreen, loop, lowVersion) {
            // return HTML for movie
            var protocal = window.location.href.match(/^[^\/]+\/\//i)[0],
                html = $.browser.msie ? ['<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="', protocal, 'download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=', lowVersion, '" width="',
                    width, '" height="', height, '" id="', id, '" align="middle"><param name="allowScriptAccess" value="always"/><param name="allowFullScreen" value="',
                    fullScreen, '"/><param name="movie" value="',
                    path, '"/><param name="loop" value="', loop, '"/><param name="menu" value="false"/><param name="quality" value="best"/><param name="bgcolor" value="',
                    bgcolor, '"/><param name="flashvars" value="', vars, '"/><param name="wmode" value="transparent"/></object>'
                ] : ['<embed id="', id, '" src="', path, '" loop="', loop, '" menu="false" quality="best" bgcolor="', bgcolor, '" width="',
                    width, '" height="', height, '" name="', id, '" align="middle" allowScriptAccess="always" allowFullScreen="',
                    fullScreen, '" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" flashvars="',
                    vars, '" wmode="transparent"/>'
                ];
            return html.join("");
        },
        /*
         * 将Flash代码插入到制定Dom中
         */
        insertFlash = function(dom, option) {
            //检查参数
            var op = $.extend({}, option || {}),
                html;
            if (!op.path || !op.id || !op.width || !op.height)
                return;
            //检查lowVersion
            op.lowVersion = String(op.lowVersion || "9,0,0,0").replace(/\./g, ",");
            //创建html
            html = getHTML(op.path, op.id, op.width, op.height, op.vars || "", op.bgcolor || "#ffffff", op.fullScreen ? "true" : "false", op.loop ? "true" : "false", op.lowVersion);
            //写入到页面
            dom ? $(dom).eq(0).html(html) : document.write(html);
        };
    // 立即检测浏览器是否是否支持Flash插件以及插件版本
    detectFlash();
    /*
     * 扩展接口
     */
    $.easyFlash = {
        insert: insertFlash,
        write: function(op) {
            insertFlash(null, op);
        },
        support: support,
        version: version
    };
})(window, jQuery);

/**
 * 跨域ajax（flash实现）
 */
(function($) {
    var flashAgent = document.getElementById("ajaxFlashAgent");
    //插入flash
    $(function() {
        if (!flashAgent) {
            $(document.body).append("<div style='position:absolute;left:-99999px;top:-99999px;z-index:1' id='ajaxFlashWrap'></div>");
            $.easyFlash.insert("#ajaxFlashWrap", {
                id: "ajaxFlashAgent",
                path: "/swf/CrossDomain.swf",
                width: 100,
                height: 100
            });
            flashAgent = document.getElementById("ajaxFlashAgent");
        }
    });

    /**
     * 跨域ajax包装接口
     */
    $.AJAX = function(type, url, data, callback) {
        //console.log("AJAX(", type, url, data, callback, ")");

        if (!flashAgent) {
            console.log("not find flashAgent, and wait ...");
            window.setTimeout(function() {
                $.AJAX(type, url, data, callback);
            }, 100);
            return;
        }
        if ($.isFunction(data)) {
            callback = data;
            data = "";
        }
        var callbackName = "flashAJAXCallBack" + (+new Date()) + parseInt(Math.random() * 489);
        window[callbackName] = function(ret) {
            //console.log("call flash OK, ret is ", ret);
            if ($.isFunction(callback)) {
                callback(ret);
            }
            window[callbackName] = undefined;
        };
        var _url = url.replace(/#.+$/, "");
        _url += _url.indexOf("?") > 0 ? "&cache=" : "?cache=";
        _url += +new Date();
        /*console.log("ready call flash, and data is:", 
            _url,
            type,
            callbackName,
            data
        );*/
        try{
            flashAgent.startRequest([
                _url,
                type,
                callbackName,
                data
            ]);
        }catch(e){
            window.setTimeout(function() {
                $.AJAX(type, url, data, callback);
            }, 100);
        }
    };

    $.GET = function(url, data, callback) {
        $.AJAX("get", url, data, callback);
    };
    $.POST = function(url, data, callback) {
        $.AJAX("post", url, data, callback);
    };

console.log("组件加载完毕");

})(window.jQuery);
