'use strict';

angular.module('calorieCounterApp')
    .config(function ($stateProvider) {
        $stateProvider.state('settings', {
            parent: 'site',
            url: '/settings',
            data: {
                roles: ['ROLE_USER','ROLE_MANAGER','ROLE_ADMIN'],
                pageTitle: 'Settings'
            },
            views: {
                'header@': {
                    templateUrl: 'templates/header.html',
                    controller: 'HeaderController'
                },
                'content@': {
                    templateUrl: 'templates/settings.html',
                    controller: 'SettingsController'
                }
            },
            resolve: {}
        });
    });
