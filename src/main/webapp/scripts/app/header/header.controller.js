'use strict';

angular.module('calorieCounterApp')
    .controller('HeaderController', function ($scope, $state, $location, Principal, Auth, UserSettings) {

        $scope.isManager = false;

        // load information about current user
        Principal.identity().then(function(account) {
            $scope.currentUser = account;

        });

        if (Principal.hasAnyAuthority(['ROLE_MANAGER','ROLE_ADMIN'])) {
            $scope.isManager = true;
        }

        UserSettings.get({},function(data) {
            $scope.userSettings = data;
        }, function(errorResponse) {
            console.log(errorResponse);
        });

        $scope.logout = function(e) {
            e.preventDefault();
            Auth.logout();
            $state.go('main');
        };

    });
