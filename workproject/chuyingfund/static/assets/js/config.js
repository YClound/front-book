/*
*
*  web 配置 
*  
*/
(function(){
    var dir = '/static',
        js = dir + '/assets/js/',
        css = dir + '/assets/css/',
        alias,module;

    // region 浏览器检测
    var UA = window.UA = function ( ua, appVersion, platform ) {
        return {
            userAgent:ua,

            // win系列
            win32 : platform === "Win32",
            ie : /MSIE ([^;]+)/.test( ua ),
            ieMobile : window.navigator.msPointerEnabled,
            ieVersion : Math.floor( (/MSIE ([^;]+)/.exec( ua ) || [0, "0"])[1] ),

            // ios系列
            ios : (/iphone|ipad/gi).test( appVersion ),
            iphone : (/iphone/gi).test( appVersion ),
            ipad : (/ipad/gi).test( appVersion ),
            iosVersion : parseFloat( ('' + (/CPU.*OS ([0-9_]{1,5})|(CPU like).*AppleWebKit.*Mobile/i.exec( ua ) || [0, ''])[1])
                .replace( 'undefined', '3_2' ).replace( '_', '.' ).replace( '_', '' ) ) || false,
            safari : /Version\//gi.test( appVersion ) && /Safari/gi.test( appVersion ),
            uiWebView : /(iPhone|iPod|iPad).*AppleWebKit(?!.*Safari)/i.test( ua ),

            // 安卓系列
            android : (/android/gi).test( appVersion ),
            androidVersion : parseFloat( "" + (/android ([0-9\.]*)/i.exec( ua ) || [0, ''])[1] ),

            // chrome
            chrome : /Chrome/gi.test( ua ),
            chromeVersion : parseInt( ( /Chrome\/([0-9]*)/gi.exec( ua ) || [0, 0] )[1], 10 ),

            // 内核
            webkit : /AppleWebKit/.test( appVersion ),

            // 其他浏览器
            uc : appVersion.indexOf( "UCBrowser" ) !== -1,
            Browser : / Browser/gi.test( appVersion ),
            MiuiBrowser : /MiuiBrowser/gi.test( appVersion ),

            // 微信
            MicroMessenger : ua.toLowerCase().match( /MicroMessenger/i ) == "micromessenger",

            // 其他
            canTouch : "ontouchstart" in document
        };
    }( navigator.userAgent, navigator.appVersion, navigator.platform );

    // PC端跳转到手机端
    if(window.innerWidth<640) {
        var url = location.href;
        var match;
        var pages = [
            'http://www.chuyingfund.com/investor/investor-list.html',
            'http://www.chuyingfund.com/investor/investor-information.html',
            'http://www.chuyingfund.com/project/project-list.html',
            'http://www.chuyingfund.com/project/project-detail.html',
        ];
        if('http://www.chuyingfund.com/' == url || 'http://www.chuyingfund.com' == url){
            //console.log('mobile mode 1');
            url = url.replace('www','m');
            location.replace(url);

            //return
        }
        for (var i = pages.length - 1; i >= 0; i--) {
            if(url.indexOf(pages[i])>-1) {
                console.log('mobile mode');
                url = url.replace('www','m');
                location.replace(url);
                return
            }
        }

    }
    // 百度统计
    var baidutongji = function() { 
        var _hmt = _hmt || [];
        (function() {
          var hm = document.createElement("script");
          hm.src = "//hm.baidu.com/hm.js?2b0a90b77bbbe85c5f8b5cb522b21777";
          var s = document.getElementsByTagName("script")[0]; 
          s.parentNode.insertBefore(hm, s);
        })();
    };
    if(location.host== 'http://www.chuyingfund.com') {
        baidutongji();
    }
    

    window.api = {};
    window.dm = {};
    window.imgErr = function (img, filename) {
        filename = filename?filename:'avatar.png';
        img.onerror = null;

        img.src = '/static/assets/images/'+filename;
        //img.src = 'http://www.dreammove.cn/Public/Home/default/img/' + (filename == null ? 'default-img.jpg' : filename)
    }
    window.isMobile = function() {
        if(window.innerWidth<=768 || 'ontouchend' in document){
            return true;
        }
        return false;
    };
    window.getRootPath = function(){   
        var pathName = window.location.pathname.substring(1);   
        var webName = pathName == '' ? '' : pathName.substring(0, pathName.indexOf('/'));   
        return window.location.protocol + '//' + window.location.host + '/';   
    }
    window.post = function (URL, PARAMS) {        
        var temp = document.createElement("form");        
        temp.action = URL;        
        temp.method = "post";        
        temp.style.display = "none";        
        for (var x in PARAMS) {        
            var opt = document.createElement("textarea");        
            opt.name = x;        
            opt.value = PARAMS[x];        
            // alert(opt.name)        
            temp.appendChild(opt);        
        }        
        document.body.appendChild(temp);        
        temp.submit();        
        return temp;        
    }     
    var _jquery = (UA.ie && UA.ieVersion<=9)?'/static/assets/vendor/jquery/jquery-1.11.3.min.js':'/static/assets/vendor/jquery/jquery-2.1.4.min.js';
    // 设置别名
    alias = {
        //项目样式
        'common.css':css + 'public/common.css',

        // 库
        'jquery': _jquery,
        'modernizr':'/static/assets/vendor/modernizr/modernizr-2.8.3.min.js',
        
        // 插件
        'dm.common':'/static/assets/js/plugins/dm.common.js',
        'dm.ajaxpost':'/static/assets/js/plugins/dm.ajaxpost.js',
        'dm.modal':'/static/assets/js/plugins/dm.modal.js',
        'dm.validate':'/static/assets/js/plugins/dm.validate2.js',
        'dm.bubble':'/static/assets/js/plugins/dm.bubble.js',
        'dm.redirect':'/static/assets/js/plugins/dm.redirect.js',
        'dm.scroll':'/static/assets/js/plugins/dm.scroll.js',
        'dm.transition':'/static/assets/js/plugins/dm.transition.js',
		'dm.tab':'/static/assets/js/plugins/dm.tab.js',
        'dm.city.select':'/static/assets/js/plugins/dm.city.select.js',
        'dm.up.avatar':'/static/assets/js/plugins/dm.up.avatar.js',
        'dm.wu':'/static/assets/js/plugins/dm.wu.js',
        'dm.redirect':'/static/assets/js/plugins/dm.redirect.js',
        'dm.input':'/static/assets/js/plugins/dm.input.js',
        'dm.up.avatar':'/static/assets/js/plugins/dm.up.avatar.js',
        'dm.util':'/static/assets/js/plugins/dm.util.js',
        'dm.loading':'/static/assets/js/plugins/dm.loading2.js',
        'dm.pager':'/static/assets/js/plugins/dm.pager.js',
        'dm.recommend':'/static/assets/js/plugins/dm.recommend.js',
        'store':'/static/assets/js/plugins/store2.js',

        'template':'/static/assets/js/plugins/template.js',
        'sortable':'/static/assets/js/plugins/Sortable.min.js',
        'webuploader':'/static/assets/vendor/webuploader/webuploader.nolog.js',
        'cropper':'/static/assets/vendor/cropper/cropper.min.js',
        'countup':'/static/assets/js/plugins/countup.min.js',
        'owl.carousel':'/static/assets/vendor/owl/owl.carousel.min.js',
        'seed':'/static/assets/igrow/seed/seed.js',
        'jqPaginator':'/static/assets/vendor/jqPaginator/jqPaginator.min.js',
        'JCShare':'/static/assets/vendor/jcshare/jquery.jcshare.js',
        'qrcode':'/static/assets/vendor/qrcode/jquery.qrcode.min.js',
        'ueditor.config':'/static/syspage/plugins/ueditor/ueditor.config.js',
        'ueditor':'/static/syspage/plugins/ueditor/ueditor.all.min.js',
        'clipboard':'/static/assets/vendor/clipboard/clipboard.min.js',
        'pdfobject':'/static/assets/vendor/pdfobject/pdfobject.js',
        'imageview':'/static/assets/vendor/imageview/jquery.imageview.js',
        'imageview.css':'/static/assets/vendor/imageview/jquery.imageview.css',
        'photoswipe':'/static/assets/vendor/photoswipe/photoswipe.min.js',
        'photoswipe-ui':'/static/assets/vendor/photoswipe/photoswipe-ui-default.min.js',
        'photoswipe.css':'/static/assets/vendor/photoswipe/photoswipe.css',
        'photoswipe-ui.css':'/static/assets/vendor/photoswipe/default-skin/default-skin.css',
        'dm.photoswipe':'/static/assets/js/plugins/dm.photoswipe.js',
        'raphael':'/static/assets/js/plugins/raphael.min.js',
        'swipe':'/static/assets/js/plugins/swipe.js',
        'laydate':'/static/assets/vendor/laydate/laydate.js',

        // 业务
        'base':'/static/assets/js/base.js',

        'header':'/static/assets/js/header.js',
        'index':'/static/assets/js/module/index.js',
        'create.common':'/static/assets/js/module/create.common.js',

        // pc 
        'login_register':'/static/assets/js/module/pc/login_register.js',

        // mobile
        'm_common':'/static/assets/js/module/mobile/m_common.js',
        'm_porject_list':'/static/assets/js/module/mobile/m_porject_list.js'

    };

    // 获取站点根目录
    function getRootPath() {
        var strFullPath = window.document.location.href;
        var strPath = window.document.location.pathname;
        var pos = strFullPath.indexOf(strPath);
        var prePath = strFullPath.substring(0, pos);
        var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
        return (prePath + postPath);
    }


    // console.log('alias',alias)
    window['seajs'] && seajs.config({
        base:'/',
        alias:alias,
        charset: 'utf-8',
        preload:[
            window.jQuery?'':'jquery',
            //window.Modernizr?'':'modernizr',
            window.base?'':'base'
        ],
        map: [
            //[ /^(.*\.(?:css|js))(.*)$/i, '$1?'+new Date().valueOf() ]
            // modules下的业务js不缓存
            // module/xxx.js
            [ /(module\/\w+)(\.js)/i,'$1$2?'+new Date().valueOf() ],
            // module/xxx/xxx.js
            [ /(module\/\w+\/\w+)(\.js)/i,'$1$2?'+new Date().valueOf() ],
            [ /(mainApp\.js)/i,'$1?'+ new Date().valueOf()]
        ]
    });

    window['seajs'] && seajs.on('error', function(module){
        console.warn('seajs error',this);
        if(module && module.status!=5){
            alert(module.status)
            console.error('seajs error: ', module);
        }
    });

})();

/*

    赋值 cookie('a','value',1)
    获取 cookie('a')
    移除 cookie.remove('a')
    全部清空 cookie.clear()
    获取全部 cookie.all()
*/
;(function(root, factory) {

    root.cookie = factory();

    /*if (typeof define === 'function' && define.amd) {
        // AMD. Register as an anonymous module.
        define([], factory);
    } else if (typeof exports === 'object') {
        // Node.js
        module.exports = factory();
    } else {
        // Browser globals (root is window)
        
        root.cookie = factory();
    }*/
}(this, function(root, undefined) {
    var Cookie = {
        cookieAPI:{
            get: function(name){
                var nameEQ = name + "=";    
                var ca = document.cookie.split(';');//把cookie分割成组    
                for(var i=0;i < ca.length;i++) {    
                    var c = ca[i];//取得字符串    
                    while (c.charAt(0)==' ') {//判断一下字符串有没有前导空格    
                        c = c.substring(1,c.length);//有的话，从第二位开始取    
                    }    
                    if (c.indexOf(nameEQ) == 0) {//如果含有我们要的name    
                        return unescape(c.substring(nameEQ.length,c.length));//解码并截取我们要值    
                    }    
                }    
                return false;
            },
            set: function(name, value, options){
                if (Cookie.isPlainObject(name)) {
                    for (var k in name) {
                        if (name.hasOwnProperty(k)) this.set(k, name[k], value);
                    }
                }else{
                    var opt = Cookie.isPlainObject(options) ? options : { expires: options },
                        expires = opt.expires !== undefined ? opt.expires : '',
                        expiresType = typeof(expires),
                        path = opt.path !== undefined ? ';path=' + opt.path : ';path=/',
                        domain = opt.domain ? ';domain=' + opt.domain : '',
                        secure = opt.secure ? ';secure' : '';

                    //过期时间
                    if (expiresType === 'string' && expires !== '') expires = new Date(expires);
                    else if (expiresType === 'number') expires = new Date(+new Date + 1000 * 60 * 60 * 24 * expires);
                    if (expires !== '' && 'toGMTString' in expires) expires = ';expires=' + expires.toGMTString();


                    document.cookie = name+"="+escape(value)+expires+path+domain+secure;   //转码并赋值    
                }
            },
            remove: function(names){
                names = Cookie.isArray(names) ? names : Cookie.toArray(arguments);
                for (var i = 0, l = names.length; i < l; i++) {
                    this.set(names[i], '', -1);
                }
                return names;  
            },
            clear: function(name){
                return this.remove(Cookie.getKeys(this.all()));
            },
            all:function () {
                if (document.cookie === '') return {};
                var cookies = document.cookie.split('; '),result = {};
                for (var i = 0, l = cookies.length; i < l; i++) {
                    var item = cookies[i].split('=');
                    result[unescape(item[0])] = unescape(item[1]);
                }
                return result;
            }
        },
        // Object.names : return []
        getKeys: Object.names || function (obj) {
            var names = [],name = '';
            for (name in obj) {
                if (obj.hasOwnProperty(name)) names.push(name);
            }
            return names;
        },
        // 所谓"纯粹的对象"，就是该对象是通过"{}"或"new Object"创建的
        isPlainObject:function (value) {
            return !!value && Object.prototype.toString.call(value) === '[object Object]';
        },
        isArray:function (value) { return value instanceof Array },
        toArray:function (value) {
            return Array.prototype.slice.call(value);
        }
    };

    cookie = function (name, value, options){
        //console.log(this)
        var argm = arguments,
        _cookie = function(){
            if (argm.length === 0) return cookie.clear();
            if (Cookie.isPlainObject(name) || (argm.length>1&&name&&value))
                return cookie.set(name, value, options);
            if (value === null) return cookie.remove(name);
            if (argm.length === 1) return cookie.get(name);
            return cookie.all();
        }
        return _cookie()
    }
    for (var a in Cookie.cookieAPI) cookie[a] = Cookie.cookieAPI[a];
    return cookie
}));