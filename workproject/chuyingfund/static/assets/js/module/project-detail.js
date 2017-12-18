define(function(require, exports, module) {
    require('template');
    require('dm.ajaxpost');
    require('dm.modal');
    require('dm.tab');
    require('dm.scroll');
    require('JCShare');
    require('dm.validate');
    require('dm.photoswipe');
   

    if (!Fundable) { var Fundable = {}; }

    Fundable.Company = {};
    //页面滚动样式
    Fundable.Company.Page = {
        init: function () {
            Fundable.Company.Page.render();
            Fundable.Company.Page.initProfileSectionNav();
        },
        render:function(){
            var id = 0;
            var arr = [];
            $('#C4 .section').removeClass('section');
            $('#C4 h1').each(function(){
                var h1 = $(this);
                var _id = (id++);
                h1.attr('data-id',_id);
                h1.addClass('section');
                arr.push({id:_id,text:h1.text()});

            });
            var tpl = '<li data-section-id="{id}" class="clearfix">'+
                          '<div class="arrow-container">'+
                            '<div class="arrow"></div>'+
                          '</div>'+
                          '<a href="javascript:;">{text}</a>'+
                        '</li>';
            var buffer = [];
            var html = ''
            for(var i = 0;i<arr.length;i++){
                console.log(arr[i])
                html = tpl.replace('{id}',arr[i]['id']);
                html = html.replace('{text}',arr[i]['text']);
                buffer.push(html);
            }

            $('#ps-nav').html(buffer.join(''));

        },
        initProfileSectionNav: function () {

            //点击烈表示执行的函数
            $('ul.ps-nav li a').on('click', function (e) { 
                e.preventDefault();
                Fundable.Company.Page.scrollToSection($(this).closest('li').attr('data-section-id')); 
                return false; 
            });
            //设置列表的第一行的样式
            $('ul.ps-nav').each(function() { 
                $(this).find('li:first').addClass('active'); 
            });
            //scroll() 方法触发 scroll 事件，或规定当发生 scroll 事件时运行的函数
            $(document).scroll(function () {
                if ($('div.ps:visible').length == 0 ) {
                    return;
                }
                // 设置或返回当前页面相对于窗口显示区左上角的 Y 位置。
                var scrollTop = (document.all ? document.scrollTop : window.pageYOffset);
                //获得位移高度
                var offset = $('div.ps:visible').offset().top;
                //console.log(offset)
                //页面滚动时设置右侧页面滚定在页面上
                if (scrollTop > offset+50 ) {
                    $('ul.ps-nav').css({
                        position: 'fixed',
                        top: 0
                    });
                }
                else {
                    $('ul.ps-nav').css({
                        position: 'static',
                        top: 0
                    });
                }
                //去除列表中列的active样式
                $('ul.ps-nav:visible li').removeClass('active');
                var id = Fundable.Company.Page.getActiveSectionId();
                $('ul.ps-nav:visible li[data-section-id="' + id + '"]').addClass('active');
            });
        },
        //获取当前滚动页面对应的data-id所对应的data-selection-id的值
        getActiveSectionId:function(){
            var sectionOffsets = [];
            $('div.ps:visible .section').each(function (i, e) {
                sectionOffsets[i] = { id: $(e).attr('data-id'), top: $(e).offset().top };
            });
            var lastId = 0;
            $('ul.ps-nav:visible li').each(function (i, e) {
                if ($(e).offset().top > sectionOffsets[i].top) {
                    lastId = $(e).attr('data-section-id');
                }
            });
            //console.log(lastId)
            return lastId;
        },
        scrollToSection:function(id) {
            //获取section距离页面顶部的距离
            var offset = $('.section[data-id="' + id + '"]').offset().top;
            console.log('offset',offset)
            //设置页面距离顶部的距离
            $('html,body').animate({
                scrollTop: offset
            }, 400);
            //设置点击a元素之后列表字体的变化以及指示图标的变化
            setTimeout(function () { $('ul.ps-nav:visible li').removeClass('active');$('ul.ps-nav:visible li[data-section-id="' + id + '"]').addClass('active'); }, 300);

        }
    };



    $(function () {
        var $colMain = $('.panel-tab'),
            _top = $colMain.offset().top;
        

        
        $('.panel-tab .tab-title').tab();
        
        Fundable.Company.Page.init();

        // 点赞功能
        $('.zan-form').on('success',function(e,res){
            var data = res.data || 1;
            
            var count = $('#zanCount').text() || 0;

            count = parseInt(count);
            // 假如返回1 则表示点赞
            if(data==1){
                $('#btnZan').addClass('active');
                $('#zanCount').text(count+1);
                $(this).find('[data-name="action"]').val('-1');
            }else {
                $('#btnZan').removeClass('active');
                $('#zanCount').text(count-1);
                $(this).find('[data-name="action"]').val('1')
            }
        });

        //申请查看融资信息和商业计划
        $(document).on('click','[data-role="apply"]',function(){
            // 假如未登录
            if(serverData.level == 0){
                location.href = api.ucLogin;
                return
            }
            // 假如是实名用户
            if(serverData.level == 1 || serverData.level == 2){
                dm.confirm('需要投资人认证才可以申请',{yes:'立即认证',no:'取消'},function(){
                    location.href = api.investorAuth;
                })
                return
            }
            $('#applyModal').modal('show');
        });
        // 留言申请
        $('.applyCheck2').on('success',function(e,res){
        
            var data = res.data || 1;
            if(res.info == "未登录")
            {
                location.href = res.data.loginAddr;
                return false;
            }
            if(res.info =="未实名认证")
            {
                location.href = res.data.certificationAddr;
                return false;
            }
            if(res.info == "未认证投资人"){
                location.href = res.data.auditInvestorAddr;
                return false;
            }
            if(res.info == "申请已发送"){
                location.reload();
            }
        });


    });

    // 分享
    $(function () {
        $(document).share({
            title: document.title+'',
            content: "雏鹰网的项目",
            url: document.URL
        });
        
    });

    //查看更多
    $(function () {
        // 默认加载第二页
        $('.more-loading').moreLoading(
            {
                parseData:function(list){
                    
                    var ret = [];

                    for (var i = list.length - 1; i >= 0; i--) {
                        var item = window.parsePhotoData(list[i]);
                    }
                    
                }
            });
    });


});