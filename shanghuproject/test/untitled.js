! function(e, t, n) {
    "use strict";

    function r(e) {
        return function() {
            var t, n, r = arguments[0],
                o = "[" + (e ? e + ":" : "") + r + "] ",
                i = arguments[1],
                s = arguments,
                a = function(e) {
                    return "function" == typeof e ? e.toString().replace(/ \{[\s\S]*$/, "") : "undefined" == typeof e ? "undefined" : "string" != typeof e ? JSON.stringify(e) : e
                };
            for (t = o + i.replace(/\{\d+\}/g, function(e) {
                    var t, n = +e.slice(1, -1);
                    return n + 2 < s.length ? (t = s[n + 2], "function" == typeof t ? t.toString().replace(/ ?\{[\s\S]*$/, "") : "undefined" == typeof t ? "undefined" : "string" != typeof t ? V(t) : t) : e
                }), t = t + "\nhttp://errors.angularjs.org/1.2.9/" + (e ? e + "/" : "") + r, n = 2; n < arguments.length; n++) t = t + (2 == n ? "?" : "&") + "p" + (n - 2) + "=" + encodeURIComponent(a(arguments[n]));
            return new Error(t)
        }
    }

    function o(e) {
        if (null == e || S(e)) return !1;
        var t = e.length;
        return 1 === e.nodeType && t ? !0 : y(e) || x(e) || 0 === t || "number" == typeof t && t > 0 && t - 1 in e
    }

    function i(e, t, n) {
        var r;
        if (e)
            if (k(e))
                for (r in e) "prototype" == r || "length" == r || "name" == r || e.hasOwnProperty && !e.hasOwnProperty(r) || t.call(n, e[r], r);
            else if (e.forEach && e.forEach !== i) e.forEach(t, n);
        else if (o(e))
            for (r = 0; r < e.length; r++) t.call(n, e[r], r);
        else
            for (r in e) e.hasOwnProperty(r) && t.call(n, e[r], r);
        return e
    }

    function s(e) {
        var t = [];
        for (var n in e) e.hasOwnProperty(n) && t.push(n);
        return t.sort()
    }

    function a(e, t, n) {
        for (var r = s(e), o = 0; o < r.length; o++) t.call(n, e[r[o]], r[o]);
        return r
    }

    function c(e) {
        return function(t, n) {
            e(n, t)
        }
    }

    function u() {
        for (var e, t = yr.length; t;) {
            if (t--, e = yr[t].charCodeAt(0), 57 == e) return yr[t] = "A", yr.join("");
            if (90 != e) return yr[t] = String.fromCharCode(e + 1), yr.join("");
            yr[t] = "0"
        }
        return yr.unshift("0"), yr.join("")
    }

    function l(e, t) {
        t ? e.$$hashKey = t : delete e.$$hashKey
    }

    function p(e) {
        var t = e.$$hashKey;
        return i(arguments, function(t) {
            t !== e && i(t, function(t, n) {
                e[n] = t
            })
        }), l(e, t), e
    }

    function d(e) {
        return parseInt(e, 10)
    }

    function f(e, t) {
        return p(new(p(function() {}, {
            prototype: e
        })), t)
    }

    function $() {}

    function h(e) {
        return e
    }

    function g(e) {
        return function() {
            return e
        }
    }

    function m(e) {
        return "undefined" == typeof e
    }

    function v(e) {
        return "undefined" != typeof e
    }

    function _(e) {
        return null != e && "object" == typeof e
    }

    function y(e) {
        return "string" == typeof e
    }

    function b(e) {
        return "number" == typeof e
    }

    function w(e) {
        return "[object Date]" === mr.call(e)
    }

    function x(e) {
        return "[object Array]" === mr.call(e)
    }

    function k(e) {
        return "function" == typeof e
    }

    function C(e) {
        return "[object RegExp]" === mr.call(e)
    }

    function S(e) {
        return e && e.document && e.location && e.alert && e.setInterval
    }

    function T(e) {
        return e && e.$evalAsync && e.$watch
    }

    function A(e) {
        return "[object File]" === mr.call(e)
    }

    function E(e) {
        return !(!e || !(e.nodeName || e.on && e.find))
    }

    function D(e, t, n) {
        var r = [];
        return i(e, function(e, o, i) {
            r.push(t.call(n, e, o, i))
        }), r
    }

    function j(e, t) {
        return -1 != O(e, t)
    }

    function O(e, t) {
        if (e.indexOf) return e.indexOf(t);
        for (var n = 0; n < e.length; n++)
            if (t === e[n]) return n;
        return -1
    }

    function I(e, t) {
        var n = O(e, t);
        return n >= 0 && e.splice(n, 1), t
    }

    function P(e, t) {
        if (S(e) || T(e)) throw vr("cpws", "Can't copy! Making copies of Window or Scope instances is not supported.");
        if (t) {
            if (e === t) throw vr("cpi", "Can't copy! Source and destination are identical.");
            if (x(e)) {
                t.length = 0;
                for (var n = 0; n < e.length; n++) t.push(P(e[n]))
            } else {
                var r = t.$$hashKey;
                i(t, function(e, n) {
                    delete t[n]
                });
                for (var o in e) t[o] = P(e[o]);
                l(t, r)
            }
        } else t = e, e && (x(e) ? t = P(e, []) : w(e) ? t = new Date(e.getTime()) : C(e) ? t = new RegExp(e.source) : _(e) && (t = P(e, {})));
        return t
    }

    function M(e, t) {
        t = t || {};
        for (var n in e) e.hasOwnProperty(n) && "$" !== n.charAt(0) && "$" !== n.charAt(1) && (t[n] = e[n]);
        return t
    }

    function q(e, t) {
        if (e === t) return !0;
        if (null === e || null === t) return !1;
        if (e !== e && t !== t) return !0;
        var r, o, i, s = typeof e,
            a = typeof t;
        if (s == a && "object" == s) {
            if (!x(e)) {
                if (w(e)) return w(t) && e.getTime() == t.getTime();
                if (C(e) && C(t)) return e.toString() == t.toString();
                if (T(e) || T(t) || S(e) || S(t) || x(t)) return !1;
                i = {};
                for (o in e)
                    if ("$" !== o.charAt(0) && !k(e[o])) {
                        if (!q(e[o], t[o])) return !1;
                        i[o] = !0
                    }
                for (o in t)
                    if (!i.hasOwnProperty(o) && "$" !== o.charAt(0) && t[o] !== n && !k(t[o])) return !1;
                return !0
            }
            if (!x(t)) return !1;
            if ((r = e.length) == t.length) {
                for (o = 0; r > o; o++)
                    if (!q(e[o], t[o])) return !1;
                return !0
            }
        }
        return !1
    }

    function N() {
        return t.securityPolicy && t.securityPolicy.isActive || t.querySelector && !(!t.querySelector("[ng-csp]") && !t.querySelector("[data-ng-csp]"))
    }

    function R(e, t, n) {
        return e.concat(hr.call(t, n))
    }

    function U(e, t) {
        return hr.call(e, t || 0)
    }

    function H(e, t) {
        var n = arguments.length > 2 ? U(arguments, 2) : [];
        return !k(t) || t instanceof RegExp ? t : n.length ? function() {
            return arguments.length ? t.apply(e, n.concat(hr.call(arguments, 0))) : t.apply(e, n)
        } : function() {
            return arguments.length ? t.apply(e, arguments) : t.call(e)
        }
    }

    function F(e, r) {
        var o = r;
        return "string" == typeof e && "$" === e.charAt(0) ? o = n : S(r) ? o = "$WINDOW" : r && t === r ? o = "$DOCUMENT" : T(r) && (o = "$SCOPE"), o
    }

    function V(e, t) {
        return "undefined" == typeof e ? n : JSON.stringify(e, F, t ? "  " : null)
    }

    function z(e) {
        return y(e) ? JSON.parse(e) : e
    }

    function L(e) {
        if ("function" == typeof e) e = !0;
        else if (e && 0 !== e.length) {
            var t = sr("" + e);
            e = !("f" == t || "0" == t || "false" == t || "no" == t || "n" == t || "[]" == t)
        } else e = !1;
        return e
    }

    function B(e) {
        e = pr(e).clone();
        try {
            e.empty()
        } catch (t) {}
        var n = 3,
            r = pr("<div>").append(e).html();
        try {
            return e[0].nodeType === n ? sr(r) : r.match(/^(<[^>]+>)/)[1].replace(/^<([\w\-]+)/, function(e, t) {
                return "<" + sr(t)
            })
        } catch (t) {
            return sr(r)
        }
    }

    function W(e) {
        try {
            return decodeURIComponent(e)
        } catch (t) {}
    }

    function Y(e) {
        var t, n, r = {};
        return i((e || "").split("&"), function(e) {
            if (e && (t = e.split("="), n = W(t[0]), v(n))) {
                var o = v(t[1]) ? W(t[1]) : !0;
                r[n] ? x(r[n]) ? r[n].push(o) : r[n] = [r[n], o] : r[n] = o
            }
        }), r
    }

    function G(e) {
        var t = [];
        return i(e, function(e, n) {
            x(e) ? i(e, function(e) {
                t.push(J(n, !0) + (e === !0 ? "" : "=" + J(e, !0)))
            }) : t.push(J(n, !0) + (e === !0 ? "" : "=" + J(e, !0)))
        }), t.length ? t.join("&") : ""
    }

    function Z(e) {
        return J(e, !0).replace(/%26/gi, "&").replace(/%3D/gi, "=").replace(/%2B/gi, "+")
    }

    function J(e, t) {
        return encodeURIComponent(e).replace(/%40/gi, "@").replace(/%3A/gi, ":").replace(/%24/g, "$").replace(/%2C/gi, ",").replace(/%20/g, t ? "%20" : "+")
    }

    function Q(e, n) {
        function r(e) {
            e && a.push(e)
        }
        var o, s, a = [e],
            c = ["ng:app", "ng-app", "x-ng-app", "data-ng-app"],
            u = /\sng[:\-]app(:\s*([\w\d_]+);?)?\s/;
        i(c, function(n) {
            c[n] = !0, r(t.getElementById(n)), n = n.replace(":", "\\:"), e.querySelectorAll && (i(e.querySelectorAll("." + n), r), i(e.querySelectorAll("." + n + "\\:"), r), i(e.querySelectorAll("[" + n + "]"), r))
        }), i(a, function(e) {
            if (!o) {
                var t = " " + e.className + " ",
                    n = u.exec(t);
                n ? (o = e, s = (n[2] || "").replace(/\s+/g, ",")) : i(e.attributes, function(t) {
                    !o && c[t.name] && (o = e, s = t.value)
                })
            }
        }), o && n(o, s ? [s] : [])
    }

    function K(n, r) {
        var o = function() {
                if (n = pr(n), n.injector()) {
                    var e = n[0] === t ? "document" : B(n);
                    throw vr("btstrpd", "App Already Bootstrapped with this Element '{0}'", e)
                }
                r = r || [], r.unshift(["$provide", function(e) {
                    e.value("$rootElement", n)
                }]), r.unshift("ng");
                var o = Dt(r);
                return o.invoke(["$rootScope", "$rootElement", "$compile", "$injector", "$animate", function(e, t, n, r) {
                    e.$apply(function() {
                        t.data("$injector", r), n(t)(e)
                    })
                }]), o
            },
            s = /^NG_DEFER_BOOTSTRAP!/;
        return e && !s.test(e.name) ? o() : (e.name = e.name.replace(s, ""), _r.resumeBootstrap = function(e) {
            i(e, function(e) {
                r.push(e)
            }), o()
        }, void 0)
    }

    function X(e, t) {
        return t = t || "_", e.replace(wr, function(e, n) {
            return (n ? t : "") + e.toLowerCase()
        })
    }

    function et() {
        dr = e.jQuery, dr ? (pr = dr, p(dr.fn, {
            scope: Or.scope,
            isolateScope: Or.isolateScope,
            controller: Or.controller,
            injector: Or.injector,
            inheritedData: Or.inheritedData
        }), lt("remove", !0, !0, !1), lt("empty", !1, !1, !1), lt("html", !1, !1, !0)) : pr = pt, _r.element = pr
    }

    function tt(e, t, n) {
        if (!e) throw vr("areq", "Argument '{0}' is {1}", t || "?", n || "required");
        return e
    }

    function nt(e, t, n) {
        return n && x(e) && (e = e[e.length - 1]), tt(k(e), t, "not a function, got " + (e && "object" == typeof e ? e.constructor.name || "Object" : typeof e)), e
    }

    function rt(e, t) {
        if ("hasOwnProperty" === e) throw vr("badname", "hasOwnProperty is not a valid {0} name", t)
    }

    function ot(e, t, n) {
        if (!t) return e;
        for (var r, o = t.split("."), i = e, s = o.length, a = 0; s > a; a++) r = o[a], e && (e = (i = e)[r]);
        return !n && k(e) ? H(i, e) : e
    }

    function it(e) {
        var t = e[0],
            n = e[e.length - 1];
        if (t === n) return pr(t);
        var r = t,
            o = [r];
        do {
            if (r = r.nextSibling, !r) break;
            o.push(r)
        } while (r !== n);
        return pr(o)
    }

    function st(e) {
        function t(e, t, n) {
            return e[t] || (e[t] = n())
        }
        var n = r("$injector"),
            o = r("ng"),
            i = t(e, "angular", Object);
        return i.$$minErr = i.$$minErr || r, t(i, "module", function() {
            var e = {};
            return function(r, i, s) {
                var a = function(e, t) {
                    if ("hasOwnProperty" === e) throw o("badname", "hasOwnProperty is not a valid {0} name", t)
                };
                return a(r, "module"), i && e.hasOwnProperty(r) && (e[r] = null), t(e, r, function() {
                    function e(e, n, r) {
                        return function() {
                            return t[r || "push"]([e, n, arguments]), c
                        }
                    }
                    if (!i) throw n("nomod", "Module '{0}' is not available! You either misspelled the module name or forgot to load it. If registering a module ensure that you specify the dependencies as the second argument.", r);
                    var t = [],
                        o = [],
                        a = e("$injector", "invoke"),
                        c = {
                            _invokeQueue: t,
                            _runBlocks: o,
                            requires: i,
                            name: r,
                            provider: e("$provide", "provider"),
                            factory: e("$provide", "factory"),
                            service: e("$provide", "service"),
                            value: e("$provide", "value"),
                            constant: e("$provide", "constant", "unshift"),
                            animation: e("$animateProvider", "register"),
                            filter: e("$filterProvider", "register"),
                            controller: e("$controllerProvider", "register"),
                            directive: e("$compileProvider", "directive"),
                            config: a,
                            run: function(e) {
                                return o.push(e), this
                            }
                        };
                    return s && a(s), c
                })
            }
        })
    }

    function at(t) {
        p(t, {
            bootstrap: K,
            copy: P,
            extend: p,
            equals: q,
            element: pr,
            forEach: i,
            injector: Dt,
            noop: $,
            bind: H,
            toJson: V,
            fromJson: z,
            identity: h,
            isUndefined: m,
            isDefined: v,
            isString: y,
            isFunction: k,
            isObject: _,
            isNumber: b,
            isElement: E,
            isArray: x,
            version: xr,
            isDate: w,
            lowercase: sr,
            uppercase: ar,
            callbacks: {
                counter: 0
            },
            $$minErr: r,
            $$csp: N
        }), fr = st(e);
        try {
            fr("ngLocale")
        } catch (n) {
            fr("ngLocale", []).provider("$locale", Kt)
        }
        fr("ng", ["ngLocale"], ["$provide", function(e) {
            e.provider({
                $$sanitizeUri: Cn
            }), e.provider("$compile", qt).directive({
                a: fo,
                input: xo,
                textarea: xo,
                form: mo,
                script: oi,
                select: ai,
                style: ui,
                option: ci,
                ngBind: Mo,
                ngBindHtml: No,
                ngBindTemplate: qo,
                ngClass: Ro,
                ngClassEven: Ho,
                ngClassOdd: Uo,
                ngCloak: Fo,
                ngController: Vo,
                ngForm: vo,
                ngHide: Ko,
                ngIf: Lo,
                ngInclude: Bo,
                ngInit: Yo,
                ngNonBindable: Go,
                ngPluralize: Zo,
                ngRepeat: Jo,
                ngShow: Qo,
                ngStyle: Xo,
                ngSwitch: ei,
                ngSwitchWhen: ti,
                ngSwitchDefault: ni,
                ngOptions: si,
                ngTransclude: ri,
                ngModel: Eo,
                ngList: Oo,
                ngChange: Do,
                required: jo,
                ngRequired: jo,
                ngValue: Po
            }).directive({
                ngInclude: Wo
            }).directive($o).directive(zo), e.provider({
                $anchorScroll: jt,
                $animate: Fr,
                $browser: It,
                $cacheFactory: Pt,
                $controller: Ut,
                $document: Ht,
                $exceptionHandler: Ft,
                $filter: qn,
                $interpolate: Jt,
                $interval: Qt,
                $http: Wt,
                $httpBackend: Gt,
                $location: dn,
                $log: fn,
                $parse: bn,
                $rootScope: kn,
                $q: wn,
                $sce: Dn,
                $sceDelegate: En,
                $sniffer: jn,
                $templateCache: Mt,
                $timeout: On,
                $window: Mn
            })
        }])
    }

    function ct() {
        return ++Sr
    }

    function ut(e) {
        return e.replace(Er, function(e, t, n, r) {
            return r ? n.toUpperCase() : n
        }).replace(Dr, "Moz$1")
    }

    function lt(e, t, n, r) {
        function o(e) {
            var o, s, a, c, u, l, p, d = n && e ? [this.filter(e)] : [this],
                f = t;
            if (!r || null != e)
                for (; d.length;)
                    for (o = d.shift(), s = 0, a = o.length; a > s; s++)
                        for (c = pr(o[s]), f ? c.triggerHandler("$destroy") : f = !f, u = 0, l = (p = c.children()).length; l > u; u++) d.push(dr(p[u]));
            return i.apply(this, arguments)
        }
        var i = dr.fn[e];
        i = i.$original || i, o.$original = i, dr.fn[e] = o
    }

    function pt(e) {
        if (e instanceof pt) return e;
        if (!(this instanceof pt)) {
            if (y(e) && "<" != e.charAt(0)) throw jr("nosel", "Looking up elements via selectors is not supported by jqLite! See: http://docs.angularjs.org/api/angular.element");
            return new pt(e)
        }
        if (y(e)) {
            var n = t.createElement("div");
            n.innerHTML = "<div>&#160;</div>" + e, n.removeChild(n.firstChild), bt(this, n.childNodes);
            var r = pr(t.createDocumentFragment());
            r.append(this)
        } else bt(this, e)
    }

    function dt(e) {
        return e.cloneNode(!0)
    }

    function ft(e) {
        ht(e);
        for (var t = 0, n = e.childNodes || []; t < n.length; t++) ft(n[t])
    }

    function $t(e, t, n, r) {
        if (v(r)) throw jr("offargs", "jqLite#off() does not support the `selector` argument");
        var o = gt(e, "events"),
            s = gt(e, "handle");
        s && (m(t) ? i(o, function(t, n) {
            Ar(e, n, t), delete o[n]
        }) : i(t.split(" "), function(t) {
            m(n) ? (Ar(e, t, o[t]), delete o[t]) : I(o[t] || [], n)
        }))
    }

    function ht(e, t) {
        var r = e[Cr],
            o = kr[r];
        if (o) {
            if (t) return delete kr[r].data[t], void 0;
            o.handle && (o.events.$destroy && o.handle({}, "$destroy"), $t(e)), delete kr[r], e[Cr] = n
        }
    }

    function gt(e, t, n) {
        var r = e[Cr],
            o = kr[r || -1];
        return v(n) ? (o || (e[Cr] = r = ct(), o = kr[r] = {}), o[t] = n, void 0) : o && o[t]
    }

    function mt(e, t, n) {
        var r = gt(e, "data"),
            o = v(n),
            i = !o && v(t),
            s = i && !_(t);
        if (r || s || gt(e, "data", r = {}), o) r[t] = n;
        else {
            if (!i) return r;
            if (s) return r && r[t];
            p(r, t)
        }
    }

    function vt(e, t) {
        return e.getAttribute ? (" " + (e.getAttribute("class") || "") + " ").replace(/[\n\t]/g, " ").indexOf(" " + t + " ") > -1 : !1
    }

    function _t(e, t) {
        t && e.setAttribute && i(t.split(" "), function(t) {
            e.setAttribute("class", br((" " + (e.getAttribute("class") || "") + " ").replace(/[\n\t]/g, " ").replace(" " + br(t) + " ", " ")))
        })
    }

    function yt(e, t) {
        if (t && e.setAttribute) {
            var n = (" " + (e.getAttribute("class") || "") + " ").replace(/[\n\t]/g, " ");
            i(t.split(" "), function(e) {
                e = br(e), -1 === n.indexOf(" " + e + " ") && (n += e + " ")
            }), e.setAttribute("class", br(n))
        }
    }

    function bt(e, t) {
        if (t) {
            t = t.nodeName || !v(t.length) || S(t) ? [t] : t;
            for (var n = 0; n < t.length; n++) e.push(t[n])
        }
    }

    function wt(e, t) {
        return xt(e, "$" + (t || "ngController") + "Controller")
    }

    function xt(e, t, r) {
        e = pr(e), 9 == e[0].nodeType && (e = e.find("html"));
        for (var o = x(t) ? t : [t]; e.length;) {
            for (var i = 0, s = o.length; s > i; i++)
                if ((r = e.data(o[i])) !== n) return r;
            e = e.parent()
        }
    }

    function kt(e) {
        for (var t = 0, n = e.childNodes; t < n.length; t++) ft(n[t]);
        for (; e.firstChild;) e.removeChild(e.firstChild)
    }

    function Ct(e, t) {
        var n = Ir[t.toLowerCase()];
        return n && Pr[e.nodeName] && n
    }

    function St(e, n) {
        var r = function(r, o) {
            if (r.preventDefault || (r.preventDefault = function() {
                    r.returnValue = !1
                }), r.stopPropagation || (r.stopPropagation = function() {
                    r.cancelBubble = !0
                }), r.target || (r.target = r.srcElement || t), m(r.defaultPrevented)) {
                var s = r.preventDefault;
                r.preventDefault = function() {
                    r.defaultPrevented = !0, s.call(r)
                }, r.defaultPrevented = !1
            }
            r.isDefaultPrevented = function() {
                return r.defaultPrevented || r.returnValue === !1
            };
            var a = M(n[o || r.type] || []);
            i(a, function(t) {
                t.call(e, r)
            }), 8 >= lr ? (r.preventDefault = null, r.stopPropagation = null, r.isDefaultPrevented = null) : (delete r.preventDefault, delete r.stopPropagation, delete r.isDefaultPrevented)
        };
        return r.elem = e, r
    }

    function Tt(e) {
        var t, r = typeof e;
        return "object" == r && null !== e ? "function" == typeof(t = e.$$hashKey) ? t = e.$$hashKey() : t === n && (t = e.$$hashKey = u()) : t = e, r + ":" + t
    }

    function At(e) {
        i(e, this.put, this)
    }

    function Et(e) {
        var t, n, r, o;
        return "function" == typeof e ? (t = e.$inject) || (t = [], e.length && (n = e.toString().replace(Rr, ""), r = n.match(Mr), i(r[1].split(qr), function(e) {
            e.replace(Nr, function(e, n, r) {
                t.push(r)
            })
        })), e.$inject = t) : x(e) ? (o = e.length - 1, nt(e[o], "fn"), t = e.slice(0, o)) : nt(e, "fn", !0), t
    }

    function Dt(e) {
        function t(e) {
            return function(t, n) {
                return _(t) ? (i(t, c(e)), void 0) : e(t, n)
            }
        }

        function n(e, t) {
            if (rt(e, "service"), (k(t) || x(t)) && (t = b.instantiate(t)), !t.$get) throw Ur("pget", "Provider '{0}' must define $get factory method.", e);
            return v[e + f] = t
        }

        function r(e, t) {
            return n(e, {
                $get: t
            })
        }

        function o(e, t) {
            return r(e, ["$injector", function(e) {
                return e.instantiate(t)
            }])
        }

        function s(e, t) {
            return r(e, g(t))
        }

        function a(e, t) {
            rt(e, "constant"), v[e] = t, w[e] = t
        }

        function u(e, t) {
            var n = b.get(e + f),
                r = n.$get;
            n.$get = function() {
                var e = C.invoke(r, n);
                return C.invoke(t, null, {
                    $delegate: e
                })
            }
        }

        function l(e) {
            var t, n, r, o, s = [];
            return i(e, function(e) {
                if (!m.get(e)) {
                    m.put(e, !0);
                    try {
                        if (y(e))
                            for (t = fr(e), s = s.concat(l(t.requires)).concat(t._runBlocks), n = t._invokeQueue, r = 0, o = n.length; o > r; r++) {
                                var i = n[r],
                                    a = b.get(i[0]);
                                a[i[1]].apply(a, i[2])
                            } else k(e) ? s.push(b.invoke(e)) : x(e) ? s.push(b.invoke(e)) : nt(e, "module")
                    } catch (c) {
                        throw x(e) && (e = e[e.length - 1]), c.message && c.stack && -1 == c.stack.indexOf(c.message) && (c = c.message + "\n" + c.stack), Ur("modulerr", "Failed to instantiate module {0} due to:\n{1}", e, c.stack || c.message || c)
                    }
                }
            }), s
        }

        function p(e, t) {
            function n(n) {
                if (e.hasOwnProperty(n)) {
                    if (e[n] === d) throw Ur("cdep", "Circular dependency found: {0}", h.join(" <- "));
                    return e[n]
                }
                try {
                    return h.unshift(n), e[n] = d, e[n] = t(n)
                } catch (r) {
                    throw e[n] === d && delete e[n], r
                } finally {
                    h.shift()
                }
            }

            function r(e, t, r) {
                var o, i, s, a = [],
                    c = Et(e);
                for (i = 0, o = c.length; o > i; i++) {
                    if (s = c[i], "string" != typeof s) throw Ur("itkn", "Incorrect injection token! Expected service name as string, got {0}", s);
                    a.push(r && r.hasOwnProperty(s) ? r[s] : n(s))
                }
                return e.$inject || (e = e[o]), e.apply(t, a)
            }

            function o(e, t) {
                var n, o, i = function() {};
                return i.prototype = (x(e) ? e[e.length - 1] : e).prototype, n = new i, o = r(e, n, t), _(o) || k(o) ? o : n
            }
            return {
                invoke: r,
                instantiate: o,
                get: n,
                annotate: Et,
                has: function(t) {
                    return v.hasOwnProperty(t + f) || e.hasOwnProperty(t)
                }
            }
        }
        var d = {},
            f = "Provider",
            h = [],
            m = new At,
            v = {
                $provide: {
                    provider: t(n),
                    factory: t(r),
                    service: t(o),
                    value: t(s),
                    constant: t(a),
                    decorator: u
                }
            },
            b = v.$injector = p(v, function() {
                throw Ur("unpr", "Unknown provider: {0}", h.join(" <- "))
            }),
            w = {},
            C = w.$injector = p(w, function(e) {
                var t = b.get(e + f);
                return C.invoke(t.$get, t)
            });
        return i(l(e), function(e) {
            C.invoke(e || $)
        }), C
    }

    function jt() {
        var e = !0;
        this.disableAutoScrolling = function() {
            e = !1
        }, this.$get = ["$window", "$location", "$rootScope", function(t, n, r) {
            function o(e) {
                var t = null;
                return i(e, function(e) {
                    t || "a" !== sr(e.nodeName) || (t = e)
                }), t
            }

            function s() {
                var e, r = n.hash();
                r ? (e = a.getElementById(r)) ? e.scrollIntoView() : (e = o(a.getElementsByName(r))) ? e.scrollIntoView() : "top" === r && t.scrollTo(0, 0) : t.scrollTo(0, 0)
            }
            var a = t.document;
            return e && r.$watch(function() {
                return n.hash()
            }, function() {
                r.$evalAsync(s)
            }), s
        }]
    }

    function Ot(e, t, r, o) {
        function s(e) {
            try {
                e.apply(null, U(arguments, 1))
            } finally {
                if (v--, 0 === v)
                    for (; _.length;) try {
                        _.pop()()
                    } catch (t) {
                        r.error(t)
                    }
            }
        }

        function a(e, t) {
            ! function n() {
                i(w, function(e) {
                    e()
                }), b = t(n, e)
            }()
        }

        function c() {
            C = null, x != u.url() && (x = u.url(), i(S, function(e) {
                e(u.url())
            }))
        }
        var u = this,
            l = t[0],
            p = e.location,
            d = e.history,
            f = e.setTimeout,
            h = e.clearTimeout,
            g = {};
        u.isMock = !1;
        var v = 0,
            _ = [];
        u.$$completeOutstandingRequest = s, u.$$incOutstandingRequestCount = function() {
            v++
        }, u.notifyWhenNoOutstandingRequests = function(e) {
            i(w, function(e) {
                e()
            }), 0 === v ? e() : _.push(e)
        };
        var b, w = [];
        u.addPollFn = function(e) {
            return m(b) && a(100, f), w.push(e), e
        };
        var x = p.href,
            k = t.find("base"),
            C = null;
        u.url = function(t, n) {
            if (p !== e.location && (p = e.location), d !== e.history && (d = e.history), t) {
                if (x == t) return;
                return x = t, o.history ? n ? d.replaceState(null, "", t) : (d.pushState(null, "", t), k.attr("href", k.attr("href"))) : (C = t, n ? p.replace(t) : p.href = t), u
            }
            return C || p.href.replace(/%27/g, "'")
        };
        var S = [],
            T = !1;
        u.onUrlChange = function(t) {
            return T || (o.history && pr(e).on("popstate", c), o.hashchange ? pr(e).on("hashchange", c) : u.addPollFn(c), T = !0), S.push(t), t
        }, u.baseHref = function() {
            var e = k.attr("href");
            return e ? e.replace(/^(https?\:)?\/\/[^\/]*/, "") : ""
        };
        var A = {},
            E = "",
            D = u.baseHref();
        u.cookies = function(e, t) {
            var o, i, s, a, c;
            if (!e) {
                if (l.cookie !== E)
                    for (E = l.cookie, i = E.split("; "), A = {}, a = 0; a < i.length; a++) s = i[a], c = s.indexOf("="), c > 0 && (e = unescape(s.substring(0, c)), A[e] === n && (A[e] = unescape(s.substring(c + 1))));
                return A
            }
            t === n ? l.cookie = escape(e) + "=;path=" + D + ";expires=Thu, 01 Jan 1970 00:00:00 GMT" : y(t) && (o = (l.cookie = escape(e) + "=" + escape(t) + ";path=" + D).length + 1, o > 4096 && r.warn("Cookie '" + e + "' possibly not set or overflowed because it was too large (" + o + " > 4096 bytes)!"))
        }, u.defer = function(e, t) {
            var n;
            return v++, n = f(function() {
                delete g[n], s(e)
            }, t || 0), g[n] = !0, n
        }, u.defer.cancel = function(e) {
            return g[e] ? (delete g[e], h(e), s($), !0) : !1
        }
    }

    function It() {
        this.$get = ["$window", "$log", "$sniffer", "$document", function(e, t, n, r) {
            return new Ot(e, r, t, n)
        }]
    }

    function Pt() {
        this.$get = function() {
            function e(e, n) {
                function o(e) {
                    e != d && (f ? f == e && (f = e.n) : f = e, i(e.n, e.p), i(e, d), d = e, d.n = null)
                }

                function i(e, t) {
                    e != t && (e && (e.p = t), t && (t.n = e))
                }
                if (e in t) throw r("$cacheFactory")("iid", "CacheId '{0}' is already taken!", e);
                var s = 0,
                    a = p({}, n, {
                        id: e
                    }),
                    c = {},
                    u = n && n.capacity || Number.MAX_VALUE,
                    l = {},
                    d = null,
                    f = null;
                return t[e] = {
                    put: function(e, t) {
                        var n = l[e] || (l[e] = {
                            key: e
                        });
                        return o(n), m(t) ? void 0 : (e in c || s++, c[e] = t, s > u && this.remove(f.key), t)
                    },
                    get: function(e) {
                        var t = l[e];
                        if (t) return o(t), c[e]
                    },
                    remove: function(e) {
                        var t = l[e];
                        t && (t == d && (d = t.p), t == f && (f = t.n), i(t.n, t.p), delete l[e], delete c[e], s--)
                    },
                    removeAll: function() {
                        c = {}, s = 0, l = {}, d = f = null
                    },
                    destroy: function() {
                        c = null, a = null, l = null, delete t[e]
                    },
                    info: function() {
                        return p({}, a, {
                            size: s
                        })
                    }
                }
            }
            var t = {};
            return e.info = function() {
                var e = {};
                return i(t, function(t, n) {
                    e[n] = t.info()
                }), e
            }, e.get = function(e) {
                return t[e]
            }, e
        }
    }

    function Mt() {
        this.$get = ["$cacheFactory", function(e) {
            return e("templates")
        }]
    }

    function qt(e, r) {
        var o = {},
            s = "Directive",
            a = /^\s*directive\:\s*([\d\w\-_]+)\s+(.*)$/,
            u = /(([\d\w\-_]+)(?:\:([^;]+))?;?)/,
            l = /^(on[a-z]+|formaction)$/;
        this.directive = function d(t, n) {
            return rt(t, "directive"), y(t) ? (tt(n, "directiveFactory"), o.hasOwnProperty(t) || (o[t] = [], e.factory(t + s, ["$injector", "$exceptionHandler", function(e, n) {
                var r = [];
                return i(o[t], function(o, i) {
                    try {
                        var s = e.invoke(o);
                        k(s) ? s = {
                            compile: g(s)
                        } : !s.compile && s.link && (s.compile = g(s.link)), s.priority = s.priority || 0, s.index = i, s.name = s.name || t, s.require = s.require || s.controller && s.name, s.restrict = s.restrict || "A", r.push(s)
                    } catch (a) {
                        n(a)
                    }
                }), r
            }])), o[t].push(n)) : i(t, c(d)), this
        }, this.aHrefSanitizationWhitelist = function(e) {
            return v(e) ? (r.aHrefSanitizationWhitelist(e), this) : r.aHrefSanitizationWhitelist()
        }, this.imgSrcSanitizationWhitelist = function(e) {
            return v(e) ? (r.imgSrcSanitizationWhitelist(e), this) : r.imgSrcSanitizationWhitelist()
        }, this.$get = ["$injector", "$interpolate", "$exceptionHandler", "$http", "$templateCache", "$parse", "$controller", "$rootScope", "$document", "$sce", "$animate", "$$sanitizeUri", function(e, r, c, d, $, m, v, b, w, C, S, T) {
            function A(e, t, n, r, o) {
                e instanceof pr || (e = pr(e)), i(e, function(t, n) {
                    3 == t.nodeType && t.nodeValue.match(/\S+/) && (e[n] = t = pr(t).wrap("<span></span>").parent()[0])
                });
                var s = D(e, t, e, n, r, o);
                return E(e, "ng-scope"),
                    function(t, n, r) {
                        tt(t, "scope");
                        var o = n ? Or.clone.call(e) : e;
                        i(r, function(e, t) {
                            o.data("$" + t + "Controller", e)
                        });
                        for (var a = 0, c = o.length; c > a; a++) {
                            var u = o[a],
                                l = u.nodeType;
                            (1 === l || 9 === l) && o.eq(a).data("$scope", t)
                        }
                        return n && n(o, t), s && s(t, o, o), o
                    }
            }

            function E(e, t) {
                try {
                    e.addClass(t)
                } catch (n) {}
            }

            function D(e, t, r, o, i, s) {
                function a(e, r, o, i) {
                    var s, a, c, u, l, p, d, f, h, g = r.length,
                        m = new Array(g);
                    for (d = 0; g > d; d++) m[d] = r[d];
                    for (d = 0, h = 0, f = $.length; f > d; h++) c = m[h], s = $[d++], a = $[d++], u = pr(c), s ? (s.scope ? (l = e.$new(), u.data("$scope", l)) : l = e, p = s.transclude, p || !i && t ? s(a, l, c, o, j(e, p || t)) : s(a, l, c, o, i)) : a && a(e, c.childNodes, n, i)
                }
                for (var c, u, l, p, d, f, $ = [], h = 0; h < e.length; h++) c = new K, u = O(e[h], [], c, 0 === h ? o : n, i), l = u.length ? N(u, e[h], c, t, r, null, [], [], s) : null, l && l.scope && E(pr(e[h]), "ng-scope"), d = l && l.terminal || !(p = e[h].childNodes) || !p.length ? null : D(p, l ? l.transclude : t), $.push(l, d), f = f || l || d, s = null;
                return f ? a : null
            }

            function j(e, t) {
                return function(n, r, o) {
                    var i = !1;
                    n || (n = e.$new(), n.$$transcluded = !0, i = !0);
                    var s = t(n, r, o);
                    return i && s.on("$destroy", H(n, n.$destroy)), s
                }
            }

            function O(e, t, n, r, o) {
                var i, s, c = e.nodeType,
                    l = n.$attr;
                switch (c) {
                    case 1:
                        F(t, Nt($r(e).toLowerCase()), "E", r, o);
                        for (var p, d, f, $, h, g = e.attributes, m = 0, v = g && g.length; v > m; m++) {
                            var _ = !1,
                                b = !1;
                            if (p = g[m], !lr || lr >= 8 || p.specified) {
                                d = p.name, $ = Nt(d), ot.test($) && (d = X($.substr(6), "-"));
                                var w = $.replace(/(Start|End)$/, "");
                                $ === w + "Start" && (_ = d, b = d.substr(0, d.length - 5) + "end", d = d.substr(0, d.length - 6)), f = Nt(d.toLowerCase()), l[f] = d, n[f] = h = br(p.value), Ct(e, f) && (n[f] = !0), Z(e, t, h, f), F(t, f, "A", r, o, _, b)
                            }
                        }
                        if (s = e.className, y(s) && "" !== s)
                            for (; i = u.exec(s);) f = Nt(i[2]), F(t, f, "C", r, o) && (n[f] = br(i[3])), s = s.substr(i.index + i[0].length);
                        break;
                    case 3:
                        Y(t, e.nodeValue);
                        break;
                    case 8:
                        try {
                            i = a.exec(e.nodeValue), i && (f = Nt(i[1]), F(t, f, "M", r, o) && (n[f] = br(i[2])))
                        } catch (x) {}
                }
                return t.sort(L), t
            }

            function I(e, t, n) {
                var r = [],
                    o = 0;
                if (t && e.hasAttribute && e.hasAttribute(t)) {
                    do {
                        if (!e) throw Vr("uterdir", "Unterminated attribute, found '{0}' but no matching '{1}' found.", t, n);
                        1 == e.nodeType && (e.hasAttribute(t) && o++, e.hasAttribute(n) && o--), r.push(e), e = e.nextSibling
                    } while (o > 0)
                } else r.push(e);
                return pr(r)
            }

            function P(e, t, n) {
                return function(r, o, i, s, a) {
                    return o = I(o[0], t, n), e(r, o, i, s, a)
                }
            }

            function N(e, o, s, a, u, l, p, d, f) {
                function $(e, t, n, r) {
                    e && (n && (e = P(e, n, r)), e.require = w.require, (H === w || w.$$isolateScope) && (e = Q(e, {
                        isolateScope: !0
                    })), p.push(e)), t && (n && (t = P(t, n, r)), t.require = w.require, (H === w || w.$$isolateScope) && (t = Q(t, {
                        isolateScope: !0
                    })), d.push(t))
                }

                function h(e, t, n) {
                    var r, o = "data",
                        s = !1;
                    if (y(e)) {
                        for (;
                            "^" == (r = e.charAt(0)) || "?" == r;) e = e.substr(1), "^" == r && (o = "inheritedData"), s = s || "?" == r;
                        if (r = null, n && "data" === o && (r = n[e]), r = r || t[o]("$" + e + "Controller"), !r && !s) throw Vr("ctreq", "Controller '{0}', required by directive '{1}', can't be found!", e, C);
                        return r
                    }
                    return x(e) && (r = [], i(e, function(e) {
                        r.push(h(e, t, n))
                    })), r
                }

                function g(e, t, a, u, l) {
                    function f(e, t) {
                        var r;
                        return arguments.length < 2 && (t = e, e = n), G && (r = C), l(e, t, r)
                    }
                    var $, g, _, y, b, w, x, k, C = {};
                    if ($ = o === a ? s : M(s, new K(pr(a), s.$attr)), g = $.$$element, H) {
                        var S = /^\s*([@=&])(\??)\s*(\w*)\s*$/,
                            T = pr(a);
                        x = t.$new(!0), F && F === H.$$originalDirective ? T.data("$isolateScope", x) : T.data("$isolateScopeNoTemplate", x), E(T, "ng-isolate-scope"), i(H.scope, function(e, n) {
                            var o, i, s, a, c = e.match(S) || [],
                                u = c[3] || n,
                                l = "?" == c[2],
                                p = c[1];
                            switch (x.$$isolateBindings[n] = p + u, p) {
                                case "@":
                                    $.$observe(u, function(e) {
                                        x[n] = e
                                    }), $.$$observers[u].$$scope = t, $[u] && (x[n] = r($[u])(t));
                                    break;
                                case "=":
                                    if (l && !$[u]) return;
                                    i = m($[u]), a = i.literal ? q : function(e, t) {
                                        return e === t
                                    }, s = i.assign || function() {
                                        throw o = x[n] = i(t), Vr("nonassign", "Expression '{0}' used with directive '{1}' is non-assignable!", $[u], H.name)
                                    }, o = x[n] = i(t), x.$watch(function() {
                                        var e = i(t);
                                        return a(e, x[n]) || (a(e, o) ? s(t, e = x[n]) : x[n] = e), o = e
                                    }, null, i.literal);
                                    break;
                                case "&":
                                    i = m($[u]), x[n] = function(e) {
                                        return i(t, e)
                                    };
                                    break;
                                default:
                                    throw Vr("iscp", "Invalid isolate scope definition for directive '{0}'. Definition: {... {1}: '{2}' ...}", H.name, n, e)
                            }
                        })
                    }
                    for (k = l && f, N && i(N, function(e) {
                            var n, r = {
                                $scope: e === H || e.$$isolateScope ? x : t,
                                $element: g,
                                $attrs: $,
                                $transclude: k
                            };
                            w = e.controller, "@" == w && (w = $[e.name]), n = v(w, r), C[e.name] = n, G || g.data("$" + e.name + "Controller", n), e.controllerAs && (r.$scope[e.controllerAs] = n)
                        }), _ = 0, y = p.length; y > _; _++) try {
                        b = p[_], b(b.isolateScope ? x : t, g, $, b.require && h(b.require, g, C), k)
                    } catch (A) {
                        c(A, B(g))
                    }
                    var D = t;
                    for (H && (H.template || null === H.templateUrl) && (D = x), e && e(D, a.childNodes, n, l), _ = d.length - 1; _ >= 0; _--) try {
                        b = d[_], b(b.isolateScope ? x : t, g, $, b.require && h(b.require, g, C), k)
                    } catch (A) {
                        c(A, B(g))
                    }
                }
                f = f || {};
                for (var b, w, C, S, T, D, j = -Number.MAX_VALUE, N = f.controllerDirectives, H = f.newIsolateScopeDirective, F = f.templateDirective, L = f.nonTlbTranscludeDirective, Y = !1, G = !1, Z = s.$$element = pr(o), X = l, et = a, tt = 0, nt = e.length; nt > tt; tt++) {
                    w = e[tt];
                    var ot = w.$$start,
                        it = w.$$end;
                    if (ot && (Z = I(o, ot, it)), S = n, j > w.priority) break;
                    if ((D = w.scope) && (b = b || w, w.templateUrl || (W("new/isolated scope", H, w, Z), _(D) && (H = w))), C = w.name, !w.templateUrl && w.controller && (D = w.controller, N = N || {}, W("'" + C + "' controller", N[C], w, Z), N[C] = w), (D = w.transclude) && (Y = !0, w.$$tlb || (W("transclusion", L, w, Z), L = w), "element" == D ? (G = !0, j = w.priority, S = I(o, ot, it), Z = s.$$element = pr(t.createComment(" " + C + ": " + s[C] + " ")), o = Z[0], J(u, pr(U(S)), o), et = A(S, a, j, X && X.name, {
                            nonTlbTranscludeDirective: L
                        })) : (S = pr(dt(o)).contents(), Z.empty(), et = A(S, a))), w.template)
                        if (W("template", F, w, Z), F = w, D = k(w.template) ? w.template(Z, s) : w.template, D = rt(D), w.replace) {
                            if (X = w, S = pr("<div>" + br(D) + "</div>").contents(), o = S[0], 1 != S.length || 1 !== o.nodeType) throw Vr("tplrt", "Template for directive '{0}' must have exactly one root element. {1}", C, "");
                            J(u, Z, o);
                            var st = {
                                    $attr: {}
                                },
                                at = O(o, [], st),
                                ct = e.splice(tt + 1, e.length - (tt + 1));
                            H && R(at), e = e.concat(at).concat(ct), V(s, st), nt = e.length
                        } else Z.html(D);
                    if (w.templateUrl) W("template", F, w, Z), F = w, w.replace && (X = w), g = z(e.splice(tt, e.length - tt), Z, s, u, et, p, d, {
                        controllerDirectives: N,
                        newIsolateScopeDirective: H,
                        templateDirective: F,
                        nonTlbTranscludeDirective: L
                    }), nt = e.length;
                    else if (w.compile) try {
                        T = w.compile(Z, s, et), k(T) ? $(null, T, ot, it) : T && $(T.pre, T.post, ot, it)
                    } catch (ut) {
                        c(ut, B(Z))
                    }
                    w.terminal && (g.terminal = !0, j = Math.max(j, w.priority))
                }
                return g.scope = b && b.scope === !0, g.transclude = Y && et, g
            }

            function R(e) {
                for (var t = 0, n = e.length; n > t; t++) e[t] = f(e[t], {
                    $$isolateScope: !0
                })
            }

            function F(t, r, i, a, u, l, p) {
                if (r === u) return null;
                var d = null;
                if (o.hasOwnProperty(r))
                    for (var $, h = e.get(r + s), g = 0, m = h.length; m > g; g++) try {
                        $ = h[g], (a === n || a > $.priority) && -1 != $.restrict.indexOf(i) && (l && ($ = f($, {
                            $$start: l,
                            $$end: p
                        })), t.push($), d = $)
                    } catch (v) {
                        c(v)
                    }
                return d
            }

            function V(e, t) {
                var n = t.$attr,
                    r = e.$attr,
                    o = e.$$element;
                i(e, function(r, o) {
                    "$" != o.charAt(0) && (t[o] && (r += ("style" === o ? ";" : " ") + t[o]), e.$set(o, r, !0, n[o]))
                }), i(t, function(t, i) {
                    "class" == i ? (E(o, t), e["class"] = (e["class"] ? e["class"] + " " : "") + t) : "style" == i ? (o.attr("style", o.attr("style") + ";" + t), e.style = (e.style ? e.style + ";" : "") + t) : "$" == i.charAt(0) || e.hasOwnProperty(i) || (e[i] = t, r[i] = n[i])
                })
            }

            function z(e, t, n, r, o, s, a, c) {
                var u, l, f = [],
                    h = t[0],
                    g = e.shift(),
                    m = p({}, g, {
                        templateUrl: null,
                        transclude: null,
                        replace: null,
                        $$originalDirective: g
                    }),
                    v = k(g.templateUrl) ? g.templateUrl(t, n) : g.templateUrl;
                return t.empty(), d.get(C.getTrustedResourceUrl(v), {
                        cache: $
                    }).success(function(p) {
                        var d, $, y, b;
                        if (p = rt(p), g.replace) {
                            if (y = pr("<div>" + br(p) + "</div>").contents(), d = y[0], 1 != y.length || 1 !== d.nodeType) throw Vr("tplrt", "Template for directive '{0}' must have exactly one root element. {1}", g.name, v);
                            $ = {
                                $attr: {}
                            }, J(r, t, d);
                            var w = O(d, [], $);
                            _(g.scope) && R(w), e = w.concat(e), V(n, $)
                        } else d = h, t.html(p);
                        for (e.unshift(m), u = N(e, d, n, o, t, g, s, a, c), i(r, function(e, n) {
                                e == d && (r[n] = t[0])
                            }), l = D(t[0].childNodes, o); f.length;) {
                            var x = f.shift(),
                                k = f.shift(),
                                C = f.shift(),
                                S = f.shift(),
                                T = t[0];
                            k !== h && (T = dt(d), J(C, pr(k), T)), b = u.transclude ? j(x, u.transclude) : S, u(l, x, T, r, b)
                        }
                        f = null
                    }).error(function(e, t, n, r) {
                        throw Vr("tpload", "Failed to load template: {0}", r.url)
                    }),
                    function(e, t, n, r, o) {
                        f ? (f.push(t), f.push(n), f.push(r), f.push(o)) : u(l, t, n, r, o)
                    }
            }

            function L(e, t) {
                var n = t.priority - e.priority;
                return 0 !== n ? n : e.name !== t.name ? e.name < t.name ? -1 : 1 : e.index - t.index
            }

            function W(e, t, n, r) {
                if (t) throw Vr("multidir", "Multiple directives [{0}, {1}] asking for {2} on: {3}", t.name, n.name, e, B(r))
            }

            function Y(e, t) {
                var n = r(t, !0);
                n && e.push({
                    priority: 0,
                    compile: g(function(e, t) {
                        var r = t.parent(),
                            o = r.data("$binding") || [];
                        o.push(n), E(r.data("$binding", o), "ng-binding"), e.$watch(n, function(e) {
                            t[0].nodeValue = e
                        })
                    })
                })
            }

            function G(e, t) {
                if ("srcdoc" == t) return C.HTML;
                var n = $r(e);
                return "xlinkHref" == t || "FORM" == n && "action" == t || "IMG" != n && ("src" == t || "ngSrc" == t) ? C.RESOURCE_URL : void 0
            }

            function Z(e, t, n, o) {
                var i = r(n, !0);
                if (i) {
                    if ("multiple" === o && "SELECT" === $r(e)) throw Vr("selmulti", "Binding to the 'multiple' attribute is not supported. Element: {0}", B(e));
                    t.push({
                        priority: 100,
                        compile: function() {
                            return {
                                pre: function(t, n, s) {
                                    var a = s.$$observers || (s.$$observers = {});
                                    if (l.test(o)) throw Vr("nodomevents", "Interpolations for HTML DOM event attributes are disallowed.  Please use the ng- versions (such as ng-click instead of onclick) instead.");
                                    i = r(s[o], !0, G(e, o)), i && (s[o] = i(t), (a[o] || (a[o] = [])).$$inter = !0, (s.$$observers && s.$$observers[o].$$scope || t).$watch(i, function(e, t) {
                                        "class" === o && e != t ? s.$updateClass(e, t) : s.$set(o, e)
                                    }))
                                }
                            }
                        }
                    })
                }
            }

            function J(e, n, r) {
                var o, i, s = n[0],
                    a = n.length,
                    c = s.parentNode;
                if (e)
                    for (o = 0, i = e.length; i > o; o++)
                        if (e[o] == s) {
                            e[o++] = r;
                            for (var u = o, l = u + a - 1, p = e.length; p > u; u++, l++) p > l ? e[u] = e[l] : delete e[u];
                            e.length -= a - 1;
                            break
                        }
                c && c.replaceChild(r, s);
                var d = t.createDocumentFragment();
                d.appendChild(s), r[pr.expando] = s[pr.expando];
                for (var f = 1, $ = n.length; $ > f; f++) {
                    var h = n[f];
                    pr(h).remove(), d.appendChild(h), delete n[f]
                }
                n[0] = r, n.length = 1
            }

            function Q(e, t) {
                return p(function() {
                    return e.apply(null, arguments)
                }, e, t)
            }
            var K = function(e, t) {
                this.$$element = e, this.$attr = t || {}
            };
            K.prototype = {
                $normalize: Nt,
                $addClass: function(e) {
                    e && e.length > 0 && S.addClass(this.$$element, e)
                },
                $removeClass: function(e) {
                    e && e.length > 0 && S.removeClass(this.$$element, e)
                },
                $updateClass: function(e, t) {
                    this.$removeClass(Rt(t, e)), this.$addClass(Rt(e, t))
                },
                $set: function(e, t, r, o) {
                    var s, a = Ct(this.$$element[0], e);
                    a && (this.$$element.prop(e, t), o = a), this[e] = t, o ? this.$attr[e] = o : (o = this.$attr[e], o || (this.$attr[e] = o = X(e, "-"))), s = $r(this.$$element), ("A" === s && "href" === e || "IMG" === s && "src" === e) && (this[e] = t = T(t, "src" === e)), r !== !1 && (null === t || t === n ? this.$$element.removeAttr(o) : this.$$element.attr(o, t));
                    var u = this.$$observers;
                    u && i(u[e], function(e) {
                        try {
                            e(t)
                        } catch (n) {
                            c(n)
                        }
                    })
                },
                $observe: function(e, t) {
                    var n = this,
                        r = n.$$observers || (n.$$observers = {}),
                        o = r[e] || (r[e] = []);
                    return o.push(t), b.$evalAsync(function() {
                        o.$$inter || t(n[e])
                    }), t
                }
            };
            var et = r.startSymbol(),
                nt = r.endSymbol(),
                rt = "{{" == et || "}}" == nt ? h : function(e) {
                    return e.replace(/\{\{/g, et).replace(/}}/g, nt)
                },
                ot = /^ngAttr[A-Z]/;
            return A
        }]
    }

    function Nt(e) {
        return ut(e.replace(zr, ""))
    }

    function Rt(e, t) {
        var n = "",
            r = e.split(/\s+/),
            o = t.split(/\s+/);
        e: for (var i = 0; i < r.length; i++) {
            for (var s = r[i], a = 0; a < o.length; a++)
                if (s == o[a]) continue e;
            n += (n.length > 0 ? " " : "") + s
        }
        return n
    }

    function Ut() {
        var e = {},
            t = /^(\S+)(\s+as\s+(\w+))?$/;
        this.register = function(t, n) {
            rt(t, "controller"), _(t) ? p(e, t) : e[t] = n
        }, this.$get = ["$injector", "$window", function(n, o) {
            return function(i, s) {
                var a, c, u, l;
                if (y(i) && (c = i.match(t), u = c[1], l = c[3], i = e.hasOwnProperty(u) ? e[u] : ot(s.$scope, u, !0) || ot(o, u, !0), nt(i, u, !0)), a = n.instantiate(i, s), l) {
                    if (!s || "object" != typeof s.$scope) throw r("$controller")("noscp", "Cannot export controller '{0}' as '{1}'! No $scope object provided via `locals`.", u || i.name, l);
                    s.$scope[l] = a
                }
                return a
            }
        }]
    }

    function Ht() {
        this.$get = ["$window", function(e) {
            return pr(e.document)
        }]
    }

    function Ft() {
        this.$get = ["$log", function(e) {
            return function() {
                e.error.apply(e, arguments)
            }
        }]
    }

    function Vt(e) {
        var t, n, r, o = {};
        return e ? (i(e.split("\n"), function(e) {
            r = e.indexOf(":"), t = sr(br(e.substr(0, r))), n = br(e.substr(r + 1)), t && (o[t] ? o[t] += ", " + n : o[t] = n)
        }), o) : o
    }

    function zt(e) {
        var t = _(e) ? e : n;
        return function(n) {
            return t || (t = Vt(e)), n ? t[sr(n)] || null : t
        }
    }

    function Lt(e, t, n) {
        return k(n) ? n(e, t) : (i(n, function(n) {
            e = n(e, t)
        }), e)
    }

    function Bt(e) {
        return e >= 200 && 300 > e
    }

    function Wt() {
        var e = /^\s*(\[|\{[^\{])/,
            t = /[\}\]]\s*$/,
            r = /^\)\]\}',?\n/,
            o = {
                "Content-Type": "application/json;charset=utf-8"
            },
            s = this.defaults = {
                transformResponse: [function(n) {
                    return y(n) && (n = n.replace(r, ""), e.test(n) && t.test(n) && (n = z(n))), n
                }],
                transformRequest: [function(e) {
                    return _(e) && !A(e) ? V(e) : e
                }],
                headers: {
                    common: {
                        Accept: "application/json, text/plain, */*"
                    },
                    post: P(o),
                    put: P(o),
                    patch: P(o)
                },
                xsrfCookieName: "XSRF-TOKEN",
                xsrfHeaderName: "X-XSRF-TOKEN"
            },
            c = this.interceptors = [],
            u = this.responseInterceptors = [];
        this.$get = ["$httpBackend", "$browser", "$cacheFactory", "$rootScope", "$q", "$injector", function(e, t, r, o, l, d) {
            function f(e) {
                function r(e) {
                    var t = p({}, e, {
                        data: Lt(e.data, e.headers, a.transformResponse)
                    });
                    return Bt(e.status) ? t : l.reject(t)
                }

                function o(e) {
                    function t(e) {
                        var t;
                        i(e, function(n, r) {
                            k(n) && (t = n(), null != t ? e[r] = t : delete e[r])
                        })
                    }
                    var n, r, o, a = s.headers,
                        c = p({}, e.headers);
                    a = p({}, a.common, a[sr(e.method)]), t(a), t(c);
                    e: for (n in a) {
                        r = sr(n);
                        for (o in c)
                            if (sr(o) === r) continue e;
                        c[n] = a[n]
                    }
                    return c
                }
                var a = {
                        transformRequest: s.transformRequest,
                        transformResponse: s.transformResponse
                    },
                    c = o(e);
                p(a, e), a.headers = c, a.method = ar(a.method);
                var u = Pn(a.url) ? t.cookies()[a.xsrfCookieName || s.xsrfCookieName] : n;
                u && (c[a.xsrfHeaderName || s.xsrfHeaderName] = u);
                var d = function(e) {
                        c = e.headers;
                        var t = Lt(e.data, zt(c), e.transformRequest);
                        return m(e.data) && i(c, function(e, t) {
                            "content-type" === sr(t) && delete c[t]
                        }), m(e.withCredentials) && !m(s.withCredentials) && (e.withCredentials = s.withCredentials), g(e, t, c).then(r, r)
                    },
                    f = [d, n],
                    $ = l.when(a);
                for (i(C, function(e) {
                        (e.request || e.requestError) && f.unshift(e.request, e.requestError), (e.response || e.responseError) && f.push(e.response, e.responseError)
                    }); f.length;) {
                    var h = f.shift(),
                        v = f.shift();
                    $ = $.then(h, v)
                }
                return $.success = function(e) {
                    return $.then(function(t) {
                        e(t.data, t.status, t.headers, a)
                    }), $
                }, $.error = function(e) {
                    return $.then(null, function(t) {
                        e(t.data, t.status, t.headers, a)
                    }), $
                }, $
            }

            function $() {
                i(arguments, function(e) {
                    f[e] = function(t, n) {
                        return f(p(n || {}, {
                            method: e,
                            url: t
                        }))
                    }
                })
            }

            function h() {
                i(arguments, function(e) {
                    f[e] = function(t, n, r) {
                        return f(p(r || {}, {
                            method: e,
                            url: t,
                            data: n
                        }))
                    }
                })
            }

            function g(t, n, r) {
                function i(e, t, n) {
                    u && (Bt(e) ? u.put(h, [e, t, Vt(n)]) : u.remove(h)), a(t, e, n), o.$$phase || o.$apply()
                }

                function a(e, n, r) {
                    n = Math.max(n, 0), (Bt(n) ? d.resolve : d.reject)({
                        data: e,
                        status: n,
                        headers: zt(r),
                        config: t
                    })
                }

                function c() {
                    var e = O(f.pendingRequests, t); - 1 !== e && f.pendingRequests.splice(e, 1)
                }
                var u, p, d = l.defer(),
                    $ = d.promise,
                    h = b(t.url, t.params);
                if (f.pendingRequests.push(t), $.then(c, c), (t.cache || s.cache) && t.cache !== !1 && "GET" == t.method && (u = _(t.cache) ? t.cache : _(s.cache) ? s.cache : w), u)
                    if (p = u.get(h), v(p)) {
                        if (p.then) return p.then(c, c), p;
                        x(p) ? a(p[1], p[0], P(p[2])) : a(p, 200, {})
                    } else u.put(h, $);
                return m(p) && e(t.method, h, n, i, r, t.timeout, t.withCredentials, t.responseType), $
            }

            function b(e, t) {
                if (!t) return e;
                var n = [];
                return a(t, function(e, t) {
                    null === e || m(e) || (x(e) || (e = [e]), i(e, function(e) {
                        _(e) && (e = V(e)), n.push(J(t) + "=" + J(e))
                    }))
                }), e + (-1 == e.indexOf("?") ? "?" : "&") + n.join("&")
            }
            var w = r("$http"),
                C = [];
            return i(c, function(e) {
                C.unshift(y(e) ? d.get(e) : d.invoke(e))
            }), i(u, function(e, t) {
                var n = y(e) ? d.get(e) : d.invoke(e);
                C.splice(t, 0, {
                    response: function(e) {
                        return n(l.when(e))
                    },
                    responseError: function(e) {
                        return n(l.reject(e))
                    }
                })
            }), f.pendingRequests = [], $("get", "delete", "head", "jsonp"), h("post", "put"), f.defaults = s, f
        }]
    }

    function Yt(t) {
        return 8 >= lr && "patch" === sr(t) ? new ActiveXObject("Microsoft.XMLHTTP") : new e.XMLHttpRequest
    }

    function Gt() {
        this.$get = ["$browser", "$window", "$document", function(e, t, n) {
            return Zt(e, Yt, e.defer, t.angular.callbacks, n[0])
        }]
    }

    function Zt(e, t, n, r, o) {
        function s(e, t) {
            var n = o.createElement("script"),
                r = function() {
                    n.onreadystatechange = n.onload = n.onerror = null, o.body.removeChild(n), t && t()
                };
            return n.type = "text/javascript", n.src = e, lr && 8 >= lr ? n.onreadystatechange = function() {
                /loaded|complete/.test(n.readyState) && r()
            } : n.onload = n.onerror = function() {
                r()
            }, o.body.appendChild(n), r
        }
        var a = -1;
        return function(o, c, u, l, p, d, f, h) {
            function g() {
                _ = a, b && b(), w && w.abort()
            }

            function m(t, r, o, i) {
                x && n.cancel(x), b = w = null, r = 0 === r ? o ? 200 : 404 : r, r = 1223 == r ? 204 : r, t(r, o, i), e.$$completeOutstandingRequest($)
            }
            var _;
            if (e.$$incOutstandingRequestCount(), c = c || e.url(), "jsonp" == sr(o)) {
                var y = "_" + (r.counter++).toString(36);
                r[y] = function(e) {
                    r[y].data = e
                };
                var b = s(c.replace("JSON_CALLBACK", "angular.callbacks." + y), function() {
                    r[y].data ? m(l, 200, r[y].data) : m(l, _ || -2), r[y] = _r.noop
                })
            } else {
                var w = t(o);
                w.open(o, c, !0), i(p, function(e, t) {
                    v(e) && w.setRequestHeader(t, e)
                }), w.onreadystatechange = function() {
                    if (w && 4 == w.readyState) {
                        var e = null,
                            t = null;
                        _ !== a && (e = w.getAllResponseHeaders(), t = "response" in w ? w.response : w.responseText), m(l, _ || w.status, t, e)
                    }
                }, f && (w.withCredentials = !0), h && (w.responseType = h), w.send(u || null)
            }
            if (d > 0) var x = n(g, d);
            else d && d.then && d.then(g)
        }
    }

    function Jt() {
        var e = "{{",
            t = "}}";
        this.startSymbol = function(t) {
            return t ? (e = t, this) : e
        }, this.endSymbol = function(e) {
            return e ? (t = e, this) : t
        }, this.$get = ["$parse", "$exceptionHandler", "$sce", function(n, r, o) {
            function i(i, c, u) {
                for (var l, p, d, f, $ = 0, h = [], g = i.length, v = !1, _ = []; g > $;) - 1 != (l = i.indexOf(e, $)) && -1 != (p = i.indexOf(t, l + s)) ? ($ != l && h.push(i.substring($, l)), h.push(d = n(f = i.substring(l + s, p))), d.exp = f, $ = p + a, v = !0) : ($ != g && h.push(i.substring($)), $ = g);
                if ((g = h.length) || (h.push(""), g = 1), u && h.length > 1) throw Lr("noconcat", "Error while interpolating: {0}\nStrict Contextual Escaping disallows interpolations that concatenate multiple expressions when a trusted value is required.  See http://docs.angularjs.org/api/ng.$sce", i);
                return !c || v ? (_.length = g, d = function(e) {
                    try {
                        for (var t, n = 0, s = g; s > n; n++) "function" == typeof(t = h[n]) && (t = t(e), t = u ? o.getTrusted(u, t) : o.valueOf(t), null === t || m(t) ? t = "" : "string" != typeof t && (t = V(t))), _[n] = t;
                        return _.join("")
                    } catch (a) {
                        var c = Lr("interr", "Can't interpolate: {0}\n{1}", i, a.toString());
                        r(c)
                    }
                }, d.exp = i, d.parts = h, d) : void 0
            }
            var s = e.length,
                a = t.length;
            return i.startSymbol = function() {
                return e
            }, i.endSymbol = function() {
                return t
            }, i
        }]
    }

    function Qt() {
        this.$get = ["$rootScope", "$window", "$q", function(e, t, n) {
            function r(r, i, s, a) {
                var c = t.setInterval,
                    u = t.clearInterval,
                    l = n.defer(),
                    p = l.promise,
                    d = 0,
                    f = v(a) && !a;
                return s = v(s) ? s : 0, p.then(null, null, r), p.$$intervalId = c(function() {
                    l.notify(d++), s > 0 && d >= s && (l.resolve(d), u(p.$$intervalId), delete o[p.$$intervalId]), f || e.$apply()
                }, i), o[p.$$intervalId] = l, p
            }
            var o = {};
            return r.cancel = function(e) {
                return e && e.$$intervalId in o ? (o[e.$$intervalId].reject("canceled"), clearInterval(e.$$intervalId), delete o[e.$$intervalId], !0) : !1
            }, r
        }]
    }

    function Kt() {
        this.$get = function() {
            return {
                id: "en-us",
                NUMBER_FORMATS: {
                    DECIMAL_SEP: ".",
                    GROUP_SEP: ",",
                    PATTERNS: [{
                        minInt: 1,
                        minFrac: 0,
                        maxFrac: 3,
                        posPre: "",
                        posSuf: "",
                        negPre: "-",
                        negSuf: "",
                        gSize: 3,
                        lgSize: 3
                    }, {
                        minInt: 1,
                        minFrac: 2,
                        maxFrac: 2,
                        posPre: "陇",
                        posSuf: "",
                        negPre: "(陇",
                        negSuf: ")",
                        gSize: 3,
                        lgSize: 3
                    }],
                    CURRENCY_SYM: "$"
                },
                DATETIME_FORMATS: {
                    MONTH: "January,February,March,April,May,June,July,August,September,October,November,December".split(","),
                    SHORTMONTH: "Jan,Feb,Mar,Apr,May,Jun,Jul,Aug,Sep,Oct,Nov,Dec".split(","),
                    DAY: "Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday".split(","),
                    SHORTDAY: "Sun,Mon,Tue,Wed,Thu,Fri,Sat".split(","),
                    AMPMS: ["AM", "PM"],
                    medium: "MMM d, y h:mm:ss a",
                    "short": "M/d/yy h:mm a",
                    fullDate: "EEEE, MMMM d, y",
                    longDate: "MMMM d, y",
                    mediumDate: "MMM d, y",
                    shortDate: "M/d/yy",
                    mediumTime: "h:mm:ss a",
                    shortTime: "h:mm a"
                },
                pluralCat: function(e) {
                    return 1 === e ? "one" : "other"
                }
            }
        }
    }

    function Xt(e) {
        for (var t = e.split("/"), n = t.length; n--;) t[n] = Z(t[n]);
        return t.join("/")
    }

    function en(e, t, n) {
        var r = In(e, n);
        t.$$protocol = r.protocol, t.$$host = r.hostname, t.$$port = d(r.port) || Wr[r.protocol] || null
    }

    function tn(e, t, n) {
        var r = "/" !== e.charAt(0);
        r && (e = "/" + e);
        var o = In(e, n);
        t.$$path = decodeURIComponent(r && "/" === o.pathname.charAt(0) ? o.pathname.substring(1) : o.pathname), t.$$search = Y(o.search), t.$$hash = decodeURIComponent(o.hash), t.$$path && "/" != t.$$path.charAt(0) && (t.$$path = "/" + t.$$path)
    }

    function nn(e, t) {
        return 0 === t.indexOf(e) ? t.substr(e.length) : void 0
    }

    function rn(e) {
        var t = e.indexOf("#");
        return -1 == t ? e : e.substr(0, t)
    }

    function on(e) {
        return e.substr(0, rn(e).lastIndexOf("/") + 1)
    }

    function sn(e) {
        return e.substring(0, e.indexOf("/", e.indexOf("//") + 2))
    }

    function an(e, t) {
        this.$$html5 = !0, t = t || "";
        var r = on(e);
        en(e, this, e), this.$$parse = function(t) {
            var n = nn(r, t);
            if (!y(n)) throw Yr("ipthprfx", 'Invalid url "{0}", missing path prefix "{1}".', t, r);
            tn(n, this, e), this.$$path || (this.$$path = "/"), this.$$compose()
        }, this.$$compose = function() {
            var e = G(this.$$search),
                t = this.$$hash ? "#" + Z(this.$$hash) : "";
            this.$$url = Xt(this.$$path) + (e ? "?" + e : "") + t, this.$$absUrl = r + this.$$url.substr(1)
        }, this.$$rewrite = function(o) {
            var i, s;
            return (i = nn(e, o)) !== n ? (s = i, (i = nn(t, i)) !== n ? r + (nn("/", i) || i) : e + s) : (i = nn(r, o)) !== n ? r + i : r == o + "/" ? r : void 0
        }
    }

    function cn(e, t) {
        var n = on(e);
        en(e, this, e), this.$$parse = function(r) {
            function o(e, t, n) {
                var r, o = /^\/?.*?:(\/.*)/;
                return 0 === t.indexOf(n) && (t = t.replace(n, "")), o.exec(t) ? e : (r = o.exec(e), r ? r[1] : e)
            }
            var i = nn(e, r) || nn(n, r),
                s = "#" == i.charAt(0) ? nn(t, i) : this.$$html5 ? i : "";
            if (!y(s)) throw Yr("ihshprfx", 'Invalid url "{0}", missing hash prefix "{1}".', r, t);
            tn(s, this, e), this.$$path = o(this.$$path, s, e), this.$$compose()
        }, this.$$compose = function() {
            var n = G(this.$$search),
                r = this.$$hash ? "#" + Z(this.$$hash) : "";
            this.$$url = Xt(this.$$path) + (n ? "?" + n : "") + r, this.$$absUrl = e + (this.$$url ? t + this.$$url : "")
        }, this.$$rewrite = function(t) {
            return rn(e) == rn(t) ? t : void 0
        }
    }

    function un(e, t) {
        this.$$html5 = !0, cn.apply(this, arguments);
        var n = on(e);
        this.$$rewrite = function(r) {
            var o;
            return e == rn(r) ? r : (o = nn(n, r)) ? e + t + o : n === r + "/" ? n : void 0
        }
    }

    function ln(e) {
        return function() {
            return this[e]
        }
    }

    function pn(e, t) {
        return function(n) {
            return m(n) ? this[e] : (this[e] = t(n), this.$$compose(), this)
        }
    }

    function dn() {
        var t = "",
            n = !1;
        this.hashPrefix = function(e) {
            return v(e) ? (t = e, this) : t
        }, this.html5Mode = function(e) {
            return v(e) ? (n = e, this) : n
        }, this.$get = ["$rootScope", "$browser", "$sniffer", "$rootElement", function(r, o, i, s) {
            function a(e) {
                r.$broadcast("$locationChangeSuccess", c.absUrl(), e)
            }
            var c, u, l, p = o.baseHref(),
                d = o.url();
            n ? (l = sn(d) + (p || "/"), u = i.history ? an : un) : (l = rn(d), u = cn), c = new u(l, "#" + t), c.$$parse(c.$$rewrite(d)), s.on("click", function(t) {
                if (!t.ctrlKey && !t.metaKey && 2 != t.which) {
                    for (var n = pr(t.target);
                        "a" !== sr(n[0].nodeName);)
                        if (n[0] === s[0] || !(n = n.parent())[0]) return;
                    var i = n.prop("href");
                    _(i) && "[object SVGAnimatedString]" === i.toString() && (i = In(i.animVal).href);
                    var a = c.$$rewrite(i);
                    i && !n.attr("target") && a && !t.isDefaultPrevented() && (t.preventDefault(), a != o.url() && (c.$$parse(a), r.$apply(), e.angular["ff-684208-preventDefault"] = !0))
                }
            }), c.absUrl() != d && o.url(c.absUrl(), !0), o.onUrlChange(function(e) {
                c.absUrl() != e && (r.$evalAsync(function() {
                    var t = c.absUrl();
                    c.$$parse(e), r.$broadcast("$locationChangeStart", e, t).defaultPrevented ? (c.$$parse(t), o.url(t)) : a(t)
                }), r.$$phase || r.$digest())
            });
            var f = 0;
            return r.$watch(function() {
                var e = o.url(),
                    t = c.$$replace;
                return f && e == c.absUrl() || (f++, r.$evalAsync(function() {
                    r.$broadcast("$locationChangeStart", c.absUrl(), e).defaultPrevented ? c.$$parse(e) : (o.url(c.absUrl(), t), a(e))
                })), c.$$replace = !1, f
            }), c
        }]
    }

    function fn() {
        var e = !0,
            t = this;
        this.debugEnabled = function(t) {
            return v(t) ? (e = t, this) : e
        }, this.$get = ["$window", function(n) {
            function r(e) {
                return e instanceof Error && (e.stack ? e = e.message && -1 === e.stack.indexOf(e.message) ? "Error: " + e.message + "\n" + e.stack : e.stack : e.sourceURL && (e = e.message + "\n" + e.sourceURL + ":" + e.line)), e
            }

            function o(e) {
                var t = n.console || {},
                    o = t[e] || t.log || $,
                    s = !1;
                try {
                    s = !!o.apply
                } catch (a) {}
                return s ? function() {
                    var e = [];
                    return i(arguments, function(t) {
                        e.push(r(t))
                    }), o.apply(t, e)
                } : function(e, t) {
                    o(e, null == t ? "" : t)
                }
            }
            return {
                log: o("log"),
                info: o("info"),
                warn: o("warn"),
                error: o("error"),
                debug: function() {
                    var n = o("debug");
                    return function() {
                        e && n.apply(t, arguments)
                    }
                }()
            }
        }]
    }

    function $n(e, t) {
        if ("constructor" === e) throw Zr("isecfld", 'Referencing "constructor" field in Angular expressions is disallowed! Expression: {0}', t);
        return e
    }

    function hn(e, t) {
        if (e) {
            if (e.constructor === e) throw Zr("isecfn", "Referencing Function in Angular expressions is disallowed! Expression: {0}", t);
            if (e.document && e.location && e.alert && e.setInterval) throw Zr("isecwindow", "Referencing the Window in Angular expressions is disallowed! Expression: {0}", t);
            if (e.children && (e.nodeName || e.on && e.find)) throw Zr("isecdom", "Referencing DOM nodes in Angular expressions is disallowed! Expression: {0}", t)
        }
        return e
    }

    function gn(e, t, r, o, i) {
        i = i || {};
        for (var s, a = t.split("."), c = 0; a.length > 1; c++) {
            s = $n(a.shift(), o);
            var u = e[s];
            u || (u = {}, e[s] = u), e = u, e.then && i.unwrapPromises && (Gr(o), "$$v" in e || ! function(e) {
                e.then(function(t) {
                    e.$$v = t
                })
            }(e), e.$$v === n && (e.$$v = {}), e = e.$$v)
        }
        return s = $n(a.shift(), o), e[s] = r, r
    }

    function mn(e, t, r, o, i, s, a) {
        return $n(e, s), $n(t, s), $n(r, s), $n(o, s), $n(i, s), a.unwrapPromises ? function(a, c) {
            var u, l = c && c.hasOwnProperty(e) ? c : a;
            return null == l ? l : (l = l[e], l && l.then && (Gr(s), "$$v" in l || (u = l, u.$$v = n, u.then(function(e) {
                u.$$v = e
            })), l = l.$$v), t ? null == l ? n : (l = l[t], l && l.then && (Gr(s), "$$v" in l || (u = l, u.$$v = n, u.then(function(e) {
                u.$$v = e
            })), l = l.$$v), r ? null == l ? n : (l = l[r], l && l.then && (Gr(s), "$$v" in l || (u = l, u.$$v = n, u.then(function(e) {
                u.$$v = e
            })), l = l.$$v), o ? null == l ? n : (l = l[o], l && l.then && (Gr(s), "$$v" in l || (u = l, u.$$v = n, u.then(function(e) {
                u.$$v = e
            })), l = l.$$v), i ? null == l ? n : (l = l[i], l && l.then && (Gr(s), "$$v" in l || (u = l, u.$$v = n, u.then(function(e) {
                u.$$v = e
            })), l = l.$$v), l) : l) : l) : l) : l)
        } : function(s, a) {
            var c = a && a.hasOwnProperty(e) ? a : s;
            return null == c ? c : (c = c[e], t ? null == c ? n : (c = c[t], r ? null == c ? n : (c = c[r], o ? null == c ? n : (c = c[o], i ? null == c ? n : c = c[i] : c) : c) : c) : c)
        }
    }

    function vn(e, t) {
        return $n(e, t),
            function(t, r) {
                return null == t ? n : (r && r.hasOwnProperty(e) ? r : t)[e]
            }
    }

    function _n(e, t, r) {
        return $n(e, r), $n(t, r),
            function(r, o) {
                return null == r ? n : (r = (o && o.hasOwnProperty(e) ? o : r)[e], null == r ? n : r[t])
            }
    }

    function yn(e, t, r) {
        if (to.hasOwnProperty(e)) return to[e];
        var o, s = e.split("."),
            a = s.length;
        if (t.unwrapPromises || 1 !== a)
            if (t.unwrapPromises || 2 !== a)
                if (t.csp) o = 6 > a ? mn(s[0], s[1], s[2], s[3], s[4], r, t) : function(e, o) {
                    var i, c = 0;
                    do i = mn(s[c++], s[c++], s[c++], s[c++], s[c++], r, t)(e, o), o = n, e = i; while (a > c);
                    return i
                };
                else {
                    var c = "var p;\n";
                    i(s, function(e, n) {
                        $n(e, r), c += "if(s == null) return undefined;\ns=" + (n ? "s" : '((k&&k.hasOwnProperty("' + e + '"))?k:s)') + '["' + e + '"];\n' + (t.unwrapPromises ? 'if (s && s.then) {\n pw("' + r.replace(/(["\r\n])/g, "\\$1") + '");\n if (!("$$v" in s)) {\n p=s;\n p.$$v = undefined;\n p.then(function(v) {p.$$v=v;});\n}\n s=s.$$v\n}\n' : "")
                    }), c += "return s;";
                    var u = new Function("s", "k", "pw", c);
                    u.toString = g(c), o = t.unwrapPromises ? function(e, t) {
                        return u(e, t, Gr)
                    } : u
                }
        else o = _n(s[0], s[1], r);
        else o = vn(s[0], r);
        return "hasOwnProperty" !== e && (to[e] = o), o
    }

    function bn() {
        var e = {},
            t = {
                csp: !1,
                unwrapPromises: !1,
                logPromiseWarnings: !0
            };
        this.unwrapPromises = function(e) {
            return v(e) ? (t.unwrapPromises = !!e, this) : t.unwrapPromises
        }, this.logPromiseWarnings = function(e) {
            return v(e) ? (t.logPromiseWarnings = e, this) : t.logPromiseWarnings
        }, this.$get = ["$filter", "$sniffer", "$log", function(n, r, o) {
            return t.csp = r.csp, Gr = function(e) {
                    t.logPromiseWarnings && !Jr.hasOwnProperty(e) && (Jr[e] = !0, o.warn("[$parse] Promise found in the expression `" + e + "`. Automatic unwrapping of promises in Angular expressions is deprecated."))
                },
                function(r) {
                    var o;
                    switch (typeof r) {
                        case "string":
                            if (e.hasOwnProperty(r)) return e[r];
                            var i = new Xr(t),
                                s = new eo(i, n, t);
                            return o = s.parse(r, !1), "hasOwnProperty" !== r && (e[r] = o), o;
                        case "function":
                            return r;
                        default:
                            return $
                    }
                }
        }]
    }

    function wn() {
        this.$get = ["$rootScope", "$exceptionHandler", function(e, t) {
            return xn(function(t) {
                e.$evalAsync(t)
            }, t)
        }]
    }

    function xn(e, t) {
        function r(e) {
            return e
        }

        function o(e) {
            return u(e)
        }

        function s(e) {
            var t = a(),
                n = 0,
                r = x(e) ? [] : {};
            return i(e, function(e, o) {
                n++, c(e).then(function(e) {
                    r.hasOwnProperty(o) || (r[o] = e, --n || t.resolve(r))
                }, function(e) {
                    r.hasOwnProperty(o) || t.reject(e)
                })
            }), 0 === n && t.resolve(r), t.promise
        }
        var a = function() {
                var i, s, l = [];
                return s = {
                    resolve: function(t) {
                        if (l) {
                            var r = l;
                            l = n, i = c(t), r.length && e(function() {
                                for (var e, t = 0, n = r.length; n > t; t++) e = r[t], i.then(e[0], e[1], e[2])
                            })
                        }
                    },
                    reject: function(e) {
                        s.resolve(u(e))
                    },
                    notify: function(t) {
                        if (l) {
                            var n = l;
                            l.length && e(function() {
                                for (var e, r = 0, o = n.length; o > r; r++) e = n[r], e[2](t)
                            })
                        }
                    },
                    promise: {
                        then: function(e, n, s) {
                            var c = a(),
                                u = function(n) {
                                    try {
                                        c.resolve((k(e) ? e : r)(n))
                                    } catch (o) {
                                        c.reject(o), t(o)
                                    }
                                },
                                p = function(e) {
                                    try {
                                        c.resolve((k(n) ? n : o)(e))
                                    } catch (r) {
                                        c.reject(r), t(r)
                                    }
                                },
                                d = function(e) {
                                    try {
                                        c.notify((k(s) ? s : r)(e))
                                    } catch (n) {
                                        t(n)
                                    }
                                };
                            return l ? l.push([u, p, d]) : i.then(u, p, d), c.promise
                        },
                        "catch": function(e) {
                            return this.then(null, e)
                        },
                        "finally": function(e) {
                            function t(e, t) {
                                var n = a();
                                return t ? n.resolve(e) : n.reject(e), n.promise
                            }

                            function n(n, o) {
                                var i = null;
                                try {
                                    i = (e || r)()
                                } catch (s) {
                                    return t(s, !1)
                                }
                                return i && k(i.then) ? i.then(function() {
                                    return t(n, o)
                                }, function(e) {
                                    return t(e, !1)
                                }) : t(n, o)
                            }
                            return this.then(function(e) {
                                return n(e, !0)
                            }, function(e) {
                                return n(e, !1)
                            })
                        }
                    }
                }
            },
            c = function(t) {
                return t && k(t.then) ? t : {
                    then: function(n) {
                        var r = a();
                        return e(function() {
                            r.resolve(n(t))
                        }), r.promise
                    }
                }
            },
            u = function(n) {
                return {
                    then: function(r, i) {
                        var s = a();
                        return e(function() {
                            try {
                                s.resolve((k(i) ? i : o)(n))
                            } catch (e) {
                                s.reject(e), t(e)
                            }
                        }), s.promise
                    }
                }
            },
            l = function(n, i, s, l) {
                var p, d = a(),
                    f = function(e) {
                        try {
                            return (k(i) ? i : r)(e)
                        } catch (n) {
                            return t(n), u(n)
                        }
                    },
                    $ = function(e) {
                        try {
                            return (k(s) ? s : o)(e)
                        } catch (n) {
                            return t(n), u(n)
                        }
                    },
                    h = function(e) {
                        try {
                            return (k(l) ? l : r)(e)
                        } catch (n) {
                            t(n)
                        }
                    };
                return e(function() {
                    c(n).then(function(e) {
                        p || (p = !0, d.resolve(c(e).then(f, $, h)))
                    }, function(e) {
                        p || (p = !0, d.resolve($(e)))
                    }, function(e) {
                        p || d.notify(h(e))
                    })
                }), d.promise
            };
        return {
            defer: a,
            reject: u,
            when: l,
            all: s
        }
    }

    function kn() {
        var e = 10,
            t = r("$rootScope"),
            n = null;
        this.digestTtl = function(t) {
            return arguments.length && (e = t), e
        }, this.$get = ["$injector", "$exceptionHandler", "$parse", "$browser", function(r, s, a, c) {
            function l() {
                this.$id = u(), this.$$phase = this.$parent = this.$$watchers = this.$$nextSibling = this.$$prevSibling = this.$$childHead = this.$$childTail = null, this["this"] = this.$root = this, this.$$destroyed = !1, this.$$asyncQueue = [], this.$$postDigestQueue = [], this.$$listeners = {}, this.$$listenerCount = {}, this.$$isolateBindings = {}
            }

            function p(e) {
                if (m.$$phase) throw t("inprog", "{0} already in progress", m.$$phase);
                m.$$phase = e
            }

            function d() {
                m.$$phase = null
            }

            function f(e, t) {
                var n = a(e);
                return nt(n, t), n
            }

            function h(e, t, n) {
                do e.$$listenerCount[n] -= t, 0 === e.$$listenerCount[n] && delete e.$$listenerCount[n]; while (e = e.$parent)
            }

            function g() {}
            l.prototype = {
                constructor: l,
                $new: function(e) {
                    var t, n;
                    return e ? (n = new l, n.$root = this.$root, n.$$asyncQueue = this.$$asyncQueue, n.$$postDigestQueue = this.$$postDigestQueue) : (t = function() {}, t.prototype = this, n = new t, n.$id = u()), n["this"] = n, n.$$listeners = {}, n.$$listenerCount = {}, n.$parent = this, n.$$watchers = n.$$nextSibling = n.$$childHead = n.$$childTail = null, n.$$prevSibling = this.$$childTail, this.$$childHead ? (this.$$childTail.$$nextSibling = n, this.$$childTail = n) : this.$$childHead = this.$$childTail = n, n
                },
                $watch: function(e, t, r) {
                    var o = this,
                        i = f(e, "watch"),
                        s = o.$$watchers,
                        a = {
                            fn: t,
                            last: g,
                            get: i,
                            exp: e,
                            eq: !!r
                        };
                    if (n = null, !k(t)) {
                        var c = f(t || $, "listener");
                        a.fn = function(e, t, n) {
                            c(n)
                        }
                    }
                    if ("string" == typeof e && i.constant) {
                        var u = a.fn;
                        a.fn = function(e, t, n) {
                            u.call(this, e, t, n), I(s, a)
                        }
                    }
                    return s || (s = o.$$watchers = []), s.unshift(a),
                        function() {
                            I(s, a), n = null
                        }
                },
                $watchCollection: function(e, t) {
                    function n() {
                        s = l(c);
                        var e, t;
                        if (_(s))
                            if (o(s)) {
                                i !== p && (i = p, f = i.length = 0, u++), e = s.length, f !== e && (u++, i.length = f = e);
                                for (var n = 0; e > n; n++) i[n] !== s[n] && (u++, i[n] = s[n])
                            } else {
                                i !== d && (i = d = {}, f = 0, u++), e = 0;
                                for (t in s) s.hasOwnProperty(t) && (e++, i.hasOwnProperty(t) ? i[t] !== s[t] && (u++, i[t] = s[t]) : (f++, i[t] = s[t], u++));
                                if (f > e) {
                                    u++;
                                    for (t in i) i.hasOwnProperty(t) && !s.hasOwnProperty(t) && (f--, delete i[t])
                                }
                            }
                        else i !== s && (i = s, u++);
                        return u
                    }

                    function r() {
                        t(s, i, c)
                    }
                    var i, s, c = this,
                        u = 0,
                        l = a(e),
                        p = [],
                        d = {},
                        f = 0;
                    return this.$watch(n, r)
                },
                $digest: function() {
                    var r, o, i, a, c, u, l, f, $, h, m, v = this.$$asyncQueue,
                        _ = this.$$postDigestQueue,
                        y = e,
                        b = this,
                        w = [];
                    p("$digest"), n = null;
                    do {
                        for (u = !1, f = b; v.length;) {
                            try {
                                m = v.shift(), m.scope.$eval(m.expression)
                            } catch (x) {
                                d(), s(x)
                            }
                            n = null
                        }
                        e: do {
                            if (a = f.$$watchers)
                                for (c = a.length; c--;) try {
                                    if (r = a[c])
                                        if ((o = r.get(f)) === (i = r.last) || (r.eq ? q(o, i) : "number" == typeof o && "number" == typeof i && isNaN(o) && isNaN(i))) {
                                            if (r === n) {
                                                u = !1;
                                                break e
                                            }
                                        } else u = !0, n = r, r.last = r.eq ? P(o) : o, r.fn(o, i === g ? o : i, f), 5 > y && ($ = 4 - y, w[$] || (w[$] = []), h = k(r.exp) ? "fn: " + (r.exp.name || r.exp.toString()) : r.exp, h += "; newVal: " + V(o) + "; oldVal: " + V(i), w[$].push(h))
                                } catch (x) {
                                    d(), s(x)
                                }
                            if (!(l = f.$$childHead || f !== b && f.$$nextSibling))
                                for (; f !== b && !(l = f.$$nextSibling);) f = f.$parent
                        } while (f = l);
                        if ((u || v.length) && !y--) throw d(), t("infdig", "{0} $digest() iterations reached. Aborting!\nWatchers fired in the last 5 iterations: {1}", e, V(w))
                    } while (u || v.length);
                    for (d(); _.length;) try {
                        _.shift()()
                    } catch (x) {
                        s(x)
                    }
                },
                $destroy: function() {
                    if (!this.$$destroyed) {
                        var e = this.$parent;
                        this.$broadcast("$destroy"), this.$$destroyed = !0, this !== m && (i(this.$$listenerCount, H(null, h, this)), e.$$childHead == this && (e.$$childHead = this.$$nextSibling), e.$$childTail == this && (e.$$childTail = this.$$prevSibling), this.$$prevSibling && (this.$$prevSibling.$$nextSibling = this.$$nextSibling), this.$$nextSibling && (this.$$nextSibling.$$prevSibling = this.$$prevSibling), this.$parent = this.$$nextSibling = this.$$prevSibling = this.$$childHead = this.$$childTail = null)
                    }
                },
                $eval: function(e, t) {
                    return a(e)(this, t)
                },
                $evalAsync: function(e) {
                    m.$$phase || m.$$asyncQueue.length || c.defer(function() {
                        m.$$asyncQueue.length && m.$digest()
                    }), this.$$asyncQueue.push({
                        scope: this,
                        expression: e
                    })
                },
                $$postDigest: function(e) {
                    this.$$postDigestQueue.push(e)
                },
                $apply: function(e) {
                    try {
                        return p("$apply"), this.$eval(e)
                    } catch (t) {
                        s(t)
                    } finally {
                        d();
                        try {
                            m.$digest()
                        } catch (t) {
                            throw s(t), t
                        }
                    }
                },
                $on: function(e, t) {
                    var n = this.$$listeners[e];
                    n || (this.$$listeners[e] = n = []), n.push(t);
                    var r = this;
                    do r.$$listenerCount[e] || (r.$$listenerCount[e] = 0), r.$$listenerCount[e]++; while (r = r.$parent);
                    var o = this;
                    return function() {
                        n[O(n, t)] = null, h(o, 1, e)
                    }
                },
                $emit: function(e) {
                    var t, n, r, o = [],
                        i = this,
                        a = !1,
                        c = {
                            name: e,
                            targetScope: i,
                            stopPropagation: function() {
                                a = !0
                            },
                            preventDefault: function() {
                                c.defaultPrevented = !0
                            },
                            defaultPrevented: !1
                        },
                        u = R([c], arguments, 1);
                    do {
                        for (t = i.$$listeners[e] || o, c.currentScope = i, n = 0, r = t.length; r > n; n++)
                            if (t[n]) try {
                                t[n].apply(null, u)
                            } catch (l) {
                                s(l)
                            } else t.splice(n, 1), n--, r--;
                        if (a) return c;
                        i = i.$parent
                    } while (i);
                    return c
                },
                $broadcast: function(e) {
                    for (var t, n, r, o = this, i = o, a = o, c = {
                            name: e,
                            targetScope: o,
                            preventDefault: function() {
                                c.defaultPrevented = !0
                            },
                            defaultPrevented: !1
                        }, u = R([c], arguments, 1); i = a;) {
                        for (c.currentScope = i, t = i.$$listeners[e] || [], n = 0, r = t.length; r > n; n++)
                            if (t[n]) try {
                                t[n].apply(null, u)
                            } catch (l) {
                                s(l)
                            } else t.splice(n, 1), n--, r--;
                        if (!(a = i.$$listenerCount[e] && i.$$childHead || i !== o && i.$$nextSibling))
                            for (; i !== o && !(a = i.$$nextSibling);) i = i.$parent
                    }
                    return c
                }
            };
            var m = new l;
            return m
        }]
    }

    function Cn() {
        var e = /^\s*(https?|ftp|mailto|tel|file):/,
            t = /^\s*(https?|ftp|file):|data:image\//;
        this.aHrefSanitizationWhitelist = function(t) {
            return v(t) ? (e = t, this) : e
        }, this.imgSrcSanitizationWhitelist = function(e) {
            return v(e) ? (t = e, this) : t
        }, this.$get = function() {
            return function(n, r) {
                var o, i = r ? t : e;
                return lr && !(lr >= 8) || (o = In(n).href, "" === o || o.match(i)) ? n : "unsafe:" + o
            }
        }
    }

    function Sn(e) {
        return e.replace(/([-()\[\]{}+?*.$\^|,:#<!\\])/g, "\\$1").replace(/\x08/g, "\\x08")
    }

    function Tn(e) {
        if ("self" === e) return e;
        if (y(e)) {
            if (e.indexOf("***") > -1) throw no("iwcard", "Illegal sequence *** in string matcher.  String: {0}", e);
            return e = Sn(e).replace("\\*\\*", ".*").replace("\\*", "[^:/.?&;]*"), new RegExp("^" + e + "$")
        }
        if (C(e)) return new RegExp("^" + e.source + "$");
        throw no("imatcher", 'Matchers may only be "self", string patterns or RegExp objects')
    }

    function An(e) {
        var t = [];
        return v(e) && i(e, function(e) {
            t.push(Tn(e))
        }), t
    }

    function En() {
        this.SCE_CONTEXTS = ro;
        var e = ["self"],
            t = [];
        this.resourceUrlWhitelist = function(t) {
            return arguments.length && (e = An(t)), e
        }, this.resourceUrlBlacklist = function(e) {
            return arguments.length && (t = An(e)), t
        }, this.$get = ["$injector", function(r) {
            function o(e, t) {
                return "self" === e ? Pn(t) : !!e.exec(t.href)
            }

            function i(n) {
                var r, i, s = In(n.toString()),
                    a = !1;
                for (r = 0, i = e.length; i > r; r++)
                    if (o(e[r], s)) {
                        a = !0;
                        break
                    }
                if (a)
                    for (r = 0, i = t.length; i > r; r++)
                        if (o(t[r], s)) {
                            a = !1;
                            break
                        }
                return a
            }

            function s(e) {
                var t = function(e) {
                    this.$$unwrapTrustedValue = function() {
                        return e
                    }
                };
                return e && (t.prototype = new e), t.prototype.valueOf = function() {
                    return this.$$unwrapTrustedValue()
                }, t.prototype.toString = function() {
                    return this.$$unwrapTrustedValue().toString()
                }, t
            }

            function a(e, t) {
                var r = d.hasOwnProperty(e) ? d[e] : null;
                if (!r) throw no("icontext", "Attempted to trust a value in invalid context. Context: {0}; Value: {1}", e, t);
                if (null === t || t === n || "" === t) return t;
                if ("string" != typeof t) throw no("itype", "Attempted to trust a non-string value in a content requiring a string: Context: {0}", e);
                return new r(t)
            }

            function c(e) {
                return e instanceof p ? e.$$unwrapTrustedValue() : e
            }

            function u(e, t) {
                if (null === t || t === n || "" === t) return t;
                var r = d.hasOwnProperty(e) ? d[e] : null;
                if (r && t instanceof r) return t.$$unwrapTrustedValue();
                if (e === ro.RESOURCE_URL) {
                    if (i(t)) return t;
                    throw no("insecurl", "Blocked loading resource from url not allowed by $sceDelegate policy.  URL: {0}", t.toString())
                }
                if (e === ro.HTML) return l(t);
                throw no("unsafe", "Attempting to use an unsafe value in a safe context.")
            }
            var l = function() {
                throw no("unsafe", "Attempting to use an unsafe value in a safe context.")
            };
            r.has("$sanitize") && (l = r.get("$sanitize"));
            var p = s(),
                d = {};
            return d[ro.HTML] = s(p), d[ro.CSS] = s(p), d[ro.URL] = s(p), d[ro.JS] = s(p), d[ro.RESOURCE_URL] = s(d[ro.URL]), {
                trustAs: a,
                getTrusted: u,
                valueOf: c
            }
        }]
    }

    function Dn() {
        var e = !0;
        this.enabled = function(t) {
            return arguments.length && (e = !!t), e
        }, this.$get = ["$parse", "$sniffer", "$sceDelegate", function(t, n, r) {
            if (e && n.msie && n.msieDocumentMode < 8) throw no("iequirks", "Strict Contextual Escaping does not support Internet Explorer version < 9 in quirks mode.  You can fix this by adding the text <!doctype html> to the top of your HTML document.  See http://docs.angularjs.org/api/ng.$sce for more information.");
            var o = P(ro);
            o.isEnabled = function() {
                return e
            }, o.trustAs = r.trustAs, o.getTrusted = r.getTrusted, o.valueOf = r.valueOf, e || (o.trustAs = o.getTrusted = function(e, t) {
                return t
            }, o.valueOf = h), o.parseAs = function(e, n) {
                var r = t(n);
                return r.literal && r.constant ? r : function(t, n) {
                    return o.getTrusted(e, r(t, n))
                }
            };
            var s = o.parseAs,
                a = o.getTrusted,
                c = o.trustAs;
            return i(ro, function(e, t) {
                var n = sr(t);
                o[ut("parse_as_" + n)] = function(t) {
                    return s(e, t)
                }, o[ut("get_trusted_" + n)] = function(t) {
                    return a(e, t)
                }, o[ut("trust_as_" + n)] = function(t) {
                    return c(e, t)
                }
            }), o
        }]
    }

    function jn() {
        this.$get = ["$window", "$document", function(e, t) {
            var n, r, o = {},
                i = d((/android (\d+)/.exec(sr((e.navigator || {}).userAgent)) || [])[1]),
                s = /Boxee/i.test((e.navigator || {}).userAgent),
                a = t[0] || {},
                c = a.documentMode,
                u = /^(Moz|webkit|O|ms)(?=[A-Z])/,
                l = a.body && a.body.style,
                p = !1,
                f = !1;
            if (l) {
                for (var $ in l)
                    if (r = u.exec($)) {
                        n = r[0], n = n.substr(0, 1).toUpperCase() + n.substr(1);
                        break
                    }
                n || (n = "WebkitOpacity" in l && "webkit"), p = !!("transition" in l || n + "Transition" in l), f = !!("animation" in l || n + "Animation" in l), !i || p && f || (p = y(a.body.style.webkitTransition), f = y(a.body.style.webkitAnimation))
            }
            return {
                history: !(!e.history || !e.history.pushState || 4 > i || s),
                hashchange: "onhashchange" in e && (!c || c > 7),
                hasEvent: function(e) {
                    if ("input" == e && 9 == lr) return !1;
                    if (m(o[e])) {
                        var t = a.createElement("div");
                        o[e] = "on" + e in t
                    }
                    return o[e]
                },
                csp: N(),
                vendorPrefix: n,
                transitions: p,
                animations: f,
                android: i,
                msie: lr,
                msieDocumentMode: c
            }
        }]
    }

    function On() {
        this.$get = ["$rootScope", "$browser", "$q", "$exceptionHandler", function(e, t, n, r) {
            function o(o, s, a) {
                var c, u = n.defer(),
                    l = u.promise,
                    p = v(a) && !a;
                return c = t.defer(function() {
                    try {
                        u.resolve(o())
                    } catch (t) {
                        u.reject(t), r(t)
                    } finally {
                        delete i[l.$$timeoutId]
                    }
                    p || e.$apply()
                }, s), l.$$timeoutId = c, i[c] = u, l
            }
            var i = {};
            return o.cancel = function(e) {
                return e && e.$$timeoutId in i ? (i[e.$$timeoutId].reject("canceled"), delete i[e.$$timeoutId], t.defer.cancel(e.$$timeoutId)) : !1
            }, o
        }]
    }

    function In(e) {
        var t = e;
        return lr && (oo.setAttribute("href", t), t = oo.href), oo.setAttribute("href", t), {
            href: oo.href,
            protocol: oo.protocol ? oo.protocol.replace(/:$/, "") : "",
            host: oo.host,
            search: oo.search ? oo.search.replace(/^\?/, "") : "",
            hash: oo.hash ? oo.hash.replace(/^#/, "") : "",
            hostname: oo.hostname,
            port: oo.port,
            pathname: "/" === oo.pathname.charAt(0) ? oo.pathname : "/" + oo.pathname
        }
    }

    function Pn(e) {
        var t = y(e) ? In(e) : e;
        return t.protocol === io.protocol && t.host === io.host
    }

    function Mn() {
        this.$get = g(e)
    }

    function qn(e) {
        function t(r, o) {
            if (_(r)) {
                var s = {};
                return i(r, function(e, n) {
                    s[n] = t(n, e)
                }), s
            }
            return e.factory(r + n, o)
        }
        var n = "Filter";
        this.register = t, this.$get = ["$injector", function(e) {
            return function(t) {
                return e.get(t + n)
            }
        }], t("currency", Rn), t("date", Wn), t("filter", Nn), t("json", Yn), t("limitTo", Gn), t("lowercase", lo), t("number", Un), t("orderBy", Zn), t("uppercase", po)
    }

    function Nn() {
        return function(e, t, n) {
            if (!x(e)) return e;
            var r = typeof n,
                o = [];
            o.check = function(e) {
                for (var t = 0; t < o.length; t++)
                    if (!o[t](e)) return !1;
                return !0
            }, "function" !== r && (n = "boolean" === r && n ? function(e, t) {
                return _r.equals(e, t)
            } : function(e, t) {
                return t = ("" + t).toLowerCase(), ("" + e).toLowerCase().indexOf(t) > -1
            });
            var i = function(e, t) {
                if ("string" == typeof t && "!" === t.charAt(0)) return !i(e, t.substr(1));
                switch (typeof e) {
                    case "boolean":
                    case "number":
                    case "string":
                        return n(e, t);
                    case "object":
                        switch (typeof t) {
                            case "object":
                                return n(e, t);
                            default:
                                for (var r in e)
                                    if ("$" !== r.charAt(0) && i(e[r], t)) return !0
                        }
                        return !1;
                    case "array":
                        for (var o = 0; o < e.length; o++)
                            if (i(e[o], t)) return !0;
                        return !1;
                    default:
                        return !1
                }
            };
            switch (typeof t) {
                case "boolean":
                case "number":
                case "string":
                    t = {
                        $: t
                    };
                case "object":
                    for (var s in t) ! function(e) {
                        "undefined" != typeof t[e] && o.push(function(n) {
                            return i("$" == e ? n : ot(n, e), t[e])
                        })
                    }(s);
                    break;
                case "function":
                    o.push(t);
                    break;
                default:
                    return e
            }
            for (var a = [], c = 0; c < e.length; c++) {
                var u = e[c];
                o.check(u) && a.push(u)
            }
            return a
        }
    }

    function Rn(e) {
        var t = e.NUMBER_FORMATS;
        return function(e, n) {
            return m(n) && (n = t.CURRENCY_SYM), Hn(e, t.PATTERNS[1], t.GROUP_SEP, t.DECIMAL_SEP, 2).replace(/\u00A4/g, n)
        }
    }

    function Un(e) {
        var t = e.NUMBER_FORMATS;
        return function(e, n) {
            return Hn(e, t.PATTERNS[0], t.GROUP_SEP, t.DECIMAL_SEP, n)
        }
    }

    function Hn(e, t, n, r, o) {
        if (isNaN(e) || !isFinite(e)) return "";
        var i = 0 > e;
        e = Math.abs(e);
        var s = e + "",
            a = "",
            c = [],
            u = !1;
        if (-1 !== s.indexOf("e")) {
            var l = s.match(/([\d\.]+)e(-?)(\d+)/);
            l && "-" == l[2] && l[3] > o + 1 ? s = "0" : (a = s, u = !0)
        }
        if (u) o > 0 && e > -1 && 1 > e && (a = e.toFixed(o));
        else {
            var p = (s.split(so)[1] || "").length;
            m(o) && (o = Math.min(Math.max(t.minFrac, p), t.maxFrac));
            var d = Math.pow(10, o);
            e = Math.round(e * d) / d;
            var f = ("" + e).split(so),
                $ = f[0];
            f = f[1] || "";
            var h, g = 0,
                v = t.lgSize,
                _ = t.gSize;
            if ($.length >= v + _)
                for (g = $.length - v, h = 0; g > h; h++) 0 === (g - h) % _ && 0 !== h && (a += n), a += $.charAt(h);
            for (h = g; h < $.length; h++) 0 === ($.length - h) % v && 0 !== h && (a += n), a += $.charAt(h);
            for (; f.length < o;) f += "0";
            o && "0" !== o && (a += r + f.substr(0, o))
        }
        return c.push(i ? t.negPre : t.posPre), c.push(a), c.push(i ? t.negSuf : t.posSuf), c.join("")
    }

    function Fn(e, t, n) {
        var r = "";
        for (0 > e && (r = "-", e = -e), e = "" + e; e.length < t;) e = "0" + e;
        return n && (e = e.substr(e.length - t)), r + e
    }

    function Vn(e, t, n, r) {
        return n = n || 0,
            function(o) {
                var i = o["get" + e]();
                return (n > 0 || i > -n) && (i += n), 0 === i && -12 == n && (i = 12), Fn(i, t, r)
            }
    }

    function zn(e, t) {
        return function(n, r) {
            var o = n["get" + e](),
                i = ar(t ? "SHORT" + e : e);
            return r[i][o]
        }
    }

    function Ln(e) {
        var t = -1 * e.getTimezoneOffset(),
            n = t >= 0 ? "+" : "";
        return n += Fn(Math[t > 0 ? "floor" : "ceil"](t / 60), 2) + Fn(Math.abs(t % 60), 2)
    }

    function Bn(e, t) {
        return e.getHours() < 12 ? t.AMPMS[0] : t.AMPMS[1]
    }

    function Wn(e) {
        function t(e) {
            var t;
            if (t = e.match(n)) {
                var r = new Date(0),
                    o = 0,
                    i = 0,
                    s = t[8] ? r.setUTCFullYear : r.setFullYear,
                    a = t[8] ? r.setUTCHours : r.setHours;
                t[9] && (o = d(t[9] + t[10]), i = d(t[9] + t[11])), s.call(r, d(t[1]), d(t[2]) - 1, d(t[3]));
                var c = d(t[4] || 0) - o,
                    u = d(t[5] || 0) - i,
                    l = d(t[6] || 0),
                    p = Math.round(1e3 * parseFloat("0." + (t[7] || 0)));
                return a.call(r, c, u, l, p), r
            }
            return e
        }
        var n = /^(\d{4})-?(\d\d)-?(\d\d)(?:T(\d\d)(?::?(\d\d)(?::?(\d\d)(?:\.(\d+))?)?)?(Z|([+-])(\d\d):?(\d\d))?)?$/;
        return function(n, r) {
            var o, s, a = "",
                c = [];
            if (r = r || "mediumDate", r = e.DATETIME_FORMATS[r] || r, y(n) && (n = uo.test(n) ? d(n) : t(n)), b(n) && (n = new Date(n)), !w(n)) return n;
            for (; r;) s = co.exec(r), s ? (c = R(c, s, 1), r = c.pop()) : (c.push(r), r = null);
            return i(c, function(t) {
                o = ao[t], a += o ? o(n, e.DATETIME_FORMATS) : t.replace(/(^'|'$)/g, "").replace(/''/g, "'")
            }), a
        }
    }

    function Yn() {
        return function(e) {
            return V(e, !0)
        }
    }

    function Gn() {
        return function(e, t) {
            if (!x(e) && !y(e)) return e;
            if (t = d(t), y(e)) return t ? t >= 0 ? e.slice(0, t) : e.slice(t, e.length) : "";
            var n, r, o = [];
            for (t > e.length ? t = e.length : t < -e.length && (t = -e.length), t > 0 ? (n = 0, r = t) : (n = e.length + t, r = e.length); r > n; n++) o.push(e[n]);
            return o
        }
    }

    function Zn(e) {
        return function(t, n, r) {
            function o(e, t) {
                for (var r = 0; r < n.length; r++) {
                    var o = n[r](e, t);
                    if (0 !== o) return o
                }
                return 0
            }

            function i(e, t) {
                return L(t) ? function(t, n) {
                    return e(n, t)
                } : e
            }

            function s(e, t) {
                var n = typeof e,
                    r = typeof t;
                return n == r ? ("string" == n && (e = e.toLowerCase(), t = t.toLowerCase()), e === t ? 0 : t > e ? -1 : 1) : r > n ? -1 : 1
            }
            if (!x(t)) return t;
            if (!n) return t;
            n = x(n) ? n : [n], n = D(n, function(t) {
                var n = !1,
                    r = t || h;
                return y(t) && (("+" == t.charAt(0) || "-" == t.charAt(0)) && (n = "-" == t.charAt(0), t = t.substring(1)), r = e(t)), i(function(e, t) {
                    return s(r(e), r(t))
                }, n)
            });
            for (var a = [], c = 0; c < t.length; c++) a.push(t[c]);
            return a.sort(i(o, r))
        }
    }

    function Jn(e) {
        return k(e) && (e = {
            link: e
        }), e.restrict = e.restrict || "AC", g(e)
    }

    function Qn(e, t) {
        function n(t, n) {
            n = n ? "-" + X(n, "-") : "", e.removeClass((t ? Co : ko) + n).addClass((t ? ko : Co) + n)
        }
        var r = this,
            o = e.parent().controller("form") || ho,
            s = 0,
            a = r.$error = {},
            c = [];
        r.$name = t.name || t.ngForm, r.$dirty = !1, r.$pristine = !0, r.$valid = !0, r.$invalid = !1, o.$addControl(r), e.addClass(So), n(!0), r.$addControl = function(e) {
            rt(e.$name, "input"), c.push(e), e.$name && (r[e.$name] = e)
        }, r.$removeControl = function(e) {
            e.$name && r[e.$name] === e && delete r[e.$name], i(a, function(t, n) {
                r.$setValidity(n, !0, e)
            }), I(c, e)
        }, r.$setValidity = function(e, t, i) {
            var c = a[e];
            if (t) c && (I(c, i), c.length || (s--, s || (n(t), r.$valid = !0, r.$invalid = !1), a[e] = !1, n(!0, e), o.$setValidity(e, !0, r)));
            else {
                if (s || n(t), c) {
                    if (j(c, i)) return
                } else a[e] = c = [], s++, n(!1, e), o.$setValidity(e, !1, r);
                c.push(i), r.$valid = !1, r.$invalid = !0
            }
        }, r.$setDirty = function() {
            e.removeClass(So).addClass(To), r.$dirty = !0, r.$pristine = !1, o.$setDirty()
        }, r.$setPristine = function() {
            e.removeClass(To).addClass(So), r.$dirty = !1, r.$pristine = !0, i(c, function(e) {
                e.$setPristine()
            })
        }
    }

    function Kn(e, t, r, o) {
        return e.$setValidity(t, r), r ? o : n
    }

    function Xn(e, t, n, o, i, s) {
        if (!i.android) {
            var a = !1;
            t.on("compositionstart", function() {
                a = !0
            }), t.on("compositionend", function() {
                a = !1
            })
        }
        var c = function() {
            if (!a) {
                var r = t.val();
                L(n.ngTrim || "T") && (r = br(r)), o.$viewValue !== r && (e.$$phase ? o.$setViewValue(r) : e.$apply(function() {
                    o.$setViewValue(r)
                }))
            }
        };
        if (i.hasEvent("input")) t.on("input", c);
        else {
            var u, l = function() {
                u || (u = s.defer(function() {
                    c(), u = null
                }))
            };
            t.on("keydown", function(e) {
                var t = e.keyCode;
                91 === t || t > 15 && 19 > t || t >= 37 && 40 >= t || l()
            }), i.hasEvent("paste") && t.on("paste cut", l)
        }
        t.on("change", c), o.$render = function() {
            t.val(o.$isEmpty(o.$viewValue) ? "" : o.$viewValue)
        };
        var p, f, $ = n.ngPattern;
        if ($) {
            var h = function(e, t) {
                return Kn(o, "pattern", o.$isEmpty(t) || e.test(t), t)
            };
            f = $.match(/^\/(.*)\/([gim]*)$/), f ? ($ = new RegExp(f[1], f[2]), p = function(e) {
                return h($, e)
            }) : p = function(n) {
                var o = e.$eval($);
                if (!o || !o.test) throw r("ngPattern")("noregexp", "Expected {0} to be a RegExp but was {1}. Element: {2}", $, o, B(t));
                return h(o, n)
            }, o.$formatters.push(p), o.$parsers.push(p)
        }
        if (n.ngMinlength) {
            var g = d(n.ngMinlength),
                m = function(e) {
                    return Kn(o, "minlength", o.$isEmpty(e) || e.length >= g, e)
                };
            o.$parsers.push(m), o.$formatters.push(m)
        }
        if (n.ngMaxlength) {
            var v = d(n.ngMaxlength),
                _ = function(e) {
                    return Kn(o, "maxlength", o.$isEmpty(e) || e.length <= v, e)
                };
            o.$parsers.push(_), o.$formatters.push(_)
        }
    }

    function er(e, t, r, o, i, s) {
        if (Xn(e, t, r, o, i, s), o.$parsers.push(function(e) {
                var t = o.$isEmpty(e);
                return t || bo.test(e) ? (o.$setValidity("number", !0), "" === e ? null : t ? e : parseFloat(e)) : (o.$setValidity("number", !1), n)
            }), o.$formatters.push(function(e) {
                return o.$isEmpty(e) ? "" : "" + e
            }), r.min) {
            var a = function(e) {
                var t = parseFloat(r.min);
                return Kn(o, "min", o.$isEmpty(e) || e >= t, e)
            };
            o.$parsers.push(a), o.$formatters.push(a)
        }
        if (r.max) {
            var c = function(e) {
                var t = parseFloat(r.max);
                return Kn(o, "max", o.$isEmpty(e) || t >= e, e)
            };
            o.$parsers.push(c), o.$formatters.push(c)
        }
        o.$formatters.push(function(e) {
            return Kn(o, "number", o.$isEmpty(e) || b(e), e)
        })
    }

    function tr(e, t, n, r, o, i) {
        Xn(e, t, n, r, o, i);
        var s = function(e) {
            return Kn(r, "url", r.$isEmpty(e) || _o.test(e), e)
        };
        r.$formatters.push(s), r.$parsers.push(s)
    }

    function nr(e, t, n, r, o, i) {
        Xn(e, t, n, r, o, i);
        var s = function(e) {
            return Kn(r, "email", r.$isEmpty(e) || yo.test(e), e)
        };
        r.$formatters.push(s), r.$parsers.push(s)
    }

    function rr(e, t, n, r) {
        m(n.name) && t.attr("name", u()), t.on("click", function() {
            t[0].checked && e.$apply(function() {
                r.$setViewValue(n.value)
            })
        }), r.$render = function() {
            var e = n.value;
            t[0].checked = e == r.$viewValue
        }, n.$observe("value", r.$render)
    }

    function or(e, t, n, r) {
        var o = n.ngTrueValue,
            i = n.ngFalseValue;
        y(o) || (o = !0), y(i) || (i = !1), t.on("click", function() {
            e.$apply(function() {
                r.$setViewValue(t[0].checked)
            })
        }), r.$render = function() {
            t[0].checked = r.$viewValue
        }, r.$isEmpty = function(e) {
            return e !== o
        }, r.$formatters.push(function(e) {
            return e === o
        }), r.$parsers.push(function(e) {
            return e ? o : i
        })
    }

    function ir(e, t) {
        return e = "ngClass" + e,
            function() {
                return {
                    restrict: "AC",
                    link: function(n, r, o) {
                        function s(e) {
                            if (t === !0 || n.$index % 2 === t) {
                                var r = a(e || "");
                                c ? q(e, c) || o.$updateClass(r, a(c)) : o.$addClass(r)
                            }
                            c = P(e)
                        }

                        function a(e) {
                            if (x(e)) return e.join(" ");
                            if (_(e)) {
                                var t = [];
                                return i(e, function(e, n) {
                                    e && t.push(n)
                                }), t.join(" ")
                            }
                            return e
                        }
                        var c;
                        n.$watch(o[e], s, !0), o.$observe("class", function() {
                            s(n.$eval(o[e]))
                        }), "ngClass" !== e && n.$watch("$index", function(r, i) {
                            var s = 1 & r;
                            if (1 & s !== i) {
                                var c = a(n.$eval(o[e]));
                                s === t ? o.$addClass(c) : o.$removeClass(c)
                            }
                        })
                    }
                }
            }
    }
    var sr = function(e) {
            return y(e) ? e.toLowerCase() : e
        },
        ar = function(e) {
            return y(e) ? e.toUpperCase() : e
        },
        cr = function(e) {
            return y(e) ? e.replace(/[A-Z]/g, function(e) {
                return String.fromCharCode(32 | e.charCodeAt(0))
            }) : e
        },
        ur = function(e) {
            return y(e) ? e.replace(/[a-z]/g, function(e) {
                return String.fromCharCode(-33 & e.charCodeAt(0))
            }) : e
        };
    "i" !== "I".toLowerCase() && (sr = cr, ar = ur);
    var lr, pr, dr, fr, $r, hr = [].slice,
        gr = [].push,
        mr = Object.prototype.toString,
        vr = r("ng"),
        _r = (e.angular, e.angular || (e.angular = {})),
        yr = ["0", "0", "0"];
    lr = d((/msie (\d+)/.exec(sr(navigator.userAgent)) || [])[1]), isNaN(lr) && (lr = d((/trident\/.*; rv:(\d+)/.exec(sr(navigator.userAgent)) || [])[1])), $.$inject = [], h.$inject = [];
    var br = function() {
        return String.prototype.trim ? function(e) {
            return y(e) ? e.trim() : e
        } : function(e) {
            return y(e) ? e.replace(/^\s\s*/, "").replace(/\s\s*$/, "") : e
        }
    }();
    $r = 9 > lr ? function(e) {
        return e = e.nodeName ? e : e[0], e.scopeName && "HTML" != e.scopeName ? ar(e.scopeName + ":" + e.nodeName) : e.nodeName
    } : function(e) {
        return e.nodeName ? e.nodeName : e[0].nodeName
    };
    var wr = /[A-Z]/g,
        xr = {
            full: "1.2.9",
            major: 1,
            minor: 2,
            dot: 9,
            codeName: "enchanted-articulacy"
        },
        kr = pt.cache = {},
        Cr = pt.expando = "ng-" + (new Date).getTime(),
        Sr = 1,
        Tr = e.document.addEventListener ? function(e, t, n) {
            e.addEventListener(t, n, !1)
        } : function(e, t, n) {
            e.attachEvent("on" + t, n)
        },
        Ar = e.document.removeEventListener ? function(e, t, n) {
            e.removeEventListener(t, n, !1)
        } : function(e, t, n) {
            e.detachEvent("on" + t, n)
        },
        Er = /([\:\-\_]+(.))/g,
        Dr = /^moz([A-Z])/,
        jr = r("jqLite"),
        Or = pt.prototype = {
            ready: function(n) {
                function r() {
                    o || (o = !0, n())
                }
                var o = !1;
                "complete" === t.readyState ? setTimeout(r) : (this.on("DOMContentLoaded", r), pt(e).on("load", r))
            },
            toString: function() {
                var e = [];
                return i(this, function(t) {
                    e.push("" + t)
                }), "[" + e.join(", ") + "]"
            },
            eq: function(e) {
                return e >= 0 ? pr(this[e]) : pr(this[this.length + e])
            },
            length: 0,
            push: gr,
            sort: [].sort,
            splice: [].splice
        },
        Ir = {};
    i("multiple,selected,checked,disabled,readOnly,required,open".split(","), function(e) {
        Ir[sr(e)] = e
    });
    var Pr = {};
    i("input,select,option,textarea,button,form,details".split(","), function(e) {
        Pr[ar(e)] = !0
    }), i({
        data: mt,
        inheritedData: xt,
        scope: function(e) {
            return pr(e).data("$scope") || xt(e.parentNode || e, ["$isolateScope", "$scope"])
        },
        isolateScope: function(e) {
            return pr(e).data("$isolateScope") || pr(e).data("$isolateScopeNoTemplate")
        },
        controller: wt,
        injector: function(e) {
            return xt(e, "$injector")
        },
        removeAttr: function(e, t) {
            e.removeAttribute(t)
        },
        hasClass: vt,
        css: function(e, t, r) {
            if (t = ut(t), !v(r)) {
                var o;
                return 8 >= lr && (o = e.currentStyle && e.currentStyle[t], "" === o && (o = "auto")), o = o || e.style[t], 8 >= lr && (o = "" === o ? n : o), o
            }
            e.style[t] = r
        },
        attr: function(e, t, r) {
            var o = sr(t);
            if (Ir[o]) {
                if (!v(r)) return e[t] || (e.attributes.getNamedItem(t) || $).specified ? o : n;
                r ? (e[t] = !0, e.setAttribute(t, o)) : (e[t] = !1, e.removeAttribute(o))
            } else if (v(r)) e.setAttribute(t, r);
            else if (e.getAttribute) {
                var i = e.getAttribute(t, 2);
                return null === i ? n : i
            }
        },
        prop: function(e, t, n) {
            return v(n) ? (e[t] = n, void 0) : e[t]
        },
        text: function() {
            function e(e, n) {
                var r = t[e.nodeType];
                return m(n) ? r ? e[r] : "" : (e[r] = n, void 0)
            }
            var t = [];
            return 9 > lr ? (t[1] = "innerText", t[3] = "nodeValue") : t[1] = t[3] = "textContent", e.$dv = "", e
        }(),
        val: function(e, t) {
            if (m(t)) {
                if ("SELECT" === $r(e) && e.multiple) {
                    var n = [];
                    return i(e.options, function(e) {
                        e.selected && n.push(e.value || e.text)
                    }), 0 === n.length ? null : n
                }
                return e.value
            }
            e.value = t
        },
        html: function(e, t) {
            if (m(t)) return e.innerHTML;
            for (var n = 0, r = e.childNodes; n < r.length; n++) ft(r[n]);
            e.innerHTML = t
        },
        empty: kt
    }, function(e, t) {
        pt.prototype[t] = function(t, r) {
            var o, i;
            if (e !== kt && (2 == e.length && e !== vt && e !== wt ? t : r) === n) {
                if (_(t)) {
                    for (o = 0; o < this.length; o++)
                        if (e === mt) e(this[o], t);
                        else
                            for (i in t) e(this[o], i, t[i]);
                    return this
                }
                for (var s = e.$dv, a = s === n ? Math.min(this.length, 1) : this.length, c = 0; a > c; c++) {
                    var u = e(this[c], t, r);
                    s = s ? s + u : u
                }
                return s
            }
            for (o = 0; o < this.length; o++) e(this[o], t, r);
            return this
        }
    }), i({
        removeData: ht,
        dealoc: ft,
        on: function li(e, n, r, o) {
            if (v(o)) throw jr("onargs", "jqLite#on() does not support the `selector` or `eventData` parameters");
            var s = gt(e, "events"),
                a = gt(e, "handle");
            s || gt(e, "events", s = {}), a || gt(e, "handle", a = St(e, s)), i(n.split(" "), function(n) {
                var o = s[n];
                if (!o) {
                    if ("mouseenter" == n || "mouseleave" == n) {
                        var i = t.body.contains || t.body.compareDocumentPosition ? function(e, t) {
                            var n = 9 === e.nodeType ? e.documentElement : e,
                                r = t && t.parentNode;
                            return e === r || !(!r || 1 !== r.nodeType || !(n.contains ? n.contains(r) : e.compareDocumentPosition && 16 & e.compareDocumentPosition(r)))
                        } : function(e, t) {
                            if (t)
                                for (; t = t.parentNode;)
                                    if (t === e) return !0;
                            return !1
                        };
                        s[n] = [];
                        var c = {
                            mouseleave: "mouseout",
                            mouseenter: "mouseover"
                        };
                        li(e, c[n], function(e) {
                            var t = this,
                                r = e.relatedTarget;
                            (!r || r !== t && !i(t, r)) && a(e, n)
                        })
                    } else Tr(e, n, a), s[n] = [];
                    o = s[n]
                }
                o.push(r)
            })
        },
        off: $t,
        one: function(e, t, n) {
            e = pr(e), e.on(t, function r() {
                e.off(t, n), e.off(t, r)
            }), e.on(t, n)
        },
        replaceWith: function(e, t) {
            var n, r = e.parentNode;
            ft(e), i(new pt(t), function(t) {
                n ? r.insertBefore(t, n.nextSibling) : r.replaceChild(t, e), n = t
            })
        },
        children: function(e) {
            var t = [];
            return i(e.childNodes, function(e) {
                1 === e.nodeType && t.push(e)
            }), t
        },
        contents: function(e) {
            return e.childNodes || []
        },
        append: function(e, t) {
            i(new pt(t), function(t) {
                (1 === e.nodeType || 11 === e.nodeType) && e.appendChild(t)
            })
        },
        prepend: function(e, t) {
            if (1 === e.nodeType) {
                var n = e.firstChild;
                i(new pt(t), function(t) {
                    e.insertBefore(t, n)
                })
            }
        },
        wrap: function(e, t) {
            t = pr(t)[0];
            var n = e.parentNode;
            n && n.replaceChild(t, e), t.appendChild(e)
        },
        remove: function(e) {
            ft(e);
            var t = e.parentNode;
            t && t.removeChild(e)
        },
        after: function(e, t) {
            var n = e,
                r = e.parentNode;
            i(new pt(t), function(e) {
                r.insertBefore(e, n.nextSibling), n = e
            })
        },
        addClass: yt,
        removeClass: _t,
        toggleClass: function(e, t, n) {
            m(n) && (n = !vt(e, t)), (n ? yt : _t)(e, t)
        },
        parent: function(e) {
            var t = e.parentNode;
            return t && 11 !== t.nodeType ? t : null
        },
        next: function(e) {
            if (e.nextElementSibling) return e.nextElementSibling;
            for (var t = e.nextSibling; null != t && 1 !== t.nodeType;) t = t.nextSibling;
            return t
        },
        find: function(e, t) {
            return e.getElementsByTagName ? e.getElementsByTagName(t) : []
        },
        clone: dt,
        triggerHandler: function(e, t, n) {
            var r = (gt(e, "events") || {})[t];
            n = n || [];
            var o = [{
                preventDefault: $,
                stopPropagation: $
            }];
            i(r, function(t) {
                t.apply(e, o.concat(n))
            })
        }
    }, function(e, t) {
        pt.prototype[t] = function(t, n, r) {
            for (var o, i = 0; i < this.length; i++) m(o) ? (o = e(this[i], t, n, r), v(o) && (o = pr(o))) : bt(o, e(this[i], t, n, r));
            return v(o) ? o : this
        }, pt.prototype.bind = pt.prototype.on, pt.prototype.unbind = pt.prototype.off
    }), At.prototype = {
        put: function(e, t) {
            this[Tt(e)] = t
        },
        get: function(e) {
            return this[Tt(e)]
        },
        remove: function(e) {
            var t = this[e = Tt(e)];
            return delete this[e], t
        }
    };
    var Mr = /^function\s*[^\(]*\(\s*([^\)]*)\)/m,
        qr = /,/,
        Nr = /^\s*(_?)(\S+?)\1\s*$/,
        Rr = /((\/\/.*$)|(\/\*[\s\S]*?\*\/))/gm,
        Ur = r("$injector"),
        Hr = r("$animate"),
        Fr = ["$provide", function(e) {
            this.$$selectors = {}, this.register = function(t, n) {
                var r = t + "-animation";
                if (t && "." != t.charAt(0)) throw Hr("notcsel", "Expecting class selector starting with '.' got '{0}'.", t);
                this.$$selectors[t.substr(1)] = r, e.factory(r, n)
            }, this.classNameFilter = function(e) {
                return 1 === arguments.length && (this.$$classNameFilter = e instanceof RegExp ? e : null), this.$$classNameFilter
            }, this.$get = ["$timeout", function(e) {
                return {
                    enter: function(t, n, r, o) {
                        r ? r.after(t) : (n && n[0] || (n = r.parent()), n.append(t)), o && e(o, 0, !1)
                    },
                    leave: function(t, n) {
                        t.remove(), n && e(n, 0, !1)
                    },
                    move: function(e, t, n, r) {
                        this.enter(e, t, n, r)
                    },
                    addClass: function(t, n, r) {
                        n = y(n) ? n : x(n) ? n.join(" ") : "", i(t, function(e) {
                            yt(e, n)
                        }), r && e(r, 0, !1)
                    },
                    removeClass: function(t, n, r) {
                        n = y(n) ? n : x(n) ? n.join(" ") : "", i(t, function(e) {
                            _t(e, n)
                        }), r && e(r, 0, !1)
                    },
                    enabled: $
                }
            }]
        }],
        Vr = r("$compile");
    qt.$inject = ["$provide", "$$sanitizeUriProvider"];
    var zr = /^(x[\:\-_]|data[\:\-_])/i,
        Lr = r("$interpolate"),
        Br = /^([^\?#]*)(\?([^#]*))?(#(.*))?$/,
        Wr = {
            http: 80,
            https: 443,
            ftp: 21
        },
        Yr = r("$location");
    un.prototype = cn.prototype = an.prototype = {
        $$html5: !1,
        $$replace: !1,
        absUrl: ln("$$absUrl"),
        url: function(e, t) {
            if (m(e)) return this.$$url;
            var n = Br.exec(e);
            return n[1] && this.path(decodeURIComponent(n[1])), (n[2] || n[1]) && this.search(n[3] || ""), this.hash(n[5] || "", t), this
        },
        protocol: ln("$$protocol"),
        host: ln("$$host"),
        port: ln("$$port"),
        path: pn("$$path", function(e) {
            return "/" == e.charAt(0) ? e : "/" + e
        }),
        search: function(e, t) {
            switch (arguments.length) {
                case 0:
                    return this.$$search;
                case 1:
                    if (y(e)) this.$$search = Y(e);
                    else {
                        if (!_(e)) throw Yr("isrcharg", "The first argument of the `$location#search()` call must be a string or an object.");
                        this.$$search = e
                    }
                    break;
                default:
                    m(t) || null === t ? delete this.$$search[e] : this.$$search[e] = t
            }
            return this.$$compose(), this
        },
        hash: pn("$$hash", h),
        replace: function() {
            return this.$$replace = !0, this
        }
    };
    var Gr, Zr = r("$parse"),
        Jr = {},
        Qr = {
            "null": function() {
                return null
            },
            "true": function() {
                return !0
            },
            "false": function() {
                return !1
            },
            undefined: $,
            "+": function(e, t, r, o) {
                return r = r(e, t), o = o(e, t), v(r) ? v(o) ? r + o : r : v(o) ? o : n
            },
            "-": function(e, t, n, r) {
                return n = n(e, t), r = r(e, t), (v(n) ? n : 0) - (v(r) ? r : 0)
            },
            "*": function(e, t, n, r) {
                return n(e, t) * r(e, t)
            },
            "/": function(e, t, n, r) {
                return n(e, t) / r(e, t)
            },
            "%": function(e, t, n, r) {
                return n(e, t) % r(e, t)
            },
            "^": function(e, t, n, r) {
                return n(e, t) ^ r(e, t)
            },
            "=": $,
            "===": function(e, t, n, r) {
                return n(e, t) === r(e, t)
            },
            "!==": function(e, t, n, r) {
                return n(e, t) !== r(e, t)
            },
            "==": function(e, t, n, r) {
                return n(e, t) == r(e, t)
            },
            "!=": function(e, t, n, r) {
                return n(e, t) != r(e, t)
            },
            "<": function(e, t, n, r) {
                return n(e, t) < r(e, t)
            },
            ">": function(e, t, n, r) {
                return n(e, t) > r(e, t)
            },
            "<=": function(e, t, n, r) {
                return n(e, t) <= r(e, t)
            },
            ">=": function(e, t, n, r) {
                return n(e, t) >= r(e, t)
            },
            "&&": function(e, t, n, r) {
                return n(e, t) && r(e, t)
            },
            "||": function(e, t, n, r) {
                return n(e, t) || r(e, t)
            },
            "&": function(e, t, n, r) {
                return n(e, t) & r(e, t)
            },
            "|": function(e, t, n, r) {
                return r(e, t)(e, t, n(e, t))
            },
            "!": function(e, t, n) {
                return !n(e, t)
            }
        },
        Kr = {
            n: "\n",
            f: "\f",
            r: "\r",
            t: "  ",
            v: "",
            "'": "'",
            '"': '"'
        },
        Xr = function(e) {
            this.options = e
        };
    Xr.prototype = {
        constructor: Xr,
        lex: function(e) {
            this.text = e, this.index = 0, this.ch = n, this.lastCh = ":", this.tokens = [];
            for (var t, r = []; this.index < this.text.length;) {
                if (this.ch = this.text.charAt(this.index), this.is("\"'")) this.readString(this.ch);
                else if (this.isNumber(this.ch) || this.is(".") && this.isNumber(this.peek())) this.readNumber();
                else if (this.isIdent(this.ch)) this.readIdent(), this.was("{,") && "{" === r[0] && (t = this.tokens[this.tokens.length - 1]) && (t.json = -1 === t.text.indexOf("."));
                else if (this.is("(){}[].,;:?")) this.tokens.push({
                    index: this.index,
                    text: this.ch,
                    json: this.was(":[,") && this.is("{[") || this.is("}]:,")
                }), this.is("{[") && r.unshift(this.ch), this.is("}]") && r.shift(), this.index++;
                else {
                    if (this.isWhitespace(this.ch)) {
                        this.index++;
                        continue
                    }
                    var o = this.ch + this.peek(),
                        i = o + this.peek(2),
                        s = Qr[this.ch],
                        a = Qr[o],
                        c = Qr[i];
                    c ? (this.tokens.push({
                        index: this.index,
                        text: i,
                        fn: c
                    }), this.index += 3) : a ? (this.tokens.push({
                        index: this.index,
                        text: o,
                        fn: a
                    }), this.index += 2) : s ? (this.tokens.push({
                        index: this.index,
                        text: this.ch,
                        fn: s,
                        json: this.was("[,:") && this.is("+-")
                    }), this.index += 1) : this.throwError("Unexpected next character ", this.index, this.index + 1)
                }
                this.lastCh = this.ch
            }
            return this.tokens
        },
        is: function(e) {
            return -1 !== e.indexOf(this.ch)
        },
        was: function(e) {
            return -1 !== e.indexOf(this.lastCh)
        },
        peek: function(e) {
            var t = e || 1;
            return this.index + t < this.text.length ? this.text.charAt(this.index + t) : !1
        },
        isNumber: function(e) {
            return e >= "0" && "9" >= e
        },
        isWhitespace: function(e) {
            return " " === e || "\r" === e || "   " === e || "\n" === e || "" === e || "聽" === e
        },
        isIdent: function(e) {
            return e >= "a" && "z" >= e || e >= "A" && "Z" >= e || "_" === e || "$" === e
        },
        isExpOperator: function(e) {
            return "-" === e || "+" === e || this.isNumber(e)
        },
        throwError: function(e, t, n) {
            n = n || this.index;
            var r = v(t) ? "s " + t + "-" + this.index + " [" + this.text.substring(t, n) + "]" : " " + n;
            throw Zr("lexerr", "Lexer Error: {0} at column{1} in expression [{2}].", e, r, this.text)
        },
        readNumber: function() {
            for (var e = "", t = this.index; this.index < this.text.length;) {
                var n = sr(this.text.charAt(this.index));
                if ("." == n || this.isNumber(n)) e += n;
                else {
                    var r = this.peek();
                    if ("e" == n && this.isExpOperator(r)) e += n;
                    else if (this.isExpOperator(n) && r && this.isNumber(r) && "e" == e.charAt(e.length - 1)) e += n;
                    else {
                        if (!this.isExpOperator(n) || r && this.isNumber(r) || "e" != e.charAt(e.length - 1)) break;
                        this.throwError("Invalid exponent")
                    }
                }
                this.index++
            }
            e = 1 * e, this.tokens.push({
                index: t,
                text: e,
                json: !0,
                fn: function() {
                    return e
                }
            })
        },
        readIdent: function() {
            for (var e, t, n, r, o = this, i = "", s = this.index; this.index < this.text.length && (r = this.text.charAt(this.index), "." === r || this.isIdent(r) || this.isNumber(r));) "." === r && (e = this.index), i += r, this.index++;
            if (e)
                for (t = this.index; t < this.text.length;) {
                    if (r = this.text.charAt(t), "(" === r) {
                        n = i.substr(e - s + 1), i = i.substr(0, e - s), this.index = t;
                        break
                    }
                    if (!this.isWhitespace(r)) break;
                    t++
                }
            var a = {
                index: s,
                text: i
            };
            if (Qr.hasOwnProperty(i)) a.fn = Qr[i], a.json = Qr[i];
            else {
                var c = yn(i, this.options, this.text);
                a.fn = p(function(e, t) {
                    return c(e, t)
                }, {
                    assign: function(e, t) {
                        return gn(e, i, t, o.text, o.options)
                    }
                })
            }
            this.tokens.push(a), n && (this.tokens.push({
                index: e,
                text: ".",
                json: !1
            }), this.tokens.push({
                index: e + 1,
                text: n,
                json: !1
            }))
        },
        readString: function(e) {
            var t = this.index;
            this.index++;
            for (var n = "", r = e, o = !1; this.index < this.text.length;) {
                var i = this.text.charAt(this.index);
                if (r += i, o) {
                    if ("u" === i) {
                        var s = this.text.substring(this.index + 1, this.index + 5);
                        s.match(/[\da-f]{4}/i) || this.throwError("Invalid unicode escape [\\u" + s + "]"), this.index += 4, n += String.fromCharCode(parseInt(s, 16))
                    } else {
                        var a = Kr[i];
                        n += a ? a : i
                    }
                    o = !1
                } else if ("\\" === i) o = !0;
                else {
                    if (i === e) return this.index++, this.tokens.push({
                        index: t,
                        text: r,
                        string: n,
                        json: !0,
                        fn: function() {
                            return n
                        }
                    }), void 0;
                    n += i
                }
                this.index++
            }
            this.throwError("Unterminated quote", t)
        }
    };
    var eo = function(e, t, n) {
        this.lexer = e, this.$filter = t, this.options = n
    };
    eo.ZERO = function() {
        return 0
    }, eo.prototype = {
        constructor: eo,
        parse: function(e, t) {
            this.text = e, this.json = t, this.tokens = this.lexer.lex(e), t && (this.assignment = this.logicalOR, this.functionCall = this.fieldAccess = this.objectIndex = this.filterChain = function() {
                this.throwError("is not valid json", {
                    text: e,
                    index: 0
                })
            });
            var n = t ? this.primary() : this.statements();
            return 0 !== this.tokens.length && this.throwError("is an unexpected token", this.tokens[0]), n.literal = !!n.literal, n.constant = !!n.constant, n
        },
        primary: function() {
            var e;
            if (this.expect("(")) e = this.filterChain(), this.consume(")");
            else if (this.expect("[")) e = this.arrayDeclaration();
            else if (this.expect("{")) e = this.object();
            else {
                var t = this.expect();
                e = t.fn, e || this.throwError("not a primary expression", t), t.json && (e.constant = !0, e.literal = !0)
            }
            for (var n, r; n = this.expect("(", "[", ".");) "(" === n.text ? (e = this.functionCall(e, r), r = null) : "[" === n.text ? (r = e, e = this.objectIndex(e)) : "." === n.text ? (r = e, e = this.fieldAccess(e)) : this.throwError("IMPOSSIBLE");
            return e
        },
        throwError: function(e, t) {
            throw Zr("syntax", "Syntax Error: Token '{0}' {1} at column {2} of the expression [{3}] starting at [{4}].", t.text, e, t.index + 1, this.text, this.text.substring(t.index))
        },
        peekToken: function() {
            if (0 === this.tokens.length) throw Zr("ueoe", "Unexpected end of expression: {0}", this.text);
            return this.tokens[0]
        },
        peek: function(e, t, n, r) {
            if (this.tokens.length > 0) {
                var o = this.tokens[0],
                    i = o.text;
                if (i === e || i === t || i === n || i === r || !e && !t && !n && !r) return o
            }
            return !1
        },
        expect: function(e, t, n, r) {
            var o = this.peek(e, t, n, r);
            return o ? (this.json && !o.json && this.throwError("is not valid json", o), this.tokens.shift(), o) : !1
        },
        consume: function(e) {
            this.expect(e) || this.throwError("is unexpected, expecting [" + e + "]", this.peek())
        },
        unaryFn: function(e, t) {
            return p(function(n, r) {
                return e(n, r, t)
            }, {
                constant: t.constant
            })
        },
        ternaryFn: function(e, t, n) {
            return p(function(r, o) {
                return e(r, o) ? t(r, o) : n(r, o)
            }, {
                constant: e.constant && t.constant && n.constant
            })
        },
        binaryFn: function(e, t, n) {
            return p(function(r, o) {
                return t(r, o, e, n)
            }, {
                constant: e.constant && n.constant
            })
        },
        statements: function() {
            for (var e = [];;)
                if (this.tokens.length > 0 && !this.peek("}", ")", ";", "]") && e.push(this.filterChain()), !this.expect(";")) return 1 === e.length ? e[0] : function(t, n) {
                    for (var r, o = 0; o < e.length; o++) {
                        var i = e[o];
                        i && (r = i(t, n))
                    }
                    return r
                }
        },
        filterChain: function() {
            for (var e, t = this.expression();;) {
                if (!(e = this.expect("|"))) return t;
                t = this.binaryFn(t, e.fn, this.filter())
            }
        },
        filter: function() {
            for (var e = this.expect(), t = this.$filter(e.text), n = [];;) {
                if (!(e = this.expect(":"))) {
                    var r = function(e, r, o) {
                        for (var i = [o], s = 0; s < n.length; s++) i.push(n[s](e, r));
                        return t.apply(e, i)
                    };
                    return function() {
                        return r
                    }
                }
                n.push(this.expression())
            }
        },
        expression: function() {
            return this.assignment()
        },
        assignment: function() {
            var e, t, n = this.ternary();
            return (t = this.expect("=")) ? (n.assign || this.throwError("implies assignment but [" + this.text.substring(0, t.index) + "] can not be assigned to", t), e = this.ternary(), function(t, r) {
                return n.assign(t, e(t, r), r)
            }) : n
        },
        ternary: function() {
            var e, t, n = this.logicalOR();
            return (t = this.expect("?")) ? (e = this.ternary(), (t = this.expect(":")) ? this.ternaryFn(n, e, this.ternary()) : (this.throwError("expected :", t), void 0)) : n
        },
        logicalOR: function() {
            for (var e, t = this.logicalAND();;) {
                if (!(e = this.expect("||"))) return t;
                t = this.binaryFn(t, e.fn, this.logicalAND())
            }
        },
        logicalAND: function() {
            var e, t = this.equality();
            return (e = this.expect("&&")) && (t = this.binaryFn(t, e.fn, this.logicalAND())), t
        },
        equality: function() {
            var e, t = this.relational();
            return (e = this.expect("==", "!=", "===", "!==")) && (t = this.binaryFn(t, e.fn, this.equality())), t
        },
        relational: function() {
            var e, t = this.additive();
            return (e = this.expect("<", ">", "<=", ">=")) && (t = this.binaryFn(t, e.fn, this.relational())), t
        },
        additive: function() {
            for (var e, t = this.multiplicative(); e = this.expect("+", "-");) t = this.binaryFn(t, e.fn, this.multiplicative());
            return t
        },
        multiplicative: function() {
            for (var e, t = this.unary(); e = this.expect("*", "/", "%");) t = this.binaryFn(t, e.fn, this.unary());
            return t
        },
        unary: function() {
            var e;
            return this.expect("+") ? this.primary() : (e = this.expect("-")) ? this.binaryFn(eo.ZERO, e.fn, this.unary()) : (e = this.expect("!")) ? this.unaryFn(e.fn, this.unary()) : this.primary()
        },
        fieldAccess: function(e) {
            var t = this,
                n = this.expect().text,
                r = yn(n, this.options, this.text);
            return p(function(t, n, o) {
                return r(o || e(t, n), n)
            }, {
                assign: function(r, o, i) {
                    return gn(e(r, i), n, o, t.text, t.options)
                }
            })
        },
        objectIndex: function(e) {
            var t = this,
                r = this.expression();
            return this.consume("]"), p(function(o, i) {
                var s, a, c = e(o, i),
                    u = r(o, i);
                return c ? (s = hn(c[u], t.text), s && s.then && t.options.unwrapPromises && (a = s, "$$v" in s || (a.$$v = n, a.then(function(e) {
                    a.$$v = e
                })), s = s.$$v), s) : n
            }, {
                assign: function(n, o, i) {
                    var s = r(n, i),
                        a = hn(e(n, i), t.text);
                    return a[s] = o
                }
            })
        },
        functionCall: function(e, t) {
            var n = [];
            if (")" !== this.peekToken().text)
                do n.push(this.expression()); while (this.expect(","));
            this.consume(")");
            var r = this;
            return function(o, i) {
                for (var s = [], a = t ? t(o, i) : o, c = 0; c < n.length; c++) s.push(n[c](o, i));
                var u = e(o, i, a) || $;
                hn(a, r.text), hn(u, r.text);
                var l = u.apply ? u.apply(a, s) : u(s[0], s[1], s[2], s[3], s[4]);
                return hn(l, r.text)
            }
        },
        arrayDeclaration: function() {
            var e = [],
                t = !0;
            if ("]" !== this.peekToken().text)
                do {
                    var n = this.expression();
                    e.push(n), n.constant || (t = !1)
                } while (this.expect(","));
            return this.consume("]"), p(function(t, n) {
                for (var r = [], o = 0; o < e.length; o++) r.push(e[o](t, n));
                return r
            }, {
                literal: !0,
                constant: t
            })
        },
        object: function() {
            var e = [],
                t = !0;
            if ("}" !== this.peekToken().text)
                do {
                    var n = this.expect(),
                        r = n.string || n.text;
                    this.consume(":");
                    var o = this.expression();
                    e.push({
                        key: r,
                        value: o
                    }), o.constant || (t = !1)
                } while (this.expect(","));
            return this.consume("}"), p(function(t, n) {
                for (var r = {}, o = 0; o < e.length; o++) {
                    var i = e[o];
                    r[i.key] = i.value(t, n)
                }
                return r
            }, {
                literal: !0,
                constant: t
            })
        }
    };
    var to = {},
        no = r("$sce"),
        ro = {
            HTML: "html",
            CSS: "css",
            URL: "url",
            RESOURCE_URL: "resourceUrl",
            JS: "js"
        },
        oo = t.createElement("a"),
        io = In(e.location.href, !0);
    qn.$inject = ["$provide"], Rn.$inject = ["$locale"], Un.$inject = ["$locale"];
    var so = ".",
        ao = {
            yyyy: Vn("FullYear", 4),
            yy: Vn("FullYear", 2, 0, !0),
            y: Vn("FullYear", 1),
            MMMM: zn("Month"),
            MMM: zn("Month", !0),
            MM: Vn("Month", 2, 1),
            M: Vn("Month", 1, 1),
            dd: Vn("Date", 2),
            d: Vn("Date", 1),
            HH: Vn("Hours", 2),
            H: Vn("Hours", 1),
            hh: Vn("Hours", 2, -12),
            h: Vn("Hours", 1, -12),
            mm: Vn("Minutes", 2),
            m: Vn("Minutes", 1),
            ss: Vn("Seconds", 2),
            s: Vn("Seconds", 1),
            sss: Vn("Milliseconds", 3),
            EEEE: zn("Day"),
            EEE: zn("Day", !0),
            a: Bn,
            Z: Ln
        },
        co = /((?:[^yMdHhmsaZE']+)|(?:'(?:[^']|'')*')|(?:E+|y+|M+|d+|H+|h+|m+|s+|a|Z))(.*)/,
        uo = /^\-?\d+$/;
    Wn.$inject = ["$locale"];
    var lo = g(sr),
        po = g(ar);
    Zn.$inject = ["$parse"];
    var fo = g({
            restrict: "E",
            compile: function(e, n) {
                return 8 >= lr && (n.href || n.name || n.$set("href", ""), e.append(t.createComment("IE fix"))), n.href || n.name ? void 0 : function(e, t) {
                    t.on("click", function(e) {
                        t.attr("href") || e.preventDefault()
                    })
                }
            }
        }),
        $o = {};
    i(Ir, function(e, t) {
        if ("multiple" != e) {
            var n = Nt("ng-" + t);
            $o[n] = function() {
                return {
                    priority: 100,
                    link: function(e, r, o) {
                        e.$watch(o[n], function(e) {
                            o.$set(t, !!e)
                        })
                    }
                }
            }
        }
    }), i(["src", "srcset", "href"], function(e) {
        var t = Nt("ng-" + e);
        $o[t] = function() {
            return {
                priority: 99,
                link: function(n, r, o) {
                    o.$observe(t, function(t) {
                        t && (o.$set(e, t), lr && r.prop(e, o[e]))
                    })
                }
            }
        }
    });
    var ho = {
        $addControl: $,
        $removeControl: $,
        $setValidity: $,
        $setDirty: $,
        $setPristine: $
    };
    Qn.$inject = ["$element", "$attrs", "$scope"];
    var go = function(e) {
            return ["$timeout", function(t) {
                var r = {
                    name: "form",
                    restrict: e ? "EAC" : "E",
                    controller: Qn,
                    compile: function() {
                        return {
                            pre: function(e, r, o, i) {
                                if (!o.action) {
                                    var s = function(e) {
                                        e.preventDefault ? e.preventDefault() : e.returnValue = !1
                                    };
                                    Tr(r[0], "submit", s), r.on("$destroy", function() {
                                        t(function() {
                                            Ar(r[0], "submit", s)
                                        }, 0, !1)
                                    })
                                }
                                var a = r.parent().controller("form"),
                                    c = o.name || o.ngForm;
                                c && gn(e, c, i, c), a && r.on("$destroy", function() {
                                    a.$removeControl(i), c && gn(e, c, n, c), p(i, ho)
                                })
                            }
                        }
                    }
                };
                return r
            }]
        },
        mo = go(),
        vo = go(!0),
        _o = /^(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?$/,
        yo = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}$/,
        bo = /^\s*(\-|\+)?(\d+|(\d*(\.\d*)))\s*$/,
        wo = {
            text: Xn,
            number: er,
            url: tr,
            email: nr,
            radio: rr,
            checkbox: or,
            hidden: $,
            button: $,
            submit: $,
            reset: $
        },
        xo = ["$browser", "$sniffer", function(e, t) {
            return {
                restrict: "E",
                require: "?ngModel",
                link: function(n, r, o, i) {
                    i && (wo[sr(o.type)] || wo.text)(n, r, o, i, t, e)
                }
            }
        }],
        ko = "ng-valid",
        Co = "ng-invalid",
        So = "ng-pristine",
        To = "ng-dirty",
        Ao = ["$scope", "$exceptionHandler", "$attrs", "$element", "$parse", function(e, t, n, o, s) {
            function a(e, t) {
                t = t ? "-" + X(t, "-") : "", o.removeClass((e ? Co : ko) + t).addClass((e ? ko : Co) + t)
            }
            this.$viewValue = Number.NaN, this.$modelValue = Number.NaN, this.$parsers = [], this.$formatters = [], this.$viewChangeListeners = [], this.$pristine = !0, this.$dirty = !1, this.$valid = !0, this.$invalid = !1, this.$name = n.name;
            var c = s(n.ngModel),
                u = c.assign;
            if (!u) throw r("ngModel")("nonassign", "Expression '{0}' is non-assignable. Element: {1}", n.ngModel, B(o));
            this.$render = $, this.$isEmpty = function(e) {
                return m(e) || "" === e || null === e || e !== e
            };
            var l = o.inheritedData("$formController") || ho,
                p = 0,
                d = this.$error = {};
            o.addClass(So), a(!0), this.$setValidity = function(e, t) {
                d[e] !== !t && (t ? (d[e] && p--, p || (a(!0), this.$valid = !0, this.$invalid = !1)) : (a(!1), this.$invalid = !0, this.$valid = !1, p++), d[e] = !t, a(t, e), l.$setValidity(e, t, this))
            }, this.$setPristine = function() {
                this.$dirty = !1, this.$pristine = !0, o.removeClass(To).addClass(So)
            }, this.$setViewValue = function(n) {
                this.$viewValue = n, this.$pristine && (this.$dirty = !0, this.$pristine = !1, o.removeClass(So).addClass(To), l.$setDirty()), i(this.$parsers, function(e) {
                    n = e(n)
                }), this.$modelValue !== n && (this.$modelValue = n, u(e, n), i(this.$viewChangeListeners, function(e) {
                    try {
                        e()
                    } catch (n) {
                        t(n)
                    }
                }))
            };
            var f = this;
            e.$watch(function() {
                var t = c(e);
                if (f.$modelValue !== t) {
                    var n = f.$formatters,
                        r = n.length;
                    for (f.$modelValue = t; r--;) t = n[r](t);
                    f.$viewValue !== t && (f.$viewValue = t, f.$render())
                }
                return t
            })
        }],
        Eo = function() {
            return {
                require: ["ngModel", "^?form"],
                controller: Ao,
                link: function(e, t, n, r) {
                    var o = r[0],
                        i = r[1] || ho;
                    i.$addControl(o), e.$on("$destroy", function() {
                        i.$removeControl(o)
                    })
                }
            }
        },
        Do = g({
            require: "ngModel",
            link: function(e, t, n, r) {
                r.$viewChangeListeners.push(function() {
                    e.$eval(n.ngChange)
                })
            }
        }),
        jo = function() {
            return {
                require: "?ngModel",
                link: function(e, t, n, r) {
                    if (r) {
                        n.required = !0;
                        var o = function(e) {
                            return n.required && r.$isEmpty(e) ? (r.$setValidity("required", !1), void 0) : (r.$setValidity("required", !0), e)
                        };
                        r.$formatters.push(o), r.$parsers.unshift(o), n.$observe("required", function() {
                            o(r.$viewValue)
                        })
                    }
                }
            }
        },
        Oo = function() {
            return {
                require: "ngModel",
                link: function(e, t, r, o) {
                    var s = /\/(.*)\//.exec(r.ngList),
                        a = s && new RegExp(s[1]) || r.ngList || ",",
                        c = function(e) {
                            if (!m(e)) {
                                var t = [];
                                return e && i(e.split(a), function(e) {
                                    e && t.push(br(e))
                                }), t
                            }
                        };
                    o.$parsers.push(c), o.$formatters.push(function(e) {
                        return x(e) ? e.join(", ") : n
                    }), o.$isEmpty = function(e) {
                        return !e || !e.length
                    }
                }
            }
        },
        Io = /^(true|false|\d+)$/,
        Po = function() {
            return {
                priority: 100,
                compile: function(e, t) {
                    return Io.test(t.ngValue) ? function(e, t, n) {
                        n.$set("value", e.$eval(n.ngValue))
                    } : function(e, t, n) {
                        e.$watch(n.ngValue, function(e) {
                            n.$set("value", e)
                        })
                    }
                }
            }
        },
        Mo = Jn(function(e, t, r) {
            t.addClass("ng-binding").data("$binding", r.ngBind), e.$watch(r.ngBind, function(e) {
                t.text(e == n ? "" : e)
            })
        }),
        qo = ["$interpolate", function(e) {
            return function(t, n, r) {
                var o = e(n.attr(r.$attr.ngBindTemplate));
                n.addClass("ng-binding").data("$binding", o), r.$observe("ngBindTemplate", function(e) {
                    n.text(e)
                })
            }
        }],
        No = ["$sce", "$parse", function(e, t) {
            return function(n, r, o) {
                function i() {
                    return (s(n) || "").toString()
                }
                r.addClass("ng-binding").data("$binding", o.ngBindHtml);
                var s = t(o.ngBindHtml);
                n.$watch(i, function() {
                    r.html(e.getTrustedHtml(s(n)) || "")
                })
            }
        }],
        Ro = ir("", !0),
        Uo = ir("Odd", 0),
        Ho = ir("Even", 1),
        Fo = Jn({
            compile: function(e, t) {
                t.$set("ngCloak", n), e.removeClass("ng-cloak")
            }
        }),
        Vo = [function() {
            return {
                scope: !0,
                controller: "@",
                priority: 500
            }
        }],
        zo = {};
    i("click dblclick mousedown mouseup mouseover mouseout mousemove mouseenter mouseleave keydown keyup keypress submit focus blur copy cut paste".split(" "), function(e) {
        var t = Nt("ng-" + e);
        zo[t] = ["$parse", function(n) {
            return {
                compile: function(r, o) {
                    var i = n(o[t]);
                    return function(t, n) {
                        n.on(sr(e), function(e) {
                            t.$apply(function() {
                                i(t, {
                                    $event: e
                                })
                            })
                        })
                    }
                }
            }
        }]
    });
    var Lo = ["$animate", function(e) {
            return {
                transclude: "element",
                priority: 600,
                terminal: !0,
                restrict: "A",
                $$tlb: !0,
                link: function(n, r, o, i, s) {
                    var a, c;
                    n.$watch(o.ngIf, function(i) {
                        L(i) ? c || (c = n.$new(), s(c, function(n) {
                            n[n.length++] = t.createComment(" end ngIf: " + o.ngIf + " "), a = {
                                clone: n
                            }, e.enter(n, r.parent(), r)
                        })) : (c && (c.$destroy(), c = null), a && (e.leave(it(a.clone)), a = null))
                    })
                }
            }
        }],
        Bo = ["$http", "$templateCache", "$anchorScroll", "$animate", "$sce", function(e, t, n, r, o) {
            return {
                restrict: "ECA",
                priority: 400,
                terminal: !0,
                transclude: "element",
                controller: _r.noop,
                compile: function(i, s) {
                    var a = s.ngInclude || s.src,
                        c = s.onload || "",
                        u = s.autoscroll;
                    return function(i, s, l, p, d) {
                        var f, $, h = 0,
                            g = function() {
                                f && (f.$destroy(), f = null), $ && (r.leave($), $ = null)
                            };
                        i.$watch(o.parseAsResourceUrl(a), function(o) {
                            var a = function() {
                                    !v(u) || u && !i.$eval(u) || n()
                                },
                                l = ++h;
                            o ? (e.get(o, {
                                cache: t
                            }).success(function(e) {
                                if (l === h) {
                                    var t = i.$new();
                                    p.template = e;
                                    var n = d(t, function(e) {
                                        g(), r.enter(e, null, s, a)
                                    });
                                    f = t, $ = n, f.$emit("$includeContentLoaded"), i.$eval(c)
                                }
                            }).error(function() {
                                l === h && g()
                            }), i.$emit("$includeContentRequested")) : (g(), p.template = null)
                        })
                    }
                }
            }
        }],
        Wo = ["$compile", function(e) {
            return {
                restrict: "ECA",
                priority: -400,
                require: "ngInclude",
                link: function(t, n, r, o) {
                    n.html(o.template), e(n.contents())(t)
                }
            }
        }],
        Yo = Jn({
            priority: 450,
            compile: function() {
                return {
                    pre: function(e, t, n) {
                        e.$eval(n.ngInit)
                    }
                }
            }
        }),
        Go = Jn({
            terminal: !0,
            priority: 1e3
        }),
        Zo = ["$locale", "$interpolate", function(e, t) {
            var n = /{}/g;
            return {
                restrict: "EA",
                link: function(r, o, s) {
                    var a = s.count,
                        c = s.$attr.when && o.attr(s.$attr.when),
                        u = s.offset || 0,
                        l = r.$eval(c) || {},
                        p = {},
                        d = t.startSymbol(),
                        f = t.endSymbol(),
                        $ = /^when(Minus)?(.+)$/;
                    i(s, function(e, t) {
                        $.test(t) && (l[sr(t.replace("when", "").replace("Minus", "-"))] = o.attr(s.$attr[t]))
                    }), i(l, function(e, r) {
                        p[r] = t(e.replace(n, d + a + "-" + u + f))
                    }), r.$watch(function() {
                        var t = parseFloat(r.$eval(a));
                        return isNaN(t) ? "" : (t in l || (t = e.pluralCat(t - u)), p[t](r, o, !0))
                    }, function(e) {
                        o.text(e)
                    })
                }
            }
        }],
        Jo = ["$parse", "$animate", function(e, n) {
            function s(e) {
                return e.clone[0]
            }

            function a(e) {
                return e.clone[e.clone.length - 1]
            }
            var c = "$$NG_REMOVED",
                u = r("ngRepeat");
            return {
                transclude: "element",
                priority: 1e3,
                terminal: !0,
                $$tlb: !0,
                link: function(r, l, p, d, f) {
                    var $, h, g, m, v, _, y, b, w, x = p.ngRepeat,
                        k = x.match(/^\s*([\s\S]+?)\s+in\s+([\s\S]+?)(?:\s+track\s+by\s+([\s\S]+?))?\s*$/),
                        C = {
                            $id: Tt
                        };
                    if (!k) throw u("iexp", "Expected expression in form of '_item_ in _collection_[ track by _id_]' but got '{0}'.", x);
                    if (_ = k[1], y = k[2], $ = k[3], $ ? (h = e($), g = function(e, t, n) {
                            return w && (C[w] = e), C[b] = t, C.$index = n, h(r, C)
                        }) : (m = function(e, t) {
                            return Tt(t)
                        }, v = function(e) {
                            return e
                        }), k = _.match(/^(?:([\$\w]+)|\(([\$\w]+)\s*,\s*([\$\w]+)\))$/), !k) throw u("iidexp", "'_item_' in '_item_ in _collection_' should be an identifier or '(_key_, _value_)' expression, but got '{0}'.", _);
                    b = k[3] || k[1], w = k[2];
                    var S = {};
                    r.$watchCollection(y, function(e) {
                        var p, d, $, h, _, y, k, C, T, A, E, D, j = l[0],
                            O = {},
                            I = [];
                        if (o(e)) A = e, T = g || m;
                        else {
                            T = g || v, A = [];
                            for (y in e) e.hasOwnProperty(y) && "$" != y.charAt(0) && A.push(y);
                            A.sort()
                        }
                        for (h = A.length, d = I.length = A.length, p = 0; d > p; p++)
                            if (y = e === A ? p : A[p], k = e[y], C = T(y, k, p), rt(C, "`track by` id"), S.hasOwnProperty(C)) E = S[C], delete S[C], O[C] = E, I[p] = E;
                            else {
                                if (O.hasOwnProperty(C)) throw i(I, function(e) {
                                    e && e.scope && (S[e.id] = e)
                                }), u("dupes", "Duplicates in a repeater are not allowed. Use 'track by' expression to specify unique keys. Repeater: {0}, Duplicate key: {1}", x, C);
                                I[p] = {
                                    id: C
                                }, O[C] = !1
                            }
                        for (y in S) S.hasOwnProperty(y) && (E = S[y], D = it(E.clone), n.leave(D), i(D, function(e) {
                            e[c] = !0
                        }), E.scope.$destroy());
                        for (p = 0, d = A.length; d > p; p++) {
                            if (y = e === A ? p : A[p], k = e[y], E = I[p], I[p - 1] && (j = a(I[p - 1])), E.scope) {
                                _ = E.scope, $ = j;
                                do $ = $.nextSibling; while ($ && $[c]);
                                s(E) != $ && n.move(it(E.clone), null, pr(j)), j = a(E)
                            } else _ = r.$new();
                            _[b] = k, w && (_[w] = y), _.$index = p, _.$first = 0 === p, _.$last = p === h - 1, _.$middle = !(_.$first || _.$last), _.$odd = !(_.$even = 0 === (1 & p)), E.scope || f(_, function(e) {
                                e[e.length++] = t.createComment(" end ngRepeat: " + x + " "), n.enter(e, null, pr(j)), j = e, E.scope = _, E.clone = e, O[E.id] = E
                            })
                        }
                        S = O
                    })
                }
            }
        }],
        Qo = ["$animate", function(e) {
            return function(t, n, r) {
                t.$watch(r.ngShow, function(t) {
                    e[L(t) ? "removeClass" : "addClass"](n, "ng-hide")
                })
            }
        }],
        Ko = ["$animate", function(e) {
            return function(t, n, r) {
                t.$watch(r.ngHide, function(t) {
                    e[L(t) ? "addClass" : "removeClass"](n, "ng-hide")
                })
            }
        }],
        Xo = Jn(function(e, t, n) {
            e.$watch(n.ngStyle, function(e, n) {
                n && e !== n && i(n, function(e, n) {
                    t.css(n, "")
                }), e && t.css(e)
            }, !0)
        }),
        ei = ["$animate", function(e) {
            return {
                restrict: "EA",
                require: "ngSwitch",
                controller: ["$scope", function() {
                    this.cases = {}
                }],
                link: function(t, n, r, o) {
                    var s, a, c = r.ngSwitch || r.on,
                        u = [];
                    t.$watch(c, function(n) {
                        for (var c = 0, l = u.length; l > c; c++) u[c].$destroy(), e.leave(a[c]);
                        a = [], u = [], (s = o.cases["!" + n] || o.cases["?"]) && (t.$eval(r.change), i(s, function(n) {
                            var r = t.$new();
                            u.push(r), n.transclude(r, function(t) {
                                var r = n.element;
                                a.push(t), e.enter(t, r.parent(), r)
                            })
                        }))
                    })
                }
            }
        }],
        ti = Jn({
            transclude: "element",
            priority: 800,
            require: "^ngSwitch",
            link: function(e, t, n, r, o) {
                r.cases["!" + n.ngSwitchWhen] = r.cases["!" + n.ngSwitchWhen] || [], r.cases["!" + n.ngSwitchWhen].push({
                    transclude: o,
                    element: t
                })
            }
        }),
        ni = Jn({
            transclude: "element",
            priority: 800,
            require: "^ngSwitch",
            link: function(e, t, n, r, o) {
                r.cases["?"] = r.cases["?"] || [], r.cases["?"].push({
                    transclude: o,
                    element: t
                })
            }
        }),
        ri = Jn({
            controller: ["$element", "$transclude", function(e, t) {
                if (!t) throw r("ngTransclude")("orphan", "Illegal use of ngTransclude directive in the template! No parent directive that requires a transclusion found. Element: {0}", B(e));
                this.$transclude = t
            }],
            link: function(e, t, n, r) {
                r.$transclude(function(e) {
                    t.empty(), t.append(e)
                })
            }
        }),
        oi = ["$templateCache", function(e) {
            return {
                restrict: "E",
                terminal: !0,
                compile: function(t, n) {
                    if ("text/ng-template" == n.type) {
                        var r = n.id,
                            o = t[0].text;
                        e.put(r, o)
                    }
                }
            }
        }],
        ii = r("ngOptions"),
        si = g({
            terminal: !0
        }),
        ai = ["$compile", "$parse", function(e, r) {
            var o = /^\s*([\s\S]+?)(?:\s+as\s+([\s\S]+?))?(?:\s+group\s+by\s+([\s\S]+?))?\s+for\s+(?:([\$\w][\$\w]*)|(?:\(\s*([\$\w][\$\w]*)\s*,\s*([\$\w][\$\w]*)\s*\)))\s+in\s+([\s\S]+?)(?:\s+track\s+by\s+([\s\S]+?))?$/,
                a = {
                    $setViewValue: $
                };
            return {
                restrict: "E",
                require: ["select", "?ngModel"],
                controller: ["$element", "$scope", "$attrs", function(e, t, n) {
                    var r, o, i = this,
                        s = {},
                        c = a;
                    i.databound = n.ngModel, i.init = function(e, t, n) {
                        c = e, r = t, o = n
                    }, i.addOption = function(t) {
                        rt(t, '"option value"'), s[t] = !0, c.$viewValue == t && (e.val(t), o.parent() && o.remove())
                    }, i.removeOption = function(e) {
                        this.hasOption(e) && (delete s[e], c.$viewValue == e && this.renderUnknownOption(e))
                    }, i.renderUnknownOption = function(t) {
                        var n = "? " + Tt(t) + " ?";
                        o.val(n), e.prepend(o), e.val(n), o.prop("selected", !0)
                    }, i.hasOption = function(e) {
                        return s.hasOwnProperty(e)
                    }, t.$on("$destroy", function() {
                        i.renderUnknownOption = $
                    })
                }],
                link: function(a, c, u, l) {
                    function p(e, t, n, r) {
                        n.$render = function() {
                            var e = n.$viewValue;
                            r.hasOption(e) ? (C.parent() && C.remove(), t.val(e), "" === e && $.prop("selected", !0)) : m(e) && $ ? t.val("") : r.renderUnknownOption(e)
                        }, t.on("change", function() {
                            e.$apply(function() {
                                C.parent() && C.remove(), n.$setViewValue(t.val())
                            })
                        })
                    }

                    function d(e, t, n) {
                        var r;
                        n.$render = function() {
                            var e = new At(n.$viewValue);
                            i(t.find("option"), function(t) {
                                t.selected = v(e.get(t.value))
                            })
                        }, e.$watch(function() {
                            q(r, n.$viewValue) || (r = P(n.$viewValue), n.$render())
                        }), t.on("change", function() {
                            e.$apply(function() {
                                var e = [];
                                i(t.find("option"), function(t) {
                                    t.selected && e.push(t.value)
                                }), n.$setViewValue(e)
                            })
                        })
                    }

                    function f(t, i, a) {
                        function c() {
                            var e, n, r, o, c, u, g, y, S, T, A, E, D, j, O, I = {
                                    "": []
                                },
                                P = [""],
                                M = a.$modelValue,
                                q = h(t) || [],
                                N = d ? s(q) : q,
                                R = {},
                                U = !1;
                            if (_)
                                if (m && x(M)) {
                                    U = new At([]);
                                    for (var H = 0; H < M.length; H++) R[p] = M[H], U.put(m(t, R), M[H])
                                } else U = new At(M);
                            for (A = 0; S = N.length, S > A; A++) {
                                if (g = A, d) {
                                    if (g = N[A], "$" === g.charAt(0)) continue;
                                    R[d] = g
                                }
                                if (R[p] = q[g], e = f(t, R) || "", (n = I[e]) || (n = I[e] = [], P.push(e)), _) E = v(U.remove(m ? m(t, R) : $(t, R)));
                                else {
                                    if (m) {
                                        var F = {};
                                        F[p] = M, E = m(t, F) === m(t, R)
                                    } else E = M === $(t, R);
                                    U = U || E
                                }
                                O = l(t, R), O = v(O) ? O : "", n.push({
                                    id: m ? m(t, R) : d ? N[A] : A,
                                    label: O,
                                    selected: E
                                })
                            }
                            for (_ || (b || null === M ? I[""].unshift({
                                    id: "",
                                    label: "",
                                    selected: !U
                                }) : U || I[""].unshift({
                                    id: "?",
                                    label: "",
                                    selected: !0
                                })), T = 0, y = P.length; y > T; T++) {
                                for (e = P[T], n = I[e], C.length <= T ? (o = {
                                        element: k.clone().attr("label", e),
                                        label: n.label
                                    }, c = [o], C.push(c), i.append(o.element)) : (c = C[T], o = c[0], o.label != e && o.element.attr("label", o.label = e)), D = null, A = 0, S = n.length; S > A; A++) r = n[A], (u = c[A + 1]) ? (D = u.element, u.label !== r.label && D.text(u.label = r.label), u.id !== r.id && D.val(u.id = r.id), D[0].selected !== r.selected && D.prop("selected", u.selected = r.selected)) : ("" === r.id && b ? j = b : (j = w.clone()).val(r.id).attr("selected", r.selected).text(r.label), c.push(u = {
                                    element: j,
                                    label: r.label,
                                    id: r.id,
                                    selected: r.selected
                                }), D ? D.after(j) : o.element.append(j), D = j);
                                for (A++; c.length > A;) c.pop().element.remove()
                            }
                            for (; C.length > T;) C.pop()[0].element.remove()
                        }
                        var u;
                        if (!(u = y.match(o))) throw ii("iexp", "Expected expression in form of '_select_ (as _label_)? for (_key_,)?_value_ in _collection_' but got '{0}'. Element: {1}", y, B(i));
                        var l = r(u[2] || u[1]),
                            p = u[4] || u[6],
                            d = u[5],
                            f = r(u[3] || ""),
                            $ = r(u[2] ? u[1] : p),
                            h = r(u[7]),
                            g = u[8],
                            m = g ? r(u[8]) : null,
                            C = [
                                [{
                                    element: i,
                                    label: ""
                                }]
                            ];
                        b && (e(b)(t), b.removeClass("ng-scope"), b.remove()), i.empty(), i.on("change", function() {
                            t.$apply(function() {
                                var e, r, o, s, c, u, l, f, g, v = h(t) || [],
                                    y = {};
                                if (_) {
                                    for (o = [], u = 0, f = C.length; f > u; u++)
                                        for (e = C[u], c = 1, l = e.length; l > c; c++)
                                            if ((s = e[c].element)[0].selected) {
                                                if (r = s.val(), d && (y[d] = r), m)
                                                    for (g = 0; g < v.length && (y[p] = v[g], m(t, y) != r); g++);
                                                else y[p] = v[r];
                                                o.push($(t, y))
                                            }
                                } else if (r = i.val(), "?" == r) o = n;
                                else if ("" === r) o = null;
                                else if (m) {
                                    for (g = 0; g < v.length; g++)
                                        if (y[p] = v[g], m(t, y) == r) {
                                            o = $(t, y);
                                            break
                                        }
                                } else y[p] = v[r], d && (y[d] = r), o = $(t, y);
                                a.$setViewValue(o)
                            })
                        }), a.$render = c, t.$watch(c)
                    }
                    if (l[1]) {
                        for (var $, h = l[0], g = l[1], _ = u.multiple, y = u.ngOptions, b = !1, w = pr(t.createElement("option")), k = pr(t.createElement("optgroup")), C = w.clone(), S = 0, T = c.children(), A = T.length; A > S; S++)
                            if ("" === T[S].value) {
                                $ = b = T.eq(S);
                                break
                            }
                        h.init(g, b, C), _ && (g.$isEmpty = function(e) {
                            return !e || 0 === e.length
                        }), y ? f(a, c, g) : _ ? d(a, c, g) : p(a, c, g, h)
                    }
                }
            }
        }],
        ci = ["$interpolate", function(e) {
            var t = {
                addOption: $,
                removeOption: $
            };
            return {
                restrict: "E",
                priority: 100,
                compile: function(n, r) {
                    if (m(r.value)) {
                        var o = e(n.text(), !0);
                        o || r.$set("value", n.text())
                    }
                    return function(e, n, r) {
                        var i = "$selectController",
                            s = n.parent(),
                            a = s.data(i) || s.parent().data(i);
                        a && a.databound ? n.prop("selected", !1) : a = t, o ? e.$watch(o, function(e, t) {
                            r.$set("value", e), e !== t && a.removeOption(t), a.addOption(e)
                        }) : a.addOption(r.value), n.on("$destroy", function() {
                            a.removeOption(r.value)
                        })
                    }
                }
            }
        }],
        ui = g({
            restrict: "E",
            terminal: !0
        });
    et(), at(_r), pr(t).ready(function() {
        Q(t, K)
    })
}(window, document), !angular.$$csp() && angular.element(document).find("head").prepend('<style type="text/css">@charset "UTF-8";[ng\\:cloak],[ng-cloak],[data-ng-cloak],[x-ng-cloak],.ng-cloak,.x-ng-cloak,.ng-hide{display:none !important;}ng\\:form{display:block;}</style>'),
    function() {
        this.app = angular.module("dji-store", ["ui.bootstrap", "duScroll", "ngSanitize"]), this.app.config(["$httpProvider", function(e) {
            return e.defaults.headers.common["X-CSRF-Token"] = $("meta[name=csrf-token]").attr("content")
        }])
    }.call(this),
    function() {
        this.AddressCtrl = ["$scope", function(e) {
            var t, n, r, o;
            return n = /\d+/, o = /^[\d|\w]+$/, t = /^[a-zA-Z0-9-!@#$%^&*()_+|~=`{}\[\]:";'<>?,.\/\s]+$/, r = {
                kr: /^\d{5}$/,
                us: /^\d{5}$/
            }, e.country = "", e.init_origin_address = function(t) {
                return "cn" === t.country ? ($("#china-city-init").data("state", t.state).data("city", t.city).data("district", t.district).data("street", t.street), e.city = "", e.district = "", e.street = "", setTimeout(function() {
                    return $(".state").val(t.state).change()
                }, 100)) : (e.city = t.city, e.state = t.state, e.district = t.district || "", e.street = t.street || ""), e.submiting = !1, e.country = t.country, e.first_name_label_change(e.country), e.first_name = t.first_name, e.last_name = t.last_name, e.phone = t.phone, e.zip_code = t.zip_code, e.address = t.address, e.address2 = t.address2
            }, e.init_mobile = function() {
                return e.error_scroll = !0
            }, e.init_pc = function() {
                return e.error_scroll = !0
            }, e.error_scroll_action = function() {
                return e.error_scroll ? window.scrollTo(0, 0) : void 0
            }, e.CNShow = function(t) {
                return e.CountryShow(t, ["cn"])
            }, e.USShow = function(t) {
                return e.CountryShow(t, ["us"])
            }, e.CountryShow = function(t, n) {
                return -1 !== n.indexOf(e[t])
            }, e.clearCityState = function() {
                return e.first_name_label_change(e.country), e.city = "", e.state = "", e.district = "", e.street = ""
            }, e.error = function(t) {
                var n;
                return e.errors && (n = e.errors[t]), n
            }, e.validate = function(t) {
                return e.errors[t] = null, e[t] || (e.errors[t] = I18n.t("js.error.blank")), e[t] && e[t].length > 255 && (e.errors[t] = I18n.t("js.error.too_long", {
                    count: 255
                })), !e.errors[t]
            }, e.shipping_error = function(t) {
                var n;
                return e.errors && (n = e.errors["shipping_address." + t]), n ? n.join(",") : void 0
            }, e.set_errors = function(t) {
                return e.errors = {}, "string" == typeof t ? e.errors = t : t.constructor === Object ? $.each(t, function(t, n) {
                    return e.errors[t] = Array.isArray(n) ? n[0] : n
                }) : void 0
            }, e.validate_address = function() {
                var o, i, s, a, c, u, l, p, d, f, $;
                if (s = !0, "cn" === e.country)
                    for (f = ["first_name", "phone", "address", "district", "city", "state", "country", "district", "street"], a = 0, l = f.length; l > a; a++) o = f[a], e.validate(o) || (s = !1);
                else {
                    for (i = ["first_name", "last_name", "phone", "address", "city", "state", "country", "zip_code"], e.init_ignore_last_name && e.init_ignore_last_name() && i.splice(1, 1), c = 0, p = i.length; p > c; c++) o = i[c], e.validate(o) || (s = !1);
                    if ("jp" !== e.country && "de" !== e.country && "at" !== e.country && "li" !== e.country && "hk" !== e.country && "mo" !== e.country && "tw" !== e.country && "kr" !== e.country)
                        for ($ = ["address", "address2", "state", "city", "first_name", "last_name"], u = 0, d = $.length; d > u; u++) o = $[u], e[o] && (t.test(e[o]) || (e.errors[o] = I18n.t("js.error.invalid_address"), s = !1))
                }
                return e.phone && !n.test(e.phone) && (e.errors.phone = I18n.t("js.error.invalid_phone"), s = !1), r[e.country] && !r[e.country].test(e.zip_code) && (e.errors.zip_code = I18n.t("js.error.invalid_zip_code"), s = !1), s
            }, e.shipping_error = function(t) {
                var n;
                return e.errors && (n = e.errors["shipping_address." + t]), n ? n.join(",") : void 0
            }, e.set_errors = function(t) {
                return e.errors = {}, "string" == typeof t ? e.errors = t : t.constructor === Object ? $.each(t, function(t, n) {
                    return e.errors[t] = Array.isArray(n) ? n[0] : n
                }) : void 0
            }, e.stateSelectable = function(t) {
                return e.CNShow(t) || e.USShow(t) || e.CountryShow(t, ["ca"])
            }, e.showDefaultCountry = function() {
                return e.country = "cn", e.use_default_country = !0
            }, e.first_name_label_change = function(t) {
                return e.first_name_label = "cn" === t ? I18n.t("js.user.full_name") : "cn" !== t && "zh-CN" === I18n.locale ? I18n.t("js.user.first_name_cn") : I18n.t("js.user.first_name")
            }, e.clearCity = function() {}, e.clearDistrict = function() {
                return e.district = ""
            }, e.update_state = function() {
                var e;
                return e = $("select.state"), e.change()
            }, e.set_country_text = function() {
                return e.country_text = $("#country option:selected").text()
            }, e.init_child = function(t) {
                return e = t
            }
        }]
    }.call(this),
    function() {
        this.CartsCtrl = ["$scope", "$http", "$window", "$filter", function($scope, $http, $window, $filter) {
            return $scope.tcurrency = function() {
                return "CNY" === $scope.currency ? "RMB" : $scope.currency
            }, $scope.subtotal = function(e) {
                return e.product.price * e.quantity
            }, $scope.total = function() {
                var e, t, n, r, o;
                for (t = 0, o = $scope.cart.items, n = 0, r = o.length; r > n; n++) e = o[n], t += $scope.subtotal(e);
                return t
            }, $scope.shipping_fee = function() {
                return $scope.cart.shipping_fee_cents
            }, $scope.change = function(e) {
                var t;
                return t = $http.post("/cart/change.json", {
                    product_id: e.product.id,
                    quantity: e.quantity,
                    type: e.product.type
                }), t.success(function(e) {
                    return (void 0 !== e.shipping_fee_cents || null !== e.shipping_fee_cents) && ($scope.cart.shipping_fee_cents = e.shipping_fee_cents), $scope.show_gift()
                })
            }, $scope.increment = function(e) {
                return e.quantity < e.max_quantity || e.product.slug.match(/special-\d+/) ? (e.quantity += 1, $scope.change(e)) : void 0
            }, $scope.decrement = function(e) {
                return e.quantity > 1 ? (e.quantity -= 1, $scope.change(e)) : void 0
            }, $scope["delete"] = function(e, t) {
                var n;
                return n = $http.post("/cart/change.json", {
                    product_id: e.product.id,
                    quantity: 0,
                    type: e.product.type
                }), n.success(function(e) {
                    var n;
                    return n = e.delete_flag ? 2 : 1, $scope.cart.items.splice(t, n), (void 0 !== e.shipping_fee_cents || null !== e.shipping_fee_cents) && ($scope.cart.shipping_fee_cents = e.shipping_fee_cents), $scope.show_gift()
                })
            }, $scope.show_gift = function() {
                var request;
                return request = $http.get(I18n.url("/cart/cart-gift.js?currency=" + $scope.currency + "&country=" + $scope.country + "&source=cart")), request.success(function(data) {
                    return eval(data)
                })
            }, $scope.weights = function() {
                var e, t, n, r, o;
                for (t = 0, o = $scope.cart.items, n = 0, r = o.length; r > n; n++) e = o[n], t += e.quantity * e.product.weight;
                return t
            }, $scope.virtual_address = function(e) {
                return e.product.configs.virtual_address
            }, $scope.limit_quantity = function(e) {
                return e.max_quantity <= 1
            }, $scope.is_dji_care = function(e) {
                return "dji_care" === e.product.configs.category
            }, $scope.item_class = function(e) {
                return $scope.is_dji_care(e) ? "none-border" : ""
            }, $scope.is_dji_care_main = function(e) {
                return "dji_care_main" === e.product.configs.category
            }
        }]
    }.call(this),
    function() {
        var e, t, n;
        this.app.filter("show_money", function() {
            return function(e, t, r) {
                return n(e, t, r)
            }
        }), n = function(e, n, r) {
            var o, i, s, a, c, u, l, p;
            if (!("" + e)) return "value abnormal!";
            if (!n) return "argument lost: currency ";
            if (!r) return "argument lost: country ";
            if (2 !== r.length) return "country_code size should be 2";
            switch (e = "" + e, n) {
                case "CNY":
                case "RMB":
                    return t(e, "楼", "", ".", 2, "");
                case "JPY":
                    return t(e, "楼 ", ",", "", 0, "");
                case "KRW":
                    return t(e, "鈧�", ",", "", 0, "");
                case "EUR":
                    return l = ["", " 鈧�"], a = l[0], u = l[1], ("ie" === r || "lv" === r || "mt" === r) && (p = ["鈧�", ""], a = p[0], u = p[1]), s = "at" === r || "de" === r || "it" === r || "no" === r || "es" === r ? "." : " ", i = "ie" === r || "ch" === r ? "." : ",", t(e, a, s, i, 2, u);
                case "GBP":
                    return t(e, "拢", ",", ".", 2, "");
                case "AUD":
                case "HKD":
                case "USD":
                    return t(e, n + " $", ",", ".", 2, "");
                case "TWD":
                    return c = t(e, "NT$", ",", ".", 2, ""), o = c.split("."), "00" === o[1] ? o[0] : c;
                default:
                    return "unsupported currency: " + n
            }
        }, t = function(t, n, r, o, i, s) {
            var a, c, u;
            return null == s && (s = ""), c = t.length - i, 1 > c ? (u = Array(i - t.length + 1).join("0"), n + "0" + o + u + t + s) : (a = o + t.slice(c, t.length), n + e(t.slice(0, c), r) + a + s)
        }, e = function(e, t) {
            var n, r, o;
            return null == t && (t = ","), e ? (n = Math.abs(e).toFixed(0), r = (r = n.length) > 3 ? r % 3 : 0, o = "", r && (o += n.substr(0, r) + t), o + n.substr(r).replace(/(\d{3})(?=\d)/g, "$1" + t)) : "null format_intger(value"
        }, this.app.filter("money", function() {
            return function(e, t) {
                return null == t && (t = 100), "use_'show_money(value,currency,country)'_instead"
            }
        })
    }.call(this),
    function() {
        this.GatewayMigsCtrl = ["$scope", "$http", "$window", function(e, t, n) {
            return e.error = null, e.pay = function() {
                var r;
                return e.submiting = !0, r = t.post(I18n.url("/gateway/migs"), e.postData()), r.success(function(e) {
                    return n.location = e.url
                }), r.error(function(t) {
                    return e.error = t.errors
                }), r["finally"](function() {
                    return e.submiting = !1
                })
            }, e.postData = function() {
                return {
                    uuid: e.order.uuid,
                    number: e.number,
                    month: e.month,
                    year: e.year,
                    cvc: e.cvc
                }
            }
        }]
    }.call(this),
    function() {
        this.GatewayPaypalExpressCtrl = ["$scope", "$http", "$window", function(e, t, n) {
            return e.init_child(e), e.submiting = !1, e.server_error = !1, e.agreement = !0, e.error_scroll = !1, e.init_ignore_last_name = function() {
                return e.ignore_last_name = !1
            }, e.init_order = function(t) {
                return e.order_data = t
            }, e.submiting_btn = function() {
                return $("input[type='submit']")
            }, e.set_submiting = function() {
                var t;
                return e.submiting = !0, t = e.submiting_btn(), t.val(t.attr("text-submiting"))
            }, e.finish_submiting = function() {
                var t;
                return e.submiting = !1, t = e.submiting_btn(), t.val(t.attr("text"))
            }, e.obj2QueryStr = function(e) {
                var t, n, r, o;
                for (n = [], r = 0, o = e.length; o > r; r++) t = e[r], e.hasOwnProperty(t) && n.push(t + "=" + e[t]);
                return n
            }, e.submitCheck = function() {
                var t, n;
                return n = App.regs.isDJIApp.test(window.navigator.userAgent), t = e.obj2QueryStr(e.order_data), n ? (location.href = "ios::pay::" + t.join("&"), !1) : !0
            }, e.native_pay = function(t) {
                var n, r;
                return console.log("order:", t), n = t.shipping_address, r = {
                    method: "chooseNativePay",
                    gateway: "paypal",
                    language: t.lang,
                    payInfo: {
                        currencyCode: t.currency,
                        amount: t.total,
                        shortDescription: self._getOrderDesc(t),
                        uuid: t.uuid,
                        clientId: t.client_id.production,
                        sandboxId: t.client_id.sandbox,
                        merchant: t.merchant_name,
                        policy: t.policy_url,
                        agreement: t.agreement_url,
                        production: t.production,
                        shippingAddress: {
                            firstName: n.first_name,
                            lastName: n.last_name,
                            address1: n.address,
                            address2: n.address2,
                            city: n.city,
                            state: n.state,
                            country: n.country_text,
                            countryCode: n.country,
                            zipCode: n.zip_code,
                            nightPhoneA: n.phone,
                            nightPhoneB: n.phone
                        }
                    },
                    callback: function(n) {
                        var r;
                        return 0 === n.retcode ? (r = {
                            uuid: t.uuid,
                            payment: "paypal"
                        }, r.data = JSON.stringify(n.data.result.response), console.log("pay success", r.data)) : -1 === n.retcode ? e.create_paypal_order() : e.server_error = !0
                    }
                }, call_native(rsq_data)
            }, e.native_pay_available = function() {
                return "undefined" != typeof call_native
            }, e.goto_pay = function() {
                return e.native_pay_available() ? e.native_pay(e.order_data) : e.create_paypal_order()
            }, e.postData = function() {
                var t;
                return t = {
                    ignore_last_name: e.ignore_last_name,
                    first_name: e.first_name,
                    last_name: e.last_name,
                    phone: e.phone,
                    city: e.city,
                    state: e.state,
                    district: e.district,
                    street: e.street,
                    country: e.country,
                    zip_code: e.zip_code,
                    address: e.address,
                    address2: e.address2
                }, {
                    email: $("input[name='email']").val(),
                    PayerID: $("input[name='PayerID']").val(),
                    payer_id: $("input[name='payer_id']").val(),
                    token: $("input[name='token']").val(),
                    source: $("input[name='source']").val(),
                    order: {
                        shipping_address_attributes: t
                    },
                    agreement: $('input[name="agreement"]').prop("checked")
                }
            }, e.create_paypal_order = function() {
                var r, o;
                return o = $("form").attr("action"), e.errors = {}, e.server_error = !1, e.init_ignore_last_name(), e.set_submiting(), e.validate_address() ? (e.errors = {}, r = t.post(o, e.postData()), r.success(function(t) {
                    return 3 === t.status ? (e.set_errors(t.data), e.finish_submiting(), e.error_scroll_action(), !1) : t.data ? n.location = t.data : void 0
                }), r.error(function() {
                    return e.server_error = !0, e.finish_submiting()
                })) : (e.error_scroll_action(), e.finish_submiting(), !1)
            }
        }]
    }.call(this),
    function() {
        this.GatewayStripeCtrl = ["$scope", "$http", "$window", function(e, t, n) {
            return Stripe.setPublishableKey("pk_054EgnjIUGmwcm4hCDBy7LTT8Vd9R"), e.pay = function() {
                return Stripe.card.createToken({
                    number: e.card_number,
                    cvc: e.card_cvc,
                    exp_month: e.exp_month,
                    exp_year: e.exp_year
                }, e.stripeResponseHandler)
            }, e.stripeResponseHandler = function(r, o) {
                var i, s;
                return o.error ? alert(o.error) : (s = o.id, i = t.post("/gateway/stripe", {
                    token: s,
                    order_id: e.order.id
                }), i.success(function(e) {
                    return n.location = e.url
                }), i.error(function(e) {
                    return alert(e.errors)
                }))
            }
        }]
    }.call(this),
    function() {
        var __indexOf = [].indexOf || function(e) {
            for (var t = 0, n = this.length; n > t; t++)
                if (t in this && this[t] === e) return t;
            return -1
        };
        this.OrdersCtrl = ["$scope", "$http", "$window", "$document", "$timeout", "$filter", function($scope, $http, $window, $document, $timeout, $filter) {
            var address_for_not_cn_regexp, email_validator_regexp, mobile_validator_regexp, numble_validator_regexp, phone_validator_regexp, shipping_item_prefix, zip_code_regexp, zip_code_validator_regexp;
            return email_validator_regexp = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/, phone_validator_regexp = /\d+/, mobile_validator_regexp = /^0?(13[0-9]|15[012356789]|18[0-9]|14[57]|170)[0-9]{8}$/, numble_validator_regexp = /^\d+(\.*)(\d*)$/, zip_code_validator_regexp = /^[\d|\w]+$/, zip_code_regexp = {
                kr: /^\d{5}$/,
                us: /^\d{5}$/
            }, address_for_not_cn_regexp = /^[a-zA-Z0-9-!@#$%^&*()_+|~=`{}\[\]:";'<>?,.\/\s]+$/, shipping_item_prefix = {
                last_name: I18n.t("js.shipping_address.last_name"),
                phone: I18n.t("js.shipping_address.phone"),
                address: I18n.t("js.shipping_address.address"),
                district: I18n.t("js.shipping_address.district"),
                street: I18n.t("js.shipping_address.street"),
                city: I18n.t("js.shipping_address.city"),
                state: I18n.t("js.shipping_address.state"),
                country: I18n.t("js.shipping_address.country"),
                zip_code: I18n.t("js.shipping_address.zip_code")
            }, $scope.step = 0, $scope.user_type = "guest", $scope.valid_zip_code = "unselect", $scope.valid_coupon = null, $scope.coupon_join_promotion = !1, $scope.coupon_cents = 0, $scope.coupons = [], $scope.credit_discount_cents = 0, $scope.china_selected = $scope.usa_selected = $scope.jp_selected = !1, $scope.tr_selected = !1, $scope.paypal_disabled = !1, $scope.migs_3party_disabled = !1, $scope.cybersource_disabled = !1, $scope.alipay_disabled = !1, $scope.alipay_unionpay_disabled = !1, $scope.cash_on_delivery_disabled = !1, $scope.transfer_payment_disabled = !1, $scope.globebill_disabled = !0, $scope.billing_country = null, $scope.payment_method = null, $scope.allow_use_payment_tokenization = !1, $scope.info_card = !0, $scope.last_name = "", $scope.choose_invoice = !1, $scope.invoice_type = "vat_common", $scope.invoice_title_radio = "personal", $scope.company_name = "", $scope.products_black_country = [], $scope.errors = {}, $scope.editing_old_address = !1, $scope.validating_address = !1, $scope.shipping_type = "", $scope.tcurrency = function() {
                return "CNY" === $scope.currency ? "RMB" : $scope.currency
            }, $scope.useNewStep2 = function() {
                return $scope.is_hk_airport_store_user ? !1 : $scope.cart.use_new_payment_method
            }, $scope.needBillingAddress = function() {
                return $scope.useNewStep2() || "cybersource_new" === $scope.gateway
            }, $scope.canUsePaymentMethod = function(e) {
                var t, n, r, o;
                return n = {
                    iDEAL: 1e6,
                    "Carte Bancaire": 1e6,
                    Giropay: 5e5
                }, o = $scope.subtotal_cents(), o > n[e] ? !1 : (t = {
                    nl: ["iDEAL"],
                    fr: ["Carte Bancaire"],
                    de: ["Giropay"]
                }, r = t[$scope.country], r && -1 !== r.indexOf(e))
            }, $scope.email = function() {
                return $scope.guest_email || $scope.user_email || ""
            }, $scope.useCoupon = function() {
                var e, t, n;
                return $scope.coupon_error = null, $scope.coupon ? $scope.coupon.length >= 8 && $scope.coupon.length <= 25 ? (e = $scope.country || "us", t = $scope.first_item_price(), n = $http.get(I18n.url("/coupon/" + e + "/" + t + "/" + $scope.coupon + ".json?email=" + $scope.email())), n.success(function(e) {
                    return $scope.coupon_error = null, $scope.valid_coupon = $scope.coupon, $scope.coupon_join_promotion = e.join_promotion, $scope.coupon_cents = e.price_cents, $scope.calculatePrice(), $scope.show_gift(), $scope.delivery_status = e.delivery_status, $scope.clearCredit()
                }), n.error(function(e) {
                    return $scope.valid_coupon = null, $scope.coupon_error = e.error, $scope.coupon_cents = 0, $scope.show_gift()
                })) : $scope.coupon_error = I18n.t("js.error.coupon.length_invalid") : void 0
            }, $scope.findUseCoupons = function() {
                var e;
                return e = $http.post(I18n.url("/coupon/order_use_coupons.json"), {
                    country_code: $scope.country
                }), e.success(function(e) {
                    return $scope.coupons = e.coupons
                }), e.error(function() {})
            }, $scope.selectCoupon = function(e) {
                var t;
                return t = $('.order-coupon-item[name="' + e + '"]'), t.hasClass("active") || ($(".order-coupon-item.active").removeClass("active"), t.addClass("active"), $scope.coupon = e, $scope.useCoupon(), t.removeClass("active")), !0
            }, $scope.cancelCoupon = function() {
                return $scope.valid_coupon ? ($scope.valid_coupon = null, $scope.coupon_cents = 0, $scope.coupon = null, $scope.show_gift()) : void 0
            }, $scope.coupon_promotion_status = function() {
                return $scope.valid_coupon && !$scope.coupon_join_promotion ? !1 : !0
            }, $scope.useCredit = function() {
                return $scope.credit_error = null, numble_validator_regexp.test($scope.credit) ? $scope.credit * $scope.subunit_to_unit() > $scope.credit_available() ? $scope.credit_error = I18n.t("js.error.credit.not_enough") : $scope.credit * $scope.subunit_to_unit() > $scope.subtotal_cents() + $scope.order.tax_cents - $scope.coupon_cents ? $scope.credit_error = I18n.t("js.error.credit.exceed_product_price") : ($scope.credit_error = null, $scope.valid_credit = !0, $scope.credit_discount_cents = parseInt($scope.credit * $scope.subunit_to_unit())) : $scope.credit_error = I18n.t("js.error.credit.format_incorrect")
            }, $scope.clearCredit = function() {
                return $scope.valid_credit = !1, $scope.credit_discount_cents = 0, $scope.credit = null
            }, $scope.shippingData = function() {
                return {
                    email: $scope.guest_email || $scope.user_email,
                    subtotal: $scope.subtotal_cents(),
                    coupon: $scope.coupon,
                    state: $scope.state,
                    city: $scope.city,
                    shipping_type: $scope.shipping_type,
                    t: $scope.ctype.t,
                    i: $scope.ctype.i,
                    q: $scope.ctype.q,
                    k: $scope.ctype.k,
                    asi: $scope.ctype.asi,
                    bind_care_id: $scope.cart.bind_care_id
                }
            }, $scope.initShippingType = function() {
                var e, t;
                return $scope.country && "global" !== $scope.country ? ($(".shipping-type").hide(), $scope.shipping_blocking = !0, e = $scope.tcurrency(), t = $http.get(I18n.url("/shipping/list/" + $scope.country + ".json"), {
                    params: $scope.shippingData()
                }), t.success(function(e) {
                    var t;
                    return t = !1, $scope.shipping_datas = [], $.each(e, function(e, n) {
                        var r, o;
                        return o = $('input[value="' + n.type + '"]'), r = 0 === n.price ? n.free_label : $filter("show_money")(n.price, $scope.tcurrency(), $scope.country), r = " (" + r + ")", o.next().text(r), n.price = r, $scope.shipping_datas.push(n), o.parent().parent().show(), n.type === $scope.shipping_type ? t = !0 : void 0
                    }), 0 === e.length && "" !== $scope.shipping_type ? $scope.shipping_type = "" : 0 === e.length || t || ($scope.shipping_type = e[0].type), $scope.calculateShipping(), $scope.shipping_blocking = !1
                }), t.error(function() {
                    return $scope.shipping_blocking = !1
                })) : void 0
            }, $scope.updateCountryProc = function() {
                var e;
                return $scope.errors = {}, $scope.validate_not_sale(), $scope.currency = countries[$scope.country], $scope.is_hk_airport_store_user && ($scope.currency = "HKD"), $scope.findUseCoupons(), $scope.useCoupon(), $scope.initShippingType(), $scope.china_selected = "cn" === $scope.country, $scope.usa_selected = "us" === $scope.country, $scope.mx_selected = "mx" === $scope.country, $scope.ae_selected = "ae" === $scope.country, $scope.jp_selected = "jp" === $scope.country, $scope.th_selected = "th" === $scope.country, $scope.id_selected = "id" === $scope.country, $scope.tr_selected = "hk" === $scope.country || "mo" === $scope.country || "tw" === $scope.country, $scope.transfer_selected = "USD" === $scope.currency || "EUR" === $scope.currency || "CNY" === $scope.currency || "JPY" === $scope.currency || "HKD" === $scope.currency || "AUD" === $scope.currency, $scope.paypal_disabled = $scope.migs_3party_disabled = $scope.cybersource_disabled = $scope.china_selected, $.inArray($scope.country, ["kr", "pr"]) > -1 && ($scope.paypal_disabled = !0), $scope.cash_on_delivery_disabled = $scope.alipay_disabled = $scope.alipay_unionpay_disabled = $scope.wxpay_disabled = !$scope.china_selected, $scope.transfer_payment_disabled = !$scope.transfer_selected, e = $scope.cart.disabled_gateway, $.each(["transfer_payment", "cash_on_delivery", "alipay", "alipay_unionpay", "wxpay"], function(t, n) {
                    return e[n] ? $scope[n + "_disabled"] = !0 : void 0
                }), $scope.gateway = $scope.china_selected ? "alipay" : "cybersource_new", $scope.isPaypalPromotionCountry() && "paypal" !== $scope.gateway && $scope.paypal_date && ($scope.gateway = "paypal"), !$scope.paypal_disabled && $scope.big_order() && ($scope.cybersource_disabled = !0, $scope.gateway = "paypal"), $scope.payment_method = $scope.useNewStep2() ? "Paypal" : null, $scope.discount = $scope["bundled_" + $scope.currency], $scope.showPaypalPromotionPayment(), $scope.showPaypalPromotionOrder(), $scope.update_dji_care_country_error($scope.country), $scope.is_hk_airport_store_user ? $scope.gateway = "cybersource_new" : void 0
            }, $scope.updateCart = function() {
                var e, t, n;
                return $scope.country && "global" !== $scope.country ? (e = $scope.cart.buy_now ? "t=bn" : "", e = 0 !== $scope.cart.bind_care_id ? e + "&bind_care_id=" + $scope.cart.bind_care_id : e, n = "facebook" === $scope.source ? "/buy/country_cart/" + $scope.country + ".json?product=" + $scope.cart.items[0].product.slug + "&" + e : "/buy/country_cart/" + $scope.country + ".json?" + e, t = $http.get(I18n.url(n)), t.success(function(e) {
                    return $scope.cart = JSON.parse(e.list), $scope.updateCountryProc(), $scope.show_gift()
                })) : void 0
            }, $scope.show_gift = function() {
                var request;
                return request = $http.get(I18n.url("/cart/cart-gift.js?currency=" + $scope.currency + "&country=" + $scope.country + "&source=checkout&coupon=" + ($scope.valid_coupon || "") + "&email=" + $scope.email())), request.success(function(data) {
                    return eval(data)
                })
            }, $scope.updateCountry = function() {
                return "" !== $scope.country && $scope.updateCart(), $scope.kr_selected = "kr" === $scope.country ? !0 : !1, $scope.check_dhl_place(), $scope.init_use_payment_token()
            }, $scope.updateState = function() {
                return "CA" === $scope.state ? ($scope.ca_selected = !0, $scope.ga_selected = !1) : "GA" === $scope.state ? ($scope.ga_selected = !0, $scope.ca_selected = !1) : ($scope.ca_selected = !1, $scope.ga_selected = !1)
            }, $scope.check_dhl_place = function() {
                var e, t;
                return $scope.show_two_batteries_tip = "mo" === (e = $scope.country) || "hk" === e || "au" === e || "nz" === e || "kr" === e || "tw" === e || "my" === e || "sg" === e || "no" === e || "ch" === e || "li" === e || "pr" === e || "us" === $scope.country && ("AS" === (t = $scope.state) || "GU" === t || "MP" === t || "UM" === t || "VI" === t) ? !0 : !1
            }, $scope.updateCity = function() {
                var e, t, n, r, o, i, s, a;
                for (r = [], s = $scope.cart.items, o = 0, i = s.length; i > o; o++) t = s[o], r.push(t.product.slug);
                return n = ["dji-visiting-service", "phantom-3-professional-visiting-service", "phantom-3-advanced-visiting-service", "phantom-3-standard-visiting-service"], e = ["娣卞湷甯�", "鏉窞甯�", "骞垮窞甯�", "瑗垮畨甯�", "鑻忓窞甯�", "鍗椾含甯�"], Array.prototype.some || (Array.prototype.some = function(e) {
                    var t;
                    return function() {
                        var n, r, o;
                        for (o = [], n = 0, r = this.length; r > n; n++) t = this[n], e(t) && o.push(t);
                        return o
                    }.call(this).length > 0
                }), $scope.sz_selected = r.some(function(e) {
                    return __indexOf.call(n, e) >= 0
                }) && (a = $scope.city, __indexOf.call(e, a) < 0), $scope.has_p4 = __indexOf.call(r, "phantom-4") >= 0, $scope.has_p3p = __indexOf.call(r, "phantom-3-professional") >= 0, $scope.has_osmo = __indexOf.call(r, "osmo") >= 0, $scope.has_p4_and_backpack = $scope.has_p4 && __indexOf.call(r, "multifunctional-backpack-for-phantom-series") >= 0
            }, $scope.init_after_sale = function() {
                return $scope.is_after_sale() && (0 === $scope.checkout_step || 1 === $scope.checkout_step && $scope.finishStep(0)) ? ($scope.gateway = "alipay", $scope.gotoStep(3)) : void 0
            }, $scope.init_virtual = function() {
                return $scope.is_virtual_address() && (0 === $scope.checkout_step || 1 === $scope.checkout_step && $scope.finishStep(0)) ? $scope.gotoStep(2) : void 0
            }, $scope.is_after_sale = function() {
                return $scope.cart.buy_now && $scope.cart.after_sale && 1 === $scope.cart.items.length && $scope.cart.items[0].product.is_after_sale
            }, $scope.is_virtual_address = function() {
                return $scope.cart.virtual_address
            }, $scope.big_order = function() {
                var e;
                return e = $scope.subtotal_cents(), ("us" === $scope.country || "ca" === $scope.country) && e > 5e5 || "au" === $scope.country && e > 7e5 || "jp" === $scope.country && e > 6e5
            }, $scope.calculateShipping = function() {
                var e, t;
                return $scope.is_hk_airport_store_user ? ($scope.order.shipping_fee_cents = 0, void 0) : $scope.country && "global" !== $scope.country ? ($scope.shipping_blocking = !0, e = $scope.guest_email || $scope.user_email, t = $http.get("/shipping/" + $scope.country + "/" + $scope.cart.weights + ".json", {
                    params: {
                        email: e,
                        subtotal: $scope.subtotal_cents(),
                        coupon: $scope.coupon,
                        state: $scope.state,
                        city: $scope.city,
                        shipping_type: $scope.shipping_type,
                        t: $scope.ctype.t,
                        i: $scope.ctype.i,
                        q: $scope.ctype.q,
                        k: $scope.ctype.k,
                        asi: $scope.ctype.asi,
                        bind_care_id: $scope.cart.bind_care_id
                    }
                }), t.success(function(e) {
                    return $scope.order.shipping_fee_cents = e.price || 0, $scope.calculateTax(), $scope.shipping_blocking = !1
                }), t.error(function() {
                    return $scope.shipping_blocking = !1
                })) : void 0
            }, $scope.shipping_fee_label = function() {
                return $filter("show_money")($scope.order.shipping_fee_cents, $scope.tcurrency(), $scope.country)
            }, $scope.calculateTax = function() {
                var e, t;
                if ($scope.is_hk_airport_store_user) return $scope.order.vat_tax_cents = 0, void 0;
                if ($scope.country) {
                    if (e = vat_tax[$scope.country]) return $scope.order.tax_cents = 0, t = $scope.total_cents() / (1 + e), $scope.order.vat_tax_cents = parseInt(t * e);
                    if ($scope.order.vat_tax_cents = 0, $scope.zip_code && 5 === $scope.zip_code.length) return $scope.valid_zip_code = $scope.zip_code, $scope.order.tax_cents = $scope.zip_code_tax[$scope.zip_code] || 0
                }
            }, $scope.showGstTitle = function() {
                return -1 !== ["sg", "my", "jp", "hk", "au", "nz"].indexOf($scope.country)
            }, $scope.calculatePrice = function() {
                return $scope.calculateShipping()
            }, $scope.subtotal_cents = function() {
                var e, t;
                return e = $scope.country, $scope.is_hk_airport_store_user && (e = "hk"), t = $scope.coupon_promotion_status() ? $scope.cart.subtotals[e] || $scope.cart.subtotals[$scope.currency] : $scope.cart.origin_subtotals[e] || $scope.cart.origin_subtotals[$scope.currency], t ? t.cents : "-"
            }, $scope.total_cents = function() {
                return "-" === $scope.subtotal_cents() ? "-" : $scope.subtotal_cents() + $scope.order.tax_cents + $scope.order.shipping_fee_cents - $scope.coupon_cents - $scope.credit_discount_cents
            }, $scope.sf_total_cents_valid = function() {
                var e;
                return e = $scope.total_cents(), "-" !== e && e >= $scope.sf_price_range[0] && e <= $scope.sf_price_range[1] ? !0 : !1
            }, $scope.credit_available = function() {
                return $scope.user && $scope.user.id ? $scope.user.credit[$scope.currency] : 0
            }, $scope.is_a_mobile = function() {
                return "cn" === $scope.country && mobile_validator_regexp.test($scope.phone)
            }, $scope.create = function() {
                var e;
                return setTimeout(function() {
                    return window.ga && "cn" === $scope.country ? ga("send", "event", "Button", "click", "pc_checkout_place_order") : void 0
                }), $scope.submiting = !0, e = $http.post(I18n.url("/orders.json"), $scope.postData()), e.success(function(e) {
                    return $scope.errors = {}, $scope.pay_with = e.pay_with, $scope.url = e.url, $scope.goto_pay(e)
                }), e.error(function(e) {
                    return $scope.errors = e.errors
                }), e["finally"](function() {
                    return $scope.submiting = !1
                })
            }, $scope.goto_pay = function(e) {
                var t;
                return t = I18n.url("/orders/" + e.uuid + "/redirect?_statistics=true"), $http.get(t), $window.location = $scope.url
            }, $scope.pay_with_paypal = function() {
                return "paypal" === $scope.pay_with
            }, $scope.pay_with_worldpay = function() {
                return "worldpay" === $scope.pay_with
            }, $scope.pay_with_alipay = function() {
                return "alipay" === $scope.pay_with
            }, $scope.pay_with_wxpay = function() {
                return "wxpay" === $scope.pay_with
            }, $scope.pay_with_creditcard = function() {
                var e;
                return e = $scope.pay_with, "cybersource_new" === e || "migs_3party" === e || "migs" === e
            }, $scope.postData = function() {
                var e, t, n, r, o, i, s, a, c, u, l;
                for ($scope.get_autofill_value(), s = {
                        first_name: $scope.first_name,
                        last_name: $scope.last_name || "",
                        phone: $scope.phone,
                        district: $scope.district,
                        street: $scope.street,
                        city: $scope.city,
                        state: $scope.state,
                        country: $scope.country,
                        zip_code: $scope.zip_code,
                        address: $scope.address,
                        address2: $scope.address2,
                        category: $scope.category()
                    }, o = [], l = $scope.cart.items, c = 0, u = l.length; u > c; c++) t = l[c], o.push({
                    item_id: t.product.id,
                    item_type: t.product.type,
                    quantity: t.quantity
                });
                return i = {
                    items_attributes: o,
                    agreement: $scope.agreement,
                    shipping_address_attributes: s,
                    coupon: $scope.valid_coupon,
                    gateway: $scope.gateway,
                    credit_discount_cents: $scope.credit_discount_cents,
                    source: $scope.source
                }, $scope.is_a_mobile() && (i.track_with_sms = $scope.track_with_sms), $scope.payment_method && (i.payment_method_attributes = {
                    name: $scope.payment_method
                }), r = $scope.useNewStep2() ? "JCB" === $scope.payment_method : "cybersource_new" === $scope.gateway, r && $scope.allow_use_payment_tokenization && (i.payment_token_switch_attributes = {
                    turn_on: !0
                }, a = parseInt($scope.use_payment_token), 0 !== a && (i.payment_token_switch_attributes.user_payment_token_info_id = a)), $scope.choose_invoice && ("personal" === $scope.invoice_title_radio && ($scope.invoice_title = $scope.first_name + $scope.last_name), "company" === $scope.invoice_title_radio && ($scope.invoice_title = $scope.company_name), "vat_common_elc" === $scope.choose_invoice && ($scope.invoice_type = "vat_common_elc"), "vat_common" === $scope.choose_invoice && ($scope.invoice_type = "vat_common"), "" === $scope.choose_invoice && ($scope.invoice_type = ""), n = {
                    invoice_type: $scope.invoice_type,
                    invoice_title: $scope.invoice_title
                }, i.invoice_attributes = n), $scope.china_selected || "cybersource_new" !== $scope.gateway && "worldpay" !== $scope.gateway || $scope.is_hk_airport_store_user || (e = {
                    first_name: $scope.billing_first_name,
                    last_name: $scope.billing_last_name || "",
                    phone: $scope.billing_phone,
                    city: $scope.billing_city,
                    state: $scope.billing_state,
                    country: $scope.billing_country,
                    zip_code: $scope.billing_zip_code,
                    address: $scope.billing_address,
                    address2: $scope.billing_address2
                }, i.billing_address_attributes = e), {
                    email: $scope.guest_email,
                    order: i,
                    t: $scope.ctype.t,
                    i: $scope.ctype.i,
                    q: $scope.ctype.q,
                    k: $scope.ctype.k,
                    asi: $scope.ctype.asi,
                    bind_care_id: $scope.cart.bind_care_id,
                    shipping_type: $scope.shipping_type
                }
            }, $scope.error = function(e) {
                var t;
                return $scope.errors && (t = $scope.errors[e]), t
            }, $scope.shipping_error = function(e) {
                var t;
                return $scope.errors && (t = $scope.errors["shipping_address." + e]), t ? t.join(",") : void 0
            }, $scope.CNShow = function(e) {
                return $scope.CountryShow(e, ["cn"])
            }, $scope.USShow = function(e) {
                return $scope.CountryShow(e, ["us"])
            }, $scope.CountryShow = function(e, t) {
                return -1 !== t.indexOf($scope[e])
            }, $scope.clearCityState = function() {
                return $scope.billing_country ? ($scope.billing_city = "", $scope.billing_state = "", $scope.billing_district = "", $scope.billing_street = "") : ($scope.city = "", $scope.state = "", $scope.district = "", $scope.street = "", $scope.first_name_label_change($scope.country))
            }, $scope.stateSelectable = function(e) {
                return $scope.CNShow(e) || $scope.USShow(e) || $scope.CountryShow(e, ["ca"])
            }, $scope.showDefaultCountry = function() {
                return $scope.country = "cn", $scope.use_default_country = !0
            }, $scope.subunit_to_unit = function() {
                return $scope.currencies[$scope.currency].subunit_to_unit
            }, $scope.symbol = function() {
                return $scope.currencies[$scope.currency].symbol
            }, $scope.get_autofill_value = function() {
                var e, t, n, r, o;
                for (r = ["first_name", "last_name", "phone", "address", "address2", "city", "state", "district", "street", "zip_code"], o = [], t = 0, n = r.length; n > t; t++) e = r[t], $scope[e] ? o.push(void 0) : o.push($scope[e] = $("#" + e).val() || "");
                return o
            }, $scope.update_dji_care_country_error = function(e) {
                return $scope.errors.invalid_dji_care_country = $scope.cart.dji_care_country && $scope.cart.dji_care_country !== e ? !0 : !1
            }, $scope.show_shipping_method = function() {
                return $scope.cart.show_shipping_method
            }, $scope.is_new_address = function() {
                return 0 === parseInt($scope.use_address)
            }, $scope.filter_china_payments = function(e) {
                return "ShipServices::SfExpressExpedited" === e ? ($scope.cash_on_delivery_disabled = !0, $scope.transfer_payment_disabled = !0) : "ShipServices::SfExpressStandard" === e ? ($scope.cash_on_delivery_disabled = !1, $scope.transfer_payment_disabled = !1) : void 0
            }, $scope.$watch("user.shipping_address", function(e) {
                return e && e[0] ? ($scope.use_address = e[0].id, $scope.is_hk_airport_store_user || $scope.choose_address(e[0]), $scope.using_address = e[0]) : void 0
            }), $scope.$watch("country", function() {
                return $scope.updateCountry(), $scope.clearCredit()
            }), $scope.$watch("state", function() {
                return $scope.initShippingType(), $scope.updateState()
            }), $scope.$watch("city", function() {
                return $scope.updateCity()
            }), $scope.$watch("district", function(e) {
                return e ? $scope.sf_delivery_address_valid($scope.state + $scope.city, $scope.district) : void 0
            }), $scope.$watch("total_cents()", function() {
                var e;
                return "cash_on_delivery" !== $scope.gateway || $scope.sf_delivery_available() || (e = $('#step2 button[ng-click="edit(2)"'), e.hasClass("ng-hide")) ? void 0 : setTimeout(function() {
                    return e.click(), $scope.gateway = ""
                }, 10)
            }), $scope.$watch("shipping_type", function(e) {
                return $scope.shipping_blocking = !0, e && ($scope.filter_china_payments(e), $.each($scope.shipping_datas, function(e, t) {
                    return t.type === $scope.shipping_type ? ($scope.shipping_data = t, !1) : void 0
                })), $scope.shipping_data || ($scope.shipping_data = ""), $scope.calculateShipping(), $scope.shipping_blocking = !1
            }), $scope.checkout_as_guest = function() {
                return $scope.validate_user_info() ? $scope.gotoStep1() : (alert($scope.errors.guest_email || $scope.errors.guest_email_confirmation), void 0)
            }, $scope.noPaste = function(e) {
                return e.preventDefault()
            }, $scope.login = function() {
                var e, t, n;
                return $scope.validate_user_login_info() ? ($scope.login_submiting = !0, t = {
                    email: $scope.user_email,
                    password: $scope.password,
                    cart_id: $scope.cart_id,
                    methods: ["shipping_address"],
                    t: $scope.ctype.t,
                    i: $scope.ctype.i,
                    q: $scope.ctype.q,
                    k: $scope.ctype.k,
                    asi: $scope.ctype.asi
                }, n = $http.post(I18n.url("/sessions.json"), t), n.success(function(e) {
                    return $scope.is_hk_airport_store_user && $window.location.reload(), $scope.checkout_step = 0, $scope.user_login_with(e), $scope.gotoStep1(), $scope.init_use_payment_token()
                }), n.error(function(e) {
                    return alert(e.error)
                }), n["finally"](function() {
                    return $scope.login_submiting = !1
                })) : ((e = $scope.errors.user_email) ? alert(e) : alert($scope.errors.password), void 0)
            }, $scope.show_address_edit = function() {
                return 0 === parseInt($scope.use_address) || $scope.editing_old_address
            }, $scope.edit_old_address = function(e) {
                var t, n;
                return $scope.editing_old_address = !0, $scope.use_address = e, t = $("input[value=" + $scope.use_address + "]"), t.attr("disabled", "disabled"), n = t.parent(), n.prevUntil().hide(), n.nextUntil().hide(), !0
            }, $scope.edit_old_address_cancel = function() {
                var e, t;
                return $scope.editing_old_address = !1, e = $("input[value=" + $scope.use_address + "]"), e.removeAttr("disabled"), t = e.parent(), t.prevUntil().show(), t.nextUntil().show(), setTimeout(function() {
                    return e.click()
                }, 100), !0
            }, $scope.save_old_address = function() {
                var e;
                return $scope.errors = {}, $scope.validate_address() ? ($scope.errors = {}, e = parseInt($scope.use_address), $.ajax({
                    url: $scope.user.edit_address_url,
                    type: "POST",
                    dataType: "JSON",
                    data: {
                        id: e,
                        addrs: $scope.postData().order.shipping_address_attributes
                    },
                    success: function(t) {
                        var n, r;
                        return 0 === t.status ? (r = [], n = t.data.address, $.each($scope.user.shipping_address, function(t, o) {
                            return o.id === e ? (n.$$hashKey = o.$$hashKey, r.push(n)) : r.push(o), $scope.user.shipping_address = r, $scope.use_address = e, $scope.using_address = r, $scope.edit_old_address_cancel()
                        })) : ($scope.set_errors(t.errors), $scope.$apply())
                    }
                }), $scope.check_dhl_place()) : !1
            }, $scope.set_errors = function(e) {
                return $scope.errors = {}, "string" == typeof e ? $scope.errors = e : e.constructor === Object ? $.each(e, function(e, t) {
                    return $scope.errors[e] = Array.isArray(t) ? t[0] : t
                }) : void 0
            }, $scope.configs = function() {
                return $scope.cart.items[0].product.configs
            }, $scope.category = function() {
                return $scope.configs().category || ""
            }, $scope.validate_address_remote = function(e, t) {
                var n, r, o;
                return $scope.validating_address = !0, o = parseInt($scope.use_address), r = 0 !== o ? {
                    id: $scope.use_address
                } : {
                    addrs: $scope.postData().order.shipping_address_attributes
                }, r.category = $scope.category(), n = $http.post(I18n.url("/user/validate_address.json"), r), n.success(function(n) {
                    return 0 === n.status ? ($scope.validating_address = !1, e(), $scope.findUseCoupons()) : ($scope.validating_address = !1, $scope.set_errors(n.errors), t())
                }), n.error(function() {
                    return $scope.validating_address = !1, t()
                })
            }, $scope.save_address = function() {
                var e;
                if (!$scope.validate_address()) return e = $scope.errors, 0 !== parseInt($scope.use_address) && ($scope.edit_old_address($scope.use_address), null !== $scope.using_address && $scope.choose_address($scope.using_address, !0), $scope.errors = e), void 0;
                if ($scope.validate_invoice()) return $scope.validate_address_remote(function() {
                    return $scope.gotoStep(2)
                }, function() {
                    return 0 !== parseInt($scope.use_address) ? $scope.edit_old_address($scope.use_address) : void 0
                }), $scope.check_dhl_place()
            }, $scope.clear_billing = function() {
                return "paypal" === $scope.gateway ? $scope.is_same_address = !0 : void 0
            }, $scope.save_payment = function() {
                return $scope.is_same_address === !0 && ($scope.billing_first_name = $scope.first_name, $scope.billing_last_name = $scope.last_name || "", $scope.billing_address = $scope.address, $scope.billing_address2 = $scope.address2, $scope.billing_city = $scope.city, $scope.billing_state = $scope.state, $scope.billing_country = $scope.country, $scope.billing_zip_code = $scope.zip_code, $scope.billing_phone = $scope.phone), $scope.validate_billing_address() && ($scope.gateway || $scope.useNewStep2()) ? $scope.gotoStep(3) : void 0
            }, $scope.gateway_text = function() {
                return $scope[$scope.gateway]
            }, $scope.country_text = function(e) {
                return $("#billing_country option[value='" + e + "']").text()
            }, $scope.sf_delivery_available = function() {
                return $scope.CNShow("country") && "cash_on_delivery" === $scope.gateway ? $scope.sf_delivery_availability && $scope.sf_total_cents_valid() : !0
            }, $scope.sf_delivery_address_valid = function(e, t) {
                var n;
                return $scope.CNShow("country") ? (n = $http.get(I18n.url("/user/sf_delivery_available?state_city=" + e + "&district=" + t)), n.success(function(e) {
                    return 0 === e.status ? $scope.sf_delivery_availability = !0 : ("cash_on_delivery" === $scope.gateway && ($scope.gateway = "alipay"), $scope.sf_delivery_availability = !1)
                }), n.error(function() {
                    return "cash_on_delivery" === $scope.gateway && ($scope.gateway = "alipay"), $scope.sf_delivery_availability = !1
                })) : void 0
            }, $scope.choose_address = function(e, t) {
                var n;
                return null == e && (e = null), null == t && (t = !1), $scope.errors = {}, n = $("#china-city-init").data("input-tip"), e ? ($("#china-city-init").data("state", e.state).data("city", e.city).data("district", e.district).data("street", e.street), $scope.first_name = e.first_name, $scope.last_name = e.last_name || "", $scope.address = e.address, $scope.address2 = e.address2, $scope.country = e.country, $scope.zip_code = e.zip_code, $scope.phone = e.phone, $scope.first_name_label_change($scope.country), t && "cn" === e.country && $scope.editing_old_address ? setTimeout(function() {
                    var t, r, o, i;
                    for (i = ["#city", "#district", "#street"], r = 0, o = i.length; o > r; r++) t = i[r], $(t).prepend("<option value=''>" + n + "</option>");
                    return $(".state").val(e.state).change()
                }, 100) : ($scope.district = e.district, $scope.street = e.street, $scope.city = e.city, $scope.state = e.state), $scope.using_address = e) : ($scope.use_address = 0, $("#china-city-init").data("state", "").data("city", "").data("district", "").data("street", ""), $scope.first_name = "", $scope.last_name = "", $scope.address = "", $scope.address2 = "", $scope.district = "", $scope.street = "", $scope.city = "", $scope.country = $scope.global_country, $scope.state = "", $scope.zip_code = "", $scope.phone = "", $scope.first_name_label_change($scope.country), setTimeout(function() {
                    var e, t, r, o, i;
                    for (o = ["#city", "#district", "#street"], i = [], t = 0, r = o.length; r > t; t++) e = o[t], $(e).html(""), i.push($(e).prepend("<option value=''>" + n + "</option>"));
                    return i
                }, 100)), !0
            }, $scope.first_name_label_change = function(e) {
                return $scope.first_name_label = "cn" === e ? I18n.t("js.user.full_name") : "cn" !== e && "zh-CN" === I18n.locale ? I18n.t("js.user.first_name_cn") : I18n.t("js.user.first_name")
            }, $scope.user_login_with = function(e) {
                var t, n, r;
                $scope.update_csrf_token(e.token), r = [];
                for (t in e) n = e[t], r.push($scope.user[t] = n);
                return r
            }, $scope.islogin = function() {
                return $scope.user && $scope.user.id
            }, $scope.update_csrf_token = function(e) {
                return $('meta[name="csrf-token"]').attr("content", e), $http.defaults.headers.common["X-CSRF-Token"] = e
            }, $scope.first_item_price = function() {
                return $scope.cart.items[0].product.prices[$scope.currency].cents
            }, $scope.price = function(e) {
                var t, n, r;
                return n = $scope.currency, t = $scope.country, $scope.is_hk_airport_store_user && (t = "hk"), r = $scope.coupon_promotion_status() ? e.product.prices[t] || e.product.prices[n] : e.product.origin_prices[t] || e.product.origin_prices[n], r ? r.cents : "-"
            }, $scope.validate_not_sale = function() {
                var e;
                return e = $scope.country, $scope.products_black_country = [], $scope.errors.country_not_ship_to_step_1 = null, $.each($scope.cart.items, function(t, n) {
                    var r;
                    return r = n.product, -1 !== r.not_sale_countries.indexOf(e) ? ($scope.errors.country_not_ship_to_step_1 = !0, $scope.products_black_country.push(r.title)) : void 0
                })
            }, $scope.gotoStep = function(e) {
                return $scope.step = e
            }, $scope.gotoStep1 = function() {
                return $scope.gotoStep(1), $scope.scrollTo(1), $scope.updateCountryProc(), $scope.calculateShipping(), $scope.init_after_sale(), $scope.init_virtual()
            }, $scope.edit = function(e) {
                return $scope.step = e
            }, $scope.finishStep = function(e) {
                return e < $scope.step
            }, $scope.isStep = function(e) {
                return $scope.step === e
            }, $scope.validate_user_info = function() {
                return $scope.errors.guest_email = $scope.errors.guest_email_confirmation = null, $scope.guest_email || ($scope.errors.guest_email = I18n.t("js.error.blank_email")), $scope.guest_email && !email_validator_regexp.test($scope.guest_email) && ($scope.errors.guest_email = I18n.t("js.error.invalid_email")), $scope.guest_email !== $scope.guest_email_confirmation && ($scope.errors.guest_email_confirmation = I18n.t("js.error.anonymous_order.email_not_match")), !$scope.errors.guest_email && !$scope.errors.guest_email_confirmation
            }, $scope.validate_user_login_info = function() {
                return $scope.errors.user_email = $scope.errors.password = null, $scope.user_email || ($scope.errors.user_email = I18n.t("js.error.blank_email")), $scope.user_email && !email_validator_regexp.test($scope.user_email) && ($scope.errors.user_email = I18n.t("js.error.invalid_email")), $scope.password || ($scope.errors.password = I18n.t("js.error.blank_password")), !$scope.errors.user_email && !$scope.errors.password
            }, $scope.address_error = function() {
                var e, t, n, r, o, i, s;
                if ("cn" === $scope.country) {
                    for (i = ["first_name", "phone", "address", "city", "state", "country", "zip_code", "district", "street", "invalid_dji_care_country"], t = 0, r = i.length; r > t; t++)
                        if (e = i[t], $scope.error(e)) return !0
                } else
                    for (s = ["first_name", "last_name", "phone", "address", "city", "state", "country", "zip_code", "invalid_dji_care_country"], n = 0, o = s.length; o > n; n++)
                        if (e = s[n], $scope.error(e)) return !0;
                return !1
            }, $scope.validate_invoice = function() {
                var e;
                return $scope.is_hk_airport_store_user ? !0 : (e = !0, "cn" === $scope.country ? (e = $scope.validate("invoice_title"), e && (e = $scope.validate("select_invoice_type")), e && ($scope.choose_invoice && "personal" === $scope.invoice_title_radio && ($scope.invoice_title = $scope.first_name + $scope.last_name), $scope.choose_invoice && "company" === $scope.invoice_title_radio && ($scope.invoice_title = $scope.company_name))) : ($scope.choose_invoice = !1, $scope.invoice_type = "", $scope.invoice_title = "", $scope.company_name = ""), e)
            }, $scope.validate_address = function() {
                var e, t, n, r, o, i, s, a, c, u, l;
                if (t = !0, "cn" === $scope.country)
                    for (c = ["first_name", "phone", "address", "city", "state", "country", "district", "street"], n = 0, i = c.length; i > n; n++) e = c[n], $scope.validate(e) || (t = !1);
                else {
                    for (u = ["first_name", "last_name", "phone", "address", "city", "state", "country", "zip_code"], r = 0, s = u.length; s > r; r++) e = u[r], $scope.validate(e) || (t = !1);
                    if ("jp" !== $scope.country && "de" !== $scope.country && "at" !== $scope.country && "li" !== $scope.country && "hk" !== $scope.country && "mo" !== $scope.country && "tw" !== $scope.country && "kr" !== $scope.country)
                        for (l = ["address", "address2", "state", "city", "first_name", "last_name"], o = 0, a = l.length; a > o; o++) e = l[o], $scope[e] && (address_for_not_cn_regexp.test($scope[e]) || ($scope.errors[e] = I18n.t("js.error.invalid_address"), t = !1))
                }
                return $scope.phone && !phone_validator_regexp.test($scope.phone) && ($scope.errors.phone = I18n.t("js.error.invalid_phone"), t = !1), zip_code_regexp[$scope.country] && !zip_code_regexp[$scope.country].test($scope.zip_code) && ($scope.errors.zip_code = I18n.t("js.error.invalid_zip_code"), t = !1), t
            }, $scope.validate_billing_address = function() {
                var e, t, n, r, o, i, s, a;
                if ($scope.is_hk_airport_store_user) return !0;
                if (t = !0, "cn" === $scope.billing_country)
                    for (s = ["billing_first_name", "billing_phone", "billing_address", "billing_city", "billing_state", "billing_country"], n = 0, o = s.length; o > n; n++) e = s[n], $scope.validate(e) || (t = !1);
                else
                    for (a = ["billing_first_name", "billing_last_name", "billing_phone", "billing_address", "billing_city", "billing_state", "billing_country", "billing_zip_code"], r = 0, i = a.length; i > r; r++) e = a[r], $scope.validate(e) || (t = !1);
                return $scope.phone && !phone_validator_regexp.test($scope.phone) && ($scope.errors.phone = I18n.t("js.error.invalid_phone"), t = !1), null !== $scope.billing_country || zip_code_regexp.us.test($scope.billing_zip_code) || ($scope.errors.billing_zip_code = I18n.t("js.error.invalid_zip_code"), t = !1), zip_code_regexp[$scope.billing_country] && !zip_code_regexp[$scope.billing_country].test($scope.billing_zip_code) && ($scope.errors.billing_zip_code = I18n.t("js.error.invalid_zip_code"), t = !1), t
            }, $scope.validate = function(e) {
                var t;
                return $scope.errors[e] = null, "invoice_title" === e ? $scope.choose_invoice && "company" === $scope.invoice_title_radio && !$scope.company_name && ($scope.errors[e] = "鍙戠エ鎶ご 蹇呭～", $scope.errors.select_invoice_type = "") : "select_invoice_type" === e ? ($scope.choose_invoice === !1 || "undefined" === $scope.choose_invoice) && ($scope.errors[e] = "璇烽€夋嫨鍙戠エ淇℃伅") : (t = "first_name" === e ? $scope.first_name_label_change($scope.country) : shipping_item_prefix[e], $scope[e] || ($scope.errors[e] = I18n.t("js.error.blank")), $scope[e] && $scope[e].length > 255 && ($scope.errors[e] = I18n.t("js.error.too_long", {
                    count: 255
                }))), !$scope.errors[e]
            }, $scope.goto_other_product = function() {
                return $window.location = $scope.product_url
            }, $scope.scrollTo = function(e) {
                return $timeout(function() {
                    var t;
                    return t = angular.element(document.getElementById("step" + e)), $document.scrollToElement(t, 0, 2e3)
                }, 100)
            }, $scope.showP2sEdmPromotionOrder = function() {
                var e;
                return e = $scope.user.is_join_p2s_edm, void 0 !== e && e
            }, $scope.showP2sBatEdmPromotionOrder = function() {
                var e;
                return e = $scope.user.is_join_p2s_bat_edm, void 0 !== e && e
            }, $scope.showPaypalPromotionPayment = function() {
                return $scope.isPaypalPromotionCountry()
            }, $scope.showPaypalPromotionOrder = function() {
                return $scope.isPaypalPromotionCountry() && "paypal" === $scope.gateway
            }, $scope.isPaypalPromotionCountry = function() {
                return "USD" === $scope.currency && ("sg" === $scope.country || "kr" === $scope.country || "hk" === $scope.country || "tw" === $scope.country)
            }, $scope.select_invoice = function() {
                return $scope.choose_invoice ? $scope.company_name = "" : void 0
            }, $scope.$watch("invoice_title_radio", function() {
                return "company" === $scope.invoice_title_radio ? ($(".company-name").show(), $(".company-input").focus()) : $(".company-name").hide()
            }), $scope.showChineseNewYearShippingTip = function() {
                var e, t, n, r, o, i;
                return n = new Date, r = new Date(n.getTime() + 6e4 * n.getTimezoneOffset()), t = Date.UTC(2016, 1, 4, 16, 0, 0), e = Date.UTC(2016, 1, 10, 16, 0, 0), i = Date.UTC(2016, 1, 6, 8, 0, 0), o = Date.UTC(2016, 1, 14, 8, 0, 0), -1 !== ["pr", "hk", "mo", "tw", "kr", "au", "nz", "co", "my", "sg", "ch", "no", "li"].indexOf($scope.country) && r >= i && o > r ? !0 : -1 !== ["cn"].indexOf($scope.country) && r >= t && e > r ? !0 : !1
            }, $scope.showCnChineseNewYearShippingTip = function() {
                return "cn" === $scope.country
            }, $scope.step_1_next_btn_disabled = function() {
                return $scope.islogin() && ($scope.error("country_not_ship_to_step_1") || $scope.editing_old_address || $scope.validating_address || $scope.shipping_blocking || $scope.address_error() && !$scope.is_new_address())
            }, $scope.remove_payment_token_info = function(e) {
                var t;
                return console.log("remove payment_token info id: ", e), $scope.remove_payment_token_processing = !0, t = $http["delete"](I18n.url("/user/payment_token_infos/" + e + ".json")), t.success(function() {
                    return $scope.remove_local_payment_token_info(e)
                }), t.error(function(t, n) {
                    return 404 === n && $scope.remove_local_payment_token_info(e), 422 === n ? alert(t.message) : void 0
                }), t["finally"](function() {
                    return $scope.remove_payment_token_processing = !1
                })
            }, $scope.remove_local_payment_token_info = function(e) {
                var t;
                return t = $scope.user.payment_token_infos.findIndex(function(t) {
                    return t.id === e
                }), $scope.user.payment_token_infos.splice(t, 1), $scope.init_use_payment_token()
            }, $scope.init_use_payment_token = function() {
                return "us" === $scope.country && $scope.islogin() ? $scope.user.payment_token_infos[0] ? ($scope.use_payment_token = $scope.user.payment_token_infos[0].id, $scope.allow_use_payment_tokenization = !0) : $scope.use_payment_token = 0 : $scope.allow_use_payment_tokenization = !1
            }, $scope.save_my_order_text = function() {
                return $scope.info_card = $scope.allow_use_payment_tokenization ? !1 : !0
            }, $scope.isCredit = function() {
                var e;
                return "us" === $scope.country ? (e = $scope.useNewStep2() ? "JCB" === $scope.payment_method : "cybersource_new" === $scope.gateway, e ? !0 : !1) : !1
            }
        }]
    }.call(this),
    function() {
        this.ProductsCtrl = ["$scope", "$http", "$window", "$filter", function($scope, $http, $window, $filter) {
            return $scope.bind_care = 0, $scope.tcurrency = function() {
                return "CNY" === $scope.currency ? "RMB" : $scope.currency
            }, $scope.select = function(e) {
                var t;
                if (e.selected) {
                    if (!$scope.find(e)) return $scope.cart.items.push(e)
                } else if (t = $scope.find(e), null != t) return $scope.cart.items.splice(t, 1)
            }, $scope.selected = function(e) {
                return null != $scope.find(e)
            }, $scope.select_combo = function(e) {
                return e.selected ? $.each(e.products, function(e, t) {
                    return $scope.find(t) ? void 0 : $scope.cart.items.push(t)
                }) : $.each(e.products, function(e, t) {
                    var n;
                    return n = $scope.find(t), null != n ? $scope.cart.items.splice(n, 1) : void 0
                })
            }, $scope.subtotal = function() {
                var e, t, n, r, o, i, s, a, c;
                for (n = $scope.cart.select_candidate_product.price, a = $scope.cart.items, r = 0, i = a.length; i > r; r++) t = a[r], n += t.price;
                for (c = $scope.cart.combos, o = 0, s = c.length; s > o; o++) e = c[o], e.selected && (n -= e.price);
                return n
            }, $scope.money_or_status_text = function() {
                return $scope.cart.select_candidate_product.on_sale || $scope.cart.select_candidate_product.out_of_stock ? $scope.cart.select_candidate_product.price_str : $scope.cart.select_candidate_product.status_text
            }, $scope.promotion_link = function() {
                return ""
            }, $scope.add_to_cart = function() {
                return $scope.loading = !0, $scope.float_cart()
            }, $scope.float_cart = function() {
                var cart_items, product, request, slt_p;
                return cart_items = function() {
                    var e, t, n, r;
                    for (n = $scope.cart.items, r = [], e = 0, t = n.length; t > e; e++) product = n[e], r.push(product.slug);
                    return r
                }(), cart_items.unshift($scope.cart.select_candidate_product.slug), slt_p = $scope.cart.select_candidate_product, request = $http.post(I18n.url("/cart/float-cart.js"), {
                    s_slug: slt_p.slug,
                    product_slugs: cart_items,
                    quantity: $scope.cart.product.quantity,
                    combo_id: $scope.cart.combo_id,
                    bind_care: $scope.need_bind_care(),
                    bind_care_id: $scope.bind_care
                }), request.success(function(data) {
                    return eval(data), $scope.loading = !1
                }), request.error(function() {
                    return $scope.loading = !1
                })
            }, $scope.stock_notice = function() {
                var request;
                return $scope.loading = !0, request = $http.get(I18n.url("/stock_notices/new.js?stock_notice[item_id]=" + $scope.cart.select_candidate_product.id + "&stock_notice[item_type]=" + $scope.cart.select_candidate_product.item_type)), request.success(function(data) {
                    return $scope.loading = !1, eval(data)
                }), request.error(function() {
                    return $scope.loading = !1
                })
            }, $scope.buy_now = function() {
                var e, t, n, r, o;
                return $scope.loading = !0, t = $scope.cart.combo_id, r = $scope.cart.select_candidate_product.variant_id, n = t ? "_" + t : r, e = $scope.bind_care || 0, o = "https://" + window.location.host + I18n.url("/buy/checkout?t=bn&i=" + n + "&q=" + $scope.cart.product.quantity + "&bind_care_id=" + e), $scope.new_checkout_login_url && (o = $scope.new_checkout_login_url + "?callback=" + encodeURIComponent(o)), window.location = o
            }, $scope.dji_care_buy_now = function() {
                var request;
                return $scope.loading = !0, request = $http.get(I18n.url("/dji_care_infos/new.js?id=" + $scope.cart.select_candidate_product.variant_id + "&q=" + $scope.cart.product.quantity)), request.success(function(data) {
                    return $scope.loading = !1, eval(data)
                }), request.error(function() {
                    return $scope.loading = !1
                })
            }, $scope.go_enjoy_buy_now = function() {
                var e;
                return e = $("#go-enjoy").data($scope.cart.select_candidate_product.variant_id.toString()), e ? $scope.go_enjoy_directly_checkout() : $scope.go_enjoy_eligible_and_checkout()
            }, $scope.go_enjoy_directly_checkout = function() {
                var e;
                return $scope.loading = !0, e = $http.get(I18n.url("/go-enjoy/eligibility_response?product_id=" + $scope.cart.select_candidate_product.variant_id + "&quantity=" + $scope.cart.product.quantity + "&zip_code=" + $('input[name="zip_code"]').val())), e.success(function(e) {
                    return 0 === e.status ? window.location = e.msg : $scope.loading = !1
                }), e.error(function() {
                    return $scope.loading = !1
                })
            }, $scope.go_enjoy_eligible_and_checkout = function() {
                var request;
                return $scope.loading = !0, request = $http.get(I18n.url("/go-enjoy/eligibility.js?i=" + $scope.cart.select_candidate_product.variant_id + "&q=" + $scope.cart.product.quantity)), request.success(function(data) {
                    return $scope.loading = !1, eval(data)
                }), request.error(function() {
                    return $scope.loading = !1
                })
            }, $scope.go_enjoy_eligible_validate = function() {
                var request;
                return $scope.loading = !0, request = $http.get(I18n.url("/go-enjoy/eligibility.js?i=" + $scope.cart.select_candidate_product.variant_id + "&q=" + $scope.cart.product.quantity + "&o=1")), request.success(function(data) {
                    return $scope.loading = !1, eval(data)
                }), request.error(function() {
                    return $scope.loading = !1
                })
            }, $scope.is_dji_care_main = function() {
                return "dji_care_main" === $scope.cart.select_candidate_product.function_status.category
            }, $scope.dji_care_title = function(e) {
                return e.care_short_title
            }, $scope.dji_care_id = function(e) {
                return e.care_id
            }, $scope.dji_care_price = function(e) {
                return e.care_price
            }, $scope.dji_care_normal_price = function(e) {
                return e.care_normal_price
            }, $scope.dji_care_price_changed = function(e) {
                return $scope.dji_care_price(e) !== $scope.dji_care_normal_price(e)
            }, $scope.goto_buy_now = function() {
                var e;
                return e = $scope.cart.select_candidate_product.function_status.category, "dji_care" === e ? $scope.dji_care_buy_now() : "go_enjoy" === e ? $scope.go_enjoy_buy_now() : $scope.buy_now()
            }, $scope.init_bind_care = function() {
                return $scope.bind_care = 0, $(".box-dji-care").removeClass("active")
            }, $scope.bind_care_status = function() {
                var e, t;
                return e = $(".box-dji-care"), t = !1, $.each(e, function(e, n) {
                    return $(n).hasClass("active") ? (t = !0, !1) : void 0
                }), t
            }, $scope.set_bind_care_checkbox = function(e) {
                return $('.box-dji-care[name="' + e.care_id + '"]').addClass("active")
            }, $scope.get_bind_care_status = function(e) {
                return $('.box-dji-care[name="' + e.care_id + '"]').hasClass("active")
            }, $scope.select_care_option = function(e) {
                return $scope.get_bind_care_status(e) ? $scope.init_bind_care() : ($scope.init_bind_care(), $scope.set_bind_care_checkbox(e, !0), $scope.bind_care = e.care_id)
            }, $scope.need_bind_care = function() {
                return $scope.is_dji_care_main() && $scope.bind_care_status()
            }, $scope.find = function(e) {
                var t, n, r, o, i;
                for (i = $scope.cart.items, t = r = 0, o = i.length; o > r; t = ++r)
                    if (n = i[t], n.id === e.id) return t
            }, $scope.increment = function() {
                return $scope.cart.product.quantity < $scope.cart.product.max_quantity || $scope.cart.select_candidate_product.slug.match(/special-\d+/) ? $scope.cart.product.quantity = parseInt($scope.cart.product.quantity) + 1 : $scope.valid_quantity()
            }, $scope.decrement = function() {
                return $scope.cart.product.quantity > 1 ? $scope.cart.product.quantity = parseInt($scope.cart.product.quantity) - 1 : $scope.valid_quantity()
            }, $scope.quantity_by_keybord = function(e) {
                return 38 === e.keyCode ? $scope.increment() : 40 === e.keyCode ? $scope.decrement() : void 0
            }, $scope.valid_quantity = function() {
                var e;
                return e = $scope.cart.product.max_quantity, !$scope.cart.product.quantity.toString().match(/^\d+$/) || $scope.cart.product.quantity < 1 ? $scope.cart.product.quantity = 1 : $scope.cart.product.quantity > e ? $scope.cart.product.quantity = e : void 0
            }
        }]
    }.call(this),
    function() {
        this.SessionsCtrl = ["$scope", "$http", "$window", function(e, t, n) {
            return e.login = function() {
                var r;
                return r = t.post("/sessions/0/login", {
                    password: e.password,
                    email: e.email
                }), r.success(function(t) {
                    return 1 === t.status ? n.location = "/admin_orders/" : e.errors = t.msg
                })
            }, e.logout = function() {
                var r;
                return r = t.post("/sessions/0/logout", {}), r.success(function(t) {
                    return 1 === t.status ? n.location = "/admin_orders/" : e.errors = t.msg
                })
            }
        }]
    }.call(this),
    function() {
        this.UserCtrl = ["$scope", "$http", "$window", function($scope, $http, $window) {
            return $scope.SaveReturn = function() {
                var e;
                return $scope.submiting = !0, e = $http.post("/user/order_returned_resp", {
                    edit_id: $scope.edit_id,
                    email: $scope.email,
                    reason: $scope.reason
                }), e.success(function(e) {
                    return 0 === e.status ? ($scope.success = !0, $window.location = I18n.url("/user/orders")) : $scope.errors = e.errors, $scope.submiting = !1
                })
            }, $scope.GetGoodsChk = function() {
                return $scope.sn.length < 11 ? $scope.errors = I18n.t("js.error.user.sn_length_tips") : void 0
            }, $scope.GetGoods = function() {
                var e;
                return 11 === $scope.sn.length || 14 === $scope.sn.length ? ($scope.GettingGoods = !0, e = $http.post(I18n.url("/user/getgoods"), {
                    sn: $scope.sn
                }), e.success(function(e) {
                    return $scope.GettingGoods = !1, 0 === e.status ? ($scope.errors = null, $scope.product_name = e.errors) : ($scope.product_name = null, $scope.errors = e.errors), $scope.submiting = !1
                })) : void 0
            }, $scope.ResendLink = function() {
                var e;
                return $scope.submiting = !0, e = $http.post(I18n.url("/user/send_active_link"), {
                    email: $scope.email
                }), e.success(function(e) {
                    return 0 === e.status ? $scope.success = !0 : $scope.errors = e.errors, $scope.submiting = !1
                })
            }, $scope.Edit_addr = function(e) {
                var t;
                return $scope.showadd = !1, $scope.edit_id = e, $scope.errors = {}, e > 0 && ($scope.showedit = !0, window.scrollTo(0, 0)), t = $http.get(I18n.url("/user/edit_addr/?id=" + e)), t.success(function(e) {
                    return e.status ? $scope.errors.mix = e.errors : ("cn" === e.country ? ($("#china-city-init").data("state", e.state).data("city", e.city).data("district", e.district).data("street", e.street), $scope.city = "", $scope.district = "", $scope.street = "", setTimeout(function() {
                        return $(".state").val(e.state).change()
                    }, 100)) : ($scope.city = e.city, $scope.state = e.state, $scope.district = e.district || "", $scope.street = e.street || ""), $scope.submiting = !1, $scope.country = e.country, $scope.first_name_label_change($scope.country), $scope.first_name = e.first_name, $scope.last_name = e.last_name, $scope.phone = e.phone, $scope.zip_code = e.zip_code, $scope.address = e.address, $scope.address2 = e.address2)
                })
            }, $scope.DelAddr = function(e) {
                var t;
                return confirm(I18n.t("js.error.user.del_addr_tips")) ? (t = $http.post(I18n.url("/user/" + e + "/del_addr/")), t.success(function(e) {
                    return 0 === e.status ? $window.location = I18n.url("/user/address") : void 0
                })) : void 0
            }, $scope.SaveAddr = function() {
                var e;
                return $scope.$apply, $scope.errors = {}, $scope.validate_address() ? ($scope.errors = {}, e = $http.post(I18n.url("/user/add_addr"), {
                    id: $scope.edit_id,
                    addrs: $scope.postData().order.shipping_address_attributes
                }), e.success(function(e) {
                    return $scope.submiting = !1, 0 === e.status ? ($scope.edit_id > 0 ? $scope.update_success = !0 : $scope.add_success = !0, $scope.success = !0, $window.location = I18n.url("/user/address")) : $scope.errors = e.errors
                })) : !1
            }, $scope.SaveOdAddr = function() {
                var e;
                return $scope.errors = {}, $scope.validate_address() ? ($scope.errors = {}, e = $http.post(I18n.url("/user/edit_od_addr_resp"), {
                    id: $scope.edit_id,
                    addrs: $scope.postData().order.shipping_address_attributes
                }), e.success(function(e) {
                    return $scope.submiting = !1, 0 === e.status ? ($scope.success = !0, $window.location = e.data.url) : ($scope.set_errors(e.errors), !1)
                })) : !1
            }, $scope.SaveMobileOdAddr = function() {
                var e;
                return $scope.errors = {}, $scope.validate_address() ? ($scope.errors = {}, e = $http.put(I18n.url("/addresses/" + $scope.addr_id), {
                    addrs: $scope.postData().order.shipping_address_attributes
                }), e.success(function(e) {
                    return $scope.submiting = !1, 0 === e.status ? ($scope.success = !0, $window.location = document.referrer, setTimeout(function() {
                        return window.scrollTo(0, document.body.scrollHeight)
                    }, 100)) : ($scope.set_errors(e.errors), $scope.error_scroll_action(), !1)
                })) : ($scope.error_scroll_action(), !1)
            }, $scope.showadd_toggle = function() {
                return window.scrollTo(0, 0), $scope.first_name = "", $scope.last_name = "", $scope.phone = "", $scope.city = "", $scope.state = "", $scope.district = "", $scope.street = "", $scope.country = "", $scope.zip_code = "", $scope.address = "", $scope.address2 = "", $scope.first_name_label_change($scope.country), $scope.showadd = !0
            }, $scope.hideAddrWin = function() {
                return $scope.showedit = !1, $scope.showadd = !1
            }, $scope.RestPsw = function() {
                var e;
                return "" === $scope.newpassword || "" === $scope.retpassword ? ($scope.errors = I18n.t("js.error.user.need_new_psw"), !1) : $scope.checkInput($scope.newpassword, "checkpass") ? ($scope.errors = null, $scope.retpassword !== $scope.newpassword ? ($scope.errors = I18n.t("js.error.user.ret_not_corr"), !1) : ($scope.errors = null, $scope.submiting = !0, e = $http.post(I18n.url("/user/rest_psw_resp"), {
                    password: $scope.newpassword
                }), e.success(function(e) {
                    return $scope.submiting = !1, 0 === e.status ? ($scope.success = !0, $window.location = I18n.url("/user/login")) : ($scope.submiting = !1, $scope.errors = e.errors)
                }))) : ($scope.errors = I18n.t("js.error.user.pass_format"), !1)
            }, $scope.getPsw = function() {
                var e;
                return $scope.checkInput($scope.email, "checkmail") ? ($scope.submiting = !0, e = $http.post(I18n.url("/user/retrieve_psw"), {
                    email: $scope.email
                }), e.success(function(e) {
                    return $scope.submiting = !1, 0 === e.status ? ($scope.errors = null, $scope.success = !0) : $scope.errors = e.errors
                })) : ($scope.errors = I18n.t("js.error.user.eamil_format"), !1)
            }, $scope.CheckPass = function() {
                var e;
                return "" === $scope.oldpassword ? ($scope.errors = I18n.t("js.error.user.need_org_psw"), !1) : "" === $scope.newpassword || "" === $scope.retpassword ? ($scope.errors = I18n.t("js.error.user.need_new_psw"), !1) : $scope.checkInput($scope.newpassword, "checkpass") ? ($scope.errors = null, $scope.retpassword !== $scope.newpassword ? ($scope.errors = I18n.t("js.error.user.ret_not_corr"), !1) : ($scope.errors = null, $scope.submiting = !0, e = $http.post(I18n.url("/user/checkpass"), {
                    password: $scope.oldpassword
                }), e.success(function(e) {
                    return $scope.submiting = !1, 0 === e.status ? ($scope.errors = null, $scope.updatepass()) : $scope.errors = e.errors
                }))) : ($scope.errors = I18n.t("js.error.user.pass_format"), !1)
            }, $scope.updatepass = function() {
                var e;
                return $scope.submiting = !0, e = $http.post(I18n.url("/user/updatepsw"), {
                    oldpassword: $scope.oldpassword,
                    newpassword: $scope.newpassword
                }), e.success(function(e) {
                    return $scope.submiting = !1, 0 === e.status ? ($scope.success = !0, $window.location = I18n.url("/user/logout")) : $scope.errors = e.errors
                })
            }, $scope.GetGift = function() {
                var e;
                return $scope.submiting = !0, e = $http.post(I18n.url("/user/order_gift"), $scope.postData()), e.success(function(e) {
                    return 0 === e.status ? $window.location = I18n.url("/user/") : 302 === e.status ? $window.location = e.errors : $scope.errors = e.errors
                }), e.error(function(e) {
                    return $scope.errors = e.errors
                }), e["finally"](function() {
                    return $scope.submiting = !1
                })
            }, $scope.postData = function() {
                var e;
                return e = {
                    first_name: $scope.first_name,
                    last_name: $scope.last_name,
                    phone: $scope.phone,
                    city: $scope.city,
                    state: $scope.state,
                    district: $scope.district,
                    street: $scope.street,
                    country: $scope.country,
                    zip_code: $scope.zip_code,
                    address: $scope.address,
                    address2: $scope.address2
                }, {
                    order: {
                        items_attributes: [{
                            product_id: $scope.product_id,
                            quantity: 1
                        }],
                        shipping_address_attributes: e,
                        coupon: "",
                        gateway: "credit",
                        agreement: !0,
                        currency: "CNY"
                    }
                }
            }, $scope.login = function() {
                var e;
                return $scope.submiting = !0, "" === $scope.email ? ($scope.errors = I18n.t("js.error.user.login_need_email"), $scope.submiting = !1, !1) : $scope.checkInput($scope.email, "checkmail") ? (e = $http.post(I18n.url("/user/login_response"), {
                    email: $scope.email,
                    password: $scope.password,
                    _rucaptcha: $scope.captcha
                }), e.success(function(e) {
                    return $scope.submiting = !1, 0 === e.status ? $window.location = e.errors : ($scope.errors = e.errors, $scope.refreshCaptcha())
                })) : ($scope.errors = I18n.t("js.error.user.eamil_format"), $scope.submiting = !1, !1)
            }, $scope.logout = function() {}, $scope.LoginSubmit = function(e) {
                return 13 === e.which ? $scope.login() : void 0
            }, $scope.refreshCaptcha = function() {
                return $scope.captchaTimestamp = (new Date).getTime(), $scope.captcha = ""
            }, $scope.register = function() {
                var e, t;
                return $scope.autoset_nick_name && $scope.email && (t = $scope.email.match(/(.*)@/), t && ($scope.nick_name = t[1].replace(/[^a-zA-Z0-9]/g, ""))), $scope.checkreg() ? ($scope.submiting = !0, e = $http.post(I18n.url("/user/register_response"), {
                    email: $scope.email,
                    nick_name: $scope.nick_name,
                    password: $scope.password
                }), e.success(function(e) {
                    return $scope.submiting = !1, 0 === e.status ? $window.location = e.errors : $scope.errors = e.errors
                })) : void 0
            }, $scope.checkreg = function() {
                return "" === $scope.email ? ($scope.errors = I18n.t("js.error.user.login_need_email"), !1) : $scope.checkInput($scope.email, "checkmail") ? "" === $scope.nick_name ? ($scope.errors = I18n.t("js.error.user.need_nickname"), !1) : $scope.checkInput($scope.nick_name, "regcheckname") ? "" === $scope.password || "" === $scope.rep_password ? ($scope.errors = I18n.t("js.error.user.need_psw"), !1) : $scope.checkInput($scope.password, "checkpass") ? $scope.rep_password !== $scope.password ? ($scope.errors = I18n.t("js.error.user.ret_not_corr"), !1) : ($scope.errors = null, !0) : ($scope.errors = I18n.t("js.error.user.pass_format"), !1) : ($scope.errors = I18n.t("js.error.user.nickname_format"), !1) : ($scope.errors = I18n.t("js.error.user.eamil_format"), !1)
            }, $scope.checkInput = function(vals, action) {
                var reg;
                switch (reg = /^[a-zA-Z0-9]{6,20}$/g, action) {
                    case "regcheckname":
                        reg = /^[a-zA-Z0-9\s]{4,20}$/g;
                        break;
                    case "checkmail":
                        reg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/g;
                        break;
                    case "checkpass":
                        reg = /^[a-zA-Z0-9!@#$%^&*]{6,20}$/g;
                        break;
                    case "checkpass2":
                        reg = eval("/^" + $("#PWD2").val() + "$/ig")
                }
                return reg.test(vals)
            }, $scope.profile = function() {
                var e;
                return $scope.submiting = !0, e = $http.post(I18n.url("/user/profile_response"), {
                    email: $scope.login,
                    mobile_number: $scope.mobile_number,
                    country: $scope.country,
                    locations: $scope.location,
                    nick_name: $scope.nick_name,
                    first_name: $scope.first_name,
                    last_name: $scope.last_name,
                    phone: $scope.phone
                }), e.success(function(e) {
                    return $scope.submiting = !1, 0 === e.status ? ($scope.success = !0, $window.location = I18n.url("/user")) : $scope.errors = e.errors
                })
            }, $scope.bindProduct = function(e) {
                var t;
                return $scope.submiting = !0, "" === $scope.product_name || "" === $scope.sn ? ($scope.errors = I18n.t("js.error.user.prod_bin_tip"), $scope.submiting = !1) : ($scope.errors = null, t = $http.post(I18n.url("/user/bind_product_response"), {
                    product_name: $scope.product_name,
                    sn: $scope.sn,
                    dealer_id: $scope.dealer,
                    dealer_phone: $scope.dealer_contact,
                    purchase_date: $scope.date,
                    city: $scope.parent.city,
                    id: e
                }), t.success(function(e) {
                    return $scope.submiting = !1, 0 === e.status ? ($scope.success = !0, $scope.genCode(), $("#prod_sn").attr("readonly", "readonly")) : $scope.errors = e.errors
                }))
            }, $scope.genCode = function() {
                return $scope.code = !0, $scope.code_src = "https://chart.googleapis.com/chart?cht=qr&chs=200x200&choe=UTF-8&chld=L|4&chl=" + $scope.qr_path + $scope.sn, $scope.link = $scope.qr_path + $scope.sn
            }, $scope.today = function() {
                return $scope.sdate = new Date, $scope.edate = new Date
            }, $scope.today(), $scope.clear = function() {
                return $scope.sdate = null, $scope.edate = null
            }, $scope.disabled = function(e, t) {
                return "day" === t && (0 === e.getDay() || 6 === e.getDay())
            }, $scope.toggleMin = function() {
                var e;
                return $scope.minDate = null != (e = $scope.minDate) ? e : {
                    "null": new Date
                }
            }, $scope.toggleMin(), $scope.sopen = function() {
                return $timeout(function() {
                    return $scope.sdateOpened = !0
                })
            }, $scope.eopen = function() {
                return $timeout(function() {
                    return $scope.edateOpened = !0
                })
            }, $scope.toggoleShowreg = function() {
                return $scope.showreg = !$scope.showreg
            }, $scope.dateOptions = {
                "show-Weeks": !1,
                "year-format": "'yy'",
                "month-format": "'mm'",
                "day-format": "'dd'"
            }, $scope.format = "yyyy-MM-dd"
        }]
    }.call(this),
    function(e) {
        function t() {
            var e, t, o = ["email", "password"];
            for (e = 0; e < this.length; e++) t = this[e], (o.indexOf(t.id) > -1 || !n(t)) && (r(t), c(t))
        }

        function n(e) {
            var t = e.value,
                n = e.$$currentValue;
            return t || n ? t === n : !0
        }

        function r(e) {
            e.$$currentValue = e.value
        }

        function o(t) {
            var n = e.jQuery || e.angular.element,
                r = n.prototype,
                o = r.val;
            r.val = function(e) {
                var n = o.apply(this, arguments);
                return arguments.length > 0 && a(this, function(n) {
                    t(n, e)
                }), n
            }
        }

        function i(e, t) {
            function n(e) {
                var n = e.target;
                t(n)
            }
            l.addEventListener(e, n, !0)
        }

        function s(e) {
            for (; e;) {
                if ("FORM" === e.nodeName || e.attributes && e.attributes.role && "form" === e.attributes.role.value) return u(e);
                e = e.parentNode
            }
            return u()
        }

        function a(e, t) {
            if (e.forEach) return e.forEach(t);
            var n;
            for (n = 0; n < e.length; n++) t(e[n])
        }

        function c(t) {
            var n = e.document,
                r = n.createEvent("HTMLEvents");
            r.initEvent("change", !0, !0), t.dispatchEvent(r)
        }
        var u = e.jQuery || e.angular.element,
            l = e.document.documentElement,
            p = u(l);
        i("change", r), o(r), u.prototype.checkAndTriggerAutoFillEvent = t, i("blur", function(t) {
            e.setTimeout(function() {
                s(t).find("input").checkAndTriggerAutoFillEvent()
            }, 20)
        }), e.document.addEventListener("DOMContentLoaded", function() {
            e.setTimeout(function() {
                p.find("input").checkAndTriggerAutoFillEvent()
            }, 200)
        }, !1)
    }(window), angular.module("ui.bootstrap", ["ui.bootstrap.tpls", "ui.bootstrap.transition", "ui.bootstrap.collapse", "ui.bootstrap.accordion", "ui.bootstrap.alert", "ui.bootstrap.bindHtml", "ui.bootstrap.buttons", "ui.bootstrap.carousel", "ui.bootstrap.position", "ui.bootstrap.datepicker", "ui.bootstrap.dropdownToggle", "ui.bootstrap.modal", "ui.bootstrap.pagination", "ui.bootstrap.tooltip", "ui.bootstrap.popover", "ui.bootstrap.progressbar", "ui.bootstrap.rating", "ui.bootstrap.tabs", "ui.bootstrap.timepicker", "ui.bootstrap.typeahead"]), angular.module("ui.bootstrap.tpls", ["template/accordion/accordion-group.html", "template/accordion/accordion.html", "template/alert/alert.html", "template/carousel/carousel.html", "template/carousel/slide.html", "template/datepicker/datepicker.html", "template/datepicker/popup.html", "template/modal/backdrop.html", "template/modal/window.html", "template/pagination/pager.html", "template/pagination/pagination.html", "template/tooltip/tooltip-html-unsafe-popup.html", "template/tooltip/tooltip-popup.html", "template/popover/popover.html", "template/progressbar/bar.html", "template/progressbar/progress.html", "template/rating/rating.html", "template/tabs/tab.html", "template/tabs/tabset-titles.html", "template/tabs/tabset.html", "template/timepicker/timepicker.html", "template/typeahead/typeahead-match.html", "template/typeahead/typeahead-popup.html"]), angular.module("ui.bootstrap.transition", []).factory("$transition", ["$q", "$timeout", "$rootScope", function(e, t, n) {
        function r(e) {
            for (var t in e)
                if (void 0 !== i.style[t]) return e[t]
        }
        var o = function(r, i, s) {
                s = s || {};
                var a = e.defer(),
                    c = o[s.animation ? "animationEndEventName" : "transitionEndEventName"],
                    u = function() {
                        n.$apply(function() {
                            r.unbind(c, u), a.resolve(r)
                        })
                    };
                return c && r.bind(c, u), t(function() {
                    angular.isString(i) ? r.addClass(i) : angular.isFunction(i) ? i(r) : angular.isObject(i) && r.css(i), c || a.resolve(r)
                }), a.promise.cancel = function() {
                    c && r.unbind(c, u), a.reject("Transition cancelled")
                }, a.promise
            },
            i = document.createElement("trans"),
            s = {
                WebkitTransition: "webkitTransitionEnd",
                MozTransition: "transitionend",
                OTransition: "oTransitionEnd",
                transition: "transitionend"
            },
            a = {
                WebkitTransition: "webkitAnimationEnd",
                MozTransition: "animationend",
                OTransition: "oAnimationEnd",
                transition: "animationend"
            };
        return o.transitionEndEventName = r(s), o.animationEndEventName = r(a), o
    }]), angular.module("ui.bootstrap.collapse", ["ui.bootstrap.transition"]).directive("collapse", ["$transition", function(e) {
        var t = function(e, t, n) {
            t.removeClass("collapse"), t.css({
                height: n
            }), t[0].offsetWidth, t.addClass("collapse")
        };
        return {
            link: function(n, r, o) {
                var i, s = !0;
                n.$watch(o.collapse, function(e) {
                    e ? l() : u()
                });
                var a, c = function(t) {
                        return a && a.cancel(), a = e(r, t), a.then(function() {
                            a = void 0
                        }, function() {
                            a = void 0
                        }), a
                    },
                    u = function() {
                        s ? (s = !1, i || (t(n, r, "auto"), r.addClass("in"))) : c({
                            height: r[0].scrollHeight + "px"
                        }).then(function() {
                            i || (t(n, r, "auto"), r.addClass("in"))
                        }), i = !1
                    },
                    l = function() {
                        i = !0, r.removeClass("in"), s ? (s = !1, t(n, r, 0)) : (t(n, r, r[0].scrollHeight + "px"), c({
                            height: "0"
                        }))
                    }
            }
        }
    }]), angular.module("ui.bootstrap.accordion", ["ui.bootstrap.collapse"]).constant("accordionConfig", {
        closeOthers: !0
    }).controller("AccordionController", ["$scope", "$attrs", "accordionConfig", function(e, t, n) {
        this.groups = [], this.scope = e, this.closeOthers = function(r) {
            var o = angular.isDefined(t.closeOthers) ? e.$eval(t.closeOthers) : n.closeOthers;
            o && angular.forEach(this.groups, function(e) {
                e !== r && (e.isOpen = !1)
            })
        }, this.addGroup = function(e) {
            var t = this;
            this.groups.push(e), e.$on("$destroy", function() {
                t.removeGroup(e)
            })
        }, this.removeGroup = function(e) {
            var t = this.groups.indexOf(e); - 1 !== t && this.groups.splice(this.groups.indexOf(e), 1)
        }
    }]).directive("accordion", function() {
        return {
            restrict: "EA",
            controller: "AccordionController",
            transclude: !0,
            replace: !1,
            templateUrl: "template/accordion/accordion.html"
        }
    }).directive("accordionGroup", ["$parse", "$transition", "$timeout", function(e) {
        return {
            require: "^accordion",
            restrict: "EA",
            transclude: !0,
            replace: !0,
            templateUrl: "template/accordion/accordion-group.html",
            scope: {
                heading: "@"
            },
            controller: ["$scope", function() {
                this.setHeading = function(e) {
                    this.heading = e
                }
            }],
            link: function(t, n, r, o) {
                var i, s;
                o.addGroup(t), t.isOpen = !1, r.isOpen && (i = e(r.isOpen), s = i.assign, o.scope.$watch(i, function(e) {
                    t.isOpen = !!e
                })), t.$watch("isOpen", function(e) {
                    e && o.closeOthers(t), s && s(o.scope, e)
                })
            }
        }
    }]).directive("accordionHeading", function() {
        return {
            restrict: "EA",
            transclude: !0,
            template: "",
            replace: !0,
            require: "^accordionGroup",
            compile: function(e, t, n) {
                return function(e, t, r, o) {
                    o.setHeading(n(e, function() {}))
                }
            }
        }
    }).directive("accordionTransclude", function() {
        return {
            require: "^accordionGroup",
            link: function(e, t, n, r) {
                e.$watch(function() {
                    return r[n.accordionTransclude]
                }, function(e) {
                    e && (t.html(""), t.append(e))
                })
            }
        }
    }), angular.module("ui.bootstrap.alert", []).directive("alert", function() {
        return {
            restrict: "EA",
            templateUrl: "template/alert/alert.html",
            transclude: !0,
            replace: !0,
            scope: {
                type: "=",
                close: "&"
            },
            link: function(e, t, n) {
                e.closeable = "close" in n
            }
        }
    }), angular.module("ui.bootstrap.bindHtml", []).directive("bindHtmlUnsafe", function() {
        return function(e, t, n) {
            t.addClass("ng-binding").data("$binding", n.bindHtmlUnsafe), e.$watch(n.bindHtmlUnsafe, function(e) {
                t.html(e || "")
            })
        }
    }), angular.module("ui.bootstrap.buttons", []).constant("buttonConfig", {
        activeClass: "active",
        toggleEvent: "click"
    }).directive("btnRadio", ["buttonConfig", function(e) {
        var t = e.activeClass || "active",
            n = e.toggleEvent || "click";
        return {
            require: "ngModel",
            link: function(e, r, o, i) {
                i.$render = function() {
                    r.toggleClass(t, angular.equals(i.$modelValue, e.$eval(o.btnRadio)))
                }, r.bind(n, function() {
                    r.hasClass(t) || e.$apply(function() {
                        i.$setViewValue(e.$eval(o.btnRadio)), i.$render()
                    })
                })
            }
        }
    }]).directive("btnCheckbox", ["buttonConfig", function(e) {
        var t = e.activeClass || "active",
            n = e.toggleEvent || "click";
        return {
            require: "ngModel",
            link: function(e, r, o, i) {
                function s() {
                    var t = e.$eval(o.btnCheckboxTrue);
                    return angular.isDefined(t) ? t : !0
                }

                function a() {
                    var t = e.$eval(o.btnCheckboxFalse);
                    return angular.isDefined(t) ? t : !1
                }
                i.$render = function() {
                    r.toggleClass(t, angular.equals(i.$modelValue, s()))
                }, r.bind(n, function() {
                    e.$apply(function() {
                        i.$setViewValue(r.hasClass(t) ? a() : s()), i.$render()
                    })
                })
            }
        }
    }]), angular.module("ui.bootstrap.carousel", ["ui.bootstrap.transition"]).controller("CarouselController", ["$scope", "$timeout", "$transition", "$q", function(e, t, n) {
        function r() {
            function n() {
                i ? (e.next(), r()) : e.pause()
            }
            o && t.cancel(o);
            var s = +e.interval;
            !isNaN(s) && s >= 0 && (o = t(n, s))
        }
        var o, i, s = this,
            a = s.slides = [],
            c = -1;
        s.currentSlide = null, s.select = function(o, i) {
            function u() {
                s.currentSlide && angular.isString(i) && !e.noTransition && o.$element ? (o.$element.addClass(i), o.$element[0].offsetWidth, angular.forEach(a, function(e) {
                    angular.extend(e, {
                        direction: "",
                        entering: !1,
                        leaving: !1,
                        active: !1
                    })
                }), angular.extend(o, {
                    direction: i,
                    active: !0,
                    entering: !0
                }), angular.extend(s.currentSlide || {}, {
                    direction: i,
                    leaving: !0
                }), e.$currentTransition = n(o.$element, {}), function(t, n) {
                    e.$currentTransition.then(function() {
                        l(t, n)
                    }, function() {
                        l(t, n)
                    })
                }(o, s.currentSlide)) : l(o, s.currentSlide), s.currentSlide = o, c = p, r()
            }

            function l(t, n) {
                angular.extend(t, {
                    direction: "",
                    active: !0,
                    leaving: !1,
                    entering: !1
                }), angular.extend(n || {}, {
                    direction: "",
                    active: !1,
                    leaving: !1,
                    entering: !1
                }), e.$currentTransition = null
            }
            var p = a.indexOf(o);
            void 0 === i && (i = p > c ? "next" : "prev"), o && o !== s.currentSlide && (e.$currentTransition ? (e.$currentTransition.cancel(), t(u)) : u())
        }, s.indexOfSlide = function(e) {
            return a.indexOf(e)
        }, e.next = function() {
            var t = (c + 1) % a.length;
            return e.$currentTransition ? void 0 : s.select(a[t], "next")
        }, e.prev = function() {
            var t = 0 > c - 1 ? a.length - 1 : c - 1;
            return e.$currentTransition ? void 0 : s.select(a[t], "prev")
        }, e.select = function(e) {
            s.select(e)
        }, e.isActive = function(e) {
            return s.currentSlide === e
        }, e.slides = function() {
            return a
        }, e.$watch("interval", r), e.play = function() {
            i || (i = !0, r())
        }, e.pause = function() {
            e.noPause || (i = !1, o && t.cancel(o))
        }, s.addSlide = function(t, n) {
            t.$element = n, a.push(t), 1 === a.length || t.active ? (s.select(a[a.length - 1]), 1 == a.length && e.play()) : t.active = !1
        }, s.removeSlide = function(e) {
            var t = a.indexOf(e);
            a.splice(t, 1), a.length > 0 && e.active ? t >= a.length ? s.select(a[t - 1]) : s.select(a[t]) : c > t && c--
        }
    }]).directive("carousel", [function() {
        return {
            restrict: "EA",
            transclude: !0,
            replace: !0,
            controller: "CarouselController",
            require: "carousel",
            templateUrl: "template/carousel/carousel.html",
            scope: {
                interval: "=",
                noTransition: "=",
                noPause: "="
            }
        }
    }]).directive("slide", ["$parse", function(e) {
        return {
            require: "^carousel",
            restrict: "EA",
            transclude: !0,
            replace: !0,
            templateUrl: "template/carousel/slide.html",
            scope: {},
            link: function(t, n, r, o) {
                if (r.active) {
                    var i = e(r.active),
                        s = i.assign,
                        a = t.active = i(t.$parent);
                    t.$watch(function() {
                        var e = i(t.$parent);
                        return e !== t.active && (e !== a ? a = t.active = e : s(t.$parent, e = a = t.active)), e
                    })
                }
                o.addSlide(t, n), t.$on("$destroy", function() {
                    o.removeSlide(t)
                }), t.$watch("active", function(e) {
                    e && o.select(t)
                })
            }
        }
    }]), angular.module("ui.bootstrap.position", []).factory("$position", ["$document", "$window", function(e, t) {
        function n(e, n) {
            return e.currentStyle ? e.currentStyle[n] : t.getComputedStyle ? t.getComputedStyle(e)[n] : e.style[n]
        }

        function r(e) {
            return "static" === (n(e, "position") || "static")
        }
        var o = function(t) {
            for (var n = e[0], o = t.offsetParent || n; o && o !== n && r(o);) o = o.offsetParent;
            return o || n
        };
        return {
            position: function(t) {
                var n = this.offset(t),
                    r = {
                        top: 0,
                        left: 0
                    },
                    i = o(t[0]);
                i != e[0] && (r = this.offset(angular.element(i)), r.top += i.clientTop - i.scrollTop, r.left += i.clientLeft - i.scrollLeft);
                var s = t[0].getBoundingClientRect();
                return {
                    width: s.width || t.prop("offsetWidth"),
                    height: s.height || t.prop("offsetHeight"),
                    top: n.top - r.top,
                    left: n.left - r.left
                }
            },
            offset: function(n) {
                var r = n[0].getBoundingClientRect();
                return {
                    width: r.width || n.prop("offsetWidth"),
                    height: r.height || n.prop("offsetHeight"),
                    top: r.top + (t.pageYOffset || e[0].body.scrollTop || e[0].documentElement.scrollTop),
                    left: r.left + (t.pageXOffset || e[0].body.scrollLeft || e[0].documentElement.scrollLeft)
                }
            }
        }
    }]), angular.module("ui.bootstrap.datepicker", ["ui.bootstrap.position"]).constant("datepickerConfig", {
        dayFormat: "dd",
        monthFormat: "MMMM",
        yearFormat: "yyyy",
        dayHeaderFormat: "EEE",
        dayTitleFormat: "MMMM yyyy",
        monthTitleFormat: "yyyy",
        showWeeks: !0,
        startingDay: 0,
        yearRange: 20,
        minDate: null,
        maxDate: null
    }).controller("DatepickerController", ["$scope", "$attrs", "dateFilter", "datepickerConfig", function(e, t, n, r) {
        function o(t, n) {
            return angular.isDefined(t) ? e.$parent.$eval(t) : n
        }

        function i(e, t) {
            return new Date(e, t, 0).getDate()
        }

        function s(e, t) {
            for (var n = new Array(t), r = e, o = 0; t > o;) n[o++] = new Date(r), r.setDate(r.getDate() + 1);
            return n
        }

        function a(e, t, r, o) {
            return {
                date: e,
                label: n(e, t),
                selected: !!r,
                secondary: !!o
            }
        }
        var c = {
                day: o(t.dayFormat, r.dayFormat),
                month: o(t.monthFormat, r.monthFormat),
                year: o(t.yearFormat, r.yearFormat),
                dayHeader: o(t.dayHeaderFormat, r.dayHeaderFormat),
                dayTitle: o(t.dayTitleFormat, r.dayTitleFormat),
                monthTitle: o(t.monthTitleFormat, r.monthTitleFormat)
            },
            u = o(t.startingDay, r.startingDay),
            l = o(t.yearRange, r.yearRange);
        this.minDate = r.minDate ? new Date(r.minDate) : null, this.maxDate = r.maxDate ? new Date(r.maxDate) : null, this.modes = [{
            name: "day",
            getVisibleDates: function(e, t) {
                var r = e.getFullYear(),
                    o = e.getMonth(),
                    l = new Date(r, o, 1),
                    p = u - l.getDay(),
                    d = p > 0 ? 7 - p : -p,
                    f = new Date(l),
                    $ = 0;
                d > 0 && (f.setDate(-d + 1), $ += d), $ += i(r, o + 1), $ += (7 - $ % 7) % 7;
                for (var h = s(f, $), g = new Array(7), m = 0; $ > m; m++) {
                    var v = new Date(h[m]);
                    h[m] = a(v, c.day, t && t.getDate() === v.getDate() && t.getMonth() === v.getMonth() && t.getFullYear() === v.getFullYear(), v.getMonth() !== o)
                }
                for (var _ = 0; 7 > _; _++) g[_] = n(h[_].date, c.dayHeader);
                return {
                    objects: h,
                    title: n(e, c.dayTitle),
                    labels: g
                }
            },
            compare: function(e, t) {
                return new Date(e.getFullYear(), e.getMonth(), e.getDate()) - new Date(t.getFullYear(), t.getMonth(), t.getDate())
            },
            split: 7,
            step: {
                months: 1
            }
        }, {
            name: "month",
            getVisibleDates: function(e, t) {
                for (var r = new Array(12), o = e.getFullYear(), i = 0; 12 > i; i++) {
                    var s = new Date(o, i, 1);
                    r[i] = a(s, c.month, t && t.getMonth() === i && t.getFullYear() === o)
                }
                return {
                    objects: r,
                    title: n(e, c.monthTitle)
                }
            },
            compare: function(e, t) {
                return new Date(e.getFullYear(), e.getMonth()) - new Date(t.getFullYear(), t.getMonth())
            },
            split: 3,
            step: {
                years: 1
            }
        }, {
            name: "year",
            getVisibleDates: function(e, t) {
                for (var n = new Array(l), r = e.getFullYear(), o = parseInt((r - 1) / l, 10) * l + 1, i = 0; l > i; i++) {
                    var s = new Date(o + i, 0, 1);
                    n[i] = a(s, c.year, t && t.getFullYear() === s.getFullYear())
                }
                return {
                    objects: n,
                    title: [n[0].label, n[l - 1].label].join(" - ")
                }
            },
            compare: function(e, t) {
                return e.getFullYear() - t.getFullYear()
            },
            split: 5,
            step: {
                years: l
            }
        }], this.isDisabled = function(t, n) {
            var r = this.modes[n || 0];
            return this.minDate && r.compare(t, this.minDate) < 0 || this.maxDate && r.compare(t, this.maxDate) > 0 || e.dateDisabled && e.dateDisabled({
                date: t,
                mode: r.name
            })
        }
    }]).directive("datepicker", ["dateFilter", "$parse", "datepickerConfig", "$log", function(e, t, n, r) {
        return {
            restrict: "EA",
            replace: !0,
            templateUrl: "template/datepicker/datepicker.html",
            scope: {
                dateDisabled: "&"
            },
            require: ["datepicker", "?^ngModel"],
            controller: "DatepickerController",
            link: function(e, o, i, s) {
                function a() {
                    e.showWeekNumbers = 0 === $ && g
                }

                function c(e, t) {
                    for (var n = []; e.length > 0;) n.push(e.splice(0, t));
                    return n
                }

                function u(t) {
                    var n = null,
                        o = !0;
                    f.$modelValue && (n = new Date(f.$modelValue), isNaN(n) ? (o = !1, r.error('Datepicker directive: "ng-model" value must be a Date object, a number of milliseconds since 01.01.1970 or a string representing an RFC2822 or ISO 8601 date.')) : t && (h = n)), f.$setValidity("date", o);
                    var i = d.modes[$],
                        s = i.getVisibleDates(h, n);
                    angular.forEach(s.objects, function(e) {
                        e.disabled = d.isDisabled(e.date, $)
                    }), f.$setValidity("date-disabled", !n || !d.isDisabled(n)), e.rows = c(s.objects, i.split), e.labels = s.labels || [], e.title = s.title
                }

                function l(e) {
                    $ = e, a(), u()
                }

                function p(e) {
                    var t = new Date(e);
                    t.setDate(t.getDate() + 4 - (t.getDay() || 7));
                    var n = t.getTime();
                    return t.setMonth(0), t.setDate(1), Math.floor(Math.round((n - t) / 864e5) / 7) + 1
                }
                var d = s[0],
                    f = s[1];
                if (f) {
                    var $ = 0,
                        h = new Date,
                        g = n.showWeeks;
                    i.showWeeks ? e.$parent.$watch(t(i.showWeeks), function(e) {
                        g = !!e, a()
                    }) : a(), i.min && e.$parent.$watch(t(i.min), function(e) {
                        d.minDate = e ? new Date(e) : null, u()
                    }), i.max && e.$parent.$watch(t(i.max), function(e) {
                        d.maxDate = e ? new Date(e) : null, u()
                    }), f.$render = function() {
                        u(!0)
                    }, e.select = function(e) {
                        if (0 === $) {
                            var t = new Date(f.$modelValue);
                            t.setFullYear(e.getFullYear(), e.getMonth(), e.getDate()), f.$setViewValue(t), u(!0)
                        } else h = e, l($ - 1)
                    }, e.move = function(e) {
                        var t = d.modes[$].step;
                        h.setMonth(h.getMonth() + e * (t.months || 0)), h.setFullYear(h.getFullYear() + e * (t.years || 0)), u()
                    }, e.toggleMode = function() {
                        l(($ + 1) % d.modes.length)
                    }, e.getWeekNumber = function(t) {
                        return 0 === $ && e.showWeekNumbers && 7 === t.length ? p(t[0].date) : null
                    }
                }
            }
        }
    }]).constant("datepickerPopupConfig", {
        dateFormat: "yyyy-MM-dd",
        currentText: "Today",
        toggleWeeksText: "Weeks",
        clearText: "Clear",
        closeText: "Done",
        closeOnDateSelection: !0,
        appendToBody: !1
    }).directive("datepickerPopup", ["$compile", "$parse", "$document", "$position", "dateFilter", "datepickerPopupConfig", "datepickerConfig", function(e, t, n, r, o, i, s) {
        return {
            restrict: "EA",
            require: "ngModel",
            link: function(a, c, u, l) {
                function p(e) {
                    b ? b(a, !!e) : _.isOpen = !!e
                }

                function d(e) {
                    if (e) {
                        if (angular.isDate(e)) return l.$setValidity("date", !0), e;
                        if (angular.isString(e)) {
                            var t = new Date(e);
                            return isNaN(t) ? (l.$setValidity("date", !1), void 0) : (l.$setValidity("date", !0), t)
                        }
                        return l.$setValidity("date", !1), void 0
                    }
                    return l.$setValidity("date", !0), null
                }

                function f() {
                    _.date = l.$modelValue, h()
                }

                function $(e, n, r) {
                    e && (a.$watch(t(e), function(e) {
                        _[n] = e
                    }), C.attr(r || n, n))
                }

                function h() {
                    _.position = v ? r.offset(c) : r.position(c), _.position.top = _.position.top + c.prop("offsetHeight")
                }
                var g;
                u.$observe("datepickerPopup", function(e) {
                    g = e || i.dateFormat, l.$render()
                });
                var m = angular.isDefined(u.closeOnDateSelection) ? a.$eval(u.closeOnDateSelection) : i.closeOnDateSelection,
                    v = angular.isDefined(u.datepickerAppendToBody) ? a.$eval(u.datepickerAppendToBody) : i.appendToBody,
                    _ = a.$new();
                a.$on("$destroy", function() {
                    _.$destroy()
                }), u.$observe("currentText", function(e) {
                    _.currentText = angular.isDefined(e) ? e : i.currentText
                }), u.$observe("toggleWeeksText", function(e) {
                    _.toggleWeeksText = angular.isDefined(e) ? e : i.toggleWeeksText
                }), u.$observe("clearText", function(e) {
                    _.clearText = angular.isDefined(e) ? e : i.clearText
                }), u.$observe("closeText", function(e) {
                    _.closeText = angular.isDefined(e) ? e : i.closeText
                });
                var y, b;
                u.isOpen && (y = t(u.isOpen), b = y.assign, a.$watch(y, function(e) {
                    _.isOpen = !!e
                })), _.isOpen = y ? y(a) : !1;
                var w = function(e) {
                        _.isOpen && e.target !== c[0] && _.$apply(function() {
                            p(!1)
                        })
                    },
                    x = function() {
                        _.$apply(function() {
                            p(!0)
                        })
                    },
                    k = angular.element("<div datepicker-popup-wrap><div datepicker></div></div>");
                k.attr({
                    "ng-model": "date",
                    "ng-change": "dateSelection()"
                });
                var C = angular.element(k.children()[0]);
                u.datepickerOptions && C.attr(angular.extend({}, a.$eval(u.datepickerOptions))), l.$parsers.unshift(d), _.dateSelection = function() {
                    l.$setViewValue(_.date), l.$render(), m && p(!1)
                }, c.bind("input change keyup", function() {
                    _.$apply(function() {
                        f()
                    })
                }), l.$render = function() {
                    var e = l.$viewValue ? o(l.$viewValue, g) : "";
                    c.val(e), f()
                }, $(u.min, "min"), $(u.max, "max"), u.showWeeks ? $(u.showWeeks, "showWeeks", "show-weeks") : (_.showWeeks = s.showWeeks, C.attr("show-weeks", "showWeeks")), u.dateDisabled && C.attr("date-disabled", u.dateDisabled);
                var S = !1,
                    T = !1;
                _.$watch("isOpen", function(e) {
                    e ? (h(), n.bind("click", w), T && c.unbind("focus", x), c[0].focus(), S = !0) : (S && n.unbind("click", w), c.bind("focus", x), T = !0), b && b(a, e)
                });
                var A = t(u.ngModel).assign;
                _.today = function() {
                    A(a, new Date)
                }, _.clear = function() {
                    A(a, null)
                };
                var E = e(k)(_);
                v ? n.find("body").append(E) : c.after(E)
            }
        }
    }]).directive("datepickerPopupWrap", function() {
        return {
            restrict: "EA",
            replace: !0,
            transclude: !0,
            templateUrl: "template/datepicker/popup.html",
            link: function(e, t) {
                t.bind("click", function(e) {
                    e.preventDefault(), e.stopPropagation()
                })
            }
        }
    }), angular.module("ui.bootstrap.dropdownToggle", []).directive("dropdownToggle", ["$document", "$location", function(e) {
        var t = null,
            n = angular.noop;
        return {
            restrict: "CA",
            link: function(r, o) {
                r.$watch("$location.path", function() {
                    n()
                }), o.parent().bind("click", function() {
                    n()
                }), o.bind("click", function(r) {
                    var i = o === t;
                    r.preventDefault(), r.stopPropagation(), t && n(), i || o.hasClass("disabled") || o.prop("disabled") || (o.parent().addClass("open"), t = o, n = function(r) {
                        r && (r.preventDefault(), r.stopPropagation()), e.unbind("click", n), o.parent().removeClass("open"), n = angular.noop, t = null
                    }, e.bind("click", n))
                })
            }
        }
    }]), angular.module("ui.bootstrap.modal", []).factory("$$stackedMap", function() {
        return {
            createNew: function() {
                var e = [];
                return {
                    add: function(t, n) {
                        e.push({
                            key: t,
                            value: n
                        })
                    },
                    get: function(t) {
                        for (var n = 0; n < e.length; n++)
                            if (t == e[n].key) return e[n]
                    },
                    keys: function() {
                        for (var t = [], n = 0; n < e.length; n++) t.push(e[n].key);
                        return t
                    },
                    top: function() {
                        return e[e.length - 1]
                    },
                    remove: function(t) {
                        for (var n = -1, r = 0; r < e.length; r++)
                            if (t == e[r].key) {
                                n = r;
                                break
                            }
                        return e.splice(n, 1)[0]
                    },
                    removeTop: function() {
                        return e.splice(e.length - 1, 1)[0]
                    },
                    length: function() {
                        return e.length
                    }
                }
            }
        }
    }).directive("modalBackdrop", ["$timeout", function(e) {
        return {
            restrict: "EA",
            replace: !0,
            templateUrl: "template/modal/backdrop.html",
            link: function(t) {
                e(function() {
                    t.animate = !0
                })
            }
        }
    }]).directive("modalWindow", ["$modalStack", "$timeout", function(e, t) {
        return {
            restrict: "EA",
            scope: {
                index: "@"
            },
            replace: !0,
            transclude: !0,
            templateUrl: "template/modal/window.html",
            link: function(n, r, o) {
                n.windowClass = o.windowClass || "", t(function() {
                    n.animate = !0
                }), n.close = function(t) {
                    var n = e.getTop();
                    n && n.value.backdrop && "static" != n.value.backdrop && t.target === t.currentTarget && (t.preventDefault(), t.stopPropagation(), e.dismiss(n.key, "backdrop click"))
                }
            }
        }
    }]).factory("$modalStack", ["$document", "$compile", "$rootScope", "$$stackedMap", function(e, t, n, r) {
        function o() {
            for (var e = -1, t = l.keys(), n = 0; n < t.length; n++) l.get(t[n]).value.backdrop && (e = n);
            return e
        }

        function i(e) {
            var t = l.get(e).value;
            l.remove(e), t.modalDomEl.remove(), a && -1 == o() && (a.remove(), a = void 0), t.modalScope.$destroy()
        }
        var s, a, c = n.$new(!0),
            u = e.find("body").eq(0),
            l = r.createNew(),
            p = {};
        return n.$watch(l.length, function() {
            u.toggleClass("modal-open", l.length() > 0)
        }), n.$watch(o, function(e) {
            c.index = e
        }), e.bind("keydown", function(e) {
            var t;
            27 === e.which && (t = l.top(), t && t.value.keyboard && n.$apply(function() {
                p.dismiss(t.key)
            }))
        }), p.open = function(e, n) {
            l.add(e, {
                deferred: n.deferred,
                modalScope: n.scope,
                backdrop: n.backdrop,
                keyboard: n.keyboard
            });
            var r = angular.element("<div modal-window></div>");
            r.attr("window-class", n.windowClass), r.attr("index", l.length() - 1), r.html(n.content);
            var i = t(r)(n.scope);
            l.top().value.modalDomEl = i, u.append(i), o() >= 0 && !a && (s = angular.element("<div modal-backdrop></div>"), a = t(s)(c), u.append(a))
        }, p.close = function(e, t) {
            var n = l.get(e).value;
            n && (n.deferred.resolve(t), i(e))
        }, p.dismiss = function(e, t) {
            var n = l.get(e).value;
            n && (n.deferred.reject(t), i(e))
        }, p.getTop = function() {
            return l.top()
        }, p
    }]).provider("$modal", function() {
        var e = {
            options: {
                backdrop: !0,
                keyboard: !0
            },
            $get: ["$injector", "$rootScope", "$q", "$http", "$templateCache", "$controller", "$modalStack", function(t, n, r, o, i, s, a) {
                function c(e) {
                    return e.template ? r.when(e.template) : o.get(e.templateUrl, {
                        cache: i
                    }).then(function(e) {
                        return e.data
                    })
                }

                function u(e) {
                    var n = [];
                    return angular.forEach(e, function(e) {
                        (angular.isFunction(e) || angular.isArray(e)) && n.push(r.when(t.invoke(e)))
                    }), n
                }
                var l = {};
                return l.open = function(t) {
                    var o = r.defer(),
                        i = r.defer(),
                        l = {
                            result: o.promise,
                            opened: i.promise,
                            close: function(e) {
                                a.close(l, e)
                            },
                            dismiss: function(e) {
                                a.dismiss(l, e)
                            }
                        };
                    if (t = angular.extend({}, e.options, t), t.resolve = t.resolve || {}, !t.template && !t.templateUrl) throw new Error("One of template or templateUrl options is required.");
                    var p = r.all([c(t)].concat(u(t.resolve)));
                    return p.then(function(e) {
                        var r = (t.scope || n).$new();
                        r.$close = l.close, r.$dismiss = l.dismiss;
                        var i, c = {},
                            u = 1;
                        t.controller && (c.$scope = r, c.$modalInstance = l, angular.forEach(t.resolve, function(t, n) {
                            c[n] = e[u++]
                        }), i = s(t.controller, c)), a.open(l, {
                            scope: r,
                            deferred: o,
                            content: e[0],
                            backdrop: t.backdrop,
                            keyboard: t.keyboard,
                            windowClass: t.windowClass
                        })
                    }, function(e) {
                        o.reject(e)
                    }), p.then(function() {
                        i.resolve(!0)
                    }, function() {
                        i.reject(!1)
                    }), l
                }, l
            }]
        };
        return e
    }), angular.module("ui.bootstrap.pagination", []).controller("PaginationController", ["$scope", "$attrs", "$parse", "$interpolate", function(e, t, n, r) {
        var o = this,
            i = t.numPages ? n(t.numPages).assign : angular.noop;
        this.init = function(r) {
            t.itemsPerPage ? e.$parent.$watch(n(t.itemsPerPage), function(t) {
                o.itemsPerPage = parseInt(t, 10), e.totalPages = o.calculateTotalPages()
            }) : this.itemsPerPage = r
        }, this.noPrevious = function() {
            return 1 === this.page
        }, this.noNext = function() {
            return this.page === e.totalPages
        }, this.isActive = function(e) {
            return this.page === e
        }, this.calculateTotalPages = function() {
            var t = this.itemsPerPage < 1 ? 1 : Math.ceil(e.totalItems / this.itemsPerPage);
            return Math.max(t || 0, 1)
        }, this.getAttributeValue = function(t, n, o) {
            return angular.isDefined(t) ? o ? r(t)(e.$parent) : e.$parent.$eval(t) : n
        }, this.render = function() {
            this.page = parseInt(e.page, 10) || 1, this.page > 0 && this.page <= e.totalPages && (e.pages = this.getPages(this.page, e.totalPages))
        }, e.selectPage = function(t) {
            !o.isActive(t) && t > 0 && t <= e.totalPages && (e.page = t, e.onSelectPage({
                page: t
            }))
        }, e.$watch("page", function() {
            o.render()
        }), e.$watch("totalItems", function() {
            e.totalPages = o.calculateTotalPages()
        }), e.$watch("totalPages", function(t) {
            i(e.$parent, t), o.page > t ? e.selectPage(t) : o.render()
        })
    }]).constant("paginationConfig", {
        itemsPerPage: 10,
        boundaryLinks: !1,
        directionLinks: !0,
        firstText: "First",
        previousText: "Previous",
        nextText: "Next",
        lastText: "Last",
        rotate: !0
    }).directive("pagination", ["$parse", "paginationConfig", function(e, t) {
        return {
            restrict: "EA",
            scope: {
                page: "=",
                totalItems: "=",
                onSelectPage: " &"
            },
            controller: "PaginationController",
            templateUrl: "template/pagination/pagination.html",
            replace: !0,
            link: function(n, r, o, i) {
                function s(e, t, n, r) {
                    return {
                        number: e,
                        text: t,
                        active: n,
                        disabled: r
                    }
                }
                var a, c = i.getAttributeValue(o.boundaryLinks, t.boundaryLinks),
                    u = i.getAttributeValue(o.directionLinks, t.directionLinks),
                    l = i.getAttributeValue(o.firstText, t.firstText, !0),
                    p = i.getAttributeValue(o.previousText, t.previousText, !0),
                    d = i.getAttributeValue(o.nextText, t.nextText, !0),
                    f = i.getAttributeValue(o.lastText, t.lastText, !0),
                    $ = i.getAttributeValue(o.rotate, t.rotate);
                i.init(t.itemsPerPage), o.maxSize && n.$parent.$watch(e(o.maxSize), function(e) {
                    a = parseInt(e, 10), i.render()
                }), i.getPages = function(e, t) {
                    var n = [],
                        r = 1,
                        o = t,
                        h = angular.isDefined(a) && t > a;
                    h && ($ ? (r = Math.max(e - Math.floor(a / 2), 1), o = r + a - 1, o > t && (o = t, r = o - a + 1)) : (r = (Math.ceil(e / a) - 1) * a + 1, o = Math.min(r + a - 1, t)));
                    for (var g = r; o >= g; g++) {
                        var m = s(g, g, i.isActive(g), !1);
                        n.push(m)
                    }
                    if (h && !$) {
                        if (r > 1) {
                            var v = s(r - 1, "...", !1, !1);
                            n.unshift(v)
                        }
                        if (t > o) {
                            var _ = s(o + 1, "...", !1, !1);
                            n.push(_)
                        }
                    }
                    if (u) {
                        var y = s(e - 1, p, !1, i.noPrevious());
                        n.unshift(y);
                        var b = s(e + 1, d, !1, i.noNext());
                        n.push(b)
                    }
                    if (c) {
                        var w = s(1, l, !1, i.noPrevious());
                        n.unshift(w);
                        var x = s(t, f, !1, i.noNext());
                        n.push(x)
                    }
                    return n
                }
            }
        }
    }]).constant("pagerConfig", {
        itemsPerPage: 10,
        previousText: "芦 Previous",
        nextText: "Next 禄",
        align: !0
    }).directive("pager", ["pagerConfig", function(e) {
        return {
            restrict: "EA",
            scope: {
                page: "=",
                totalItems: "=",
                onSelectPage: " &"
            },
            controller: "PaginationController",
            templateUrl: "template/pagination/pager.html",
            replace: !0,
            link: function(t, n, r, o) {
                function i(e, t, n, r, o) {
                    return {
                        number: e,
                        text: t,
                        disabled: n,
                        previous: c && r,
                        next: c && o
                    }
                }
                var s = o.getAttributeValue(r.previousText, e.previousText, !0),
                    a = o.getAttributeValue(r.nextText, e.nextText, !0),
                    c = o.getAttributeValue(r.align, e.align);
                o.init(e.itemsPerPage), o.getPages = function(e) {
                    return [i(e - 1, s, o.noPrevious(), !0, !1), i(e + 1, a, o.noNext(), !1, !0)]
                }
            }
        }
    }]), angular.module("ui.bootstrap.tooltip", ["ui.bootstrap.position", "ui.bootstrap.bindHtml"]).provider("$tooltip", function() {
        function e(e) {
            var t = /[A-Z]/g,
                n = "-";
            return e.replace(t, function(e, t) {
                return (t ? n : "") + e.toLowerCase()
            })
        }
        var t = {
                placement: "top",
                animation: !0,
                popupDelay: 0
            },
            n = {
                mouseenter: "mouseleave",
                click: "click",
                focus: "blur"
            },
            r = {};
        this.options = function(e) {
            angular.extend(r, e)
        }, this.setTriggers = function(e) {
            angular.extend(n, e)
        }, this.$get = ["$window", "$compile", "$timeout", "$parse", "$document", "$position", "$interpolate", function(o, i, s, a, c, u, l) {
            return function(o, p, d) {
                function f(e) {
                    var t = e || $.trigger || d,
                        r = n[t] || t;
                    return {
                        show: t,
                        hide: r
                    }
                }
                var $ = angular.extend({}, t, r),
                    h = e(o),
                    g = l.startSymbol(),
                    m = l.endSymbol(),
                    v = "<" + h + '-popup title="' + g + "tt_title" + m + '" content="' + g + "tt_content" + m + '" placement="' + g + "tt_placement" + m + '" animation="tt_animation()" is-open="tt_isOpen"></' + h + "-popup>";
                return {
                    restrict: "EA",
                    scope: !0,
                    link: function(e, t, n) {
                        function r() {
                            e.tt_isOpen ? d() : l()
                        }

                        function l() {
                            (!C || e.$eval(n[p + "Enable"])) && (e.tt_popupDelay ? _ = s(h, e.tt_popupDelay) : e.$apply(h))
                        }

                        function d() {
                            e.$apply(function() {
                                g()
                            })
                        }

                        function h() {
                            var n, r, o, i;
                            if (e.tt_content) {
                                switch (m && s.cancel(m), b.css({
                                    top: 0,
                                    left: 0,
                                    display: "block"
                                }), w ? (y = y || c.find("body"), y.append(b)) : t.after(b), n = w ? u.offset(t) : u.position(t), r = b.prop("offsetWidth"), o = b.prop("offsetHeight"), e.tt_placement) {
                                    case "right":
                                        i = {
                                            top: n.top + n.height / 2 - o / 2,
                                            left: n.left + n.width
                                        };
                                        break;
                                    case "bottom":
                                        i = {
                                            top: n.top + n.height,
                                            left: n.left + n.width / 2 - r / 2
                                        };
                                        break;
                                    case "left":
                                        i = {
                                            top: n.top + n.height / 2 - o / 2,
                                            left: n.left - r
                                        };
                                        break;
                                    default:
                                        i = {
                                            top: n.top - o,
                                            left: n.left + n.width / 2 - r / 2
                                        }
                                }
                                i.top += "px", i.left += "px", b.css(i), e.tt_isOpen = !0
                            }
                        }

                        function g() {
                            e.tt_isOpen = !1, s.cancel(_), angular.isDefined(e.tt_animation) && e.tt_animation() ? m = s(function() {
                                b.remove()
                            }, 500) : b.remove()
                        }
                        var m, _, y, b = i(v)(e),
                            w = angular.isDefined($.appendToBody) ? $.appendToBody : !1,
                            x = f(void 0),
                            k = !1,
                            C = angular.isDefined(n[p + "Enable"]);
                        e.tt_isOpen = !1, n.$observe(o, function(t) {
                            t ? e.tt_content = t : e.tt_isOpen && g()
                        }), n.$observe(p + "Title", function(t) {
                            e.tt_title = t
                        }), n.$observe(p + "Placement", function(t) {
                            e.tt_placement = angular.isDefined(t) ? t : $.placement
                        }), n.$observe(p + "Animation", function(t) {
                            e.tt_animation = angular.isDefined(t) ? a(t) : function() {
                                return $.animation
                            }
                        }), n.$observe(p + "PopupDelay", function(t) {
                            var n = parseInt(t, 10);
                            e.tt_popupDelay = isNaN(n) ? $.popupDelay : n
                        }), n.$observe(p + "Trigger", function(e) {
                            k && (t.unbind(x.show, l), t.unbind(x.hide, d)), x = f(e), x.show === x.hide ? t.bind(x.show, r) : (t.bind(x.show, l), t.bind(x.hide, d)), k = !0
                        }), n.$observe(p + "AppendToBody", function(t) {
                            w = angular.isDefined(t) ? a(t)(e) : w
                        }), w && e.$on("$locationChangeSuccess", function() {
                            e.tt_isOpen && g()
                        }), e.$on("$destroy", function() {
                            e.tt_isOpen ? g() : b.remove()
                        })
                    }
                }
            }
        }]
    }).directive("tooltipPopup", function() {
        return {
            restrict: "E",
            replace: !0,
            scope: {
                content: "@",
                placement: "@",
                animation: "&",
                isOpen: "&"
            },
            templateUrl: "template/tooltip/tooltip-popup.html"
        }
    }).directive("tooltip", ["$tooltip", function(e) {
        return e("tooltip", "tooltip", "mouseenter")
    }]).directive("tooltipHtmlUnsafePopup", function() {
        return {
            restrict: "E",
            replace: !0,
            scope: {
                content: "@",
                placement: "@",
                animation: "&",
                isOpen: "&"
            },
            templateUrl: "template/tooltip/tooltip-html-unsafe-popup.html"
        }
    }).directive("tooltipHtmlUnsafe", ["$tooltip", function(e) {
        return e("tooltipHtmlUnsafe", "tooltip", "mouseenter")
    }]), angular.module("ui.bootstrap.popover", ["ui.bootstrap.tooltip"]).directive("popoverPopup", function() {
        return {
            restrict: "EA",
            replace: !0,
            scope: {
                title: "@",
                content: "@",
                placement: "@",
                animation: "&",
                isOpen: "&"
            },
            templateUrl: "template/popover/popover.html"
        }
    }).directive("popover", ["$compile", "$timeout", "$parse", "$window", "$tooltip", function(e, t, n, r, o) {
        return o("popover", "popover", "click")
    }]), angular.module("ui.bootstrap.progressbar", ["ui.bootstrap.transition"]).constant("progressConfig", {
        animate: !0,
        autoType: !1,
        stackedTypes: ["success", "info", "warning", "danger"]
    }).controller("ProgressBarController", ["$scope", "$attrs", "progressConfig", function(e, t, n) {
        function r(e) {
            return s[e]
        }
        var o = angular.isDefined(t.animate) ? e.$eval(t.animate) : n.animate,
            i = angular.isDefined(t.autoType) ? e.$eval(t.autoType) : n.autoType,
            s = angular.isDefined(t.stackedTypes) ? e.$eval("[" + t.stackedTypes + "]") : n.stackedTypes;
        this.makeBar = function(e, t, n) {
            var s = angular.isObject(e) ? e.value : e || 0,
                a = angular.isObject(t) ? t.value : t || 0,
                c = angular.isObject(e) && angular.isDefined(e.type) ? e.type : i ? r(n || 0) : null;
            return {
                from: a,
                to: s,
                type: c,
                animate: o
            }
        }, this.addBar = function(t) {
            e.bars.push(t), e.totalPercent += t.to
        }, this.clearBars = function() {
            e.bars = [], e.totalPercent = 0
        }, this.clearBars()
    }]).directive("progress", function() {
        return {
            restrict: "EA",
            replace: !0,
            controller: "ProgressBarController",
            scope: {
                value: "=percent",
                onFull: "&",
                onEmpty: "&"
            },
            templateUrl: "template/progressbar/progress.html",
            link: function(e, t, n, r) {
                e.$watch("value", function(e, t) {
                    if (r.clearBars(), angular.isArray(e))
                        for (var n = 0, o = e.length; o > n; n++) r.addBar(r.makeBar(e[n], t[n], n));
                    else r.addBar(r.makeBar(e, t))
                }, !0), e.$watch("totalPercent", function(t) {
                    t >= 100 ? e.onFull() : 0 >= t && e.onEmpty()
                }, !0)
            }
        }
    }).directive("progressbar", ["$transition", function(e) {
        return {
            restrict: "EA",
            replace: !0,
            scope: {
                width: "=",
                old: "=",
                type: "=",
                animate: "="
            },
            templateUrl: "template/progressbar/bar.html",
            link: function(t, n) {
                t.$watch("width", function(r) {
                    t.animate ? (n.css("width", t.old + "%"), e(n, {
                        width: r + "%"
                    })) : n.css("width", r + "%")
                })
            }
        }
    }]), angular.module("ui.bootstrap.rating", []).constant("ratingConfig", {
        max: 5,
        stateOn: null,
        stateOff: null
    }).controller("RatingController", ["$scope", "$attrs", "$parse", "ratingConfig", function(e, t, n, r) {
        this.maxRange = angular.isDefined(t.max) ? e.$parent.$eval(t.max) : r.max, this.stateOn = angular.isDefined(t.stateOn) ? e.$parent.$eval(t.stateOn) : r.stateOn, this.stateOff = angular.isDefined(t.stateOff) ? e.$parent.$eval(t.stateOff) : r.stateOff, this.createRateObjects = function(e) {
            for (var t = {
                    stateOn: this.stateOn,
                    stateOff: this.stateOff
                }, n = 0, r = e.length; r > n; n++) e[n] = angular.extend({
                index: n
            }, t, e[n]);
            return e
        }, e.range = angular.isDefined(t.ratingStates) ? this.createRateObjects(angular.copy(e.$parent.$eval(t.ratingStates))) : this.createRateObjects(new Array(this.maxRange)), e.rate = function(t) {
            e.readonly || e.value === t || (e.value = t)
        }, e.enter = function(t) {
            e.readonly || (e.val = t), e.onHover({
                value: t
            })
        }, e.reset = function() {
            e.val = angular.copy(e.value), e.onLeave()
        }, e.$watch("value", function(t) {
            e.val = t
        }), e.readonly = !1, t.readonly && e.$parent.$watch(n(t.readonly), function(t) {
            e.readonly = !!t
        })
    }]).directive("rating", function() {
        return {
            restrict: "EA",
            scope: {
                value: "=",
                onHover: "&",
                onLeave: "&"
            },
            controller: "RatingController",
            templateUrl: "template/rating/rating.html",
            replace: !0
        }
    }), angular.module("ui.bootstrap.tabs", []).directive("tabs", function() {
        return function() {
            throw new Error("The `tabs` directive is deprecated, please migrate to `tabset`. Instructions can be found at http://github.com/angular-ui/bootstrap/tree/master/CHANGELOG.md")
        }
    }).controller("TabsetController", ["$scope", function(e) {
        var t = this,
            n = t.tabs = e.tabs = [];
        t.select = function(e) {
            angular.forEach(n, function(e) {
                e.active = !1
            }), e.active = !0
        }, t.addTab = function(e) {
            n.push(e), (1 === n.length || e.active) && t.select(e)
        }, t.removeTab = function(e) {
            var r = n.indexOf(e);
            if (e.active && n.length > 1) {
                var o = r == n.length - 1 ? r - 1 : r + 1;
                t.select(n[o])
            }
            n.splice(r, 1)
        }
    }]).directive("tabset", function() {
        return {
            restrict: "EA",
            transclude: !0,
            replace: !0,
            require: "^tabset",
            scope: {},
            controller: "TabsetController",
            templateUrl: "template/tabs/tabset.html",
            compile: function(e, t, n) {
                return function(e, t, r, o) {
                    e.vertical = angular.isDefined(r.vertical) ? e.$parent.$eval(r.vertical) : !1, e.justified = angular.isDefined(r.justified) ? e.$parent.$eval(r.justified) : !1, e.type = angular.isDefined(r.type) ? e.$parent.$eval(r.type) : "tabs", e.direction = angular.isDefined(r.direction) ? e.$parent.$eval(r.direction) : "top", e.tabsAbove = "below" != e.direction, o.$scope = e, o.$transcludeFn = n
                }
            }
        }
    }).directive("tab", ["$parse", function(e) {
        return {
            require: "^tabset",
            restrict: "EA",
            replace: !0,
            templateUrl: "template/tabs/tab.html",
            transclude: !0,
            scope: {
                heading: "@",
                onSelect: "&select",
                onDeselect: "&deselect"
            },
            controller: function() {},
            compile: function(t, n, r) {
                return function(t, n, o, i) {
                    var s, a;
                    o.active ? (s = e(o.active), a = s.assign, t.$parent.$watch(s, function(e, n) {
                        e !== n && (t.active = !!e)
                    }), t.active = s(t.$parent)) : a = s = angular.noop, t.$watch("active", function(e) {
                        a(t.$parent, e), e ? (i.select(t), t.onSelect()) : t.onDeselect()
                    }), t.disabled = !1, o.disabled && t.$parent.$watch(e(o.disabled), function(e) {
                        t.disabled = !!e
                    }), t.select = function() {
                        t.disabled || (t.active = !0)
                    }, i.addTab(t), t.$on("$destroy", function() {
                        i.removeTab(t)
                    }), t.$transcludeFn = r
                }
            }
        }
    }]).directive("tabHeadingTransclude", [function() {
        return {
            restrict: "A",
            require: "^tab",
            link: function(e, t) {
                e.$watch("headingElement", function(e) {
                    e && (t.html(""), t.append(e))
                })
            }
        }
    }]).directive("tabContentTransclude", function() {
        function e(e) {
            return e.tagName && (e.hasAttribute("tab-heading") || e.hasAttribute("data-tab-heading") || "tab-heading" === e.tagName.toLowerCase() || "data-tab-heading" === e.tagName.toLowerCase())
        }
        return {
            restrict: "A",
            require: "^tabset",
            link: function(t, n, r) {
                var o = t.$eval(r.tabContentTransclude);
                o.$transcludeFn(o.$parent, function(t) {
                    angular.forEach(t, function(t) {
                        e(t) ? o.headingElement = t : n.append(t)
                    })
                })
            }
        }
    }).directive("tabsetTitles", function() {
        return {
            restrict: "A",
            require: "^tabset",
            templateUrl: "template/tabs/tabset-titles.html",
            replace: !0,
            link: function(e, t, n, r) {
                e.$eval(n.tabsetTitles) ? r.$transcludeFn(r.$scope.$parent, function(e) {
                    t.append(e)
                }) : t.remove()
            }
        }
    }), angular.module("ui.bootstrap.timepicker", []).constant("timepickerConfig", {
        hourStep: 1,
        minuteStep: 1,
        showMeridian: !0,
        meridians: ["AM", "PM"],
        readonlyInput: !1,
        mousewheel: !0
    }).directive("timepicker", ["$parse", "$log", "timepickerConfig", function(e, t, n) {
        return {
            restrict: "EA",
            require: "?^ngModel",
            replace: !0,
            scope: {},
            templateUrl: "template/timepicker/timepicker.html",
            link: function(r, o, i, s) {
                function a() {
                    var e = parseInt(r.hours, 10),
                        t = r.showMeridian ? e > 0 && 13 > e : e >= 0 && 24 > e;
                    return t ? (r.showMeridian && (12 === e && (e = 0), r.meridian === h[1] && (e += 12)), e) : void 0
                }

                function c() {
                    var e = parseInt(r.minutes, 10);
                    return e >= 0 && 60 > e ? e : void 0
                }

                function u(e) {
                    return angular.isDefined(e) && e.toString().length < 2 ? "0" + e : e
                }

                function l(e) {
                    p(), s.$setViewValue(new Date($)), d(e)
                }

                function p() {
                    s.$setValidity("time", !0), r.invalidHours = !1, r.invalidMinutes = !1
                }

                function d(e) {
                    var t = $.getHours(),
                        n = $.getMinutes();
                    r.showMeridian && (t = 0 === t || 12 === t ? 12 : t % 12), r.hours = "h" === e ? t : u(t), r.minutes = "m" === e ? n : u(n), r.meridian = $.getHours() < 12 ? h[0] : h[1]
                }

                function f(e) {
                    var t = new Date($.getTime() + 6e4 * e);
                    $.setHours(t.getHours(), t.getMinutes()), l()
                }
                if (s) {
                    var $ = new Date,
                        h = n.meridians,
                        g = n.hourStep;
                    i.hourStep && r.$parent.$watch(e(i.hourStep), function(e) {
                        g = parseInt(e, 10)
                    });
                    var m = n.minuteStep;
                    i.minuteStep && r.$parent.$watch(e(i.minuteStep), function(e) {
                        m = parseInt(e, 10)
                    }), r.showMeridian = n.showMeridian, i.showMeridian && r.$parent.$watch(e(i.showMeridian), function(e) {
                        if (r.showMeridian = !!e, s.$error.time) {
                            var t = a(),
                                n = c();
                            angular.isDefined(t) && angular.isDefined(n) && ($.setHours(t), l())
                        } else d()
                    });
                    var v = o.find("input"),
                        _ = v.eq(0),
                        y = v.eq(1),
                        b = angular.isDefined(i.mousewheel) ? r.$eval(i.mousewheel) : n.mousewheel;
                    if (b) {
                        var w = function(e) {
                            e.originalEvent && (e = e.originalEvent);
                            var t = e.wheelDelta ? e.wheelDelta : -e.deltaY;
                            return e.detail || t > 0
                        };
                        _.bind("mousewheel wheel", function(e) {
                            r.$apply(w(e) ? r.incrementHours() : r.decrementHours()), e.preventDefault()
                        }), y.bind("mousewheel wheel", function(e) {
                            r.$apply(w(e) ? r.incrementMinutes() : r.decrementMinutes()), e.preventDefault()
                        })
                    }
                    if (r.readonlyInput = angular.isDefined(i.readonlyInput) ? r.$eval(i.readonlyInput) : n.readonlyInput, r.readonlyInput) r.updateHours = angular.noop, r.updateMinutes = angular.noop;
                    else {
                        var x = function(e, t) {
                            s.$setViewValue(null), s.$setValidity("time", !1), angular.isDefined(e) && (r.invalidHours = e), angular.isDefined(t) && (r.invalidMinutes = t)
                        };
                        r.updateHours = function() {
                            var e = a();
                            angular.isDefined(e) ? ($.setHours(e), l("h")) : x(!0)
                        }, _.bind("blur", function() {
                            !r.validHours && r.hours < 10 && r.$apply(function() {
                                r.hours = u(r.hours)
                            })
                        }), r.updateMinutes = function() {
                            var e = c();
                            angular.isDefined(e) ? ($.setMinutes(e), l("m")) : x(void 0, !0)
                        }, y.bind("blur", function() {
                            !r.invalidMinutes && r.minutes < 10 && r.$apply(function() {
                                r.minutes = u(r.minutes)
                            })
                        })
                    }
                    s.$render = function() {
                        var e = s.$modelValue ? new Date(s.$modelValue) : null;
                        isNaN(e) ? (s.$setValidity("time", !1), t.error('Timepicker directive: "ng-model" value must be a Date object, a number of milliseconds since 01.01.1970 or a string representing an RFC2822 or ISO 8601 date.')) : (e && ($ = e), p(), d())
                    }, r.incrementHours = function() {
                        f(60 * g)
                    }, r.decrementHours = function() {
                        f(60 * -g)
                    }, r.incrementMinutes = function() {
                        f(m)
                    }, r.decrementMinutes = function() {
                        f(-m)
                    }, r.toggleMeridian = function() {
                        f(720 * ($.getHours() < 12 ? 1 : -1))
                    }
                }
            }
        }
    }]), angular.module("ui.bootstrap.typeahead", ["ui.bootstrap.position", "ui.bootstrap.bindHtml"]).factory("typeaheadParser", ["$parse", function(e) {
        var t = /^\s*(.*?)(?:\s+as\s+(.*?))?\s+for\s+(?:([\$\w][\$\w\d]*))\s+in\s+(.*)$/;
        return {
            parse: function(n) {
                var r = n.match(t);
                if (!r) throw new Error("Expected typeahead specification in form of '_modelValue_ (as _label_)? for _item_ in _collection_' but got '" + n + "'.");
                return {
                    itemName: r[3],
                    source: e(r[4]),
                    viewMapper: e(r[2] || r[1]),
                    modelMapper: e(r[1])
                }
            }
        }
    }]).directive("typeahead", ["$compile", "$parse", "$q", "$timeout", "$document", "$position", "typeaheadParser", function(e, t, n, r, o, i, s) {
        var a = [9, 13, 27, 38, 40];
        return {
            require: "ngModel",
            link: function(c, u, l, p) {
                var d, f = c.$eval(l.typeaheadMinLength) || 1,
                    $ = c.$eval(l.typeaheadWaitMs) || 0,
                    h = c.$eval(l.typeaheadEditable) !== !1,
                    g = t(l.typeaheadLoading).assign || angular.noop,
                    m = t(l.typeaheadOnSelect),
                    v = l.typeaheadInputFormatter ? t(l.typeaheadInputFormatter) : void 0,
                    _ = t(l.ngModel).assign,
                    y = s.parse(l.typeahead),
                    b = angular.element("<typeahead-popup></typeahead-popup>");
                b.attr({
                    matches: "matches",
                    active: "activeIdx",
                    select: "select(activeIdx)",
                    query: "query",
                    position: "position"
                }), angular.isDefined(l.typeaheadTemplateUrl) && b.attr("template-url", l.typeaheadTemplateUrl);
                var w = c.$new();
                c.$on("$destroy", function() {
                    w.$destroy()
                });
                var x = function() {
                        w.matches = [], w.activeIdx = -1
                    },
                    k = function(e) {
                        var t = {
                            $viewValue: e
                        };
                        g(c, !0), n.when(y.source(c, t)).then(function(n) {
                            if (e === p.$viewValue && d) {
                                if (n.length > 0) {
                                    w.activeIdx = 0, w.matches.length = 0;
                                    for (var r = 0; r < n.length; r++) t[y.itemName] = n[r], w.matches.push({
                                        label: y.viewMapper(w, t),
                                        model: n[r]
                                    });
                                    w.query = e, w.position = i.position(u), w.position.top = w.position.top + u.prop("offsetHeight")
                                } else x();
                                g(c, !1)
                            }
                        }, function() {
                            x(), g(c, !1)
                        })
                    };
                x(), w.query = void 0;
                var C;
                p.$parsers.unshift(function(e) {
                    return d = !0, e && e.length >= f ? $ > 0 ? (C && r.cancel(C), C = r(function() {
                        k(e)
                    }, $)) : k(e) : (g(c, !1), x()), h ? e : e ? (p.$setValidity("editable", !1), void 0) : (p.$setValidity("editable", !0), e)
                }), p.$formatters.push(function(e) {
                    var t, n, r = {};
                    return v ? (r.$model = e, v(c, r)) : (r[y.itemName] = e, t = y.viewMapper(c, r), r[y.itemName] = void 0, n = y.viewMapper(c, r), t !== n ? t : e)
                }), w.select = function(e) {
                    var t, n, r = {};
                    r[y.itemName] = n = w.matches[e].model, t = y.modelMapper(c, r), _(c, t), p.$setValidity("editable", !0), m(c, {
                        $item: n,
                        $model: t,
                        $label: y.viewMapper(c, r)
                    }), x(), u[0].focus()
                }, u.bind("keydown", function(e) {
                    return 0 === w.matches.length || -1 === a.indexOf(e.which) ? (13 === e.which && e.preventDefault(), void 0) : (e.preventDefault(), 40 === e.which ? (w.activeIdx = (w.activeIdx + 1) % w.matches.length, w.$digest()) : 38 === e.which ? (w.activeIdx = (w.activeIdx ? w.activeIdx : w.matches.length) - 1, w.$digest()) : 13 === e.which || 9 === e.which ? w.$apply(function() {
                        w.select(w.activeIdx)
                    }) : 27 === e.which && (e.stopPropagation(), x(), w.$digest()), void 0)
                }), u.bind("blur", function() {
                    d = !1
                });
                var S = function(e) {
                    u[0] !== e.target && (x(), w.$digest())
                };
                o.bind("click", S), c.$on("$destroy", function() {
                    o.unbind("click", S)
                }), u.after(e(b)(w))
            }
        }
    }]).directive("typeaheadPopup", function() {
        return {
            restrict: "E",
            scope: {
                matches: "=",
                query: "=",
                active: "=",
                position: "=",
                select: "&"
            },
            replace: !0,
            templateUrl: "template/typeahead/typeahead-popup.html",
            link: function(e, t, n) {
                e.templateUrl = n.templateUrl, e.isOpen = function() {
                    return e.matches.length > 0
                }, e.isActive = function(t) {
                    return e.active == t
                }, e.selectActive = function(t) {
                    e.active = t
                }, e.selectMatch = function(t) {
                    e.select({
                        activeIdx: t
                    })
                }
            }
        }
    }).directive("typeaheadMatch", ["$http", "$templateCache", "$compile", "$parse", function(e, t, n, r) {
        return {
            restrict: "E",
            scope: {
                index: "=",
                match: "=",
                query: "="
            },
            link: function(o, i, s) {
                var a = r(s.templateUrl)(o.$parent) || "template/typeahead/typeahead-match.html";
                e.get(a, {
                    cache: t
                }).success(function(e) {
                    i.replaceWith(n(e.trim())(o))
                })
            }
        }
    }]).filter("typeaheadHighlight", function() {
        function e(e) {
            return e.replace(/([.?*+^$[\]\\(){}|-])/g, "\\$1")
        }
        return function(t, n) {
            return n ? t.replace(new RegExp(e(n), "gi"), "<strong>$&</strong>") : t
        }
    }), angular.module("template/accordion/accordion-group.html", []).run(["$templateCache", function(e) {
        e.put("template/accordion/accordion-group.html", '<div class="panel panel-default">\n  <div class="panel-heading">\n    <h4 class="panel-title">\n      <a href="" class="accordion-toggle" ng-click="isOpen = !isOpen" accordion-transclude="heading">{{heading}}</a>\n    </h4>\n  </div>\n  <div class="panel-collapse" collapse="!isOpen">\n     <div class="panel-body" ng-transclude></div>\n  </div>\n</div>')
    }]), angular.module("template/accordion/accordion.html", []).run(["$templateCache", function(e) {
        e.put("template/accordion/accordion.html", '<div class="panel-group" ng-transclude></div>')
    }]), angular.module("template/alert/alert.html", []).run(["$templateCache", function(e) {
        e.put("template/alert/alert.html", "<div class='alert' ng-class='\"alert-\" + (type || \"warning\")'>\n    <button ng-show='closeable' type='button' class='close' ng-click='close()'>&times;</button>\n    <div ng-transclude></div>\n</div>\n")
    }]), angular.module("template/carousel/carousel.html", []).run(["$templateCache", function(e) {
        e.put("template/carousel/carousel.html", '<div ng-mouseenter="pause()" ng-mouseleave="play()" class="carousel">\n    <ol class="carousel-indicators" ng-show="slides().length > 1">\n        <li ng-repeat="slide in slides()" ng-class="{active: isActive(slide)}" ng-click="select(slide)"></li>\n    </ol>\n    <div class="carousel-inner" ng-transclude></div>\n    <a class="left carousel-control" ng-click="prev()" ng-show="slides().length > 1"><span class="icon-prev"></span></a>\n    <a class="right carousel-control" ng-click="next()" ng-show="slides().length > 1"><span class="icon-next"></span></a>\n</div>\n')
    }]), angular.module("template/carousel/slide.html", []).run(["$templateCache", function(e) {
        e.put("template/carousel/slide.html", "<div ng-class=\"{\n    'active': leaving || (active && !entering),\n    'prev': (next || active) && direction=='prev',\n    'next': (next || active) && direction=='next',\n    'right': direction=='prev',\n    'left': direction=='next'\n  }\" class=\"item text-center\" ng-transclude></div>\n")
    }]), angular.module("template/datepicker/datepicker.html", []).run(["$templateCache", function(e) {
        e.put("template/datepicker/datepicker.html", '<table style="table-layout:fixed;" class="table-condensed">\n  <!-- secondary: last month, disabled: disabled -->\n  <thead class="text-center">\n    <tr>\n      <th style="overflow: hidden; min-width: 26px">\n        <button type="button" class="btn btn-xs btn-link" ng-click="move(-1)"> \n          <span class="glyphicon glyphicon-chevron-left"> </span> \n        </button>\n      </th>\n      <th colspan="{{rows[0].length - 2 + showWeekNumbers}}"><button type="button" class="btn btn-md btn-link btn-block" ng-click="toggleMode()"><strong>{{title}}</strong></button></th>\n      <th style="overflow: hidden; min-width: 26px">\n        <button type="button" class="btn btn-xs btn-link" ng-click="move(1)"> \n          <span class="glyphicon glyphicon-chevron-right"> </span> \n        </button>\n      </th>\n    </tr>\n    <tr ng-show="labels.length > 0">\n      <th class="text-center" ng-show="showWeekNumbers" style="overflow: hidden; min-width: 26px"><h6>#</h6></th>\n      <th class="text-center" ng-repeat="label in labels" style="overflow: hidden; min-width: 26px"><h6>{{label}}</h6></th>\n    </tr>\n  </thead>\n  <tbody>\n    <tr ng-repeat="row in rows">\n      <td ng-show="showWeekNumbers" class="text-center" style="overflow: hidden; min-width: 26px"><button type="button" class="btn btn-xs btn-link" disabled><strong><em>{{ getWeekNumber(row) }}</em></strong></button></td>\n      <td ng-repeat="dt in row" class="text-center" style="overflow: hidden; min-width: 26px">\n        <button type="button" style="width: 100%; border: 0px" class="btn btn-xs" ng-class="{\'btn-primary\': dt.selected, \'btn-default\': !dt.selected}" ng-click="select(dt.date)" ng-disabled="dt.disabled"><span ng-class="{\'text-muted\': dt.secondary && !dt.selected}">{{dt.label}}</span></button>\n      </td>\n    </tr>\n  </tbody>\n</table>')
    }]), angular.module("template/datepicker/popup.html", []).run(["$templateCache", function(e) {
        e.put("template/datepicker/popup.html", '<ul class="dropdown-menu" ng-style="{display: (isOpen && \'block\') || \'none\', top: position.top+\'px\', left: position.left+\'px\'}">\n    <li ng-transclude></li>\n   <li class="divider"></li>\n <li style="padding: 9px;">\n        <span class="btn-group">\n          <button class="btn btn-xs btn-default" ng-click="today()">Today</button>\n          <button class="btn btn-xs btn-info" ng-click="showWeeks = ! showWeeks" ng-class="{active: showWeeks}">Weeks</button>\n          <button class="btn btn-xs btn-danger" ng-click="clear()">Clear</button>\n       </span>\n       <button class="btn btn-xs btn-success pull-right" ng-click="isOpen = false">Close</button>\n    </li>\n</ul>\n')
    }]), angular.module("template/modal/backdrop.html", []).run(["$templateCache", function(e) {
        e.put("template/modal/backdrop.html", '<div class="modal-backdrop fade" ng-class="{in: animate}" ng-style="{\'z-index\': 1040 + index*10}"></div>')
    }]), angular.module("template/modal/window.html", []).run(["$templateCache", function(e) {
        e.put("template/modal/window.html", '<div class="modal fade {{ windowClass }}" ng-class="{in: animate}" ng-style="{\'z-index\': 1050 + index*10, display: \'block\'}" ng-click="close($event)">\n    <div class="modal-dialog"><div class="modal-content" ng-transclude></div></div>\n</div>')
    }]), angular.module("template/pagination/pager.html", []).run(["$templateCache", function(e) {
        e.put("template/pagination/pager.html", '<ul class="pager">\n    <li ng-repeat="page in pages" ng-class="{disabled: page.disabled, previous: page.previous, next: page.next}"><a ng-click="selectPage(page.number)">{{page.text}}</a></li>\n</ul>')
    }]), angular.module("template/pagination/pagination.html", []).run(["$templateCache", function(e) {
        e.put("template/pagination/pagination.html", '<ul class="pagination">\n  <li ng-repeat="page in pages" ng-class="{active: page.active, disabled: page.disabled}"><a ng-click="selectPage(page.number)">{{page.text}}</a></li>\n</ul>\n')
    }]), angular.module("template/tooltip/tooltip-html-unsafe-popup.html", []).run(["$templateCache", function(e) {
        e.put("template/tooltip/tooltip-html-unsafe-popup.html", '<div class="tooltip {{placement}}" ng-class="{ in: isOpen(), fade: animation() }">\n  <div class="tooltip-arrow"></div>\n  <div class="tooltip-inner" bind-html-unsafe="content"></div>\n</div>\n')
    }]), angular.module("template/tooltip/tooltip-popup.html", []).run(["$templateCache", function(e) {
        e.put("template/tooltip/tooltip-popup.html", '<div class="tooltip {{placement}}" ng-class="{ in: isOpen(), fade: animation() }">\n  <div class="tooltip-arrow"></div>\n  <div class="tooltip-inner" ng-bind="content"></div>\n</div>\n')
    }]), angular.module("template/popover/popover.html", []).run(["$templateCache", function(e) {
        e.put("template/popover/popover.html", '<div class="popover {{placement}}" ng-class="{ in: isOpen(), fade: animation() }">\n  <div class="arrow"></div>\n\n  <div class="popover-inner">\n      <h3 class="popover-title" ng-bind="title" ng-show="title"></h3>\n      <div class="popover-content" ng-bind="content"></div>\n  </div>\n</div>\n')
    }]), angular.module("template/progressbar/bar.html", []).run(["$templateCache", function(e) {
        e.put("template/progressbar/bar.html", '<div class="bar" ng-class=\'type && "bar-" + type\'></div>')
    }]), angular.module("template/progressbar/progress.html", []).run(["$templateCache", function(e) {
        e.put("template/progressbar/progress.html", '<div class="progress"><progressbar ng-repeat="bar in bars" width="bar.to" old="bar.from" animate="bar.animate" type="bar.type"></progressbar></div>')
    }]), angular.module("template/rating/rating.html", []).run(["$templateCache", function(e) {
        e.put("template/rating/rating.html", '<span ng-mouseleave="reset()">\n    <i ng-repeat="r in range" ng-mouseenter="enter($index + 1)" ng-click="rate($index + 1)" class="glyphicon" ng-class="$index < val && (r.stateOn || \'glyphicon-star\') || (r.stateOff || \'glyphicon-star-empty\')"></i>\n</span>')
    }]), angular.module("template/tabs/tab.html", []).run(["$templateCache", function(e) {
        e.put("template/tabs/tab.html", '<li ng-class="{active: active, disabled: disabled}">\n  <a ng-click="select()" tab-heading-transclude>{{heading}}</a>\n</li>\n')
    }]), angular.module("template/tabs/tabset-titles.html", []).run(["$templateCache", function(e) {
        e.put("template/tabs/tabset-titles.html", "<ul class=\"nav {{type && 'nav-' + type}}\" ng-class=\"{'nav-stacked': vertical, 'nav-justified': justified}\">\n</ul>\n")
    }]), angular.module("template/tabs/tabset.html", []).run(["$templateCache", function(e) {
        e.put("template/tabs/tabset.html", '\n<div class="tabbable" ng-class="{\'tabs-right\': direction == \'right\', \'tabs-left\': direction == \'left\', \'tabs-below\': direction == \'below\'}">\n  <div tabset-titles="tabsAbove"></div>\n  <div class="tab-content">\n    <div class="tab-pane" \n         ng-repeat="tab in tabs" \n         ng-class="{active: tab.active}"\n         tab-content-transclude="tab">\n    </div>\n  </div>\n  <div tabset-titles="!tabsAbove"></div>\n</div>\n')
    }]), angular.module("template/timepicker/timepicker.html", []).run(["$templateCache", function(e) {
        e.put("template/timepicker/timepicker.html", '<span>\n    <div class="row">\n        <div class="col-xs-4 text-center">\n            <a ng-click="incrementHours()" class="btn btn-link"><i class="glyphicon glyphicon-chevron-up"></i></a>\n        </div>\n        <div class="col-xs-6 text-center">\n            <a ng-click="incrementMinutes()" class="btn btn-link"><i class="glyphicon glyphicon-chevron-up"></i></a>\n        </div>\n        <div class="col-xs-2"> </div>\n    </div>\n\n    <div class="row">\n        <div class="col-xs-4">\n            <div class="form-group" ng-class="{\'has-error\': invalidHours}" style="margin-bottom: 0px">\n                <input type="text" ng-model="hours" ng-change="updateHours()" class="form-control text-center" ng-mousewheel="incrementHours()" ng-readonly="readonlyInput" maxlength="2"> \n            </div>\n        </div>\n        <div class="col-xs-6">\n            <div class="input-group" ng-class="{\'has-error\': invalidMinutes}">\n                <span class="input-group-addon">:</span>\n                <input type="text" ng-model="minutes" ng-change="updateMinutes()" class="form-control text-center" ng-readonly="readonlyInput" maxlength="2">\n            </div>\n        </div>\n        <div class="col-xs-2">\n            <button ng-click="toggleMeridian()" class="btn btn-default text-center" ng-show="showMeridian">{{meridian}}</button>\n        </div>\n    </div>\n\n    <div class="row">\n        <div class="col-xs-4 text-center">\n            <a ng-click="decrementHours()" class="btn btn-link"><i class="glyphicon glyphicon-chevron-down"></i></a>\n        </div>\n        <div class="col-xs-6 text-center">\n            <a ng-click="decrementMinutes()" class="btn btn-link"><i class="glyphicon glyphicon-chevron-down"></i></a>\n        </div>\n        <div class="col-xs-2"> </div>\n    </div>\n</span>')
    }]), angular.module("template/typeahead/typeahead-match.html", []).run(["$templateCache", function(e) {
        e.put("template/typeahead/typeahead-match.html", '<a tabindex="-1" bind-html-unsafe="match.label | typeaheadHighlight:query"></a>')
    }]), angular.module("template/typeahead/typeahead-popup.html", []).run(["$templateCache", function(e) {
        e.put("template/typeahead/typeahead-popup.html", "<ul class=\"dropdown-menu\" ng-style=\"{display: isOpen()&&'block' || 'none', top: position.top+'px', left: position.left+'px'}\">\n" + '    <li ng-repeat="match in matches" ng-class="{active: isActive($index) }" ng-mouseenter="selectActive($index)" ng-click="selectMatch($index)">\n        <typeahead-match index="$index" match="match" query="query" template-url="templateUrl"></typeahead-match>\n    </li>\n</ul>')
    }]);
var duScrollDefaultEasing = function(e) {
    return .5 > e ? Math.pow(2 * e, 2) / 2 : 1 - Math.pow(2 * (1 - e), 2) / 2
};
angular.module("duScroll", ["duScroll.scrollspy", "duScroll.requestAnimation", "duScroll.smoothScroll", "duScroll.scrollContainer", "duScroll.scrollHelpers"]).value("duScrollDuration", 1e3).value("duScrollEasing", duScrollDefaultEasing), angular.module("duScroll.scrollHelpers", []).run(["$window", "requestAnimation", "duScrollEasing", function(e, t, n) {
        var r = angular.element.prototype;
        this.$get = function() {
            return r
        };
        var o = function(e) {
                return "undefined" != typeof HTMLDocument && e instanceof HTMLDocument || e.nodeType && e.nodeType === e.DOCUMENT_NODE
            },
            i = function(e) {
                return "undefined" != typeof HTMLElement && e instanceof HTMLElement || e.nodeType && e.nodeType === e.ELEMENT_NODE
            },
            s = function(e) {
                return i(e) || o(e) ? e : e[0]
            };
        r.scrollTo = function(t, n, r) {
            if (angular.isElement(t)) return this.scrollToElement(t, 0, n, r);
            if (r) return this.scrollToAnimated.apply(this, arguments);
            var i = s(this);
            return o(i) ? e.scrollTo(t, n) : (i.scrollLeft = t, i.scrollTop = n, void 0)
        }, r.scrollToAnimated = function(e, r, o, i) {
            o && !i && (i = n);
            var s = this.scrollLeft(),
                a = this.scrollTop(),
                c = Math.round(e - s),
                u = Math.round(r - a);
            if (c || u) {
                var l = 0,
                    p = Math.ceil(o / 60),
                    d = function() {
                        l++;
                        var e = l === p ? 1 : i(l / p);
                        this.scrollTo(s + Math.ceil(c * e), a + Math.ceil(u * e)), p > l && t(d)
                    }.bind(this);
                d()
            }
        }, r.scrollToElement = function(e, t, n, r) {
            var o = s(this),
                a = this.scrollTop() + s(e).getBoundingClientRect().top - t;
            i(o) && (a -= o.getBoundingClientRect().top), this.scrollTo(0, a, n, r)
        }, r.scrollLeft = function(t, n, r) {
            if (angular.isNumber(t)) return this.scrollTo(t, this.scrollTop(), n, r);
            var i = s(this);
            return o(i) ? e.scrollX || document.documentElement.scrollLeft || document.body.scrollLeft : i.scrollLeft
        }, r.scrollTop = function(t, n, r) {
            if (angular.isNumber(t)) return this.scrollTo(this.scrollTop(), t, n, r);
            var i = s(this);
            return o(i) ? e.scrollY || document.documentElement.scrollTop || document.body.scrollTop : i.scrollTop
        }
    }]), angular.module("duScroll.requestAnimation", []).factory("requestAnimation", ["$window", "$timeout", function(e, t) {
        return e.requestAnimationFrame || e.webkitRequestAnimationFrame || e.mozRequestAnimationFrame || e.oRequestAnimationFrame || e.msRequestAnimationFrame || function(e) {
            t(e, 1e3 / 60)
        }
    }]), angular.module("duScroll.spyAPI", ["duScroll.scrollContainerAPI"]).factory("spyAPI", ["$rootScope", "scrollContainerAPI", function(e, t) {
        var n = function(t) {
                return function() {
                    var n = t.container,
                        r = n[0],
                        o = 0;
                    r instanceof HTMLElement && (o = r.getBoundingClientRect().top);
                    var i, s, a, c, u, l;
                    for (c = t.spies, s = t.currentlyActive, a = void 0, i = 0; i < c.length; i++) {
                        if (u = c[i], l = u.getTargetPosition(), !l) return;
                        l.top + u.offset - o < 20 && -1 * l.top + o < l.height && (!a || a.top < l.top) && (a = {
                            top: l.top,
                            spy: u
                        })
                    }
                    a && (a = a.spy), s !== a && (s && (s.$element.removeClass("active"), e.$broadcast("duScrollspy:becameInactive", s.$element)), a && (a.$element.addClass("active"), e.$broadcast("duScrollspy:becameActive", a.$element)), t.currentlyActive = a)
                }
            },
            r = {},
            o = function(e) {
                var t = e.$id,
                    o = {
                        spies: []
                    };
                return o.handler = n(o), r[t] = o, e.$on("$destroy", function() {
                    i(e)
                }), t
            },
            i = function(e) {
                var t = e.$id,
                    n = r[t],
                    o = n.container;
                o && o.off("scroll", n.handler), delete r[t]
            },
            s = o(e),
            a = function(e) {
                return r[e.$id] ? r[e.$id] : e.$parent ? a(e.$parent) : r[s]
            },
            c = function(e) {
                return a(e.$element.scope())
            },
            u = function(e) {
                var n = c(e);
                c(e).spies.push(e), n.container || (n.container = t.getContainer(e.$element.scope()), n.container.on("scroll", n.handler).triggerHandler("scroll"))
            },
            l = function(e) {
                var t = c(e);
                e === t.currentlyActive && (t.currentlyActive = null);
                var n = t.spies.indexOf(e); - 1 !== n && t.spies.splice(n, 1)
            };
        return {
            addSpy: u,
            removeSpy: l,
            createContext: o,
            destroyContext: i
        }
    }]), angular.module("duScroll.scrollContainerAPI", []).factory("scrollContainerAPI", ["$document", function(e) {
        var t = {},
            n = function(e, n) {
                var r = e.$id;
                return t[r] = n, r
            },
            r = function(e) {
                return t[e.$id] ? e.$id : e.$parent ? r(e.$parent) : void 0
            },
            o = function(n) {
                var o = r(n);
                return o ? t[o] : e
            },
            i = function(e) {
                var n = r(e);
                n && delete t[n]
            };
        return {
            getContainerId: r,
            getContainer: o,
            setContainer: n,
            removeContainer: i
        }
    }]), angular.module("duScroll.smoothScroll", ["duScroll.scrollHelpers", "duScroll.scrollContainerAPI"]).directive("duSmoothScroll", ["duScrollDuration", "scrollContainerAPI", function(e, t) {
        return {
            link: function(n, r, o) {
                var i = angular.element(r[0]);
                i.on("click", function(r) {
                    if (o.href && -1 !== o.href.indexOf("#")) {
                        var i = document.getElementById(o.href.replace(/.*(?=#[^\s]+$)/, "").substring(1));
                        if (i && i.getBoundingClientRect) {
                            r.stopPropagation && r.stopPropagation(), r.preventDefault && r.preventDefault();
                            var s = o.offset ? parseInt(o.offset, 10) : 0,
                                a = o.duration ? parseInt(o.duration, 10) : e,
                                c = t.getContainer(n);
                            c.scrollToElement(angular.element(i), isNaN(s) ? 0 : s, isNaN(a) ? 0 : a)
                        }
                    }
                })
            }
        }
    }]), angular.module("duScroll.spyContext", ["duScroll.spyAPI"]).directive("duSpyContext", ["spyAPI", function(e) {
        return {
            restrict: "A",
            scope: !0,
            compile: function() {
                return {
                    pre: function(t) {
                        e.createContext(t)
                    }
                }
            }
        }
    }]), angular.module("duScroll.scrollContainer", ["duScroll.scrollContainerAPI"]).directive("duScrollContainer", ["scrollContainerAPI", function(e) {
        return {
            restrict: "A",
            scope: !0,
            compile: function() {
                return {
                    pre: function(t, n, r) {
                        r.$observe("duScrollContainer", function(r) {
                            angular.isString(r) && (r = document.getElementById(r)), r = angular.isElement(r) ? angular.element(r) : n, e.setContainer(t, r), t.$on("$destroy", function() {
                                e.removeContainer(t)
                            })
                        })
                    }
                }
            }
        }
    }]), angular.module("duScroll.scrollspy", ["duScroll.spyAPI"]).directive("duScrollspy", ["spyAPI", "$timeout", function(e, t) {
        var n = function(e, t, n) {
            angular.isElement(e) ? this.target = e : angular.isString(e) && (this.targetId = e), this.$element = t, this.offset = n
        };
        return n.prototype.getTargetElement = function() {
            return !this.target && this.targetId && (this.target = document.getElementById(this.targetId)), this.target
        }, n.prototype.getTargetPosition = function() {
            var e = this.getTargetElement();
            return e ? e.getBoundingClientRect() : void 0
        }, n.prototype.flushTargetCache = function() {
            this.targetId && (this.target = void 0)
        }, {
            link: function(r, o, i) {
                var s, a = i.ngHref || i.href;
                a && -1 !== a.indexOf("#") ? s = a.replace(/.*(?=#[^\s]+$)/, "").substring(1) : i.duScrollspy && (s = i.duScrollspy), s && t(function() {
                    var t = new n(s, o, -(i.offset ? parseInt(i.offset, 10) : 0));
                    e.addSpy(t), r.$on("$destroy", function() {
                        e.removeSpy(t)
                    }), r.$on("$locationChangeSuccess", t.flushTargetCache.bind(t))
                }, 0)
            }
        }
    }]),
    function(e, t) {
        "use strict";

        function n(e) {
            var t, n = {},
                r = e.split(",");
            for (t = 0; t < r.length; t++) n[r[t]] = !0;
            return n
        }

        function r(e, n) {
            function r(e, r, s, a) {
                if (r = t.lowercase(r), b[r])
                    for (; g.last() && w[g.last()];) i("", g.last());
                y[r] && g.last() == r && i("", r), a = m[r] || !!a, a || g.push(r);
                var c = {};
                s.replace(l, function(e, t, n, r, i) {
                    var s = n || r || i || "";
                    c[t] = o(s)
                }), n.start && n.start(r, c, a)
            }

            function i(e, r) {
                var o, i = 0;
                if (r = t.lowercase(r))
                    for (i = g.length - 1; i >= 0 && g[i] != r; i--);
                if (i >= 0) {
                    for (o = g.length - 1; o >= i; o--) n.end && n.end(g[o]);
                    g.length = i
                }
            }
            var s, a, h, g = [],
                v = e;
            for (g.last = function() {
                    return g[g.length - 1]
                }; e;) {
                if (a = !0, g.last() && x[g.last()]) e = e.replace(new RegExp("(.*)<\\s*\\/\\s*" + g.last() + "[^>]*>", "i"), function(e, t) {
                    return t = t.replace(f, "$1").replace($, "$1"), n.chars && n.chars(o(t)), ""
                }), i("", g.last());
                else if (0 === e.indexOf("<!--") ? (s = e.indexOf("-->"), s >= 0 && (n.comment && n.comment(e.substring(4, s)), e = e.substring(s + 3), a = !1)) : d.test(e) ? (h = e.match(u), h && (e = e.substring(h[0].length), h[0].replace(u, i), a = !1)) : p.test(e) && (h = e.match(c), h && (e = e.substring(h[0].length), h[0].replace(c, r), a = !1)), a) {
                    s = e.indexOf("<");
                    var _ = 0 > s ? e : e.substring(0, s);
                    e = 0 > s ? "" : e.substring(s), n.chars && n.chars(o(_))
                }
                if (e == v) throw "Parse Error: " + e;
                v = e
            }
            i()
        }

        function o(e) {
            return T.innerHTML = e.replace(/</g, "&lt;"), T.innerText || T.textContent || ""
        }

        function i(e) {
            return e.replace(/&/g, "&amp;").replace(g, function(e) {
                return "&#" + e.charCodeAt(0) + ";"
            }).replace(/</g, "&lt;").replace(/>/g, "&gt;")
        }

        function s(e) {
            var n = !1,
                r = t.bind(e, e.push);
            return {
                start: function(e, o, s) {
                    e = t.lowercase(e), !n && x[e] && (n = e), n || 1 != k[e] || (r("<"), r(e), t.forEach(o, function(e, n) {
                        var o = t.lowercase(n);
                        1 != S[o] || C[o] === !0 && !e.match(h) || (r(" "), r(n), r('="'), r(i(e)), r('"'))
                    }), r(s ? "/>" : ">"))
                },
                end: function(e) {
                    e = t.lowercase(e), n || 1 != k[e] || (r("</"), r(e), r(">")), e == n && (n = !1)
                },
                chars: function(e) {
                    n || r(i(e))
                }
            }
        }
        var a = function(e) {
                var t = [];
                return r(e, s(t)), t.join("")
            },
            c = /^<\s*([\w:-]+)((?:\s+[\w:-]+(?:\s*=\s*(?:(?:"[^"]*")|(?:'[^']*')|[^>\s]+))?)*)\s*(\/?)\s*>/,
            u = /^<\s*\/\s*([\w:-]+)[^>]*>/,
            l = /([\w:-]+)(?:\s*=\s*(?:(?:"((?:[^"])*)")|(?:'((?:[^'])*)')|([^>\s]+)))?/g,
            p = /^</,
            d = /^<\s*\//,
            f = /<!--(.*?)-->/g,
            $ = /<!\[CDATA\[(.*?)]]>/g,
            h = /^((ftp|https?):\/\/|mailto:|#)/,
            g = /([^\#-~| |!])/g,
            m = n("area,br,col,hr,img,wbr"),
            v = n("colgroup,dd,dt,li,p,tbody,td,tfoot,th,thead,tr"),
            _ = n("rp,rt"),
            y = t.extend({}, _, v),
            b = t.extend({}, v, n("address,article,aside,blockquote,caption,center,del,dir,div,dl,figure,figcaption,footer,h1,h2,h3,h4,h5,h6,header,hgroup,hr,ins,map,menu,nav,ol,pre,script,section,table,ul")),
            w = t.extend({}, _, n("a,abbr,acronym,b,bdi,bdo,big,br,cite,code,del,dfn,em,font,i,img,ins,kbd,label,map,mark,q,ruby,rp,rt,s,samp,small,span,strike,strong,sub,sup,time,tt,u,var")),
            x = n("script,style"),
            k = t.extend({}, m, b, w, y),
            C = n("background,cite,href,longdesc,src,usemap"),
            S = t.extend({}, C, n("abbr,align,alt,axis,bgcolor,border,cellpadding,cellspacing,class,clear,color,cols,colspan,compact,coords,dir,face,headers,height,hreflang,hspace,ismap,lang,language,nohref,nowrap,rel,rev,rows,rowspan,rules,scope,scrolling,shape,span,start,summary,target,title,type,valign,value,vspace,width")),
            T = document.createElement("pre");
        t.module("ngSanitize", []).value("$sanitize", a), t.module("ngSanitize").directive("ngBindHtml", ["$sanitize", function(e) {
            return function(t, n, r) {
                n.addClass("ng-binding").data("$binding", r.ngBindHtml), t.$watch(r.ngBindHtml, function(t) {
                    t = e(t), n.html(t || "")
                })
            }
        }]), t.module("ngSanitize").filter("linky", function() {
            var e = /((ftp|https?):\/\/|(mailto:)?[A-Za-z0-9._%+-]+@)\S*[^\s\.\;\,\(\)\{\}\<\>]/,
                t = /^mailto:/;
            return function(n) {
                if (!n) return n;
                for (var r, o, i, a = n, c = [], u = s(c); r = a.match(e);) o = r[0], r[2] == r[3] && (o = "mailto:" + o), i = r.index, u.chars(a.substr(0, i)), u.start("a", {
                    href: o
                }), u.chars(r[0].replace(t, "")), u.end("a"), a = a.substring(i + r[0].length);
                return u.chars(a), c.join("")
            }
        })
    }(window, window.angular),
    function() {
        ! function(e) {
            var t;
            t = {
                init: function() {
                    return e(this).on("click", function() {
                        var n, r, o, i, s, a;
                        r = e(this).hasClass("btn-gray"), a = e(this).data("slug"), o = e(this).data("product_id"), i = e(this).data("quantity"), n = e(this).data("add-text"), s = e(this).data("remove-text"), r ? (t.removeFromCart(o, i), e(this).find("span").text(n), e(this).removeClass("btn-gray"), e(this).addClass("btn-primary"), e(this).children("i").removeClass("fa-check"), e(this).children("i").addClass("fa-shopping-cart")) : (t.addToCart(a), e(this).find("span").text(s), e(this).removeClass("btn-primary"), e(this).addClass("btn-gray"), e(this).children("i").removeClass("fa-shopping-cart"), e(this).children("i").addClass("fa-check"))
                    })
                },
                updateCart: function() {
                    e("#cart-badge")[0] && e.get(I18n.url("/cart/badge.js"))
                },
                addToCart: function(n) {
                    e.post(I18n.url("/cart/add_batch.json"), {
                        product_slugs: [n]
                    }, function() {
                        return t.updateCart()
                    })
                },
                removeFromCart: function(n, r) {
                    e.post(I18n.url("/cart/change.json"), {
                        product_id: n,
                        quantity: r
                    }, function() {
                        return t.updateCart()
                    })
                }
            }, e.fn.addtocart = function(n) {
                t[n] ? t[n].apply(this, Array.prototype.slice.call(arguments, 1)) : "object" != typeof n && n ? e.error("Method " + n + " does not exist on jQuery.addtocart") : t.init.apply(this, arguments)
            }
        }(jQuery)
    }.call(this),
    function() {
        ! function(e, t) {
            t(e).scroll(function() {
                var e, n;
                n = t(".cart-to-top").offset() || {}, e = t(".cart-nav").offset() || {}, n && e && (e.top > n.top ? t(".cart-nav").addClass("visible") : t(".cart-nav").removeClass("visible"))
            })
        }(window, jQuery)
    }.call(this),
    function() {
        $(".add-to-cart-btn").addtocart()
    }.call(this);