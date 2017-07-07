<template>
    <div class="am-list-item">
        <div :style="'width:' + labelWidth + 'rem;'" class="am-list-label nowrap cust-field-label">{{label}}</div>
        <div class="am-list-control select-dropdown-wrap " :class="{'collapsed':collapsed}">
            <div @click="toggleCollapsed($event)" class="select-dropdown-input">
                <div class="am-list-arrow"><span class="am-icon arrow vertical" style="display: inline-block"></span></div>
                <input  ref="select" type="text" autocomplete="off" :name="name" v-model.trim="value"
                        :placeholder="placeholder"  :readonly="readonly" class="input" style="padding-right: 30px;" >
            </div>
            <div class="select-dropdown">
                <ul class="select-dropdown-list">
                    <li v-for="(item,idx) in opts" class="select-dropdown-item" @click.stop="change(item)"
                        :class="{'disabled':item.disabled,'active':item.label==value}">
                        <span href="javascript:;" :data-value="item.value" >{{item.label}}</span>
                    </li>
                </ul>
            </div>

        </div>


    </div>

</template>

<script>


    export default {
        props : {
            initialValue:String,//初始值
            mandatory : {//是否必填
                type : Boolean,
                default : false
            },
            labelWidth : {//label的宽度
                type : Number,
                default : 1
            },
            readonly: {//只读
                type: Boolean,
                default: true
            },
            requiredMsg:String,//报错信息
            placeholder: String,//placeholder
            label : String,//label名字
            name : String,//input的name
            option : {//option的值数组格式
                type : Array,
                value : function () {
                    return [];
                }
            }
        },
        data () {
            return {
                collapsed: true,
                errorMsg : '',
                focused : false,
                valid : true,
                value : '',
                guid : Math.random() + '_sel'
            }
        },
        computed : {
            //当依赖的值发生变化时，计算属性
            valid : function() {
                if (this.mandatory && !this.value) {
                    this.errorMsg = this.requiredMsg || '';//this.label + '必须填写';
                    return false;
                }
                this.errorMsg = '';
                return true;
            },
            opts : function() {
                return this.option;
            }
        },
        methods : {
            //初始化选择框的值
            init: function () {
                var me = this;
                var  list = this.option || [];
                var initialValue = this.initialValue;

                if(list.length == 1) {
                    me.value = list[0].label;
                    return;
                }
                list.forEach(function (item,i) {
                    if(initialValue === item.label || initialValue === item.value) {
                        me.value = item.label;
                    }
                });

            },
            //隐藏或打开列表
            toggleCollapsed: function (e) {
                e.stopPropagation();
                var el = $(this.$el).find('.select-dropdown-wrap');
                if(el.hasClass('collapsed')){
                    this.collapsed = false;
                }else{
                    this.collapsed = true;
                }
            },
            resetValue : function() {},

            change : function(item) {
                // 点击列表
                if(item.disabled) {
                    return;
                }
                this.collapsed = true;

                if(this.value == item.value) {
                    return;
                }
                this.value = item.label;
                this.$emit('userchange',item,this);
            }
        },
        components : {},
        activated: function () {
            this.init();
        },
        mounted : function() {
            var self = this;
            var toggleCollapsed = function () {
                self.collapsed = true;
            };
            $(document).off('click',toggleCollapsed).on('click',toggleCollapsed);
            this.init();
        }
    };
</script>

<style lang="less" rel="stylesheet/less" scoped>
    .select-dropdown-wrap {
        position: relative;
        cursor: pointer;
        .arrow {
            transition: all .3s;
            -webkit-transition: all .3s;
            transform:rotate(180deg);
            -webkit-transform:rotate(180deg);
        }
        &.collapsed {
            .select-dropdown {
                display: none;
            }
            .arrow {
                transform:rotate(0deg);
                -webkit-transform:rotate(0deg);
            }
        }

        .select-dropdown-input {
            position: relative;

            .input  {
                box-sizing: border-box;
            }
        }
        .select-dropdown {
            padding: .15rem 0;
            margin-top: .10rem;
            position: absolute;
            width: 100%;
            top: 100%;
            left:0;
            background-color: #FFF;
            min-height: 100px;
            z-index: 5;
            box-shadow:0 0 5px rgba(0,0,0,.2);
            border-color:#d7d7d7;
            border-radius: .03rem;
        }

        .select-dropdown-item {
            display: block;
            padding: .10rem .15rem;
            font-size: .12rem;

            &.disabled {
                color: #ddd;
            }
            &.active {
                background-color: #f8f8f8;
            }
        }

        .am-list-arrow {
            position: absolute;
            height:100%;
            right: 0;
            width: 25px;
            text-align: center;
            margin:0;


        }
    }
</style>
