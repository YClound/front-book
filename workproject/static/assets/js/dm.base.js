/* 全局 */
(function(){
     window.COVER = {
        version: "0.0.1",
        debug: !1,
        imgServer: "http://7xte9b.com1.z0.glb.clouddn.com/",
        DownloadApp: "http://a.app.qq.com/o/simple.jsp?pkgname=cn.thecover.www.covermedia",
        ApiToken: sessionStorage.getItem("TOKEN"),
        wx_appid: "wxd25e4cb6f268aca0",
        timestamp: (new Date).getTime(),
        apiurl: {
            weixingetConf: "http://www.chuyingfund.com/jssdk/signAlgorithm.html",
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
        photoUrl: function(e) {
            return this.imgServer + e
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
        ajax: function(e, t, n) {
           
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
        backTop: function() {
            var e = document.querySelector(".tc-back-top");
            COVER.isEmpty(e) || (e.onclick = function() {
                window.scrollTo(0, 0)
            }),
            window.onscroll = function() {
                var t = document.documentElement.scrollTop || document.body.scrollTop,
                n = window.screen.availHeight;
                t > n / 2 ? e.style.display = "inline-block": e.style.display = "none"
            }
        },
        backPrev: function() {
            var e = window.history.go( - 1);
            if (this.isEmpty(e)) {
                var t = localStorage.getItem("_backurl");
                this.isEmpty(t) ? this.skip("index.html") : this.skip(t)
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
                      complete: function(xhr, textStatus) {
                        
                        //called when complete
                      },
                      success: function(data, textStatus, xhr) {
                        /*if(typeof data === 'string'){
                            document.getElementById('xxx').innerHTML=data;
                        }else {
                            document.getElementById('xxx').innerHTML=JSON.stringify(data);
                        }*/
                        var r = data;
                        wx.config({
                            debug: !1,
                            appId: window.COVER.wx_appid,
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
            mqq: {
                setShareInfo: function(e) {
                    e.image_url = e.imgUrl,
                    e.share_url = e.link,
                    e.callback = e.success,
                    mqq.setShareInfo(e)
                }
            },
            qzone: {
                share: function(e) {
                    mqq.invoke("share", "toQQ", e, e.success),
                    mqq.invoke("share", "toQQ", e, e.success)
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
    window.addEventListener('error', function (e) {
        var target = e.target
        if (target.tagName === 'IMG' && !target.hasAttribute('noerr')) {
            target.setAttribute('noerr', '1')
            var dft = target.dataset ? target.dataset.default : target.getAttribute('data-default')
            target.src = '/Public/Home/mobile/img/' + (dft || 'default-img.jpg')
        }
    }, true)
    // fz = 320px/20=16px, max-width=640px
    /*!function () {
        var a = document.documentElement;
        var b = a.clientWidth / 20;
        a.style.fontSize = (b > 32 ? 32 : b) + "px"
    }();*/

    var console, length, method, methods, noop;
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
    }

    

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

/* plugins */
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
                    this.options.remote && this.$modal.find(".modal-content").load(this.options.remote,
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
            i = device && device.mobile() ? '<div class="modal modal-alert fade" tabindex="-1"><div class="modal-dialog"><div class="modal-content"><div class="modal-body">' + t + '</div><div class="modal-footer"><button type="button" class="btn-primary" data-dismiss="modal">确定</button></div></div></div></div>': '<div class="modal modal-alert fade" tabindex="-1"><div class="modal-dialog"><div class="modal-content"><div class="modal-header">提示<button type="button" class="close" data-dismiss="modal">&times;</button></div><div class="modal-body">' + t + '</div><div class="modal-footer"><button type="button" class="btn-primary" data-dismiss="modal">确定</button></div></div></div></div>';
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

    $(function() {
        $("form").not("[novalidate]").validate("init")
    });

    //word-count
    $(function () {
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

        $('input[maxlength], textarea[maxlength]').not('[nocount]').wordCount()
    });
    // 密码 明示还是不显示
    $(function() {
        device.mobile() && $("input[type=password]").each(function() {
            var t = $(this),
            e = t.siblings(".show-password");
            e.length || (e = $('<div class="show-password poa r-0 t-3 p-6 c-c3 fz-medium status-off"><i class="if icon-eye-fill on"></i> <i class="if icon-eye-close off"></i></div>').insertAfter(t)),
            e.on("click",
            function() {
                "text" === t.attr("type") ? (t.attr("type", "password"), e.removeClass("status-on").addClass("status-off")) : (t.attr("type", "text"), e.removeClass("status-off").addClass("status-on"))
            })
        })
    });
    // ...
    $.fn.prettyCheck = function() {
        return this.each(function() {
            var t = $(this);
            t.siblings(".dummy-check").length || t.after('<label class="dummy-check" for="' + t.attr("id") + '"></label>')
        })
    };
    $(function() {
        $(".check-square, .check-circle, .radio-circle").find(":checkbox, :radio").prettyCheck()
    });
    // 加减
    /*$(".number-pick").each(function() {

        var t = $(this),
        e = t.find(".btn-minus"),
        n = t.find(".btn-plus"),
        i = t.find(".input-visual"),
        a = t.find(".input-real"),
        o = parseFloat(i.data("min")),
        r = parseFloat(i.data("max")),
        s = parseFloat(i.data("step"));
        e.click(function() {
            console.log('btn-minus click');
            var e = parseFloat(a.val());
            if (e -= s, o > e) {
                var n = i.data("min-msg") || "小于最小值 @@";
                return n = n.replace(/@@/g, o),
                void i.bubble(n)
            }
            a.val(e),
            i.val(dm.sepThousand(e)),
            t.trigger("update", e)
        }),
        n.click(function() {
            console.log('btn-plus click');
            var e = parseFloat(a.val());
            return e += s,
            e > r ? void i.bubble("超出最大值") : (a.val(e), i.val(dm.sepThousand(e)), void t.trigger("update", e))
        }),
        i.on("input propertychange",
        function() {
            var e = $(this).val().replace(/,/g, "");
            a.val(e),
            t.trigger("update", e)
        })
    });*/
    // 图片验证码
    $.fn.reloadVerify = function() {
        return this.each(function() {
            var t = $(this),
            e = t.attr("src");~e.indexOf("?") ? t.attr("src", e + "&random=" + Math.random()) : t.attr("src", e.replace(/\?.*$/, "") + "?" + Math.random())
        })
    },
    $(".verify-img").click(function() {
        $(this).reloadVerify()
    });



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
                alert: window.device && device.mobile() ? "notice": "alert"
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

                } else if (res.status > 0) {
                    
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
                } else if ('' + res.status === '0') {
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
                    threshold: 0
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
                    if ($e.is(':hidden')) {
                        return false
                    }
                    var et = l($e),
                        eb = et + $e.height();

                    //console.log('inview','window.scrollTop:',wt,',windowB:',wb,',loadingTop:',et,',loadingB:',eb,',threshold:',o.threshold);
                    //console.log('result:',et,wb,et <= wb + o.threshold,eb >= wt - o.threshold && et <= wb + o.threshold);
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
                $offset = $form.find("[name=offset]") || $form.find("[data-name=offset]"),
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
            var templateId = $wrap.data('template');
            var $list = $($wrap.data('listWrap'));
            var parseData = (options && options.parseData)?options.parseData : function(){};
            
            if(!$wrap.data("init") && templateId){
                console.log('templateId',templateId)
                $wrap.on('data',function(e,json){
                    
                    var html = '',list = json.list;
                    
                    parseData(list);
                    for (var i = 0, len = list.length; i < len; i++) {
                        html += template(templateId, list[i])
                    }
                    var $list2 = $(html).appendTo($list);
                    //$list.append(html);
                    //console.log('on data 111111111');
                           
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
                $offset = $form.find("[name=offset]") || $form.find("[data-name=offset]"),
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
                    console.log('totalPage',totalPage,',offset',offset);
                    if(0 === totalPage) {
                        $wrap.addClass("blank");
                    }else {
                        if(null != totalPage && offset > totalPage) {
                            $wrap.addClass("end")
                        }else {
                            
                            $loading.inview(function(){
                                //console.warn('submit');
                                $form.submit();
                            },{threshold:0});
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

        /*$(function(){
            $(".infinite-loading").on('data',function(e,json){
                console.log(json)
            });
            $(".infinite-loading:visible").infiniteLoading();
        });*/
        //console.log(1)
        //$(".infinite-loading:visible").infiniteLoading();

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

    $(function () {
        $('.city-select').citySelect()
    });


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
    $(function() {
        $(".paginator").paginator()
    });

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
    $(function(){
        window.smsHelper.init();
    });


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

    /* dmSelect */
    (function(t) {
        
        t.fn.extend({
            dmSelect: function(e) {
                if ("undefined" == typeof document.body.style.maxHeight) return this;
                var n = {
                    customClass: "dm-select-",
                    mapClass: !0,
                    mapStyle: !0
                };
                e = t.extend(n, e);
                var i = e.customClass,
                a = function(e, n) {
                    var i = e.find(":selected"),
                    a = n.children(":first"),
                    r = i.html() || "&nbsp;";
                    a.html(r),
                    i.attr("disabled") ? n.addClass(o("disabled-option")) : n.removeClass(o("disabled-option")),
                    setTimeout(function() {
                        n.removeClass(o("open")),
                        t(document).off("mouseup.dmSelect")
                    },
                    60)
                },
                o = function(t) {
                    return i + t
                };
                return this.each(function() {
                    var n = t(this),
                    i = t("<span></span>").addClass(o("inner")),
                    r = t("<span></span>");
                    n.after(r.append(i).append('<i class="if icon-angle-down poa r-0 zi-1"></i>')),
                    r.addClass("dm-select"),
                    e.mapClass && r.addClass(n.attr("class")),
                    e.mapStyle && r.attr("style", n.attr("style")),
                    n.addClass("has-dm-select").on("render.dmSelect",
                    function() {
                        a(n, r),
                        n.css("width", "");
                        var t = parseInt(n.outerWidth(), 10) - (parseInt(r.outerWidth(), 10) - parseInt(r.width(), 10));
                        r.css({
                            display: "inline-block"
                        });
                        var e = r.outerHeight();
                        n.attr("disabled") ? r.addClass(o("disabled")) : r.removeClass(o("disabled")),
                        i.css({
                            width: t,
                            display: "inline-block"
                        }),
                        n.css({
                            "-webkit-appearance": "menulist-button",
                            width: r.outerWidth(),
                            position: "absolute",
                            opacity: 0,
                            height: e,
                            fontSize: r.css("font-size")
                        })
                    }).on("change.dmSelect",
                    function() {
                        r.addClass(o("changed")),
                        a(n, r)
                    }).on("keyup.dmSelect",
                    function(t) {
                        r.hasClass(o("open")) ? 13 != t.which && 27 != t.which || a(n, r) : (n.trigger("blur.dmSelect"), n.trigger("focus.dmSelect"))
                    }).on("mousedown.dmSelect",
                    function() {
                        r.removeClass(o("changed"))
                    }).on("mouseup.dmSelect",
                    function(e) {
                        r.hasClass(o("open")) || (t("." + o("open")).not(r).length > 0 && "undefined" != typeof InstallTrigger ? n.trigger("focus.dmSelect") : (r.addClass(o("open")), e.stopPropagation(), t(document).one("mouseup.dmSelect",
                        function(e) {
                            e.target != n.get(0) && t.inArray(e.target, n.find("*").get()) < 0 ? n.trigger("blur.dmSelect") : a(n, r)
                        })))
                    }).on("focus.dmSelect",
                    function() {
                        r.removeClass(o("changed")).addClass(o("focus"))
                    }).on("blur.dmSelect",
                    function() {
                        r.removeClass(o("focus") + " " + o("open"))
                    }).on("mouseenter.dmSelect",
                    function() {
                        r.addClass(o("hover"))
                    }).on("mouseleave.dmSelect",
                    function() {
                        r.removeClass(o("hover"))
                    }).trigger("render.dmSelect")
                })
            }
        })
    }) (jQuery);
    

    // 分享
    $(function(){
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
    });


})();
    


