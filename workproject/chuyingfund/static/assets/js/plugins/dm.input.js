/**
 * Created by MMMMMMI on 2015/12/11.
 */
$.fn.inputMask = function () {
    return this.each(function () {
        var $ele = $(this),
            $form = $ele.closest('form'),
            maskType = $ele.data('mask')
        var fieldName = $ele.attr('name')
        $ele.removeAttr('name')
        var $inputHidden = $("<input type='hidden' name='" + fieldName + "'>").appendTo($form)

        var updateValue = function (val, type) {
            unbindevent()
            var result
            if (type === 'number') result = maskNumber(val)
            else if (type === 'bankcard') result = maskBankCard(val)
            if (result) {
                $ele.val(result.visual)
                $inputHidden.val($.trim(result.real))
            }
            bindEvent()
        }

        var bindEvent = function () {
            $ele.on('input.mask propertychange.mask', function () {
                updateValue($ele.val(), maskType)
            })
            if (!!window.ActiveXObject && document.documentMode == 9) { // IE 9 退格键
                $ele.on('keydown.mask', function (e) {
                    if (e.which === 8) {
                        setTimeout(function () {
                            updateValue($ele.val(), maskType)
                        }, 0)
                    }
                })
            }
        }
        var unbindevent = function () {
            $ele.off('.mask')
        }

        bindEvent()

        function maskNumber(val) {
            var number = val.replace(/,/g, "");
            var digit = number.indexOf("."); // 取得小数点的位置
            var int = number.substr(0, digit); // 取得小数中的整数部分
            var mag = [];

            function getNumber() {
                var i = integer.length; // 整数的个数
                while (i > 0) {
                    var word = integer.substring(i, i - 3); // 每隔3位截取一组数字
                    i -= 3;
                    mag.unshift(word); // 分别将截取的数字压入数组
                }
            }

            if (number.indexOf(".") == -1) { // 整数时
                var integer = number;
                getNumber();
                number = mag;
            }
            else { // 小数时
                var integer = int;
                getNumber();
                number = mag + number.substring(digit);
            }
            return {
                visual: number,
                real: val.replace(/,/g, "")
            }
        }

        function maskBankCard(val) {
            var bankcard = val.replace(/\s/g, '').replace(/(\d{4})(?=\d)/g, "$1 ")
            return {
                visual: bankcard,
                real: val.replace(/\s/g, '')

            }
        }
    })
}
$(function () {
    var browser = {
        versions: function () {
            var u = navigator.userAgent, app = navigator.appVersion;
            return {//移动终端浏览器版本信息
                trident: u.indexOf('Trident') > -1, //IE内核
                presto: u.indexOf('Presto') > -1, //opera内核
                webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
                gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
                mobile: !!u.match(/AppleWebKit.*Mobile.*/) || !!u.match(/AppleWebKit/), //是否为移动终端
                ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
                android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
                iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //是否为iPhone或者QQHD浏览器
                iPad: u.indexOf('iPad') > -1, //是否iPad
                webApp: u.indexOf('Safari') == -1,//是否web应该程序，没有头部与底部
                weixin: u.indexOf('MicroMessenger') > -1 //是否微信 （2015-01-22新增）
            };
        }(),
        language: (navigator.browserLanguage || navigator.language).toLowerCase()
    }
    if (!browser.versions.android && !browser.versions.weixin) {
        $("input[data-mask]").inputMask()
    }
})

