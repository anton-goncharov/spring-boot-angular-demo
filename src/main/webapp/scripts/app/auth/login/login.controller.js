'use strict';

angular.module('calorieCounterApp')
    .controller('LoginController', function ($rootScope, $scope, $state, Auth, Principal) {
        $scope.user = {};
        $scope.errors = {};
        $scope.errorMessage = "";

        Principal.identity().then(function (account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
            if ($scope.isAuthenticated()) {
                $state.go('main');
            }
        });

        $scope.rememberMe = false;

        $scope.login = function (event) {
            event.preventDefault();
            if (!$scope.username || !$scope.password) {
                $scope.authenticationError = true;
                return;
            }
            Auth.login({
                username: $scope.username,
                password: $scope.password,
                rememberMe: $scope.rememberMe
            }).then(function (response) {
                $scope.authenticationError = false;
                $state.go('dashboard');
            }).catch(function (errorResponse) {
                $scope.errorMessage = errorResponse.data.message;
                $scope.authenticationError = true;
            });
        };
    });
