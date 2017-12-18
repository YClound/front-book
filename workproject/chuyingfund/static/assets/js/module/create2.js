define(function(require, exports, module) {
    require('header');
    //require('ueditor.config');
    require('create.common');
    require('ueditor');
    require('webuploader');
    require('template');
    require('dm.ajaxpost');
    require('dm.modal');
    require('dm.validate');
    require('dm.wu');


    console.log('create2.js');

    $(function(){
        var detail = UE.getEditor('detail');
        var businessPlan = UE.getEditor('businessPlan');

    });

    
    
    $('.submit-form').on('beforeSend',function(res){
        
    });
    $('.submit-form').on('success',function(res){
        console.log(res);
        // 跳转
        // location.href = '';
    });
    $('.submit-form').on('fail',function(res){
        console.log(res);
        // 跳转
        // location.href = '';
    });



    //PPT
    $(function () {
        return;
        
        var $fileList, $form, $wu, fileList, i, pathArr, sort
        $wu = $('#wu-ppt');
        if (!$wu.length) {
            return;
        }
        $form = $wu.closest('form');
        $fileList = $wu.find('.file-list');
        sort = function () {
            return $fileList.children().each(function (i) {
                var $input, id, val;
                id = $(this).attr('id');
                $input = $('[data-id=' + id + ']').filter('[name*=path]');
                if ($input.length) {
                    val = $input.val();
                    val = val.replace(/(,\d*)*$/, ',' + i);
                    return $input.val(val);
                }
            });
        };
        i = 1;
        $wu.on('success', function (e, info) {
            var file, html, response;
            file = info.file;
            response = info.response;
            html = '<input type="hidden" name="path[' + i + ']" data-id="' + file.id + '" value="' + response.path + '">';
            html += '<input type="hidden" name="md5[' + i + ']" data-id="' + file.id + '" value="' + response.md5 + '">';
            html += '<input type="hidden" name="sha1[' + i + ']" data-id="' + file.id + '" value="' + response.sha1 + '">';
            i++;
            $form.append(html);
            return sort();
        })
        $wu.on('delete', function (e, file) {
            var $input;
            $input = $form.find('[data-id=' + file.id + ']');
            if ($input.attr('name') === 'display_path[]') {
                $input.replaceWith('<input type="hidden" name="del_id[]" value="' + file.id + '">');
            } else {
                $input.remove();
            }
            return sort();
        });
        pathArr = [];
        $form.find('[name="display_path[]"]').each(function () {
            return pathArr.push({
                id: $(this).data('id'),
                url: $(this).val()
            })
        })
        $wu.wu({
            thumb: {
                width: 120,
                height: 90
            }
        }, pathArr);
        sort();

        /* 拖拽排序 */
        fileList = $fileList.get(0);
        return window.sortable = Sortable.create(fileList, {
            ghostClass: 'ghost',
            onSort: sort
        })
    });
      
});