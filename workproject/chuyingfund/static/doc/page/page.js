define(function(require, exports, module) {
    require('template');
    require('dm.ajaxpost');
    require('dm.modal');
    require('dm.scroll');
    


    $(function () {
        //直接加载第一页数据 $('.more-loading').moreLoading(true)

        // 默认加载第二页
        $('.more-loading').moreLoading()
    });
});