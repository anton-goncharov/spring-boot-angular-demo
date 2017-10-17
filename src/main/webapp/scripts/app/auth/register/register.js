'use strict';

angular.module('calorieCounterApp')
    .config(function ($stateProvider) {
        $stateProvider.state('register', {
            parent: 'site',
            url: '/register',
            data: {
                roles: [],
                pageTitle: 'Sign Up'
            },
            views: {
                'content@': {
                    templateUrl: 'templates/register.html',
                    controller: 'RegisterController'
                }
            },
            resolve: {}
        });
    });
