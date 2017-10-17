'use strict';

angular.module('calorieCounterApp')
    .controller('SettingsController', function ($scope, $state, $timeout, $location, Principal, UserSettings) {

        $scope.settings = {
            firstName: null,
            lastName: null,
            expectedCalories: null
        };

        $scope.image = null;

        // load information about current user
        Principal.identity().then(function (account) {
            $scope.currentUser = account;
        });

        UserSettings.get({},function(data) {
            $scope.settings = data;
        }, function(errorResponse) {
            console.log(errorResponse);
        });

        $scope.sendForm = function () {

            UserSettings.save({
                firstName: $scope.settings.firstName,
                lastName: $scope.settings.lastName,
                expectedCalories: $scope.settings.expectedCalories || -1,
                file: $scope.image
            }, function (response) {
                $state.go("main");
            }, function(errorResponse) {
                console.log(errorResponse);
            });

        }

    });
