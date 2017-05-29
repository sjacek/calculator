/**
 * Created by jacek.sztajnke on 2017-05-22.
 */

app.controller('calculationController', function ($scope, $http) {
    var urlBase = "";
    $scope.show_previous = false;
    $scope.show_integral= false;
    $scope.selection = [];
    $http.defaults.headers.post["Content-Type"] = "application/json";

    function findAllCalculations() {
        //get all calculations and display initially
        $http.get(urlBase + '/calculations').success(function (data) {
            if (data._embedded !== undefined) {
                $scope.calculations = data._embedded.calculations;
            } else {
                $scope.calculations = [];
            }
        });
    }

    findAllCalculations();

    $scope.calculate = function() {
        if ($scope.expression === "") {
            alert("Input expression");
            return;
        }

        $http.post(urlBase + '/v1/calculator/calculate', {
            expression: $scope.expression
        }).success(function (data, status, headers) {
            $scope.result = data.string;
            // Refetching EVERYTHING every time can get expensive over time
            // Better solution would be to $http.get(headers()["location"]) and add it to the list
        });
    };

    $scope.calculateIntegral = function() {
        if ($scope.expression === "") {
            alert("Input parameters");
            return;
        }

        $http.post(urlBase + '/v1/calculator/calculateIntegral', {
            intervalBegin: $scope.intervalBegin,
            intervalEnd: $scope.intervalEnd,
            threads: $scope.threads,
            repetitions: $scope.repetitions
        }).success(function (data, status, headers) {
            $scope.integralResult = data.string;
            // Refetching EVERYTHING every time can get expensive over time
            // Better solution would be to $http.get(headers()["location"]) and add it to the list

        });
    }
});
