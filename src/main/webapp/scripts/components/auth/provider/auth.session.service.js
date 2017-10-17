'use strict';

angular.module('calorieCounterApp')
    .factory('AuthServerProvider', ['$http', function loginService($http) {
        return {
            login: function(credentials) {
                var data = 'email=' + encodeURIComponent(credentials.username) +
                    '&password=' + encodeURIComponent(credentials.password) +
                    '&remember-me=' + credentials.rememberMe + '&submit=Login';
                return $http.post('auth/login', data, {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'                        
                    }
                }).success(function (response) {
                    return response;
                });
            },
            logout: function() {
                // logout from the server
                $http.post('auth/logout').success(function (response) {
                    // to get a new csrf token call the api
                    $http.get('auth/account');
                    return response;
                });
            }
        };
    }]);
