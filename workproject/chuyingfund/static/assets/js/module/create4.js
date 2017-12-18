define(function(require, exports, module) {
    require('header');

    require('create.common');
    require('dm.ajaxpost');
    require('dm.modal');
    require('dm.validate');

   

    // 融资计划联动验证
    $(function () {
        // 出让股权
        var $valuation = $('#project_valuation'),
            $need = $('#need_fund'),
            $rate = $('#fund_rate')

        $valuation.add($need).on('input propertychange', function () {
            var valuation = parseInt($valuation.val(), 10) || 0,
                need = parseInt($need.val(), 10) || 0;
            if (valuation && need) {
                $rate.val(Math.round(need / valuation * 10000) / 100);
            } else {
                $rate.val('');
            }
        });

        // 是否允许超募
        var $inputOverFund = $('input[name=over_fund]')
        $('[name="can_over_fund"]').change(function () {
            if (this.value == 0) {
                $inputOverFund.val(0).prop('disabled', true)
            } else {
                $inputOverFund.prop('disabled', false)
            }
        });

        // 数据提交成功后的动作
        $('.ajax-form').on('success',function(){
          console.log('success')
        });

    });
      
});