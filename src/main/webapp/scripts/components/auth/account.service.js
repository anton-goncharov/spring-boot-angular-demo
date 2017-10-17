'use strict';

angular.module('calorieCounterApp')
    .factory('Account', ['$resource', function Account($resource) {
        return $resource('auth/account', {}, {
            'get': { method: 'GET', params: {}, isArray: false,
                interceptor: {
                    response: function(response) {
                        // expose response
                        return response;
                    }
                }
            }
        });
    }]);
