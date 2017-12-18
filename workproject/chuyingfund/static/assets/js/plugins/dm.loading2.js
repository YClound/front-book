
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
        console.log(this)
    })
    return this
}
