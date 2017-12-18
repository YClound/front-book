define(function(require, exports, module) {
    require('m_common');

    $(function(){
        $('.proj-item form').on('success',function(){
            dm.notice('取消成功');
            var $item = $(this).closest('.proj-item')
            $item.fadeOut(500,function(){
                $item.remove();
            })
        })
    });


    
      
});