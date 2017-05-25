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
            $http.get(urlBase + '/calculations').success(function (data) {
                if (data._embedded != undefined) {
                    $scope.calculations = data._embedded.calculations;
                } else {
                    $scope.calculations = [];
                }
                $scope.expression = "";
                $scope.result = "";
            });
        }

        // findAllCalculations();

        $scope.calculate = function calculate() {
            if ($scope.expression == "") {
                alert("Wprowadź wyrażenie");
            }
            else {
                $http.post(urlBase + '/v1/calculator/calculate', {
                    expression: $scope.expression
                }).success(function (data, status, headers) {
                    $scope.result = data.string;
                    // Refetching EVERYTHING every time can get expensive over time
                    // Better solution would be to $http.get(headers()["location"]) and add it to the list
                    // findAllCalculations();
                });
            }
        }
    });
