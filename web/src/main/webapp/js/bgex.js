moduleBgex = angular.module("BgexModule",['ngResource','ui.bootstrap']);

/*moduleBgex.directive('requirementDisplay', ['$compile',function(TYPE_ACTION) {
        var requirement = {
            restrict: 'E',
            replace: true,
            template : "<h6>{{requirement.id}}</h6><requirement-display requirement='requirement' ng-repeat='requirement in requirement.children'></requirement-display>",
            scope : {
                requirement:"="
            },
            link: function(scope, element, attrs) {
               console.log("Requirement : "+scope.requirement);
            }
        }
        return requirement;
    }]);
*/

moduleBgex.directive('requirementDisplay', ['$compile',function($compile) {
        var requirement = {
            restrict: 'E',
            replace: true,
            scope : {
                requirement:"=",
                isCollapsed:"="
            },
            template : "<ul collapse='isCollapsed'><li><span class=\"clickable\" ng-click='collapsed = !collapsed'>{{requirement.id}}</span><i ng-class='{true:{true:\"icon-chevron-down\", false :\"icon-chevron-up\"}[collapsed],false:\"\"}[hasChildren()]'></i></li></ul>",
            link: function(scopeParent, element, attrs) {   
                
                var scope = scopeParent.$new();      
                scope.collapsed = true;
                
                scope.hasChildren = function(){
                    return angular.isArray(scope.requirement.children) && scope.requirement.children.length > 0;
                }
                
                if(scope.hasChildren()){
                    angular.element(element.children()[0]).append("<requirement-display requirement='child' is-collapsed='collapsed' ng-repeat='child in requirement.children'></requirement-display>")
                    $compile(element.contents())(scope);
                }

            }
        }
        return requirement;
    }]);

moduleBgex.controller("BgexController",['$scope','$http',function($scope,$http){
    
    //recuperation du json du projet
    $scope.project = supervisorData;
  
    $scope.titre = "BGEX";
}]);
