<template>
    <span>
        <!-- 打开引导页面 -->
        <a v-show="text" @click="toggleGuide" href="javascript:void(0);" v-text="text" style="padding: 0.05rem;"></a>
        <!-- 引导页面 -->
        <div ref="guideWrapper" class="guide-wrapper collapsed">
            <div class="main" v-html="hdata">
            </div>

            <div align="center" class="cb-wrapper am-wingblank rem5 am-whitespace">
                <div class="am-checkbox mini">
                    <input :id="id" type="checkbox" :checked="remember" :name="id"  v-model="remember"  @change="rememberChange">
                    <label :for="id" class="icon-check"></label>
                    <label :for="id" class="am-ft-md" style="margin-left: 0.25rem;">下次登录不再提示</label>
                </div>
            </div>
            <button class="am-button submit-button cust-primary-button" @click="toggleGuide">知道了</button>
        </div>
    </span>
</template>

<script>
    //import aa from './aa.vue';
    
    export default {
        props : {
            // 是否显示不再提示
            isRemember: {
                type: Boolean,
                default:true
            },
            enable: {
                type: String,
                default: 'true'
            },
            // 默认是否自动展开
            auto: {
                type: Boolean,
                default: false
            },
            text: {
                type: String,
                default:'帐号获取指引'
            },
            // 引导页地址
            guideUrl: {
                type: String,
                default:''
            },
            guideUrls : {
                type : Object,
                default : function () {
                    return {}
                }
            },
            itemCode : String
        },
        data () {
            return {
                id:'_hide',
                remember : false,
                loaded : false,
                hdata : ''
            }
        },
        computed : {
           hasGuide: function () {

           }
        },
        methods : {
            // init checkbox
            rememberStatus:function () {
                var store = Utils.localStore.getData('guide_remember') || {},itemCode = this.itemCode;
                var result = store[itemCode];

                this.remember = result;

            },
            // checkbox change
            rememberChange: function (e) {
                var store = Utils.localStore.getData('guide_remember') || {} ,itemCode = this.itemCode;

                this.remember = e.currentTarget.checked;
                store[itemCode] = this.remember;
                Utils.localStore.setData('guide_remember',store);
            },
            toggleGuide : function() {
                var me = this;
                var url = this.guideUrls[this.itemCode] || this.guideUrl || '';
                var prefix = '';
                var uri = CDN_URL + prefix + url + '?' + new Date().getTime();
                // 假如传参则打开
                if(typeof arguments[0] == 'boolean') {
                    $(this.$refs.guideWrapper).removeClass('collapsed');
                }else {
                    $(this.$refs.guideWrapper).toggleClass('collapsed');
                }
                this.rememberStatus();
                // 展开
                if (!$(this.$refs.guideWrapper).hasClass('collapsed')) {
                    //load html
                     $.get(uri, function(data) {
                        me.hdata = data.replaceAll('CDN_URL', CDN_URL);
                        me.loaded = true;
                     });
                }
            },
            open: function () {
                // 不再提示 true 默认false {guide_remember:false}
                var store = Utils.localStore.getData('guide_remember') || {};
                var remember = store[this.itemCode] || this.remember;

                // 假如自动且没有勾选记住则打开
                if(this.auto && !remember && this.guideUrl) {
                    this.toggleGuide(true);
                }
                this.remember = remember;
            }
        },
        components : {},
        mounted: function () {
            var _guid =  function(){
                var S4 = function(){
                    return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
                };
                return (S4() + S4() + S4() + S4() + S4() + S4() + S4() + S4());
            };
            this.id = this.id + _guid();
        },
        activated:function () {
            this.loaded = false;
            this.remember = false;
            $(this.$refs.guideWrapper).addClass('collapsed');
            this.hdata = '';
        }
    };
</script>

<style lang="less" rel="stylesheet/less" scoped>
    .guide-wrapper {
        position : fixed;
        z-index : 10000;
        background-color: #fff;
        top : 0;
        left : 0;
        right : 0;
        bottom : 0;
        -webkit-transition: all .3s;
        -moz-transition: all .3s;
        -ms-transition: all .3s;
        -o-transition: all .3s;
        transition: all .3s;
        &.collapsed {
            top : 100%;
            bottom : -20rem;
        }
    
        .main {
            overflow-y : scroll;
            font-family: PingFangSC-Light;
            font-size: .15rem;
            color: #333333;
            letter-spacing: -0.0037rem;
            line-height: .24rem;
            text-align: left;
            position: absolute;
            left : 0;
            right : 0;
            top : 0;
            bottom : .85rem;
        }
    
        .cb-wrapper {
            position : absolute;
            bottom : 0.47rem;
            left : 0;
            right : 0;
            left : 0;
        }
        .submit-button {
            -webkit-border-radius:0!important;
            -moz-border-radius:0!important;
            border-radius:0;
            position : absolute;
            bottom : 0;
            left : 0;
        }
    }
</style>
<style lang="less" rel="stylesheet/less">
    .guide-wrapper {
        -webkit-user-select: text;
        user-select: text;
        -webkit-overflow-scrolling: touch;
        .step_headline {
            font-size: .18rem;
            text-align: center;
            padding-top: .15rem;
        }
        .step {
            padding-bottom: .1rem;
            padding-top: .1rem;
            padding-left: .1rem;
            padding-right: .1rem;


            .step1_img {
                content: url(../../images/email/pop3/Group1.png);
                background-size: .15rem;
                width: .15rem;
                height: .15rem;
                border-radius: 100%;
                float: left;
                margin-top: .03rem;
            }

            .step2_img {
                content: url(../../images/email/pop3/Group2.png);
                background-size: .15rem;
                width: .15rem;
                height: .15rem;
                border-radius: 100%;
                float: left;
                margin-top: .03rem;
            }

            .step3_img {
                content: url(../../images/email/pop3/Group3.png);
                background-size: .15rem;
                width: .15rem;
                height: .15rem;
                border-radius: 100%;
                float: left;
                margin-top: .03rem;
            }

            .step4_img {
                content: url(../../images/email/pop3/Group4.png);
                background-size: .15rem;
                width: .15rem;
                height: .15rem;
                border-radius: 100%;
                float: left;
                margin-top: .03rem;
            }

            .step5_img {
                content: url(../../images/email/pop3/Group5.png);
                background-size: .15rem;
                width: .15rem;
                height: .15rem;
                border-radius: 100%;
                float: left;
                margin-top: .03rem;
            }
            .main {
                padding: .12rem;
                font-family: PingFangSC-Light;
                font-size: .15rem;
                color: #333333;
                letter-spacing: -0.0037rem;
                line-height: .24rem;
                text-align: left;
            }
            .step_titile {
                padding-bottom: .1rem;
            }

            .step_titile_content {
                margin-left: .2rem;
            }
        }
    }
</style>
