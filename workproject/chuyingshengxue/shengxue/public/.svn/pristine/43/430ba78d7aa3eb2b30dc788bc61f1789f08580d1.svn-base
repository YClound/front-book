/*var linkLoad = function(url, callback) {

    var link = document.createElement("link");
    link.type = "text/css";
    link.rel = "stylesheet";
    if (useOnload) link.onload = function() {
        link.onload = function() {};
        setTimeout(callback, 7)
    };
    else var loadInterval = setInterval(function() {
        for (var i = 0; i < document.styleSheets.length; i++) {
            var sheet = document.styleSheets[i];
            if (sheet.href == link.href) {
                clearInterval(loadInterval);
                return callback()
            }
        }
    }, 10);
    link.href = url;
    head.appendChild(link)
};*/
(function(win){
    var X = win['X'] || {};
    X.obj2str = function(obj){
        //Object.prototype.toString.call(res.info) === "[object Array]"
        var str = [];
        switch (true) {
            case typeof obj === 'undefined':
              str = '';
              break;
            case typeof obj === 'string':
              str = '\"' + obj.replace(/([\"\\])/g, '\\$1').replace(/(\n)/g, '\\n').replace(/(\r)/g, '\\r').replace(/(\t)/g, '\\t') + '\"';
              break;
            case typeof obj === 'object':

              if (!(Object.prototype.toString.call(obj) === "[object Array]")) {
                console.log(121)
                for (var i in obj) {
                  str.push('\"' + i + '\":' + this.obj2str(obj[i]));
                }
                if (!!document.all && !/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/.test(obj.toString)) {
                  str.push('toString:' + obj.toString.toString());
                }
                str = '{' + str.join() + '}';
              } else {
                for (var j = 0; j < obj.length; j++) {
                  str.push(this.obj2str(obj[j]));
                }
                str = '[' + str.join() + ']';
              }
              break;
            default:
              str = obj.toString().replace(/\"\:/g, '":""');
              break;
        }
      return str;
    };
    X.http = function (opts) {
      var dataType = opts['dataType'].toLocaleLowerCase();
      var data = decodeURIComponent(typeof opts.data === 'undefined' ? '' : typeof opts.data === 'object' ? this.obj2str(opts.data) : opts.data);
      if (dataType === 'jsonp') {
        var fnName = 'jsonp_' + Math.floor(Math.random() * 10E10) + '_' + new Date().getTime();
        opts.url = opts.url + '?callback=' + fnName + '&' + data + '&' + 'r=' + new Date().getTime();
        var script = this.addScript(opts.url, null);
        (function(fn, sucFn, script) {
          window[fnName] = function (data) {
            sucFn && sucFn(data);
            doc.getElementsByTagName('head')[0].removeChild(script);
          };
        })(fnName, opts['sucFn'], script);
      } else {
        var req = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP');
        var type = typeof opts.type === 'undefined' ? 'get' : opts.type;
        if (type && type.toLocaleLowerCase() === 'post') {
          req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=UTF-8');
        } else if (data !== '') {
          opts.url = opts.url + '?' + data;
        }
        req.open(type, opts.url, true);
        req.send(data);
        req.onreadystatechange = function () {
          if (req.readyState === 4) {
            if (req.status === 200) {
              opts['sucFn'] && opts['sucFn'](dataType === 'json' ? X.parseJSON(req.responseText) : req.responseText);
            } else {
              opts['failFn'] && opts['failFn'](req.status);
            }
          }
        };
      }
    };
})(window);
(function(){
    function loadScript(url, callback){ 
        var script = document.createElement("script");
        script.setAttribute('async', '');
        script.type = "text/javascript"; 
        if (script.readyState){ //IE 
            script.onreadystatechange = function(){ 
                if (script.readyState == "loaded" || script.readyState == "complete"){ 
                    script.onreadystatechange = null; 
                    callback(); 
                } 
            }; 
        } else { //Others: Firefox, Safari, Chrome, and Opera 
            script.onload = function(){ 
                callback(); 
            }; 
        } 
        script.src = url; 
        var s = document.getElementsByTagName('body')[0];
        var first_script = document.getElementsByTagName('script')[0];
        first_script.parentNode.insertBefore(script, first_script);
        //s.appendChild(script);
    } 

  //loadScript("//click.dji.com/statistics/spider.js", function(){ } )
})();

(function(w) {
    w.SF = {
        staticUrl: "https://sf-static.b0.upaiyun.com/v-57b18bba"
    };
    w.SF.token = (function() {
        var _ixDH7m = //'FWq'
            'FWq' + /* 'a5M'//'a5M' */ '' + '' ///*'nw'*/'nw'
            + '2c4' //'AX'
            + //'Ss'
            '733' + '3b' //'0j'
            + //'Ql'
            'Ql' + //'xJZ'
            'xJZ' + //'Rdv'
            'Rdv' + '4e' //'ECr'
            + /* 'oq'//'oq' */ '' + 'd2' //'GS'
            + '18' //'Fm'
            + //'BAQ'
            '5c' + //'wFW'
            'fa8' + '15c' //'p'
            + //'vA'
            '37' + //'uS9'
            '425' + 'jz' //'jz'
            + '8f9' //'Yw'
            + //'VzK'
            '6' + //'Yjt'
            'Yjt' + //'B'
            'B' + /* 'b6'//'b6' */ '' + //'rCF'
            'e',
            _Xbl = [
                [0, 3],
                [8, 10],
                [8, 11],
                [8, 11],
                [27, 29],
                [31, 34],
                [31, 32]
            ];

        for (var i = 0; i < _Xbl.length; i++) {
            _ixDH7m = _ixDH7m.substring(0, _Xbl[i][0]) + _ixDH7m.substring(_Xbl[i][1]);
        }

        return _ixDH7m;
    })();;
})(window);

(function(){
    function getUrlParam(a) {
        var b = new RegExp("(^|&)" + a + "=([^&]*)(&|$)", "i"),
        c = window.location.search.substr(1).match(b);
        return null != c ? unescape(c[2]) : null
    }
    function zxppFunc() {
        function a(a) { !
            function() {
                function q() {
                    m = $(a).width(),
                    n = $(a).height(),
                    k.width = m,
                    k.height = n,
                    l.fillStyle = "#FFF",
                    p = !0,
                    requestAnimFrame(u)
                }
                function u() {
                    if (l.clearRect(0, 0, m, n), p) {
                        for (o = 0; i > o; o++) t.vy += .01,
                        t = s[o],
                        t.y += t.vy,
                        t.x += t.vx,
                        l.save(),
                        l.translate(t.x + t.width / 2, t.y + t.height / 2),
                        l.rotate(t.rotate += 5 * (Math.PI / 180) * Math.random()),
                        l.translate( - t.x - t.width / 2, -t.y - t.height / 2),
                        l.drawImage(t.img, t.x, t.y, t.width, t.height),
                        l.restore(),
                        t.y > n && t.reset();
                        requestAnimFrame(u)
                    }
                }
                var t, s, b = document.getElementById("img1"),
                c = document.getElementById("img2"),
                d = document.getElementById("img3"),
                e = document.getElementById("img4"),
                f = document.getElementById("img5"),
                g = document.getElementById("img6"),
                h = [[b, 20, 15], [c, 17, 20], [d, 20, 16], [e, 19, 19], [f, 13, 13], [g, 17, 30]],
                i = 20,
                j = document.querySelector(a),
                k = document.createElement("canvas"),
                l = k.getContext("2d"),
                m = $(a).width(),
                n = $(a).height(),
                o = 0,
                p = !0,
                r = function() {
                    this.x = 0,
                    this.y = Math.random() * -n,
                    this.vy = 0,
                    this.vx = 0,
                    this.r = 0,
                    this.reset()
                };
                for (r.prototype.reset = function() {
                    this.x = Math.random() * m,
                    this.y = Math.random() * -n,
                    this.vy = 1 + 1 * Math.random(),
                    this.vx = .5 - Math.random(),
                    this.r = 1 + 2 * Math.random(),
                    this.o = .5 + .5 * Math.random();
                    var a = parseInt(100 * Math.random());
                    a = 5 >= a ? 1 : a > 5 && 30 >= a ? 0 : a > 20 && 50 >= a ? 2 : a > 50 && 80 >= a ? 3 : a > 80 && 90 >= a ? 4 : a > 90 && 95 >= a ? 5 : 0,
                    this.scale = 1,
                    this.width = h[a][1] * this.scale,
                    this.height = h[a][2] * this.scale,
                    this.img = h[a][0],
                    this.rotate = 0
                },
                k.style.position = "absolute", k.style.left = k.style.top = "0", s = [], o = 0; i > o; o++) t = new r,
                s.push(t);
                window.requestAnimFrame = function() {
                    return window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame ||
                    function(a) {
                        window.setTimeout(a, 1e3 / 60)
                    }
                } (),
                q(),
                j.appendChild(k)
            } ()
        }
        setTimeout(function() {
            a("#page1")
        },
        1e3)
    }

    
})();
    