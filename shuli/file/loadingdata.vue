<template>

    <div class="loading-wrapper">
        <loading-display pull-data-url="/email/pullBaseInfo.json" @complete="completePolling" @fail="failCallback"
                         my-result-class="am-email-result" class="loading-wrapper" >
            <page-title title="获取邮箱账单"/>
            <div class="am-whitespace rem5"></div>
            <div class="am-list">
                <div v-show="!isDetail && !noData" class="am-list-header font-color-666">
                    <div>{{emailAddr}}</div>
                    <div>更新时间: {{updatetime}}</div>
                </div>

                <div v-show="noData" class="am-list-content am-ft-16 email-nodata" style="overflow:none;"></div>

                <div v-show="!noData" class="am-list-body">
                    <div id="basic-info"
                         class="border1 bg-white background-image-none boder-radius-5 margin-0-15">
                        <div id="basic-info-show" class="am-list-content am-ft-16 margin-top-13" style="overflow:hidden;">
                            <div id="email_nav" v-show="navShow">
                                <template v-for="item in dataNav">

                                    <div class="am-list sicon">
                                        <a class="am-list-item bankNav" @click="navclick(item)">
                                            <div class="am-list-thumb">
                                                <div :id="item.bankCode"></div>
                                            </div>
                                            <div class="am-list-content">
                                                <div class="am-list-title">{{item.bankName}}</div>
                                                <div class="am-list-brief">{{item.userName}}</div>
                                            </div>
                                            <div class="am-list-arrow" aria-hidden="true">
                                                <span class="am-icon arrow horizontal"></span>
                                            </div>
                                        </a>
                                    </div>
                                </template>
                                <p class="am-ft-center am-ft-gray am-ft-12" style="line-height:.4rem; color: #C5C5C5"> 以上信息仅包含邮箱账单基本信息
                                </p>
                            </div>
                            <div id="email_detail" v-show="detailShow">

                                <div id="div-header" :class="bankTitleClass" style="padding: 0rem .20rem;">
                                    <div role="bankLogo" class="am-flexbox-item bankLogo"></div>
                                    <div class="bankNameDiv">
                                        <div role="bankName" class="am-ft-md">{{bankName}}</div>
                                        <div role="userName" class="am-ft-12">{{userName}}</div>
                                    </div>
                                    <div class="align-right emailClose" @click=closeDetail()></div>
                                </div>
                                <div class="am-list email-detail-list" am-mode='flat'>
                                    <div class="am-list-body ">
                                        <template v-for="(item, idx) in detailData">

                                            <div class='am-list-item rich'>
                                                <div class="am-list-content">
                                                    <div class='am-list-brief riched' style="background-color: #fff">
                                                        <div style='flex:1;'>
                                                            <div class='am-flexbox floatLeft'>
                                                                <div class='w80 am-ft-md color333'>{{item[0].billDate |
                                                                    billDataFormat}}
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <template v-if="item[0].isShowDetail">
                                                            <div class="toggle-arrow floatRight" @click="toggleClick($event)"></div>
                                                        </template>
                                                        <template v-else="!item[0].isShowDetail">
                                                            <div class="toggle-arrow expandable floatRight"
                                                                 @click="toggleClick($event)"></div>
                                                        </template>
                                                    </div>

                                                    <template v-for="obj in item">
                                                        <div scope='detail' v-show="item[0].isShowDetail"
                                                             class="email-detail-div ">
                                                            <div class="am-flexbox firstTitle">
                                                                <div class="am-flexbox-item am-ft-12 color666">
                                                                    {{obj.billCycle}}
                                                                </div>
                                                                <div class="am-ft-12 color666 w80 align-right">
                                                                    尾号{{obj.cardNo | cardFormat}}
                                                                </div>
                                                            </div>
                                                            <div class="am-flexbox secondTitle">
                                                                <div class="w30per am-ft-13 color666">还款日</div>
                                                                <div class="w40per am-ft-13 color666">最低应还</div>
                                                                <div class="w30per am-ft-13 color666">账单金额</div>
                                                            </div>
                                                            <div class="am-flexbox contentLine">
                                                                <div class="w30per am-ft-md color333">
                                                                    {{obj.payDueDate | dateFormat}}
                                                                </div>
                                                                <div class="w40per am-ft-md color333">
                                                                    {{obj.minPayment | moneyFormat}}
                                                                </div>
                                                                <div class="w30per am-ft-md color333">
                                                                    {{obj.oweAmount | moneyFormat}}
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </template>

                                                </div>
                                            </div>
                                        </template>

                                    </div>
                                </div>

                            </div>


                        </div>
                    </div>
                </div>
            </div>

            <div v-show="detailExport" class="export-email-wrapper">
                <label>明细数据将发送至以下邮箱，请确保信息正确</label>
                <div class="am-list form" align="center">
                    <div class="am-list-body">
                        <text-field ref="email" mandatory @userchange="emailChanged" @focus="emailInputFocus" @blur="emailInputBlur" type="email" label=""
                                    name="email" placeholder="example@***.com">
                        </text-field>
                    </div>
                </div>
                <div class="am-whitespace rem5"></div>
                <div class="error">{{errorMsg}}</div>
                <div class="export-email-wrapper-btn">
                    <button type="button" class="am-button" :disabled="invalid" @click="sendEmail">获取明细数据</button>
                </div>

            </div>

            <div slot="fail">
                <div class="failure" v-if="state=='fail'">
                    <div class="failure-header">提交时间: {{updatetime}}</div>
                    <div class="failure-main border1">
                        <div class="failure-body ">

                            <div class="center divider">
                                <div class="center_center title">
                                    <span class="close"></span>
                                    <span class="am-ft-orange">同步失败</span>
                                </div>
                            </div>
                            <div class="am-ft-center am-ft-gray" style="font-size:.14rem;padding-bottom:.15rem;">
                                <p>账户归属: <span>{{itemName}}</span></p>
                            </div>
                            <div class="am-ft-center am-ft-orange" style="font-size:.14rem;padding-bottom:.15rem;">
                                <p><span>{{failMsg}}</span></p>
                            </div>

                        </div>
                        <div class="failure-footer">
                            <button type="button" class="am-button white" style="border:none;color:#ff8208;">
                                重试：请返回应用重新进入
                            </button>
                        </div>
                    </div>

                </div>

            </div>
            <div id="companyInfoDiv" slot="company">
                <company-info ref="companyInfo" adjust-to=".loading-wrapper"></company-info>
            </div>
        </loading-display>
    </div>


</template>

<script>
    import loadingDisplay from '../../../components/h5/loading-display.vue';
    import companyInfo from '../../../components/h5/company-info.vue';
    import dateformat from 'dateformat';
    import textField from '../../../components/h5/form/text-field.vue';
    import pageTitle from '../../../components/page-title.vue';

    export default {
        props: {},
        filters: {
            dateFormat: function (value) {
                if (!value) {
                    return '';
                }
                return dateformat(value, 'yyyy-mm-dd');
            },
            billDataFormat: function (value) {
                if (!value) {
                    return '';
                }
                return value.substr(0, 4) + '年' + value.substr(5) + "月账单"
            },
            cardFormat: function (value) {
                if (!value) {
                    return '';
                }
                return value.substr(value.length-4);
            },
            moneyFormat: function (value) {

                if (0 == value || '0'== value){
                    return '0.00';
                }else if (null == value || ''== value) {
                    return '';
                }
                var formatValue = parseFloat(value);
                formatValue = formatValue / 100;
                return formatValue.toFixed(2);
            }
        },
        data () {
            return {
                errorMsg: '',
                updatetime: '',
                invalid: true,
                state: 'loading', //loading, success, fail
                result: {},
                dataNav: {},
                detailData: {},
                navShow: true,
                detailShow: false,
                emailData: {},
                bankCode: '',
                bankName: '',
                userName: '',
                bankTitleClass: '',
                emailAddr: '',
                showDetail: false,
                detailExport: false,
                isShowCompany: false,
                noData: false,
                isDetail: false,
                itemName: '',
                failMsg: ''
            }
        },
        methods: {
            emailChanged: function (value) {
                var reg = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;

                if (reg.test(value)) {
                    this.invalid = false;
                } else {
                    this.invalid = true;
                }
            },
            sendEmail: function () {
                var email = this.$refs.email.getValue();
                var me = this;
                $.ajax({
                    type: "post",
                    url: "/metadata/saveTrialDetailMail.json",
                    dataType: 'json',
                    data: {email: email},
                    success: function (data) {
                        if (data.success) {
                            Utils.jumpTo("/detailmail/" + email);
                        } else {
                            me.errorMsg = data.msg || '';
                        }
                    },
                    error: function () {
                        me.errorMsg = '网络走神了';
                    }
                });
            },

            failCallback: function (msg,extraData) {
                if(extraData.itemName){
                    this.itemName = extraData.itemName || '';
                }
                this.failMsg = msg;
                this.state = 'fail';
            },
            completePolling: function (data,extraData) {

                var me = this;

                if(!data || 0 == data.length ){
                    this.noData = true;
                }

                var emailData = data;
                var emailAddr = this.getEmailAddr(emailData);
                this.emailAddr = emailAddr;
                var dataNav = this.getbankJson(emailData);
                this.dataNav = dataNav;
                this.emailData = emailData;
                this.result = data;

                if(extraData.detailExport){
                    if ('true' == extraData.detailExport){
                        this.detailExport = true;
                    }
                }

                if(extraData.showDetail){
                    if ('true' == extraData.showDetail){
                        this.showDetail = true;
                    }
                }

                $('#companyInfoDiv').addClass('company-info-hide');
                setTimeout(function () {
                    me.$refs.companyInfo.adjust();
                    $('#companyInfoDiv').removeClass('company-info-hide');
                    $('#companyInfoDiv').addClass('company-info-show')
                },4000)
            },


            getEmailAddr: function (jsonData) {
                var emailAddr = '';
                $.each(jsonData, function (index, data) {
                    if (data.email) {
                        emailAddr = data.email;
                        return;
                    }
                });
                return emailAddr;
            },

            getbankJson: function (jsonData) {
                var me = this;
                var bankArr = new Array();
                var bankJson = new Array();
                $.each(jsonData, function (index, data) {
                    if(typeof(data.userName)=="undefined" ){
                        data.userName = ''
                    }
                    var bankObj = new Object();
                    var key = data.bankCode + "_" + data.userName
                    if (!me.iscontainsKey(bankArr, key)) {
                        bankObj.key = key;
                        bankObj.value = data.bankName;
                        bankArr.push(bankObj);
                        bankJson.push({bankCode: data.bankCode, userName: data.userName, bankName: data.bankName});
                    }
                });
                bankJson.sort(function (a, b) {
                 if (a.bankCode == b.bankCode) {
                 return a.userName.localeCompare(b.userName);
                 } else {
                 return b.bankCode.localeCompare(a.bankCode);
                 }

                 });
                return bankJson;
            },


            iscontainsKey: function (objArr, key) {
                var flag = false;
                $.each(objArr, function (index, data) {
                    if (data.key == key) {
                        flag = true;
                        return
                    }
                });
                return flag;
            },

            navclick: function (item) {
                var me = this;
                me.bankCode = item.bankCode;
                me.bankName = item.bankName;
                me.userName = item.userName;
                me.bankTitleClass = 'namebar am-flexbox font16 header-' + item.bankCode;
                me.detailShow = true;
                me.navShow = false;
                var detailData = me.getDetailData(item.bankCode, item.userName);
                var detailDataForShow = new Array();
                $.each(detailData, function (index, data) {
                    var detail = new Array();
                    detail = data;
                    if (0 == index) {
                        detail[0].isShowDetail = true;
                    } else {
                        detail[0].isShowDetail = false;
                    }
                    detailDataForShow.push(detail);
                });
                me.detailData = detailDataForShow;
                $('.toggle-arrow').trigger('click');
                me.isDetail = true;
                me.isShowCompany = false;
                $('#companyInfoDiv').removeClass('company-info-show');
                $('#companyInfoDiv').addClass('company-info-hide')
                setTimeout(function () {
                    me.$refs.companyInfo.adjust();
                    $('#companyInfoDiv').removeClass('company-info-hide');
                    $('#companyInfoDiv').addClass('company-info-show')
                },200)
            },

            closeDetail: function () {
                var me = this;
                me.detailShow = false;
                me.navShow = true;
                me.detailData = new Array();
                me.isDetail = false;
                me.isShowCompany = false;
                $('#companyInfoDiv').removeClass('company-info-show');
                $('#companyInfoDiv').addClass('company-info-hide');
                setTimeout(function () {
                    me.$refs.companyInfo.adjust();
                    $('#companyInfoDiv').removeClass('company-info-hide');
                    $('#companyInfoDiv').addClass('company-info-show')
                },200)
            },

            getDetailData: function (bankCode, userName) {
                var bankDataArray = new Array();
                $.each(this.emailData, function (index, bankData) {
                    if (bankData.bankCode == bankCode && bankData.userName == userName) {
                        bankDataArray.push(bankData);
                    }
                });
                var sortDataArray = new Array();
                var month = new Array();
                for (var i = 0; i < bankDataArray.length; i++) {

                    if(typeof(bankDataArray[i].billDate)=="undefined" ){
                        bankDataArray[i].billDate = ''
                    }

                    if (month.toString().indexOf(bankDataArray[i].billDate) < 0) {
                        month.push(bankDataArray[i].billDate);
                    }
                }

                 month.sort(function (a, b) {
                 return b.localeCompare(a);
                 });

                for (var j = 0; j < month.length; j++) {
                    var monthData = new Array();
                    for (var k = 0; k < bankDataArray.length; k++) {
                        if (bankDataArray[k].billDate == month[j]) {
                            monthData.push(bankDataArray[k]);
                        }
                    }
                    sortDataArray.push(monthData);
                }
                return sortDataArray;
            },

            toggleClick: function (event) {

                var me = this;

                if ($(event.currentTarget).attr('class').indexOf('expandable') > -1){
                    $(event.currentTarget).parent().parent().find('.email-detail-div').show();
                    $(event.currentTarget).removeClass('expandable');
                }else {
                    $(event.currentTarget).parent().parent().find('.email-detail-div').hide();
                    $(event.currentTarget).addClass('expandable');
                }

                $('#companyInfoDiv').removeClass('company-info-show');
                $('#companyInfoDiv').addClass('company-info-hide');
                setTimeout(function () {
                    me.$refs.companyInfo.adjust();
                    $('#companyInfoDiv').removeClass('company-info-hide');
                    $('#companyInfoDiv').addClass('company-info-show')
                },200)

            },

            getResultValue: function (key) {

                var v = this.result[key];
                if (key === 'accountBalance') {
                    return v ? v / 100 : '';
                }
                return v;
            },
            emailInputFocus: function () {
                var me = this;
                setTimeout(function () {
                    me.$refs.companyInfo.adjust();
                    $('#companyInfoDiv').removeClass('company-info-show');
                    $('#companyInfoDiv').addClass('company-info-hide')
                },10);
            },
            emailInputBlur: function () {
                var me = this;
                setTimeout(function () {
                    me.$refs.companyInfo.adjust();
                    $('#companyInfoDiv').removeClass('company-info-hide');
                    $('#companyInfoDiv').addClass('company-info-show')
                },500);
            }
        },
        components: {companyInfo, loadingDisplay, textField, pageTitle},
        mounted: function () {
            this.updatetime = dateformat(new Date(), 'yyyy-mm-dd');
        }
    };
</script>

<style lang="less" rel="stylesheet/less" scoped>
    .field-label {
        vertical-align: top;
    }

    .field-value {
        white-space: normal;
    }

    /*select result list*/
    .w30 {
        width: 40%;
    }

    .am-result-icon img {
        width: inherit;
    }

    .am-result-brief {
        color: #C5C5C5 !important;
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

    #basic-info {
        border: .01rem #ccc solid;
        background-color: #FFFFFF !important;
        border-radius: .05rem !important;
        margin: 0 .15rem !important;
    }

    .baseline {
        align-items: baseline;
        min-height: .3rem;
    }
    .failure .close{
        display: inline-block;
        position: relative;
        width:.13rem;
        height:.13rem;
        border:.01rem #ff8208 solid;
        border-radius:50%;
    }
    .failure .divider {
        margin: .25rem .15rem;
    }
    .failure .close:before, .failure .close:after{
        content: '';
        position: absolute;
        width:.01rem;
        height:.11rem;
        background:#ff8208;
        top:.01rem;
        left:.06rem;
    }

    .failure .close:before{
        -webkit-transform: rotate(45deg);
        transform: rotate(45deg);
    }
    .failure .close:after{
        -webkit-transform: rotate(-45deg);
        transform: rotate(-45deg);
    }
    .failure {
        padding: .15rem;
    }
    .failure-header{
        padding-bottom: .08rem;
        font-size: .12rem;
        color: #a5a5a5;
    }
    .failure-footer {
        border-top: .01rem solid #ddd;
    }
    .border1 {
        border:.01rem #ccc solid;
        background-color: #FFFFFF ;
        border-radius: .05rem;
        padding: 0;
        text-align: center;
    }

    .divider{position:relative;text-align:center}
    .divider:after{content:'';display:block;position:absolute;width:100%;height:.01rem;top:50%;border-top:.01rem solid #ff8208;left:0}
    .divider .title{display:inline-block;padding:0 1em;position:relative;z-index:2;background: #FFF;}

    /*- - - - - -style#邮箱账单- - - - - - - -*/

    #CMB {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/CMB.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    #COMM {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/COMM.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    #CCB {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/CCB.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    #ICBC {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/ICBC.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    #ABC {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/ABC.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    #BOC {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/BOC.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    #CEB {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/CEB.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    #CGB {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/CGB.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    #HXB {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/HXB.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    #CMBC {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/CMBC.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    #NBB {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/NBB.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    #PAB {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/PAB.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    #SPDB {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/SPDB.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    #CIB {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/CIB.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    #ECITIC {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/ECITIC.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    .email-main {
        line-height: 1 !important;
        min-height: 3.00rem !important;
    }

    #emailAddr {
        padding-left: .15rem;
        padding-top: .05rem;
        margin-bottom: -.10rem;
    }

    /*招商银行*/
    .header-CMB {
        background-image: linear-gradient(90deg, #f66790 2%, #f87169 100%);
    }

    .header-CMB > .bankLogo {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/CMB.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    /*交通银行*/
    .header-COMM {
        background-image: linear-gradient(131deg, #53b8f9 0%, #3bd2c0 100%);
    }

    .header-COMM > .bankLogo {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/COMM.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    /*建设银行*/
    .header-CCB {
        background-image: linear-gradient(134deg, #448ce3 0%, #2f52b3 100%);
    }

    .header-CCB > .bankLogo {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/CCB.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    /*工商银行*/
    .header-ICBC {
        background-image: linear-gradient(134deg, #448ce3 0%, #2f52b3 100%);
    }

    .header-ICBC > .bankLogo {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/ICBC.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    /*农业银行*/
    .header-ABC {
        background-image: linear-gradient(131deg, #53b8f9 0%, #3bd2c0 100%);
    }

    .header-ABC > .bankLogo {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/ABC.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    /*中国银行*/
    .header-BOC {
        background-image: linear-gradient(90deg, #f66790 2%, #f87169 100%);
    }

    .header-BOC > .bankLogo {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/BOC.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    /*光大银行*/
    .header-CEB {
        background-image: linear-gradient(134deg, #448ce3 0%, #2f52b3 100%);
    }

    .header-CEB > .bankLogo {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/CEB.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    /*广发银行*/
    .header-CGB {
        background-image: linear-gradient(90deg, #f66790 2%, #f87169 100%);
    }

    .header-CGB > .bankLogo {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/CGB.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    /*华夏银行*/
    .header-HXB {
        background-image: linear-gradient(90deg, #f66790 2%, #f87169 100%);
    }

    .header-HXB > .bankLogo {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/HXB.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    /*民生银行*/
    .header-CMBC {
        background-image: linear-gradient(131deg, #53b8f9 0%, #3bd2c0 100%);
    }

    .header-CMBC > .bankLogo {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/CMBC.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    /*宁波银行*/
    .header-NBB {
        background-image: linear-gradient(90deg, #f66790 2%, #f87169 100%);
    }

    .header-NBB > .bankLogo {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/NBB.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    /*平安银行*/
    .header-PAB {
        background-image: linear-gradient(90deg, #f66790 2%, #f87169 100%);
    }

    .header-PAB > .bankLogo {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/PAB.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    /*浦发银行*/
    .header-SPDB {
        background-image: linear-gradient(134deg, #448ce3 0%, #2f52b3 100%);
    }

    .header-SPDB > .bankLogo {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/SPDB.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    /*兴业银行*/
    .header-CIB {
        background-image: linear-gradient(134deg, #448ce3 0%, #2f52b3 100%);
    }

    .header-CIB > .bankLogo {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/CIB.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    /*中信银行*/
    .header-ECITIC {
        background-image: linear-gradient(90deg, #f66790 2%, #f87169 100%);
    }

    .header-ECITIC > .bankLogo {
        height: .36rem;
        width: .36rem;
        background-image: url('../../../images/email/ECITIC.png');
        background-size: .36rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    .toggle-arrow {
        height: .20rem;
        background-image: url('../../../images/email/arrow-up.png');
        background-size: .20rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    .toggle-arrow.expandable {
        transform: rotate(180deg);
    }

    .emailClose {
        height: .20rem;
        width: .20rem;
        background-image: url('../../../images/email/close.png');
        background-size: .20rem;
        background-repeat: no-repeat;
        background-position: center center;
    }

    .email-nodata {
        text-align: center;
        margin-bottom: .30rem;
    }

    .email-nodata:before {
        content: '';
        display: block;
        background-image: url('../../../images/email/nodata.png');
        background-size: 1.80rem 1.80rem;
        height: 1.80rem;
        width: 1.80rem;
        margin-top: .5rem;
        display: inline-block;
    }

    .email-nodata:after {
        content: '暂时查询不到您近六个月以内的账单';
        font-size: .14rem;
        color: #666666;
        margin-top: .20rem;
        display: block;
    }

    .padding-0-15 {
        padding: 0 .15rem !important;
    }

    .margin-0-15 {
        margin: 0 .15rem !important;
    }

    .namebar {
        height: .76rem;
    }

    .am-list:not([am-version]) {
        padding: 0rem !important;
    }

    .am-list:not([am-version]) .am-list-item, .am-list:not([am-version])[am-mode~=flat] .am-list-item {
        padding: .15rem !important;
        min-height: .16rem !important;
    }

    .bankNameDiv {
        color: #FFFFFF;
        width: 75%;
        line-height: 1.2;
    }

    .email-detail-list {
        padding-top: 0rem !important;
        overflow: hidden;
    }

    /* am-list-patch */
    .am-list-item > .am-list-content {
        margin-left: .05rem;
    }

    .am-list-item.rich {
        vertical-align: top !important;
        -webkit-box-align: start !important;
        align-items: flex-start !important;
        padding-top: .15rem !important;
    }

    .am-list-content.rich-content {
        overflow: visible !important;
    }

    .am-list-content.rich-content > div.am-list-title {
        margin-left: -.05rem;
    }

    .am-list-content.rich-content > div.am-list-title > span {
        margin-left: .10rem;
    }

    .am-list-item.rich .riched {
        background-color: #f5f5f5;
        border-radius: .03rem;
        padding: .01rem;
        display: flex;
        display: -webkit-flex;
        align-items: center;
    }

    .am-list-content > a:active > .riched {
        background-color: #e4e4e4 !important;
    }

    .am-list-item > .am-list-arrow {
        margin-right: .13rem !important;
    }

    .toggle-arrow {
        width: .20rem !important;
    }

    .markdown-body {
        background-color: #fff;
        width: .35rem;
        text-align: center;
        color: #00AAEE;
        font-size: .14rem;
        line-height: 1.5;
        border-radius: .5rem;
        font-weight: 700;
        margin-left: .5rem;
    }

    .display-block > .toggle-arrow {
        text-align: center;
        margin-top: -.10rem;
        background-color: #FFFFFF;
        margin-bottom: .10rem;
    }

    .display-block > .toggle-arrow:active {
        background-color: #FFFFFF;
    }

    .email-detail-div {
        background-color: #f5f5f5;
        padding: .10rem .10rem .18rem .10rem;
        margin-top: .10rem;
    }

    .email-detail-div > .firstTitle {
        margin-bottom: .18rem;
    }

    .email-detail-div > .secondTitle {
        text-align: center !important;
        margin-bottom: .05rem;
    }

    .email-detail-div > .contentLine {
        text-align: center !important;
    }

    .background-none {
        background: none !important;
    }

    .w110 {
        width: 1.10rem !important;
    }

    .w20per {
        width: 20% !important;
    }

    .w30per {
        width: 30% !important;
    }

    .w40per {
        width: 40% !important;
    }

    .color333 {
        color: #333333 !important;
    }

    .color666 {
        color: #666666 !important;
    }

    .bg-white {
        background: #FFFFFF !important;
    }

    .margin-bottom-10 {
        margin-bottom: .10rem !important;
    }

    .u-footer {
        padding: .15rem 0;
        color: #999;
        text-align: center;
        width: 100%;
    }

    .mask, footer.company-info-abs {
        position: absolute !important;
        bottom: 0 !important;
    }

    .am-flexbox {
        -webkit-align-items: center !important;
    }

    .floatLeft {
        float: left !important;
    }

    .floatRight {
        float: right !important;
    }

    .export-email-wrapper {
        margin: .15rem 0;
        text-align: center;
    }

    .export-email-wrapper-btn {
        padding: 0 .15rem;
    }

    .company-info-hide {
        opacity:0;
        filter:Alpha(opacity=0); /* IE8 以及更早的浏览器 */
    }

    .company-info-show {
        opacity:1;
        filter:Alpha(opacity=100); /* IE8 以及更早的浏览器 */
    }


</style>
