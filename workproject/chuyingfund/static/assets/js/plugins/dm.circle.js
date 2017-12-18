if (jQuery == null) {
    throw new Error('dm.circle.js requires jQuery')
}
/*if (Raphael == null) {
    throw new Error('dm.circle.js requires Raphael')
}
if (CountUp == null) {
    throw new Error('dm.circle.js requires countUp.js')
}
*/
(function ($) {
    $.fn.circle = function (options) {
        var defaultOptions = {
            color: null
        }, o = $.extend({}, defaultOptions, options)

        return this.each(function () {
            var elm = this,
                $elm = $(this),
                $var = $elm.find('var'),
                percent = parseInt($var.text(), 10) || 0,
                w = $(elm).width(),
                half = w / 2,
                strokeWidth = $elm.data('strokeWidth') || 10,
                param = {"stroke-width": strokeWidth},
                R = (w - strokeWidth) / 2 - 5,
                duration = 2000,
                perTime = duration / 100

            duration = percent < perTime ? duration : perTime * percent
            if (!o.color) {
                o.color = $elm.css('color')
            }
            param.stroke = o.color
            var pager = Raphael(elm, w, w)


            pager.circle(half, half, R).attr({stroke: '#eee', "stroke-width": strokeWidth})


            if (percent == 0) return
            pager.customAttributes.arc = function (value, total) {
                var alpha = 360 / total * value,
                    a = (90 - alpha) * Math.PI / 180,
                    x = half + R * Math.cos(a),
                    y = half - R * Math.sin(a),
                    path;
                if (total <= value) {
                    path = [["M", half, half - R], ["A", R, R, 0, 1, 1, half - 0.01, half - R]];
                } else {
                    path = [["M", half, half - R], ["A", R, R, 0, +(alpha > 180), 1, x, y]];
                }
                return {path: path};
            };

            var arc = pager.path().attr(param).attr({arc: [0, 100, R]})
            var countUp = new CountUp($var[0], 0, percent, 0, (duration + 200) / 1000)
            $elm.on('circle', function () {
                arc.animate({arc: [percent, 100, R]}, duration, ">")
                countUp.start();
            })
        })
    }
})(window.jQuery)
