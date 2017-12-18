$(function(){
    if (!window.dm) {
        throw new Error('require dm')
    }
    /*
        模板控制器
    */
    var iTemplate = window.iTemplate = (function(){
        var template = function(){};
        template.prototype = {
            // 针对数组数据 iTemplate.makeList('<p a="{a}">{b}</p>', [{a:1,b:2},{a:22,b:33}] ) return '<p a="1">2</p><p a="22">33</p>'
            makeList: function(tpl, arr, fn){
                var res = [], $10 = [], reg = /{(.+?)}/g, json2 = {}, index = 0;
                for(var el = 0;el<arr.length;el++){
                    if(typeof fn === "function"){
                        json2 = fn.call(this, el, arr[el], index++)||{};
                    }
                    res.push(
                         tpl.replace(reg, function($1, $2){
                            return ($2 in json2)? json2[$2]: (undefined === arr[el][$2]? '':arr[el][$2]);
                        })
                    );
                }
                return res.join('');
            },
            // 针对单个数据 iTemplate.substitute('<p a="{a}">{b}</p>',{a:1,b:2}) return '<p a="1">2</p>'
            substitute: function(tpl, obj){
                if (!(Object.prototype.toString.call(tpl) === '[object String]')) {
                    return '';
                }
                if(!(Object.prototype.toString.call(obj) === '[object Object]' && 'isPrototypeOf' in obj)) {
                    return tpl;
                }
                //    /\{([^{}]+)\}/g
                return tpl.replace(/\{(.*?)\}/igm , function(match, key) {
                    if(typeof obj[key] != 'undefined'){
                        return obj[key];
                    }
                    return '';
                });
            }
        }
        return new template();
    })();

    // 高频执行事件/方法的防抖 取自 UnderscoreJS 实用框架 
    // 添加resize的回调函数，但是只允许它每300毫秒执行一次  
    /* window.addEventListener('resize', debounce(function(event) {

        console.log('这里写resize过程 ') 

    }, 300));*/ 
    function debounce(func, wait, immediate) {
        var timeout;
        return function() {
            var context = this,
                args = arguments;
            var later = function() {
                timeout = null;
                if (!immediate) func.apply(context, args);
            };
            var callNow = immediate && !timeout;
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
            if (callNow) func.apply(context, args);
        };
    }

    $.fn.autocomplete = function(options){
        var options = options || {};
        var self = this;
        var $this = $(this);
        var action = $this.data('action');
        var tplID = $this.data('tpl');
        var tpl = $('#'+tplID).html();
        var ul = $this.find('.search-list');


        var cancel = $this.find('.search-cancel'),
            layer = $(".search-lay"),
            form = $(".search-form"),
            ipt = $(".search-input");
        
        cancel.click(function(){
            $(this).hide();
            layer.hide();
            ipt.val('');
        });
        
        // 点击搜索结果
        ul.on('click','a',function(e){
            var result = $this.find('[data-name=key]');
            var a = $(this);
            var value = a.data('value');
            var key = a.data('key');

            result.val(key);
            ul.hide();

        });

        form.on("submit",function(){
            if(window.localStorage){
                if(typeof localStorage.searchkey !== "undefined"){
                    var searchkey = (localStorage.searchkey !== "") ? JSON.parse(localStorage.searchkey) : [];
                }else{
                    var searchkey =[];
                }
                
                var isadd = true;
                
                jQuery.each(searchkey,function(k,v){
                    if(typeof v != "undefined" && v == ipt.val() ){
                        searchkey.splice(k,1);
                    }
                })
                searchkey.unshift(ipt.val());
                
                searchkey.splice(8);
            
                window.localStorage.setItem('searchkey',JSON.stringify(searchkey));
            }
        });
        
        
        // <li class="search"> <a href="{url}"> <em class="related">{key}</em> </a> </li> 
        // <li class="search"> <a href="{url}"> {title} </a> </li> 
        var isLoading = false;
        var searchParams = {};
        // 搜索参数
        $this.find('.search-param').each(function(_,ele){
            searchParams[ele.name] = ele.value;
        });
        ipt.on("focus input paste", function(){
            var showul = ul;
            var showli = '';
            var keyword = ipt.val();
            var data = {
                keyword:keyword
            };
            data = $.extend({},searchParams,data);

            var ajax = function(){
               
                
                if(isLoading){
                    return
                }

                isLoading = true;
                dm.ajax('get', action, data).done(function(res){
                    var buffer = [];
                    if(''+res.status === '0'){
                        var data = res.data || {};
                        var list = data.list || [];
                        
                        for (var i = 0; i < list.length; i++) {
                            var item = list[i];
                            var html = iTemplate.substitute(tpl,item);

                            if(item.value.indexOf(keyword)>-1){
                                html = html.replace(keyword, '<em class="related">'+keyword+'</em>');
                                
                            }
                            buffer.push(html);

                        };
                        //console.log(buffer)
                        showul.html(buffer.join(''));
                        layer.show();
                        cancel.show();                  
                    }
                }).always(function(){
                    //console.log('isLoading')
                    setTimeout(function(){
                        isLoading = false;
                    },400);
                    
                });
                
                /*jQuery.ajax({
                    type: "get",
                    data: data,
                    dataType:"json",
                    url: action,
                    success:function(res) {
                        
                        var buffer = [];
                        if(''+res.status === '0'){
                            var data = res.data || {};
                            var list = data.list || [];
                            
                            for (var i = 0; i < list.length; i++) {
                                var item = list[i];
                                var html = iTemplate.substitute(tpl,item);
                                if(item.value.indexOf(keyword)>-1){
                                    html = html.replace(keyword, '<em class="related">'+keyword+'</em>');
                                    
                                }
                                buffer.push(html);

                            };
                            //console.log(buffer)
                            showul.html(buffer.join(''));
                            layer.show();
                            cancel.show();                  
                        }
                    }
                }).always(function(){
                    //console.log('isLoading')
                    setTimeout(function(){
                        isLoading = false;
                    },500);
                    
                });*/
            }
            
            if(keyword!=''){
                
                ajax();

            }else{
                if(typeof window.localStorage !=="undefined" && typeof localStorage.searchkey !=="undefined"){
                    var searchkey = (localStorage.searchkey !== "") ? JSON.parse(localStorage.searchkey) : [];                              
                    if(ipt.val() == ""){
                       
                        jQuery.each(searchkey,function(k,v){
                            showli += '<li class="history"><a href="/s.php?wd='+encodeURIComponent(v)+'">'+v+'</a></li>';
                        });
                        
                        if(searchkey.length > 0){
                            showli += '<li class="clean"><a href="javascript:void(0);">清除历史记录</a></li>';
                        }
                        
                    }else{
                        
                        jQuery.each(searchkey,function(k,v){
                            if(v.indexOf(ipt.val()) == 0){
                                showli += '<li class="history"><a href="/s.php?wd='+encodeURIComponent(v)+'">'+v+'</a></li>';
                            }
                        });
                        
                    }
                }
                if(showli!=''){
                    showul.append(showli);
                    layer.show();
                    cancel.show();
                }    
                    
            }
        });
        
        layer.on("click",".clean",function(){
            if(typeof window.localStorage !=="undefined" && typeof localStorage.searchkey !=="undefined"){
                localStorage.searchkey = "";
                layer.hide();
            }
        });
    };
    
    $('.autocomplete').autocomplete();
});