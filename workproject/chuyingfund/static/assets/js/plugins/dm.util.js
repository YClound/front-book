define(function(require){
    // human_date(+new Date/1000)
    function human_date(h) {
        var g = Math.floor((new Date).getTime() / 1000),
        l = new Date;
        l.setHours(0);
        l.setMinutes(0);
        l.setSeconds(0);
        l = Math.floor(l.getTime() / 1000);
        if (h > l && 0 <= g - h) {
            return 50 >= g - h ? (g = 10 * Math.floor((g - h) % 60 / 10), (10 < h ? g: 10) + "\u79d2\u524d") : 3600 > g - h ? Math.ceil((g - h) / 60) + "\u5206\u949f\u524d": Math.ceil((g - h) / 3600) + "\u5c0f\u65f6\u524d"
        }
        g = new Date;
        g.setTime(1000 * h);
        h = g.getMonth() + 1;
        var l = g.getDate(),
        k = g.getHours(),
        j = g.getMinutes();
        10 > h && (h = "0" + h);
        10 > l && (l = "0" + l);
        10 > k && (k = "0" + k);
        10 > j && (j = "0" + j);
        return g.getFullYear() + "-" + h + "-" + l + " " + k + ":" + j
    }
    var Util = {
        human_date:human_date,
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
        },
        // 判断闰年
        IsRunYear: function(year) {
            var monarr = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
            //闰年的话
            if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                monarr[1] = "29";
                return true;
            }
            return false;
        },
        // 获取url 参数 http://www.baidu.com/q?name=1&age=2 return {name:1,age:2}
        getQuery : function(){
            var search = window.location.search;
            var key = arguments[0];
      
            if (search.indexOf('?') != -1) {
                var params = search.substr(1).split('&');
                var query = {};
                var q = [];
                var name = '';

                for (i = 0; i < params.length; i++) {
                    q = params[i].split('=');
                    name = decodeURIComponent(q[0]);

                    if (name.substr(-2) == '[]') {
                        if (!query[name]) {
                            query[name] = [];
                        }
                        query[name].push(q[1]);
                    } else {
                        query[name] = q[1];
                    }

                }
                if (key) {
                    if (query[key]) {
                        return query[key];
                    }

                    return null;
                } else {
                    return query;
                }
            }

        },
        // 格式化日期 formatDate(new Date(1384931712000), 'yyyy-MM-dd hh:mm:ss')
        formatDate: function( date, fmt ) {
            var o = {
                "M+": date.getMonth() + 1,
                //月份   
                "d+": date.getDate(),
                //日   
                "h+": date.getHours(),
                //小时   
                "m+": date.getMinutes(),
                //分   
                "s+": date.getSeconds(),
                //秒   
                "q+": Math.floor((date.getMonth() + 3) / 3),
                //季度   
                "S": date.getMilliseconds() //毫秒   
            };
            if (/(y+)/.test(fmt)) {
                fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
            }
            for (var k in o) {
                if (new RegExp("(" + k + ")").test(fmt)) {
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
                }
            }
            return fmt;
        },
        // 获取站点根目录
        getRootPath: function() {
            var strFullPath = window.document.location.href;
            var strPath = window.document.location.pathname;
            var pos = strFullPath.indexOf(strPath);
            var prePath = strFullPath.substring(0, pos);
            var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
            return (prePath + postPath);
        },
    }; 

    return Util;
});