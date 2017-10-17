'use strict';

angular.module('calorieCounterApp')
    .factory('Users', ['$resource', function Users($resource) {
        return $resource('api/v1/user/:id', {}, {
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

