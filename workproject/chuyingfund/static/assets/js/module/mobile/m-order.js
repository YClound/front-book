define(function(require, exports, module){
    require('template');
    // 假如返回跳转地址则跳转
    $(function(){
        var $titles = $('.s-tab-title  a');
        var $tabWrap = $('.s-tab-title');

        // 点击收藏
        $(document).on('click','.btn-collect',function(e){
            var $btn = $(this);
            var url = $(this).data('url'),id = $(this).data('id');


            dm.getJSON(url,{id:id}).done(function(){
                dm.notice('收藏成功');
                //$btn.text('已收藏').prop('disabled',true);
                $btn.closest('.project').remove();

            });
        });
        // 数据加载
        $('.infinite-loading').on('data', function (e, json) {
            // 百分比
            for (var i = 0, len = json.list.length; i < len; i++) {
                var p = json.list[i], percent;
                if (p.stage == 1) {percent = 0}
                else {percent = p.total_fund / p.need_fund * 100;}
                json.list[i].percent = percent || 0;
                p.project_news = p.project_news || '';
                p.project_news = dm.trim(p.project_news,15);
            }
            json.urlDetail = "/project/project-detail.html?projectId=0000";
            var html = template('order-list', json);
            var $list = $(html).appendTo($(this).siblings('ul'));
            /*$list.find('.train').inview(function () {
                var $this = $(this),percent = +$this.data('percent');
                percent = percent > 100 ? 100 : percent;
                $this.css('width', (percent) + '%')
            });*/
            //$(this).siblings('ul').append(template('order-list', json))
        });

        

        
        
        $titles.on('show',function(e,index){
            
            var xx = $(e.currentTarget).attr('data-index');
            
            //dm.router.go('/' + xx);
            $('.tab-content>div').eq(index).find('.infinite-loading').show().infiniteLoading();
        });

        $titles.eq(0).addClass('active');
        $tabWrap.tab().init();

        /*dm.router(['/all', '/wait', '/success'], function (path) {
            path = path.replace(/^\//, '') || 'all';
            
            $titles.filter('[data-index=' + path + ']').trigger('click')
        })*/
        
        
        
        //$('.infinite-loading').show().infiniteLoading();

    });
});