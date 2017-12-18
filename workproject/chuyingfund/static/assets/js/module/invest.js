define(function(require, exports, module) {
    require('header');

    require('template');
    require('dm.ajaxpost');
    require('dm.modal');
    require('dm.validate');
    require('dm.city.select');
    require('clipboard')
    
    // 剪贴板
    // TODO: ios safari 兼容性
    function makeSelection(start, end, child, parent) {
        var range = document.createRange();
        range.setStart(parent.childNodes[child], start);
        range.setEnd(parent.childNodes[child], end);

        var sel = window.getSelection();
        sel.removeAllRanges();
        sel.addRange(range);
    }

    $(function () {
        var clipboard = new Clipboard('.btn-copy', {
            text: function (trigger) {
                return $(trigger).siblings('.can-copy').text()
            }
        })
        clipboard.on('success', function (e) {
            dm.notice('已复制')
        })
        clipboard.on('error', function (e) {
            dm.notice('复制失败, 请手动选择复制')
        })


        var canCopy = new Clipboard('.can-copy', {
            text: function (trigger) {
                return $(trigger).text()
            }
        })
        canCopy.on('success', function () {
            dm.notice('已复制')
        })
        canCopy.on('error', function () {
            dm.notice('复制失败, 请手动选择复制')
        })
    })

    
    // 跟投
    var follow = (function () {
        var that = {}
        var updatePercent = function () {
            var investAmount = parseInt(that.$input.val(), 10) || 0
            var percent = investAmount / that.finalValuation * 100
            that.$percent.html(Math.round(percent * 10000) / 10000)

            if (that.$totalInvest.length) {
                var _total = '' + (serverData.paidAmount + investAmount)
                that.$totalInvest.html(_total.replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,"))
            }

        }
        var minus = function () {
            var val = parseInt(that.$input.val(), 10) || that.min
            val -= that.step
            if (val < that.min) {
                val = that.min
                that.$minus.bubble('最低跟投金额至少为 <strong class="c-danger"> ' + that.min + ' </strong> 元')
            }
            that.$input.val(val)
            updatePercent()
        }
        var plus = function () {
            var val = parseInt(that.$input.val(), 10) || that.min
            that.$input.val(val + that.step)
            updatePercent()
        }

        var bindEvent = function () {
            that.$minus.on('click', minus)
            that.$plus.on('click', plus)
            $(document.body).on('keydown', function (e) {
                if (e.target.tagName === 'INPUT') {
                    return
                }
                if (e.which === 39) {
                    plus()
                } else if (e.which === 37) {
                    minus()
                }
            })
            that.$input.on('input propertychange', updatePercent)
        }
        that.init = function () {
            that.$pick = $('.number-pick');
            if (!that.$pick) {
                return;
            }
            that.$minus = that.$pick.find('.btn-minus');
            that.$plus = that.$pick.find('.btn-plus');
            that.$input = that.$pick.find('[type=number]');
            that.$percent = $('.percent')
            that.$totalInvest = $('.total-invest')
            that.finalValuation = parseInt($('#final_valuation').val(), 10) || 0

            that.min = parseInt(that.$input.attr('min'), 10) || 0;
            that.step = parseInt(that.$input.attr('step'), 10) || 1;
            that.$minus.data('bubbleDuration', 3000);
            that.$plus.data('bubbleDuration', 3000);
            return bindEvent();
        };
        return that;
    })();

    //地址
    var address = (function () {
        var that = {}
        var bindEvent = function () {

            var reset = function () {
                that.$form[0].reset()
                that.$form.find(':submit').text('添 加')
                that.$form.find('[name=id]').val('')
                that.$modal.find('.city-select').citySelect('reset')
            }
            // 添加地址按钮
            $('.btn-new-address').on('click', function () {
                reset()
            })

            // 修改地址按钮
            that.$list.on('click', '.edit', function () {
                var $item = $(this).closest('li')
                that.$modal.modal('show')
                that.$modal.find('[name=id]').val($item.data('id'))
                that.$modal.find('.city-select').citySelect('update', $item.data('prov'), $item.data('city'))
                that.$modal.find('[name=address]').val($item.children('label').text().replace(/\S* +\S* +/, ''))
                that.$modal.find('[name=postno]').val($item.data('postno'))
                if ($item.data('default')) that.$form.find('[name=status]').prop('checked', true)
                else that.$form.find('[name=status]').prop('checked', false)
                that.$modal.find('.btn-save-addr').text('修 改')
            })

            // 添加或修改成功后
            that.$form.on('success', function (e, json) {
                that.$modal.modal('hide')
                var data = {
                    id: json.data.id,
                    province: json.data.province,
                    city: json.data.city,
                    address: json.data.full_address,
                    postno: json.data.postno,
                    checked: true,
                    status: json.data.status
                }
                that.$list.find('.empty').remove()
                var html = template('addr-item', data);
                var $item = that.$list.find('[data-id=' + (json.data.modify_id || 0) + ']')
                if ($item.length) {
                    $item.replaceWith(html)
                } else {
                    that.$list.append(html)
                }
                reset()
            })
        }
        that.init = function () {
            that.$modal = $('.modal-address')
            that.$form = that.$modal.find('form').first()
            that.$list = $('.addr-list')
            bindEvent()
        };
        return that;
    })();

    // 领投
    var lead = function () {
        var that = {}
        var bindEvent = function () {
            $('.btn-lead-1').on('click', function () {
                location.href = $(this).data('href')
            })

            $('#form-lead-2').on('success', function (e, json) {
                window.location.href = json.data;
            })
        }
        that.init = function () {
            bindEvent()
        }
        return that
    }()


    // 跟投金额取整
    $(function () {
        $('#amount').on('blur', function () {
            var $input = $(this)
            var step = parseInt($input.attr('step'), 10) || 1,
                min = parseInt($input.attr('min'), 10)
            var val = parseInt($input.val(), 10)
            $input.val(min + Math.round((val - min) / step) * step)
        })
    })

    $(function () {
        follow.init()
        address.init()
        lead.init()
        $("#form-follow, #form_follow2, #form-append, #form-append-2, #form-append-3").on("success", function (e, json) {
            window.location.href = json.data;
        })
    });


    // 同意遵守投资规则
    var largeAmountWarn = function () {
        return {
            init: function () {
                var $form = $('#form-follow')
                var $btn = $('#btn-follow-1').on('click', function (e) {
                    e.preventDefault()

                    $form.validate(null, function () {
                        if (!$form.find('[name=agree]').prop('checked')) {
                            dm.alert('亲，您还没有同意遵守投资规则。')
                            return
                        }

                        var inputAmount = $form.find('[name=amount]').val(),
                                warnAmount = parseInt($btn.data('warnAmount'), 10) || 0
                        if (warnAmount > 0 && inputAmount > warnAmount) {
                            $('.modal-large-amount').modal()
                        } else {
                            $form.submit()
                        }
                    })
                })
                $('#btn-confirm-next').on('click', function () {
                    $form.submit()
                })
            }
        }
    }();

    $(function () {
        largeAmountWarn.init()
    });




    
      
});