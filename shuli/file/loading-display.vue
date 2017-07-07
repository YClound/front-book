<template>
    <div class="loading-display-wrapper" :class="myClass">
        <!-- 百分比 -->
        <div class="percentage flex-1" v-if="state=='loading'">
            <div class="am-ft-center am-ft-blue">
                <div class="outside">
                    <div class="cicle turn"></div>
                    <span>{{percentage}}</span>%
                </div>
                <p>数据获取中</p>
                <div class="am-ft-gray am-ft-12">预计还需{{waitSeconds}}秒，请稍候...</div>
            </div>
        </div>

        <div class="info flex-1" :class="myResultClass" v-if="state=='success'">
            <!--name-->
            <slot></slot>
        </div>
        <slot name="fail">
            <div class="failure flex-1" v-if="state=='fail'">
                <div class="am-result">
                    <div class="am-result-icon am-icon">
                        <img src="../../images/accufund/fail.png" alt="">
                    </div>
                    <div class="am-whitespace rem5"></div>
                    <div class="am-result-main">查询失败</div>
                    <!--remove it TODO-->
                    <div class="am-result-brief"></div>
                </div>
            </div>
        </slot>
        <slot name="company">
            <company-info></company-info>
        </slot>

        
        <!--infinate polling-->
        <task-poller ref="taskPoller" :max-poll-times="1000" :no-mask="noMask"></task-poller>
    </div>
</template>

<script>
    import taskPoller from './task-poller.vue';
    import companyInfo from './company-info.vue';
    import dateformat from 'dateformat';
    
    export default {
        props : {
            myClass: {
                type: String,
                default: ''
            },
            noMask : {
                type : Boolean,
                default : true
            },
            myResultClass : {
                type : String,
                default : 'am-result'
            },
            pullDataUrl : {
                type : String,
                default : '/metadata/pullBaseInfo.json'
            }
        },
        data () {
            return {
                state : 'loading', //loading, success, fail
                leftSeconds : 1,
                totalSeconds : 1,
                _pid : null,
                now: new Date().getTime()
            }
        },
        methods : {
            getResultValue : function(key) {
                if ('time' == key) {
                    return dateformat(new Date(), 'yyyy-mm-dd');
                }
                
                var v = this.result[key];
                if (key === 'depositAmount' || key === 'amount' || key === 'depositBase' || key === 'basicExpenditure') {
                    return v ? v / 100 : '';
                }
                if (key === 'depositRate') {
                    return v ? v + '%' : '';
                }
                if (key === 'region') return v + '公积金';
                return v;
            },
            _startCountDown : function() {
                var me = this;
                this._pid = setInterval(function() {
                    me.leftSeconds--;
                }, 1000);
            },
            _boostCountDown : function(callback) {
                var me = this;
                clearInterval(this._pid);
                var pid = setInterval(function() {
                    me.leftSeconds--;
                    if (me.leftSeconds < 0) {
                        clearInterval(pid);
                        callback ? callback() : null;
                    }
                }, 10);
            }
        },
        computed : {
            waitSeconds : function() {
                return Math.max(0, this.leftSeconds);
            },
            percentage : function() {
                var v = Math.floor((this.totalSeconds - Math.max(0, this.leftSeconds)) / this.totalSeconds * 100);

                return v===100?99:v;
            }
        },
        components : {companyInfo, taskPoller},
        mounted : function() {
            var me = this;
            var poller = this.$refs.taskPoller;
            var waitSecond = this.$route.params.waitSecond;
            // 默认100s
            waitSecond = (waitSecond==undefined || waitSecond == 'undefined')?100:waitSecond;
            this.totalSeconds = parseInt(waitSecond);
            this.leftSeconds = this.totalSeconds;
            
            this._startCountDown();

            var dateStart = new Date().getTime();

            poller.start(this.pullDataUrl, {
                salt : this.$route.params.salt,
                sign : this.$route.params.sign
            }, function(data) {
                data.msg = data.msg || {};
                data.msg.extraData = data.msg.extraData || {};

                if (!data.success) {
                    poller.stop();
                    me._boostCountDown(function() {
                        if(me.state != 'loading')return;
                        me.state = 'fail';
                        data.msg.extraData.itemName = data.msg.itemName;
                        me.$emit('fail', '获取失败', data.msg.extraData || {});
                    });
                    return true;
                }

                me.now = new Date().getTime();
                
                if (data.msg.status == 'WAIT') {
                    // 大于10分钟 表示超时
                    if((me.now - parseInt(dateStart)) > 600000 ) {
                        poller.stop();
                        me._boostCountDown(function() {
                            if(me.state != 'loading')return;
                            me.state = 'fail';
                            me.$emit('fail','系统超时，请稍后再试。', data.msg.extraData || {});
                        });
                        return true;
                    }else {
                        // 继续轮询
                        return false;
                    }
                } else if (data.msg.status == 'COMPLETE') {
                    poller.stop();
                    me._boostCountDown(function() {
                        if(me.state != 'loading')return;
                        me.state = 'success';
                        me.$emit('complete', data.msg.info, data.msg.extraData || {});
                    });
                    return true;
                } else {
                    poller.stop();
                    me._boostCountDown(function() {
                        if(me.state != 'loading')return;
                        me.state = 'fail';
                        me.$emit('fail', '网络走神了', data.msg.extraData || {});
                    });
                    return true;
                }

            });
        }
    };
</script>

<style lang="less" rel="stylesheet/less" scoped>
    .percentage .am-ft-center {
        padding-top: 1.15rem;
    }
    
    .outside {
        position: relative;
        margin: 0 auto .15rem;
        width: 1.25rem;
        height: 1.25rem;
        line-height: 1.25rem;
        overflow: hidden;
    }
    
    .cicle {
        position: absolute;
        z-index: -1;
        left: 0;
        width: 1.25rem;
        height: 1.25rem;
        background: url(../../images/ring.png) 0 0 no-repeat;
        background-size: 1.25rem;
        border-radius: 50%;
    }
    
    .turn {
        animation: turning infinite 0.75s linear;
        -webkit-animation: turning infinite 0.75s linear;
    }
    
    @keyframes turning {
        0% {
            transform: rotate(0deg);
        }
        100% {
            transform: rotate(360deg);
        }
    }
    
    @-webkit-keyframes turning {
        0% {
            -webkit-transform: rotate(0deg);
        }
        100% {
            -webkit-transform: rotate(360deg);
        }
    }
    
    /*select result list*/
    .am-result-icon img {
        width: inherit;
    }
    
    .am-result-brief {
        color: #C5C5C5 !important;
    }
    
    /*select error*/
    .am-result {
        box-shadow : 0rem 0rem .03rem #dadada;
        overflow: hidden;
        min-height: .3rem;
        padding: .25rem .1rem;
        background: -webkit-gradient(linear, left top, left bottom, from(#e5e5e5), to(#e5e5e5)) bottom left no-repeat, #fff;
        background: -webkit-linear-gradient(270deg, #e5e5e5, #e5e5e5, rgba(229, 229, 229, 0)) bottom left no-repeat, #fff;
        background: linear-gradient(180deg, #e5e5e5, #e5e5e5, rgba(229, 229, 229, 0)) bottom left no-repeat, #fff;
        -webkit-background-size: 100% .01rem;
        color: #000;
        font-size: .21rem;
        text-align: center;
    }
    
    .am-result .am-result-main {
        margin-top: .1rem;
        color: #333;
        font-size: .16rem;
        line-height: .16rem;
    }
    
    .am-result .am-result-sub {
        margin-top: .01rem;
        margin-bottom: -.05rem;
        font-size: .25rem;
        font-family: sans-serif;
    }
    
    .am-result .am-result-icon.am-icon {
        display: block;
        width: .6rem;
        height: .6rem;
        margin: 0 auto;
        z-index: 1;
    }
    
    .am-result .am-result-brief {
        margin-top: .07rem;
        font-size: .14rem;
        color: #666;
        font-weight: 400;
    }
</style>
