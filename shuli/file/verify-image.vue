<template>
    <div class="image-verify-wrapper" :style="style" style="width: 2.8rem;">
        <div class="image-verify-content" :class="{'done':vc}" :style="'background-image:url(' + vc + ')'" @click="refresh">
            <div class="tip">{{tip}}</div>
        </div>
        <slot :me="this">
            <div class="image-verify-tip">
                <div class="">看不清楚？换一个（字母区分大小写）</div>
            </div>
        </slot>
    </div>
</template>

<script>
    //import aa from './aa.vue';
    export default {
        props : {
            auto:{
                type: Boolean,
                default:true
            },
            ajaxUrl:{
                type: String,
                default:''
            },
            width:{
                type: Number,
                default:2
            },
            height:{
                type: Number,
                default:1
            },
            style: String

        },
        data () {
            return {
                tip:'加载中...',
                // base64
                verifyCode : '',
                // png
                verifyFormat : '',
            }
        },
        computed: {
            //获取验证码图片
            vc : function() {
                if (this.verifyCode) return 'data:image/' + this.verifyFormat + ';base64,' + this.verifyCode;
                else return ''
            }
        },
        methods : {
            //刷新图片验证码
            refresh: function () {
                var me = this;

                me.$emit('refresh');
                this.tip = '加载中...';
                this.verifyCode = '';
                $.ajax({
                    url:this.ajaxUrl,
                    noMask:true,
                    success:function (res) {
                        var msg = res.msg || {};
                        if(res.success) {
                            me.verifyFormat = msg.imageFormat;
                            me.verifyCode = msg.imageContent;
                        }else {
                            me.tip = '验证码获取失败,请重试';
                            me.$emit('fail',res);
                        }
                    }
                });

            }
        },
        components : {},
        mounted : function() {
            this.auto && this.refresh();
        }
    };
</script>

<style lang="less" rel="stylesheet/less" scoped>
    .image-verify-wrapper {
        box-sizing: border-box;
        margin: 0 auto;
        /*background: #ccc;*/
        .image-verify-content {
            box-sizing: border-box;
            width: 2.2rem;
            height:1.1rem;
            position: relative;
            background-color: #fff;
            margin: 0 auto;
            background-position: center center;
            background-repeat: no-repeat;
            background-size: cover;
            border: 1px solid #ddd;
            min-width: 220px;
            min-height: 110px;

            .tip {
                text-align: center;
                position: absolute;
                color: #333;
                font-size: .16rem;
                width:100%;
                left:0;
                top:50%;
                transform: translateY(-50%);
                -webkit-transform: translateY(-50%);
            }
            &.done {
                .tip {
                    display: none;
                }
            }
        }
        .image-verify-tip {
            width: 2.2rem;
            margin:0 auto;
            min-width: 220px;
            text-align: center;
            color:#333;
            font-size: .12rem;
            background: #ccc;
            color:#333;
        }
        
        .image-verify-loader {
            position: absolute;
        }
    }
</style>
