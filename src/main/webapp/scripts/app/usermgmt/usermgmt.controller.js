'use strict';

angular.module('calorieCounterApp')
    .controller('UserMgmtController', function ($scope, $state, $location, Principal, Users) {

        $scope.isAdmin = false;

        // load information about current user
        Principal.identity().then(function(account) {
            $scope.currentUser = account;
        });

        if (Principal.hasAnyAuthority(['ROLE_ADMIN'])) {
            $scope.isAdmin = true;
        }

        $scope.userList = [];

        Users.query(function(result) {
            // returns list of users
            $scope.userList = result;
        }, function(errorResponse) {
            console.log(errorResponse);
        });

        $scope.hasRole = function(user, role) {
            for (var i = 0; i < user.assignedRole.length; i++) {
                if (user.assignedRole[i].indexOf(role) !== -1) {
                    return true;
                }
            }
            return false;
        };

        $scope.toggleRole = function(user, role) {
            if ($scope.hasRole(user, role)) {
                for (var i = 0; i < user.assignedRole.length; i++) {
                    if (user.assignedRole[i].indexOf(role) !== -1) {
                        user.assignedRole.splice(i,1);
                    }
                }
            } else {
                user.assignedRole.push(role);
            }
            Users.save({id: user.id}, user, function(result) {
                console.log(result);
            }, function(errorResult) {
                console.log(errorResult);
            });
        };

        $scope.blockUser = function(user) {
            user.active = false;
            Users.save({id: user.id}, user, function(result) {
                // OK
            }, function(errorResult) {
                console.log(errorResult);
            });
        };

        $scope.unblockUser = function(user) {
            user.active = true;
            Users.save(user, function(result) {
                // OK
            }, function(errorResult) {
                console.log(errorResult);
            });
        };

    });
