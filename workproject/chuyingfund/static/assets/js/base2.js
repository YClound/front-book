!
function(t) {
    function e(i) {
        if (n[i]) return n[i].exports;
        var a = n[i] = {
            exports: {},
            id: i,
            loaded: !1
        };
        return t[i].call(a.exports, a, a.exports, e),
        a.loaded = !0,
        a.exports
    }
    var n = {};
    return e.m = t,
    e.c = n,
    e.p = "",
    e(0)
} ([function(t, e, n) {
    n(1),
    n(2),
    n(3),
    n(4),
    n(5),
    n(6),
    n(7),
    n(8),
    n(9),
    n(10),
    n(11),
    n(12),
    n(13),
    n(14),
    n(15),
    n(16),
    n(17),
    n(18),
    t.exports = n(19)
},
function(t, e) { !
    function() {
        var t, e, n, i, a;
        for (n = void 0, a = function() {},
        i = ["assert", "clear", "count", "debug", "dir", "dirxml", "error", "exception", "group", "groupCollapsed", "groupEnd", "info", "log", "markTimeline", "profile", "profileEnd", "table", "time", "timeEnd", "timeline", "timelineEnd", "timeStamp", "trace", "warn"], e = i.length, t = window.console = window.console || {}; e--;) n = i[e],
        t[n] || (t[n] = a)
    } ()
},
function(t, e) { !
    function(t, e, n) {
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
    } (window, document)
},
function(t, e, n) {
    var i; (function() {
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
        i = function() {
            return a
        }.call(e, n, e, t),
        !(void 0 !== i && (t.exports = i))
    }).call(this)
},
function(t, e) {
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
    $.fn.flushStyle = function(t) {
        return this.each(function() {
            this.offsetWidth,
            setTimeout($.proxy(t, this), 0)
        })
    },
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
    dm.sepThousand = function(t) {
        var e = t.toString(),
        n = e.indexOf(".");
        return e.replace(/\d(?=(?:\d{3})+(?:\.|$))/g,
        function(t, e) {
            return 0 > n || n > e ? t + ",": t
        })
    },
    dm.sepCardNo = function(t) {
        return ("" + t).replace(/\D/g, "").replace(/....(?!$)/g, "$& ")
    },
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
    dm.projStageName = function(t) {
        return {
            0 : "椤圭洰鍙戣捣/瀹℃牳涓�",
            1 : "棰勭儹涓�",
            4 : "鍚堟姇涓�",
            5 : "铻嶈祫鎵撴涓�",
            8 : "铻嶈祫缁撴潫",
            9 : "浼楃鎴愬姛"
        } [t] || ""
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
            0 : "浼楃",
            1 : "鏀惰喘",
            2 : "澧炶祫"
        } [t]
    },
    dm.profitType = function(t) {
        return {
            1 : "杞",
            2 : "鎶曡祫",
            3 : "鍒嗙孩"
        } [t]
    },
    dm.afterStatus = function(t) {
        return {
            0 : "鏈夋晥鎶曡祫",
            1 : "杞涓�",
            2 : "杞鎴愬姛",
            3 : "閫€鍑哄鐞嗕腑",
            4 : "閫€鍑烘垚鍔�"
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
        dm.confirm("鎮ㄨ繕鏈櫥褰曪紝璇风櫥褰曞悗閲嶈瘯锛岀偣鍑荤‘瀹氬墠寰€鐧诲綍椤甸潰",
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
},
function(t, e) {
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
        i = device && device.mobile() ? '<div class="modal modal-alert fade" tabindex="-1"><div class="modal-dialog"><div class="modal-content"><div class="modal-body">' + t + '</div><div class="modal-footer"><button type="button" class="btn-primary" data-dismiss="modal">纭畾</button></div></div></div></div>': '<div class="modal modal-alert fade" tabindex="-1"><div class="modal-dialog"><div class="modal-content"><div class="modal-header">鎻愮ず<button type="button" class="close" data-dismiss="modal">&times;</button></div><div class="modal-body">' + t + '</div><div class="modal-footer"><button type="button" class="btn-primary" data-dismiss="modal">纭畾</button></div></div></div></div>';
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
    dm.toast = function(t, e, n) {
        dm.notice(t, e, "dm-toast")
    },
    dm.confirm = function(t, e, n, i, a) {
        var o = {
            yes: "纭畾",
            no: "鍙栨秷"
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
        r = device && device.mobile() ? '<div class="modal modal-confirm fade" tabindex="-1"><div class="modal-dialog"><div class="modal-content"><div class="modal-body">' + t + '</div><div class="modal-footer"><button type="button" class="btn-primary confirm" data-dismiss="modal">' + s.yes + '</button><button type="button" class="btn-o cancel" data-dismiss="modal">' + s.no + "</button></div></div></div></div>": '<div class="modal modal-confirm fade" tabindex="-1"><div class="modal-dialog"><div class="modal-content"><div class="modal-header">鎻愮ず<button type="button" class="close" data-dismiss="modal">&times;</button></div><div class="modal-body">' + t + '</div><div class="modal-footer"><button type="button" class="btn-primary confirm" data-dismiss="modal">' + s.yes + '</button><button type="button" class="btn-o cancel" data-dismiss="modal">' + s.no + "</button></div></div></div></div>";
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
},
function(t, e) { !
    function(t) {
        t.fn.bubble = function(e, n, i) {
            var a, o, r;
            return null == n && (n = "down"),
            e = null != (r = this.data("bubbleMsg")) ? r: e,
            n = this.data("bubbleDir") || n,
            i = this.data("bubbleDuration") || i || 500 * Math.max(e.length, 8),
            a = this.siblings(".bubble"),
            o = {
                up: "down",
                down: "up",
                right: "left",
                left: "right"
            },
            0 === a.length && (a = t('<div class="bubble bubble-' + n + " caret-" + o[n] + '"></div>').insertAfter(this)),
            a.html(e),
            "right" === n ? a.css({
                left: null != this.data("bubbleLeft") ? this.data("bubbleLeft") : this.outerWidth() + 8,
                top: null != this.data("bubbleTop") ? this.data("bubbleTop") : (this.outerHeight() - a.outerHeight()) / 2
            }) : "down" === n ? a.css({
                left: null != this.data("bubbleLeft") ? this.data("bubbleLeft") : .5 * this.outerWidth(),
                top: null != this.data("bubbleTop") ? this.data("bubbleTop") : this.outerHeight() + 8
            }) : "up" === n && a.css({
                left: null != this.data("bubbleLeft") ? this.data("bubbleLeft") : .5 * this.outerWidth(),
                bottom: null != this.data("bubbleBottom") ? this.data("bubbleBottom") : this.outerHeight() + 8
            }),
            clearTimeout(a.data("bubbleTimer")),
            a.data("bubbleTimer", setTimeout(function() {
                return a.removeBubble()
            },
            i)),
            this
        },
        t.fn.removeBubble = function() {
            return t(this).hasClass("bubble") ? this.remove() : this.siblings(".bubble").remove(),
            this
        }
    } (jQuery)
},
function(module, exports) { !
    function($) {
        var defaultOption, initForm, initInput, matchRule, msgInfo, patternInfo, validateForm, validateInput;
        patternInfo = {
            required: /\S+/,
            email: /^[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$/,
            url: /^\s*((https?|ftp):\/\/)?(([^:\n\r]+):([^@\n\r]+)@)?((www\.)?([^\/\n\r]+))\/?([^?\n\r]+)?\??([^#\n\r]*)?#?([^\n\r]*)$/,
            tel: /^\s*(?:\+?(\d{1,3}))?([-. (]*(\d{3})[-. )]*)?((\d{3})[-. ]*(\d{2,4})(?:[-.x ]*(\d+))?)\s*$/,
            number: /^\d[\d\.]*$/
        },
        msgInfo = {
            required: "璇峰～鍐欐瀛楁銆�",
            email: "email 涓嶅悎娉�",
            url: "url 涓嶅悎娉�",
            tel: "鐢佃瘽鍙风爜涓嶅悎娉�",
            number: "涓嶆槸鏁板瓧",
            maxlength: "瓒呭嚭瀛楁暟闄愬埗",
            min: "蹇呴』澶т簬@@",
            max: "蹇呴』灏忎簬@@",
            pattern: "涓嶅悎娉�"
        },
        matchRule = function(str, rule, pattern) {
            var result = !0;
            switch (null == pattern && (pattern = !0), rule) {
            case "required":
                pattern && (result = patternInfo.required.test(str));
                break;
            case "email":
                pattern && (result = patternInfo.email.test(str));
                break;
            case "url":
                pattern && (result = patternInfo.url.test(str));
                break;
            case "tel":
                pattern && (result = patternInfo.tel.test(str));
                break;
            case "number":
                pattern && (result = patternInfo.number.test(str));
                break;
            case "maxlength":
                return $.trim(str).length <= pattern;
            case "min":
                return $.isNumeric(pattern) ? parseFloat(str) >= parseFloat(pattern) : new Date(str) >= new Date(pattern);
            case "max":
                return $.isNumeric(pattern) ? parseFloat(str) <= parseFloat(pattern) : new Date(str) <= new Date(pattern);
            case "pattern":
                return eval(pattern).test(str)
            }
            return result
        },
        defaultOption = {
            batch: !1,
            typing: !1,
            blur: !1
        },
        $.fn.buildRuleList = function() {
            return this.each(function(t, e) {
                var n, i, a, o = {},
                r = $(e),
                s = r.attr("type");
                o.required = r.attr("required");
                for (var d = ["email", "url", "tel", "number"], l = 0, c = d.length; c > l; l++) {
                    var u = d[l];
                    if (s === u) {
                        o[u] = !0;
                        break
                    }
                }
                i = ["maxlength", "max", "min", "pattern"];
                for (var f = 0,
                h = i.length; h > f; f++) n = i[f],
                a = r.attr(n),
                null != a && "" !== a && (o[n] = a);
                var m = r.data("ajax");
                m && (o.ajax = m),
                r.data("ruleList", o)
            })
        },
        initForm = function(t) {
            t.attr("novalidate", "novalidate").find("input, textarea, select").validate("init")
        },
        initInput = function(t) {
            t.buildRuleList().off(".validate").on("input.validate propertychange.validate",
            function() {
                return $(this).validate({
                    typing: !0
                })
            }),
            t.on("blur.validate",
            function() {
                var t = $(this);
                t.val() && t.validate({
                    blur: !0
                })
            })
        },
        validateForm = function(t, e, n, i) {
            var a = !0;
            t.data("bubbled", 0),
            t.find(".bubble").removeBubble(),
            t.find("input, textarea, select").not("[disabled]").validate({
                batch: !0
            },
            $.noop,
            function() {
                a = !1
            }),
            a ? n() : i()
        },
        validateInput = function(t, e, n, i) {
            if (!e.typing || t.data("readyValidate")) {
                var a = $.trim(t.val()),
                o = t.data("ruleList"),
                r = !0,
                s = "";
                if (o || (o = t.buildRuleList().data("ruleList")), t.removeBubble(), a.length > 0 || o.required) {
                    for (var d in o) if ("ajax" != d && o.hasOwnProperty(d)) {
                        var l = o[d];
                        if (!matchRule(a, d, l)) {
                            r = !1,
                            s = t.data(d + "Msg") || msgInfo[d],
                            s = s.replace(/@@/g, l);
                            break
                        }
                    }
                    if (a.length && o.ajax && t.data("lastValidateValue") !== t.val()) {
                        var c = function() {
                            t.data("ajaxReady", !1),
                            t.data("ajaxQueue", !1),
                            t.data("lastValidateValue", t.val());
                            var e = {};
                            e[t.attr("name")] = t.val(),
                            dm.getJSON(o.ajax, e, {
                                alert: "noop"
                            }).fail(function(e) {
                                t.addClass("error").bubble(e.info)
                            }).always(function() {
                                t.data("ajaxReady", !0),
                                t.data("ajaxQueue") && c()
                            })
                        },
                        u = t.data("ajaxReady");
                        null == u || u ? c() : t.data("ajaxQueue", !0)
                    }
                }
                if (r) t.removeClass("error"),
                n();
                else {
                    var f = t.closest("form");
                    if (e.typing) t.addClass("error");
                    else if (t.addClass("error"), !e.batch || !f.data("bubbled")) {
                        f.data("bubbled", 1);
                        var h = "down";
                        t.bubble(s, t.data("bubbleDir") || h),
                        e.blur || t.focus()
                    }
                    i()
                }
                return t.data("readyValidate", !0)
            }
        },
        $.fn.validate = function(t, e, n) {
            var i;
            return null == e && (e = $.noop),
            null == n && (n = $.noop),
            "string" != typeof t ? (t = $.extend({},
            defaultOption, t), this.each(function() {
                var i;
                i = $(this),
                i.is("form") ? validateForm(i, t, e, n) : validateInput(i, t, e, n)
            })) : (i = t, t = e, "init" === i ? this.each(function() {
                var t;
                return t = $(this),
                t.is("form") ? initForm(t) : initInput(t)
            }) : void 0)
        }
    } (jQuery),
    $(function() {
        $("form").not("[novalidate]").validate("init")
    }),
    $(function() {
        var t = function(t, e) {
            var n = $('<div class="word-count"><var>' + t.val().length + "</var>/" + e + "</div>").insertAfter(t);
            return n.find("var")
        };
        $.fn.wordCount = function() {
            return this.each(function() {
                var e = $(this),
                n = e.attr("maxlength");
                if (n) {
                    var i = t(e, n);
                    e.is("input") && e.css("padding-right", ("" + n).length + 1.8 + "em"),
                    e.on("input.count propertychange.count",
                    function() {
                        i.html(e.val().length)
                    })
                }
            })
        },
        $("input[maxlength], textarea[maxlength]").not("[nocount]").wordCount()
    }),
    $(function() {
        device.mobile() && $("input[type=password]").each(function() {
            var t = $(this),
            e = t.siblings(".show-password");
            e.length || (e = $('<div class="show-password poa r-0 t-0 p-6 c-c3 fz-medium status-off"><i class="if icon-eye-fill on"></i> <i class="if icon-eye-close off"></i></div>').insertAfter(t)),
            e.on("click",
            function() {
                "text" === t.attr("type") ? (t.attr("type", "password"), e.removeClass("status-on").addClass("status-off")) : (t.attr("type", "text"), e.removeClass("status-off").addClass("status-on"))
            })
        })
    }),
    $.fn.prettyCheck = function() {
        return this.each(function() {
            var t = $(this);
            t.siblings(".dummy-check").length || t.after('<label class="dummy-check" for="' + t.attr("id") + '"></label>')
        })
    },
    $(function() {
        $(".check-square, .check-circle, .radio-circle").find(":checkbox, :radio").prettyCheck()
    }),
    $(".number-pick").each(function() {
        var t = $(this),
        e = t.find(".btn-minus"),
        n = t.find(".btn-plus"),
        i = t.find(".input-visual"),
        a = t.find(".input-real"),
        o = parseFloat(i.data("min")),
        r = parseFloat(i.data("max")),
        s = parseFloat(i.data("step"));
        e.click(function() {
            var e = parseFloat(a.val());
            if (e -= s, o > e) {
                var n = i.data("min-msg") || "灏忎簬鏈€灏忓€� @@";
                return n = n.replace(/@@/g, o),
                void i.bubble(n)
            }
            a.val(e),
            i.val(dm.sepThousand(e)),
            t.trigger("update", e)
        }),
        n.click(function() {
            var e = parseFloat(a.val());
            return e += s,
            e > r ? void i.bubble("瓒呭嚭鏈€澶у€�") : (a.val(e), i.val(dm.sepThousand(e)), void t.trigger("update", e))
        }),
        i.on("input propertychange",
        function() {
            var e = $(this).val().replace(/,/g, "");
            a.val(e),
            t.trigger("update", e)
        })
    }),
    $.fn.reloadVerify = function() {
        return this.each(function() {
            var t = $(this),
            e = t.attr("src");~e.indexOf("?") ? t.attr("src", e + "&random=" + Math.random()) : t.attr("src", e.replace(/\?.*$/, "") + "?" + Math.random())
        })
    },
    $(".verify-img").click(function() {
        $(this).reloadVerify()
    })
},
function(t, e) {
    window.dm = window.dm || {},
    !
    function(t) {
        t.fn.disable = function(e, n) {
            return null == n && (n = !0),
            this.each(function() {
                var i = t(this).prop("disabled", !0),
                a = e;
                clearTimeout(i.data("loadingTimer"));
                var o = i.data("html");
                if (null == o && (o = i.html(), i.data("html", o)), a || (a = o), !n) return void i.html(a);
                var r = 0,
                s = function() {
                    i.html(a + new Array(r + 1).join(".")),
                    r++,
                    4 === r && (r = 0),
                    i.data("loadingTimer", setTimeout(s, 1200))
                };
                s()
            })
        },
        t.fn.enable = function() {
            return this.each(function() {
                var e = t(this).prop("disabled", !1);
                clearTimeout(e.data("loadingTimer"));
                var n = e.data("html");
                return n ? e.html(n) : void 0
            })
        }
    } (jQuery),
    $.ajaxSetup({
        cache: !1
    }),
    dm.noop = function() {},
    dm.ajax = function(t, e, n, i) {
        var a = $.Deferred(),
        o = {};
        o.alert = device && device.mobile() ? "notice": "alert";
        var r = $.extend({},
        o, i);
        return $[t](e, n).done(function(e) {
            if ("get" === t) return void a.resolve(e);
            if (e || (dm[r.alert]("鏈嶅姟鍣ㄦ湭杩斿洖鏁版嵁"), a.reject()), "" + e.status == "0") a.resolve(e);
            else if ("" + e.status == "1") {
                var n = [];
                if ("[object Array]" === Object.prototype.toString.call(e.info)) for (var i = e.info,
                o = 0,
                s = i.length; s > o; o++) n.push(i[o].message);
                else n.push(e.info);
                dm[r.alert](n.join("<br>")),
                a.reject(e)
            } else if ("" + e.status == "2") dm.confirm(e.info,
            function() {
                window.location.href = e.data
            }),
            a.reject(e);
            else if ("" + e.status == "9") {
                var n = "",
                d = function() {
                    var t = e.data;
                    t += ~t.indexOf("?") ? "&": "?",
                    location.href = t + "redirect_url=" + encodeURIComponent(location.href)
                };
                "NEED_REAL_NAME_AUTH" === e.info ? n = "鎮ㄨ繕鏈繘琛屽疄鍚嶈璇�, 鏄惁绔嬪嵆鍓嶅線瀹炲悕璁よ瘉": "NEED_BANK_AUTH" === e.info ? n = "鎮ㄨ繕鏈繘琛岄摱琛屽崱瀹炲悕璁よ瘉锛屾槸鍚︾珛鍗冲墠寰€璁よ瘉": "NEED_QUESTION_AUTH" === e.info && (n = "鎮ㄨ繕鏈繘琛屽悎鏍兼姇璧勪汉, 鏄惁绔嬪嵆鍓嶅線鍚堟牸鎶曡祫浜鸿璇�"),
                n ? dm.confirm(n, d) : d(),
                a.reject(e)
            } else dm[r.alert]("鏈煡閿欒"),
            a.reject(e)
        }).fail(function(t) {
            dm[r.alert]("鍔犺浇澶辫触, 璇烽噸璇�"),
            a.reject(t)
        }),
        a
    },
    dm.getJSON = function(t, e, n) {
        return dm.ajax("getJSON", t, e, n)
    },
    dm.get = function(t, e, n) {
        return dm.ajax("get", t, e, n)
    },
    dm.post = function(t, e, n) {
        return dm.ajax("post", t, e, n)
    },
    $(document).off("submit.ajax").on("submit.ajax", "form:not([native])",
    function(t, e) {
        t.preventDefault();
        var n = $(this),
        i = n.find(":submit"),
        a = function() {
            i.disable();
            var t = n.data("ajaxMethod");
            t || (t = "post"),
            n.trigger("beforeSend"),
            dm.ajax(t, n.attr("action"), n.serialize(), n.data()).done(function(t) {
                if (null != e && e.success) for (var i in e) n.triggerHandler(i + "." + e[i], t);
                else n.triggerHandler("success", t)
            }).fail(function(t) {
                n.triggerHandler("fail", t)
            }).always(function(t) {
                i.enable(),
                n.triggerHandler("always", t)
            })
        };
        return null != n.validate ? n.validate(null, a) : a()
    }),
    $(":submit").filter("[name]").click(function() {
        var t = $(this),
        e = t.closest("form"),
        n = t.attr("name"),
        i = e.find("input[name=" + n + "]");
        0 === i.length && (i = $('<input type="hidden" name="' + n + '">').appendTo("form")),
        i.val(t.val())
    })
},
function(t, e) {
    $(function() {
        var t = $(window);
        $.fn.inview = function(e, n) {
            var i = {
                threshold: 0
            },
            a = $.extend({},
            i, n),
            o = this,
            r = o.closest(".view, .scroll-win"),
            s = t;
            r.length && (s = r);
            var d = o.data("inview-id") || 0;
            d ? s.off(".inview" + d) : (d = (s.data("inview-id") || 0) + 1, s.data("inview-id", d), o.data("inview-id", d));
            var l = function(t) {
                return r.length ? t.position().top: t.offset().top
            },
            c = 0,
            u = function() {
                var t = s.scrollTop(),
                e = t + s.height(),
                n = o.filter(function() {
                    var n = $(this);
                    if (n.is(":hidden")) return ! 1;
                    var i = l(n),
                    o = i + n.height();
                    return o >= t - a.threshold && i <= e + a.threshold
                }).trigger("inview", t > c);
                c = t,
                o = o.not(n),
                0 === o.length && s.off(".inview" + d)
            };
            return this.off("inview").one("inview", e),
            s.on("scroll.inview.inview" + d + " resize.inview.inview" + d + " lookup.inview.inview" + d, u),
            u(),
            this
        },
        $.fn.headroom = function(e) {
            var n = {
                offset: 200,
                offsetBottom: 10,
                tolerance: 20,
                unpinnedClass: "headroom--unpinned",
                bottom: !1
            },
            i = 0,
            a = 0,
            o = this.addClass("headroom"),
            r = $.extend({},
            n, e),
            s = function() {
                var e = t.scrollTop(),
                n = function() {
                    e - a > r.tolerance ? o.addClass(r.unpinnedClass).trigger("unpinned") : a - e > r.tolerance && o.removeClass(r.unpinnedClass).trigger("pinned")
                };
                e < r.offset ? o.removeClass(r.unpinnedClass).trigger("pinned") : n(),
                a = e,
                i = 0
            },
            d = function() {
                i || (a = t.scrollTop(), i = setTimeout(s, 100))
            };
            return t.on("scroll.headroom resize.headroom lookup.headroom", d),
            this
        },
        $.fn.simpleStick = function(e) {
            var n = {
                offset: 200,
                stickClass: "stick"
            },
            i = this,
            a = $.extend({},
            n, e),
            o = function() {
                var e = t.scrollTop();
                e <= a.offset ? i.add(i.parent()).removeClass(a.stickClass) : (a.bottom && e > a.bottom ? i.addClass("bottom") : i.removeClass("bottom"), i.add(i.parent()).addClass(a.stickClass))
            },
            r = i.data("stickId");
            return r ? t.off(".simple-stick-" + r) : (r = (new Date).getTime(), i.data("stickId", r)),
            t.on("scroll.simple-stick-" + r + " resize.simple-stick-" + r + " lookup.simple-stick-" + r, o),
            o(),
            this
        },
        $.fn.stick = function(e) {
            var n = this,
            i = n.parent(),
            a = [],
            o = [],
            r = [],
            s = {
                offset: 0,
                hasBottom: !0
            },
            d = $.extend({},
            s, e);
            this.each(function(t) {
                var e = $(this),
                n = e.parent();
                "static" === n.css("position") && n.css("position", "relative"),
                a[t] = e.offset(),
                r[t] = {
                    position: e.css("position"),
                    left: e.css("left"),
                    top: e.css("top"),
                    bottom: e.css("bottom")
                },
                o[t] = n.offset().top + n.height() - e.height()
            });
            var l = function() {
                for (var e = t.scrollTop(), s = 0, l = a.length; l > s; s++) e > a[s].top - d.offset ? d.hasBottom && e > o[s] ? (n.eq(s).css({
                    position: "absolute",
                    left: r[s].left,
                    top: "auto",
                    bottom: 0
                }).addClass("stick"), i.addClass("stick")) : (n.eq(s).css({
                    position: "fixed",
                    left: a[s].left,
                    top: d.offset,
                    bottom: "auto"
                }).addClass("stick"), i.addClass("stick")) : (n.eq(s).css(r[s]).removeClass("stick"), i.removeClass("stick"))
            };
            return t.on("scroll.stick resize.stick lookup.stick", l),
            l(),
            this
        },
        $.fn.scrollAnchor = function(e) {
            var n = {
                threshold: 0
            },
            i = $.extend({},
            n, e),
            a = $(this),
            o = $(),
            r = [],
            s = function() {
                for (var e = t.scrollTop(), n = 0, a = 0, s = r.length; s > a; a++) e > r[a] - i.threshold && (n = a);
                o.removeClass("active").eq(n).addClass("active")
            };
            return {
                init: function() {
                    return this.update(),
                    this.enable(),
                    s(),
                    this
                },
                disable: function() {
                    t.off(".anchor"),
                    clearTimeout(this.timer)
                },
                enable: function() {
                    this.disable(),
                    this.timer = setTimeout(function() {
                        t.on("scroll.anchor resize.anchor lookup.anchor", s)
                    },
                    100)
                },
                update: function() {
                    r = [],
                    a.find("[href^='#']").each(function() {
                        var t = $(this),
                        e = $(t.attr("href"));
                        e.length && (o = o.add(t), r.push(e.offset().top))
                    })
                },
                timer: 0
            }
        },
        $.fn.infiniteLoading = function(t) {
            var e = $(this);
            if (null != t || !e.data("init")) {
                var n = e.find(".loading");
                n.length || (n = $('<div class="loading"><div class="img-dm-loading"></div><u class="loading-text">姝ｅ湪鍔犺浇涓�...</u><u class="end-text">鍔犺浇瀹屾垚</u><u class="blank-text">鏆傛棤璁板綍</u><u class="fail-text">鍔犺浇澶辫触锛岀偣鍑婚噸璇�</u></div>'), e.append(n));
                var i = function() {
                    e.removeClass("end blank fail")
                };
                "reset" === t && i(),
                e.off("click.infiniteLoading").on("click.infiniteLoading",
                function() {
                    e.hasClass("fail") && e.infiniteLoading("reset")
                });
                var a, o, r = e.find("form"),
                s = r.find("[name=offset]"),
                d = parseInt(s.val(), 10) || 1,
                l = e.data("totalPage"),
                c = e.data("listWrap");
                c ? (a = "html", o = $(c)) : (a = "json", r.data("ajaxMethod", "getJSON")),
                r.off(".infiniteLoading").on("success.infiniteLoading",
                function(t, n) {
                    if (d += 1, s.val(d), "html" === a) o.append(n),
                    n ? u() : e.addClass("end");
                    else if ("json" === a) {
                        var i = n.data && n.data.list;
                        l = n.data && n.data.page && n.data.page.total_page,
                        null == i || 0 === i.length ? 2 === d ? e.addClass("blank") : e.addClass("end") : (e.trigger("data", n.data), u())
                    }
                }).on("fail",
                function() {
                    e.addClass("fail")
                });
                var u = function() {
                    return 0 === l ? void e.addClass("blank") : void(null != l && d > l ? e.addClass("end") : n.inview(function() {
                        r.submit()
                    },
                    {
                        threshold: 0
                    }))
                };
                return u(),
                e.data("init", 1),
                this
            }
        },
        $.fn.backtop = function() {
            var e = $(this).on("click",
            function() {
                $("html,body").stop(!0, !1).animate({
                    scrollTop: 0
                },
                400)
            }),
            n = function() {
                var n = t.scrollTop();
                n >= 300 ? e.addClass("in") : e.removeClass("in")
            };
            t.on("scroll.backtop resize.backtop lookup.backtop", n),
            n()
        };
        var e = window.devicePixelRatio > 1;
        $("img[data-src]").inview(function() {
            var t, n = $(this);
            t = e ? n.data("srcRetina") || n.data("src") : n.data("src"),
            n.attr("src", t)
        }),
        $("[data-bgi]").inview(function() {
            var t = $(this);
            t.css("background-image", "url('" + t.data("bgi") + "')")
        }),
        $(".infinite-loading:visible").infiniteLoading(),
        $(".backtop").backtop(),
        device.mobile() && $("body > .s-header").headroom()
    })
},
function(t, e) {
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
                prev: '<a href="javascript:;" class="btn">' + (e.prev || t.data("prev") || "涓婁竴椤� ") + "</a>",
                next: '<a href="javascript:;" class="btn">' + (e.next || t.data("next") || "涓嬩竴椤�") + "</a>",
                onPageChange: function(e, i) {
                    "change" === i && (t.trigger("change", e), n.length && (t.siblings("ul").loading(), n.find("[name=offset]").val(e), n.submit()))
                }
            })
        })
    },
    $(function() {
        $(".paginator").paginator()
    })
},
function(t, e) {
    $.fn.tab = function() {
        var t = $(this),
        e = t.children(),
        n = t.siblings(".tab-content").children(),
        i = function(t) {
            var e = $(t).addClass("active");
            e.siblings().removeClass("active"),
            n.filter(".show").removeClass("show in"),
            n.eq(e.index()).addClass("show").flushStyle().addClass("in"),
            e.trigger("show")
        };
        return {
            init: function() {
                e.on("click",
                function() {
                    i(this)
                }),
                e.filter(".active").trigger("click")
            },
            show: i
        }
    }
},
function(t, e) {
    window.dm = window.dm || {},
    dm.router = function() {
        var t = [],
        e = function(t) {
            return null == t && (t = location.hash),
            t.replace(/^#[!]*/, "")
        },
        n = function(n, i, a) {
            "string" == typeof n && (n = [n]),
            t = n;
            var o = function(e) {
                e && -1 === t.indexOf(e) && (e = ""),
                i(e)
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
        dm.router.go(t)
    })
},
function(t, e) {
    window.dm = window.dm || {},
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
},
function(t, e) {
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
    }
},
function(t, e) {
    window.dm = window.dm || {},
    $(document).on("click", ".go-center",
    function(t) {
        if (!dm.isAPP) {
            t.preventDefault();
            var e = $(this),
            n = e.attr("href");
            if (null == n || 0 === n.indexOf("javascript")) return;
            window.location.href = dm.joinUrlSearch(n, {
                redirect_url: location.href
            })
        }
    }),
    $(document).on("click", "[data-app]",
    function(t) {
        if (dm.isAPP) {
            t.preventDefault();
            var e = $(this).data("app");
            dm.callAPP(e)
        }
    }),
    $(".btn-logout").click(function(t) {
        t.preventDefault();
        var e = $(this),
        n = e.data("confirm"),
        i = function() {
            dm.loading();
            var t, n = 0;
            dm.getJSON(e.attr("href")).always(function() {
                n += 100
            }),
            $.getJSON(e.data("ssoLogout")).always(function() {
                n += 100
            }),
            t = setInterval(function() {
                n >= 200 ? (clearInterval(t), "1" === serverData.requireAuth ? location.href = "/": location.reload()) : n += 3
            },
            300)
        };
        n ? dm.confirm("浣犵‘瀹氳閫€鍑虹櫥褰曞悧锛�", i) : i()
    }),
    dm.ssoAutoLogin = function(t, e) {
        null == t && (t = function() {
            window.location.reload()
        }),
        $.getJSON(api.ssoAutoLogin).then(function(i) {
            0 == i.status ? $.getJSON(api.ssoAutoLoginLocal, {
                ticket: i.ticket
            }).then(function(i) {
                0 == i.status ? t() : (clearTimeout(n), e && e())
            }) : (clearTimeout(n), e && e())
        });
        var n = setTimeout(function() {
            e && e()
        },
        15e3)
    },
    $(".link-login").click(function(t) {
        t.preventDefault();
        var e = $(this);
        dm.loading(),
        dm.ssoAutoLogin(null,
        function() {
            var t = e.attr("href");
            dm.qs.redirect_url ? window.location.href = dm.joinUrlSearch(t, {
                redirect_url: dm.qs.redirect_url
            }) : window.location.href = dm.joinUrlSearch(t, {
                redirect_url: location.href
            })
        })
    }),
    $(".link-try-login").click(function(t) {
        t.preventDefault();
        var e = $(this);
        dm.loading();
        var n = function() {
            dm.loading("hide"),
            window.location.href = e.attr("href")
        };
        dm.ssoAutoLogin(n, n)
    }),
    serverData.doSSOAutoLogin && dm.ssoAutoLogin(),
    dm.qs.login_uc && $.getJSON(api.loginUC + "?user_token=" + dm.qs.login_uc + "&callback=?")
},
function(t, e) {
    $(".btn-sms").each(function() {
        var t = $(this),
        e = t.closest("form"),
        n = e.find("#tel"),
        i = e.find('[name="verify"]');
        n.length || (n = e.find(".tel")),
        n.length || (n = e.find("[type=tel]")),
        t.click(function() {
            var e = {
                url: t.data("ajax") || api.sms || "",
                waiting: "璇风瓑寰� <var>60</var> 绉�"
            };
            $.extend(e, t.data());
            var a = function() {
                t.disable(),
                dm.getJSON(e.url, {
                    phone: n.val(),
                    verify: i.val(),
                    ischeck: t.data("checkType") || 0
                }).done(function(n) {
                    t.disable(e.waiting, !1);
                    var i = t.find("var"),
                    a = 0,
                    o = function() {
                        var e = (parseInt(i.text(), 10) || 0) - 1;
                        0 >= e ? t.enable() : (i.text(e), a = setTimeout(o, 1e3))
                    };
                    o()
                }).fail(function() {
                    t.enable()
                })
            };
            n.length ? n.validate(null, a) : a()
        })
    })
},
function(t, e) { !
    function(t) {
        var e;
        t.fn.citySelect = function(n, i, a) {
            var o, r = this;
            if (r.length) {
                if (o = function() {
                    r.each(function() {
                        var o, r, s, d, l, c, u, f, h, m, v, p, g, b, w, $, y, k;
                        if (u = t(this), s = u.find(".dropdown-content"), h = s.find(".tab-title"), f = s.find(".tab-content"), o = u.children(".btn"), c = f.children(".prov-box"), r = f.children(".city-box"), l = u.find("[name=province]"), d = u.find("[name=city]"), !c.html() || n) {
                            if (v = function(t, n) {
                                var i, a, o, s, d, l, c;
                                for (t = "" + t, l = 0, c = e.length; c > l; l++) if (o = e[l], t === "" + o.id) {
                                    i = o.cities,
                                    d = "";
                                    for (a in i) s = i[a],
                                    d += '<u data-id="' + a + '">' + s + "</u>";
                                    r.html(d),
                                    r.find("[data-id=" + n + "]").addClass("checked")
                                }
                            },
                            k = function(t, e) {
                                var n = c.children().removeClass("checked").filter("[data-id=" + (t || 0) + "]").addClass("checked").text();
                                v(t, e);
                                var i = r.children().removeClass("checked").filter("[data-id=" + (e || 0) + "]").addClass("checked").text();
                                t || e ? o.val(n + " / " + i) : o.val("璇烽€夋嫨鍩庡競"),
                                l.val(t),
                                d.val(e)
                            },
                            "update" === n) return void k(i, a);
                            if (y = function(t) {
                                h.children('[data-target="' + t + '"]').addClass("active").siblings().removeClass("active"),
                                f.children(t).show().siblings().hide()
                            },
                            "reset" === n) return o.val("璇烽€夋嫨鍩庡競"),
                            l.val(""),
                            d.val(""),
                            r.html('<b class="c-danger">璇烽€夋嫨鐪佷唤</b>'),
                            void y(".prov-box");
                            for (p = "", g = 0, b = e.length; b > g; g++) m = e[g],
                            p += '<u data-id="' + m.id + '">' + m.name + "</u>";
                            c.html(p),
                            $ = l.val() || "",
                            w = d.val() || "",
                            w && k($, w),
                            u.on("click.stopPropagation",
                            function(t) {
                                t.stopPropagation()
                            }),
                            t(document).on("click.closeDropdown",
                            function() {
                                return s.hide()
                            }),
                            o.on("click.dropdown",
                            function(t) {
                                o.closest(".disabled").length || (s.is(":visible") ? s.hide() : s.show(0,
                                function() {
                                    var t = (window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight) - f.offset().top;
                                    t = 300 > t ? 300 : t,
                                    f.height() > t && f.css("height", t)
                                }))
                            }),
                            h.on("click.switchTab", "li",
                            function() {
                                var e, n;
                                e = t(this),
                                n = e.data("target"),
                                y(n)
                            }),
                            c.on("click.selectProv", "u",
                            function() {
                                var e, n, i;
                                return e = t(this),
                                e.addClass("checked").siblings(".checked").removeClass("checked"),
                                y(".city-box"),
                                n = e.data("id"),
                                i = e.text(),
                                v(n),
                                o.val(i),
                                l.val(n)
                            }),
                            r.on("click.selectDone", "u",
                            function() {
                                var e, n;
                                return e = t(this),
                                e.addClass("checked").siblings(".checked").removeClass("checked"),
                                n = o.val().split("/").map(function(t) {
                                    return t.trim()
                                }),
                                n[1] = e.text(),
                                o.val(n.join(" / ")).validate(),
                                s.hide(),
                                d.val(e.data("id"))
                            })
                        }
                    })
                },
                e) o();
                else {
                    if (!api.cities) throw new Error("require api.cities");
                    t.getJSON(api.cities).then(function(t) {
                        e = t,
                        o()
                    })
                }
                return this
            }
        }
    } (jQuery),
    $(function() {
        $(".city-select").citySelect()
    })
},
function(t, e) { !
    function(t) {
        "use strict";
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
    } (jQuery)
},
function(t, e) {
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
    },
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
    })
}]);