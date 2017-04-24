/*
    bootstrap指令模块
*/
angular.module("ui.bootstrap", ["ui.bootstrap.tpls","ui.bootstrap.pagination"]);
angular.module("ui.bootstrap.tpls", ["template/pagination/pager.html","template/pagination/pagination.html"]);
angular.module('ui.bootstrap.pagination', [])
.controller('UibPaginationController', ['$scope', '$attrs', '$parse', function($scope, $attrs, $parse) {
  var self = this,
      ngModelCtrl = { $setViewValue: angular.noop }, // nullModelCtrl
      setNumPages = $attrs.numPages ? $parse($attrs.numPages).assign : angular.noop;

  this.init = function(ngModelCtrl_, config) {
    ngModelCtrl = ngModelCtrl_;
    this.config = config;

    ngModelCtrl.$render = function() {
      self.render();
    };

    if ($attrs.itemsPerPage) {
      $scope.$parent.$watch($parse($attrs.itemsPerPage), function(value) {
        self.itemsPerPage = parseInt(value, 10);
        $scope.totalPages = self.calculateTotalPages();
      });
    } else {
      this.itemsPerPage = config.itemsPerPage;
    }

    $scope.$watch('totalItems', function() {
      $scope.totalPages = self.calculateTotalPages();
    });

    $scope.$watch('totalPages', function(value) {
      setNumPages($scope.$parent, value); // Readonly variable

      if ( $scope.page > value ) {
        $scope.selectPage(value);
      } else {
        ngModelCtrl.$render();
      }
    });
  };

  this.calculateTotalPages = function() {
    var totalPages = this.itemsPerPage < 1 ? 1 : Math.ceil($scope.totalItems / this.itemsPerPage);
    return Math.max(totalPages || 0, 1);
  };

  this.render = function() {
    $scope.page = parseInt(ngModelCtrl.$viewValue, 10) || 1;
  };

  $scope.selectPage = function(page, evt) {
    if (evt) {
      evt.preventDefault();
    }

    var clickAllowed = !$scope.ngDisabled || !evt;
    if (clickAllowed && $scope.page !== page && page > 0 && page <= $scope.totalPages) {
      if (evt && evt.target) {
        evt.target.blur();
      }
      ngModelCtrl.$setViewValue(page);
      ngModelCtrl.$render();
    }
  };

  $scope.getText = function(key) {
    return $scope[key + 'Text'] || self.config[key + 'Text'];
  };

  $scope.noPrevious = function() {
    return $scope.page === 1;
  };

  $scope.noNext = function() {
    return $scope.page === $scope.totalPages;
  };
}])

.constant('uibPaginationConfig', {
  itemsPerPage: 10,
  boundaryLinks: false,
  directionLinks: true,
  firstText: '首页',
  previousText: '上一页',
  nextText: '下一页',
  lastText: '尾页',
  rotate: true
})

.directive('uibPagination', ['$parse', 'uibPaginationConfig', function($parse, paginationConfig) {
  return {
    restrict: 'EA',
    scope: {
      totalItems: '=',
      firstText: '@',
      previousText: '@',
      nextText: '@',
      lastText: '@',
      ngDisabled:'='
    },
    require: ['uibPagination', '?ngModel'],
    controller: 'UibPaginationController',
    controllerAs: 'pagination',
    templateUrl: function(element, attrs) {
      return attrs.templateUrl || 'template/pagination/pagination.html';
    },
    replace: true,
    link: function(scope, element, attrs, ctrls) {
      var paginationCtrl = ctrls[0], ngModelCtrl = ctrls[1];

      if (!ngModelCtrl) {
         return; // do nothing if no ng-model
      }

      // Setup configuration parameters
      var maxSize = angular.isDefined(attrs.maxSize) ? scope.$parent.$eval(attrs.maxSize) : paginationConfig.maxSize,
          rotate = angular.isDefined(attrs.rotate) ? scope.$parent.$eval(attrs.rotate) : paginationConfig.rotate;
      scope.boundaryLinks = angular.isDefined(attrs.boundaryLinks) ? scope.$parent.$eval(attrs.boundaryLinks) : paginationConfig.boundaryLinks;
      scope.directionLinks = angular.isDefined(attrs.directionLinks) ? scope.$parent.$eval(attrs.directionLinks) : paginationConfig.directionLinks;

      paginationCtrl.init(ngModelCtrl, paginationConfig);

      if (attrs.maxSize) {
        scope.$parent.$watch($parse(attrs.maxSize), function(value) {
          maxSize = parseInt(value, 10);
          paginationCtrl.render();
        });
      }

      // Create page object used in template
      function makePage(number, text, isActive) {
        return {
          number: number,
          text: text,
          active: isActive
        };
      }

      function getPages(currentPage, totalPages) {
        var pages = [];

        // Default page limits
        var startPage = 1, endPage = totalPages;
        var isMaxSized = angular.isDefined(maxSize) && maxSize < totalPages;

        // recompute if maxSize
        if (isMaxSized) {
          if (rotate) {
            // Current page is displayed in the middle of the visible ones
            startPage = Math.max(currentPage - Math.floor(maxSize/2), 1);
            endPage   = startPage + maxSize - 1;

            // Adjust if limit is exceeded
            if (endPage > totalPages) {
              endPage   = totalPages;
              startPage = endPage - maxSize + 1;
            }
          } else {
            // Visible pages are paginated with maxSize
            startPage = ((Math.ceil(currentPage / maxSize) - 1) * maxSize) + 1;

            // Adjust last page if limit is exceeded
            endPage = Math.min(startPage + maxSize - 1, totalPages);
          }
        }

        // Add page number links
        for (var number = startPage; number <= endPage; number++) {
          var page = makePage(number, number, number === currentPage);
          pages.push(page);
        }

        // Add links to move between page sets
        if (isMaxSized && ! rotate) {
          if (startPage > 1) {
            var previousPageSet = makePage(startPage - 1, '...', false);
            pages.unshift(previousPageSet);
          }

          if (endPage < totalPages) {
            var nextPageSet = makePage(endPage + 1, '...', false);
            pages.push(nextPageSet);
          }
        }

        return pages;
      }

      var originalRender = paginationCtrl.render;
      paginationCtrl.render = function() {
        originalRender();
        if (scope.page > 0 && scope.page <= scope.totalPages) {
          scope.pages = getPages(scope.page, scope.totalPages);
        }
      };
    }
  };
}])

.constant('uibPagerConfig', {
  itemsPerPage: 10,
  previousText: '« Previous',
  nextText: 'Next »',
  align: true
})

.directive('uibPager', ['uibPagerConfig', function(pagerConfig) {
  return {
    restrict: 'EA',
    scope: {
      totalItems: '=',
      previousText: '@',
      nextText: '@',
      ngDisabled: '='
    },
    require: ['uibPager', '?ngModel'],
    controller: 'UibPaginationController',
    controllerAs: 'pagination',
    templateUrl: function(element, attrs) {
      return attrs.templateUrl || 'template/pagination/pager.html';
    },
    replace: true,
    link: function(scope, element, attrs, ctrls) {
      var paginationCtrl = ctrls[0], ngModelCtrl = ctrls[1];

      if (!ngModelCtrl) {
         return; // do nothing if no ng-model
      }

      scope.align = angular.isDefined(attrs.align) ? scope.$parent.$eval(attrs.align) : pagerConfig.align;
      paginationCtrl.init(ngModelCtrl, pagerConfig);
    }
  };
}]);

/* Deprecated Pagination Below */

angular.module('ui.bootstrap.pagination')
.value('$paginationSuppressWarning', false)
.controller('PaginationController', ['$scope', '$attrs', '$parse', '$log', '$paginationSuppressWarning', function($scope, $attrs, $parse, $log, $paginationSuppressWarning) {
  if (!$paginationSuppressWarning) {
    $log.warn('PaginationController is now deprecated. Use UibPaginationController instead.');
  }

  var self = this,
    ngModelCtrl = { $setViewValue: angular.noop }, // nullModelCtrl
    setNumPages = $attrs.numPages ? $parse($attrs.numPages).assign : angular.noop;

  this.init = function(ngModelCtrl_, config) {
    ngModelCtrl = ngModelCtrl_;
    this.config = config;

    ngModelCtrl.$render = function() {
      self.render();
    };

    if ($attrs.itemsPerPage) {
      $scope.$parent.$watch($parse($attrs.itemsPerPage), function(value) {
        self.itemsPerPage = parseInt(value, 10);
        $scope.totalPages = self.calculateTotalPages();
      });
    } else {
      this.itemsPerPage = config.itemsPerPage;
    }

    $scope.$watch('totalItems', function() {
      $scope.totalPages = self.calculateTotalPages();
    });

    $scope.$watch('totalPages', function(value) {
      setNumPages($scope.$parent, value); // Readonly variable

      if ( $scope.page > value ) {
        $scope.selectPage(value);
      } else {
        ngModelCtrl.$render();
      }
    });
  };

  this.calculateTotalPages = function() {
    var totalPages = this.itemsPerPage < 1 ? 1 : Math.ceil($scope.totalItems / this.itemsPerPage);
    return Math.max(totalPages || 0, 1);
  };

  this.render = function() {
    $scope.page = parseInt(ngModelCtrl.$viewValue, 10) || 1;
  };

  $scope.selectPage = function(page, evt) {
    if (evt) {
      evt.preventDefault();
    }

    var clickAllowed = !$scope.ngDisabled || !evt;
    if (clickAllowed && $scope.page !== page && page > 0 && page <= $scope.totalPages) {
      if (evt && evt.target) {
        evt.target.blur();
      }
      ngModelCtrl.$setViewValue(page);
      ngModelCtrl.$render();
    }
  };

  $scope.getText = function(key) {
    return $scope[key + 'Text'] || self.config[key + 'Text'];
  };

  $scope.noPrevious = function() {
    return $scope.page === 1;
  };

  $scope.noNext = function() {
    return $scope.page === $scope.totalPages;
  };
}])
.directive('pagination', ['$parse', 'uibPaginationConfig', '$log', '$paginationSuppressWarning', function($parse, paginationConfig, $log, $paginationSuppressWarning) {
  return {
    restrict: 'EA',
    scope: {
      totalItems: '=',
      firstText: '@',
      previousText: '@',
      nextText: '@',
      lastText: '@',
      ngDisabled:'='
    },
    require: ['pagination', '?ngModel'],
    controller: 'PaginationController',
    controllerAs: 'pagination',
    templateUrl: function(element, attrs) {
      return attrs.templateUrl || 'template/pagination/pagination.html';
    },
    replace: true,
    link: function(scope, element, attrs, ctrls) {
      if (!$paginationSuppressWarning) {
        $log.warn('pagination is now deprecated. Use uib-pagination instead.');
      }
      var paginationCtrl = ctrls[0], ngModelCtrl = ctrls[1];

      if (!ngModelCtrl) {
         return; // do nothing if no ng-model
      }

      // Setup configuration parameters
      var maxSize = angular.isDefined(attrs.maxSize) ? scope.$parent.$eval(attrs.maxSize) : paginationConfig.maxSize,
          rotate = angular.isDefined(attrs.rotate) ? scope.$parent.$eval(attrs.rotate) : paginationConfig.rotate;
      scope.boundaryLinks = angular.isDefined(attrs.boundaryLinks) ? scope.$parent.$eval(attrs.boundaryLinks) : paginationConfig.boundaryLinks;
      scope.directionLinks = angular.isDefined(attrs.directionLinks) ? scope.$parent.$eval(attrs.directionLinks) : paginationConfig.directionLinks;

      paginationCtrl.init(ngModelCtrl, paginationConfig);

      if (attrs.maxSize) {
        scope.$parent.$watch($parse(attrs.maxSize), function(value) {
          maxSize = parseInt(value, 10);
          paginationCtrl.render();
        });
      }

      // Create page object used in template
      function makePage(number, text, isActive) {
        return {
          number: number,
          text: text,
          active: isActive
        };
      }

      function getPages(currentPage, totalPages) {
        var pages = [];

        // Default page limits
        var startPage = 1, endPage = totalPages;
        var isMaxSized = angular.isDefined(maxSize) && maxSize < totalPages;

        // recompute if maxSize
        if (isMaxSized) {
          if (rotate) {
            // Current page is displayed in the middle of the visible ones
            startPage = Math.max(currentPage - Math.floor(maxSize/2), 1);
            endPage   = startPage + maxSize - 1;

            // Adjust if limit is exceeded
            if (endPage > totalPages) {
              endPage   = totalPages;
              startPage = endPage - maxSize + 1;
            }
          } else {
            // Visible pages are paginated with maxSize
            startPage = ((Math.ceil(currentPage / maxSize) - 1) * maxSize) + 1;

            // Adjust last page if limit is exceeded
            endPage = Math.min(startPage + maxSize - 1, totalPages);
          }
        }

        // Add page number links
        for (var number = startPage; number <= endPage; number++) {
          var page = makePage(number, number, number === currentPage);
          pages.push(page);
        }

        // Add links to move between page sets
        if (isMaxSized && ! rotate) {
          if (startPage > 1) {
            var previousPageSet = makePage(startPage - 1, '...', false);
            pages.unshift(previousPageSet);
          }

          if (endPage < totalPages) {
            var nextPageSet = makePage(endPage + 1, '...', false);
            pages.push(nextPageSet);
          }
        }

        return pages;
      }

      var originalRender = paginationCtrl.render;
      paginationCtrl.render = function() {
        originalRender();
        if (scope.page > 0 && scope.page <= scope.totalPages) {
          scope.pages = getPages(scope.page, scope.totalPages);
        }
      };
    }
  };
}])

.directive('pager', ['uibPagerConfig', '$log', '$paginationSuppressWarning', function(pagerConfig, $log, $paginationSuppressWarning) {
  return {
    restrict: 'EA',
    scope: {
      totalItems: '=',
      previousText: '@',
      nextText: '@',
      ngDisabled: '='
    },
    require: ['pager', '?ngModel'],
    controller: 'PaginationController',
    controllerAs: 'pagination',
    templateUrl: function(element, attrs) {
      return attrs.templateUrl || 'template/pagination/pager.html';
    },
    replace: true,
    link: function(scope, element, attrs, ctrls) {
      if (!$paginationSuppressWarning) {
        $log.warn('pager is now deprecated. Use uib-pager instead.');
      }
      var paginationCtrl = ctrls[0], ngModelCtrl = ctrls[1];

      if (!ngModelCtrl) {
         return; // do nothing if no ng-model
      }

      scope.align = angular.isDefined(attrs.align) ? scope.$parent.$eval(attrs.align) : pagerConfig.align;
      paginationCtrl.init(ngModelCtrl, pagerConfig);
    }
  };
}]);
angular.module("template/pagination/pager.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/pagination/pager.html",
    "<ul class=\"pager\">\n" +
    "  <li ng-class=\"{disabled: noPrevious()||ngDisabled, previous: align}\"><a href ng-click=\"selectPage(page - 1, $event)\">{{::getText('previous')}}</a></li>\n" +
    "  <li ng-class=\"{disabled: noNext()||ngDisabled, next: align}\"><a href ng-click=\"selectPage(page + 1, $event)\">{{::getText('next')}}</a></li>\n" +
    "</ul>\n" +
    "");
}]);

angular.module("template/pagination/pagination.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("template/pagination/pagination.html",
    "<ul class=\"pagination\">\n" +
    "  <li ng-if=\"::boundaryLinks\" ng-class=\"{disabled: noPrevious()||ngDisabled}\" class=\"pagination-first\"><a href ng-click=\"selectPage(1, $event)\">{{::getText('first')}}</a></li>\n" +
    "  <li ng-if=\"::directionLinks\" ng-class=\"{disabled: noPrevious()||ngDisabled}\" class=\"pagination-prev\"><a href ng-click=\"selectPage(page - 1, $event)\">{{::getText('previous')}}</a></li>\n" +
    "  <li ng-repeat=\"page in pages track by $index\" ng-class=\"{active: page.active,disabled: ngDisabled&&!page.active}\" class=\"pagination-page\"><a href ng-click=\"selectPage(page.number, $event)\">{{page.text}}</a></li>\n" +
    "  <li ng-if=\"::directionLinks\" ng-class=\"{disabled: noNext()||ngDisabled}\" class=\"pagination-next\"><a href ng-click=\"selectPage(page + 1, $event)\">{{::getText('next')}}</a></li>\n" +
    "  <li ng-if=\"::boundaryLinks\" ng-class=\"{disabled: noNext()||ngDisabled}\" class=\"pagination-last\"><a href ng-click=\"selectPage(totalPages, $event)\">{{::getText('last')}}</a></li>\n" +
    "</ul>\n" +
    "");
}]);

/*
    自定义指令模块
*/
var appCore = angular.module('angular-core',['ui.bootstrap']);

appCore.factory('Message', [function(){
    var M = window.Message || {show:function(){}}
    return M
}]);

/**
 * 编辑器UMeditor
 */
appCore.directive('metaUmeditor', function () {
    return {
        restrict: 'AE',
        scope: {
            config: '=metaUmeditorConfig'
        },
        require: 'ngModel',
        transclude: true,
        link: function (scope, element, attr, ngModel) {
            if(!window.UM){
                return;
            }
            // 获取当前的DOM元素
            var _dom = element[0];
            var modelContent,_updateByRender,editorReady;
            var _placeholder = '';
            var _id = attr.id ? attr.id : "_editor" + Math.floor(Math.random() * 100).toString() + (Date.now());
            
            var _config = scope.config || {
                    // 这里可以选择自己需要的工具按钮名称,此处仅选择如下七个
                    toolbar: ['source undo redo bold italic underline'],
                    // focus时自动清空初始化时的内容
                    autoClearinitialContent: true,
                    // 关闭字数统计
                    wordCount: false,
                    // 关闭elementPath
                    elementPathEnabled: false,
                    // frame高度
                    // initialFrameHeight: 300
                };


            _dom.setAttribute('id', _id);

            var _umeditor = UM.getEditor(_id, _config);

            /**
			 * 对于umeditor添加内容改变事件，内容改变触发ngModel改变.
			 */
            var editorToModel = function () {
                
                if (_umeditor.hasContents()){

                    ngModel.$setViewValue(_umeditor.getContent());
                }
                else{
                    ngModel.$setViewValue(undefined);
                }
                _updateByRender = false;
            };

            /**
             * umeditor准备就绪后，执行逻辑
             * 如果ngModel存在
             *   则给在编辑器中赋值
             *   给编辑器添加内容改变的监听事件.
             * 如果不存在
             *   则写入提示文案
             */

            _umeditor.ready(function () {
                editorReady = true;
                // console.log('ready',ngModel.$viewValue)
                if (ngModel.$viewValue) {
                    _umeditor.setContent(ngModel.$viewValue);
                    
                } else {
                    _umeditor.setContent(_placeholder);
                }
                _umeditor.addListener('contentChange', editorToModel);
                //_umeditor.execCommand('fontsize', '32px');
                //_umeditor.execCommand('fontfamily', '"Microsoft YaHei","微软雅黑"')
            });


            
            function setEditorContent(content){
                //console.log(_umeditor,1,content)
                if(content){
                    _umeditor.setContent(content)
                }
            }
            ngModel.$render = function() {
                _updateByRender = true;
                modelContent = (ngModel.$isEmpty(ngModel.$viewValue) ? "" : ngModel.$viewValue);
                setEditorContent(modelContent);
            };
            
            scope.$on("$destroy", function() {
                if (UM.delEditor) {
                    UM.delEditor(_id);
                }
            });

        }
    }
});
// hover显示与隐藏 <h4 mouse-over-leave hover="hover">测试一下 <span ng-show="hover">显示内容</span></h4>
appCore.directive('mouseOverLeave', function(){
    return {
        restrict: 'A',
        scope: {
            hover: "="
        },
        link: function(scope, elem, attr){
            elem.bind('mouseover', function(){
                elem.css("cursor", "pointer");
                scope.$apply(function(){
                    scope.hover = true;
                });
            });
            elem.bind('mouseleave', function(){
                scope.$apply(function(){
                    scope.hover = false;
                });
            });
        }
    }
});
// 上传 require webuploader.js
appCore.directive('wu',function(){
    return {
            restrict : 'A',
            scope: {
                config: '=wuConfig'
            },
            require: '?ngModel',
            link:function(scope, element, attr, ngModel){
                
                
                var $wu = element.wu();
                
                $wu.on('success',function(event,result){
                  var response = result.response,file = result.file, path = response.path;
                  ngModel.$setViewValue(path)
                });
                $wu.on('delete',function(event,file){
                  ngModel.$setViewValue('')
                });

                ngModel.$render = function() {
                    content = (ngModel.$isEmpty(ngModel.$viewValue) ? "" : ngModel.$viewValue);
                    content && $wu.triggerHandler('files',content);
                };

            }
    }; 
});
// 导入excel webuploader.js
appCore.directive('excel',function(){
    return {
            restrict : 'A',
            require: '?ngModel',
            link:function(scope, element, attr, ngModel){
                
                
                var $wu = element.wu({

                });
                
                $wu.on('success',function(event,result){
                	console.log(result)
                  var response = result.response,file = result.file,message = response.message || '导入成功';
                  var callback = attr['excel'];
                  var data = response.data;
                  var fail = data.fail || 0;
                  var success = data.success || 0;
                  var error = data.error || [];
                  var buffer = [];
                  
                  buffer.push('<div>导入成功：'+success+'条</div>');
                  buffer.push('<div>导入失败：'+fail+'条</div>');
                  for(var i = 0;i<error.length;i++){
                	  buffer.push('<div>序号：'+error[i]["number"]+','+error[i]["errorMessage"]+'</div>');
                  }
                  scope[callback]();
                  if(0 == fail){
                	  dm.alert(buffer.join(''),null,'success');
                  }else {
                	  dm.alert(buffer.join(''),null,'excel');
                  }
                  

                });
                $wu.on('start',function(){
                  dm.loading('正在导入');        
                });
                $wu.on('progress',function(event,progress){
                  console.log('progress',progress)
                  dm.loading(progress);        
                });
                $wu.on('complete',function(){
                  dm.loading('hide');        
                });
                $wu.on('fail',function(event,res){
                  console.log('fail',res)
                  dm.alert('上传失败');        
                });
            }
    }; 
});
// 城市三级选择
appCore.directive('citypicker',function(){
    return {
            restrict : 'A',
            require: '?ngModel',
            link:function(scope, element, attr, ngModel){
                
                var citypicker = element.citypicker();
                
                element.on('fetch',function(){
                  var value = element.val();
                  console.log('citypicker fetch',value)
                  ngModel.$setViewValue(value)
                });
                element.on('change',function(){
                  var value = element.val();
                  //console.log('11 change',value)
                  ngModel.$setViewValue(value)
                });
                
                ngModel.$render = function() {
                    modelContent = (ngModel.$isEmpty(ngModel.$viewValue) ? "" : ngModel.$viewValue);
                    //console.log(33,modelContent)
                    element.val(modelContent)
                    element.trigger('change')
                };
            }
    }; 
});

/**
 * 日历插件 YYYY-MM-DD hh:mm:ss
 */
appCore.directive('laydate', function () {
    return {
        restrict: 'AE',
        scope: {
            config: '=laydateConfig'
        },
        require: 'ngModel',
        link: function (scope, element, attr, ngModel) {
            if(!window.laydate){
                return;
            }
            // 获取当前的DOM元素
            var _dom = element[0];
            
            var _config = scope.config || {
                    
                };
           
            
            element.on('clear',function(){
            	ngModel.$setViewValue('')
            });
            _dom.onclick = function(){
            	 $(document).off('click.clear').on('click.clear','#laydate_clear',function(){
                 	element.trigger('clear');
                 });
            	 laydate({
            		 format: 'YYYY-MM-DD',
            		 istoday: false,
            		 choose: function(datas){ 
	                    // 选择日期完毕的回调
	                    // alert('得到：'+datas);
	                    ngModel.$setViewValue(datas)
	                }
            	 })
            }

            ngModel.$render = function() {
                modelContent = (ngModel.$isEmpty(ngModel.$viewValue) ? "" : ngModel.$viewValue);
                _dom.value = modelContent
            };
            

        }
    }
});



//# sourceMappingURL=angular-ueditor.js.map
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
            scope.$on('$destroy',function(){// 销毁的时候取消事件监听
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
    // var dm = window.dm || {loading:function(){}};
    return {};
}]);

/* 自定义 MNotice */
appCore.factory('mNotice',function(){
    // var dm = window.dm || {notice:function(){}};
    return {};
});
/* 
    自定义ajax 
    @params
        _expire:1405492661238 //过期时间戳

*/
appCore.factory('http', ['$http', '$q','mLoading','Message',
    function($http, $q ,mLoading , Message) {
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
                    	// 登录失效
                    	if(data.code == 10007){
                            var hash = location.hash || '';
                            dm.alert("您的会话已过期，请重新登录",function(){
                            	location.href = '../admin/login?hash=' + hash.replace('#/','');
                            })
                         }
                        message = data.message;
                        // 错误提醒
                        Message.show(message,'error');
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
                    var data = data || {};
                    // console.warn(data)
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
                    // 错误提醒
                    Message.show(config.url+' '+data.message,'error');
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
            var map = API.map || {};

            API.demo = API.demo || '';
            // 假如是实际请求
            if ( location.href.indexOf('localhost')==-1) {
                if(map[url]){
                  url = map[url]
                }else {
                  url = API.root + API.demo + url;
                }   
            }else {
                if(url.indexOf('.json')==-1){
                  url = API.root + API.demo + url + '.json';
                }else {
                  url = API.root + API.demo + url;
                }
                
            }

            return url;
        };
        
        var $resource = function(url, options, actions) {
            var url = url || '',
                options = options || {}, actions = actions || {},
                resourse = {}, params;

            
            resourse = {
                url: url,
                // 加载name文件
                invoke:function(name,data,options){
                    var url = name?this.url + '/' +name:this.url,
                        options = options || {},
                        method = options.method?options.method:'post',
                        data = data || {};

                    url = checkURL(url);
                    return http[method](url, data);

                },
                // 获取列表json数据
                list: function(data, successCallback, failCallback,always) {
                    var url = this.url + '/list',
                        data = data || {};

                    // data[page] = data._page ? data._page : 1;
                    // data[pagesize] = data._pagesize ? data._pagesize : 20;
                    url = checkURL(url);

                    return http.get(url, data ,successCallback, failCallback,always);
                },
                // 请求get.json文件
                get: function(data, successCallback, failCallback,always) {
                    var url = this.url + '/get',
                        data = data || {};

                    url = checkURL(url);
                    return http.get(url, data, successCallback, failCallback,always);
                },
                search: function(data, successCallback, failCallback,always) {
                    var url = this.url + '/search',
                        data = data || {};

                    // data[page] = data._page ? data._page : 1;
                    // data[pagesize] = data._pagesize ? data._pagesize : 20;
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




