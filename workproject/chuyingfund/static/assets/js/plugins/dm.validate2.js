/* 提示框 */
$(function ($) {

    $.fn.bubble = function (msg, direction, duration) {
        var $bubble, caretDirection, ref;
        if (direction == null) {
            direction = 'down';
        }

        msg = (ref = this.data('bubbleMsg')) != null ? ref : msg;
        direction = this.data('bubbleDir') || direction;
        duration = this.data('bubbleDuration') || duration || Math.max(msg.length, 8) * 500;
        $bubble = this.siblings('.bubble');
        caretDirection = {
            'up': 'down',
            'down': 'up',
            'right': 'left',
            'left': 'right'
        };
        
        if ($bubble.length === 0) {
            $bubble = $('<div class="bubble bubble-' + direction + ' caret-' + caretDirection[direction] + '"></div>').insertAfter(this);
        }
        $bubble.html(msg);
        if (direction === 'right') {
            $bubble.css({
                'left': this.data('bubbleLeft') != null ? this.data('bubbleLeft') : this.outerWidth() + 8,
                'top': this.data('bubbleTop') != null ? this.data('bubbleTop') : (this.outerHeight() - $bubble.outerHeight()) / 2
            });
        } else if (direction === 'down') {
            $bubble.css({
                'left': this.data('bubbleLeft') != null ? this.data('bubbleLeft') : this.outerWidth() * 0.5,
                'top': this.data('bubbleTop') != null ? this.data('bubbleTop') : this.outerHeight() + 8
            });
        } else if (direction === 'up') {
            $bubble.css({
                'left': this.data('bubbleLeft') != null ? this.data('bubbleLeft') : this.outerWidth() * 0.5,
                'bottom': this.data('bubbleBottom') != null ? this.data('bubbleBottom') : this.outerHeight() + 8
            });
        }
        clearTimeout($bubble.data('bubbleTimer'));
        $bubble.data('bubbleTimer', setTimeout(function () {
            return $bubble.removeBubble();
        }, duration));
        return this;
    };
    return $.fn.removeBubble = function () {
        if ($(this).hasClass('bubble')) {
            this.remove();
        } else {
            this.siblings('.bubble').remove();
        }
        return this;
    };
});

(function ($) {
    var ruleMethods = {
        china_name:function(value){
            var re = /[\u4E00-\u9FA5\uF900-\uFA2D]/;

            if(!value){
                return false;
            }
            if(value.length<2){
                return false;
            }
            if(/[A-Za-z]/.test(value)){
                return false;
            }
            if(re.test(value)){
                return true;
            }

            return false;
        },
        idcard:function(value) {
            var idCard = value;
            var Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 ];// 加权因子  
            var ValideCode = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ];// 身份证验证位值.10代表X 
            /** 
             * 判断身份证号码为18位时最后的验证位是否正确 
             * @param a_idCard 身份证号码数组 
             * @return 
             */ 
            var isTrueValidateCodeBy18IdCard = function(idCard) {  
                var sum = 0; // 声明加权求和变量  
                var a_idCard=idCard.split("");
                   if (a_idCard[17].toLowerCase() == 'x') {  
                        a_idCard[17] = 10;// 将最后位为x的验证码替换为10方便后续操作  
                    }
                    for ( var i = 0; i < 17; i++) {  
                        sum += Wi[i] * a_idCard[i];// 加权求和  
                    }
                    if (a_idCard[17] == ValideCode[sum % 11]) {  
                        return true;  
                    } else {  
                        return false;  
                    }  
            };
             /** 
              * 验证18位数身份证号码中的生日是否是有效生日 
              * @param idCard 18位书身份证字符串 
              * @return 
              */ 
            var isValidityBrithBy18IdCard = function(idCard18){  
                var year =  idCard18.substring(6,10);  
                var month = idCard18.substring(10,12);  
                var day = idCard18.substring(12,14);  
                temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));  
                // 这里用getFullYear()获取年份，避免千年虫问题  
                if(temp_date.getFullYear()!=parseFloat(year)  
                      ||temp_date.getMonth()!=parseFloat(month)-1  
                      ||temp_date.getDate()!=parseFloat(day)){  
                        return false;  
                }else{  
                    temp_date = year+"-"+month+"-"+day; 
                    return true;  
                }  
            };
            /** 
             * 验证15位数身份证号码中的生日是否是有效生日 
             * @param idCard15 15位书身份证字符串 
             * @return 
             */ 
            var isValidityBrithBy15IdCard = function(idCard15){  
                var year =  idCard15.substring(6,8);  
                var month = idCard15.substring(8,10);  
                var day = idCard15.substring(10,12);  
                var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));  
                // 对于老身份证中的你年龄则不需考虑千年虫问题而使用getYear()方法  
                if(temp_date.getYear()!=parseFloat(year)  
                        ||temp_date.getMonth()!=parseFloat(month)-1  
                        ||temp_date.getDate()!=parseFloat(day)){  
                          return false;  
                }else{  
                    return true;  
                }  
            };
            if(idCard){
                if(/^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/.test(idCard)){
                    if(idCard.length == 15 && isValidityBrithBy15IdCard(idCard)){  
                        return true;
                    }else if(idCard.length == 18 && isValidityBrithBy18IdCard(idCard) && isTrueValidateCodeBy18IdCard(idCard)){  
                        return true;  
                    }else{
                        
                        return false; 
                    }
                }else{
                    return false; 
                }
            }
        },
        // 6-20位 必须包含数字和英文
        password:function(value){
            if(!value || value.length<6 || value.length>20) {
                return false;
            } else {
                if(/[A-Za-z].*[0-9]|[0-9].*[A-Za-z]/.test(value)){
                    return true;
                }else {
                    return false;
                }
                
            }
            
        },
        password2:function(value){
            var pa1 = 1;
            if(!value){}
        },
        // 6-16位,支持数字、字母和_、－、／
        username:function(value){
            var re = new RegExp("['\"&\\n\r\t\b\f@,;%/]");
            if(!value || value.length<6 || value.length>16 || re.test(value)) {
                return false;
            } else {
                return true;
            }
            
        },
        //手机号码规则：可以以0开头+三位固定号段+8为数字*/
        cellphone:function(value){
            //170是虚拟号段
            /*移动号段16个
             *  134、135、136、137、138、139、147、150、151、152、157、158、159、182、183、187、188 170  
             */
            var pattern1 = /^0{0,1}(13[4-9]|147|15[0-2]|15[7-9]|18[23478]|178|170)[0-9]{8}$/;  
            /*联通号段7个  
            130、131、132、155、156、185、186  
            */  
            var pattern2 = /^0{0,1}(13[0-2]|15[56]|145|18[56]|176)[0-9]{8}$/;  
            /*电信号段4个  
            133、153、180、189   
            */  
            var pattern3 = /^0{0,1}(133|153|180|181|189|177)[0-9]{8}$/;
            if(value!='' && !pattern1.test(value) && !pattern2.test(value) 
                    && !pattern3.test(value)) {
                //this.ErrorMessage[name] = this.messages['cellphone'];
                return false;
            } else {
                return true;
            }
        },
    };
    /*
     元素中存储的变量:
     input: ruleList(规则列表对象), readyValidate(已验证过的字段, 处于"热"状态, 随打字响应)
     form: bubbled(已有弹出)
     */
    var defaultOption, initForm, initInput, matchRule, msgInfo, patternInfo, validateForm, validateInput;
    patternInfo = {
        required: /\S+/,
        email: /^[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$/,
        url: /^\s*((https?|ftp):\/\/)?(([^:\n\r]+):([^@\n\r]+)@)?((www\.)?([^\/\n\r]+))\/?([^?\n\r]+)?\??([^#\n\r]*)?#?([^\n\r]*)$/,
        tel: /^\s*(?:\+?(\d{1,3}))?([-. (]*(\d{3})[-. )]*)?((\d{3})[-. ]*(\d{2,4})(?:[-.x ]*(\d+))?)\s*$/,
        number: /^\d[\d\.]*$/
    };
    msgInfo = {
        required: '请填写此字段。',
        email: 'email 不合法',
        url: 'url 不合法',
        tel: '电话号码不合法',
        number: '不是数字',
        maxlength: '超出字数限制',
        min: '必须大于@@',
        max: '必须小于@@',

        idcard:'身份证格式有误',
        username:'格式有误 用户名6-16位',
        password:'格式有误 密码6-20位 必须包含数字和英文',
        cellphone:'手机号码不合法',
        pattern: '不合法'
    };
    // 仅支持以下验证 required email url tel number maxlength min max pattern
    matchRule = function (str, rule, pattern) {
        var result = true
        if (pattern == null) pattern = true

        switch (rule) {
            case 'required':
                if (pattern) {
                    result = patternInfo.required.test(str);
                }
                break;
            case 'email':
                if (pattern) {
                    result = patternInfo.email.test(str);
                }
                break;
            case 'url':
                if (pattern) {
                    result = patternInfo.url.test(str);
                }
                break;
            case 'tel':
                if (pattern) {
                    result = patternInfo.tel.test(str);
                }
                break;
            case 'number':
                if (pattern) {
                    result = patternInfo.number.test(str);
                }
                break;
            case 'maxlength':
                return $.trim(str).length <= pattern;
            case 'min':
                if ($.isNumeric(pattern)) {
                    return parseFloat(str) >= parseFloat(pattern);
                } else {
                    return new Date(str) >= new Date(pattern);
                }
                break;
            case 'max':
                if ($.isNumeric(pattern)) {
                    return parseFloat(str) <= parseFloat(pattern);
                } else {
                    return new Date(str) <= new Date(pattern);
                }
                break;
            case 'pattern':
                return eval(pattern).test(str);
            default:
                // 自定义的rules
                
                for(var _rule in ruleMethods) {
                    if(rule === _rule){
                        
                        var method = ruleMethods[_rule];
                        return method(str);
                    }
                }
                break;
        }
                
        return result;
    };
    defaultOption = {
        batch: false,
        typing: false,
        blur: false
    };
    // 获取input的验证规则 放在input对象里  ruleList {required:true,maxlength:2,pattern:/\w/}
    $.fn.buildRuleList = function () {
        return this.each(function (idx, elm) {
            var attr, attrList, attrValue;
            var ruleList = {},
                $elm = $(elm),
                type = $elm.attr('type');
            ruleList['required'] = $elm.attr('required');
            var typeList = ['email', 'url', 'number'];
            for (var i = 0, len = typeList.length; i < len; i++) {
                var _t = typeList[i];
                if (type === _t) {
                    ruleList[_t] = true;
                    break;
                }
            }
            attrList = ['maxlength', 'max', 'min', 'pattern'];
            for (var j = 0, len1 = attrList.length; j < len1; j++) {
                attr = attrList[j];
                attrValue = $elm.attr(attr);
                if (attrValue != null && attrValue !== '') {
                    ruleList[attr] = attrValue;
                }
            }
            var _ajax = $elm.data('ajax');
            if (_ajax) ruleList['ajax'] = _ajax;

            var _rule = $elm.data('rule');
            if (_rule) ruleList[_rule] = _rule;
            
            $elm.data('ruleList', ruleList);
        });
    };
    initForm = function ($form) {
        $form.attr('novalidate', 'novalidate').find('input, textarea, select').validate('init');
    };
    initInput = function ($input) {
        if($input.data('rule') =='checkbox' || $input.data('rule') =='radio'){
            $input.buildRuleList();
            return;
        }
        // 打字的时候验证
        $input.buildRuleList().off('.validate').on('input.validate propertychange.validate', function () {
            return $(this).validate({
                typing: true
            })
        })
        // 默认失去焦点的时候也去验证 否则加属性 blurSilence=ture 
        if (!$input.data('blurSilence')) {
            $input.on('blur.validate', function () {
                $(this).validate({
                    blur: true
                })
            })
        }
    };
    validateForm = function ($form, option, success, fail) {
        var result = true
        $form.data('bubbled', 0)
        $form.find('.bubble').removeBubble();
        // 验证的对象
        $form.find('input, textarea, select,[data-rule="radio"],[data-rule="checkbox"]').validate({
            batch: true
        }, $.noop, function () {
            result = false
        })
        if (result) success()
        else fail()
    }
    // 错误信息 data-ruleName-msg='balabalabala'
    validateInput = function ($input, option, success, fail) {
        if (option.typing && !$input.data('readyValidate')) return;
        var value = $.trim($input.val()) || '',
            ruleList = $input.data('ruleList'),
            result = true,
            msg = ''
        if (!ruleList) ruleList = $input.buildRuleList().data('ruleList');

        $input.removeBubble();

        if($input.data('rule')=='radio' || $input.data('rule')=='checkbox'){
            var name = $input.data('name');
            var count = 0;
            var ruleName = $input.data('rule')
            $('[name='+name+']').each(function(){
                if(this.checked){
                    count++
                }
            });
            //console.log(1111,count,name)
            if(count>0){
                result = true;
            }else {
                result = false;
                msg = $input.data(ruleName + 'Msg') || msgInfo[ruleName] || '格式有误';
            }

        }
        if (!(value.length === 0 && !ruleList['required'])) {
            for (var ruleName in ruleList) {
                if (ruleList.hasOwnProperty(ruleName)) {
                    var rule = ruleList[ruleName];
                    if (!matchRule(value, ruleName, rule)) {
                        result = false;
                        //console.log(ruleName + 'Msg',$input.data(ruleName + 'Msg'))
                        msg = $input.data(ruleName + 'Msg') || msgInfo[ruleName] || '格式有误';
                        msg = msg.replace(/@@/g, rule);
                        break;
                    }
                }
            }
        }

        // ajax
        if (result && ruleList.ajax && $input.data('lastValidateValue') !== $input.val()) {
            var doAjax = function () {
                $input.data('ajaxReady', false)
                $input.data('ajaxQueue', false)
                $input.data('lastValidateValue', $input.val())
                var _data = {}
                _data[$input.attr('name')] = $input.val()
                dm.getJSON(ruleList.ajax, _data, {alert: 'noop'}).fail(function (json) {
                    $input.addClass('error').bubble(json.info || json.message)
                }).always(function () {
                    $input.data('ajaxReady', true)
                    if ($input.data('ajaxQueue')) {
                        doAjax()
                    }
                })
            }
            var ajaxReady = $input.data('ajaxReady')
            if (ajaxReady == null || ajaxReady) {
                doAjax()
            } else {
                $input.data('ajaxQueue', true)
            }
        }
        // 验证格式正确
        if (result) {
            $input.removeClass('error')
            success()
        } else {
            var $form = $input.closest('form')
            if (option.typing) {
                $input.addClass('error')
            } else {
                $input.addClass('error')
                if (!option.batch || !$form.data('bubbled')) {
                    $form.data('bubbled', 1)
                    $input.bubble(msg, $input.data('bubbleDir') || 'down')
                    if (!option.blur) {
                        $input.focus()
                    }
                }
            }
            fail()
        }
        return $input.data('readyValidate', true)
    };
    return $.fn.validate = function (option, success, fail) {
        
        var method;
        if (success == null) success = $.noop
        if (fail == null) fail = $.noop
        if (typeof option === 'string') {
            method = option;
            option = success;
            if (method === 'init') {
                
                return this.each(function () {
                    var $this;
                    $this = $(this);
                    
                    if ($this.is('form')) {

                        return initForm($this);
                    } else {

                        return initInput($this);
                    }
                });
            }
        } else {
            option = $.extend({}, defaultOption, option);
            return this.each(function () {
                var $this;
                $this = $(this)
                if ($this.is('form')) {
                    validateForm($this, option, success, fail);
                } else {
                    
                    validateInput($this, option, success, fail);
                }
            });
        }
    };
})(jQuery);


$(function () {
    //console.log(3)
    $('form').not('[novalidate]').validate('init');
});

//word-count
$(function () {
    var buildDOM = function ($input, maxlength) {
        var $countWrap = $('<div class="word-count"><var>' + $input.val().length + '</var>/' + maxlength + '</div>').insertAfter($input);
        return $countWrap.find('var')
    };
    $.fn.wordCount = function () {
        return this.each(function () {
            var $input = $(this),
                maxlength = $input.attr('maxlength')
            if (!maxlength) {
                return
            }
            var $var = buildDOM($input, maxlength)
            if ($input.is('input')) $input.css('padding-right', (('' + maxlength).length + 1.8) + 'em')
            $input.on('input.count propertychange.count', function () {
                $var.html($input.val().length)
            })
        })
    }

    $('input[maxlength], textarea[maxlength]').not('[nocount]').wordCount()
})
