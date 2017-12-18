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