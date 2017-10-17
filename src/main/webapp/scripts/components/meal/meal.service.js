'use strict';

angular.module('calorieCounterApp')
    .factory('MealFilter', ['$resource', function MealFilter($resource) {
        return $resource('api/v1/meal/filtered')
    }])
    .factory('Meal', ['$resource', function Meal($resource) {
        return $resource('api/v1/meal/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' },
            'save': { method:'POST' },
            'delete': { method:'DELETE' }
        })
    }]);

