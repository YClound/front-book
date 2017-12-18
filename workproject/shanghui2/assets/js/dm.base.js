/* 全局 */
(function(){
     // 微信分享
     window.COVER = {
        version: "0.0.1",
        debug: !1,
        
        wx_appid: "wxd25e4cb6f268aca0",
        timestamp: (new Date).getTime(),
        apiurl: {
            weixingetConf: "/api/wx/jssdk_config",
            setActivity: "/activity/setActivity",
            getActivity: "/activity/getActivity",
        },
        fileName: function() {
            var e = window.location.pathname.split(".html")[0].split("/").pop(),
            t = this.isEmpty(e) ? "index": e;
            return t
        },
        isEmpty: function(e) {
            return "" == e || null == e || void 0 == e
        },
        getUrl: function(e) {
            var t = new RegExp("(^|&)" + e + "=([^&]*)(&|$)", "i"),
            n = window.location.search.substr(1).match(t);
            return null != n ? unescape(n[2]) : null
        },
        getCookie: function(e) {
            var t, n = new RegExp("(^| )" + e + "=([^;]*)(;|$)");
            return (t = document.cookie.match(n)) ? unescape(t[2]) : null
        },
        setCookie: function(e, t, n) {
            var i = new Date;
            i.setTime(i.getTime() + n);
            var r = "expires=" + i.toUTCString();
            document.cookie = e + "=" + t + "; " + r
        },
        skip: function(e, t) {
            return this.amload.load(),
            this.isEmpty(t) || (localStorage._temp = JSON.stringify(t) || ""),
            window.location.href = e,
            !1
        },
        amload: {
            load: function() {
                $("body").append('<div class="tc-load-icon"><img src="http://wapcdn.thecover.cn/activity/public/laoding.gif" alt="加载中..."></div>')
            }
        },
        ajax: function() {
           
        },
        isMobile: function(e) {
            return /^(0|86|17951)?(13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8}$/.test(e)
        },
        safehtml: function(e) {
            return e.replace(/<\/h*[^>]*>/gi, "</p>").replace(/<h[^>]*>/gi, "<p>").replace(/<a[^>]*>/gi, "<a>").replace(/style="[\s\S]*?"/gi, "").replace(/<script[^>]*>[\s\S]*?<\/[^>]*script>/gi, "")
        },
        device: {
            UA: function() {
                return navigator.userAgent
            },
            isIOS: function() {
                var e = this.UA();
                return !! (/iphone/i.test(e) || /ipad/i.test(e) || /ipod/i.test(e))
            },
            isAndroid: function() {
                var e = this.UA();
                return !! /android/i.test(e)
            },
            isWeixin: function() {
                var e = this.UA();
                return !! /MicroMessenger/i.test(e)
            },
            isQQ: function() {
                var e = this.UA();
                return !! /qq/i.test(e)
            }
        },
        openapi: {
            weixin: {
                conf:{},
                // e {}
                wx_conf: function(e, t) {
                    var self = this;
                    var n = that = this,
                    i = window.location.href.split("#")[0],
                    r = {
                        url: i
                    };
                    //alert(COVER.apiurl.weixingetConf);
                    $.ajax({
                      url: COVER.apiurl.weixingetConf,
                      type: 'get',
                      dataType: 'json',
                      cache:true,
                      data: {url: i},
                      success: function(data, textStatus, xhr) {
                        var r = data.data;
                        wx.config({
                            debug: !1,
                            appId: r.appId,
                            timestamp: r.timestamp,
                            nonceStr: r.nonceStr,
                            signature: r.signature,
                            jsApiList: ["onMenuShareTimeline", "onMenuShareAppMessage"]
                        });
                        
                        n[t](e);
                        
                        
                        
                      },
                      error: function(xhr, textStatus, errorThrown) {
                         //alert('分享失败了')
                        //called when there is an error
                      }
                    });
                    
                },
                wx_share: function(e) {

                    this.wx_conf(e, "wx_onshare")
                },
                wx_chooseImage: function() {
                    this.wx_conf("", "wx_onchooseImage")
                },
                wx_hideOptionMenu: function() {
                    this.wx_conf("", "wx_onhideOptionMenu")
                },
                wx_showOptionMenu: function() {
                    this.wx_conf("", "wx_onshowOptionMenu")
                },
                wx_onshowOptionMenu: function() {
                    wx.showOptionMenu()
                },
                wx_onhideOptionMenu: function() {
                    wx.hideOptionMenu()
                },
                wx_onchooseImage: function() {
                    wx.ready(function() {
                        wx.chooseImage({
                            count: 1,
                            sizeType: ["original", "compressed"],
                            sourceType: ["album", "camera"],
                            success: function(e) {
                                e.localIds
                            }
                        })
                    })
                },
                wx_onshare: function(e) {
                    window.wx && window.wx.ready(function() {
                        wx.onMenuShareTimeline(e),
                        wx.onMenuShareAppMessage(e),
                        wx.onMenuShareQQ(e),
                        wx.onMenuShareWeibo(e),
                        wx.onMenuShareQZone(e)
                    })
                }
            },
            baidutongji: function() { 
                var _hmt = _hmt || [];
                (function() {
                  var hm = document.createElement("script");
                  hm.src = "//hm.baidu.com/hm.js?2b0a90b77bbbe85c5f8b5cb522b21777";
                  var s = document.getElementsByTagName("script")[0]; 
                  s.parentNode.insertBefore(hm, s);
                })();
            }
        }
    };
    // 动态缩略图
    window.parsePhotoData = function(data){
        var _photo = [];
        if(data.photo) {
            _photo = data.photo.split(',');
        }else {
            return;
        }
        for (var i = _photo.length - 1; i >= 0; i--) {
            var ori = _photo[i];
            var index1 = ori.lastIndexOf('/');
            if(index1>-1){
                var prefix = ori.substring(0,index1);
                var name = ori.substring(index1+1);
                var thumbnail = prefix + '/' + 'thumbnail_' + name;
            }else {
                var thumbnail = 'thumbnail_' + ori;
            }
            
            var index = ori.lastIndexOf('_');
            if(index>-1) {
                var str = ori.substring(index+1);
                var arr = str.split('.');
                var size = arr[0];
            }else {
                var size = '';
            }
                
            _photo[i] = {
                thumbnail:thumbnail,
                ori:ori,
                size:size
            };

        }
        data._photo = _photo;
        
    };



    // 图片验证码
    var imgVerifyHelper = window.imgVerifyHelper= function () {
        var $img
        return {
            init: function () {
                $img = $('.img-verify')
                $(".reload-verify").click(this.reload)
            },
            reload: function () {
                var src = $img.attr("src");
                if (~src.indexOf('?')) {
                    $img.attr("src", src + '&random=' + Math.random());
                } else {
                    $img.attr("src", src.replace(/\?.*$/, '') + '?' + Math.random());
                }
            }
        }
    }();
    /*window.addEventListener('error', function (e) {
        var target = e.target
        if (target.tagName === 'IMG' && !target.hasAttribute('noerr')) {
            target.setAttribute('noerr', '1')
            var dft = target.dataset ? target.dataset.default : target.getAttribute('data-default')
            target.src = '/Public/Home/mobile/img/' + (dft || 'default-img.jpg')
        }
    }, true)*/
    // fz = 320px/20=16px, max-width=640px
    /*!function () {
        var a = document.documentElement;
        var b = a.clientWidth / 20;
        a.style.fontSize = (b > 32 ? 32 : b) + "px"
    }();*/

    /*var console, length, method, methods, noop;
    method = void 0;
    noop = function () {
    };
    methods = ['assert', 'clear', 'count', 'debug', 'dir', 'dirxml', 'error', 'exception', 'group', 'groupCollapsed', 'groupEnd', 'info', 'log', 'markTimeline', 'profile', 'profileEnd', 'table', 'time', 'timeEnd', 'timeline', 'timelineEnd', 'timeStamp', 'trace', 'warn'];
    length = methods.length;
    console = window.console = window.console || {};
    while (length--) {
        method = methods[length];
        if (!console[method]) {
            console[method] = noop;
        }
    }*/

    

})();

/*! store2 - v2.3.0 - 2015-05-22
* Copyright (c) 2015 Nathan Bubna; Licensed MIT, GPL */
;(function(window, define) {
    var _ = {
        version: "2.3.0",
        areas: {},
        apis: {},

        // utilities
        inherit: function(api, o) {
            for (var p in api) {
                if (!o.hasOwnProperty(p)) {
                    o[p] = api[p];
                }
            }
            return o;
        },
        stringify: function(d) {
            return d === undefined || typeof d === "function" ? d+'' : JSON.stringify(d);
        },
        parse: function(s) {
            // if it doesn't parse, return as is
            try{ return JSON.parse(s); }catch(e){ return s; }
        },

        // extension hooks
        fn: function(name, fn) {
            _.storeAPI[name] = fn;
            for (var api in _.apis) {
                _.apis[api][name] = fn;
            }
        },
        get: function(area, key){ return area.getItem(key); },
        set: function(area, key, string){ area.setItem(key, string); },
        remove: function(area, key){ area.removeItem(key); },
        key: function(area, i){ return area.key(i); },
        length: function(area){ return area.length; },
        clear: function(area){ area.clear(); },

        // core functions
        Store: function(id, area, namespace) {
            var store = _.inherit(_.storeAPI, function(key, data, overwrite) {
                if (arguments.length === 0){ return store.getAll(); }
                if (data !== undefined){ return store.set(key, data, overwrite); }
                if (typeof key === "string"){ return store.get(key); }
                if (!key){ return store.clear(); }
                return store.setAll(key, data);// overwrite=data, data=key
            });
            store._id = id;
            try {
                var testKey = '_safariPrivate_';
                area.setItem(testKey, 'sucks');
                store._area = area;
                area.removeItem(testKey);
            } catch (e) {}
            if (!store._area) {
                store._area = _.inherit(_.storageAPI, { items: {}, name: 'fake' });
            }
            store._ns = namespace || '';
            if (!_.areas[id]) {
                _.areas[id] = store._area;
            }
            if (!_.apis[store._ns+store._id]) {
                _.apis[store._ns+store._id] = store;
            }
            return store;
        },
        storeAPI: {
            // admin functions
            area: function(id, area) {
                var store = this[id];
                if (!store || !store.area) {
                    store = _.Store(id, area, this._ns);//new area-specific api in this namespace
                    if (!this[id]){ this[id] = store; }
                }
                return store;
            },
            namespace: function(namespace, noSession) {
                if (!namespace){
                    return this._ns ? this._ns.substring(0,this._ns.length-1) : '';
                }
                var ns = namespace, store = this[ns];
                if (!store || !store.namespace) {
                    store = _.Store(this._id, this._area, this._ns+ns+'.');//new namespaced api
                    if (!this[ns]){ this[ns] = store; }
                    if (!noSession){ store.area('session', _.areas.session); }
                }
                return store;
            },
            isFake: function(){ return this._area.name === 'fake'; },
            toString: function() {
                return 'store'+(this._ns?'.'+this.namespace():'')+'['+this._id+']';
            },

            // storage functions
            has: function(key) {
                if (this._area.has) {
                    return this._area.has(this._in(key));//extension hook
                }
                return !!(this._in(key) in this._area);
            },
            size: function(){ return this.keys().length; },
            each: function(fn, and) {
                for (var i=0, m=_.length(this._area); i<m; i++) {
                    var key = this._out(_.key(this._area, i));
                    if (key !== undefined) {
                        if (fn.call(this, key, and || this.get(key)) === false) {
                            break;
                        }
                    }
                    if (m > _.length(this._area)) { m--; i--; }// in case of removeItem
                }
                return and || this;
            },
            keys: function() {
                return this.each(function(k, list){ list.push(k); }, []);
            },
            get: function(key, alt) {
                var s = _.get(this._area, this._in(key));
                return s !== null ? _.parse(s) : alt || s;// support alt for easy default mgmt
            },
            getAll: function() {
                return this.each(function(k, all){ all[k] = this.get(k); }, {});
            },
            set: function(key, data, overwrite) {
                var d = this.get(key);
                if (d != null && overwrite === false) {
                    return data;
                }
                return _.set(this._area, this._in(key), _.stringify(data), overwrite) || d;
            },
            setAll: function(data, overwrite) {
                var changed, val;
                for (var key in data) {
                    val = data[key];
                    if (this.set(key, val, overwrite) !== val) {
                        changed = true;
                    }
                }
                return changed;
            },
            remove: function(key) {
                var d = this.get(key);
                _.remove(this._area, this._in(key));
                return d;
            },
            clear: function() {
                if (!this._ns) {
                    _.clear(this._area);
                } else {
                    this.each(function(k){ _.remove(this._area, this._in(k)); }, 1);
                }
                return this;
            },
            clearAll: function() {
                var area = this._area;
                for (var id in _.areas) {
                    if (_.areas.hasOwnProperty(id)) {
                        this._area = _.areas[id];
                        this.clear();
                    }
                }
                this._area = area;
                return this;
            },

            // internal use functions
            _in: function(k) {
                if (typeof k !== "string"){ k = _.stringify(k); }
                return this._ns ? this._ns + k : k;
            },
            _out: function(k) {
                return this._ns ?
                    k && k.indexOf(this._ns) === 0 ?
                        k.substring(this._ns.length) :
                        undefined : // so each() knows to skip it
                    k;
            }
        },// end _.storeAPI
        storageAPI: {
            length: 0,
            has: function(k){ return this.items.hasOwnProperty(k); },
            key: function(i) {
                var c = 0;
                for (var k in this.items){
                    if (this.has(k) && i === c++) {
                        return k;
                    }
                }
            },
            setItem: function(k, v) {
                if (!this.has(k)) {
                    this.length++;
                }
                this.items[k] = v;
            },
            removeItem: function(k) {
                if (this.has(k)) {
                    delete this.items[k];
                    this.length--;
                }
            },
            getItem: function(k){ return this.has(k) ? this.items[k] : null; },
            clear: function(){ for (var k in this.list){ this.removeItem(k); } },
            toString: function(){ return this.length+' items in '+this.name+'Storage'; }
        }// end _.storageAPI
    };

    // setup the primary store fn
    if (window.store){ _.conflict = window.store; }
    var store =
        // safely set this up (throws error in IE10/32bit mode for local files)
        _.Store("local", (function(){try{ return localStorage; }catch(e){}})());
    store.local = store;// for completeness
    store._ = _;// for extenders and debuggers...
    // safely setup store.session (throws exception in FF for file:/// urls)
    store.area("session", (function(){try{ return sessionStorage; }catch(e){}})());

    //Expose store to the global object
    window.store = store;

    if (typeof define === 'function' && define.amd !== undefined) {
        define(function () {
            return store;
        });
    } else if (typeof module !== 'undefined' && module.exports) {
        module.exports = store;
    }

})(this, this.define);

/* 
    基于jQuery插件集合
 */
(function(){
    /* window.feature */
    !function(t, e, n) {
        "use strict";
        var i = e.documentElement,
        a = {
            create: function(t) {
                return e.createElement(t)
            },
            old: !!/(Android\s(1.|2.))|(Silk\/1.)/i.test(navigator.userAgent),
            pfx: function() {
                var t = e.createElement("dummy").style,
                i = ["Webkit", "Moz", "O", "ms"],
                a = {};
                return function(e) {
                    if ("undefined" == typeof a[e]) {
                        var o = e.charAt(0).toUpperCase() + e.substr(1),
                        r = (e + " " + i.join(o + " ") + o).split(" ");
                        a[e] = null;
                        for (var s in r) if (t[r[s]] !== n) {
                            a[e] = r[s];
                            break
                        }
                    }
                    return a[e]
                }
            } ()
        },
        o = {
            css3Dtransform: function() {
                var t = !a.old && null !== a.pfx("perspective");
                return !! t
            } (),
            cssTransform: function() {
                var t = !a.old && null !== a.pfx("transformOrigin");
                return !! t
            } (),
            cssTransition: function() {
                var t = null !== a.pfx("transition");
                return !! t
            } (),
            addEventListener: !!t.addEventListener,
            querySelectorAll: !!e.querySelectorAll,
            matchMedia: !!t.matchMedia,
            deviceMotion: "DeviceMotionEvent" in t,
            deviceOrientation: "DeviceOrientationEvent" in t,
            contextMenu: "contextMenu" in i && "HTMLMenuItemElement" in t,
            classList: "classList" in i,
            placeholder: "placeholder" in a.create("input"),
            localStorage: function() {
                var t = "x";
                try {
                    return localStorage.setItem(t, t),
                    localStorage.removeItem(t),
                    !0
                } catch(e) {
                    return ! 1
                }
            } (),
            historyAPI: t.history && "pushState" in t.history,
            serviceWorker: "serviceWorker" in navigator,
            viewportUnit: function(t) {
                try {
                    t.style.width = "1vw";
                    var e = "" !== t.style.width;
                    return !! e
                } catch(n) {
                    return ! 1
                }
            } (a.create("dummy")),
            remUnit: function(t) {
                try {
                    t.style.width = "1rem";
                    var e = "" !== t.style.width;
                    return !! e
                } catch(n) {
                    return ! 1
                }
            } (a.create("dummy")),
            canvas: function(t) {
                return ! (!t.getContext || !t.getContext("2d"))
            } (a.create("canvas")),
            svg: !!e.createElementNS && !!e.createElementNS("http://www.w3.org/2000/svg", "svg").createSVGRect,
            webGL: function(e) {
                try {
                    return ! (!t.WebGLRenderingContext || !e.getContext("webgl") && !e.getContext("experimental-webgl"))
                } catch(n) {
                    return ! 1
                }
            } (a.create("canvas")),
            cors: "XMLHttpRequest" in t && "withCredentials" in new XMLHttpRequest,
            touch: !!("ontouchstart" in t || t.navigator && t.navigator.msPointerEnabled && t.MSGesture || t.DocumentTouch && e instanceof DocumentTouch),
            async: "async" in a.create("script"),
            defer: "defer" in a.create("script"),
            geolocation: "geolocation" in navigator,
            srcset: "srcset" in a.create("img"),
            sizes: "sizes" in a.create("img"),
            pictureElement: "HTMLPictureElement" in t,
            testAll: function() {
                var t = " js";
                for (var e in this)"testAll" !== e && "constructor" !== e && this[e] && (t += " " + e);
                i.className += t.toLowerCase()
            }
        };

        t.feature = o
    } (window, document);

    /* window.deviec */
    (function() {
        var a, o, r, s, d, l, c, u, f, h;
        o = window.device,
        a = {},
        window.device = a,
        s = window.document.documentElement,
        h = window.navigator.userAgent.toLowerCase(),
        a.ios = function() {
            return a.iphone() || a.ipod() || a.ipad()
        },
        a.iphone = function() {
            return ! a.windows() && d("iphone")
        },
        a.ipod = function() {
            return d("ipod")
        },
        a.ipad = function() {
            return d("ipad")
        },
        a.android = function() {
            return ! a.windows() && d("android")
        },
        a.androidPhone = function() {
            return a.android() && d("mobile")
        },
        a.androidTablet = function() {
            return a.android() && !d("mobile")
        },
        a.blackberry = function() {
            return d("blackberry") || d("bb10") || d("rim")
        },
        a.blackberryPhone = function() {
            return a.blackberry() && !d("tablet")
        },
        a.blackberryTablet = function() {
            return a.blackberry() && d("tablet")
        },
        a.windows = function() {
            return d("windows")
        },
        a.windowsPhone = function() {
            return a.windows() && d("phone")
        },
        a.windowsTablet = function() {
            return a.windows() && d("touch") && !a.windowsPhone()
        },
        a.fxos = function() {
            return (d("(mobile;") || d("(tablet;")) && d("; rv:")
        },
        a.fxosPhone = function() {
            return a.fxos() && d("mobile")
        },
        a.fxosTablet = function() {
            return a.fxos() && d("tablet")
        },
        a.meego = function() {
            return d("meego")
        },
        a.cordova = function() {
            return window.cordova && "file:" === location.protocol
        },
        a.nodeWebkit = function() {
            return "object" == typeof window.process
        },
        a.mobile = function() {
            return a.androidPhone() || a.iphone() || a.ipod() || a.windowsPhone() || a.blackberryPhone() || a.fxosPhone() || a.meego()
        },
        a.tablet = function() {
            return a.ipad() || a.androidTablet() || a.blackberryTablet() || a.windowsTablet() || a.fxosTablet()
        },
        a.desktop = function() {
            return ! a.tablet() && !a.mobile()
        },
        a.television = function() {
            var t;
            for (television = ["googletv", "viera", "smarttv", "internet.tv", "netcast", "nettv", "appletv", "boxee", "kylo", "roku", "dlnadoc", "roku", "pov_tv", "hbbtv", "ce-html"], t = 0; t < television.length;) {
                if (d(television[t])) return ! 0;
                t++
            }
            return ! 1
        },
        a.portrait = function() {
            return window.innerHeight / window.innerWidth > 1
        },
        a.landscape = function() {
            return window.innerHeight / window.innerWidth < 1
        },
        a.noConflict = function() {
            return window.device = o,
            this
        },
        d = function(t) {
            return - 1 !== h.indexOf(t)
        },
        c = function(t) {
            var e;
            return e = new RegExp(t, "i"),
            s.className.match(e)
        },
        r = function(t) {
            var e = null;
            c(t) || (e = s.className.replace(/^\s+|\s+$/g, ""), s.className = e + " " + t)
        },
        f = function(t) {
            c(t) && (s.className = s.className.replace(" " + t, ""))
        },
        a.ios() ? a.ipad() ? r("ios ipad tablet") : a.iphone() ? r("ios iphone mobile") : a.ipod() && r("ios ipod mobile") : a.android() ? r(a.androidTablet() ? "android tablet": "android mobile") : a.blackberry() ? r(a.blackberryTablet() ? "blackberry tablet": "blackberry mobile") : a.windows() ? r(a.windowsTablet() ? "windows tablet": a.windowsPhone() ? "windows mobile": "desktop") : a.fxos() ? r(a.fxosTablet() ? "fxos tablet": "fxos mobile") : a.meego() ? r("meego mobile") : a.nodeWebkit() ? r("node-webkit") : a.television() ? r("television") : a.desktop() && r("desktop"),
        a.cordova() && r("cordova"),
        l = function() {
            a.landscape() ? (f("portrait"), r("landscape")) : (f("landscape"), r("portrait"))
        },
        u = Object.prototype.hasOwnProperty.call(window, "onorientationchange") ? "orientationchange": "resize",
        window.addEventListener ? window.addEventListener(u, l, !1) : window.attachEvent ? window.attachEvent(u, l) : window[u] = l,
        l(),
        console.log('device',window.device)
        //!(void 0 !== i && (t.exports = i))
    }).call(this);
    /* window.dm */
    (function(){
        window.dm = window.dm || {},
        $.each(["Function", "String", "Number", "Date", "RegExp", "Error"],
        function(t, e) {
            dm["is" + e] = function(t) {
                return toString.call(t) === "[object " + e + "]"
            }
        }),
        dm.parseQS = function(t) {
            null == t && (t = window.location.search),
            t = t.replace(/^\?/, "");
            for (var e = t.split("&"), n = {},
            i = 0, a = e.length; a > i; i++) {
                var o = e[i],
                r = o.indexOf("=");
                if ( - 1 !== r) {
                    var s = o.substr(r + 1);
                    s = decodeURIComponent(s),
                    n[o.substr(0, r)] = s
                }
            }
            return n
        },
        dm.qs = dm.parseQS(),
        !
        function() {
            var t, e = "protocol hostname host pathname port search hash href".split(" ");
            dm.parseUri = function(n) {
                t || (t = document.createElement("a")),
                t.href = n;
                for (var i = {},
                a = 0; 8 > a; a++) i[e[a]] = t[e[a]];
                return i
            }
        } (),
        dm.redirect = function(t) {
            location.href = t && t.nextUrl || dm.qs.redirect_url || serverData.redirectUrl || localStorage.getItem("redirect_url") || "/"
        },
        // ?
        dm.joinUrlSearch = function(t, e) {
            var n = dm.parseUri(t),
            i = dm.parseQS(n.search);
            null == e && (e = location.search),
            "string" == typeof e && (e = dm.parseQS(e));
            var a = $.param($.extend({},
            e, i));
            return a && (a = "?" + a),
            n.pathname + a
        },
        // ?
        $.fn.flushStyle = function(t) {
            return this.each(function() {
                this.offsetWidth,
                setTimeout($.proxy(t, this), 0)
            })
        },
        // 10000 return 1
        dm.wan = function(t, e) {
            var n = Math.pow(10, e || 4);
            return Math.round(t / 1e4 * n) / n
        },
        dm.round = function(t, e) {
            var n = Math.pow(10, e || 0);
            return Math.round(t * n) / n
        },
        dm.ceil = function(t, e) {
            var n = Math.pow(10, e || 0);
            return Math.ceil(t * n) / n
        },
        dm.floor = function(t, e) {
            var n = Math.pow(10, e || 0);
            return Math.floor(t * n) / n
        },
        // 1000000 return "1,000,000"
        dm.sepThousand = function(t) {
            var e = t.toString(),
            n = e.indexOf(".");
            return e.replace(/\d(?=(?:\d{3})+(?:\.|$))/g,
            function(t, e) {
                return 0 > n || n > e ? t + ",": t
            })
        },
        // 23494330538603654 return "2349 4330 5386 0365 6"
        dm.sepCardNo = function(t) {
            return ("" + t).replace(/\D/g, "").replace(/....(?!$)/g, "$& ")
        },
        // dm.dateString(new Date(),"yyyy-mm-dd hh:ii:ss") return "2016-06-22 11:48:14"
        dm.dateString = function(t, e) {
            if (! (t instanceof Date)) {
                var n = +t;
                if (n ? (4102416e3 > n && (n = 1e3 * n), t = new Date(n)) : t = new Date(t), !t.getTime()) return "Invalid Date"
            }
            e || (e = "yyyy-mm-dd hh:ii");
            var i = {
                "m+": t.getMonth() + 1,
                "d+": t.getDate(),
                "h+": t.getHours(),
                "i+": t.getMinutes(),
                "s+": t.getSeconds(),
                "q+": Math.floor((t.getMonth() + 3) / 3),
                S: t.getMilliseconds()
            };
            /(y+)/.test(e) && (e = e.replace(RegExp.$1, (t.getFullYear() + "").slice(4 - RegExp.$1.length)));
            for (var a in i) new RegExp("(" + a + ")").test(e) && (e = e.replace(RegExp.$1, 1 === RegExp.$1.length ? i[a] : ("00" + i[a]).slice(("" + i[a]).length)));
            return e
        },
        // dm.urlReplace('abc','http://wwww.baidu.com/xxx/0000') return "http://wwww.baidu.com/xxx/abc"
        dm.urlReplace = function(t, e) {
            e = e.replace(/0{4,}/g, t);
            var n = arguments.length;
            if (n > 2) for (var i = 2; n > i; i++) {
                var a = i - 1;
                e = e.replace(new RegExp("" + a + "{4,}", "g"), arguments[i])
            }
            return e
        },
        dm.getImgSize = function(t, e) {
            var n = new Image;
            n.onload = function() {
                e(n.width, n.height)
            },
            n.src = t
        },
        dm.projInvestStageName = function(t) {
            return {
                0 : "运行中",
                1 : "成功退出",
                2 : "项目失败"
            } [t]
        },
        dm.projStageName = function(t) {
            return {
                1 : "预热中",
                4 : "合投中",
                5 : "融资打款中",
                8 : "融资结束",
                9 : "融资成功"
            } [t]
        },
        dm.projStage = function(t) {
            return {
                1 : "yrz",
                4 : "htz",
                5 : "dkz",
                8 : "end",
                9 : "success"
            } [t]
        },
        dm.projValueChangeType = function(t) {
            return {
                0 : "众筹",
                1 : "收购",
                2 : "增资"
            } [t]
        },
        dm.profitType = function(t) {
            return {
                1 : "转让",
                2 : "投资",
                3 : "分红"
            } [t]
        },
        dm.afterStatus = function(t) {
            return {
                0 : "有效投资",
                1 : "转让中",
                2 : "转让成功",
                3 : "退出处理中",
                4 : "退出成功"
            } [t]
        },
        dm.stripTag = function(t) {
            return null == t ? "": t.replace(/<([^>]+)>/gi, "")
        },
        dm.trim = function(t, e, n) {
            t = $.trim(t);
            var i = t.length;
            return e && (t = t.slice(0, e)),
            n && i > e && (t += n),
            t
        },
        dm.back = function() {
            var t = location.href,
            e = !1;
            history.back(),
            window.onbeforeunload = function() {
                e = !0
            },
            setTimeout(function() {
                e || location.href !== t || (location.href = "/")
            },
            50)
        },
        dm.backRefresh = function(t) {
            document.referrer ? location.href = document.referrer: t ? location.href = t: history.back()
        },
        dm.needLogin = function() {
            dm.confirm("您还未登录，请登录后重试，点击确定前往登录页面",
            function() {
                location.href = api.login
            })
        },
        dm.isAPP = function() {
            return /dreammove/.test(window.navigator.userAgent.toLowerCase())
        } (),
        dm.isAndroid = function() {
            return /dreammove-android/.test(window.navigator.userAgent.toLowerCase())
        } (),
        dm.isIOS = function() {
            return /dreammove-ios/.test(window.navigator.userAgent.toLowerCase())
        } (),
        dm.callAPP = function(t) {
            if (dm.isIOS) {
                var e;
                e = document.createElement("iframe"),
                e.setAttribute("src", "dreammove://native?function=" + t),
                e.setAttribute("style", "display:none;"),
                e.setAttribute("height", "0px"),
                e.setAttribute("width", "0px"),
                e.setAttribute("frameborder", "0"),
                document.body.appendChild(e),
                e.parentNode.removeChild(e),
                e = null
            } else dm.isAndroid && JSBridge && JSBridge[t] && JSBridge[t]()
        }
    })();

    /* jquery modal */
    (function(){
        if (window.dm = window.dm || {},
        null === window.jQuery) throw new Error("modal.js requires jQuery");
        if (null == window.feature) throw new Error("modal.js requires feature");
        if (null == window.device) throw new Error("modal.js requires device"); !
        function(t) {
            "use strict";
            var e = function() {
                var t = document.createElement("div"),
                e = {
                    WebkitTransition: "webkitTransitionEnd",
                    MozTransition: "transitionend",
                    OTransition: "oTransitionEnd otransitionend",
                    transition: "transitionend"
                };
                for (var n in e) {
                    var i = e[n];
                    if (null != t.style[n]) return i
                }
                return ! 1
            } ();
            t.fn.emulateTransitionEnd = function(t) {
                var n = !1,
                i = this;
                i.one("dmTransitionEnd",
                function() {
                    return n = !0
                });
                var a = function() {
                    return n ? void 0 : i.trigger(e || "")
                };
                return setTimeout(a, t),
                this
            },
            t(function() {
                e && (t.event.special.dmTransitionEnd = {
                    bindType: e,
                    delegateType: e,
                    handle: function(e) {
                        return t(e.target).is(this) ? e.handleObj.handler.apply(this, arguments) : void 0
                    }
                })
            })
        } (jQuery),
        !
        function(t) {
            "use strict";
            var e = 150,
            n = 150,
            i = {
                backdrop: !0,
                keyboard: !0,
                show: !0,
                backdropColor: ""
            },
            a = function() {
                function i(e, n) {
                    var i = this;
                    this.options = n,
                    this.$body = t(document.body),
                    this.$modal = t(e),
                    this.$dialog = this.$modal.find(".modal-dialog"),
                    this.$backdrop = null,
                    this.isShown = null,
                    this.originalBodyPad = null,
                    this.scrollbarWidth = 0,
                    this.ignoreBackdropClick = !1,
                    this.options.remote && this.options.remote.indexOf('javascript')==-1 && this.$modal.find(".modal-content").load(this.options.remote,
                    function() {
                        return i.$modal.trigger("load")
                    })
                }
                return i.prototype.toggle = function() {
                    return this.isShown ? this.hide() : this.show(),
                    this
                },
                i.prototype.show = function() {
                    var n, i, a, o;
                    return o = this,
                    a = t.Event("show"),
                    this.$modal.trigger(a),
                    this.isShown || a.isDefaultPrevented() ? void 0 : (this.isShown = !0, this.checkScrollbar().setScrollbar(), this.$body.addClass("modal-open"), this.escapeEvent().resizeEvent(), n = this.$body, i = n.data("openModals"), null != i ? (i += 1, n.data("openModals", i)) : (i = 1, n.data("openModals", 1)), this.$modal.css("z-index", 1040 + 10 * i), this.$modal.on("click.dismiss", '[data-dismiss="modal"]',
                    function() {
                        return o.hide()
                    }), this.$dialog.on("mousedown.dismiss",
                    function() {
                        return o.$modal.one("mouseup.dismiss",
                        function(e) {
                            return t(e.target).is(o.$modal) ? o.ignoreBackdropClick = !0 : void 0
                        })
                    }), this.backdrop(function() {
                        var n, i;
                        i = feature.cssTransition && o.$modal.hasClass("fade"),
                        o.$modal.parent().length || o.$modal.appendTo(o.$body),
                        o.$modal.show().scrollTop(0),
                        o.adjustDialog(),
                        i && o.$modal[0].offsetWidth,
                        o.$modal.addClass("in"),
                        o.enforceFocus(),
                        n = t.Event("shown"),
                        i ? o.$dialog.one("dmTransitionEnd",
                        function() {
                            o.$modal.trigger("focus").trigger(n)
                        }).emulateTransitionEnd(e) : o.$modal.trigger("focus").trigger(n)
                    }), this)
                },
                i.prototype.hide = function() {
                    var n = t.Event("hide");
                    return this.$modal.trigger(n),
                    this.isShown && !n.isDefaultPrevented() ? (this.isShown = !1, this.escapeEvent().resizeEvent(), this.$body.data("openModals", this.$body.data("openModals") - 1), t(document).off("focusin.modal"), this.$modal.removeClass("in").off("click.dismiss"), feature.cssTransition && this.$modal.hasClass("fade") ? this.$modal.one("dmTransitionEnd", t.proxy(this.hideModal, this)).emulateTransitionEnd(e) : this.hideModal(), this) : void 0
                },
                i.prototype.enforceFocus = function() {
                    var e = this;
                    return t(document).off("focusin.modal").on("focusin.modal",
                    function(t) {
                        return e.$modal[0] === t.target || e.$modal.has(t.target).length ? void 0 : e.$modal.trigger("focus")
                    }),
                    this
                },
                i.prototype.escapeEvent = function() {
                    var t;
                    return t = this,
                    this.isShown && this.options.keyboard ? this.$modal.on("keydown.dismiss",
                    function(e) {
                        27 === e.which && t.hide()
                    }) : this.isShown || t.$modal.off("keydown.dismiss"),
                    this
                },
                i.prototype.resizeEvent = function() {
                    var e;
                    return e = this,
                    this.isShown ? t(window).on("resize.modal",
                    function() {
                        return e.handleUpdate()
                    }) : t(window).off("resize.modal"),
                    this
                },
                i.prototype.hideModal = function() {
                    var t;
                    return t = this,
                    this.$modal.hide(),
                    this.backdrop(function() {
                        return 0 === t.$body.data("openModals") && (t.$body.removeClass("modal-open"), t.resetAdjustments().resetScrollbar()),
                        t.$modal.trigger("hidden")
                    }),
                    this
                },
                i.prototype.removeBackdrop = function() {
                    return this.$backdrop && this.$backdrop.remove(),
                    this.$backdrop = null,
                    this
                },
                i.prototype.backdrop = function(e) {
                    var i, a, o, r, s;
                    if (s = this, i = this.$modal.hasClass("fade") ? " fade": "", this.isShown && this.options.backdrop) {
                        if (r = feature.cssTransition && i, a = this.options.backdropColor || "", s.lastVisibleModal = t(".modal-backdrop").filter(".in").removeClass("in"), this.$backdrop = t('<div class="modal-backdrop' + i + " " + a + '"></div>').css("z-index", 10 * this.$body.data("openModals") + 1039).appendTo(this.$body), this.$modal.on("click.dismiss",
                        function(t) {
                            return s.ignoreBackdropClick ? void(s.ignoreBackdropClick = !1) : void(t.target === t.currentTarget && ("static" === s.options.backdrop ? s.$modal[0].focus() : s.hide()))
                        }), r && this.$backdrop[0].offsetWidth, this.$backdrop.addClass("in"), !e) return;
                        r ? this.$backdrop.one("dmTransitionEnd", e).emulateTransitionEnd(n) : e()
                    } else ! this.isShown && this.$backdrop ? (this.$backdrop.removeClass("in"), s.lastVisibleModal.addClass("in"), o = function() {
                        return s.removeBackdrop(),
                        e && e()
                    },
                    feature.cssTransition && this.$modal.hasClass("fade") ? this.$backdrop.one("dmTransitionEnd", o).emulateTransitionEnd(n) : o()) : e && e();
                    return this
                },
                i.prototype.handleUpdate = function() {
                    return this.adjustDialog(),
                    this
                },
                i.prototype.adjustDialog = function() {
                    var t;
                    t = this.$modal[0].scrollHeight > document.documentElement.clientHeight,
                    this.$modal.css({
                        paddingLeft: !this.bodyIsOverflowing && t ? this.scrollbarWidth: "",
                        paddingRight: this.bodyIsOverflowing && !t ? this.scrollbarWidth: ""
                    });
                    var e = this.$modal.data("mt");
                    return null == e && (e = (document.documentElement.clientHeight - this.$dialog.outerHeight()) / 2 - 30),
                    this.$dialog.css("margin-top", e),
                    this
                },
                i.prototype.resetAdjustments = function() {
                    return this.$modal.css({
                        paddingLeft: "",
                        paddingRight: ""
                    }),
                    this
                },
                i.prototype.checkScrollbar = function() {
                    var t, e;
                    return e = window.innerWidth,
                    e || (t = document.documentElement.getBoundingClientRect(), e = t.right - Math.abs(t.left)),
                    this.bodyIsOverflowing = document.body.clientWidth < e,
                    this.scrollbarWidth = this._measureScrollbar(),
                    this
                },
                i.prototype.setScrollbar = function() {
                    var e = parseInt(this.$body.css("padding-right") || 0, 10);
                    return this.bodyIsOverflowing && !t(document.body).hasClass("modal-open") && this.$body.css("padding-right", e + this.scrollbarWidth),
                    t(document.body).hasClass("modal-open") ? this.originalBodyPad = e - this.scrollbarWidth: this.originalBodyPad = e,
                    this
                },
                i.prototype.resetScrollbar = function() {
                    return this.$body.css("padding-right", this.originalBodyPad),
                    this
                },
                i.prototype._measureScrollbar = function() {
                    var t, e;
                    return t = document.createElement("div"),
                    t.className = "modal-scrollbar-measure",
                    this.$body.append(t),
                    e = t.offsetWidth - t.clientWidth,
                    this.$body[0].removeChild(t),
                    e
                },
                i
            } ();
            t.fn.modal = function(e, n) {
                return this.each(function() {
                    var o = t(this),
                    r = t.extend({},
                    i, o.data(), "object" == typeof e && e),
                    s = o.data("modal");
                    s || o.data("modal", s = new a(this, r)),
                    "string" == typeof e ? s[e](n) : r.show && s.show(n)
                })
            },
            t.fn.modal.Constructor = a,
            t(document).on("click.modal", '[data-toggle="modal"]',
            function(e) {
                var n = t(this),
                i = n.attr("href"),
                a = t(n.data("target") || i && i.replace(/.*(?=#\S+$)/, "")),
                o = a.data("modal") ? "toggle": t.extend({
                    remote: !/#/.test(i) && i
                },
                a.data(), n.data());
                n.is("a") && e.preventDefault(),
                a.modal(o)
            })
        } (jQuery),
        /*
            dm.alert('这个靠谱',function(){alert('ok')},'success' || 'warning' || 'error')
        */
        dm.alert = function(t, e, n) {
            var i;
            if (n) {
                var a = {
                    success: "check-circle",
                    warning: "warning-circle",
                    error: "times-circle"
                };
                t = '<div class="modal-' + n + '-body"><i class="if icon-' + a[n] + '"></i><div>' + t + "</div></div>"
            }
            i = device && device.mobile() ? '<div class=" modal-' + n + ' modal modal-alert fade" tabindex="-1"><div class="modal-dialog"><div class="modal-content"><div class="modal-body">' + t + '</div><div class="modal-footer"><button type="button" class="btn-primary" data-dismiss="modal">确定</button></div></div></div></div>': '<div class=" modal-' + n + ' modal modal-alert fade" tabindex="-1"><div class="modal-dialog"><div class="modal-content"><div class="modal-header">提示<button type="button" class="close" data-dismiss="modal">&times;</button></div><div class="modal-body">' + t + '</div><div class="modal-footer"><button type="button" class="btn-primary" data-dismiss="modal">确定</button></div></div></div></div>';
            var o = $(i).appendTo($(document.body));
            return o.on("hidden",
            function() {
                o.remove(),
                e && e()
            }).on("shown",
            function() {
                o.find("button").focus()
            }),
            o.modal()
        },
        /*
            下方透明提醒
        */
        dm.toast = function(t, e, n) {
            dm.notice(t, e, "dm-toast")
        },
        /*
            1 dm.confirm('您还未进行实名认证, 是否立即前往实名认证', function () {alert('ok')})
            2 dm.confirm('您还未进行实名认证, 是否立即前往实名认证', {yes:'ok',no:'no',type:'success'},function () {alert('ok')})
        */
        dm.confirm = function(t, e, n, i, a) {
            var o = {
                yes: "确定",
                no: "取消"
            };
            $.isFunction(e) && (a = i, i = n, n = e),
            null == a && (a = i);
            var r, s = $.extend({},
            o, e);
            if (s.type) {
                var d = {
                    success: "check-circle",
                    warning: "warning-circle",
                    error: "times-circle"
                };
                t = '<div class="modal-' + s.type + '-body"><i class="if icon-' + d[s.type] + '"></i><div>' + t + "</div></div>"
            }
            r = device && device.mobile() ? '<div class="modal modal-confirm fade" tabindex="-1"><div class="modal-dialog"><div class="modal-content"><div class="modal-body">' + t + '</div><div class="modal-footer"><button type="button" class="btn-primary confirm" data-dismiss="modal">' + s.yes + '</button><button type="button" class="btn-o cancel" data-dismiss="modal">' + s.no + "</button></div></div></div></div>": '<div class="modal modal-confirm fade" tabindex="-1"><div class="modal-dialog"><div class="modal-content"><div class="modal-header">提示<button type="button" class="close" data-dismiss="modal">&times;</button></div><div class="modal-body">' + t + '</div><div class="modal-footer"><button type="button" class="btn-primary confirm" data-dismiss="modal">' + s.yes + '</button><button type="button" class="btn-o cancel" data-dismiss="modal">' + s.no + "</button></div></div></div></div>";
            var l = $(r).appendTo($(document.body));
            return l.on("shown",
            function() {
                l.find(".confirm").focus()
            }).on("hidden",
            function() {
                l.remove(),
                a && a()
            }),
            l.on("click", ".confirm",
            function(t) {
                t.stopPropagation(),
                l.remove(),
                n && n()
            }).on("click", ".cancel",
            function(t) {
                t.stopPropagation(),
                l.remove(),
                i && i()
            }),
            l.modal()
        },
        dm.notice = function(t, e, n) {
            null == n && (n = "dm-notice");
            var i = $("." + n);
            if (i.length) {
                var a = i.children(".text-wrap");
                i.removeClass("in"),
                a.html(t)
            } else i = $('<div class="' + n + '"><div class="text-wrap">' + t + "</div></div>").appendTo("body");
            i.off("dmTransitionEnd").flushStyle().addClass("in"),
            clearTimeout(i.data("outTimer"));
            var o = 300 * Math.max($("<a>" + t + "</a>").text().length, 10);
            o = Math.min(3e4, o),
            i.data("outTimer", setTimeout(function() {
                i.removeClass("in").one("dmTransitionEnd",
                function() {
                    $(this).remove(),
                    e && e()
                }).emulateTransitionEnd(400)
            },
            o))
        },
        !
        function(t) {
            t.fn.note = function(e) {
                return this.each(function() {
                    var n = t(this);
                    n.on("click.dismiss", "[data-dismiss=note]",
                    function() {
                        n.fadeOut()
                    }),
                    n.find(".html").html(e),
                    n.fadeIn()
                })
            }
        } (jQuery),
        $(function() {
            $(".note:visible").note()
        }),
        dm.loading = function(t) {
            var e = $(".modal-loading");
            null == t && (t = ""),
            "hide" === t ? e.modal("hide") : e.length ? e.find("var").html(t || "") : (e = $('<div class="modal modal-loading fade"><div class="modal-dialog"><var>' + t + "</var></div></div>").appendTo(document.body), e.on("hidden",
            function() {
                e.remove()
            }), e.modal({
                backdrop: "static",
                backdropColor: "white"
            }))
        },
        !
        function($) {
            $.fn.blockLoading = function(options) {
                var tpl = '<div class="loading-message "><div class="block-spinner-bar"><div class="bounce1"></div><div class="bounce2"></div><div class="bounce3"></div></div></div>';
                return this.each(function() {
                    var n = $(this);

                    if ("hide" === options){n.find('.loading-message').remove();} 
                    else {
                        if("absolute" == options){
                            n.css('position', 'relative');
                            $tpl = $(tpl).css({position:'absolute',width:'100%',height:'100%',left:0,top:0,backgroundColor:'rgba(255,255,255,0.5)'}).appendTo(n);
                            
                        }else {
                            n.html(tpl)
                        }
                        
                    }

                    
                });
            }
        } (jQuery),
        !
        !
        function(t) {
            t.fn.loading = function(e) {
                return this.each(function() {
                    var n = t(this);
                    if ("hide" === e) n.removeClass("dm-loading"),
                    n.find(".dm-loading-text").remove();
                    else if (n.addClass("dm-loading"), e) {
                        var i = n.find(".dm-loading-text");
                        i.length ? i.html(e) : n.append('<div class="dm-loading-text">' + e + "</div>")
                    }
                })
            }
        } (jQuery)
    })();


    /* jquery bubble */
    (function($){
        $.fn.bubble = function (msg, direction, duration) {
            var $bubble, caretDirection, ref;
            if (direction == null) {
                direction = 'down';
            }

            msg = (ref = this.data('bubbleMsg')) != null ? ref : msg;
            direction = this.data('bubbleDir') || direction;
            duration = this.data('bubbleDuration') || duration || Math.max(msg.length, 8) * 500;
            $bubble = this.siblings('.bubble');
            caretDirection = {
                'up': 'down',
                'down': 'up',
                'right': 'left',
                'left': 'right'
            };
            
            if ($bubble.length === 0) {
                $bubble = $('<div class="bubble bubble-' + direction + ' caret-' + caretDirection[direction] + '"></div>').insertAfter(this);
            }
            $bubble.html(msg);
            if (direction === 'right') {
                $bubble.css({
                    'left': this.data('bubbleLeft') != null ? this.data('bubbleLeft') : this.outerWidth() + 8,
                    'top': this.data('bubbleTop') != null ? this.data('bubbleTop') : (this.outerHeight() - $bubble.outerHeight()) / 2
                });
            } else if (direction === 'down') {
                $bubble.css({
                    'left': this.data('bubbleLeft') != null ? this.data('bubbleLeft') : this.outerWidth() * 0.5,
                    'top': this.data('bubbleTop') != null ? this.data('bubbleTop') : this.outerHeight() + 8
                });
            } else if (direction === 'up') {
                $bubble.css({
                    'left': this.data('bubbleLeft') != null ? this.data('bubbleLeft') : this.outerWidth() * 0.5,
                    'bottom': this.data('bubbleBottom') != null ? this.data('bubbleBottom') : this.outerHeight() + 8
                });
            }
            clearTimeout($bubble.data('bubbleTimer'));
            $bubble.data('bubbleTimer', setTimeout(function () {
                return $bubble.removeBubble();
            }, duration));
            return this;
        };
        $.fn.removeBubble = function () {
            if ($(this).hasClass('bubble')) {
                this.remove();
            } else {
                this.siblings('.bubble').remove();
            }
            return this;
        };

    })(jQuery);

    /* validate */
    (function ($) {
        var ruleMethods = {
            china_name:function(value){
                var re = /[\u4E00-\u9FA5\uF900-\uFA2D]/;

                if(!value){
                    return false;
                }
                if(value.length<2){
                    return false;
                }
                if(/[A-Za-z]/.test(value)){
                    return false;
                }
                if(re.test(value)){
                    return true;
                }

                return false;
            },
            idcard:function(value) {
                var idCard = value;
                var Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 ];// 加权因子  
                var ValideCode = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ];// 身份证验证位值.10代表X 
                /** 
                 * 判断身份证号码为18位时最后的验证位是否正确 
                 * @param a_idCard 身份证号码数组 
                 * @return 
                 */ 
                var isTrueValidateCodeBy18IdCard = function(idCard) {  
                    var sum = 0; // 声明加权求和变量  
                    var a_idCard=idCard.split("");
                       if (a_idCard[17].toLowerCase() == 'x') {  
                            a_idCard[17] = 10;// 将最后位为x的验证码替换为10方便后续操作  
                        }
                        for ( var i = 0; i < 17; i++) {  
                            sum += Wi[i] * a_idCard[i];// 加权求和  
                        }
                        if (a_idCard[17] == ValideCode[sum % 11]) {  
                            return true;  
                        } else {  
                            return false;  
                        }  
                };
                 /** 
                  * 验证18位数身份证号码中的生日是否是有效生日 
                  * @param idCard 18位书身份证字符串 
                  * @return 
                  */ 
                var isValidityBrithBy18IdCard = function(idCard18){  
                    var year =  idCard18.substring(6,10);  
                    var month = idCard18.substring(10,12);  
                    var day = idCard18.substring(12,14);  
                    temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));  
                    // 这里用getFullYear()获取年份，避免千年虫问题  
                    if(temp_date.getFullYear()!=parseFloat(year)  
                          ||temp_date.getMonth()!=parseFloat(month)-1  
                          ||temp_date.getDate()!=parseFloat(day)){  
                            return false;  
                    }else{  
                        temp_date = year+"-"+month+"-"+day; 
                        return true;  
                    }  
                };
                /** 
                 * 验证15位数身份证号码中的生日是否是有效生日 
                 * @param idCard15 15位书身份证字符串 
                 * @return 
                 */ 
                var isValidityBrithBy15IdCard = function(idCard15){  
                    var year =  idCard15.substring(6,8);  
                    var month = idCard15.substring(8,10);  
                    var day = idCard15.substring(10,12);  
                    var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));  
                    // 对于老身份证中的你年龄则不需考虑千年虫问题而使用getYear()方法  
                    if(temp_date.getYear()!=parseFloat(year)  
                            ||temp_date.getMonth()!=parseFloat(month)-1  
                            ||temp_date.getDate()!=parseFloat(day)){  
                              return false;  
                    }else{  
                        return true;  
                    }  
                };
                if(idCard){
                    if(/^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/.test(idCard)){
                        if(idCard.length == 15 && isValidityBrithBy15IdCard(idCard)){  
                            return true;
                        }else if(idCard.length == 18 && isValidityBrithBy18IdCard(idCard) && isTrueValidateCodeBy18IdCard(idCard)){  
                            return true;  
                        }else{
                            
                            return false; 
                        }
                    }else{
                        return false; 
                    }
                }
            },
            // 6-20位 必须包含数字和英文
            password:function(value){
                if(!value || value.length<6 || value.length>20) {
                    return false;
                } else {
                    if(/[A-Za-z].*[0-9]|[0-9].*[A-Za-z]/.test(value)){
                        return true;
                    }else {
                        return false;
                    }
                    
                }
                
            },
            password2:function(value){
                var pa1 = 1;
                if(!value){}
            },
            // 6-16位,支持数字、字母和_、－、／
            username:function(value){
                var re = new RegExp("['\"&\\n\r\t\b\f@,;%/]");
                if(!value || value.length<6 || value.length>16 || re.test(value)) {
                    return false;
                } else {
                    return true;
                }
                
            },
            //手机号码规则：可以以0开头+三位固定号段+8为数字*/
            cellphone:function(value){
                //170是虚拟号段
                /*移动号段16个
                 *  134、135、136、137、138、139、147、150、151、152、157、158、159、182、183、187、188 170  
                 */
                var pattern1 = /^0{0,1}(13[4-9]|147|15[0-2]|15[7-9]|18[23478]|178|170)[0-9]{8}$/;  
                /*联通号段7个  
                130、131、132、155、156、185、186  
                */  
                var pattern2 = /^0{0,1}(13[0-2]|15[56]|145|18[56]|176)[0-9]{8}$/;  
                /*电信号段4个  
                133、153、180、189   
                */  
                var pattern3 = /^0{0,1}(133|153|180|181|189|177)[0-9]{8}$/;
                if(value!='' && !pattern1.test(value) && !pattern2.test(value) 
                        && !pattern3.test(value)) {
                    //this.ErrorMessage[name] = this.messages['cellphone'];
                    return false;
                } else {
                    return true;
                }
            },
        };
        /*
         元素中存储的变量:
         input: ruleList(规则列表对象), readyValidate(已验证过的字段, 处于"热"状态, 随打字响应)
         form: bubbled(已有弹出)
         */
        var defaultOption, initForm, initInput, matchRule, msgInfo, patternInfo, validateForm, validateInput;
        patternInfo = {
            required: /\S+/,
            email: /^[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$/,
            url: /^\s*((https?|ftp):\/\/)?(([^:\n\r]+):([^@\n\r]+)@)?((www\.)?([^\/\n\r]+))\/?([^?\n\r]+)?\??([^#\n\r]*)?#?([^\n\r]*)$/,
            tel: /^\s*(?:\+?(\d{1,3}))?([-. (]*(\d{3})[-. )]*)?((\d{3})[-. ]*(\d{2,4})(?:[-.x ]*(\d+))?)\s*$/,
            number: /^\d[\d\.]*$/
        };
        msgInfo = {
            required: '请填写此字段。',
            email: 'email 不合法',
            url: 'url 不合法',
            tel: '电话号码不合法',
            number: '不是数字',
            maxlength: '超出字数限制',
            min: '必须大于@@',
            max: '必须小于@@',

            idcard:'身份证格式有误',
            username:'格式有误 用户名6-16位',
            password:'格式有误 密码6-20位 必须包含数字和英文',
            cellphone:'手机号码不合法',
            pattern: '不合法'
        };
        // 仅支持以下验证 required email url tel number maxlength min max pattern
        matchRule = function (str, rule, pattern) {
            var result = true
            if (pattern == null) pattern = true

            switch (rule) {
                case 'required':
                    if (pattern) {
                        result = patternInfo.required.test(str);
                    }
                    break;
                case 'email':
                    if (pattern) {
                        result = patternInfo.email.test(str);
                    }
                    break;
                case 'url':
                    if (pattern) {
                        result = patternInfo.url.test(str);
                    }
                    break;
                case 'tel':
                    if (pattern) {
                        result = patternInfo.tel.test(str);
                    }
                    break;
                case 'number':
                    if (pattern) {
                        result = patternInfo.number.test(str);
                    }
                    break;
                case 'maxlength':
                    return $.trim(str).length <= pattern;
                case 'min':
                    if ($.isNumeric(pattern)) {
                        return parseFloat(str) >= parseFloat(pattern);
                    } else {
                        return new Date(str) >= new Date(pattern);
                    }
                    break;
                case 'max':
                    if ($.isNumeric(pattern)) {
                        return parseFloat(str) <= parseFloat(pattern);
                    } else {
                        return new Date(str) <= new Date(pattern);
                    }
                    break;
                case 'pattern':
                    return eval(pattern).test(str);
                default:
                    // 自定义的rules
                    
                    for(var _rule in ruleMethods) {
                        if(rule === _rule){
                            
                            var method = ruleMethods[_rule];
                            return method(str);
                        }
                    }
                    break;
            }
                    
            return result;
        };
        defaultOption = {
            batch: false,
            typing: false,
            blur: false
        };
        // 获取input的验证规则 放在input对象里  ruleList {required:true,maxlength:2,pattern:/\w/}
        $.fn.buildRuleList = function () {
            return this.each(function (idx, elm) {
                var attr, attrList, attrValue;
                var ruleList = {},
                    $elm = $(elm),
                    type = $elm.attr('type');
                ruleList['required'] = $elm.attr('required');
                var typeList = ['email', 'url', 'number'];
                for (var i = 0, len = typeList.length; i < len; i++) {
                    var _t = typeList[i];
                    if (type === _t) {
                        ruleList[_t] = true;
                        break;
                    }
                }
                attrList = ['maxlength', 'max', 'min', 'pattern'];
                for (var j = 0, len1 = attrList.length; j < len1; j++) {
                    attr = attrList[j];
                    attrValue = $elm.attr(attr);
                    if (attrValue != null && attrValue !== '') {
                        ruleList[attr] = attrValue;
                    }
                }
                var _ajax = $elm.data('ajax');
                if (_ajax) ruleList['ajax'] = _ajax;

                var _rule = $elm.data('rule');
                if (_rule) ruleList[_rule] = _rule;
                
                $elm.data('ruleList', ruleList);
            });
        };
        initForm = function ($form) {
            $form.attr('novalidate', 'novalidate').find('input, textarea, select').validate('init');
        };
        initInput = function ($input) {
            if($input.data('rule') =='checkbox' || $input.data('rule') =='radio'){
                $input.buildRuleList();
                return;
            }
            // 打字的时候验证
            $input.buildRuleList().off('.validate').on('input.validate propertychange.validate', function () {
                return $(this).validate({
                    typing: true
                })
            })
            // 默认失去焦点的时候也去验证 否则加属性 blurSilence=ture 
            if (!$input.data('blurSilence')) {
                $input.on('blur.validate', function () {
                    $(this).validate({
                        blur: true
                    })
                })
            }
        };
        validateForm = function ($form, option, success, fail) {
            var result = true
            $form.data('bubbled', 0)
            $form.find('.bubble').removeBubble();
            // 验证的对象
            $form.find('input, textarea, select,[data-rule="radio"],[data-rule="checkbox"]').validate({
                batch: true
            }, $.noop, function () {
                result = false
            })
            if (result) success()
            else fail()
        }
        // 错误信息 data-ruleName-msg='balabalabala'
        validateInput = function ($input, option, success, fail) {
            if (option.typing && !$input.data('readyValidate')) return;
            var value = $.trim($input.val()) || '',
                ruleList = $input.data('ruleList'),
                result = true,
                msg = ''
            if (!ruleList) ruleList = $input.buildRuleList().data('ruleList');

            $input.removeBubble();

            if($input.data('rule')=='radio' || $input.data('rule')=='checkbox'){
                var name = $input.data('name');
                var count = 0;
                var ruleName = $input.data('rule')
                $('[name='+name+']').each(function(){
                    if(this.checked){
                        count++
                    }
                });
                //console.log(1111,count,name)
                if(count>0){
                    result = true;
                }else {
                    result = false;
                    msg = $input.data(ruleName + 'Msg') || msgInfo[ruleName] || '格式有误';
                }

            }
            if (!(value.length === 0 && !ruleList['required'])) {
                for (var ruleName in ruleList) {
                    if (ruleList.hasOwnProperty(ruleName)) {
                        var rule = ruleList[ruleName];
                        if (!matchRule(value, ruleName, rule)) {
                            result = false;
                            //console.log(ruleName + 'Msg',$input.data(ruleName + 'Msg'))
                            msg = $input.data(ruleName + 'Msg') || msgInfo[ruleName] || '格式有误';
                            msg = msg.replace(/@@/g, rule);
                            break;
                        }
                    }
                }
            }

            // ajax
            if (result && ruleList.ajax && $input.data('lastValidateValue') !== $input.val()) {
                var doAjax = function () {
                    $input.data('ajaxReady', false)
                    $input.data('ajaxQueue', false)
                    $input.data('lastValidateValue', $input.val())
                    var _data = {}
                    _data[$input.attr('name')] = $input.val()
                    dm.getJSON(ruleList.ajax, _data, {alert: 'noop'}).fail(function (json) {
                        $input.addClass('error').bubble(json.info || json.message)
                    }).always(function () {
                        $input.data('ajaxReady', true)
                        if ($input.data('ajaxQueue')) {
                            doAjax()
                        }
                    })
                }
                var ajaxReady = $input.data('ajaxReady')
                if (ajaxReady == null || ajaxReady) {
                    doAjax()
                } else {
                    $input.data('ajaxQueue', true)
                }
            }
            // 验证格式正确
            if (result) {
                $input.removeClass('error')
                success()
            } else {
                var $form = $input.closest('form')
                if (option.typing) {
                    $input.addClass('error')
                } else {
                    $input.addClass('error')
                    if (!option.batch || !$form.data('bubbled')) {
                        $form.data('bubbled', 1)
                        $input.bubble(msg, $input.data('bubbleDir') || 'down')
                        if (!option.blur) {
                            $input.focus()
                        }
                    }
                }
                fail()
            }
            return $input.data('readyValidate', true)
        };
        return $.fn.validate = function (option, success, fail) {
            
            var method;
            if (success == null) success = $.noop
            if (fail == null) fail = $.noop
            if (typeof option === 'string') {
                method = option;
                option = success;
                if (method === 'init') {
                    
                    return this.each(function () {
                        var $this;
                        $this = $(this);
                        
                        if ($this.is('form')) {

                            return initForm($this);
                        } else {

                            return initInput($this);
                        }
                    });
                }
            } else {
                option = $.extend({}, defaultOption, option);
                return this.each(function () {
                    var $this;
                    $this = $(this)
                    if ($this.is('form')) {
                        validateForm($this, option, success, fail);
                    } else {
                        
                        validateInput($this, option, success, fail);
                    }
                });
            }
        };
    })(jQuery);

    


    
    // ...
    $.fn.prettyCheck = function() {
        
        return this.each(function() {
            var t = $(this);
            t.siblings(".dummy-check").length || t.after('<label class="dummy-check" for="' + t.attr("id") + '"></label>')
        })
    };
    
    // 图片验证码
    $.fn.reloadVerify = function() {
        return this.each(function() {
            var t = $(this),
            e = t.attr("src");~e.indexOf("?") ? t.attr("src", e + "&random=" + Math.random()) : t.attr("src", e.replace(/\?.*$/, "") + "?" + Math.random())
        })
    };
    
    // ajax
    (function(){
        !function ($) {
            $.fn.disable = function (msg, loading) {
                if (loading == null) loading = true
                return this.each(function () {
                    var $btn = $(this).prop('disabled', true),
                        _msg = msg
                    clearTimeout($btn.data('loadingTimer'))

                    var oriHTML = $btn.data('html')
                    if (oriHTML == null) {
                        oriHTML = $btn.html()
                        $btn.data('html', oriHTML)
                    }

                    if (!_msg) _msg = oriHTML
                    if (!loading) {
                        $btn.html(_msg)
                        return
                    }

                    var i = 0
                    var tick = function () {
                        $btn.html(_msg + new Array(i + 1).join('.'))
                        i++
                        if (i === 4) {
                            i = 0
                        }
                        $btn.data('loadingTimer', setTimeout(tick, 1200))
                    }
                    tick()
                })
            }
            $.fn.enable = function () {
                return this.each(function () {
                    var $btn = $(this).prop('disabled', false)
                    clearTimeout($btn.data('loadingTimer'))
                    var oriHTML = $btn.data('html')
                    if (oriHTML) {
                        return $btn.html(oriHTML)
                    }
                })
            }
        }(jQuery)

        window.dm = window.dm || {};
        $.ajaxSetup({
            cache: false
        })

        dm.noop = function () {

        }
        dm.ajax = function (method, url, data, options) {
            var dtd = $.Deferred();
            var dataType = 'json';
            var defaultOption = {
                alert: window.device && device.mobile() ? "toast": "alert"
            }
            var o = $.extend({}, defaultOption, options);
            console.log(method)
            if(method === 'getJSON'){
                method = 'get';
            }
            if(method === 'html') {
                dataType = 'html';
                method = 'get';
            }
            $.ajax({
                url: url,
                type: method,
                dataType: dataType,
                data: data
            })
            .done(function(res) {
                if(dataType === 'html') {
                    dtd.resolve(res);
                    return
                }
                
                /*if(typeof res === 'string'){
                    res = eval('('+res+')');
                }*/
                if (!res) {
                    dm[o.alert]('服务器未返回数据')
                    dtd.reject()
                }
                if (res.status == 9) {
                    var msg = '',
                        redirect = function () {
                            var href = res.data
                            if (~href.indexOf('?')) href += '&'
                            else href += '?'
                            location.href = href + 'redirect_url=' + encodeURIComponent(location.href)
                        }
                    if (res.info === 'NEED_REAL_NAME_AUTH') msg = '您还未进行实名认证, 是否立即前往实名认证'
                    else if (res.info === 'NEED_BANK_AUTH') msg = '您还未进行银行卡实名认证，是否立即前往认证'
                    else if (res.info === 'NEED_QUESTION_AUTH') msg = '您还未进行合格投资人, 是否立即前往合格投资人认证'

                    if (msg) dm.confirm(msg, redirect)
                    else redirect()
                    dtd.reject()

                } else if (res.status > 0 || res.code >0) {
                	//未登录或登录已失效
                	if(res.code == 10007)
                	{
                		dm.confirm("您还未登录或登录已失效，是否重新登录？",function(){
                			location = "login.html";
                		})
                		
                	}
			
                	res.info = res.message?res.message:res.info;
                    var msg = []
                    if (Object.prototype.toString.call(res.info) === "[object Array]") {
                        var infoList = res.info
                        for (var i = 0, len = infoList.length; i < len; i++) {
                            msg.push(infoList[i].message)
                        }
                    } else {
                        msg.push(res.info)
                    }
                    dm[o.alert](msg.join('<br>'))
                    dtd.reject(res)
                } else if ('' + res.status === '0' || '' + res.code === '0') {
                    // status == 0 or '0'则表示正确
                    dtd.resolve(res)
                } else {
                    dm[o.alert]('未知错误')
                    dtd.reject(res)
                }
            })
            .fail(function(res) {
                dm[o.alert]('加载失败, 请重试');
                dtd.reject({'info':'加载失败, 请重试'});
                
            })
            .always(function() {
                //console.log("complete");
            });
            
            return dtd
        }
        dm.getJSON = function (url, data, options) {
            return dm.ajax('getJSON', url, data, options)
        }
        dm.get = function (url, data, options) {
            return dm.ajax('get', url, data, options)
        }
        dm.post = function (url, data, options) {
            return dm.ajax('post', url, data, options)
        }


        $(document).off('submit.ajax').on('submit.ajax', '.ajax-form', function (e, eventData) {
            e.preventDefault()
            var $form = $(this),
                $btn = $form.find(':submit')

            $form.trigger('before')
            var ajaxSubmit = function () {
                
                var method = $form.attr('data-ajax-method') || $form.data('ajaxMethod') || $form.attr('method')
                //console.log(1111,method)
                if (!method) method = 'post'
                var result = $form.trigger('beforeSend');
                
                var error = $form.data('error');
                //console.log('beforeSend error',error);
                if(error){
                    if(typeof error === 'string'){
                        dm.alert(error)
                    }
                    return;
                }
                $btn.disable()
                dm.ajax(method, $form.attr('action'), $form.serialize(), $form.data()).done(function (res) {
                    if (eventData != null && eventData.success) {
                        for (var _event in eventData) {
                            $form.triggerHandler(_event + '.' + eventData[_event], res)
                        }
                    }
                    else $form.triggerHandler('success', res)
                }).fail(function () {
                    $form.triggerHandler('fail')

                }).always(function () {
                    $btn.enable()
                    $form.triggerHandler('always')
                })
            }
            if ($form.validate != null) {
                return $form.validate(null, ajaxSubmit)
            } else {
                return ajaxSubmit()
            }
        });

        $(document).off('submit.native').on('submit.native', '.native-form', function (e, eventData) {
            e.preventDefault()
            var $form = $(this);

           
            if ($form.validate != null) {
                $form.validate(null, function(){
                    $form[0].submit();
                });
            }
        });


        $(':submit').filter('[name]').click(function () {
            var $btn = $(this),
                $form = $btn.closest('form'),
                name = $btn.attr('name'),
                $input = $form.find('input[name=' + name + ']')
            if ($input.length === 0) {
                $input = $('<input type="hidden" name="' + name + '">').appendTo('form')
            }
            $input.val($btn.val())
        });

    })();
    // scroll
    (function () {
        var $w = $(window);
        $.fn.inview = function (callback, options) {
            var defaultOptions = {
                    threshold: 0,
                    hidden:false
                },
                o = $.extend({}, defaultOptions, options),
                $this = this,
                loaded;
            var s = $w;
            var r = $this.closest(".view, .scroll-win");
            var viewid  = d = $this.data("inview-id") || 0;
            r.length && (s = r);
            d ? s.off(".inview" + d) : (d = (s.data("inview-id") || 0) + 1, s.data("inview-id", d), $this.data("inview-id", d));
            var c = 0;
            var l = function(t) {
                return r.length ? t.position().top: t.offset().top
            };
            var tick = function () {
                var wt = $w.scrollTop(),
                    wb = wt + $w.height()
                var $inview = $this.filter(function () {
                    var $e = $(this)
                    if (o.hidden == false && $e.is(':hidden')) {
                        return false
                    }
                    var et = l($e),
                        eb = et + $e.height();

                    // console.log('inview','window.scrollTop:',wt,',windowB:',wb,',loadingTop:',et,',loadingB:',eb,',threshold:',o.threshold);
                    // console.log('result:',et,wb,et <= wb + o.threshold,eb >= wt - o.threshold && et <= wb + o.threshold);
                    return eb >= wt - o.threshold && et <= wb + o.threshold
                })
                c = wt
                loaded = $inview.trigger('inview',wt > c)
                $this = $this.not(loaded)
                0 === $this.length && $w.off(".inview" + d)
            }
            this.off("inview").one('inview', callback)
            s.on("scroll.inview.inview" + d + " resize.inview.inview" + d + " lookup.inview.inview" + d, tick)
            tick()
            return this
        }
        $.fn.headroom = function (options) {
            var defaultOption = {
                    offset: 200,
                    offsetBottom: 10,
                    tolerance: 20,
                    unpinnedClass: 'headroom--unpinned',
                    bottom: false
                },
                timer = 0,
                y = 0,
                $this = this.addClass('headroom')
            var o = $.extend({}, defaultOption, options)

            var checkTolerance = function () {
                var wt = $w.scrollTop(),
                    check = function () {
                        if ((wt - y) > o.tolerance) {
                            $this.addClass(o.unpinnedClass)
                        } else if ((y - wt) > o.tolerance) {
                            $this.removeClass(o.unpinnedClass)
                        }
                    }
                //if (wt < o.offset || (maxScroll - wt) <= o.offsetBottom) $this.removeClass(o.unpinnedClass)
                if (wt < o.offset) $this.removeClass(o.unpinnedClass)
                else check()

                y = wt
                timer = 0
            }
            var tick = function () {
                if (!timer) {
                    y = $w.scrollTop()
                    timer = setTimeout(checkTolerance, 100)
                }

            }
            $w.on('scroll.headroom resize.headroom lookup.headroom', tick)
            return this
        }
        $.fn.simpleStick = function (options) {
            var defaultOption = {
                offset: 200,
                stickClass: 'stick'
            }, $this = this
            var o = $.extend({}, defaultOption, options)
            var tick = function () {
                var wt = $w.scrollTop()
                if (wt < o.offset) {
                    $this.removeClass(o.stickClass)
                } else {
                    $this.addClass(o.stickClass)
                }
            }
            $w.on('scroll.headroom resize.headroom lookup.headroom', tick)
            return this
        }
        $.fn.stick = function (options) {
            var $this = this,
                $parent = $this.parent(),
                offsetList = [],
                bottomList = [],
                oriCSS = [],
                defaultOptions = {
                    offset: 0,
                    hasBottom: true
                }
            var o = $.extend({}, defaultOptions, options)
            this.each(function (i) {
                var $elm = $(this),
                    $wrap = $elm.parent()
                if ($wrap.css('position') === 'static') $wrap.css('position', 'relative')
                offsetList[i] = $elm.offset()
                oriCSS[i] = {
                    position: $elm.css('position'),
                    left: $elm.css('left'),
                    top: $elm.css('top'),
                    bottom: $elm.css('bottom')
                }
                bottomList[i] = $wrap.offset().top + $wrap.height() - $elm.height()
            })
            var tick = function () {
                var wt = $w.scrollTop()
                for (var i = 0, len = offsetList.length; i < len; i++) {
                    if (wt > (offsetList[i].top - o.offset)) {
                        if (o.hasBottom && wt > bottomList[i]) {
                            $this.eq(i).css({
                                position: 'absolute',
                                left: oriCSS[i].left,
                                top: 'auto',
                                bottom: 0
                            }).addClass('stick')
                            $parent.addClass('stick')
                        } else {
                            $this.eq(i).css({
                                position: 'fixed',
                                left: offsetList[i].left,
                                top: o.offset,
                                bottom: 'auto'
                            }).addClass('stick')
                            $parent.addClass('stick')
                        }
                    } else {
                        $this.eq(i).css(oriCSS[i]).removeClass('stick')
                        $parent.removeClass("stick")
                    }
                }
            }
            $w.on('scroll.stick resize.stick lookup.stick', tick)
            tick()
            return this
        }
        $.fn.scrollAnchor = function (options) {
            var defaultOptions = {
                    threshold: 20
                },
                o = $.extend({}, defaultOptions, options)
            var $wrap = $(this),
                $anchorList = $(),
                offsetList = []
            $wrap.find("[href^='#']").each(function () {
                var $anchor = $(this),
                    $target = $($anchor.attr('href'))
                if ($target.length) {
                    $anchorList = $anchorList.add($anchor)
                    offsetList.push($target.offset().top)
                }
            })
            var tick = function () {
                var wt = $w.scrollTop(),
                    targetIndex = 0
                for (var i = 0, len = offsetList.length; i < len; i++) {
                    if (wt > offsetList[i] - o.threshold) {
                        targetIndex = i
                    }
                }
                $anchorList.removeClass('active').eq(targetIndex).addClass('active')
            }
            $w.on('scroll.anchor resize.anchor lookup.anchor', tick)
            tick()
            return this
        }
        // 点击加载更多
        $.fn.morePage = function(options) {
            var $wrap = $(this);
            var templateId = $wrap.data('template');
            var $list = $($wrap.data('listWrap'));
            var parseData = (options && options.parseData)?options.parseData : function(){};
            //console.log('templateId',templateId,$wrap)
            if(templateId){
                
                $wrap.on('data',function(e,json){
                    
                    var html = '',list = json.list;
                    
                    parseData(list);
                    for (var i = 0, len = list.length; i < len; i++) {
                        html += template(templateId, list[i])
                    }
                    $list.append(html);
                           
                });
                

            }
            if (null != options || !$wrap.data("init")) {
                
                var type, $list, $form = $wrap.find("form"),
                $offset = $form.find("[name=offset]").length?$form.find("[name=offset]"):$form.find("[data-name=offset]"),
                offset = parseInt($offset.val(), 10) || 1,
                totalPage = $wrap.data("totalPage"),
                listWrap = $wrap.data("listWrap");
                listWrap ? (type = "html", $list = $(listWrap)) : (type = "json", $form.data("ajaxMethod", "getJSON"));
                templateId?(type = "json", $form.data("ajaxMethod", "getJSON")):(void 0);
                

                $form.off(".morePage").on("success.morePage",
                function(e, res) {
                    if (offset += 1, $offset.val(offset), "html" === type) {
                        $list.append(res),
                        res ? init() : $wrap.addClass("end");
                    }
                    else if ("json" === type) {
                        //console.log('sss')
                        var list = res.data && res.data.list;
                        totalPage = res.data && res.data.page && res.data.page.total_page;
                        null == list || 0 === list.length ? ( 2 === offset ? $wrap.addClass("blank") : $wrap.addClass("end") ): ($wrap.trigger("data",res.data),init());
                    }
                }).on("fail",
                function() {
                    $wrap.addClass("fail")
                });
                var init = function() {

                    //console.log(totalPage,offset,$form.find(':submit'))
                    if(0 === totalPage){
                        $wrap.addClass("blank");
                        $form.find(':submit').prop('disabled', true).text('暂无数据');
                    }else {
                        //console.log(222222,offset , totalPage,null != totalPage && offset > totalPage)
                        if(null != totalPage && offset > totalPage) {
                            
                            $wrap.addClass("end");
                            setTimeout(function(){
                                $form.find(':submit').prop('disabled', true).text('加载完成');
                            },10)
                            
                        }else {
                            //$form.submit();
                        }
                        
                    }
                    //console.log('totalPage',totalPage,',offset',offset)
                    //return 0 === totalPage ? void $wrap.addClass("blank") : void(null != totalPage && offset > totalPage ? $wrap.addClass("end") : $form.submit())
                };

                if(totalPage && totalPage>=1) {
                    $form.submit();
                }
                return init(),
                $wrap.data("init", 1),
                
                this
            }
        };
        // 滚动加载更多
        $.fn.infiniteLoading = function(options) {
            var $wrap = $(this);
            var hidden = $wrap.data('hidden')?true:false;
            var templateId = $wrap.data('template');
            var $list = $wrap.data('listWrap')?$($wrap.data('listWrap')):$wrap.siblings('.listWrap');
            var parseData = (options && options.parseData)?options.parseData : function(){};
            
            if(!$wrap.data("init") && templateId){

                $wrap.on('data',function(e,json){
                    
                    var html = '',list = json.list;
                    
                    parseData(list);
                    for (var i = 0, len = list.length; i < len; i++) {
                        html += template(templateId, list[i])
                    }
                    var $list2 = $(html).appendTo($list);
                           
                });
                

            }
            if (null != options || !$wrap.data("init")) {

                var $loading = $wrap.find(".loading");
                $loading.length || ($loading = $('<div class="loading"><div class="img-dm-loading"></div><u class="loading-text">正在加载中...</u><u class="end-text">加载完成</u><u class="blank-text">暂无记录</u><u class="fail-text">加载失败，点击重试</u></div>'), $wrap.append($loading));
                var reset = function() {
                    $wrap.removeClass("end blank fail")
                };
                "reset" === options && reset(),
                $wrap.off("click.infiniteLoading").on("click.infiniteLoading",
                function() {
                    $wrap.hasClass("fail") && $wrap.infiniteLoading("reset")
                });
                var type, $list, $form = $wrap.find("form"),
                $offset = $form.find("[data-name=offset]"),
                
                offset = parseInt($offset.val(), 10) || 1,
                totalPage = $wrap.data("totalPage"),
                listWrap = $wrap.data("listWrap");

                listWrap ? (type = "html", $list = $(listWrap)) : (type = "json", $form.data("ajaxMethod", "getJSON"));
                templateId?(type = "json", $form.data("ajaxMethod", "getJSON")):(void 0);

                $form.off(".infiniteLoading").on("success.infiniteLoading",
                function(e, res) {
                    if (offset += 1, $offset.val(offset), "html" === type) {
                        $list.append(res),
                        res ? init() : $wrap.addClass("end");
                    }
                    else if ("json" === type) {
                        
                        var list = res.data && res.data.list;
                        totalPage = res.data && res.data.page && res.data.page.total_page;
                        if(null == list || 0 === list.length) {
                            if(2 === offset){
                                $wrap.addClass("blank")
                            }else {
                                $wrap.addClass("end")
                            }
                        }else {
                            $wrap.trigger("data",res.data);
                            //setTimeout(function(){
                                //console.log('22222222222$loading',$loading.offset().top)
                                init();
                            //},100);
                            
                            //init();
                        }
                        //null == list || 0 === list.length ? ( 2 === offset ? $wrap.addClass("blank") : $wrap.addClass("end") ): ($wrap.trigger("data",res.data),init());
                    }
                }).on("fail",
                function() {
                    $wrap.addClass("fail")
                });
                var init = function() {
                    if(0 === totalPage) {
                        $wrap.addClass("blank");
                    }else {
                        if(null != totalPage && offset > totalPage) {
                            $wrap.addClass("end")
                        }else {
                            
                            $loading.inview(function(){
                                //console.warn('submit');
                                $form.submit();
                            },{threshold:0,hidden:hidden});
                        }
                    }
                    
                };
                return init(),
                $wrap.data("init", 1),
                this
            }
        };
        $('.infinite-loading').each(function () {
            var $this = $(this);
            if (!$this.siblings('ul').children().length) {
                $this.find('[name=offset]').val(1);
            }
        });


        $.fn.backtop = function () {
            var $backtop = $(this).on('click', function () {
                $('html,body').stop(true, false).animate({
                    scrollTop: 0
                }, 400);
            })
            var tick = function () {
                var wt = $w.scrollTop()

                if (wt > 600) {
                    $backtop.stop(true, false).fadeIn()
                } else {
                    $backtop.stop(true, false).fadeOut()
                }
            }
            $w.on('scroll.backtop resize.backtop lookup.backtop', tick)
            tick()
        }

        var retina = window.devicePixelRatio > 1
        $('img[data-src]').inview(function () {
            var $this = $(this),
                src
            if (retina) src = $this.data('srcRetina') || $this.data('src')
            else src = $this.data('src')
            return $this.attr('src', src)
        })
        

    })(jQuery);
    
    /*jquery tab*/
    (function() {
        
        /*
            <div class="xx">
                <button data-tab="a1">a1</button>
                <button data-tab="a2">a2</button>
            </div>
            <div class="tab-content">
                <div class="" data-tab="a1">aaaaaaaaa</div>   
                <div class="" data-tab="a2">bbbbbb</div>   
            </div>
            example: $('.xx').hashTab();
        */
        var getTabHash = function () {
            var m;
            m = location.hash.match(/#\/([\w-]+)/);
            if (m) {
                return m[1];
            } else {
                return '';
            }
        }
        $.fn.hashTab = function () {
            var preventHashChangeEvent, previousState;
            previousState = null;
            preventHashChangeEvent = false;
            $(window).off('hashchange.switchTab').on('hashchange.switchTab', function (event) {
                var targetTab;
                if (typeof history.replaceState === "function") {
                    history.replaceState(previousState, '');
                }
                if (preventHashChangeEvent) {
                    preventHashChangeEvent = false;
                    return;
                }
                targetTab = getTabHash();
                if (targetTab) {
                    $('.tab-title').find('[data-tab=' + targetTab + ']').eq(0).triggerHandler('click.switchTab');
                } else {
                    $('.tab-title').has('[data-tab]').children().eq(0).triggerHandler('click.switchTab');
                }
            });
            return this.each(function () {
                var $activeTitle, $contents, $tabTitle, $titles, targetTab;
                $tabTitle = $(this);

                $titles = $tabTitle.children();
                $contents = $tabTitle.siblings('.tab-content').children();
                $titles.off('click.switchTab').on('click.switchTab', function () {
                    var $targetTab, $this, targetTab;
                    $this = $(this);
                    $this.addClass('active').siblings().removeClass('active');
                    $contents.hide();
                    targetTab = $this.data('tab');
                    $targetTab = $contents.filter('[data-tab=' + targetTab + ']');
                    if (!$targetTab.length) {
                        $targetTab = $contents.eq($this.index());
                    }
                    return $targetTab.fadeIn(300).trigger('in');
                });
                $titles.off('click.changeHash').on('click.changeHash', function (event) {
                    var targetTab;
                    targetTab = $(this).data('tab');
                    if (!event.isTrigger && targetTab) {
                        previousState = history.state;
                        if (location.hash !== '/' + targetTab) {
                            preventHashChangeEvent = true;
                        }
                        return location.hash = '/' + targetTab;
                    }
                });
                targetTab = getTabHash();
                $activeTitle = [];
                if (targetTab) {
                    $activeTitle = $titles.filter('[data-tab=' + targetTab + ']');
                }
                if (!$activeTitle.length) {
                    $activeTitle = $titles.filter('.active');
                }
                if (!$activeTitle.length) {
                    $activeTitle = $titles.eq(0);
                }
                return $activeTitle.triggerHandler('click.switchTab');
            });
        }


        $.fn.tab = function() {
            var $titleWrap = $(this),
            $titles = $titleWrap.children(),
            $contents = $titleWrap.siblings(".tab-content").children(),
            show = function(t) {
                var $active = $(t).addClass("active");
                $active.siblings().removeClass("active"),
                $contents.filter(".show").removeClass("show in"),
                $contents.eq($active.index()).addClass("show").flushStyle().addClass("in").trigger("in"),
                $active.trigger("show",$active.index())
            };
            console.log($titles);
            return {
                init: function() {
                    $titles.on("click",
                    function(e) {
                        e.preventDefault();
                        show(this);
                    }),
                    $titles.filter(".active").trigger("click")
                },
                show: show
            }
        }
        /*
            div.tabs2>a
            $('.tabs2').tab2();
        */
        $.fn.tab2 = function () {
            return this.each(function () {
                var $titleWrap = $(this)
                var $link = $titleWrap.find('.tab-link').length?$titleWrap.find('.tab-link'):$titleWrap.find('a')
                var $titles = $link.length?$link:$titleWrap.children(),
                    $contentsWrap = $titleWrap.siblings('.tab-content')
                var $contents = $contentsWrap.children()

                // 初始化
                var $active = $titles.filter('.active')
                if(!$active.length){
                    $titles.eq(0).addClass('active');
                }
                $titles.each(function(index, el) {
                    $(this).data('index',index).attr('index',index);
                });
                $contents.hide().eq($active.length ? $active.data('index') : 0).show().trigger('in')
                //$contentsWrap.css('min-height', $contentsWrap.height())

                // 点击事件
                $titles.on('click', function () {
                    var $this = $(this)
                    $titles.removeClass('active')
                    $this.addClass('active')
                    $contents.stop(true, false).hide().eq($this.data('index')).fadeIn().trigger('in')
                    //$contentsWrap.css('min-height', $contentsWrap.height())
                })
            })
        };
    })();


    /* router */
    (function() {
        window.dm = window.dm || {},
        dm.router = function() {
            
            var t = [],
            e = function(t) {
                return null == t && (t = location.hash),
                t.replace(/^#[!]*/, "")
            },
            // router(['/intro','/nickname'],function(){})
            n = function(n, callback, a) {
                
                "string" == typeof n && (n = [n]),
                t = n;
                var o = function(hash) {
                    hash && -1 === t.indexOf(hash) && (hash = ""),
                    callback(hash)
                };
                $(window).on("hashchange",
                function() {
                    o(e())
                }),
                o(e())
            };
            return n.push = function() {
                t.push(path)
            },
            n.go = function(t) {
                location.hash = t ? "!" + t: ""
            },
            n.backTo = function(t) {
                history.back(),
                setTimeout(function() {
                    var e = location.hash.replace(/^#[!\/]*/, "");
                    t = null == t ? "": t.replace(/^\//, ""),
                    e !== t && dm.router.go(t ? "/" + t: t)
                },
                0)
            },
            n
        } (),
        $(document).on("click", "[data-router]",
        function() {
            var t = $(this).data("router");
            console.log('data-router',t);
            dm.router.go(t)
        });
        dm.view = function() {
            var t, e, n = function(t) {
                $(t).removeClass("in").one("dmTransitionEnd",
                function() {
                    $(this).removeClass("show").trigger("hidden")
                }).emulateTransitionEnd(500).trigger("hide")
            };
            return {
                init: function() {
                    t = $(".view-wrap"),
                    e = $(".view")
                },
                show: function(i) {
                    if (i) {
                        var a = $(".view-" + i).addClass("show").css("zIndex", 1).flushStyle().addClass("in").one("dmTransitionEnd",
                        function() {
                            $(this).trigger("shown"),
                            $('.view-main').hide(),
                            n(o)
                        }).emulateTransitionEnd(500).trigger("show");
                        a.closest(".view-wrap").css("zIndex", 10);
                        var o = e.filter(".show").not(a).css("zIndex", 0);
                        $("body").addClass("view-open")
                    } else e.filter(".show").removeClass("in").one("dmTransitionEnd",
                    function() {
                        $(this).removeClass("show").trigger("hidden"),
                        t.css("zIndex", -1)
                    }).emulateTransitionEnd(500).trigger("hide"),
                    $('.view-main').show(),
                    $("body").removeClass("view-open")
                },
                hide: function(t) {
                    n(".view-" + t),
                    $("body").removeClass("view-open")
                }
            }
        } (),
        dm.view.init()

    })();

    /* city */
    (function ($) {
        var cityData;
        cityData = null;
        
        return $.fn.citySelect = function (method, arg1, arg2) {
            var citySelect, that;
            if (!this.length) {
                return;
            }
            that = this;
            citySelect = function () {
                return that.each(function () {
                    var $button, $cityBox, $dpc, $inputCity, $inputProv, $provBox, $select, $tabContent, $tabTitle, _provInfo, buildCityDOM, html, i, len, oriCity, oriProv, switchTab, updateValue;
                    $select = $(this);
                    $dpc = $select.find('.dropdown-content');
                    $tabTitle = $dpc.find('.tab-title');
                    $tabContent = $dpc.find('.tab-content');
                    $button = $select.children('.btn');
                    $provBox = $tabContent.children('.prov-box');
                    $cityBox = $tabContent.children('.city-box');
                    $inputProv = $select.find('[name2=province]');
                    $inputCity = $select.find('[name2=city]');
                    if(!$inputProv.length){
                        $inputProv = $select.find('[name=province]');
                    }
                    if(!$inputCity.length){
                        $inputCity = $select.find('[name=city]');
                    }
                    if ($provBox.html() && !method) {
                        return;
                    }
                    buildCityDOM = function (provId, city) {
                        var _cityInfo, _k, _provInfo, _v, html, i, len;
                        provId = '' + provId;
                        for (i = 0, len = cityData.length; i < len; i++) {
                            _provInfo = cityData[i];
                            if (provId === '' + _provInfo.id) {
                                _cityInfo = _provInfo.cities;
                                html = '';
                                for (_k in _cityInfo) {
                                    _v = _cityInfo[_k];
                                    html += '<u data-id="' + _k + '">' + _v + '</u>';
                                }
                                $cityBox.html(html);
                                $cityBox.find('[data-id=' + city + ']').addClass('checked')
                            }
                        }
                    };
                    updateValue = function (prov, city) {
                        var provName = $provBox.children().removeClass('checked').filter('[data-id=' + (prov || 0) + ']').addClass('checked').text()
                        buildCityDOM(prov, city)
                        var cityName = $cityBox.children().removeClass('checked').filter('[data-id=' + (city || 0) + ']').addClass('checked').text()
                        if (!prov && !city) $button.val('请选择城市')
                        else $button.val(provName + ' / ' + cityName)

                        $inputProv.val(prov)
                        $inputCity.val(city)

                    }
                    if (method === 'update') {
                        updateValue(arg1, arg2);
                        return
                    }
                    switchTab = function (target) {
                        $tabTitle.children('[data-target="' + target + '"]').addClass('active').siblings().removeClass('active');
                        $tabContent.children(target).show().siblings().hide();
                    }
                    if (method === 'reset') {
                        $button.val('请选择城市')
                        $inputProv.val('')
                        $inputCity.val('')
                        $cityBox.html('<b class="c-danger">请选择省份</b>')
                        switchTab('.prov-box')
                        return
                    }
                    html = '';
                    for (i = 0, len = cityData.length; i < len; i++) {
                        _provInfo = cityData[i];
                        html += '<u data-id="' + _provInfo.id + '">' + _provInfo.name + '</u>';
                    }
                    $provBox.html(html);
                    oriProv = $inputProv.val() || '';
                    oriCity = $inputCity.val() || '';
                    if (oriCity) {
                        updateValue(oriProv, oriCity);
                    }
                    $select.on('click.stopPropagation', function (e) {
                        e.stopPropagation();
                    });
                    $(document).on('click.closeDropdown', function () {
                        return $dpc.hide()
                    });
                    $button.on('click.dropdown', function (e) {
                        $dpc.toggle()
                    });

                    $tabTitle.on('click.switchTab', 'li', function () {
                        var $this, target;
                        $this = $(this);
                        target = $this.data('target');
                        switchTab(target);
                    });
                    $provBox.on('click.selectProv', 'u', function () {
                        var $this, provId, provName;
                        $this = $(this);
                        $this.addClass('checked').siblings('.checked').removeClass('checked');
                        switchTab('.city-box');
                        provId = $this.data('id');
                        provName = $this.text();
                        buildCityDOM(provId);
                        $button.val(provName);
                        return $inputProv.val(provId);
                    });
                    $cityBox.on('click.selectDone', 'u', function () {
                        var $this, _cityArr;
                        $this = $(this);
                        $this.addClass('checked').siblings('.checked').removeClass('checked');
                        _cityArr = $button.val().split('/').map(function (s) {
                            return s.trim();
                        });
                        _cityArr[1] = $this.text();
                        $button.val(_cityArr.join(' / ')).validate();
                        $dpc.hide();
                        return $inputCity.val($this.data('id'));
                    });
                });
            }
            if (cityData) {
                citySelect();
            } else {
                if (!api.cities) throw new Error('require api.cities')
                $.getJSON(api.cities).then(function (json) {
                    cityData = json;
                    return citySelect();
                })
            }
            return that;
        };
    })(jQuery);


    /* paginator (jqPaginator) */
    $.fn.paginator = function(t) {
        var e = $.extend({},
        {
            totalPage: 0
        },
        t);
        return this.each(function() {
            var t = $(this),
            n = t.find("form"),
            i = t.find(".page-list"),
            a = e.totalPage || t.data("totalPage") || 0;
            if (n.off("success.paginator").on("success.paginator",
            function(e, a) {
                a.data && a.data.page && a.data.page.total_page ? (t.paginator({
                    totalPage: a.data.page.total_page
                }), t.siblings("ul").removeClass("empty")) : (i.children().length && i.jqPaginator("destroy"), n.find("[name=offset]").val() <= 1 && t.siblings("ul").addClass("empty")),
                a.data && t.trigger("data", a.data)
            }).off("always.paginator").on("always.paginator",
            function() {
                t.siblings("ul").loading("hide")
            }), !a || 1 >= a) return void(i.children().length && i.jqPaginator("destroy"));
            i.length ? i.children().length && i.jqPaginator("destroy") : i = $('<div class="page-list"></div>').appendTo(t);
            var o = e.visiblePage;
            null == o && (o = t.data("visible")),
            null == o && (o = 10),
            i.jqPaginator({
                totalPages: a,
                visiblePages: o,
                currentPage: t.data("currentPage") || parseInt(n.find("[name=offset]").val(), 10) || 1,
                page: '<a href="javascript:;" class="btn">{{page}}</a>',
                prev: '<a href="javascript:;" class="btn">' + (e.prev || t.data("prev") || "上一页 ") + "</a>",
                next: '<a href="javascript:;" class="btn">' + (e.next || t.data("next") || "下一页") + "</a>",
                onPageChange: function(e, i) {
                    "change" === i && (t.trigger("change", e), n.length && (t.siblings("ul").loading(), n.find("[name=offset]").val(e), n.submit()))
                }
            })
        })
    };
    
    window.smsHelper = function () {
        var that = {}
        that.init = function () {
            
            $('.btn-sms').each(function () {
                var $btn = $(this),
                    $form = $btn.closest('form'),
                    $codeType=$form.find("#codeflag"),
                    $inputTel = $form.find('#tel, .tel, [type=tel]').eq(0),
                    waiting = $btn.data('disabledText') || '',
                    $inputImgVerify = $form.find('.input-img-verify').not('[disabled]')

                var opts = {
                    url: $btn.data("ajax") || api.sms || "",
                    waiting: waiting || "重新发送 <var>60</var> 秒"
                };
                var send = function () {
                    $btn.disable()
                    dm.getJSON(opts.url, {
                        phone: $inputTel.val(),
                        verify: $inputImgVerify.val(),
                        flag: $codeType.val(),
                        ischeck: $btn.data('check-type')
                    }).done(function (json) {
                        $btn.disable(opts.waiting, false)
                        var $var = $btn.find('var'),
                            timer = 0
                        var tick = function () {
                            var sec = (parseInt($var.text(), 10) || 0) - 1
                            if (sec <= 0) {
                                $btn.enable()
                            } else {
                                $var.text(sec)
                                timer = setTimeout(tick, 1000)
                            }
                        }
                        tick()
                    }).fail(function () {
                        $btn.enable()
                    })
                }

                $btn.click(function () {
                    var checkType = $btn.data('checkType') || 0
                    if (checkType === 'refund' || checkType === 'changemobile') send() // 提现, 手机号自动获取
                    else $inputTel.validate(null, function () {
                        if ($inputImgVerify.length) {
                            $inputImgVerify.validate(null, send)
                        }else{
                            send();
                        }
                    })
                })
            })
        }
        return that
    }();
    
    $.fn.inputMask = function() {
        return this.each(function() {
            function t(t) {
                function e() {
                    for (var t = r.length; t > 0;) {
                        var e = r.substring(t, t - 3);
                        t -= 3,
                        o.unshift(e)
                    }
                }
                var n = t.replace(/,/g, ""),
                i = n.indexOf("."),
                a = n.substr(0, i),
                o = [];
                if ( - 1 == n.indexOf(".")) {
                    var r = n;
                    e(),
                    n = o
                } else {
                    var r = a;
                    e(),
                    n = o + n.substring(i)
                }
                return {
                    visual: n,
                    real: t.replace(/,/g, "")
                }
            }
            function e(t) {
                var e = t.replace(/\s/g, "").replace(/(\d{4})(?=\d)/g, "$1 ");
                return {
                    visual: e,
                    real: t.replace(/\s/g, "")
                }
            }
            var n = $(this),
            i = n.closest("form"),
            a = n.data("mask"),
            o = n.attr("name");
            n.removeAttr("name");
            var r = $("<input type='hidden' name='" + o + "'>").appendTo(i),
            s = function(i, a) {
                l();
                var o;
                "number" === a ? o = t(i) : "bankcard" === a && (o = e(i)),
                o && (n.val(o.visual), r.val($.trim(o.real))),
                d()
            },
            d = function() {
                n.on("input.mask propertychange.mask",
                function() {
                    s(n.val(), a)
                }),
                window.ActiveXObject && 9 == document.documentMode && n.on("keydown.mask",
                function(t) {
                    8 === t.which && setTimeout(function() {
                        s(n.val(), a)
                    },
                    0)
                })
            },
            l = function() {
                n.off(".mask")
            };
            d()
        })
    };
    $(function() {
        var t = {
            versions: function() {
                var t = navigator.userAgent;
                navigator.appVersion;
                return {
                    trident: t.indexOf("Trident") > -1,
                    presto: t.indexOf("Presto") > -1,
                    webKit: t.indexOf("AppleWebKit") > -1,
                    gecko: t.indexOf("Gecko") > -1 && -1 == t.indexOf("KHTML"),
                    mobile: !!t.match(/AppleWebKit.*Mobile.*/) || !!t.match(/AppleWebKit/),
                    ios: !!t.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/),
                    android: t.indexOf("Android") > -1 || t.indexOf("Linux") > -1,
                    iPhone: t.indexOf("iPhone") > -1 || t.indexOf("Mac") > -1,
                    iPad: t.indexOf("iPad") > -1,
                    webApp: -1 == t.indexOf("Safari"),
                    weixin: t.indexOf("MicroMessenger") > -1
                }
            } (),
            language: (navigator.browserLanguage || navigator.language).toLowerCase()
        };
        t.versions.android || t.versions.weixin || $("input[data-mask]").inputMask()
    });

    // need swipe.js
    $.fn.swipeTab = function(t) {
        return this.each(function() {
            var e = $(this),
            n = $(e.data("tab-title") || ".swipe-tab-title"),
            i = n.children(".item"),
            a = n.width(),
            o = e.find(".swipe-wrap").children(),
            r = $.noop;
            n[0].scrollWidth > n[0].clientWidth && (r = function() {
                var t = i.filter(".active"),
                e = n.scrollLeft(),
                o = t.position().left,
                r = o + t.width() + 20 - a;
                r > 0 ? n.stop(!0, !1).animate({
                    scrollLeft: e + r
                }) : 0 > o && n.stop(!0, !1).animate({
                    scrollLeft: e + o
                })
            });
            var s = new Swipe(this, {
                continuous: !1,
                callback: function(e, n) {
                    var a = i.eq(e).addClass("active");
                    a.siblings().removeClass("active"),
                    r(),
                    t && t(e, n)
                }
            });
            i.on("click",
            function() {
                console.log('click')
                var e = $(this);
                if (e.hasClass("active")) {
                    var n = e.index();
                    t && t(n, o.get(n))
                }
                var i = $(this).addClass("active");
                i.siblings().removeClass("active"),
                r(),
                s.slide(i.index())
            })
        })
    };

    window['Message'] = new MessageTip;
    window.MessageTip = MessageTip;
    function MessageTip() {
        var self = this;
        
        self.msgHash = {
            'create'                                    :'数据创建',
            'add'                                       :'数据添加',
            'update'                                    :'数据更新',
            'delete'                                    :'数据删除'
        };
        self.show = function (msg,type) {
            var type = type || 'error',msg = msg || '',interval = 3000,ret;
            
            self.hide();
          
            ret = (type=='error') ? '失败':'成功';
            msg = self.msgHash[msg]?(self.msgHash[msg]+ret):msg;
            self.render();
            self.$container.find('.toast').addClass('toast-'+type);
            self.$container.find('.toast-message').html(msg);
            self.$container.fadeIn();
        
            if(type != 'error'){
                self.timer = setTimeout(function(){
                    self.hide();
                }, interval);
            }else{
                self.timer = setTimeout(function(){
                    self.hide();
                }, 8*1000);
            }
                
        };
        self.hide = function () {
            clearTimeout(self.timer);
            self.$container && self.$container.fadeOut().find('.toast').attr('class','toast');
        };
        self.render = function(){
            
            if($('#toast-container').length == 0){
                self.$container = $('<div id="toast-container"><div class="toast"><span class="toast-message"></span><span class="toast-close">close</span></div></div>').appendTo('body');
                self.$container.find('.toast-close').bind('click',function(){
                    self.hide();
                })
            }else{
                self.$container = $('#toast-container');
            }   
        } 
            
    };

    // dom 加载完毕运行
    $(function () {
        // 分享
        $(document).on('click','[data-role="share"]',function(e){
            $('body').append('<a id="share" href="javascript:void(0);" ><div>点击右上角按钮分享吧</div></a>');
            //$('body')[0].ontouchend = function(){return false}
        });
        $(document).on('click touchstart','#share',function(e){
            //e.stopPropagation()
            e.preventDefault();
            $('#share').remove();
            //$('body')[0].ontouchend = null;
        });
        // 短信接口初始化
        window.smsHelper.init();
        $(".paginator").paginator();
        $("form").not("[novalidate]").validate("init")

        var buildDOM = function ($input, maxlength) {
            var $countWrap = $('<div class="word-count"><var>' + $input.val().length + '</var>/' + maxlength + '</div>').insertAfter($input);
            return $countWrap.find('var')
        };
        $.fn.wordCount = function () {
            return this.each(function () {
                var $input = $(this),
                    maxlength = $input.attr('maxlength')
                if (!maxlength) {
                    return
                }
                var $var = buildDOM($input, maxlength)
                if ($input.is('input')) $input.css('padding-right', (('' + maxlength).length + 1.8) + 'em')
                $input.on('input.count propertychange.count', function () {
                    $var.html($input.val().length)
                })
            })
        }

        $('input[maxlength], textarea[maxlength]').not('[nocount]').wordCount();

        // 密码 明示还是不显示
        device.mobile() && $("input[type=password]").each(function() {
            var t = $(this),
            e = t.siblings(".show-password");
            e.length || (e = $('<div class="show-password poa r-0 t-3 p-6 c-c3 fz-medium status-off"><i class="if icon-eye-fill on"></i> <i class="if icon-eye-close off"></i></div>').insertAfter(t)),
            e.on("click",
            function() {
                "text" === t.attr("type") ? (t.attr("type", "password"), e.removeClass("status-on").addClass("status-off")) : (t.attr("type", "text"), e.removeClass("status-off").addClass("status-on"))
            })
        })

        // 
        $(".check-square, .check-circle, .radio-circle").find(":checkbox, :radio").prettyCheck()

        //
        /*$(".verify-img").click(function() {
            console.log(1)
            $(this).reloadVerify()
        });*/

        // 
        $('.city-select').citySelect()
    });


})();

/*jshint asi:true, expr:true */
/**
 * Plugin Name: Combo Select
 * Author : Vinay@Pebbleroad
 * Date: 23/11/2014
 * Description: 
 * Converts a select box into a searchable and keyboard friendly interface. Fallbacks to native select on mobile and tablets
 */

// $('select').comboSelect();
// Expose plugin as an AMD module if AMD loader is present:
(function (factory) {
    'use strict';
    if (typeof define === 'function' && define.amd) {
        // AMD. Register as an anonymous module.
        define(['jquery'], factory);
    } else if (typeof exports === 'object' && typeof require === 'function') {
        // Browserify
        factory(require('jquery'));
    } else {
        // Browser globals
        factory(jQuery);
    }
}(function ( $, undefined ) {

    var pluginName = "comboSelect",
        dataKey = 'comboselect';
    var defaults = {            
        comboClass         : 'combo-select',
        comboArrowClass    : 'combo-arrow',
        comboDropDownClass : 'combo-dropdown',
        inputClass         : 'combo-input text-input',
        disabledClass      : 'option-disabled',
        hoverClass         : 'option-hover',
        selectedClass      : 'option-selected',
        markerClass        : 'combo-marker',
        themeClass         : '',
        maxHeight          : 200,
        extendStyle        : true,
        focusInput         : true
    };

    /**
     * Utility functions
     */

    var keys = {
        ESC: 27,
        TAB: 9,
        RETURN: 13,
        LEFT: 37,
        UP: 38,
        RIGHT: 39,
        DOWN: 40,
        ENTER: 13,
        SHIFT: 16
    },    
    isMobile = (/android|webos|iphone|ipad|ipod|blackberry|iemobile|opera mini/i.test(navigator.userAgent.toLowerCase()));

    /**
     * Constructor
     * @param {[Node]} element [Select element]
     * @param {[Object]} options [Option object]
     */
    function Plugin ( element, options ) {
            
        /* Name of the plugin */

        this._name = pluginName;

        /* Reverse lookup */

        this.el = element

        /* Element */

        this.$el = $(element)

        /* If multiple select: stop */
        
        if(this.$el.prop('multiple')) return;

        /* Settings */

        this.settings = $.extend( {}, defaults, options, this.$el.data() );

        /* Defaults */

        this._defaults = defaults;

        /* Options */

        this.$options = this.$el.find('option, optgroup')

        /* Initialize */

        this.init();

        /* Instances */

        $.fn[ pluginName ].instances.push(this);

    }

    $.extend(Plugin.prototype, {
        init: function () {

            /* Construct the comboselect */

            this._construct();


            /* Add event bindings */          

            this._events();


        },
        _construct: function(){

            var self = this

            /**
             * Add negative TabIndex to `select`
             * Preserves previous tabindex
             */
            
            this.$el.data('plugin_'+ dataKey + '_tabindex', this.$el.prop('tabindex'))

            /* Add a tab index for desktop browsers */

            !isMobile && this.$el.prop("tabIndex", -1)

            /**
             * Wrap the Select
             */

            this.$container = this.$el.wrapAll('<div class="' + this.settings.comboClass + ' '+ this.settings.themeClass + '" />').parent();
            
            /**
             * Check if select has a width attribute
             */
            if(this.settings.extendStyle && this.$el.attr('style')){

                this.$container.attr('style', this.$el.attr("style"))
                
            }
            

            /**
             * Append dropdown arrow
             */

            this.$arrow = $('<div class="'+ this.settings.comboArrowClass+ '" />').appendTo(this.$container)


            /**
             * Append dropdown
             */

            this.$dropdown = $('<ul class="'+this.settings.comboDropDownClass+'" />').appendTo(this.$container)


            /**
             * Create dropdown options
             */

            var o = '', k = 0, p = '';

            this.selectedIndex = this.$el.prop('selectedIndex')

            this.$options.each(function(i, e){

                if(e.nodeName.toLowerCase() == 'optgroup'){

                    return o+='<li class="option-group">'+this.label+'</li>'
                }

                if(!e.value) p = e.innerHTML

                o+='<li class="'+(this.disabled? self.settings.disabledClass : "option-item") + ' ' +(k == self.selectedIndex? self.settings.selectedClass : '')+ '" data-index="'+(k)+'" data-value="'+this.value+'">'+ (this.innerHTML) + '</li>'

                k++;
            })

            this.$dropdown.html(o)

            /**
             * Items
             */

            this.$items = this.$dropdown.children();


            /**
             * Append Input
             */

            this.$input = $('<input type="text"' + (isMobile? 'tabindex="-1"': '') + ' placeholder="'+p+'" class="'+ this.settings.inputClass + '">').appendTo(this.$container)

            /* Update input text */

            this._updateInput()

        },

        _events: function(){

            /* Input: focus */

            this.$container.on('focus.input', 'input', $.proxy(this._focus, this))

            /**
             * Input: mouseup
             * For input select() event to function correctly
             */
            this.$container.on('mouseup.input', 'input', function(e){
                e.preventDefault()
            })

            /* Input: blur */

            this.$container.on('blur.input', 'input', $.proxy(this._blur, this))

            /* Select: change */

            this.$el.on('change.select', $.proxy(this._change, this))

            /* Select: focus */

            this.$el.on('focus.select', $.proxy(this._focus, this))

            /* Select: blur */

            this.$el.on('blur.select', $.proxy(this._blurSelect, this))

            /* Dropdown Arrow: click */

            this.$container.on('click.arrow', '.'+this.settings.comboArrowClass , $.proxy(this._toggle, this))

            /* Dropdown: close */

            this.$container.on('comboselect:close', $.proxy(this._close, this))

            /* Dropdown: open */

            this.$container.on('comboselect:open', $.proxy(this._open, this))


            /* HTML Click */

            $('html').off('click.comboselect').on('click.comboselect', function(){
                
                $.each($.fn[ pluginName ].instances, function(i, plugin){

                    plugin.$container.trigger('comboselect:close')

                })
            });

            /* Stop `event:click` bubbling */

            this.$container.on('click.comboselect', function(e){
                e.stopPropagation();
            })


            /* Input: keydown */

            this.$container.on('keydown', 'input', $.proxy(this._keydown, this))

            /* Input: keyup */
            
            this.$container.on('keyup', 'input', $.proxy(this._keyup, this))

            /* Dropdown item: click */

            this.$container.on('click.item', '.option-item', $.proxy(this._select, this))

        },

        _keydown: function(event){

            

            switch(event.which){

                case keys.UP:
                    this._move('up', event)
                    break;

                case keys.DOWN:
                    this._move('down', event)
                    break;
                
                case keys.TAB:
                    this._enter(event)
                    break;

                case keys.RIGHT:
                    this._autofill(event);
                    break;

                case keys.ENTER:
                    this._enter(event);
                    break;

                default:                            
                    break;


            }

        },
        

        _keyup: function(event){
            
            switch(event.which){
                case keys.ESC:                                                    
                    this.$container.trigger('comboselect:close')
                    break;

                case keys.ENTER:
                case keys.UP:
                case keys.DOWN:
                case keys.LEFT:
                case keys.RIGHT:
                case keys.TAB:
                case keys.SHIFT:                            
                    break;
                
                default:                            
                    this._filter(event.target.value)
                    break;
            }
        },
        
        _enter: function(event){

            var item = this._getHovered()

            item.length && this._select(item);

            /* Check if it enter key */
            if(event && event.which == keys.ENTER){

                if(!item.length) {
                    
                    /* Check if its illegal value */

                    this._blur();

                    return true;
                }

                event.preventDefault();
            }
            

        },
        _move: function(dir){

            var items = this._getVisible(),
                current = this._getHovered(),
                index = current.prevAll('.option-item').filter(':visible').length,
                total = items.length

            
            switch(dir){
                case 'up':
                    index--;
                    (index < 0) && (index = (total - 1));
                    break;

                case 'down':                            
                    index++;
                    (index >= total) && (index = 0);                            
                    break;
            }

            
            items
                .removeClass(this.settings.hoverClass)
                .eq(index)
                .addClass(this.settings.hoverClass)


            if(!this.opened) this.$container.trigger('comboselect:open');

            this._fixScroll()
        },

        _select: function(event){

            var item = event.currentTarget? $(event.currentTarget) : $(event);

            if(!item.length) return;

            /**
             * 1. get Index
             */
            
            var index = item.data('index');

            this._selectByIndex(index);

            this.$container.trigger('comboselect:close')                    

        },

        _selectByIndex: function(index){

            /**
             * Set selected index and trigger change
             * @type {[type]}
             */
            if(typeof index == 'undefined'){
                
                index = 0

            }
            
            if(this.$el.prop('selectedIndex') != index){

                this.$el.prop('selectedIndex', index).trigger('change');
            }

        },

        _autofill: function(){

            var item = this._getHovered();

            if(item.length){

                var index = item.data('index')

                this._selectByIndex(index)

            }

        },
        

        _filter: function(search){

            var self = this,
                items = this._getAll();
                needle = $.trim(search).toLowerCase(),
                reEscape = new RegExp('(\\' + ['/', '.', '*', '+', '?', '|', '(', ')', '[', ']', '{', '}', '\\'].join('|\\') + ')', 'g'),
                pattern = '(' + search.replace(reEscape, '\\$1') + ')';


            /**
             * Unwrap all markers
             */
            
            $('.'+self.settings.markerClass, items).contents().unwrap();

            /* Search */
            
            if(needle){

                /* Hide Disabled and optgroups */

                this.$items.filter('.option-group, .option-disabled').hide();
            
                
                items                            
                    .hide()
                    .filter(function(){

                        var $this = $(this),
                            text = $.trim($this.text()).toLowerCase();
                        
                        /* Found */
                        if(text.toString().indexOf(needle) != -1){
                                                                
                            /**
                             * Wrap the selection
                             */                                    
                            
                            $this
                                .html(function(index, oldhtml){
                                return oldhtml.replace(new RegExp(pattern, 'gi'), '<span class="'+self.settings.markerClass+'">$1</span>')
                            })                                    

                            return true
                        }

                    })
                    .show()
            }else{

                                
                this.$items.show();
            }

            /* Open the comboselect */

            this.$container.trigger('comboselect:open')
            

        },

        _highlight: function(){

            /* 
            1. Check if there is a selected item 
            2. Add hover class to it
            3. If not add hover class to first item
            */
        
            var visible = this._getVisible().removeClass(this.settings.hoverClass),
                $selected = visible.filter('.'+this.settings.selectedClass)

            if($selected.length){
                
                $selected.addClass(this.settings.hoverClass);

            }else{

                visible
                    .removeClass(this.settings.hoverClass)
                    .first()
                    .addClass(this.settings.hoverClass)
            }

        },

        _updateInput: function(){

            var selected = this.$el.prop('selectedIndex')
            
            if(this.$el.val()){
                
                text = this.$el.find('option').eq(selected).text()

                this.$input.val(text)

            }else{
                
                this.$input.val('')

            }

            return this._getAll()
                .removeClass(this.settings.selectedClass)
                .filter(function(){

                    return $(this).data('index') == selected
                })
                .addClass(this.settings.selectedClass)
        
        },
        _blurSelect: function(){

            this.$container.removeClass('combo-focus');

        },
        _focus: function(event){
            
            /* Toggle focus class */

            this.$container.toggleClass('combo-focus', !this.opened);

            /* If mobile: stop */
            
            if(isMobile) return;

            /* Open combo */

            if(!this.opened) this.$container.trigger('comboselect:open');
            
            /* Select the input */
            
            this.settings.focusInput && event && event.currentTarget && event.currentTarget.nodeName == 'INPUT' && event.currentTarget.select()
        },

        _blur: function(){

            /**
             * 1. Get hovered item
             * 2. If not check if input value == select option
             * 3. If none
             */
            
            var val = $.trim(this.$input.val().toLowerCase()),
                isNumber = !isNaN(val);
            
            var index = this.$options.filter(function(){
                
                if(isNumber){
                    return parseInt($.trim(this.innerHTML).toLowerCase()) == val
                }

                return $.trim(this.innerHTML).toLowerCase() == val

            }).prop('index')
        
            /* Select by Index */
                        
            this._selectByIndex(index)
            
        },

        _change: function(){


            this._updateInput();

        },

        _getAll: function(){

            return this.$items.filter('.option-item')

        },
        _getVisible: function(){

            return this.$items.filter('.option-item').filter(':visible')

        },

        _getHovered: function(){

            return this._getVisible().filter('.' + this.settings.hoverClass);

        },

        _open: function(){

            var self = this

            this.$container.addClass('combo-open')            

            this.opened = true
            
            /* Focus input field */            

            this.settings.focusInput && setTimeout(function(){ !self.$input.is(':focus') && self.$input.focus(); });

            /* Highligh the items */

            this._highlight()

            /* Fix scroll */

            this._fixScroll()

            /* Close all others */


            $.each($.fn[ pluginName ].instances, function(i, plugin){

                if(plugin != self && plugin.opened) plugin.$container.trigger('comboselect:close')
            })

        },

        _toggle: function(){

            this.opened? this._close.call(this) : this._open.call(this)
        },

        _close: function(){                

            this.$container.removeClass('combo-open combo-focus')

            this.$container.trigger('comboselect:closed')

            this.opened = false

            /* Show all items */

            this.$items.show();

        },
        _fixScroll: function(){
    
            /**
             * If dropdown is hidden
             */
            if(this.$dropdown.is(':hidden')) return;

            
            /**
             * Else                     
             */
            var item = this._getHovered();

            if(!item.length) return;                    

            /**
             * Scroll
             */
            
            var offsetTop,
                upperBound,
                lowerBound,
                heightDelta = item.outerHeight()

            offsetTop = item[0].offsetTop;
            
            upperBound = this.$dropdown.scrollTop();

            lowerBound = upperBound + this.settings.maxHeight - heightDelta;
            
            if (offsetTop < upperBound) {
                    
                this.$dropdown.scrollTop(offsetTop);

            } else if (offsetTop > lowerBound) {
                    
                this.$dropdown.scrollTop(offsetTop - this.settings.maxHeight + heightDelta);
            }

        },
        /**
         * Destroy API
         */
        
        dispose: function(){

            /* Remove combo arrow, input, dropdown */

            this.$arrow.remove()

            this.$input.remove()

            this.$dropdown.remove()

            /* Remove tabindex property */
            this.$el
                .removeAttr("tabindex")

            /* Check if there is a tabindex set before */

            if(!!this.$el.data('plugin_'+ dataKey + '_tabindex')){
                this.$el.prop('tabindex', this.$el.data('plugin_'+ dataKey + '_tabindex'))
            }

            /* Unwrap */

            this.$el.unwrap()

            /* Remove data */

            this.$el.removeData('plugin_'+dataKey)

            /* Remove tabindex data */

            this.$el.removeData('plugin_'+dataKey + '_tabindex')

            /* Remove change event on select */

            this.$el.off('change.select focus.select blur.select');

        }    
    });



    // A really lightweight plugin wrapper around the constructor,
    // preventing against multiple instantiations
    $.fn[ pluginName ] = function ( options, args ) {

        this.each(function() {

            var $e = $(this),
                instance = $e.data('plugin_'+dataKey)

            if (typeof options === 'string') {
                
                if (instance && typeof instance[options] === 'function') {
                        instance[options](args);
                }

            }else{

                if (instance && instance.dispose) {
                        instance.dispose();
                }

                $.data( this, "plugin_" + dataKey, new Plugin( this, options ) );

            }

        });

        // chain jQuery functions
        return this;
    };

    $.fn[ pluginName ].instances = [];

}));

    


