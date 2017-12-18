
$.fn.loading = function (options) {
    var defaultOption = {
        empty: true,
        position: 'center'
    }
    var o = $.extend({}, defaultOption, options)
    if (o.empty) this.empty()
    this.css('position', 'relative')
    var height = this.height()
    if (height < 80) {
        this.css('minHeight', 80)
        height = 80
    }
    this.each(function () {
        var r0 = 5,
            r1 = 10,
            t = 400
        var paper = Raphael(this, 90, 40),
            redC = paper.circle(25, 20, r0),
            yellowC = paper.circle(45, 20, r0),
            greenC = paper.circle(65, 20, r0)
        var css = {
            position: 'absolute',
            left: '50%',
            marginLeft: '-45',
            zIndex: 2
        }
        if (o.position === 'center') css.top = height / 2 - 20
        else if (o.position === 'top') css.top = '20px'
        else if (o.position === 'bottom') {
            css.top = 'auto'
            css.bottom = '20px'
        }
        $(paper.canvas).css(css)
        redC.attr({fill: "#ED7258", stroke: "none"})
        yellowC.attr({fill: "#F8B757", stroke: "none"})
        greenC.attr({fill: "#44BCBC", stroke: "none"})

        var cycle = function () {
            var b = Raphael.animation({r: r0}, t)
            var a = Raphael.animation({r: r1}, t, function () {
                this.animate(b)
            })
            redC.animate(a)
            yellowC.animate(a.delay(t * 0.8))
            greenC.animate(a.delay(t * 1.6))
        }
        cycle()
        setInterval(cycle, t * 4)
    })
    return this
}
