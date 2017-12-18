

var appCore = angular.module('angular-core',[]);


/*
实时监听input value
<input name="cardNo" ng-model="formData.cardNo" ng-input="onCardNoIn($value)" type="tel" required ng-minlength="16" maxlength="19" placeholder="请填写16-19位卡号">
$scope.onCardNoIn = function(value){
    console.log('input:',value);
}
*/
appCore.directive('ngInput', [function () {
    return {
        restrict: 'A',
        require: '?ngModel',
        link: function(scope, element, attrs) {
            element.on('input',oninput);
            scope.$on('$destroy',function(){//销毁的时候取消事件监听
                element.off('input',oninput);
            });
            function oninput(event){
                scope.$evalAsync(attrs['ngInput'],{$event:event,$value:this.value});
            }
        }
    }
}]);
// 若图片加载不出来 example:<img imgError >
appCore.directive('imgError',function(){
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
/* 自定义 MLoading */
appCore.factory('mLoading',['$http',function($http){
    var dm = window.dm || {loading:function(){}};
    return dm.loading;
}]);

/* 自定义 MNotice */
appCore.factory('mNotice',function(){
    var dm = window.dm || {notice:function(){}};
    return dm.notice;
});
/* 
    自定义ajax 
    @params
        _expire:1405492661238 //过期时间戳

*/
appCore.factory('http', ['$http', '$q','mLoading','mNotice',
    function($http, $q ,mLoading , mNotice) {
        var Cache = {};
        var http = window['http'] = {
            ajax: function(url, data, opts,successCallback, failCallback,always) {
                var self = this,
                    opts = opts || {},
                    data = data || {},
                    deferred = $q.defer(),
                    method = opts.type || 'GET',
                    dataType = opts.dataType || 'json',
                    timeout = opts.timeout || 60 * 1000,
                    context = opts.context || self,
                    expire = data._expire,// 数据保留时间
                    now = new Date().valueOf(),
                    params = jQuery.param(data),
                    cache_url = url + '?' + params,
                    result,
                    config = {};

       
                if('GET' === method && expire && Cache[cache_url] && ( now-Cache[cache_url]['t']<expire ) ) {
                    result = Cache[cache_url]['data'];
                    successCallback && successCallback(result);
                    deferred.resolve(result);
                    always && always();

                    return deferred.promise;
                }
                delete data._expire;
                config = {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    transformRequest: function(obj) {
                        return jQuery.param(obj);
                    },
                    method: method,
                    url: url,                        
                    dataType: dataType,
                    data: data

                };
                if (method === 'POST') {
                    config.data = data;
                } else {
                    config.params = data;
                }

                $http(config).success(function(data, status, headers, config) {
                    var message;
            
                    if (data.code && data.code != 0) {

                        message = data.message;
                        deferred.reject({
                            status:status,
                            message: message
                        });

                        
                    } else {
                        if(expire){
                            Cache[cache_url] = {
                                data:data,
                                t:now
                            };
                        }
                        successCallback && successCallback(data);
                        deferred.resolve(data);
                        always && always();
                    }


                }).error(function(data, status, headers, config) {
                    var message = '';
                    //console.warn(data)
                    if(data.code && data.code != 0){
                        message = data.message;
                        // 若未登录
                        if(data.code == 10020002){
                            var hash = location.hash || '';
                            location.href = 'http://auth.igrow.cn/auth/login?from=AUTH&go=http://m.igrow.cn/main?hash=' + hash.replace('#/','');
                        }
                    }else {
                        data = {
                            message: '服务器出错'
                        };
                        
                    }
                    failCallback && failCallback(data);
                    deferred.reject({
                        status:status,
                        message: message
                    });
                    always && always();

                });

                return deferred.promise;
            },
            get: function(url, data,successCallback, failCallback,always) {

                return this.ajax(url, data, {
                    type: 'GET'
                },successCallback, failCallback,always);

            },
            post: function(url, data, successCallback, failCallback,always) {
                var type = 'POST';
                if(location.href.indexOf('localhost')>-1){
                    type = 'GET';
                }
                return this.ajax(url, data, {
                    type: type
                },successCallback, failCallback,always);

            },
            // 处理请求错误
            handleXhrError: function(xhr) {
                var responseText,
                    error = {},
                    isResponseObject = function(xhr) {
                        return /^{/.test(xhr.responseText);
                    };

                if (xhr.statusText === 'timeout') {
                    error.message = '请求超时 ';
                } else if (xhr.message) {
                    error = xhr;
                } else if (xhr.status == 500 && isResponseObject(xhr)) {
                    try {
                        responseText = xhr.responseText.replace('/\\/g', '//');
                        error = $.parseJSON(responseText);
                        error.message = error.message || '错误未知';

                    } catch (e) {
                        console.warn('responseText parse error');
                        error = {
                            message: ' 错误未知 '
                        };
                    }

                } else {
                    error = {
                        message: ' 错误未知 '
                    };
                }

                error.status = xhr.status;

                return error;
            }

        };

        return http;


    }
]);
/*
        自定义resource 封装api请求
    *   @param url --> string ajax路径 example:假设完整路径 'http://m.igrow.cn/api/1.1b/school/people/get' 则url为'/school/people'
    *   @param options --> object 暂时没用
    *   @param actions --> object example :{ 'get2': { method:'GET',params:{ '默认参数1':'1','默认参数2':'2' } } }
    *
    *  默认返回的对象包含的方法:get,update,create,list,search,_delete   
    *  调用example
    *  var schoolPeople = resource('/school/people',{},{});
    *  schoolPeople.get({id:'1'}), function(result){
    *      console.log('返回的数据',result.data) ;
    *      
    *  },function(result){
    *      console.log( '错误信息',result.message );
    *  },function(){
    *      console.log('always')
    *  });
*/
appCore.factory('resource', ['http',
    function(http) {
        var page = '_page';
        var pagesize = '_pagesize';
        var checkURL = function(url){
            var map = API.map || {},
            
                match;

            API.demo = API.demo || '';
            // 假如是实际请求
            if ( map[url] && location.href.indexOf('localhost')==-1) {
                url = map[url]
            }else {
                url = API.root + API.demo + url + '.json';
            }

            return url;
        };
        
        var $resource = function(url, options, actions) {
            var url = url || '',
                options = options || {}, actions = actions || {},
                resourse = {}, params;

            
            resourse = {
                url: url,
                invoke:function(name,data,options){
                    var url = this.url + '/' +name,
                        options = options || {},
                        method = options.method?options.method:'post',
                        data = data || {};

                    url = checkURL(url);
                    return http[method](url, data);

                },
                list: function(data, successCallback, failCallback,always) {
                    var url = this.url + '/list',
                        data = data || {};

                    //data[page] = data._page ? data._page : 1;
                    //data[pagesize] = data._pagesize ? data._pagesize : 20;
                    url = checkURL(url);

                    return http.get(url, data ,successCallback, failCallback,always);
                },
                get: function(data, successCallback, failCallback,always) {
                    var url = this.url + '/get',
                        data = data || {};

                    url = checkURL(url);
                    return http.get(url, data, successCallback, failCallback,always);
                },
                search: function(data, successCallback, failCallback,always) {
                    var url = this.url + '/search',
                        data = data || {};

                    //data[page] = data._page ? data._page : 1;
                    //data[pagesize] = data._pagesize ? data._pagesize : 20;
                    url = checkURL(url);

                    return http.get(url, data,successCallback, failCallback,always);
                },
                _delete: function(data, successCallback, failCallback,always) {
                    var url = this.url + '/delete',
                        data = data || {};

                    url = checkURL(url);

                    return http.get(url, data,successCallback, failCallback,always);
                },
                create: function(data, successCallback, failCallback,always) {
                    var url = this.url + '/create',
                        data = data || {};

                    url = checkURL(url);

                    return http.post(url, data,successCallback, failCallback,always);
                },
                update: function(data, successCallback, failCallback,always) {
                    var url = this.url + '/update',
                        data = data || {};

                    url = checkURL(url);
                    return http.post(url, data,successCallback, failCallback,always);
                }
            };
            // 自定义action
            for (var action in actions) {
                var opts = actions[action] || {}, method = opts.method || "GET",
                    params = opts.params || {};

                method = method.toLowerCase();
                resourse[action] = (function(url, action, method, params) {

                    return function(data, successCallback, failCallback,always) {
                        var data = data || {};

                        url = resourse['url'] + '/' + action;
                        url = checkURL(url);
                        data = jQuery.extend({}, params, data);

                        return http[method](url, data, successCallback, failCallback,always);

                    };

                })(url, action, method, params)

            };



            return resourse;

        };

        return $resource;
    }
]);




