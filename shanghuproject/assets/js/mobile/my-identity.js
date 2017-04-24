$(function(){
    // tab转换
    $('.m-my-identity .tab-title').hashTab();

    // 登录表单验证成功
    $(".member-login .ajax-form").on("success",function(){
        location.href = 'my.html' ;
    });

    // 入会申请表单验证成功后
    $(".request-membership .ajax-form").on("success",function(){
        location.href = "certification.html";
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
            server: "../api/mobile/uploade.html",
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
            dm.getJSON("../api/mobile/updataphoto.html",{basepath:res.path}).done(function(res){
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

})