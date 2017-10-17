'use strict';

angular.module('calorieCounterApp')
    .factory('Register', ['$resource', function Account($resource) {
        return $resource('auth/register')}])
    .factory('Auth', function Auth($rootScope, $state, $q, Principal, AuthServerProvider, Account, Register) {
        return {
            login: function (credentials, callback) {
                var cb = callback || angular.noop;
                var deferred = $q.defer();

                AuthServerProvider.login(credentials).then(function (data) {
                    // retrieve the logged account information
                    Principal.identity(true).then(function(account) {
                        deferred.resolve(data);
                    });
                    return cb();
                }).catch(function (err) {
                    this.logout();
                    deferred.reject(err);
                    return cb(err);
                }.bind(this));

                return deferred.promise;
            },

            logout: function () {
                AuthServerProvider.logout();
                Principal.authenticate(null);
                // reset state memory
                $rootScope.previousStateName = undefined;
                $rootScope.previousStateNameParams = undefined;
            },

            authorize: function(force) {
                return Principal.identity(force)
                    .then(function() {
                        var isAuthenticated = Principal.isAuthenticated();
                        if (!isAuthenticated && $rootScope.toState.data.roles.length != 0) {
                            $state.go('login');
                        }
                        // an authenticated user shouldn't have access to login and register pages
                        if (isAuthenticated && ($rootScope.toState.name === 'login' || $rootScope.toState.name === 'register')) {
                            $state.go('dashboard');
                        }

                        //if ($rootScope.toState.data.assignedRole && $rootScope.toState.data.assignedRole.length > 0 && !Principal.hasAnyAuthority($rootScope.toState.data.assignedRole)) {
                        if ($rootScope.toState.data.roles.length > 0 && !Principal.hasAnyAuthority($rootScope.toState.data.roles)) {
                            if (isAuthenticated) {
                                // user is signed in but not authorized for target state
                                $state.go('accessdenied');
                            }
                            else {
                                // user is not authenticated. remember the state they wanted before
                                // we send them to the login state, so you can return them when you're done
                                $rootScope.previousStateName = $rootScope.toState;
                                $rootScope.previousStateNameParams = $rootScope.toStateParams;

                                $state.go('login');
                            }
                        }
                    });
            },

            createAccount: function(data, callback) {
                var cb = callback || angular.noop;

                return Register.save(data,
                    function () {
                        return cb(data);
                    },
                    function (err) {
                        console.log(err);
                        this.logout();
                        return cb(err);
                    }.bind(this)).$promise;
            }
        };
    });
