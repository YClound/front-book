<template>
    <div class="wrapper login-wrapper" role="form">
        <div class="adjust-wrapper">
            <form id="login-form" class="login-form">
                <div class="am-list form login-option">
                    <div class="am-list-body">
                        <!-- 渠道名称 -->
                        <div class="am-list-item am-input-autoclear">
                            <div style="width:.8rem;" class="am-list-label nowrap cust-field-label">{{channelLabel}}</div>
                            <div class="am-list-control">
                                <input type="text" name="cityName" @click="backToList" readonly
                                       :value="loginInfo.itemName + channelSuffix">
                            </div>
                            <div v-if="!loginTypeCodeSpecified" class="am-list-arrow" @click="backToList">
                                <span class="am-icon arrow horizontal" v-if="CAN_BACK_CITY_LIST"></span>
                            </div>
                        </div>
                        <!--<template v-if="!hasError || (hasError && showOperationZoneIfError)">-->
                        <!-- 登录方式选择 登录方式不止一种-->
                        <div class="am-list-item am-input-autoclear"
                             v-if="loginInfo.loginTypes && loginInfo.loginTypes.length > 1">
                            <div style="width:.8rem;" class="am-list-label nowrap cust-field-label">登录方式</div>
                            <div class="am-list-control login-type">
                                <select name="loginType" id="select" @change="changeLoginType" v-model="loginType">
                                    <template v-for="loginType in loginInfo.loginTypes">
                                        <option v-if="loginType.loginTypeStatus == 'ENABLE'"
                                                :value="loginType.loginTypeCode">{{loginType.loginTypeName}}
                                        </option>
                                    </template>
                                </select>
                            </div>
                            <label for="select">
                                <div class="am-list-arrow">
                                    <span class="am-icon arrow vertical"></span>
                                </div>
                            </label>
                        </div>
                        <!--</template>-->
                    </div>
                    <div class="am-list form login-form-body">
                        <div class="am-list-body">
                            <template v-if="!hasError" v-for="(field, idx) in loginInfo.loginTypeInfos">
                                <smscode-field v-if="field.loginFieldCode=='SMS_CODE'"
                                               @userchange="_checkValid"
                                               mandatory
                                               :label-width=".8"
                                               :idx="idx"
                                               :key="idx + field.loginFieldName + loginType"
                                               ref="formField"
                                               :label="field.loginFieldName"
                                               :type="field.loginFieldType"
                                               :placeholder="field.loginFieldDesc"
                                               :ext="field.loginFieldExt"
                                               :handle-send-sms="sendSms"
                                               :name="field.loginFieldCode"></smscode-field>
                                <select-field v-else-if="field.loginFieldType=='select'"
                                              ref="formField"
                                              :label-width=".8"
                                              :idx="idx"
                                              :key="idx + field.loginFieldName + loginType"
                                              @userchange="_checkValid"
                                              mandatory
                                              :type="field.loginFieldType"
                                              :label="field.loginFieldName"
                                              :option="field.loginFieldExt"
                                              :name="field.loginFieldCode"></select-field>
                                <text-field v-else
                                            :external-value="field.value"
                                            ref="formField"
                                            :label-width=".8"
                                            mandatory
                                            :idx="idx"
                                            :key="field.loginFieldName + loginType"
                                            @userchange="_checkValid"
                                            @changeverifycode="changeverifycode(field)"
                                            :item-code="itemCode"
                                            :login-type="loginType"
                                            :type="field.loginFieldType"
                                            :label="field.loginFieldName"
                                            :name="field.loginFieldCode"
                                            :placeholder="field.loginFieldDesc"
                                            :has-verify-code="field.hasVerifyCode"
                                            :verify-code="field.verifyCode"
                                            :verify-format="field.verifyFormat"
                                            :ext="field.loginFieldExt"></text-field>
                            </template>
                        </div>
                    </div>
                </div>
                <slot v-if="hasError" :me="this" name="service-unavailable-zone">
                    service unavailable!
                </slot>
            </form>
            <!--忘记密码-->
            <template v-if="forgets.length && !hasError">
                <div class="am-ft-12" style="text-align: right;padding: 0 .12rem;margin-top: .12rem;">
                    <template v-for="(item, idx) in forgets">
                        <a href="javascript:;" v-if="idx==1" style="margin: 0 .03rem">/</a>
                        <a :href="item.forgetUrl" v-text="item.forget"></a>
                    </template>
                </div>
            </template>
            <!--operation zone-->
            <template v-if="!hasError || (hasError && showOperationZoneIfError)">
                <div class="error"></div>
                <div class="am-whitespace rem5"></div>
                <div class="am-wingblank login-error am-ft-12 rem5" v-show="!hasError">
                    <div class="tip" v-html="tip"></div>
                    <div class="login-error-main" v-html="errorMsg"></div>
                    <div class="am-whitespace rem5"></div>
                </div>
                <div class="am-wingblank am-ft-14 fn-clear rem5">
                    <div class="am-checkbox mini" style="position:relative;">
                        <input :disabled="hasError" v-model="agreed" @click="_preCheckValid($event)" id="agree" type="checkbox"/>
                        <label style="position:absolute;top:0.11rem;" for="agree" class="icon-check"
                               @click="_checkValid"></label>
                        <label style="margin-left:.25rem;" class="am-ft-md" for="agree">同意
                            <router-link class="agreement-link" to="/agreement">
                                <span>{{autharrTitle}}</span>
                            </router-link>
                        </label>
                    </div>
                    <!-- 更多帮助 -->
                    <router-link :to="'/help/' + itemCode" class="fn-right more-help cust-more-help">更多帮助</router-link>
                </div>
                <div class="am-whitespace rem5"></div>
                <!-- 下一步 -->
                <div class="am-wingblank rem5">
                    <button ref="submitButton" :disabled="hasError || !valid" @click="commitLogin" type="button"
                            class="am-button submit-button cust-primary-button">{{submitBtnText}}
                    </button>
                </div>
                <div class="am-whitespace rem5"></div>
                <slot name="below-ele"></slot>
                <!-- 温馨提示 -->
                <div class="am-wingblank login-remind rem5" v-show="loginInfo.itemDescSize">
                    <span class="login-remind-title cust-remind-title">温馨提示:</span>
                    <ul class="login-remind-list">
                        <li class="login-remind-item cust-remind-item" v-html="loginDesc"
                            v-for="loginDesc in loginInfo.itemDescs"></li>
                    </ul>
                </div>
            </template>
        </div>


        <company-info v-show="showCompanyInfo" ref="footer" adjust-to=".adjust-wrapper">
            <slot name="company-info" :me="this"></slot>
        </company-info>

        <!-- 循环请求数据 -->
        <login-state-poller :auto-jump-to-verify="autoJumpToVerify" @verifycodeRequired="verifycodeRequired" ref="poller"></login-state-poller>
    </div>
</template>

<script>
    import companyInfo from './company-info.vue';
    import selectField from './form/select-field.vue';
    import smscodeField from './form/smscode-field.vue';
    import textField from './form/text-field.vue';
    import loginStatePoller from './login-state-poller.vue';

    export default {
        props : {
            expireSeconds : {
                type : Number,
                default : 120 //2 mins
            },
            autoJumpToVerify : {
                type : Boolean,
                default : true
            },
            channelSuffix : {
                type : String,
                default : ''
            },
            channelLabel : {
                type : String,
                default : '缴纳地'
            },
            submitBtnText : {
                type : String,
                default : '确 认'
            },
            showCompanyInfo : {
                type : Boolean,
                default : true
            },
            showOperationZoneIfError : {
                type : Boolean,
                default : true
            },
            listUrl : {
                type : String,
                default : 'dataitemlist.htm'
            },
            getLoginFormsUrl : {
                type : String,
                default : '/metadata/getLoginForms.json'
            },
            getLoginFieldsUrl : {
                type : String,
                default : '/metadata/queryLoginFormFields.json'
            },
            sendSmsCodeUrl : {
                type : String,
                default : '/metadata/sendSms.json'
            },
            changeVerifyCodeUrl : {
                type : String,
                default : '/metadata/acquireLoginVerifyCode.json'
            },
            submitLoginUrl : {
                type : String,
                default : '/metadata/submitLogin.json'
            },
            pollLoginStateUrl : {
                type : String,
                default : '/metadata/queryTaskStatus.json'
            }
        },
        data () {
            return {
                // 可以返回缴纳地列表
                CAN_BACK_CITY_LIST:true,
                // 错误码
                errorCode:'',
                forgets:[],
                // 是否需要引导页
                loadPage:'',
                // 引导页地址
                loadPageUrl:'',
                hasError : false,
                loginTypeCodeSpecified : false,
                loginType : '',
                autharrTitle : '',
                dataSource : '',
                itemCode : '',
                loginInfo : {
                    itemName : ''
                },
                orgCode : '',
                errorMsg : '',
                tip : '',
                valid : false,
                agreed : true,
                sendSms : function() {
                    var me = this.$parent;
                    var field = this;
                    me.errorMsg = '';
                    var formData = Utils.serialize($(me.$el).find('form'));
                    var flag = true;
                    //mandatory check
                    for (var key in formData) {
                        if (formData[key] == '' && key != this.name) {
                            flag = false;
                            continue;
                        }
                    }
                    //check if can send?

                    if (!flag) {
                        me.errorMsg = '请先填写完成其他项';
                        return false;
                    }

                    $.ajax({
                        url : me.sendSmsCodeUrl,
                        type : 'post',
                        data : {
                            smsJson : JSON.stringify({
                                step : '1',
                                params : $.extend(true, formData, {
                                    LOGIN_STEP : '1',
                                    LOGIN_SMS_CODE : ''
                                })
                            })
                        },
                        success : function(data) {
                            if (data.success) {
                                me.tip = `验证码已发送至您的手机${data.msg ? data.msg : ''},请注意查收`;
                            } else {
                                field.resetValue();
                                field.cooldownImmediately();
                                var msg = {//TODO
                                    errorMsg : data.msg
                                };
                                if (data.msg == '请输入正确的图片验证码') msg.name = 'EXT_BIZ_VERIFYCODE_ERROR';
                                else msg.name = '';

                                me.handleLoginError(msg);
                            }
                        }
                    });

                    return true;
                }
            }
        },
        watch : {
            errorMsg : function() {
                this.tip = '';
            }
        },
        methods : {
            verifycodeRequired : function() {
                this.$emit('verifycodeRequired');
            },
            // 返回listUrl
            backToList : function() {
                if (this.loginTypeCodeSpecified) return;
                if (!this.CAN_BACK_CITY_LIST) return;
                Utils.jumpTo(this.listUrl);
            },
            _preCheckValid : function(e) {
                var me = this;
                setTimeout(function() {
                    me._checkValid(e);
                }, 10);
            },
            //验证表单
            _checkValid : function(e) {
                // 没有勾选协议
                if (!this.agreed) {
                    this.valid = false;
                    return;
                }
                if (e && !this.agreed) return;

                var flag = true;
                var me = this;
                this.$refs.formField && this.$refs.formField.sort(function(o1, o2) {
                    return o1.$el.getAttribute('idx') > o2.$el.getAttribute('idx');
                });
                $.each(this.$refs.formField || [], function() {
                    if (!this.valid) {
                        flag = false;
                        me.errorMsg = this.errorMsg;
                        return false;//break
                    }
                });

                if (!flag) {
                    this.valid = false;
                    return;
                }

                this.valid = true;
                this.errorMsg = '';
            },
            findFieldsBy : function(fn) {
                return this.$refs.formField ? this.$refs.formField.filter(fn) : null;
            },
            handleLoginError : function(msg) {
                this.errorMsg = msg.errorMsg || msg.fixTips;

                var arr = this.findFieldsBy(function(item) {
                    return item.name.endsWith('VERIFY_CODE')
                });
                arr[0] && arr[0].changeVerifyCode();
                switch (msg.name) {
                    case 'EXT_BIZ_VERIFYCODE_ERROR':
                        break;
                    case 'EXT_BIZ_NAMEORPW_ERROR':
                        arr = this.findFieldsBy(function(item) {
                            return item.type == 'password'
                        });
                        arr[0] && arr[0].resetValue();
                        break;
                    case 'KNOWN_ERROR':
                        //refetch loginfields
                        var me = this;

                        if (!arr[0]) {
                            //验证码控件没找到，可能是由于邮箱账单的特殊机制引发的，就需要重新获取一遍登录字段
                            $.ajax({
                                url : this.getLoginFieldsUrl,
                                ignoreError : true,
                                type : 'get',
                                data : {
                                    loginTypeCode : this.loginType,
                                    REFRESH_CURRENT : true
                                },
                                success : function(data) {
                                    me.hasError = !data.success;
                                    me.loginInfo.loginTypeInfos = data.msg;

                                    setTimeout(function() {
                                        me.$refs.footer.adjust();
                                    }, 1);
                                }
                            });
                        }

                        break;
                }

                this.valid = false;

            },
            changeverifycode : function(field) {
                field.verifyCode = '';
                var me = this;

                me.errorCode = '';
                var extra = {LOGIN_STEP : '1'};
                $.ajax({
                    url : this.changeVerifyCodeUrl,
                    noMask : true,
                    ignoreError : true,
                    type : 'get',
                    data : {
                        formJson:JSON.stringify(extra),
                        itemCode : this.itemCode,
                        loginTypeCode : this.loginType
                    },
                    success : function(data) {
                        var msg = data.msg || {};
                        if (!data.success) {
                            me.errorCode = msg.code || '';
                            me.hasError = true;
                            return;
                        }
                        var verifyInfo = data.msg;
                        verifyInfo ? (field.verifyFormat = verifyInfo.verifyFormat) && (field.verifyCode = verifyInfo.verifyCode) : null;
                    }
                });
            },
            changeLoginType : function(e) {
                var me = this;

                $.ajax({
                    url : this.getLoginFieldsUrl,
                    ignoreError : true,
                    type : 'get',
                    data : {
                        loginTypeCode : e.target.value
                    },
                    success : function(data) {
                        me.hasError = !data.success;
                        me.setNpData(data.msg,function (loginTypeInfos) {
                            me.loginInfo.loginTypeInfos = loginTypeInfos;
                            me._checkValid();

                            me.errorMsg = '';
                            setTimeout(function() {
                                me.$refs.footer.adjust();
                            }, 1);
                        });

                    }
                });

            },
            // 提交表单
            commitLogin : function() {
                var me = this;
                var data = Utils.serialize($(this.$el).find('form'));
                delete data.cityName;
                delete data.loginType;
                data.LOGIN_STEP = data.LOGIN_STEP?data.LOGIN_STEP:'1';

                var poller = this.$refs.poller;
                $.post(this.submitLoginUrl, {
                    itemCode : this.itemCode,
                    loginTypeCode : this.loginType,
                    formJson : JSON.stringify(data)
                }, function(data) {
                    // 循环请求数据,根据返回的数据跳转地址
                    if (data.success) {
                        poller.start(me.pollLoginStateUrl, function(content) {
                            me.handleLoginError(content);
                        });
                    } else me.handleLoginError(data.msg);
                });

            },
            adjustFooter : function() {
                var me = this;
                setTimeout(function() {
                    me.$refs.footer.adjust();
                }, 1);
            },
            // 忘记用户名 忘记密码
            renderResetPwd : function (loginTypes) {
                var me = this;
                var item = '', ext = '';
                for (var i = 0; i<loginTypes.length; i++) {
                    item = loginTypes[i];
                    if(item.loginFieldCode == 'LOGIN_NAME' || item.loginFieldCode == 'LOGIN_PASSWORD') {
                        try {
                            var ext = item.loginFieldExt || '';
                            ext = ext.replaceAll("'", '"');
                            var arr = JSON.parse(ext);
                            var obj = arr[0] || {};
                            // 忘记密码字段
                            var forget = obj.forget;
                            var forgetUrl = obj.forgetUrl;
                            forget && forgetUrl && me.forgets.push({forget:forget,forgetUrl:forgetUrl})
                        }catch(e) {

                        }
                    }
                }
            },
            // 自然人信息回写
            setNpData:function (loginTypeInfos,callback) {
                // 官网挂了可能没有数据
                if(!loginTypeInfos) {
                    callback && callback([]);
                    return;
                }
                var store = Utils.store.getData('np_data') || {};
                // 获取自然人信息
                $.ajax({
                    noMask: true,
                    url: '/metadata/getExtraData.json',
                    success: function (res) {
                        var msg = res.msg || {};
                        var name = msg.name, phone = msg.phone, idCard = msg.idCard;
                        var nameUser = store['nameUser'], phoneUser = store['phoneUser'], idCardUser = store['idCardUser'];

                        $.each(loginTypeInfos, function (index,elem) {
                            var key = elem.naturalPersonCode;

                            if (key === 'nameUser') {
                                elem.value = nameUser || name || '';
                            } else if (key === 'phoneUser') {
                                elem.value = phoneUser || phone || '';
                            } else if (key === 'idCardUser') {
                                elem.value = idCardUser || idCard || '';
                            } else {

                            }
                        });

                        callback && callback(loginTypeInfos);
                    }
                })
            },
            startToLoad : function() {
                var me = this;
                this.__renderTime = new Date().getTime();
                // 获取itemCode
                this.__itemCode = this.$route.params.itemCode;
                this.hasError = false;

                this.itemCode = this.$route.params.itemCode;
                // 引导页
                me.loadPageUrl = '';
                me.loadPage = '';
                me.errorCode = '';

                // 判断能否返回缴纳地
                $.ajax({
                    noMask:true,
                    url:'/metadata/getPreferences.json',
                    success:function (res) {
                        var CAN_BACK_CITY_LIST = res.msg.CAN_BACK_CITY_LIST;
                        me.CAN_BACK_CITY_LIST = CAN_BACK_CITY_LIST!='false';
                    }
                });
                //获取邮箱信息
                $.get(this.getLoginFormsUrl, {
                    itemCode : this.itemCode
                }, function(data) {
                    data = data.msg || {};
                    var loginInfo = data.loginInfo || {};
                    var loginTypeInfos = loginInfo.loginTypeInfos || [];
                    //check closed?
                    if (data.available == false) {
                        Utils.showLoading(null, 500);
                        Utils.jumpTo('/');
                        return;
                    }
                    // 存储邮箱信息
                    me.setNpData(loginInfo.loginTypeInfos,function (loginTypeInfos) {
                        // 引导页
                        me.loadPageUrl = loginInfo.leadPageUrl || '';
                        me.loadPage = loginInfo.leadPage || '';
                        me.loginTypeCodeSpecified = data.loginTypeCodeSpecified;
                        me.autharrTitle = data.autharrTitle;
                        me.dataSource = data.dataSource;
                        me.loginInfo = data.loginInfo;
                        me.orgCode = data.orgCode;
                        me.forgets = [];
                        //check availability.
                        if (!data.loginInfo.loginTypeEnable) {
                            me.errorCode = loginInfo.verifyErrorCode || '';
                            me.hasError = true;
                        }
                        if (data.loginInfo && data.loginInfo.loginTypes && data.loginInfo.loginTypes.length) {

                            me.loginType = data.loginInfo.loginTypes[0].loginTypeCode;
                            //配置忘记密码的文字和跳转地址
                            me.renderResetPwd(data.loginInfo.loginTypeInfos || []);
                        }

                        me.agreed = true;
                        //me.hasError = false;
                        me.errorMsg = '';
                        me.$emit('loaded', data, {loginType:me.loginType});
                    });



                });
            }
        },
        beforeDestroy : function() {
            $('.loading-mask').remove();
        },
        activated : function() {
            this.$refs.footer.adjust();
            //not the save itemcode -------------------------- or ------------expired
            if (this.__itemCode != this.$route.params.itemCode || new Date().getTime() - this.__renderTime > 1000 * this.expireSeconds) {
                this.__itemCode = this.$route.params.itemCode;
                $(document.body).hide();
                this.startToLoad();
                return;
            }


        },
        components : {companyInfo, textField, smscodeField, selectField, loginStatePoller}
    };
</script>

<style lang="less" rel="stylesheet/less">
    .tip {
        color: #2d661e;
    }
    
    .login-error {
        color: red;
    }
    
    .am-checkbox label {
        font-size: .14rem !important;
    }
    
    .am-checkbox a {
        color: #666 !important;
    }
    
    .login-wrapper {
    }
    
    .login-form-body {
        margin-top : -1px;
    }
    
    .login-remind-list {
        padding-left: .20rem;
    }
    
    .login-remind-title {
        font-size: .15rem;
        color: #9E9E9E;
    }
    
    .login-remind-item {
        font-size: .13rem;
        list-style: outside disc;
        color: #9E9E9E;
    }
    
    .agreement-link {
        span {
            text-decoration: underline;
            color : #666666;
        }
        &:before {
            content : '《';
        }
        &:after {
            content : '》';
        }
    }

    .more-help {
        line-height: .24rem;
        font-size: .14rem;
    }
</style>
