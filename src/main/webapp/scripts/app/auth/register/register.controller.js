'use strict';

angular.module('calorieCounterApp')
    .controller('RegisterController', function ($scope, $stateParams, $rootScope, $state, Auth) {
        $scope.success = null;
        $scope.error = null;
        $scope.doNotMatch = null;
        $scope.errorUserExists = null;
        $scope.registerAccount = {
                "user": {
                    "id": null,
                    "email": null,
                    "firstName": null,
                    "lastName": null
                },
                "password": null
            };


        $scope.register = function () {
            if ($scope.registerAccount.password !== $scope.confirmPassword) {
                $scope.doNotMatch = 'ERROR';
            } else {
                $scope.doNotMatch = null;
                $scope.error = null;
                $scope.errorEmailExists = null;

                // create account
                $scope.isRequestInProgress = true;
                Auth.createAccount($scope.registerAccount).then(function () {
                    // and login
                    Auth.login({
                        username: $scope.registerAccount.user.email,
                        password: $scope.registerAccount.password,
                        rememberMe: false
                    }).then(function () {
                        $scope.isRequestInProgress = false;
                        // go to the dashboard page
                        $state.go('dashboard');
                    }).catch(function () {
                        $scope.isRequestInProgress = false;
                    });
                }).catch(function (response) {
                    $scope.isRequestInProgress = false;
                    $scope.success = null;
                    if (response.status === 409) {
                        $scope.errorUserExists = true;
                    } else {
                        $scope.error = true;
                    }
                });
            }
        };

    });
