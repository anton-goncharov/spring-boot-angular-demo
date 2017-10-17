'use strict';

angular.module('calorieCounterApp')
    .config(function ($stateProvider) {
        $stateProvider.state('accessdenied', {
            parent: 'site',
            url: '/accessdenied',
            data: {
                roles: []
            },
            views: {
                'content@': {
                    templateUrl: 'templates/accessdenied.html'
                }
            },
            resolve: {}
        });
    });
