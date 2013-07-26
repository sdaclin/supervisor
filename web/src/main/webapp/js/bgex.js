moduleBgex = angular.module("BgexModule",['ui.bootstrap']);

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
            template : "<ul ng-hide='isCollapsed'>"+
                            "<li>"+
                                "<i ng-class='{true:{true:\"icon-folder-close\", false :\"icon-folder-open\"}[collapsed],false:\"\"}[hasChildren()]'></i>&nbsp;&nbsp;"+
                                "<span class=\"clickable\" ng-click='collapsed = !collapsed'>{{requirement.id}}"+
                                    "<span class=\"label label-info\" style=\"margin-left : 10px;\" ng-repeat=\"tag in requirement.tags\">{{tag}}</span>"+
                                "</span>"+
                                "<br ng-show=\"requirement.comment\"/>"+
                                "<pre>{{ requirement.comment }}</pre>"+
                            "</li>"+
                        "</ul>",
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

moduleBgex.controller("BgexController",['$scope',function($scope){

    //recuperation du json du projet
    $scope.project = supervisorData;

    $scope.titre = "BGEX";
}]);
