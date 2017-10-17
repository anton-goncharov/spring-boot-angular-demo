'use strict';

angular.module('calorieCounterApp')
    .config(function ($stateProvider) {
        $stateProvider.state('dashboard', {
            parent: 'site',
            url: '/dashboard?page&size',
            data: {
                roles: ['ROLE_USER','ROLE_MANAGER','ROLE_ADMIN'],
                pageTitle: 'Dashboard'
            },
            views: {
                'header@': {
                    templateUrl: 'templates/header.html',
                    controller: 'HeaderController'
                },
                'content@': {
                    templateUrl: 'templates/dashboard.html',
                    controller: 'DashboardController'
                }
            },
            resolve: {}
        });
    });
