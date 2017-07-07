<template>
    <div class="field am-list-item am-input-autoclear" v-show="show" v-if="exist" :class="myClass">
        <!-- label -->
        <div :style="'width:' + labelWidth + 'rem;'" class="am-list-label nowrap cust-field-label" v-if="label">
            {{label}}
        </div>
        <div class="am-list-control">
            <!-- input -->
            <input @blur="onblur" @focus="onfocus" ref="input" type="text" autocomplete="off"
                   :name="name" v-model.trim="value"
                   :placeholder="placeholder" :disabled="disabled" :maxlength="maxLength" :readonly="readonly" :id="id">
        </div>
        <!-- 清空 -->
        <div class="am-list-clear" v-show="value && focused" @click="resetValue">
            <i class="am-icon-clear am-icon"></i>
        </div>
        <!-- 显示或隐藏密码 -->
        <div v-if="type=='password'" @click="togglePwdVisiblitiy()" class="am-list-thumb right">
            <img class="eye" :src="pwdVisible ? eyeOnPng : eyePng" alt="">
        </div>
        <!-- 图片验证码(背景图片) 是否含有图片验证码-->
        <div v-if="hasVerifyCode" class="verify-code-wrapper am-list-thumb right" ref="vc" @click="_changeVerifyCode">
            <span :class="{'loading-tip':verifyCode == ''}"></span>
            <div :class="{'verify-code':true,spin:verifyCode == ''}" :style="'background-image:url(' + vc + ')'"></div>
        </div>
        <!-- 根据ext判断是否含有自动填充列表 -->
        <div ref="autofill" :style="'left:' + labelWidth + 'rem;'" class="auto-fill-popover" v-if="autoFillSuffixes">
            <div v-for="suffix in autoFillSuffixes" class="auto-fill-item" @click="autoFill(suffix)">{{value.split('@')[0]}}{{suffix}}</div>
        </div>
        <slot name="actions"></slot>
    </div>

</template>

<script>


    /*
     * 频率控制 返回函数连续调用时，fn 执行频率限定为每多少时间执行一次
     * @param fn {function}  需要调用的函数
     * @param delay  {number}    延迟时间，单位毫秒
     * @param immediate  {bool} 给 immediate参数传递false 绑定的函数先执行，而不是delay后后执行。
     * @return {function}实际调用函数
     */
    var throttle = function (fn,delay, immediate, debounce) {
        var curr = +new Date(),//当前事件
            last_call = 0,
            last_exec = 0,
            timer = null,
            diff, //时间差
            context,//上下文
            args,
            exec = function () {
                last_exec = curr;
                fn.apply(context, args);
            };
        return function () {
            curr= +new Date();
            context = this,
                args = arguments,
                diff = curr - (debounce ? last_call : last_exec) - delay;
            clearTimeout(timer);
            if (debounce) {
                if (immediate) {
                    timer = setTimeout(exec, delay);
                } else if (diff >= 0) {
                    exec();
                }
            } else {
                if (diff >= 0) {
                    exec();
                } else if (immediate) {
                    timer = setTimeout(exec, -diff);
                }
            }
            last_call = curr;
        }
    };

    /*
     * 空闲控制 返回函数连续调用时，空闲时间必须大于或等于 delay，fn 才会执行
     * @param fn {function}  要调用的函数
     * @param delay   {number}    空闲时间
     * @param immediate  {bool} 给 immediate参数传递false 绑定的函数先执行，而不是delay后后执行。
     * @return {function}实际调用函数
     */

    var debounce = function (fn, delay, immediate) {
        return throttle(fn, delay, immediate, true);
    };
    export default {
        props : {
            //输入框改变时触发的函数
            handleChange:{
                type:Function,
                default : function() {return true;}
            },
            //输入框失去焦点触发的函数
            handleBlur:{
                type:Function,
                default : function() {return true;}
            },
            //相等
            equalTo : {
                type : String,
                default : ''
            },
            id : {
                type : String,
                default : ''
            },
            rule : {
                type : RegExp,
                default : null
            },
            //错误提示信息
            ruleMsg : {
                type : String,
                default : ''
            },
            readonly : {
                type : Boolean,
                default : false
            },
            //自定义class
            myClass : {
                type : String,
                default : ''
            },
            show : {
                type : Boolean,
                default : true
            },
            labelWidth : {
                type : Number,
                default : 1
            },
            type : {
                type : String,
                default : 'text'
            },
            disabled : Boolean,
            mandatory : {
                type : Boolean,
                default : false
            },
            label : String,
            name : String,
            placeholder : String,
            // 是否是图片验证
            hasVerifyCode : {
                type : Boolean,
                default : false
            },
            verifyCode : String,
            verifyFormat : String,
            //错误验证和提示
            ext : String,
            itemCode : String,
            loginType : String,
            maxLength : {
                type : Number,
                default : 64
            },
            //初始值
            externalValue : String
        },
        data () {
            return {
                regexDisabled : false,//input不可用
                pwdVisible : false,//密码是否可见
                errorMsg : '',//错误信息
                value : '',
                exist : {
                    type : Boolean,
                    default : true
                },//是否隐藏整个组件
                focused : false, //是否显示清空按钮
                eyePng : 'data:image/png;base64,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',
                eyeOnPng : 'data:image/png;base64,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'
            }
        },
        watch : {
            // 初始化后 内容改变需要重新触发
            id: function () {
                this.setType(this.type);//确定input类型
                this.value = '';
                if(this.disabled) {
                    this.regexDisabled = true;
                }else {
                    this.regexDisabled = false;
                }
                if(this.externalValue!=undefined)this.setValue(this.externalValue || '');//初始化input的值
            },
            pwdVisible : function(n) {
                //转换密码是否可见
                $(this.$refs.input).attr('type', n ? 'text' : 'password');
            },
            value : function(n, o) {
                //触发change事件
                this.$emit('change');
                this.value = this.value.substr(0, Math.max(1, this.maxLength));
            }
        },
        computed : {
            valid : function() {
                //必填vlue为空验证不通过
                if (this.mandatory && !this.value) {
                    this.errorMsg = '';//this.label + '必须填写';
                    return false;
                }
                //输入框不可用，验证通过
                if (this.regexDisabled) {
                    this.errorMsg = '';
                    return true;
                }
                //判断两个输入框不同，验证不通过
                if(this.equalTo) {
                    if(this.value != $('#'+this.equalTo).val()) {
                        this.errorMsg = this.regexErrorTip;
                        return false;
                    }
                }

                //值不为空，验证不匹配，验证不通过
                if (this.value && this.regex && !new RegExp(this.regex).test(this.value)) {
                    this.errorMsg = this.regexErrorTip;
                    return false;
                }
                
                //match exactly
                if (this.value && this.regex) {
                    var match = this.value.match(this.regex);
                    if (match == null || this.value != match[0]) {
                        this.errorMsg = this.regexErrorTip;
                        return false;
                    }
                }
                
                this.errorMsg = '';
                return true;
            },
            vc : function() {
                //根据verifyCode显示验证码图片
                if (this.verifyCode == '') return CDN_URL + '/images/spinner.png';
                return 'data:image/' + this.verifyFormat + ';base64,' + this.verifyCode;
            },
            autoFillSuffixes : function() {
                // 获取自动填充的列表
                var validatorsArr = [];
                try {
                    var ext = this.ext.replaceAll("'", '"');
                    validatorsArr = JSON.parse(ext);
                } catch (e) {
                }
    
                return validatorsArr.length ? validatorsArr[0].autoFillSuffix : null;
            },
            regex : function() {
                //获取输入框验证格式
                // 如果rule为真，验证方式为rule,否则为ext.regex
                if(this.rule) {
                    return this.rule;
                }
                var validatorsArr = [];
                try {
                    var ext = this.ext.replaceAll("'", '"');
                    validatorsArr = JSON.parse(ext);
                } catch (e) {
                }
                
                return validatorsArr.length ? validatorsArr[0].regex.replace('\\\\', '\\') : null;
            },
            regexErrorTip : function() {
                // 错误提示ruleMsg||ext.msg
                if(this.ruleMsg) {
                    return this.ruleMsg;
                }
                var validatorsArr = [];
                try {
                    var ext = this.ext.replaceAll("'", '"');
                    validatorsArr = JSON.parse(ext);
                } catch (e) {
                }
                
                return validatorsArr.length ? validatorsArr[0].msg : null;
            }
        },
        methods : {
            // 点击图标显示或隐藏密码
            togglePwdVisiblitiy : function() {
                this.pwdVisible = !this.pwdVisible;
            },
            //点击列表改变value的值，隐藏列表，触发验证
            autoFill : function(suffix) {
                this.value = this.value.split('@')[0] + suffix;
                $(this.$refs.autofill).hide();
    
                this.$emit('userchange', this.value, this);
            },
            discardFunctions : function() {
                //this.disabled为真，输入框置为不可用
                $(this.$refs.input).attr('type', 'text').prop('disabled', true);
                this.regexDisabled = true;
            },
            setType : function(type) {
                //设置输入框的类型
                this.type = type;
                $(this.$refs.input).attr('type', this.type);
            },
            setValue : function(v) {
                // 设置输入框的值
                this.value = v;
            },
            getValue : function() {
                //获取输入框的值
                return this.value;
            },
            remove : function() {
                //不显示组件
                this.exist = false;
            },
            focus : function() {
                $(this.$refs.input).focus();
                return this;
            },
            blur : function() {
                //输入框失去焦点时隐藏自动填充列表
                $(this.$refs.input).blur();
                var me = this;
                setTimeout(function() {
                    $(me.$refs.autofill).hide();
                }, 100);
                return this;
            },
            trigger : function(eName) {
                // 触发事件
                $(this.$refs.input).trigger(eName);
                return this;
            },
            resetValue : function(e,blur) {
                // 清空input的值
                this.value = '';
                !blur && $(this.$refs.input).focus();
                if (e) this.$emit('userchange', this.value, this);
                return this;
            },
            changeVerifyCode : function() {
                //改变图片验证码，清空input的值，触发获取新的验证码
                this.resetValue();
                this.$emit('changeverifycode');
            },
            _changeVerifyCode : function() {
                // 获取新的验证码
                this.$emit('changeverifycode');
            },
            onfocus : function() {
                //输入框焦点定义函数focus
                clearTimeout(this._pid);
                this.focused = true;
                this.$emit('focus',this.value,this);
            },
            onblur : function() {
                //失去焦点触发定义的函数blur
                var me = this;
                clearTimeout(this._pid);
                this._pid = setTimeout(function() {
                    me.focused = false;
                    $(me.$refs.autofill).hide();
                }, 100);
                this.$emit('blur',this.value,this);
                this.handleBlur && this.handleBlur();
            }
        },
        components : {},
        mounted : function() {
            //设置input类型
            this.setType(this.type);
            //输入框不可用
            if(this.disabled) {
                this.discardFunctions();
            }
            
            //初始外部值
            if (this.externalValue) this.setValue(this.externalValue);
            
            var me = this;
            // 避免触发两次
            var _userchange = debounce(function(){
                me.$emit('userchange', me.value, me);
                me.handleChange && me.handleChange(me.value, me);
            
                if (me.$refs.autofill) {
                    $(me.$refs.autofill)[me.value ? 'show' : 'hide']();
                }
            }, 300, true);
            $(this.$refs.input).keyup(function() {
                _userchange();
                //me.$emit('userchange', me.value);
            });
            $(this.$refs.input).on('input propertychange', function() {
                _userchange();
                //me.$emit('userchange', me.value);
            });
            
        }
    };
</script>

<style lang="less" rel="stylesheet/less" scoped>
    .field {
        position : relative;
    }
    .am-icon-clear.am-icon {
        visibility: visible !important;
    }
    
    .verify-code-wrapper {
        position : relative;
        .verify-code {
            width: .80rem;
            height: .25rem;
            background-repeat: no-repeat;
            background-position: center center;
            background-size: contain;
        }
        .spin {
            margin-right : -.2rem;
            animation : spin .5s linear infinite;
            -webkit-animation: spin .5s linear infinite;
            -moz-animation: spin .5s linear infinite;
            -m-animation: spin .5s linear infinite;
        }
        .loading-tip:before {
            content : '加载中';
            color : #cccccc;
            position : absolute;
            left : -.3rem;
            top : .02rem;
            bottom : 0;
            margin : auto;
        }
    }
    
    .am-list-label.nowrap {
        white-space: nowrap;
    }
    .eye {
        width : .24rem!important;
        height : .24rem!important;
    }
    
    .auto-fill-popover {
        border-bottom : 1px #dddddd solid;
        border-left : 1px #dddddd solid;
        border-right : 1px #dddddd solid;
        position : absolute;
        display : none;
        right : 0;
        background-color: #fff;
        z-index : 999;
        top : .48rem;
        .auto-fill-item {
            padding : .08rem;
            color : #666666;
            padding-left : .3rem;
            overflow : hidden;
            text-overflow : ellipsis;
            white-space: nowrap;
            font-size : .14rem;
            &:active {
                background-color: #dddddd;
            }
        }
    }
    .am-list-item input[disabled]::-webkit-input-placeholder{
        color:transparent !important;
    }
    .am-list-item input[disabled]::-moz-placeholder{
        color:transparent !important;
    }


</style>
