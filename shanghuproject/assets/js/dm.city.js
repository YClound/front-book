(function(){
    /*
        模板控制器
    */
    var iTemplate = (function(){
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

    var PROVINCE = 'province';
    var CITY = 'city';
    var DISTRICT = 'district';
    var STREET = 'street';
    var ChineseDistricts = window.ChineseDistricts || null;
    var TPL = '<div class="tab-city-container">'+
                    '<div class="tab-city-wrap">'+
                        '<div class="tab-city-headline flex bb aic">'+
                            '<div class="flex-1 p-7 ">请选择</div>'+
                            '<div class=""><a href="javascript:;" class="link-close">×</a></div>'+
                        '</div>'+
                        '<div class="tab-city-main">'+
                            '<div class="flex tac pb-5 pt-5 bb tab-city-links">'+
                                '<div class="flex-1 city-select-tab" style="display:none">'+
                                    '<a href="javascript:;" class="db p-3 br tab-city-link" data-role="province" data-text="省份">省份</a>'+
                                '</div>'+
                                '<div class="flex-1 city-select-tab"  style="display:none">'+
                                    '<a href="javascript:;" class="db p-3 br tab-city-link" data-role="city" data-text="城市">城市</a>'+
                                '</div>'+
                                '<div class="flex-1 city-select-tab" style="display:none" >'+
                                    '<a href="javascript:;" class="db p-3 br tab-city-link" data-role="district" data-text="区县">区县</a>'+
                                '</div>'+
                                '<div class="flex-1 city-select-tab" style="display:none" >'+
                                    '<a href="javascript:;" class="db p-3 tab-city-link" data-role="street" data-text="街道">街道</a>'+
                                '</div>'+
                            '</div>'+
                            '<div class="tab-city-hr"><div class="line"></div></div>'+
                            '<div class="tab-city-lists">'+
                                '<div class="tab-city-list city-select province" style="display:none" data-role="province"><ul></ul></div>'+
                                '<div class="tab-city-list city-select city" style="display:none" data-role="city"> <ul> </ul></div>'+
                                '<div class="tab-city-list city-select district" style="display:none" data-role="district"><ul></ul></div>'+
                                '<div class="tab-city-list city-select street" style="display:none" data-role="street"><ul></ul></div>'+
                            '</div>'+
                        '</div>'+
                    '</div>'+
                '</div>';

    var DEFAULTS = {
        level: 'district',
        province: '',
        city: '',
        district: ''
    }
    $.fn.chinaCity = function(options){
        var citySelect, that;
        if (!this.length) {
            return;
        }
        that = this;
        that.options = options;
        var citySelect = function(options){
            
            return that.each(function(){
                var self = $(this);
                var dems = [];
                var OPTIONS = {};
                var options = $.extend({}, DEFAULTS);
                var $target = $(this);
                var $btn = $(this).find('.cityBtn').length?$(this).find('.cityBtn'):$target;
                
                var $container = null;
                var $close = null;
                var $tabs = null;
                var $line = null;
                var $listWraps = null;
                var $wrap = null;
                var STEPS = ['province','city','district','street'];
               
                var $element = $target.find('[data-name="china"]');

                $element.attr('readonly', 'readonly');
                var server = self.data('server');
                //console.log('options',options)
                options.level = self.data('level')?self.data('level'):options.level;
                var defineDems =  function () {
                    var stop = false;
                    $.each([PROVINCE, CITY, DISTRICT,STREET], function (i, type) {
                        if (!stop) {
                            dems.push(type);
                        }
                        if (type === options.level) {
                            stop = true;
                        }
                    });
                };
                var render = function(){
                    var TPL_CITY = TPL;


                    $container = $(TPL_CITY).appendTo('body');
                    $close = $container.find('.close');
                    $tabs = $container.find('.tab-city-link');
                    $line = $container.find('.line');
                    $listWraps = $container.find('.tab-city-list');
                    $wrap = $container.find('.tab-city-wrap');
                    

                    var $select = $container.find('.city-select');
                    // setup this.$province, this.$city and/or this.$district object
                    $.each(STEPS, function (i, type) {
                        
                        if(dems.indexOf(type)>-1){
                            OPTIONS['$' + type] = $select.filter('[data-role=' + type + ']');
                            $tabs.filter('[data-role=' + type + ']').parent().show();
                        }else {
                            $container.find('.city-select').filter('[data-role=' + type + ']').remove();
                            $tabs.filter('[data-role=' + type + ']').parent().remove();
                        }
                            
                    });
                    
                    refresh();


                };
                // 渲染相应的地区列表
                var output = function(type){
                    var data = type === PROVINCE ? [] : [];
                    var item;
                    var districts;
                    var code ;
                    var matched = null;
                    var value ;
                    var $select = OPTIONS['$' + type];


                    item = $select.data('item');
                   
                    value = (item ? item.address : null) || OPTIONS[type];

                    if (!$select || !$select.length) {
                        return;
                    }
                    if(!ChineseDistricts) {
                        return null;
                    }
                    //console.log('$selectitem',item,OPTIONS[type],type)
                    
                    code = (
                        type === PROVINCE ? 86 :
                            type === CITY ? OPTIONS.$province && OPTIONS.$province.find('.selected').data('code') :
                                type === DISTRICT ? OPTIONS.$city && OPTIONS.$city.find('.selected').data('code') : code
                    );

                    //console.log(122211,code)
                    // 如果是街道则请求数据
                    if(type === STREET){
                        code = OPTIONS.$district.find('.selected').data('code');
                        console.log('STREET',code)
                        
                        requestData({type:STREET,id:code}).done(function(res){
                            var data = res.data || [];
                            $select.html(buildDOM(data));
                        });
                        return null;
                    }
                    
                    districts = $.isNumeric(code) ? ChineseDistricts[code] : null;
                    console.log(1,value,type,districts)
                    if ($.isPlainObject(districts)) {
                        $.each(districts, function (code, address) {
                            var provs;
                             
                            if (type === PROVINCE) {
                                provs = [];
                                for (var i = 0; i < address.length; i++) {
                                    //console.log(address[i].address,value)
                                    if (address[i].address === value) {

                                        matched = {
                                            code: address[i].code,
                                            address: address[i].address
                                        };
                                    }
                                    provs.push({
                                        code: address[i].code,
                                        address: address[i].address,
                                        selected: address[i].address === value
                                    });
                                }
                                data = data.concat(provs);
                            } else {
                                if (address === value) {
                                    matched = {
                                        code: code,
                                        address: address
                                    };
                                }
                                data.push({
                                    code: code,
                                    address: address,
                                    selected: address === value
                                });
                            }
                            
                        });
                    }
                    
                    $select.html(buildDOM(data));
                    $select.data('item', matched);
                };
                var tab =  function (type) {
                    var $selects = $container.find('.city-select');
                    var $tabs = $container.find('.city-select-tab > a');
                    var $select = OPTIONS['$' + type];
                    var $tab = $container.find('.city-select-tab > a[data-role="' + type + '"]');
                    
                    var index = dems.indexOf(type);
                    
                    var width = 1/dems.length*100;
                    var left = index*width;
                    $line.css('width',width+'%').css('left',left+'%');
                    if ($select) {
                        $selects.hide();
                        $select.show();
                        $tab.addClass('active')
                    }

                };
                var refresh = function(force){
                    // clean the data-item for each $select
                    var $select = $container.find('.city-select');
                    $select.data('item', null);

                    $container.find('.city-select-tab > a').each(function(){
                        $(this).text($(this).data('text')).removeClass('active')
                    });
                    // parse value from value of the target $element
                    var val = $element.val() || $element.data('value') || '';
                    val = val.split(',');
                    $.each(dems, function (i, type) {
                        var $tab = null;
                        if (val[i] && i < val.length) {
                            OPTIONS[type] = val[i];
                            $tab = $container.find('.city-select-tab > a').filter('[data-role="' + type + '"]');
                            $tab.text(val[i]);
                            $tab.addClass('active');


                        } else if (force) {
                            OPTIONS[type] = '';
                        }

                        output(type);
                        
                    });
                    var type = self.data('count');
                    console.log(val,'OPTIONS',OPTIONS)
                    tab(type || PROVINCE);
                };

                var init = function(){
                    defineDems();
                    render();
                    bind();
                    
                }
                var buildDOM = function(list){
                    
                    var buffer = ['<ul>'];
                    var tpl = '<li class="{selected}" data-code="{code}" title="{address}"><a href="javascript:;">{address}</a></li>';

                    for(var i = 0;i<list.length;i++){
                        var item = list[i];
                        item.code = item.code?item.code:item.id;
                        item.address = item.address?item.address:item.text;
                        item.selected = item.selected?'selected':'';
                        var html = iTemplate.substitute(tpl,item);
                        buffer.push(html);
                    }
                    buffer.push('</ul>');
                    
                    return buffer.join('');
                };
                var requestData = function(params){
                    var dtd = $.Deferred();
                    var type = params.type;
                    var id = params.id;
                    var url = server?server:'street.json';
                    if(window.ChineseDistricts && window.ChineseDistricts[id]){
                        dtd.resolve({data:window.ChineseDistricts[id]});
                        return dtd;
                    }else {
                        dtd.resolve({data:[]});
                        return dtd;
                    }

                    // 
                    $.ajax({
                        url: url,
                        type: 'GET',
                        dataType: 'json',
                        data: params
                    })
                    .done(function(res) {
                        console.log("success");

                        dtd.resolve(res);
                    })
                    .fail(function(res) {
                        dtd.reject({});
                    })
                    .always(function() {
                        console.log("complete");
                    });
                    
                    return dtd;
                };
                

                
                function open (type) {
                    type = type || PROVINCE;
                    $container.show();
                    setTimeout(function(){
                        $wrap.css('transform', 'none');
                    },100);
                   
                    //this.$textspan.addClass('open').addClass('focus');
                    tab(type);
                }
                var close = function(){
                    $wrap.css('transform', 'translate3d(0px, 100%, 0px)');
                    setTimeout(function(){
                        $container.hide();
                    },450)
                };
                var bind = function(){
                    // 关闭
                    $wrap.on('click','.link-close',function(e){
                        e.preventDefault();
                        e.stopPropagation();
                        close();
                    });
                    // 切换
                    $wrap.on('click','.tab-city-link.active',function(e){
                        e.preventDefault();
                        e.stopPropagation();
                        var $tab = $(e.currentTarget);
                        var role = $tab.data('role');
                        
                        tab(role);

                    });
                    // 选中一个地址
                    $wrap.on('click','.tab-city-list li a',function(e){
                        e.preventDefault();
                        e.stopPropagation();
                        var $current = $(e.currentTarget).parent();
                        var $select = $(e.currentTarget).closest('.city-select');
                        var $active = $select.find('.selected');
                        var address2 = $active.text();
                        var last = $select.next().length === 0;
                        var type = $select.attr('data-role');
                        var address = $current.attr('title');
                        var code = $current.data('code');
                        var $tab = $container.find('.tab-city-link').filter('[data-role="'+type+'"]');
                        var isChange;
                        $active.removeClass('selected');
                        $current.addClass('selected');
                        var item = {
                            address: address, code: code
                        };
                        
                        OPTIONS[type] = address;
                        
                        $select.data('item',item);
                        //console.log('select',item,OPTIONS)
                        $tab.text(address);
                        //console.log(111,$select.data('item'))
                        $tab.addClass('active');
                        // 假如地址变化则把下级tab变暗
                        if(address!=address2){
                            var index = dems.indexOf(type);
                            for(var i = index+1;i<dems.length;i++){
                                var $link = $container.find('.tab-city-link').filter('[data-role="'+dems[i]+'"]');
                                $link.removeClass('active').text($link.data('text'));
                            }
                        }
                        

                        if(PROVINCE === type && dems.indexOf(CITY)>-1){
                            output(CITY);
                            tab(CITY);
                            
                        }else if(CITY === type && dems.indexOf(DISTRICT)>-1){
                            output(DISTRICT);
                            tab(DISTRICT);
                        }else if(DISTRICT === type && dems.indexOf(STREET)>-1){
                            output(STREET);
                            tab(STREET);
                        }

                        // 假如是最后一列则关闭
                        if (last) {
                            var arr = [];
                            $container.find('.city-select').each(function() {
                                var item = $(this).data('item');
                                if (item) {
                                    arr.push(item.address);
                                }
                            });
                            $element.val(arr.join(','));
                            close();

                        }

                        

                    });

                    $btn.on('click',function(){
                        $container.show();
                        setTimeout(function(){
                            $wrap.css('transform', 'none');
                        },100);
                        
                    });


                    
                }
                    
                // 
                init();
                
            });
        };

        citySelect(options);

        return that;
    }
})();
$(function () {
    $('[data-toggle="chinacity"]').not('.manual').chinaCity();
});