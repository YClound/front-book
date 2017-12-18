define(function(require, exports, module) {
    require('header');
    require('webuploader');
    require('cropper');
    require('template');
    require('dm.ajaxpost');
    require('dm.modal');
    require('dm.scroll');
    require('dm.up.avatar');
    require('dm.photoswipe');
   
    $(function () {
        
        // 点击加载更多动态
        $('.more-loading').moreLoading();


    });

    $(function () {
        var $modal = $('.modal-change-avatar'),
            $form = $modal.find('form'),
            $img = $('.s-profile img')
        $modal.on('shown', function () {
            $form.upAvatar()
        })
        $form.on('success', function (e, json) {
            if(json && json.data && json.data.url) {
                $img.attr('src', json.data.url)
            }
            
        })
        $form.on('fail', function (e, json) {
            $img.attr('src', 'http://7xjckn.com2.z0.glb.qiniucdn.com/2016-04-26_571f2636c8d65.png')
        })

    });
});