define(function(require, exports, module) {
    require('header');
    require('webuploader');
    require('cropper');
    require('template');
    require('dm.ajaxpost');
    require('dm.modal');
    require('dm.up.avatar');
    require('dm.validate');
    require('dm.tab');
    require('dm.city.select');
    require('dm.wu');

   
    
    // 修改个人资料
    $(function () {
        $('#form-profile').on('success', function () {
            $(this).closest('.modal').modal('hide')
            dmAlert('修改成功', function () {
                location.reload()
            })
        })
        $('#form-realname').on('success', function (e, res) {
            dmAlert('实名认证成功', function () {
                if (res.redirect_url != '') {
                    window.location.href = res.redirect_url
                } else {
                    location.reload()
                }
            })

        })
    });

    // 头像修改
    $(function () {
        // 切换菜单
        $('.tab-title').tab();


        var $modal = $('.modal-change-avatar'),
            $form = $modal.find('form'),
            $img = $('.s-profile img');
        $modal.on('shown', function () {
            $form.upAvatar()
        });
        $form.on('success', function (e, json) {
            if(json && json.data && json.data.url) {
                $img.attr('src', json.data.url)
            }
            
        });
        $form.on('fail', function (e, json) {
            $img.attr('src', 'http://7xjckn.com2.z0.glb.qiniucdn.com/2016-04-26_571f2636c8d65.png')
        });



    });

    // 邮箱绑定
    $(function () {
        
        $('.form-email').on('success', function (e,res) {
            var res = res || {};
            var info = res.info || '提交成功';
            $('.modal-email').modal('hide')
            dm.notice(info)
            setTimeout(function () {
                location.reload()
            }, 0)
        })
    });

    // 微信解绑
    $(function () {
        wechatHelper.init()
        $('.form-unbind-weixin').on('success', function () {
            $('.modal-unbind-weixin').modal('hide')
            dm.notice('解绑成功')
            setTimeout(function () {
                location.reload()
            }, 0)
        })
    });

    // 修改手机号
    $(function () {
        //图片验证码
        imgVerifyHelper.init();
        //短信验证码
        smsHelper.init();

        var $formVerifyNewMobile = $('.form-verify-new-mobile').on('success', function () {
            $formVerifyNewMobile.hide()
            $('.form-change-mobile').show().find('[data-name=newphone]').val($('#mobile').val())
        })
        $('.form-change-mobile').on('success', function () {
            dm.alert('修改成功', function () {
                location.reload()
            })
        })
        $('.modal-change-mobile, .modal-password-edit').on('hidden', function () {
            var $form = $(this).find('form')
            if ($form.length) {
                $form[0].reset()
            }
        })
        $('.mobile-unavailable').on('click', function () {
            $('.btn-change-mobile').prop('disabled', true)
        })
        $('.mobile-available').on('click', function () {
            $('.btn-change-mobile').prop('disabled', false)
        })
    });

    // 绑定手机号
    $(function () {

        $('.form-binding-mobile').on('success', function () {
            dm.alert('绑定成功', function () {
                location.reload()
            })
        })
        
    });


    /* 投资信息*/
    $(function(){
        // 表单提交成功
        $('.otherForm').on('success',function(e,res){
            var id = $(this).data('table');
            var $table = $(id);
            var data = res.data || {};
            var clone = $table.find('.tpl').eq(0).clone().removeClass('tpl');

            for(var key in data){
                clone.find('[data-role="'+key+'"]').text(data[key]);
            }
            clone.find('.action a').attr('data-uuid', data.id);

            $table.find('tbody').append(clone);

            $(this).closest('.modal').modal('hide').find('input,textarea').val('');
        });
        $(document).on('click','.table .action a',function(e){
            e.preventDefault();
            var id = $(this).data('uuid');
            var tr = $(this).closest('tr');
            var url = $(this).attr('href');

            dm.getJSON(url,{id:id}).done(function(){
                dm.notice('删除成功');
                tr.remove();
            });
        });

        // 投资

        var $modal = $('.modal-invest-edit');
        var html = $modal.find('.modal-body').html();
        var $body = $modal.find('.modal-body');

        $(document).on('click','.btn-invest-add',function(e){
            $body.html(html);
        });
        $('.investAddForm').on('success',function(e,res){
            var id = $(this).data('table');
            var $table = $(id);
            var data = res.data || {};
            var clone = $table.find('.tpl').eq(0).clone().removeClass('tpl');

            clone.find('[data-role="logo"]').html('<img src="'+data.logo+'">');
            clone.find('[data-role="detail"]').text(data.detail);
            clone.find('.action a').attr('data-uuid', data.id);

            $table.find('tbody').append(clone);
            $(this).closest('.modal').modal('hide');
        });
        $modal.on('shown', function () {
            $modal.find('.wu').wu().removeClass('hidden');
            //$modal.find('form').validate('init').find('input[maxlength], textarea[maxlength]').not('[nocount]').wordCount()
        }).on('hidden', function () {
            $body.empty()
        });
    });



});