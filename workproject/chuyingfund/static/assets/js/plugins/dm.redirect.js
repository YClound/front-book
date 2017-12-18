!function () {
    dm.parseuri = function (str) {
        var o = {
                strictMode: false,
                key: ["source", "protocol", "authority", "userInfo", "user", "password", "host", "port", "relative", "path", "directory", "file", "query", "anchor"],
                q: {
                    name: "queryKey",
                    parser: /(?:^|&)([^&=]*)=?([^&]*)/g
                },
                parser: {
                    strict: /^(?:([^:\/?#]+):)?(?:\/\/((?:(([^:@]*)(?::([^:@]*))?)?@)?([^:\/?#]*)(?::(\d*))?))?((((?:[^?#\/]*\/)*)([^?#]*))(?:\?([^#]*))?(?:#(.*))?)/,
                    loose: /^(?:(?![^:@]+:[^:@\/]*@)([^:\/?#.]+):)?(?:\/\/)?((?:(([^:@]*)(?::([^:@]*))?)?@)?([^:\/?#]*)(?::(\d*))?)(((\/(?:[^?#](?![^?#\/]*\.[^?#\/.]+(?:[?#]|$)))*\/?)?([^?#\/]*))(?:\?([^#]*))?(?:#(.*))?)/
                }
            },
            m = o.parser[o.strictMode ? "strict" : "loose"].exec(str),
            uri = {},
            i = 14;

        while (i--) uri[o.key[i]] = m[i] || ""

        uri[o.q.name] = {}
        uri[o.key[12]].replace(o.q.parser, function ($0, $1, $2) {
            if ($1) uri[o.q.name][$1] = $2
        })

        return uri
    }


    dm.parseUrl = function (a) {
        return function (b, c, d) {
            a.href = b;
            c = {};
            for (d in a)if ("" + a[d] === a[d])c[d] = a[d];
            return c
        }
    }(document.createElement("a"))

    dm.parseQs = function (str, n, l, o, r) {
        for (n = str.split("&"), l = {}, r = 0, o = n.length; o > r; r++) {
            var s = n[r], t = s.indexOf("=");
            -1 !== t ? l[s.slice(0, t)] = decodeURIComponent(s.substr(t + 1)) : l[s] = null
        }
        return l
    }
    dm.qs = dm.parseQs(location.search.slice(1))

    dm.addParam = function (url, key, value) {
        if (!url) return url
        if (value == null) return url
        if (!!~url.indexOf('?')) url += '&'
        else url += '?'
        url += key + '=' + value
        return url
    }
    dm.removeParam = function (url, key) {
        if (!url) return url
        if (typeof key === 'string') key = [key]
        for (var i = 0, len = key.length; i < len; i++) {
            url = url.replace(new RegExp(key[i] + '=[\&]*&*'), '')
        }
        return url
    }
    dm.getRedirectUrl = function (res, userStorage) {
        // 跳转地址优先级
        // res.data -> qs.redirect_url -> serverData.redirect_url -> localStorage.getItem('redirect_url')
        if (userStorage == null) userStorage = true
        var r = (res && res.redirect_url) || dm.qs.redirect_url || serverData.redirectUrl || (userStorage && localStorage.getItem('redirect_url'))
        if (r) {
            r = dm.removeParam(r, ['ticket', 'refresh'])
            r = dm.addParam(r, 'refresh', '1')
        }
        return r

    }
    if (dm.qs.redirect_url) { // 存储 redirect_url
        localStorage.setItem('redirect_url', dm.qs.redirect_url)
    }
    var redirectUrl = dm.getRedirectUrl()
    if (redirectUrl) {
        $('.back-substation').css('visibility', 'visible').attr('href', redirectUrl)
    }
}()