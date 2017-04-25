// jQuery Disable plugin version 1.0
// Author: Andreas Nylin, andreas.nylin@gmail.com, http://andreasnylin.com
(function($) {
    $.fn.disable = function(options) {
        var opts = $.extend({}, $.fn.disable.defaults, options);
    
        function disable($this) {
            $this.attr('disabled', 'disabled');
            
            if(opts.cssClass !== '') {
                $this.addClass(opts.cssClass);
            }

            if(opts.disableEveryElement === true){
                $this.find('*').addClass('disabled');
            }
            
            //add mask
            var $loading = $("<span class='loading'><span>").attr('style',
                'left:'+($this.width()/2-8)+'px;'+
                'top:'+($this.height() /2-5)+'px;');
            $("<div>").appendTo($this)
                .addClass(opts.maskClass)
                    .append($loading);
        }
        
        function enable($this) {
            $this.removeAttr('disabled');
            //remove mask
            $this.find("."+opts.maskClass).remove();
            $this.removeClass(opts.cssClass);
            if(opts.disableEveryElement === true){
                $this.find('*').removeClass('disabled');
            }
            
        }
        
        function setEnableTimeout($this) {
            setTimeout(function() {
                enable($this);
            }, opts.enableAfterSeconds * 1000);
        }
        
        //ajax完成后，调用ajaxEnable，ajaxCallback回调处理后续渲染工作
        function setAjaxEnable($this) {
            $.ajax({
                url:opts.ajaxUrl,
                data:opts.ajaxData,
                success:function(data){
                    opts.ajaxCallback(data);
                    enable($this);
                }
            });
        }
        
        return this.each(function() {
            var $this = $(this);
            
            disable($this);
            
            if(opts.enableAfterSeconds > 0) {
                setEnableTimeout($this);
            }
            
            if(opts.enableOnAjaxComplete || opts.enableOnAjaxSuccess) {
                setAjaxEnable($this);
            }
         });
     };
  
    $.fn.disable.defaults = {
        cssClass: 'js-disabled',
        maskClass: 'js-disabled-mask',
        enableAfterSeconds: 0,
        enableOnAjaxComplete: false,
        enableOnAjaxSuccess: false,
        disableEveryElement: false,//把每个元素的class都设置为.diabled
        ajaxUrl: null,
        ajaxResponseText: null,
        ajaxCallback:null
    };
})(jQuery);




(function($) {
    $.fn.disable_restore = function(options) {
        var opts = $.extend({}, $.fn.disable.defaults, options);
        function enable($this) {
            $this.removeAttr('disabled');
            //remove mask
            $this.find("."+opts.maskClass).remove();
            $this.removeClass(opts.cssClass);
            $this.find('*').removeClass('disabled');
        }
        
        return this.each(function() {
            var $this = $(this);
            
            enable($this);
         });
     };
  
    $.fn.disable.defaults = {
        cssClass: 'js-disabled',
        maskClass: 'js-disabled-mask',
        enableAfterSeconds: 0,
        enableOnAjaxComplete: false,
        enableOnAjaxSuccess: false,
        disableEveryElement: false,//把每个元素的class都设置为.diabled
        ajaxUrl: null,
        ajaxData:{},
        ajaxResponseText: null,
        ajaxCallback:null
    };
})(jQuery);