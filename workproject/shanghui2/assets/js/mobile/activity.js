$(function(){
    var $swipeWrap = $('.swipe-wrap');
    var number = $(".swipe-tab-title").find('li').length;
    var router = [];
    for(var i= 1; i <= number;i++){
        router.push('/'+ i);
    }
    $swipeWrap.children().css('height', (window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight) - $swipeWrap.offset().top - $('.padding-footer-index').outerHeight());
    var $titles = $('.swipe-tab-title .item');
    $('.swipe-tab').swipeTab(function (index, elm) {
        dm.router.go('/' + index);
        $(elm).find('.infinite-loading').show().infiniteLoading();
    })
    dm.router(router, function (path) {
        path = path.replace(/^\//, '') || '0';
        $titles.filter('[data-index=' + path + ']').click()
    })
})