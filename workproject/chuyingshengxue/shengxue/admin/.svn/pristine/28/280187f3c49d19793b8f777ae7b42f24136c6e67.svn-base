/* 
    大纲设置 
*/
angular.module('awApp').controller("syllabusSetCtrl", ["$scope", "$state" ,"$stateParams","resource",function($scope, state,stateParams,resource) {
    var subjectDao = resource('/subject');
    var syllabusDao = resource('/syllabus');
    var tempNode = {test:true,syllabusParentId:-1,syllabusName:'保留节点',syllabusId:-100};
    // 遍历树寻找节点
    var GetNodeByID = function(Data,ID)
    {

        var Deep,T,F;
        if(ID==-1)return Data;
        for (F = Data.length;F;)
        {
            T = Data[--F]
            if (ID === T.syllabusId) return T
            if (T.children)
            {
                Deep = GetNodeByID(T.children,ID)
                if (Deep) return Deep
            }
        }
        return Data;
    };
    $scope.dataForTheTree = [];
    $scope.currentSubject = {};
    $scope.treeOptions = {
        nodeChildren: "children",
        dirSelectable: true,
        injectClasses: {
          li: "outline-item",
          liSelected: "a7",
          iExpanded: "outline-menu fa fa-minus",
          iCollapsed: "outline-menu add fa fa-plus",
          iLeaf: "",
          label: "a6",
          labelSelected: "c-blue"
      }
    };
    $scope.toAddView = function(target){
        $scope.formData = {
            syllabusLeaf:0,
            syllabusOrder:0
        };
        $scope.isAdd = true;
        // 放在某个节点下面
        if(target){
            $scope.formData.syllabusParentId = target.syllabusId;
            $scope.currentNode = target;
        }else {
            $scope.currentNode = {
                syllabusName:'根'
            };
        }
    };
    $scope.toDelView = function(node){
        var syllabusId = node.syllabusId;
        var syllabusParentId = node.syllabusParentId;
        var list = [];

        if(syllabusParentId==-1){
            list = $scope.dataForTheTree;
        }else {
            var parentNode = GetNodeByID($scope.dataForTheTree,syllabusParentId);
            console.log('parentNode',parentNode)
            list = parentNode.children || [];
        }

        
        dm.confirm('确认删除?',function(){
            syllabusDao.invoke('delete',{syllabusId:syllabusId}).then(function(){
                Message.show('删除成功','success');
                angular.forEach(list,function(node,i){
                    if(node.syllabusId == syllabusId){
                        list.splice(i,1);
                    }
                });
            })
        });
    };
    $scope.toEditView = function(target){
        $scope.isAdd = false;
        $scope.formData = angular.extend({},target);
        
    };
    $scope.saveSyllabus = function(){
        var formData = $scope.formData;
        var syllabusSubjectId = $scope.currentSubject.subjectId;


        if(!syllabusSubjectId){
            Message.show('不存在科目','warning');
            return;
        }

        if($scope.isAdd){
            formData.syllabusSubjectId = syllabusSubjectId;
            formData.syllabusParentId = $scope.currentNode.syllabusId?$scope.currentNode.syllabusId:-1;
            // create
            syllabusDao.invoke('create',formData,'post').then(function(res){
                var data = res.data || {};
                console.log('create',res,formData,$scope.currentNode)
                if(formData.syllabusLeaf==0){formData.children=[]}
                formData.syllabusId = data.syllabusId || '-1';
                console.log('node',formData);
                if($scope.currentNode.syllabusId){

                    $scope.currentNode.children = $scope.currentNode.children?$scope.currentNode.children:[];
                    $scope.currentNode.children.push(formData);
                }else {

                    $scope.dataForTheTree.push(formData);

                }
                console.log('dataForTheTree',$scope.dataForTheTree)
                Message.show('创建成功','success');
            })
        }else {

            syllabusDao.invoke('update',formData,'post').then(function(res){
                Message.show('修改成功','success');
                $scope.currentNode.syllabusName = formData.syllabusName;
                $scope.currentNode.syllabusOrder = formData.syllabusOrder;
            })
        }

    };
    $scope.showSelected = function(node){
        $scope.currentNode = node;
        console.log('showSelected',node)
    };
    
    
    
    $scope.selectSubject = function(target){
        angular.forEach($scope.subjectList,function(item){
            item._selected = false;
        });
        target._selected = true;
        var syllabusSubjectId = target.subjectId;
        $scope.currentSubject = target;
        syllabusDao.invoke('listAll',{syllabusSubjectId:syllabusSubjectId,syllabusParentId:-1}).then(function(res){
            var list = res.data || [];

            $scope.dataForTheTree = list.length?list:[tempNode];

        });
    };
    var init = function(){
        subjectDao.invoke('list',{}).then(function(res){
            var list = res.data.list || [];
            angular.forEach(list,function(item){
                item._selected = false;
            });
            $scope.subjectList = list;
            list.length && $scope.selectSubject(list[0]);
        });
    };

    init();
    

}]);
