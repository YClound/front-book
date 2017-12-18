if (!window.template) {
    console.warn('require template')
    //throw new Error('require template')
}
if (!window.WebUploader) {
    console.warn('require WebUploader')
    //throw new Error('require template')
}
/*if (!window.WebUploader.Uploader.support()) {
    console.warn('WebUploader does not support your browser.');
    //throw new Error('WebUploader does not support your browser.')
}*/
/*
 * $('form').upAvatar()
 *
 * */
!function ($) {
    var MAX_WIDTH = 410
        , isSupportBase64 = true

    !function () {
        var img = new Image
        img.onload = img.onerror = function () {
            if (this.width !== 1 || this.height !== 1) {
                isSupportBase64 = false
            }
        }
        img.src = 'data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw=='
    }()

    $.fn.upAvatar = function (options) {
        return this.each(function () {
            var $form = $(this),
                $wu = $form.find('.up-avatar'),
                $pickWrap = $wu.find('.pick-wrap'),
                $cropWrap = $wu.find('.crop-wrap'),
                $image = $wu.find('.img-wrap img'),
                $preview = $wu.find('.preview-box').children(),
                $pick = $wu.find('.pick'),
                $basepath = $form.find('[name=basepath]'),
                $w = $form.find('[name=w]'),
                $h = $form.find('[name=h]'),
                $x = $form.find('[name=x]'),
                $y = $form.find('[name=y]'),
                $confirmBtn = $form.find(':submit'),
                oriPickHTML = $pickWrap.html(),
                file = null

            if(!$basepath.length){
                $basepath = $form.find('[data-name=basepath]')
            }
            var uploader = new WebUploader.Uploader({
                pick: {
                    id: $pick,
                    multiple: false
                },
                chunked: false,
                compress: false,
                server: api.upload,
                swf: '/static/assets/vendor/webuploader/webuploader.swf',
                fileNumLimit: 1,
                onError: function () {
                    var args
                    args = [].slice.call(arguments, 0)
                    alert(args.join('\n'))
                },
                auto: true
            })
            var destroyPick = function () {
                uploader.destroy()
                $pickWrap.html(oriPickHTML)
            }
            var reset = function () {
                destroyPick()
                $image.cropper('destroy')
                $image.attr('src', '')
                $pickWrap.removeClass('webuploader-element-invisible')
                $confirmBtn.disable(null, false)
            }
            var $modal = $form.closest('.modal').off('hide.reset').on('hide.reset', reset)
                .off('show.reset').on('show.reset', reset)
            uploader.on('uploadSuccess', function (_file, res) {
                $image.attr('src', res.data.url).cropper({
                    aspectRatio: 1,
                    preview: $preview,
                    crop: function (e) {
                        $w.val(e.width)
                        $h.val(e.height)
                        $x.val(e.x)
                        $y.val(e.y)
                    }
                })
                $pickWrap.addClass('webuploader-element-invisible')
                $basepath.val(res.data.url)
                $confirmBtn.enable()
            });
            // 测试失败
            uploader.on('uploadError', function (_file, res) {
                console.log('uploadError',_file, res);
                alert('上传失败')
                return;
                $image.attr('src', '/static/assets/images/star-bg.jpg').cropper({
                    aspectRatio: 1,
                    preview: $preview,
                    crop: function (e) {
                        $w.val(e.width)
                        $h.val(e.height)
                        $x.val(e.x)
                        $y.val(e.y)
                    }
                })
                $pickWrap.addClass('webuploader-element-invisible')
                $basepath.val('')
                $confirmBtn.enable()
            });

            $form.off('success.closeModal').on('success.closeModal', function (e, json) {
                $modal.modal('hide')
                dmAlert('修改成功')
            })
        })
    }
}(jQuery)
