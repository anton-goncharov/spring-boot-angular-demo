'use strict';

angular.module('calorieCounterApp')
    .config(function ($stateProvider) {
        $stateProvider.state('login', {
            parent: 'site',
            url: '/login',
            data: {
                roles: [],
                pageTitle: 'Sign In'
            },
            views: {
                'content@': {
                    templateUrl: 'templates/login.html',
                    controller: 'LoginController'
                }
            },
            resolve: {}
        });
    });
