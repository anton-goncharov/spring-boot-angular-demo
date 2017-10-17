'use strict';

angular.module('calorieCounterApp')
    .config(function ($stateProvider) {
        $stateProvider.state('main', {
            parent: 'site',
            url: '/',
            data: {
                roles: [],
                pageTitle: 'Home'
            },
            views: {
                'content@': {
                    controller: 'MainController'
                }
            },
            resolve: {}
        });
    });
