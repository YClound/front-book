if (!window.template) {
    console.warn('dm.wu.js require template.js')
    //throw new Error('dm.wu.js require template.js')
}

$.fn.wu = function (options, files) {
    var $wu = this.first(),
        $form = $wu.closest('form'),
        $pick = $wu.find('.pick'),
        multiple = $pick.data('multiple'),
        mode = $pick.data('mode') || '';
    multiple = (multiple != null) && multiple ? true : false;

    // 额外参数
    if($pick.data('param')){
        var formData = $pick.data('param');
    }else {
        var formData = {};
    }
    //console.log(formData)
    //formData = JSON.parse(formData);
    // 同一表单多个上传组件
    var $multiWU = $form.find('.wu')
    if ($multiWU.length > 1) {
        $multiWU.each(function (i) {
            $(this).data('index', i)
        })
    }

    //  文件类型 data-accept="pdf"  $pick.data('accept') 只支持pdf or image
    var acceptInfo = {
            pdf: {
                title: 'PDF',
                extensions: 'pdf',
                mimeTypes: 'application/pdf'
            },
            image: {
                title: 'Images',
                extensions: 'gif,jpg,jpeg,bmp,png',
                mimeTypes: 'image/*'
            }
        },
        accept = $pick.data('accept') || 'image'
    accept = accept.split(',')
    var _title = [],
        _extensions = [],
        _mimeTypes = []
    for (var i = 0, len = accept.length; i < len; i++) {
        var _a = accept[i],
            _info = acceptInfo[_a]
        if (_info) {
            _title.push(_info.title)
            _extensions.push(_info.extensions)
            _mimeTypes.push(_info.mimeTypes)
        } else {
            console.warn('未知的文件类型: ' + _a)
        }
    }

    // 选项
    var defaultOptions = {
            swf: '/static/assets/vendor/webuploader/webuploader.swf',
            server: api.upload,
            formData:formData,
            pick: {
                id: $pick,
                multiple: multiple
            },
            resize: false,
            accept: {
                title: _title.join(','),
                extensions: _extensions.join(','),
                mimeTypes: _mimeTypes.join(',')
            },
            thumb: {
                width: 150,
                height: 150
            },
            auto: true,
            // 去重， 根据文件名字、文件大小和最后修改时间来生成hash Key
            duplicate: false,
            compress:{
                width: 1600,
                height: 1600,
                // 图片质量，只有type为`image/jpeg`的时候才有效。
                quality: 90, 
                // 是否允许裁剪。
                crop: false,
                // 是否保留头部meta信息。
                preserveHeaders: true,
                // 如果发现压缩后文件大小比原来还大，则使用原来图片
                // 此属性可能会影响图片自动纠正功能
                noCompressIfLarger: false,
                // 单位字节，如果图片大小小于此值，不会采用压缩。
                compressSize: 0
            }
        },
        compress = {
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
        o = $.extend({}, defaultOptions, options),
        o = mode =='avatar'?$.extend({}, o,{compress:compress}):o,
        ratio = window.devicePixelRatio || 1,
        thumbnailWidth = o.thumb.width * ratio,
        thumbnailHeight = o.thumb.height * ratio,
        state = 'pending',
        percentages = {},
        file

    if (!WebUploader.Uploader.support('flash') && WebUploader.browser.ie) {
        $wu.html('<a href="http://www.adobe.com/go/getflashplayer" target="_blank" border="0"><img alt="get flash player" src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" /></a>')
    }
    //console.log('option',o,thumbnailWidth,ratio);
    var fileIndex = 0
    var addFile = function (file, isUrl) {
        var id = multiple ? fileIndex++ : ($wu.data('index') || 0)
        if (isUrl) file = $.extend({
            id: id
        }, file)
        var templateName = 'file-item',
            isPDF = !!~['pdf'].indexOf(file.ext)
        if (isPDF) templateName += '-pdf'

        var $add = $wu.find('.file-list .add');
        if( $add.length ){
            var $fileItem = $(template(templateName, file)).insertBefore($add)
        }else {
            var $fileItem = $(template(templateName, file)).appendTo($wu.find('.file-list'))
        }
        var $fileInfo = $fileItem.find('.info'),
            $progress = $fileItem.find('.progress span'),
            $success = $fileItem.find('.success')
        if (!isPDF) {
            var $imgWrap = $fileItem.find('.img-wrap')
            if (isUrl) {
                $imgWrap.html('<img src="' + file.url + '">')
            }
        }

        if (isUrl) {
            $fileItem.on('click.close', '.close', function () {
                removeFile(file)
            })
        } else {
            $fileItem.on('click.close', '.close', function () {
                uploader.removeFile(file)
            })
            var showErr = function (code) {
                var text
                switch (code) {
                    case 'exceed_size':
                        text = '文件大小超出'
                        break
                    case 'interrupt':
                        text = '上传暂停'
                        break
                    default:
                        text = '上传失败, 请重试'
                }
                $fileInfo.show().text(text)
            }
            if (file.getStatus() === 'invalid') {
                showErr(file.statusText)
            } else {
                if (!isPDF) {
                    $imgWrap.text('预览中')
                    uploader.makeThumb(file, function (err, src) {
                        if (err) {
                            $imgWrap.text('不能预览')
                            return
                        }
                        if ($.fn.wu.isSupportBase64) {
                            $imgWrap.html('<img src="' + src + '">')
                        } else {

                            /* 服务端预览 */
                            $imgWrap.text('不支持 Base64 预览')
                        }
                    }, thumbnailWidth, thumbnailHeight)
                }
                percentages[file.id] = [file.size, 0]
                file.rotation = 0
            }
            file.on('statuschange', function (cur, prev) {
                if (prev === 'progress') {
                    $progress.hide().width(0)
                }
                if (cur === 'error' || cur === 'invalid') {
                    console.log(file.statusText)
                    showErr(file.statusText)
                    percentages[file.id][1] = 1
                } else if (cur === 'interrupt') {
                    showErr('interrupt')
                } else if (cur === 'queued') {
                    percentages[file.id][1] = 0
                } else if (cur === 'progress') {
                    $fileInfo.hide()
                    $progress.css('display','block');
                } else if (cur === 'complete') {
                    // $success.show()
                }
                $fileItem.removeClass('state-' + prev).addClass('state-' + cur)
            })
        }
    }
    var uploader = WebUploader.create(o);

    // 文件上传过程中创建进度条实时显示。
    uploader.on( 'uploadProgress', function( file, percentage ) {
        var $li = $( '#'+file.id ),
            $percent = $li.find('.progress span');

        // 避免重复创建
        if ( !$percent.length ) {
            $percent = $('<div class="progress ">' +'<span style="width: 0%">' +'</span>' +'</div>').appendTo( $li ).find('span');
        }

        $li.find('.state').text('上传中');

        $percent.css( 'width', percentage * 100 + '%' );
    });

    // 文件加入队列 ghost-hidden
    uploader.on('fileQueued', function (file) {
        //console.log(uploader.stats)
        if (!multiple) {
            $wu.find('.pick-wrap, .pick-helper').addClass('ghost-hidden')
        }
        addFile(file)
    });
    // 上传失败
    uploader.on('uploadError', function (file, res) {
        console.warn('uploadError',file,res)
    });
    // 上传成功
    uploader.on('uploadSuccess', function (file, res) {
        // 状态大于0 则失败
        if (res.status != 0) {
            var msg = []
            if (Object.prototype.toString.call(res.info) === "[object Array]") {
                var _infoList = res.info
                for (var i = 0, len1 = _infoList.length; i < len1; i++) {
                    msg.push(_infoList[i].message)
                }
            } else {
                msg = [res.info]
            }
            msg.push('上传失败, 请联系管理员')
            dmAlert(msg.join('<br>'))
            $('#' + file.id).find('.close').trigger('click')
            $wu.triggerHandler('fail')
        } else {
            //  设置路径
            file.path = res.path;
            if (!multiple) {
                var index = $wu.data('index') || 0
                $form.find('[name^=basepath]').eq(index).val(res.path)
                $form.find('[name^=md5]').eq(index).val(res.md5)
                $form.find('[name^=sha1]').eq(index).val(res.sha1)
                $form.find('[name^=filename]').eq(index).val(file.name)
                $('#' + file.id).find('.file-path').attr('href', res.path)
                // 自定义一个存储路径 input
                $wu.find('.file-upload').val(res.path)
            }else {

                var $path = $wu.find('.file-upload');
                var path = $path.val() || '';
                var buffer = (path==='')?[]:path.split(',');

                buffer.push(res.path);
                $path.val(buffer.join(','));

            }

            $wu.triggerHandler('success', {
                file: file,
                response: res
            })
        }
    });
    uploader.on('fileDequeued', function (file) {
        return removeFile(file)
    });
    // 文件校验出错
    uploader.on('error', function (code, data) {
        switch (code) {
            case 'Q_EXCEED_NUM_LIMIT':
                return dmAlert('最多只允许上传 ' + data + ' 个文件')
        }
    });
    var removeFile = function (file) {
        var $fileItem
        $fileItem = $('#' + file.id)
        delete percentages[file.id]
        $fileItem.remove()
        if (!multiple) {
            $wu.find('.pick-wrap, .pick-helper').removeClass('ghost-hidden')
            var index = $wu.data('index') || 0
            $form.find('[name^=basepath]').eq(index).val('')
            $form.find('[name^=md5]').eq(index).val('')
            $form.find('[name^=sha1]').eq(index).val('')
            $form.find('[name^=filename]').eq(index).val('')
            $wu.find('.file-upload').val('')
        }else {
            var url = file.path || '';
            var $path = $wu.find('.file-upload');
            var path = $path.val() || '';
            var buffer = path.split(',');
            var index = buffer.indexOf(url);
            if(index>-1){
                buffer.splice(index,1);
                console.log('delete ok');
            }
            $path.val(buffer.join(','));
        }

        $wu.triggerHandler('delete', file)
    }
    //不存在文件列表就自建
    if (!files) {
        var index = $wu.data('index') || 0
        var $basePath = $form.find('[name^=basepath]').eq(index)
        if ($basePath.length) {
            var basePath = $basePath.val()
            if (basePath) {
                files = [{
                    url: $basePath.data('protectedUrl') || basePath,
                    ext: basePath.slice(basePath.lastIndexOf('.') + 1),
                    name: $form.find('[name^=filename]').eq(index).val()
                }]
            }
        }
    }
    if (files) {
        if (typeof files === 'string') {
            files = [
                {
                    url: files
                }
            ]
        } else if (Object.prototype.toString.call(files) === "[object Object]") {
            files = [files]
        }
        for (var i = 0, len1 = files.length; i < len1; i++) {
            addFile(files[i], true)
        }
        if (!multiple) {
            $wu.find('.pick-wrap, .pick-helper').addClass('ghost-hidden')
        }
    }
    $wu.data('uploader',uploader);
    return $wu
}
$.fn.wu.isSupportBase64 = true
!function () {
    var img = new Image
    img.onload = img.onerror = function () {
        if (this.width !== 1 || this.height !== 1) {
            $.fn.wu.isSupportBase64 = false
        }
    }
    img.src = 'data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw=='
}()

$(function () {
    $('.wu:visible').not('.manual').each(function () {
        $(this).wu()
    })
})
