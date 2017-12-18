define(function(require, exports, module){
    require('dm.ajaxpost');
    require('dm.modal');
    require('/static/assets/js/plugins/raphael-min.js');
    require('countup');
    require('dm.scroll');
    require('/static/assets/js/plugins/dm.circle.js');
     $(function () {

        //圆形圈的设计
        $('.percent-circle').circle().trigger('circle');
    });
});