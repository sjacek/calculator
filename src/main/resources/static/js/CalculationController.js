/**
 * Created by jacek.sztajnke on 2017-05-22.
 */

app.controller('calculationController', function ($scope, $http) {

        var urlBase = "";
        $scope.toggle = true;
        $scope.selection = [];
        $http.defaults.headers.post["Content-Type"] = "application/json";
        //
        function findAllCalculations() {
            //get all calculations and display initially
            $http.get(urlBase + '/calculations/search/findAll').success(function (data) {
                if (data._embedded != undefined) {
                    $scope.calculations = data._embedded.calculations;
                } else {
                    $scope.calculations = [];
                }
                for (var i = 0; i < $scope.calculations.length; i++) {
                    if ($scope.calculations[i].taskStatus == 'COMPLETED') {
                        $scope.selection.push($scope.calculations[i].taskId);
                    }
                }
                $scope.expression = "";
                $scope.result = "";
            });
        }

        findAllCalculations();

        $scope.calculate = function calculate() {
            if ($scope.expression == "") {
                alert("Wprowadź wyrażenie");
            }
            else {
                $http.post(urlBase + '/v1/calculator/calculate', {
                    expression: $scope.expression
                }).success(function (data, status, headers) {
                    $scope.result = data.string;
                    // var newTaskUri = headers()["location"];
                    // console.log("Might be good to GET " + newTaskUri + " and append the task.");
                    // Refetching EVERYTHING every time can get expensive over time
                    // Better solution would be to $http.get(headers()["location"]) and add it to the list
                    findAllCalculations();
                });
            }
        }
        //
        // findAllTasks();
        //
        // //add a new task
        // $scope.addTask = function addTask() {
        //     if($scope.taskName=="" || $scope.taskDesc=="" || $scope.taskPriority == "" || $scope.taskStatus == ""){
        //         alert("Insufficient Data! Please provide values for task name, description, priortiy and status");
        //     }
        //     else{
        //         $http.post(urlBase + '/tasks', {
        //             taskName: $scope.taskName,
        //             taskDescription: $scope.taskDesc,
        //             taskPriority: $scope.taskPriority,
        //             taskStatus: $scope.taskStatus
        //         }).
        //         success(function(data, status, headers) {
        //             alert("Task added");
        //             var newTaskUri = headers()["location"];
        //             console.log("Might be good to GET " + newTaskUri + " and append the task.");
        //             // Refetching EVERYTHING every time can get expensive over time
        //             // Better solution would be to $http.get(headers()["location"]) and add it to the list
        //             findAllTasks();
        //         });
        //     }
        // };
        //
        // // toggle selection for a given task by task id
        // $scope.toggleSelection = function toggleSelection(taskUri) {
        //     var idx = $scope.selection.indexOf(taskUri);
        //
        //     // is currently selected
        //     // HTTP PATCH to ACTIVE state
        //     if (idx > -1) {
        //         $http.patch(taskUri, { taskStatus: 'ACTIVE' }).
        //         success(function(data) {
        //             alert("Task unmarked");
        //             findAllTasks();
        //         });
        //         $scope.selection.splice(idx, 1);
        //     }
        //
        //     // is newly selected
        //     // HTTP PATCH to COMPLETED state
        //     else {
        //         $http.patch(taskUri, { taskStatus: 'COMPLETED' }).
        //         success(function(data) {
        //             alert("Task marked completed");
        //             findAllTasks();
        //         });
        //         $scope.selection.push(taskUri);
        //     }
        // };
        //
        //
        // // Archive Completed Tasks
        // $scope.archiveTasks = function archiveTasks() {
        //     $scope.selection.forEach(function(taskUri) {
        //         if (taskUri != undefined) {
        //             $http.patch(taskUri, { taskArchived: 1});
        //         }
        //     });
        //     alert("Successfully Archived");
        //     console.log("It's risky to run this without confirming all the patches are done. when.js is great for that");
        //     findAllTasks();
        // };

    });

// //Angularjs Directive for confirm dialog box
// app.directive('ngConfirmClick', [
//     function () {
//         return {
//             link: function (scope, element, attr) {
//                 var msg = attr.ngConfirmClick || "Are you sure?";
//                 var clickAction = attr.confirmedClick;
//                 element.bind('click', function (event) {
//                     if (window.confirm(msg)) {
//                         scope.$eval(clickAction);
//                     }
//                 });
//             }
//         };
//     }]);