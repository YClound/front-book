define(function(require, exports, module) {
    require('template');
    

    var numberPick = function () {
        var that = {}
        var updatePercent = function () {
            var investAmount = parseInt(that.$input.val(), 10) || 0
            var percent = investAmount / that.finalValuation * 100
            that.$percent.html(Math.round(percent * 10000) / 10000)


            that.$input.bubble('您投资的金额为 <strong class="c-danger"> ' + dm.wan(investAmount) + ' </strong> 万元','down')
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
            that.$minus.on('click', minus)
            that.$plus.on('click', plus)
            that.$input.on('input propertychange', updatePercent)
        }
        return that
    }();
    //地址
    var address = (function () {
        var $editForm, $list, $investForm, $investAddrId, $addrText
        var reset = function () {
            $editForm[0].reset()
            $editForm.find(':submit').text('添 加')
            $editForm.find('[name=id]').val('')
            $editForm.find('.city-select').citySelect('reset')
        }
        var bindEvent = function () {

            // 第一屏选择邮寄地址
            $('.s-address').on('click', function (e) {
                
                var addrId = $investAddrId.val();
                console.log('addrId',addrId)
                $list.children().removeClass('active').filter('[data-id=' + addrId + ']').addClass('active')

            })

            // 第二屏地址列表选择地址
            $list.on('click', '.addr-item', function (e) {
                var $this = $(this)
                $('.no-data-1').css('display','none');
                $('.has-data-1').removeAttr('style');
                $this.addClass('active').siblings().removeClass('active');
                $investAddrId.val($this.data('id'))
                $addrText.text($this.find('.addr-detail').text())
                dm.router.go('/');
            })

            // 添加地址按钮
            $('.btn-new-address').on('click', function () {
                reset()
            })

            // 修改地址按钮
            $list.on('click', '.edit', function (e) {
                e.stopPropagation();

                var $item = $(this).closest('.addr-item')
                $editForm.find('[name=id]').val($item.data('id'))
                $editForm.find('.city-select').citySelect('update', $item.data('prov'), $item.data('city'))
                $editForm.find('[name=address]').val($item.find('.addr-detail').text().replace(/\S* +\S* +/, ''))
                $editForm.find('[name=postno]').val($item.data('postno'))
                $editForm.find(':submit').text('修 改')
                dm.router.go('/address-edit');
            })
            // 删除地址按钮
            $list.on('click', '.delete', function (e) {
                e.stopPropagation()
            })
            // 设为默认地址
            $list.on('click', '.set-default', function (e) {
                e.stopPropagation()
                var $btn = $(this)
                $btn.closest('form').one('success', function () {
                    $list.find('.set-default').removeClass('active')
                    $btn.addClass('active')
                })
            })

            // 添加或修改成功后
            $editForm.on('success', function (e, json) {
                var data = {
                    id: json.data.id,
                    province: json.data.province,
                    city: json.data.city,
                    address: json.data.full_address,
                    postno: json.data.postno,
                    checked: true,
                    status: json.data.status,
                    active: false,
                    name:json.data.name,        
                    phone:json.data.phone
                }

                if($('.no-data').is(':visible')){
                    $('.no-data').css('display','none');
                    $('.has-data').removeAttr('style');
                    $('.no-data-1').css('display','none');
                    $('.has-data-1').removeAttr('style');
                    $investAddrId.val(json.data.id)
                    $addrText.text(json.data.full_address)
                }
                
                $list.find('.empty').remove()
                var $item = $list.find('[data-id=' + (json.data.modify_id || 0) + ']')
                if ($item.length) {
                    data.active = $item.hasClass('active')
                    $item.replaceWith(template('addr-item', data))
                } else {
                    $list.append(template('addr-item', data))
                }
                reset()
                history.back()
            })
        }
        return {
            init: function () {
                $editForm = $('.form-address-edit')
                $list = $('.s-address-list')
                $investForm = $('#form-follow,#form-lead-2')
                $investAddrId = $investForm.find('[name=addr]')
                $addrText = $('#addr-text')
                bindEvent()
            }
        }
    })();

    $(function () {
        $('.s-header').headroom();
        
        numberPick.init();
        address.init();

        $('#form-follow').on('success',function(e,res){
            if(res.data) {       
                location.href = res.data;      
            }
        });
        $('.view').on('shown',function(){
            $('.bubble').remove();
            $('#view').hide();
        }).on('hide',function(){
            $('#view').show();
        });
        dm.router(['/address','/address-edit'], function (path) {
            dm.view.show(path.replace(/^\//, ''));
        });
        
        
    });



    
      
});