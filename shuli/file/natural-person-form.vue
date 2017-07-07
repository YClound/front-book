<template>
    <div class="am-npf-wrapper">
        <!-- 头部显示 -->
        <slot name="head"></slot>
        
        <!-- 自然人表单信息（姓名、手机号码、身份证号码） -->
        <div class="am-list form">
            <div class="am-list-body">
                <slot name="prefield"></slot>
        
                <!--form body-->
                <template v-for="cfg in configs">
                    
                    <template v-if="cfg.type == 'nameUser'">
                        <text-field :label-width='.8' ref="nameUser" mandatory @userchange="nameChanged" type="text" :label="cfg.label || '机主姓名'"
                                    name="nameUser" :placeholder="cfg.placeholder || '请填写姓名'"
                                    :max-length="24"
                                    :disabled="!nameModifiable || NAME_DESENSITIZED" :show="isShowName">
                        </text-field>
                    </template>
                    <template v-else-if="cfg.type == 'idCardUser'">
                        <text-field :label-width='.8' ref="idCardUser" mandatory @userchange="idCardChanged" type="text"
                                    :label="cfg.label || '身份证号码'"
                                    name="idCardUser"
                                    :placeholder="cfg.placeholder || '请填写身份证号码'" :max-length="18" :disabled="!idCardModifiable || ID_DESENSITIZED" :show="isShowIdcard">
                        </text-field>
                    
                    </template>
                    <template v-else-if="cfg.type == 'phoneUser'">
                        <text-field :label-width='.8' ref="phoneUser" mandatory @userchange="phoneChanged" type="tel" :label="cfg.label || '手机号码'"
                                    name="phoneUser" :placeholder="cfg.placeholder || '请填写手机号码'" :max-length="11"
                                    :disabled="!phoneModifiable || PHONE_DESENSITIZED" :show="isShowPhone" my-class="am-phone">
                        </text-field>
                    </template>
                
                </template>
                <slot name="afterfield"></slot>
            </div>
        </div>
        <!-- 表单错误和按钮 -->
        <slot name="tail"></slot>
    </div>
</template>

<script>
    import textField from './form/text-field.vue';

    var defs = [{
        type : 'nameUser',
        label : '机主姓名',
        disabled : false
    }, {
        type : 'idCardUser',
        label : '身份证号码',
        disabled : false
    }, {
        type : 'phoneUser',
        label : '手机号码',
        disabled : false
    }];
    
    export default {
        props : {
            name : String,
            // 需要校验的字段
            checkFields : {
                type : Array,
                default : () => ['name','phone','idcard']
            },
            config : {
                type : Array,
                default : defs
            }
        },
        data () {
            return {
                NAME_DESENSITIZED: false,//输入框是否可用
                ID_DESENSITIZED: false,
                PHONE_DESENSITIZED: false,
                showNpfFields:true,
                isShowName:true, //是否显示字段
                isShowPhone:true,
                isShowIdcard:true,
                valid_name:false,//验证自然人信息
                valid_phone:false,
                valid_idcard:false,
                isChecked : false,
                errorMsg : '',//错误信息
                nameModifiable : true, //是否可改
                idCardModifiable : true,
                phoneModifiable : true,
                nameUser : '',
                idCardUser : '',
                phoneUser : ''
            }
        },
        computed : {
            dirty: function () {
                return this.nameUser || this.phoneUser || this.idCardUser;
            },
            //使用父组件传过来的config覆盖defs的值，获取configs数组
            configs : function() {
                var configArr = [];
                var config = this.config || [];
                defs.forEach(function(v, i) {
                    var cfg = config[i];
                    if (cfg && v.type != cfg.type) {
                        delete v.label;
                    }
                    var target = $.extend(v, cfg || {});
                    configArr.push(target);
                });

                return configArr;
            }
        },
        methods : {
            //添加class
            addClass: function (cls) {
                var field = $(this.$el);

                field.addClass(cls);
            },
            //隐藏相应的输入框
            hideField : function(field) {
                this.showNpfFields = false;
                if ('name' === field) {
                    this.isShowName = false;
                } else if ('phone' === field) {
                    this.isShowPhone = false;
                } else if ('idcard' === field) {
                    this.isShowIdcard = false;
                }
            },
            // 加载表单
            loadFrom : function(url, callback) {
                var me = this;
                $.get(url, null, function(data) {
                    var msg = data.msg || {} , LOGIN_STEP = msg.LOGIN_STEP;

                    var DESENSITIZED = {
                        NAME_DESENSITIZED:msg.NAME_DESENSITIZED?true:false,
                        ID_DESENSITIZED:msg.ID_DESENSITIZED?true:false,
                        PHONE_DESENSITIZED:msg.PHONE_DESENSITIZED?true:false
                    };
                    callback ? callback(data,DESENSITIZED) : null;
                    !LOGIN_STEP && me.setData(msg);

                });
            },
            setData : function(extraData) {
                this.dirty = true;
                //console.log('setData',extraData)
                var me = this;
                if (extraData['phone'] || extraData['phoneUser'] ) {
                    extraData['phoneModifiable'] ? (me['phoneModifiable'] = true) : (me['phoneModifiable'] = false);
                }
                if (extraData['idCard']  || extraData['idCardUser']) {
                    extraData['idCardModifiable'] ? (me['idCardModifiable'] = true) :  (me['idCardModifiable'] = false);
                }
                if (extraData['name']  || extraData['nameUser'] ) {
                    extraData['nameModifiable'] ? (me['nameModifiable'] = true) :  (me['nameModifiable'] = false);
                }

                this.NAME_DESENSITIZED = extraData.NAME_DESENSITIZED?true:false;
                this.ID_DESENSITIZED = extraData.ID_DESENSITIZED?true:false;
                this.PHONE_DESENSITIZED = extraData.PHONE_DESENSITIZED?true:false;
                this.nameUser =  extraData['nameUser'] || extraData.name ||  '';
                this.idCardUser = extraData['idCardUser'] ||  extraData.idCard || '';
                this.phoneUser =  extraData['phoneUser'] ||  extraData.phone || '';
                this.$refs.nameUser[0].setValue(this.nameUser);
                this.$refs.idCardUser[0].setValue(this.idCardUser);
                this.$refs.phoneUser[0].setValue(this.phoneUser);

                this.nameChanged(this.nameUser);
                this.idCardChanged(this.idCardUser);
                this.phoneChanged(this.phoneUser);

            },
            // 获取填写的自然人信息
            getData : function() {
                return {
                    nameUser : this.nameUser,
                    idCardUser : this.idCardUser,
                    phoneUser : this.phoneUser,
                    nameModifiable : this.nameModifiable,
                    idCardModifiable : this.idCardModifiable,
                    phoneModifiable : this.phoneModifiable,
                    NAME_DESENSITIZED: this.NAME_DESENSITIZED,
                    ID_DESENSITIZED: this.ID_DESENSITIZED,
                    PHONE_DESENSITIZED: this.PHONE_DESENSITIZED

                };
            },
            // 验证姓名
            nameChanged : function(name) {
                this.nameUser = name || '';
                if (!this.NAME_DESENSITIZED && this.checkFields.indexOf('name')>-1 && (this.nameUser.length<2)) {
                    this.errorMsg = '请填写有效的姓名';
                    this.valid_name = false;
                }else {
                    this.errorMsg = '';
                    this.valid_name = true;
                }
                if(!this.showNpfFields) {
                    this.errorMsg = '';
                    this.valid_name = true;
                }
                this._validate();
            },
            //验证身份证号码
            idCardChanged : function(idCard) {
                this.idCardUser = idCard || '';
                if (!this.ID_DESENSITIZED && this.checkFields.indexOf('idcard')>-1 && (!/(^\d{17}(\d|x|X)$)|^(\d{15}$)/.test(this.idCardUser))) {
                    this.errorMsg = '身份证格式有误';
                    this.valid_idcard = false;
                }else {
                    this.errorMsg = '';
                    this.valid_idcard = true;
                }
                if(!this.showNpfFields) {
                    this.errorMsg = '';
                    this.valid_idcard = true;
                }
                this._validate();
            },
            //验证手机
            phoneChanged : function(phone) {
                var isChange = phone && this.phoneUser && (phone!=this.phoneUser);
                this.phoneUser = phone || '';
                isChange && this.$emit('phonechange',phone);
                if (!this.PHONE_DESENSITIZED && this.checkFields.indexOf('phone')>-1 && (!/^1\d{10}$/.test(this.phoneUser))) {
                    this.errorMsg = '';
                    if(phone && phone.length==11) {
                        this.errorMsg = '手机号码格式有误';
                    }
                    this.valid_phone = false;
                }else {
                    this.errorMsg = '';
                    this.valid_phone = true;
                }
                this._validate();
            },
            _validate : function() {
                var flag = false;
                var showNpfFields = this.showNpfFields;
                var data = this.getData();

                flag = this.valid_name && this.valid_phone && this.valid_idcard;

                if(!this.showNpfFields) {
                    flag = this.valid_phone;
                }
                //console.log('checkFields',this.checkFields,this.showNpfFields)
                //console.log(this.valid_name, this.valid_idcard, this.valid_phone , this.errorMsg)
                //验证通过错误信息清空
                if(flag) {
                    this.$emit('complete',this.getData());
                }else {
                    //验证不通过，显示错误信息
                    if(!this.errorMsg && this.dirty) {
                        if(!this.valid_name && showNpfFields) {
                            this.$emit('incomplete','请填写有效的姓名',data);
                            return;
                        }
                        if(!this.valid_idcard && showNpfFields) {
                            this.$emit('incomplete','身份证格式有误',data);
                            return;
                        }
                        if(!this.valid_phone) {

                            if(!this.phoneUser) {
                                this.$emit('incomplete','请填写手机号码',data);
                            }else if(this.phoneUser.length==11) {
                                this.$emit('incomplete','手机号码格式有误',data);
                            }else {
                                this.$emit('incomplete','',data);
                            }
                            return;

                        }
                    }else {
                        this.$emit('incomplete',this.errorMsg,data);
                    }

                }
            }
        },
        components : {textField}
    };
</script>

<style lang="less" rel="stylesheet/less" scoped>
</style>
