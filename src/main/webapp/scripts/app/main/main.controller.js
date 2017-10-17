'use strict';

angular.module('calorieCounterApp')
    .controller('MainController', function ($scope, $state, Principal) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
            if ($scope.isAuthenticated()) {
            	$state.go('dashboard');
            } else {
                $state.go('login');
            }
        });
        
    });
