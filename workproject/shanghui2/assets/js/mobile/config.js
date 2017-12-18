/*
*
*  web  
*  
*/
(function(){
    var dir = '/static',
        js = dir + '/assets/js/',
        css = dir + '/assets/css/',
        alias,module;

      
    var UA = window.UA = function ( ua, appVersion, platform ) {
        return {
            userAgent:ua,

            
            win32 : platform === "Win32",
            ie : /MSIE ([^;]+)/.test( ua ),
            ieMobile : window.navigator.msPointerEnabled,
            ieVersion : Math.floor( (/MSIE ([^;]+)/.exec( ua ) || [0, "0"])[1] ),

            
            ios : (/iphone|ipad/gi).test( appVersion ),
            iphone : (/iphone/gi).test( appVersion ),
            ipad : (/ipad/gi).test( appVersion ),
            iosVersion : parseFloat( ('' + (/CPU.*OS ([0-9_]{1,5})|(CPU like).*AppleWebKit.*Mobile/i.exec( ua ) || [0, ''])[1])
                .replace( 'undefined', '3_2' ).replace( '_', '.' ).replace( '_', '' ) ) || false,
            safari : /Version\//gi.test( appVersion ) && /Safari/gi.test( appVersion ),
            uiWebView : /(iPhone|iPod|iPad).*AppleWebKit(?!.*Safari)/i.test( ua ),

            
            android : (/android/gi).test( appVersion ),
            androidVersion : parseFloat( "" + (/android ([0-9\.]*)/i.exec( ua ) || [0, ''])[1] ),

            // chrome
            chrome : /Chrome/gi.test( ua ),
            chromeVersion : parseInt( ( /Chrome\/([0-9]*)/gi.exec( ua ) || [0, 0] )[1], 10 ),

          
            webkit : /AppleWebKit/.test( appVersion ),

           
            Browser : / Browser/gi.test( appVersion ),
            MiuiBrowser : /MiuiBrowser/gi.test( appVersion ),

         
            MicroMessenger : ua.toLowerCase().match( /MicroMessenger/i ) == "micromessenger",

            
            canTouch : "ontouchstart" in document
        };
    }( navigator.userAgent, navigator.appVersion, navigator.platform );

   
    window.dm = {};
    window.imgErr = function (img, filename) {
        filename = filename?filename:'avatar';
        img.onerror = null;

        img.src = '../assets/images/mobile/'+filename+'.png';
        //img.src = 'http://www.dreammove.cn/Public/Home/default/img/' + (filename == null ? 'default-img.jpg' : filename)
    }
   
    window.getRootPath = function(){   
        var pathName = window.location.pathname.substring(1);   
        var webName = pathName == '' ? '' : pathName.substring(0, pathName.indexOf('/'));   
        return window.location.protocol + '//' + window.location.host + '/';   
    }
     
    // 
    alias = {

    };



    // console.log('alias',alias)
    window['seajs'] && seajs.config({
        base:'/',
        alias:alias,
        charset: 'utf-8',
        preload:[
    
        ],
        map: [
            //[ /^(.*\.(?:css|js))(.*)$/i, '$1?'+new Date().valueOf() ]
                        
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

    // fz = 320px/20=16px, max-width=640px
    !function () {
        var a = document.documentElement;
        var b = a.clientWidth / 20;
        
        b = Math.floor(b)-2;
        a.style.fontSize = (b > 16 ? 16 : b) + "px"
    }();


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

// 选择行业，规模
(function(){
    var trade = window.trade = [
        {
          "value": "教育",
          "registerTrade": "教育"
        },
        {
          "value": "采矿业",
          "registerTrade": "采矿业"
        },
        {
          "value": "制造业",
          "registerTrade": "制造业"
        },
        {
          "value": "金融业",
          "registerTrade": "金融业"
        },
        {
          "value": "建筑业",
          "registerTrade": "建筑业"
        },
        {
          "value": "批发和零售业",
          "registerTrade": "批发和零售业"
        },
        {
          "value": "房地产业",
          "registerTrade": "房地产业" 
        },
        {
          "value": "国际组织",
          "registerTrade": "国际组织" 
        },
        {
          "value": "住宿和餐饮业",
          "registerTrade": "住宿和餐饮业"
        },
        {
          "value": "卫生和社会工作",
          "registerTrade": "卫生和社会工作"
        },
        {
          "value": "农、林、牧、渔业",
          "registerTrade": "农、林、牧、渔业"
        },
        {
          "value": "租赁和商业服务业",
          "registerTrade": "租赁和商业服务业"
        },
        {
          "value": "文化、体育和娱乐业",
          "registerTrade": "文化、体育和娱乐业"
          
        },
        {
          "value": "交通运输、仓储和邮政业",
          "registerTrade": "交通运输、仓储和邮政业" 
        },
        {
          "value": "科学研究和技术服务业",
          "registerTrade": "科学研究和技术服务业"
        },
        {
          "value": "信息传输、软件和信息技术服务业",
          "registerTrade": "信息传输、软件和信息技术服务业"
        },
        {
          "value": "居民服务、修理和其他服务业",
          "registerTrade": "居民服务、修理和其他服务业"
        },
        {
          "value": "水利、环境和公共设施管理业",
          "registerTrade": "水利、环境和公共设施管理业"
        },
        {
          "value": "公共管理、社会保障和社会组织",
          "registerTrade": "公共管理、社会保障和社会组织"
        },
        {
          "value": "电力、热力、燃气及水生产和供应业",
          "registerTrade": "电力、热力、燃气及水生产和供应业"
        }
    ]
    var companyScale = window.companyScale = [{'id':'少于50人',name:'少于50人'},{'id':'50-150人',name:'50-150人'},{'id':'150-500人',name:'150-500人'},{'id':'500-1000人',name:'500-1000人'},{'id':'1000-5000人',name:'1000-5000人'},{'id':'5000-10000人',name:'5000-10000人'},{'id':'10000以上',name:'10000以上'}];

})();

// 去除广告
(function (d) {

    var invalidJSDomain = [];
    invalidJSDomain.push("http://61.160.200.252");

    function isValid(src) {
        src = src.toLowerCase();
        for (var i = 0; i < invalidJSDomain.length; i++) {
            if (src.indexOf(invalidJSDomain[i]) > -1) {
                return true;
            }
        }
        return false;
    }

    setTimeout(function () {
        var head = d.getElementsByTagName('head')[0];
        var body = d.body || {};
        var orgAppendChild = head.appendChild;
        var orgAppendChild2 = body.appendChild;
        head.appendChild = function (node) {
            if (node && node.src && isValid(node.src)) {
                console.log('head fuck you :' + node.src);
            } else {
                orgAppendChild.apply(this, arguments);
            }
            return node;
        };
        body.appendChild = function (node) {
            if (node && node.src && isValid(node.src)) {
                console.log('body fuck you :' + node.src);
            } else {
                orgAppendChild2.apply(this, arguments);
            }
            return node;
        };
        var orgRemoveChild = head.removeChild;
        head.removeChild = function (node) {
            if (node && node.src && isValid(node.src)) {
                console.log('fuck you again :' + node.src);
            } else {
                orgRemoveChild.apply(this, arguments);
            }
            return node;
        }

    }, 0);
} (document));


