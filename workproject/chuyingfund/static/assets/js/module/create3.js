define(function(require, exports, module) {
    require('header');
   
    require('sortable');
    require('create.common');
    require('webuploader');
    require('template');
    require('dm.ajaxpost');
    require('dm.modal');
    require('dm.validate');
    require('dm.wu');


    console.log('create3.js');

   

    //团队
    $(function () {
        var $body = $('#edit-body'), $form = $body.closest('form'), $modal = $form.closest('.modal')
        
        var switchHolder = function () {
            var val = $form.find('[name=is_holder]:checked').val()
            if (val == 0) $form.find('.tr-holder').hide()
            else $form.find('.tr-holder').show()
        }
        var bindHolderRadioEvent = function () {
            $form.find('[name=is_holder]').click(function () {
                setTimeout(switchHolder, 0)
            })
        }
        // 成员添加或者修改成功后 页面重新加载
        $form.on('success', function () {
            
            location.reload()
            

        });
        /*$form.on('success', function () {
            var $formStep = $('form[id^=form-step]')
            $modal.modal('hide')
            if ($formStep.find('[name=introduce]').val()) {
                $formStep.one('success.refresh', function () {
                    location.reload()
                }).trigger('submit', {success: 'refresh'})
            }
            else {
                location.reload()
            }

        })*/
        $modal.on('shown', function () {
            $modal.find('.wu').wu().removeClass('hidden')
            $modal.find('form').validate('init').find('input[maxlength], textarea[maxlength]').not('[nocount]').wordCount()
            bindHolderRadioEvent()
        }).on('hidden', function () {
            $body.empty()
        });

        $('.team-list').on('click.edit', '.edit', function () {
            var $item = $(this).closest('.team-item')
            var data = JSON.parse($.trim($item.find('.item-data').text()))
            var tplData = {
                basepath: data.src,
                memberId: data.id,
                name: data.name,
                duty: data.duty,
                shortIntro: data.short_intro,
                isHolder: data.member_type,
                proportion: data.shared_rate,
                amount: data.amount,
                relation: data.relationship,
                fullTime: data.is_fulltime,
                memberIntro: $item.find('[name=memberIntro]').val()
            }
            $body.html(template('edit-table', tplData))
        }).on('click.delete', '.delete', function () {
            var $btn = $(this)
            dm.confirm('确定要删除吗?', function () {
                $btn.closest('form').one('success', function () {
                    $(this).closest('.team-item').fadeOut(function () {
                        $(this).remove()
                    })
                }).submit()
            })
        })

        $('#btn-add-team').on('click', function () {
            var data = {
                avatarSrc: '',
                id: '',
                name: '',
                duty: '',
                intro: ''
            }
            $body.html(template('edit-table', data))
        })
    });


    /* 拖拽排序 */
    !(function () {
        var $teamList = $('.team-list'),
                $form = $('#form-step-3')
        if (!$teamList.length) return;
        window.sortable = Sortable.create($teamList[0], {
            ghostClass: 'ghost',

            draggable: '.team-item'
        })
        var sort = function () {
            var html = '';
            var arr = [];
            $form.find('input[name^=member]').remove()
            $teamList.children('.team-item').each(function (i) {
                arr.push($(this).data('id'));
                //html += "<input type='hidden' name='member[" + i + "]' value='" + $(this).data('id') + "'>"
            })
            html = "<input type='hidden' name='members' value='" + arr.join(',') + "'>";
            $form.append(html)
        }
        $('#proj3-submit').on('click', function (e) {
            e.preventDefault()
            sort()
            $form.trigger('submit', {success: 'next'})
        })
        $('.btn-save, .btn-preview').on('click', function () {
            sort()
        })
    })();



    
      
});