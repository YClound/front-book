/* 所有数据接口以及全局变量 */
(function(){
    
    //window.UEDITOR_HOME_URL = '/';
    // UEDITOR上传路径
    window.ueditorServerUrl = '/static/api/upload.html';
    var api = window.api = {
            // 获取用户项目
            userProjectList:'/static/assets/data/userProjectList.json',
            // 想投资人推荐某项目
            userProjectRecommend:'/static/assets/data/userProjectRecommend.json',
            // 同意查看项目
            agreeProjectApply:"/static/assets/data/form.json",
            // 收藏
            followproject:'/static/api/followproject.html',
            // 项目预览地址
            preview:'',
            // 文件上传地址
            upload:'/static/assets/data/upload.json',
            // 身份城市接口
            cities:'/static/assets/data/city.json',
            sms:'/sms/getSMSing.html',
            ssoUcenter: 'http://user.undm.cn/user/sso?callback=?',
            ssoLocal: '/sso/login.html',
            ucLogin: "/static/front/login-register.html?appid=chuying&redirect_url=" + encodeURIComponent(location.href),
            
            ucRegister:"/static/front/login-register.html",
            investorAuth:"/static/front/investor-approve.html",
            ucLogout: 'http://user.undm.cn/user/user/logout?callback=?'
    };

})();

(function () {
    var console, length, method, methods, noop;
    method = void 0;
    noop = function () {
    };
    methods = ['assert', 'clear', 'count', 'debug', 'dir', 'dirxml', 'error', 'exception', 'group', 'groupCollapsed', 'groupEnd', 'info', 'log', 'markTimeline', 'profile', 'profileEnd', 'table', 'time', 'timeEnd', 'timeline', 'timelineEnd', 'timeStamp', 'trace', 'warn'];
    length = methods.length;
    console = window.console = window.console || {};
    while (length--) {
        method = methods[length];
        if (!console[method]) {
            console[method] = noop;
        }
    }
    // 动态缩略图
    window.parsePhotoData = function(data){
        var _photo = [];
        if(data.photo) {
            _photo = data.photo.split(',');
        }else {
            return;
        }
        for (var i = _photo.length - 1; i >= 0; i--) {
            var ori = _photo[i];
            var index1 = ori.lastIndexOf('/');
            if(index1>-1){
                var prefix = ori.substring(0,index1);
                var name = ori.substring(index1+1);
                var thumbnail = prefix + '/' + 'thumbnail_' + name;
            }else {
                var thumbnail = 'thumbnail_' + ori;
            }
            
            var index = ori.lastIndexOf('_');
            if(index>-1) {
                var str = ori.substring(index+1);
                var arr = str.split('.');
                var size = arr[0];
            }else {
                var size = '';
            }
                
            _photo[i] = {
                thumbnail:thumbnail,
                ori:ori,
                size:size
            };

        }
        data._photo = _photo;
        
    };
    // 微信
    window.wechatHelper = function () {
        var $modal, $btn
        return {
            init: function () {
                $modal = $('.modal-qr')
                $btn = $('.btn-wechat-login')
                $btn.on('click', function () {
                    wechatHelper.showQR()
                })
            },
            showQR: function () {
                new WxLogin({
                    id: 'qr-login',
                    appid: 'wx53875df8b75e576d',
                    scope: 'snsapi_login',
                    redirect_uri: 'http://user.undm.cn/member/bindwx',
                    state: 'dreammove2015',
                    style: 'black',
                    href: ''
                })
            }
        }
    }()

    window.smsHelper = function () {
        var that = {}
        that.init = function () {
            $('.btn-sms').each(function () {
                var $btn = $(this),
                    $form = $btn.closest('form'),
                    $codeType=$form.find("#codeflag"),
                    $inputTel = $form.find('#tel, .tel, [type=tel]').eq(0),
                    $inputImgVerify = $form.find('.input-img-verify').not('[disabled]')


                var send = function () {
                    $btn.disable()
                    dm.getJSON(api.sms, {
                        phone: $inputTel.val(),
                        verify: $inputImgVerify.val(),
                        flag: $codeType.val(),
                        ischeck: $btn.data('check-type')
                    }).done(function (json) {
                        $btn.disable('<var>60</var> 秒', false)
                        var $var = $btn.find('var'),
                            timer = 0
                        var tick = function () {
                            var sec = (parseInt($var.text(), 10) || 0) - 1
                            if (sec <= 0) {
                                $btn.enable()
                            } else {
                                $var.text(sec)
                                timer = setTimeout(tick, 1000)
                            }
                        }
                        tick()
                    }).fail(function () {
                        $btn.enable()
                    })
                }

                $btn.click(function () {
                    var checkType = $btn.data('checkType') || 0
                    if (checkType === 'refund' || checkType === 'changemobile') send() // 提现, 手机号自动获取
                    else $inputTel.validate(null, function () {
                        if ($inputImgVerify.length) {
                            $inputImgVerify.validate(null, send)
                        }else{
                            send();
                        }
                    })
                })
            })
        }
        return that
    }();


    // 图片验证码
    var imgVerifyHelper = window.imgVerifyHelper= function () {
        var $img
        return {
            init: function () {
                $img = $('.img-verify')
                $(".reload-verify").click(this.reload)
            },
            reload: function () {
                var src = $img.attr("src");
                if (~src.indexOf('?')) {
                    $img.attr("src", src + '&random=' + Math.random());
                } else {
                    $img.attr("src", src.replace(/\?.*$/, '') + '?' + Math.random());
                }
            }
        }
    }();
    
    // fz = 320px/20=16px, max-width=640px
    // 手机端
    //!function(){var a=document.documentElement;var b=a.clientWidth/20;a.style.fontSize=(b>32?32:b)+"px"}();

    


})();


