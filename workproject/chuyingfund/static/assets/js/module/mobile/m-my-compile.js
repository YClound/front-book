define(function(require, exports, module) {
    require('webuploader')

    $(function () {
        dm.router(['/nickname', '/personal', '/company', '/intro','/telephone','/password','/area'], function (path) {
            console.log(22);
            dm.view.show(path.replace(/^\//, ''));
       });   
    });
    
    // 焦点
    $(function () {
        var field
        $('[data-router]').click(function () {
            field = $(this).data('field');
        })
        $('.view').on('shown', function () {
            var $this = $(this);
            var $fo = $this.find('[name=' + field + ']');
            if (!$fo.length) $fo = $(this).find('input:visible').eq(0);
            $fo.focus();
        })
    })

    // 表单赋值
    $('.var-field').each(function () {
        var $this = $(this)
        var name = $this.data('name')
        $('[name="' + name + '"]').val($.trim($this.text()))
    })

     $('form').on('success', function () {
        if(res.data==null||res.data=="")dm.alert('请登录邮箱完成修改');
        history.back();
        setTimeout(function () {
            if (location.hash.replace(/^#[!\/]*/, '')) {
                dm.router.go('/')
            }
        }, 0);

        $('.var-field').each(function () {
            var $this = $(this)
            var name = $this.data('name')
            $this.html($('[name="' + name + '"]').val());
            if(name == "password"){ $this.html("");}
        });

        $('.var-city').html($('.city-select .btn-city-text').val().replace(/\s*\/\s*/, ' '))
    });




     $('#password').on('beforeSend',function(){
        $(this).data('error','');
        var $newPW = $('input[name=new-password]').val();
        var $verifyPW = $('input[name=verify-password]').val();
        if($newPW != $verifyPW){
          $(this).data('error',true);
          $('.password-tip').css('display','block');
        }else{
          $('.password-tip').css('display','none');
        }
        
      });


     // 头像上传
     $(function(){
        var $pick = $('.wu-avatar .pick');
        console.log($pick)
        var uploader = new WebUploader.Uploader({
            pick: {
                id: $pick,
                multiple: false
            },
            chunked: false,
            compress: {
                width: 150,
                height: 150,
                // 图片质量，只有type为`image/jpeg`的时候才有效。
                quality: 90,
                // 是否允许放大，如果想要生成小图的时候不失真，此选项应该设置为false.
                allowMagnify: false,
                // 是否允许裁剪。
                crop: true,
                // 是否保留头部meta信息。
                preserveHeaders: true,
                // 如果发现压缩后文件大小比原来还大，则使用原来图片
                // 此属性可能会影响图片自动纠正功能
                noCompressIfLarger: false,
                // 单位字节，如果图片大小小于此值，不会采用压缩。
                compressSize: 0
            },
            server: api.upload,
            fileNumLimit: 1,
            onError: function () {
                var args
                args = [].slice.call(arguments, 0)
                alert(args.join('\n'))
            },
            auto: true
        })
        uploader.on('uploadComplete',function(){
            dm.loading('hide');
        });
        uploader.on('uploadProgress', function (file, percentage) {
            dm.loading('上传中:'+ percentage * 100 + '%')
        });
        uploader.on('uploadSuccess', function (_file, res) {
            dm.getJSON("/account/info/updataphoto.html",{basepath:res.path}).done(function(res){
                if(res.status ==0){
                    dm.notice('修改成功',function(){
                        location.reload();
                    })
                }
            });
                    
        });
        // 测试失败
        uploader.on('uploadError', function (_file, res) {
            dm.alert('上传失败')
        });
     });


});