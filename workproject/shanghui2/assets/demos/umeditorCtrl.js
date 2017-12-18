/* 
     
*/
angular.module('awApp').controller("umeditorCtrl", ["$scope", "$state" ,"auther",function($scope, state,auther) {
    
    console.log('umeditor controller',auther)
      
    $scope.nothing = 'xx';
    $scope.all_config = {
      initialFrameHeight:200,
      toolbar:[
        'source | undo redo | bold italic underline strikethrough | superscript subscript | forecolor backcolor | removeformat |',
        'insertorderedlist insertunorderedlist | selectall cleardoc paragraph | fontfamily fontsize' ,
        '| justifyleft justifycenter justifyright justifyjustify |',
        'link unlink |  image  ',
        '| horizontal print preview  fullscreen', 'drafts', 'formula'
      ]
    };

}]);
