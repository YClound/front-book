define(function(require, exports, module){
    require('webuploader');
    require('header');       
    require('cropper');
    require('template');
    require('dm.validate');
    require('dm.wu');
    require('dm.util');
    require('dm.photoswipe');
    require('dm.ajaxpost');
    require('dm.modal');
    require('dm.up.avatar');
    require('dm.tab');
    require('dm.pager');
    $(function () {
        // 切换菜单
        /*$('.tab-title').ajaxTab();
        $('.tab-content>div').on('in',function(){
            window.proID = $(this).find('.pro-id').val() || '';
        });
        $('.tab-content>div').eq(0).trigger('in');*/

        team.init();
        content.init();
        
    });
    //项目内容
    var content = function () {
        return {
            init: function(){
                var $showContent,$packUpContent;
                $showContent = $(".project-content .show-content");
                $packUpContent = $(".project-content .packup-content");
                var textLength = $(".project-content .content-main").text().length;
                //console.log(textLength);
                /*if(textLength < 200){
                    $showContent.hide();
                }*/
                $showContent.on("click",function(){
                    $(".project-content .all-content").show();
                    $packUpContent.show();
                    $showContent.hide();
                    $(".project-content .content-main").hide();
                });
                 $packUpContent.on("click",function(){
                    $(".project-content .all-content").hide();
                    $packUpContent.hide();
                    $showContent.show();
                    $(".project-content .content-main").show();
                });

            }
        }
    }();


    //团队列表
    var team = function () {
            return {
                init: function () {
                    var $list, $items, $contentsWrap, $contents;
                   
                    
                        $list = $('.col-main .member-list');

                        $list.find('.member-item').on('active', function () {
                            var $this = $(this);
                            var $box = $(this).closest('.team-wrap');
                            $contentsWrap = $box.find('.member-content');
                            $items = $box.find('.member-item');
                            $contents = $contentsWrap.children();
                            $items.removeClass('active');
                            $this.addClass('active');
                            var index = $(this).index();
                            $contents.hide().eq(index).fadeIn();
                            $contentsWrap.css('min-height', $contentsWrap.height());
                        });
                        $list.on('click', '.member-item', function () {

                            $(this).trigger('active');
                        });
                    
                        
                    
                }        
            };
    }();

    /*发布项目动态*/
    $(function(){

        var $modalFeed = $('.modal-feed'),$feedBody = $('#feed-table-body'),uploader;

        $modalFeed.on('shown', function () {
            
            $modalFeed.find('.wu').wu({
                fileNumLimit:9
            }).removeClass('hidden');
            $modalFeed.find('form').validate('init').find('input[maxlength], textarea[maxlength]').not('[nocount]').wordCount();
            window.wu2 = true;
            uploader = $modalFeed.find('.wu').data('uploader');
            uploader.on('startUpload',function(){
                console.log('startUpload');
                window.lock = true;
            });
            uploader.on('uploadFinished',function(){
                console.log('uploadFinished');
                window.lock = false;
            });
            
            
        }).on('hidden', function () {
            $feedBody.empty();
            
        });

        $('.btn-publish').on('click',function(){
            $feedBody.html(template('feedPublish', {}));
        });
        
        $('.form-feed').on('beforeSend',function(){
            $(this).find('#proid').val($("#projectId").val());
            $(this).data('error','');
            if(window.lock){
                dm.alert('图片还在上传中，请稍等');
                $(this).data('error',true);
            }
        });
        $('.form-feed').on('success',function(e,res){
            var data = res.data || {};
            var _photo = [];
            if(data.photo) {
                _photo = data.photo.split(',');
            }
            for (var i = _photo.length - 1; i >= 0; i--) {
                var ori = _photo[i];
                var index1 = ori.lastIndexOf('/');
                if(index1>-1){
                    var prefix = ori.substring(0,index1);
                    var name = ori.substring(index1+1);
                    var thumbnail = prefix + '/' + 'thumbnail_' + name;
                }else {
                    var thumbnail = 'thumbnail_' + ori;
                }
                
                var index = ori.lastIndexOf('_');
                if(index>-1) {
                    var str = ori.substring(index+1);
                    var arr = str.split('.');
                    var size = arr[0];
                }else {
                    var size = '';
                }
                    
                _photo[i] = {
                    thumbnail:thumbnail,
                    ori:ori,
                    size:size
                };

            }
            data._photo = _photo;

            var html = template('feed-item', data);
            $('.feed-list').prepend(html);
            $modalFeed.modal('hide');
            console.log('success')
            //无项目动态时增加动态不刷新时,去除无项目动态时的背景      
            $('.project-report').attr("data-total-page",1);      
            //增加项目动态（模板）的删除刷新        
            $('.feed-list').find(".form-project-status").on("success",function(e,res){       
                location.reload();       
            });
            $('.form-project-status').on('success',function(e,res){  
                location.reload();                
            });

        });

            
            
    });

    /* 项目核心数据审核以及修改 */
    $(function(){

        $('#applyLimitForm').on('success',function(){
            dm.alert('修改成功');
        });
        // 权限修改的时候提交
        $('#applyLimitForm input').on('change',function(){
            $('#applyLimitForm').submit();
        });

        // 同意申请
        $(document).on('click','.btn-agree[data-code="0"]',function(){
            var data = $(this).data() || {};
            var $this = $(this);

            dm.confirm('确认同意吗？',function(){
                dm.getJSON(api.agreeProjectApply, data).done(function(){
                    dm.notice('修改成功');
                    $this.attr('data-code','1').html('<u class="c-9">已同意</u>');

                });
            })
                
        });
        // 不是投资人阻止跳转
        $(document).on('click','.project-apply-item a',function(e){
            //e.preventDefault();
            var type = $(this).closest('.project-apply-item').data('type');
            //console.log(1,type)
            if(type!=='合格投资人'){
                e.preventDefault();
            }
        });
        // 申请的分页
        $('#pagerApply').pager();

    });


});