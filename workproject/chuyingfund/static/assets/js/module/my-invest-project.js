define(function(require, exports, module){
    require('dm.ajaxpost');
    require('dm.modal');
    require('dm.tab');
    require('/static/assets/js/plugins/raphael-min.js');
    require('countup');
    require('dm.scroll');
    require('/static/assets/js/plugins/dm.circle.js');

     $(function () {
        // 切换菜单
        $('.tab-title').ajaxTab();

        //圆形圈的设计
        $('.percent-circle').circle().trigger('circle');
        //移到收藏夹确认
        $('.move-favorite-conform').on('click',function(){
            dm.confirm('是否确定将该项目要移到收藏夹中',{},function(){
                window.location.reload();},function(){
                    window.location.reload();
                 });
        });


    });


});