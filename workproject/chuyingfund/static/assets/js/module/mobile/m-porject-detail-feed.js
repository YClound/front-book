define(function(require, exports, module) {
    require('/static/assets/js/module/mobile/m-project-common.js')
    require('dm.photoswipe');
    
    $(function () {
        $('.infinite-loading').infiniteLoading({
            parseData:function(list){
                var ret = [];

                for (var i = list.length - 1; i >= 0; i--) {
                    var item = window.parsePhotoData(list[i]);
                }
                
            }
        });
        
    });



    
      
});