'use strict';

angular.module('calorieCounterApp')
    .factory('UserSettings', ['$resource', function UserSettings($resource) {
        return $resource('api/v1/usersettings', {}, {
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': {
                method: 'POST',
                headers: {'Content-Type': undefined},
                transformRequest: function(data)  {
                    var fd = new FormData();
                    angular.forEach(data, function(value, key) {
                        fd.append(key, value)
                    });
                    return fd;
                }
            }
        })

    }]);

