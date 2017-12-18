// 生日提醒
angular.module('awApp').controller('birthdayCtrl',['$scope','resource',
    function(scope,resource){
        var birthdayDao = resource('/birthday');
        scope.queryModel = {
            page : 1,
            pageSize : 10,
        }
        scope.query = function(){
            birthdayDao.invoke('list',scope.queryModel).then(function(res){
                var data = res.data;
                var list = data.list || [];
                var pager = data.pager;
                scope.dataList = list;
                scope.pagerOption = pager;
                scope.queryModel.page = pager.page;
                scope.queryModel.autoSendMessage = data.autoMessage;

                angular.forEach(list,function(elem,index){
                    var userIdcard = elem.userIdcard;
                    var birthday = userIdcard.slice(10,14);
                    var month = birthday.slice(0, 2);
                    var date = birthday.slice(2);
                    scope.dataList[index].birthday = month + "月" + date + "日";
                    var dateNumber = elem.dateNumber;
                    scope.dataList[index].dateString = dateNumber + "天";
                    if(dateNumber <= 3){
                        scope.dataList[index].dateClass = 1;
                    }
                });
                scope.autoSet();
            },function(){})
        }
        scope.search = function(){
            scope.queryModel.page = 1;
            scope.query();
        }
        scope.clear = function(){
            scope.queryModel = {
                page : 1,
                pageSize : 10,
            }
        }

        scope.search();

        // 单个发送祝福信息
        scope.sendMessage = function(item){
            scope.formData = {
                realName : '',
                message : "祝你生日快乐！"
            };
            scope.formData.userId = item.userId;
            scope.formData.realName = item.realName;
        }
        scope.send = function(){
            if(scope.formData.realName){
                birthdayDao.invoke("send",scope.formData).then(function(){
                    scope.cbAll = false;
                    scope.queryModel.page = 1;
                    scope.query();
                }, function(){}); 
            }else{
                dm.notice("请选择要发送祝福的会员")
            }
        }
        //批量发送
        scope.cbAll = false;
        scope.cbAllchange = function(){
            var list = scope.dataList || [];
            angular.forEach(list,function(value,key){
                value._selected = scope.cbAll;
            })
        }
        scope.batchDel = function(){
            scope.formData={
                realName : '',
                message : "祝你生日快乐！"
            };
            var realName = [],userId=[];
            var dataList = scope.dataList || [];
            console.log(dataList)
            angular.forEach(dataList,function(elem,index){
                if(elem._selected){
                    realName.push(elem.realName);
                    userId.push(elem.userId);
                }
            })
            if(userId.length){
                scope.formData.userId = userId.join(',');
                scope.formData.realName = realName.join(',');
            }
        }

        // 生日当天自动发送请求
        scope.autoSet = function(){
            birthdayDao.invoke("set",{'autoSendMessage':scope.autoSendMessage}).then(function(res){
                scope.autoSendMessage = res.data.autoMessage;
            },function(){})
        }
        scope.autoSend = function(){
            birthdayDao.invoke('autoSend',{'autoSendMessage':scope.autoSendMessage})
            .then(function(res){
                console.log("自动发送祝福修改成功！");
            },function(){})
        }

        scope.selectArticleType = function(text){
            var el = $('#greetings').get(0);
            var value = $('#greetings').val() || '';
            var pos = 0;
            if ('selectionStart' in el) {
                pos = el.selectionStart;
            } else if ('selection' in document) {
                el.focus();
                var Sel = document.selection.createRange();
                var SelLength = document.selection.createRange().text.length;
                Sel.moveStart('character', -el.value.length);
                pos = Sel.text.length - SelLength;
            }
            console.log(pos)
            var arry = value.split('');
            arry.splice(pos,0,text);
            $('#greetings').val(arry.join(""));
        }
    }
]);