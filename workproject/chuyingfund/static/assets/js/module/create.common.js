define(function(require, exports, module) {
    require('store');


    //form persist
    /*$(function () {
        return $('form').each(function (index, form) {
            var $form, data, storeData;
            $form = $(form);
            storeData = store(location.pathname + location.search + '-' + index);
            if (storeData) {
                for (data in storeData) {
                    $form.find('[name=user]').val();
                }
            }
            return $form.find('input, textarea, select').on('input.store, propertychange.store', function () {
                console.log('store',this)
                var etc;
                return etc = 0;
            });
        });
    });*/

    //表单提交成功后回调
    $(function () {
        $('[id^=form-step]').on('success.next', function (e, data) {
            console.log('success.next',data)
            if(data && data.data){
                location.href = data.data
            }
            
        }).on('success.save', function (e,data) {
            if(data && data.redirect){
                return dmAlert('保存成功',function(){
                    location.href = data.redirect
                });
            }else {
                return dmAlert('保存成功');
            }
            
        }).on('success.preview', function () {
            if(api.preview) {
                this.newWindow.location.href = api.preview
            }
            
        })
    });

    //保存、预览
    $(function () {
        var $buttons = $('.btn-save, .btn-preview').on('click', function (e) {
            e.preventDefault()
            var $btn = $(this),
                $form = $('form[id^=form-step]')

            $form.validate(null, function () {
                $buttons.each(function () {
                    $(this).disable(null, false)
                })
                $form.one('always.enableSaveBtn', function () {
                    $buttons.enable()
                })
                if ($btn.hasClass('btn-preview')) {
                    $form[0].newWindow = window.open()
                    $form.trigger('submit', {
                        success: 'preview'
                    }).one('fail', function () {
                        this.newWindow.close()
                    })
                } else {
                    $form.trigger('submit', {success: 'save'})
                }

            })
        })
        $('.btn-submit').click(function (e) {
            e.preventDefault()
            $(this).closest('form').trigger('submit', {success: 'next'})
        })
    });

    
    // 本页可能还有未保存的数据，您即将离开此页
    $(function () {
        var stopClose = function () {
            window.onbeforeunload = function () {
                return '本页可能还有未保存的数据，您即将离开此页'
            }
        }
        var enableClose = function () {
            window.onbeforeunload = null
        }
        $('.btn-save, .btn-submit').on('click', function () {
            enableClose()
        })
        $('input, textarea').on('input propertychange', stopClose)
        $('input, select').change(stopClose)
        $('.wu').click(stopClose)
    });


    
      
});