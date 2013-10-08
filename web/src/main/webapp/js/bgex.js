moduleBgex = angular.module("BgexModule", ['ui.bootstrap']);

moduleBgex.directive('requirementDisplay', ['$compile', function($compile) {
        var requirement = {
            restrict: 'E',
            replace: true,
            scope: {
                requirement: "=",
                isCollapsed: "="
            },
            template: "<ul ng-hide='isCollapsed'>" +
                    "<li>" +
                    "<i ng-class='{true:{true:\"icon-folder-close\", false :\"icon-folder-open\"}[collapsed],false:\"\"}[hasChildren()]'></i>&nbsp;&nbsp;" +
                    "<span class=\"clickable\" ng-click='collapsed = !collapsed' diff-color='requirement.state'>{{requirement.id}}" +
                    "<span class=\"label label-info\" style=\"margin-left : 10px;\" ng-repeat=\"tag in requirement.tags\">{{tag}}</span>" +
                    "</span>" +
                    "<br ng-show=\"requirement.comment\"/>" +
                    "<pre>{{ requirement.comment }}</pre>" +
                    "</li>" +
                    "</ul>",
            link: function(scopeParent, element, attrs) {

                var scope = scopeParent.$new();
                scope.collapsed = true;

                scope.hasChildren = function() {
                    return angular.isArray(scope.requirement.children) && scope.requirement.children.length > 0;
                }

                if (scope.hasChildren()) {
                    angular.element(element.children()[0]).append("<requirement-display requirement='child' is-collapsed='collapsed' ng-repeat='child in requirement.children'></requirement-display>")
                    $compile(element.contents())(scope);
                }

            }
        }
        return requirement;
    }]);

moduleBgex.directive('diffColor', function() {
        var diffColor = {
            restrict: 'A',
            link: function(scope, element, attrs,model) {
                element.addClass(scope.$eval(attrs.diffColor));
                
            }
        }
        return diffColor;
    });

moduleBgex.directive("rickRoll", function () {
  return {
    link: function (scope, element, attrs) {
      var konami_keys = [38, 38, 40, 40, 37, 39, 37, 39, 66, 65];
      var konami_index = 0;
      element.bind("keydown",function(e){
          if (e.keyCode === konami_keys[konami_index++]) {
              if (konami_index === konami_keys.length) {
                  console.log("Rick rolled");
                  document.location.href="http://rickrolled.fr/";
              }
          } else {
              konami_index = 0;
          }
      });
    }
  };
});

moduleBgex.controller("BgexController", ['$scope', function($scope) {

        //Search an element in tab that equals "element", use "identifier" for checking equality
        var searchForElement = function(tab, element, identifier) {
            for (var i = 0; i < tab.length; i++) {
                if (tab[i][identifier] == element[identifier]) {
                    return tab[i];
                }
            }
            return false;
        }
        
        /**
         * Generic diff function : Creates and returns a new element that represents the difference between the latest version element and the earliest version element
         * @param {type} newElement The latest eleemnt
         * @param {type} oldElement The earliest element
         * @param {type} identifier A string representing the property name of the element identifier
         * @param {type} subElementsField A string representing the property name of the subelements list
         * @param {type} subElementsIdField A string representing the property name of the subelements id
         * @param {type} subElementDiff A function to call to get the diff element of the element's subelements
         * @returns {_L52.getDifferenceGeneric.element} An object that holds the difference between newElement and oldElement
         */
         var getDifferenceGeneric = function(newElement, oldElement,identifier,subElementsField,subElementIdField,subElementDiff){
             
             //prepare the new object holding the difference between newElement and oldElement 
             var element={};

            //if the oldElement does not exist or is not an object
            if(angular.isUndefined(oldElement) || ! angular.isObject(oldElement)){
                //do not go any further, the diff element will simply be newElement flagged as "added"
                element = newElement;
                element.state="added";
            }
            
            //oldElement and newElement are both defined,
            else{
                
                //check if they are identical (ie. same ID)
                if(newElement[identifier] === oldElement[identifier]){
                    //the diff element gets the exact same properties than newElement
                    //BUT its subElements are to be computed later on, so we empty them
                    element = angular.copy(newElement);
                    element[subElementsField] = [];
                    //here, we could additionally check each property change for this element (EXCEPT subElementsField) and mark it "modified" if necessary
                    
                    //check the subelements
                    angular.forEach(newElement[subElementsField],function(child,key){
                        //can we find a match in oldElement's subelements list ?
                        var oldChild = searchForElement(oldElement[subElementsField],child,subElementIdField);
                        var childToAdd = {};

                        //this subelement exists in oldElement
                        if(oldChild !== false){
                            //we need to go deeper... to get the diff element of this sub element
                            childToAdd = subElementDiff(child,oldChild);
                            //parent with modified children should appear as "modified" as well
                            /*if(childToAdd.state=="modified")
                                element.state ="modified";*/
                        }
                        else{
                            //the subelement was not found in oldElement : it's a new subelement
                            childToAdd = angular.copy(child);
                            childToAdd.state="added";
                            //the subelement was added in the latest version -> the current element is flagged as modified
                            element.state="modified";
                        }
                        //add this sub element to the subelement list
                        element[subElementsField].push(childToAdd);
                    });
                    
                    //now check if some subelements from the earliest element have been removed
                    //by checking if each sub element form the earliest element is not in the sub element list of the latest element
                    angular.forEach(oldElement[subElementsField],function(child,key){
                        
                        var foundElement = searchForElement(newElement[subElementsField],child,subElementIdField);
                        if(foundElement === false){
                            //this sub element couldn't be found -> it was removed
                            var oldChildCpy= angular.copy(child);
                            oldChildCpy.state = "removed";
                            //add this sub element, flagged as "removed", to the list
                            element[subElementsField].push(oldChildCpy);
                            //declare the diff element as modified since at least an element has been removed from the earliest element
                            element.state="modified";
                        }
                    });
                }
                //newElement and oldElement differ
                else{
                    element = newElement;
                    console.log("I'm never used :(");
                    element.state="added";
                }
            }
            return element;
        }

        var getDiffPhase = function(newPhase, oldPhase){
            return getDifferenceGeneric(newPhase,oldPhase,"name","artifacts","name",getDiffArtifact);
        }

        var getDiffArtifact = function(newArtifact, oldArtifact){
            return getDifferenceGeneric(newArtifact,oldArtifact,"name","requirements","id",getDiffRequirement);
        }

        var getDiffRequirement = function(newRequirement, oldRequirement){
            return getDifferenceGeneric(newRequirement,oldRequirement,"id","children","id",getDiffRequirement);
        }
        
        //Add or remove a version object to the list of versions to diff
        $scope.setUnsetDate = function(date, version,versionName) {
            
            //if this date is already in datesToDiff, remove it ( = checkbox unchecked )
            var indexDateVersion = -1;
            for(var i =0;i<$scope.diffs[versionName].datesToDiff.length && indexDateVersion===-1;i++){ 
                if($scope.diffs[versionName].datesToDiff[i].date === date){
                    indexDateVersion = i;
                }
            }
            
            if(indexDateVersion > -1){
               $scope.diffs[versionName].datesToDiff.splice(indexDateVersion,1);
            }else{
               $scope.diffs[versionName].datesToDiff.push({'date': date, 'version': version});
            }
            
            if($scope.diffs[versionName].datesToDiff.length >= 2){
                $scope.computeDifferences(versionName);
            }else{
                $scope.diffs[versionName].diff = null;
            }
        }
        
        //controls the appearing of checkboxes
        $scope.startStopDiff = function(version) {
            $scope.diffs[version].active = ! $scope.diffs[version].active;
        }
        
        $scope.isDiffActive = function(version){
            return $scope.diffs[version].active;
        }
        
        var getDate = function(date) {
            var splitDate = date.split("/");
            return new Date(splitDate[2], splitDate[1] - 1, splitDate[0]);
        }

        var getOldest = function(version) {
            var oldest = $scope.diffs[version].datesToDiff[0];
            angular.forEach($scope.diffs[version].datesToDiff, function(value, key) {
                if (getDate(value.date) < getDate(oldest.date))
                    oldest = value;
            });
            return oldest;
        }

        var getNewest = function(version) {
            var newest = $scope.diffs[version].datesToDiff[0];
            angular.forEach($scope.diffs[version].datesToDiff, function(value, key) {
                if (getDate(value.date) > getDate(newest.date))
                    newest = value;
            });
            return newest;
        }
        
        $scope.computeDifferences = function(version) {
            var oldestVersion = getOldest(version).version;
            var newestVersion = getNewest(version).version;

            console.log("Oldest : " + oldestVersion);
            console.log("Newest : " + newestVersion);

            //for each phase, compute its diff
            var diffPhases = [];
            angular.forEach(newestVersion.phases,function(newPhase,key){
                 var oldPhase = searchForElement(oldestVersion.phases,newPhase,"name");
                 diffPhases.push(getDiffPhase(newPhase,oldPhase));
            });

            //compute the requirement tree diff
            var rootRequirement = getDiffRequirement(newestVersion.rootRequirement, oldestVersion.rootRequirement);

            $scope.diffs[version].diff={phases : diffPhases, rootRequirement : rootRequirement};          
            console.log( $scope.diffs[version].diff);
        }
        
        /**
         * Initialisation
         */
        
        //recuperation du json du projet
        $scope.project = supervisorData;
        
        $scope.titre = "BGEX";
        
        $scope.diffs = {};
        //creation des variabes stockant les diffs
        angular.forEach($scope.project.versions,function(mapDateVersion,nomVersion){
            $scope.diffs[nomVersion] = {
                active :false,
                datesToDiff : []   
            };  
       });
       
    }]);