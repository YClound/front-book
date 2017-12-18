define(function(require, exports, module) {
    var Utils = require('utils');
    require('angular-route');
    require('angular-lazyload');
    require('angular-core');
    require('angular-touch');
    require('angular-sanitize');
    
    var app = angular.module('mainApp', ['ngRoute','ngTouch','angular-lazyload', 'angular-core','ngSanitize']);

    //配置期
    app.config(['$routeProvider','$compileProvider',
        function($routeProvider,$compileProvider) {
            // 去除链接中的unsafe
            $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|file|javascript):/);
        }
    ]);
    // 若图片加载不出来 example:<img imgError >
    app.directive('imgError',function(){
        return {
                restrict : 'A',
                link:function(scope, elm, attr, ngModelCtrl){
                    var imgError = '/assets/img/public/img-error.jpg';
                 
                    elm[0].onerror = function(){
                        this.src = imgError;
                        this.setAttribute('data-original', imgError);
                    };
                }
        }; 
    });
    /*
        获取用户信息
        获取当前学期
        获取当前学生或者老师

     */
    // 入口
    app.controller('mainController',['$scope','$q','$route','$timeout','routeConfig','resource', 'mLoading','mNotice','$routeParams',function($scope,$q,$route,$timeout,routeConfig,resource,mLoading,mNotice,$routeParams){
        var userDao = resource('/user'),
            semesterDao = resource('/school/semester'),
            teacherDao = resource('/school/teacher'),
            teacherClassDao = resource('/school/teacher/class'),
            gradeDao = resource('/school/grade'),
            IGrow = window['IGrow'] || {},
            hash = location.hash || '',
            new_hash = hash ,
            is_weixin_hash = false,
            index,
            semesterPromise,
            userPromise,
            errors = 0,
            failCallback = function(result){
                errors++;
                if(result && result.message){
                    mNotice(result.message,'error');
                }
                $('.hello-everyone').html('sorry,进入失败');
            };
        
        
        
        // iphone下传过来的hash会影响路由
        
        index = hash.indexOf('wechat_redirect');
        if(index>-1){
            is_weixin_hash = true;
            new_hash = decodeURIComponent(hash);
            new_hash = new_hash.replace(/#wechat_redirect/g,'');
            new_hash = new_hash.replace(/#\//g,'');
            
        }
        if(is_weixin_hash){
            location.replace('http://m.igrow.cn/weixin#/'+new_hash);
            return;
        }

       
        var mode = Utils.getQuery('mode');
        if(mode && mode =='demo') {
            window.API.mode = 'demo';
        }
    
    
        // 正式进入
        $('body').append('<div class="hello-everyone" style="text-align:center;padding:15px;">正在进入...</div>');

        // 获取当前学期
        semesterPromise = semesterDao.list({},function(result){
            var semesterList = result.data || [];

            IGrow.semester = IGrow.getCurrentSemester(semesterList); 
            if(!IGrow.semester){
                mNotice('当前学期不存在','error');
                errors ++;
            }
            

        },function(result){
            failCallback(result);
        });
        // 获取当前用户
        userPromise = userDao.get({},function(result){
            var user = result.data || {};

            $scope.user = IGrow.user = user;
            

        },function(result){
            failCallback(result);
        });

        $q.all( [semesterPromise,userPromise] ).then(function(){
            var studentDao,teacherDao;
            
            if(errors>0){
                failCallback();
                return;
            }
            // 若是学生
            if( isStudent(IGrow.user) ) {
                $('body').attr('role','student');
                studentDao = resource('/school/student');
                studentDao.get( { _relatedfields : 'class.name,class.id,grade.id,grade.name' } ).then(function(result){
                    var student = result.data || {};

                    IGrow.student = student;
                    initRouteConifg();

                }, function(result){
                    failCallback(result);
                });

            } else {
                $('body').attr('role','teacher');
                teacherDao = resource('/school/teacher');
                gradeDao = resource('/school/grade');
                schoolClassDao = resource('/school/class');
                var gradeList,classList;
                teacherDao.get( {} ).then(function(result){
                    var teacher = result.data || {};

                    IGrow.teacher = teacher;

                    var promiseGrade = gradeDao.list({},function(result){
                        gradeList = result.data || [];

                    },function(result){
                        failCallback(result);
                    });

                    var promiseClass = schoolClassDao.list({},function(result){
                        classList = result.data || [];
                    },function(result){
                        failCallback(result);
                    });

                    $q.all([promiseGrade,promiseClass]).then(function(){

                        angular.forEach(classList, function(item,_){
                            var gradeid = item.gradeid,grade = Utils.getItem(gradeList,{id:gradeid});

                            if(grade){
                                item._name = grade.name + item.name;
                            }
                        });
                        IGrow.teacher.classes = classList.sort(function(x,y){
                            return (x.gradeid-y.gradeid)>0?1:-1;
                        });
                        // init route
                        initRouteConifg();

                    }, function(){

                    });
                    

                }, function(result){
                    failCallback(result);
                });
            }
                
                

        }, function(){
            
        });
        
        function isStudent(user){
            return user.typeid==4?true:false;
        }
        function initRouteConifg(){
            // 绑定图片预览
            Utils.bindPreviewPhoto();
            // 假如没有路由 则显现完整菜单
            if(!location.hash){
                $scope.flag = true;
                $('.intro').show();
            }
            
            $('.hello-everyone').remove();
            // 配置路由
            routeConfig(IGrow.modules);
            $route.reload();

        }
            
       
        
    }]);

    //运行期
    app.run(['$lazyload', function($lazyload) {
            //Step5: init lazyload & hold refs
            $lazyload.init(app);
            app.register = $lazyload.register;

        }
    ]);

    module.exports = app;
});