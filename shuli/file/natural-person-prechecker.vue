<template>
    <div>
        <natural-person-form ref="npf" v-if="!completed" v-show="required" @complete="complete" @incomplete="incomplete"
                             :config="config">
            <!-- 顶部信息 -->
            <div slot="head" class="header" align="center">
                <section class="safety-msg cust-certification"><span class="i-login-safety">
                <img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB8AAAAiCAYAAACnSgJKAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyNpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChNYWNpbnRvc2gpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkQ0OUEzNjg3NkZERDExRTU4MEY1ODM2MTJCRTg2NERBIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkQ0OUEzNjg4NkZERDExRTU4MEY1ODM2MTJCRTg2NERBIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6RDQ5QTM2ODU2RkREMTFFNTgwRjU4MzYxMkJFODY0REEiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6RDQ5QTM2ODY2RkREMTFFNTgwRjU4MzYxMkJFODY0REEiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz7SIuATAAADGklEQVR42ryYa0sUURjHZ9312tK6VitJRRFF28VuZlL2QqkgikCiBD9A9KJ30TfpIxSkUaAVRS+8QPf7HZQupGurdpHWytXd3P4P/A8chpnZ2VlnD/xQZ86c/3PO8zznPMeA0fPT8NACoAlsAGPgXu5kdKHQQco8CMs3x8l2cBh0BK5MB/0WrwZdYA8IgYdARHfKcxhQ7Zd4AzgNNoMcuAoegR4gSx4HZ2DAKve+y+/zCnAAtIIa8A1cBzNanzA4BurBLHgMBhEHf72Ki2gzaAFR/v1Mggv8s1nFvfxG3v8BT8Q1MCLlVrxKE41QNAGGwJSL1awF+8FGkAFp8FZcBCPG7cQjtHw3l1FER8F9kPSQFVGmY5wxkmFaPhdjYMicEt8i6UKfloNPjORJo/gmY25jWtbQiF+gW4mfBzEwTFG3O88prtAlzi5fZq0Du5g5X8ro4zBf3ipAWNpqRniFi76Sjh/BDe4RMRHMah2Chv9tQV+KrLZkoRKIq40tp36ZpQFLSiBeTq05JT7DB0tLIB6mVlqJT9MXkRKIRyieUj7+TvE6i86dIN9hcdbm+aRFGtZS64ea+Tj345U2hYPXZvVtjFoTauZjfCAvKiUYtM6XHQY/x58XTN84GdNArYQecEkuz1of/V3PgkROvKReTAwz5+M+im/i3j4iNZ8u/hLMc/8N+yAcYhUkE3xlLqMk3T7QH80+iDfyDJCa4LNVDTfI2TcyJRarVXJCEpRDWPKclbhULO84+4NFppne2rWK6I1T9SrHaoobS1OeQRPcSOYd+uxgoP0GvWrWduIi3Mvaq5UBaNe6wUWHQkJE23hw3YTwhJu6/T0LxjRL4jUellrKpiMUHoDwi0IuDf3gKVOjg9Womxakj9s14QG73LNrspR9jNB94ChYAR7o1YipLeds61gk9kH4tVPiG3kMuM04OMQAFBfckVPJdMFo4Z0ty7PiGoS/FntdUk1ET3B2YrTMSPy4nvV+kDEi1W8/hDOLcVczbxZtFKtiSZRlqo3IikB0yu1gAY//HFjGy+NWXjDuQnS00EH+CzAA6vrjpfY84QYAAAAASUVORK5CYII="></span>通过VeriSign国际安全认证
                </section>
            </div>
            <!-- 错误信息展示 -->
            <div slot="tail" class="am-wingblank am-ft-12 rem5">
                <div class="am-whitespace rem5"></div>
                <div class="error">{{errorMsg}}</div>
                <div class="am-whitespace rem5"></div>
            </div>
            <!-- 下一步按钮 -->
            <div slot="tail" class="am-wingblank am-ft-12 rem5">
                <button type="button" class="am-button cust-primary-button" @click="commit"
                        :disabled="!valid">下一步
                </button>
            </div>
        </natural-person-form>
        <slot v-if="completed"></slot>
    </div>
</template>

<script>
    import naturalPersonForm from './natural-person-form.vue';
    import textField from './form/text-field.vue'
    
    export default {
        props : {
            name : String
        },
        data () {
            return {
                show : false,
                completed : false,
                required : false,
                errorMsg : '',
                valid : false,
                config : [{
                    type : 'nameUser',
                    label : '姓名',
                    placeholder : '请填写姓名'
                }, {
                    type : 'phoneUser',
                    label : '手机号码',
                    placeholder : '请填写手机号码'
                }, {
                    type : 'idCardUser',
                    label : '身份证号码',
                    placeholder : '请填写身份证号码'
                }]//自定义自然人信息
            }
        },
        methods : {
            // 提交表单
            commit : function() {
                var me = this;
                $.post('/metadata/submitNaturalPersonInfo.json', this.$refs.npf.getData(), function(data) {
                    if (data.success) {
                        //存储自然人表单信息对象
                        Utils.store.setData('np_data', me.$refs.npf.getData());
                        Utils.store.setData('np_filled', {p : true});
                        //隐藏自然人表单
                        me._completeCollect();
                    }
                });
            },
            complete : function() {
                this.errorMsg = '';
                this.valid = true;
            },
            incomplete : function(errorMsg) {
                this.errorMsg = errorMsg;
                this.valid = false;
            },
            // 完成自然人数据采集，显示下一页
            _completeCollect : function() {
                this.completed = true;
                var me = this;
                setTimeout(function() {
                    me.$emit('completed');
                }, 1);
            }
        },
        mounted : function() {
            var me = this;
            var npf = this.$refs.npf; //自然人表单
            
            //查看用户是否已经完成了自然人信息采集
            if (Utils.store.getData('np_filled')) {
                //user has completed this form before
                this._completeCollect();
                return;
            }
            
            // 检验自然人信息是否必须，需要的话存储信息，不需要则显示下一步
            $.ajax({
                type : 'get',
                noMask : true,
                url : '/metadata/checkRequireNaturalPerson.json',
                success : function(data) {
                    me.required = data.success;
                    if (data.success) {//required
                        npf.setData(data.msg.extraData);
                        me.$emit('formInited');
                    } else {
                        Utils.store.setData('np_filled', {p : true});
                        me._completeCollect();
                    }
                }
            });
        },
        components : {naturalPersonForm, textField}
    };
</script>

<style lang="less" rel="stylesheet/less" scoped>
    .header {
        background-color: white;
        display: block;
        color: #999999;
        font-size: .12rem;
        padding-top: .15rem;
        padding-bottom: .15rem;
        .safety-msg {
            img {
                height: .18rem;
                vertical-align: middle;
                margin: 0 .02rem .01rem 0;
            }
        }
    }
    
    .error {
        color: red;
        height: .18rem;
    }
</style>
