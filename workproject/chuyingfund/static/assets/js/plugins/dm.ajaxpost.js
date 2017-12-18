!function ($) {
    $.fn.disable = function (msg, loading) {
        if (loading == null) loading = true
        return this.each(function () {
            var $btn = $(this).prop('disabled', true),
                _msg = msg
            clearTimeout($btn.data('loadingTimer'))

            var oriHTML = $btn.data('html')
            if (oriHTML == null) {
                oriHTML = $btn.html()
                $btn.data('html', oriHTML)
            }

            if (!_msg) _msg = oriHTML
            if (!loading) {
                $btn.html(_msg)
                return
            }

            var i = 0
            var tick = function () {
                $btn.html(_msg + new Array(i + 1).join('.'))
                i++
                if (i === 4) {
                    i = 0
                }
                $btn.data('loadingTimer', setTimeout(tick, 1200))
            }
            tick()
        })
    }
    $.fn.enable = function () {
        return this.each(function () {
            var $btn = $(this).prop('disabled', false)
            clearTimeout($btn.data('loadingTimer'))
            var oriHTML = $btn.data('html')
            if (oriHTML) {
                return $btn.html(oriHTML)
            }
        })
    }
}(jQuery)

window.dm = window.dm || {};
$.ajaxSetup({
    cache: false
})

dm.noop = function () {

}
dm.ajax = function (method, url, data, options) {
    var dtd = $.Deferred();
    var dataType = 'json';
    var defaultOption = {
        alert: 'alert'
    }
    var o = $.extend({}, defaultOption, options);
    if(method === 'getJSON'){
        method = 'get';
    }
    if(method === 'html') {
        dataType = 'html';
        method = 'get';
    }
    $.ajax({
        url: url,
        type: method,
        dataType: dataType,
        data: data
    })
    .done(function(res) {
        if(dataType === 'html') {
            dtd.resolve(res);
            return
        }

        /*if(typeof res === 'string'){
            res = eval('('+res+')');
        }*/
        if (!res) {
            dm[o.alert]('服务器未返回数据')
            dtd.reject()
        }
        if (res.status == 9) {
            var msg = '',
                redirect = function () {
                    var href = res.data
                    if (~href.indexOf('?')) href += '&'
                    else href += '?'
                    location.href = href + 'redirect_url=' + encodeURIComponent(location.href)
                }
            if (res.info === 'NEED_REAL_NAME_AUTH') msg = '您还未进行实名认证, 是否立即前往实名认证'
            else if (res.info === 'NEED_BANK_AUTH') msg = '您还未进行银行卡实名认证，是否立即前往认证'
            else if (res.info === 'NEED_QUESTION_AUTH') msg = '您还未进行合格投资人, 是否立即前往合格投资人认证'

            if (msg) dm.confirm(msg, redirect)
            else redirect()
            dtd.reject()

        } else if (res.status > 0) {
            
            var msg = []
            if (Object.prototype.toString.call(res.info) === "[object Array]") {
                var infoList = res.info
                for (var i = 0, len = infoList.length; i < len; i++) {
                    msg.push(infoList[i].message)
                }
            } else {
                msg.push(res.info)
            }
            dm[o.alert](msg.join('<br>'))
            dtd.reject(res)
        } else if ('' + res.status === '0') {
            // status == 0 or '0'则表示正确
            dtd.resolve(res)
        } else {
            dm[o.alert]('未知错误')
            dtd.reject(res)
        }
    })
    .fail(function(res) {
        dm[o.alert]('加载失败, 请重试');
        dtd.reject({'info':'加载失败, 请重试'});
        
    })
    .always(function() {
        //console.log("complete");
    });
    
    return dtd
}
dm.getJSON = function (url, data, options) {
    return dm.ajax('getJSON', url, data, options)
}
dm.get = function (url, data, options) {
    return dm.ajax('get', url, data, options)
}
dm.post = function (url, data, options) {
    return dm.ajax('post', url, data, options)
}


$(document).off('submit.ajax').on('submit.ajax', '.ajax-form', function (e, eventData) {
    e.preventDefault()
    var $form = $(this),
        $btn = $form.find(':submit')

    var ajaxSubmit = function () {
        
        var method = $form.data('ajaxMethod') || $form.attr('method')
        if (!method) method = 'post'
        var result = $form.trigger('beforeSend');
        
        var error = $form.data('error');
        //console.log('beforeSend error',error);
        if(error){
            if(typeof error === 'string'){
                dm.alert(error)
            }
            return;
        }
        $btn.disable()
        dm.ajax(method, $form.attr('action'), $form.serialize(), $form.data()).done(function (res) {
            if (eventData != null && eventData.success) {
                for (var _event in eventData) {
                    $form.triggerHandler(_event + '.' + eventData[_event], res)
                }
            }
            else $form.triggerHandler('success', res)
        }).fail(function () {
            $form.triggerHandler('fail')

        }).always(function () {
            $btn.enable()
            $form.triggerHandler('always')
        })
    }
    if ($form.validate != null) {
        return $form.validate(null, ajaxSubmit)
    } else {
        return ajaxSubmit()
    }
});

$(document).off('submit.native').on('submit.native', '.native-form', function (e, eventData) {
    e.preventDefault()
    var $form = $(this);

   
    if ($form.validate != null) {
        $form.validate(null, function(){
            $form[0].submit();
        });
    }
});


$(':submit').filter('[name]').click(function () {
    var $btn = $(this),
        $form = $btn.closest('form'),
        name = $btn.attr('name'),
        $input = $form.find('input[name=' + name + ']')
    if ($input.length === 0) {
        $input = $('<input type="hidden" name="' + name + '">').appendTo('form')
    }
    $input.val($btn.val())
});

