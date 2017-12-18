define(function(require, exports, module) {
    require('template');
    require('dm.photoswipe');
    require('/static/assets/js/module/mobile/m-project-common.js')
    require('/static/assets/js/plugins/raphael-min.js');
    require('countup');
    require('/static/assets/js/plugins/dm.circle.js');
    
    $(function () {
        $('#proj-header').headroom();
        // expandable
        $('.expandable').each(function () {
            
            var $wrap = $(this),
                $win = $wrap.find('.win'),
                $content = $wrap.find('.content'),
                $btn = $wrap.find('.btn-more')
            var height = $content.height(),
                winHeight = $win.height()
            // height < winHeight
            if (false) {
                $btn.hide()
            } else {
                $btn.on('click', function () {
                    
                    if ($btn.hasClass('expand')) { // 如果是展开则收缩
                        $win.stop(true, false).animate({height: winHeight})
                        $btn.removeClass('expand')
                    } else { // 如果是收缩则展开
                        $win.stop(true, false).animate({height: height})
                        $btn.addClass('expand')
                    }
                })
            }
        });


        // 点击按钮时
        $('.view-news').on('show',function(){
            $('.btn-project-detail').css("color","#222");
            $('.btn-news').css("color","#1fbaf3");
            $('.btn-plan').css("color","#222");

        })
        $('.view-plan').on('show',function(){
            $('.btn-project-detail').css("color","#222");
            $('.btn-plan').css("color","#1fbaf3");
            $('.btn-news').css("color","#222");
        })
        $('.btn-project-detail').on('click',function(){
            $('.btn-project-detail').css("color","#1fbaf3");
            $('.btn-plan').css("color","#222");
            $('.btn-news').css("color","#222");
        })



        // 项目融资百分比
        $('[data-limit="1"]').find('.percent-circle').circle().trigger('circle');


        // 申请查看核心数据
        $('.applyCheckForm').on('beforeSend',function(){
            var url = $(this).attr('action');
            var $btn = $(this).find('.btn-apply');
            $(this).data('error','');

            if(!serverData.uid) {
                dm.confirm('您还未登录，请登录后重试，点击确定前往登录页面', function () {
                    location.href = API.ucLogin
                });
                $(this).data('error',true);
                return;
            }
            $btn.disable('处理中',false);
            // 验证身份 return {status:0,data:{code:'',url:'',need_apply:1} }
            /*
                code 
                0: 未登录
                1: 登录未实名认证
                2: 实名认证但未认证投资人
                3: 认证投资人
                4: 投资机构

                need_apply 0 不需要申请只要投资人即可查看 1 投资人也需要申请 -1 全部公开
            */
            dm.getJSON(url,{}).done(function(res){
                var data = res.data || {};
                var url = data.url;
                var code = data.code;
                var need_apply = data.need_apply;
                if( code == 0 ){
                    dm.confirm('您还未登录，请登录后重试，点击确定前往登录页面', function () {
                        location.href = url;
                    });
                }else if(code == 1){
                    dm.confirm('只有实名认证才可查看，点击确定前往认证', function () {
                        location.href = url;
                    });
                }else if(code == 2){
                    dm.confirm('只有认证投资人才可查看，点击确定前往认证', function () {
                        location.href = url;
                    });
                }else if(code == 3 && need_apply==1){
                    dm.router.go('/apply');
                    
                }

            }).always(function(){
                $btn.enable();
            });
        });

        // 发送申请信息
        $('#applyForm').on('success', function () {
            dm.notice('申请已发送');
            history.back();
            setTimeout(function () {
                if (location.hash.replace(/^#[!\/]*/, '')) {
                    dm.router.go('/')
                }
            }, 0)

            $('#applying').hide();
            $('#waiting').show();
        });

        // 收藏
        $('.btn-star').on('click', function () {
            var $btn = $(this),projectId=$(this).data('projectid'),collect=$("#collect").attr("data-collect");
            dm.getJSON(API.followproject,{projectId:projectId,collect:collect}).done(function (json) {
                if(json.data.url) {
                    dm.confirm('您还未登录，请登录后重试，点击确定前往登录页面',function(){
                        location.href = json.data.url;
                    })
                }else {
                    if ($btn.hasClass('status-on')) {
                        $btn.removeClass('status-on').addClass('status-off');
                        $("#collect").attr("data-collect",0);
                        dm.notice('取消收藏成功');
                    } else {
                        $btn.removeClass('status-off').addClass('status-on');
                        $("#collect").attr("data-collect",1);
                        dm.notice('收藏成功');
                    }
                }

                    
            })
        });

        // 查看商业计划书
        $('.btn-plan').on('click',function(e){
            e.preventDefault();
            var url = $(this).data('url');
            if(!serverData.uid) {
                dm.confirm('您还未登录，请登录后重试，点击确定前往登录页面', function () {
                    location.href = API.ucLogin
                });
                return;
            }
            
            dm.getJSON(url,{}).done(function(res){
                var data = res.data || {};
                var url = data.url;
                var code = data.code;
                var need_apply = data.need_apply;
                if(need_apply ==3) {
                    // 可以查看      
                    dm.router.go('/plan');
                }
                else if( code == 0 ){
                    dm.confirm('您还未登录，请登录后重试，点击确定前往登录页面', function () {
                        location.href = url;
                    });
                }else if(code == 1){
                    dm.confirm('只有实名认证才可查看，点击确定前往认证', function () {
                        location.href = url;
                    });
                }else if(code == 2){
                    dm.confirm('只有认证投资人才可查看，点击确定前往认证', function () {
                        location.href = url;
                    });
                }else if(code == 3 && need_apply==1){
                    dm.router.go('/apply');
                    
                }else if(code==3 && need_apply==2){
                    dm.notice('您的申请已提交，审核通过将通知您');
                }else {
                    // 可以查看
                    dm.router.go('/apply');
                }

            });
        });

        // 查看项目动态
        $('.btn-news').on('click',function(e){
                 $('.btn-project-detail').css("color","#1fbaf3");
            e.preventDefault();
            dm.router.go('/news');
        });

         // 查看项目详情
        $('.btn-project-detail').on('click',function(e){
            e.preventDefault();
            dm.router.go('/');
        });


        // 项目动态更多
        $('.more-page').morePage({
            parseData:function(list){
                var ret = [];

                for (var i = list.length - 1; i >= 0; i--) {
                    var item = window.parsePhotoData(list[i]);
                }
                
            }
        });
        dm.router(['/apply','/plan','/news'], function (path) {
            dm.view.show(path.replace(/^\//, ''));
        });

        $('.view').on('show',function(){
            console.log("1111");
            $('.m-main').hide();
        })


        $('.view').on('hidden',function(){
            console.log("222");
            $('.m-main').show();
        })
        
        
    });



    
      
});