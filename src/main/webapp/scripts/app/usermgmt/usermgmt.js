'use strict';

angular.module('calorieCounterApp')
    .config(function ($stateProvider) {
        $stateProvider.state('usermgmt', {
            parent: 'site',
            url: '/usermgmt',
            data: {
                roles: ['ROLE_MANAGER','ROLE_ADMIN'],
                pageTitle: 'User Management'
            },
            views: {
                'header@': {
                    templateUrl: 'templates/header.html',
                    controller: 'HeaderController'
                },
                'content@': {
                    templateUrl: 'templates/usermgmt.html',
                    controller: 'UserMgmtController'
                }
            },
            resolve: {}
        });
    });
