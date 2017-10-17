'use strict';

angular.module('calorieCounterApp')
    .controller('DashboardController', function ($scope, $rootScope, $state, $location, Auth, Principal, Meal, MealFilter, UserSettings) {

        // load information about current user
        Principal.identity().then(function(account) {
            $scope.currentUser = account;
        });

        UserSettings.get({},function(data) {
            $scope.userSettings = data;
        }, function(errorResponse) {
            console.log(errorResponse);
        });

        // load user's meals
        $scope.meals = {};
        $scope.page = ($location.search()).page || 0;
        $scope.daysOnPage = ($location.search()).size || 5;

        $scope.loadDashboardMealList = function() {
            Meal.get({page: $scope.page, size: $scope.daysOnPage}, function (result) {
                $scope.meals = result;
                $scope.totalPages = result.totalPages;
            }, function (errorResult) {
                console.log(errorResult);
            });
        };

        $scope.showAddPanel = true;
        $scope.showFilterPanel = false;

        $scope.dateOptions = {
            showWeeks: false,
            initDate: new Date(),
            startingDay: 1  // Monday
        };

        $scope.filter = {
            toDate : new Date(),
            toTime : new Date(),
            fromDate : new Date(),
            fromTime : new Date()
        };

        // datepicker popups
        $scope.openAddDtPopup = function() {
            $scope.addDtPopupOpened = true;
        };
        $scope.openFilterFromPopup = function() {
            $scope.filterFromPopupOpened = true;
        };
        $scope.openFilterToPopup = function() {
            $scope.filterToPopupOpened = true;
        };

        // add new entry & filter buttons
        $scope.onAddNewEntryClick = function() {
            $scope.showAddPanel = !$scope.showAddPanel;
            $scope.showFilterPanel = false;
        };

        $scope.onFilterClick = function() {
            $scope.showFilterPanel = !$scope.showFilterPanel;
            $scope.showAddPanel = false;
        };

        /************ ADD NEW ENTRY ************/
        $scope.initNewEntryForm = function() {
            $scope.addNewEntryForm = {
                name: null,
                calories: null,
                date: new Date(),
                time: new Date()
            };
            $scope.isAddNewEntryValid = false;
        };

        $scope.initNewEntryForm();

        $scope.addNewEntrySubmit = function() {
            $scope.isAddRequestInProgress = true;
            Meal.save($scope.addNewEntryForm, function(result) {
                $scope.loadDashboardMealList();
                $scope.initNewEntryForm();
                $scope.isAddRequestInProgress = false;
            }, function(errorResponse) {
                console.log("error: " + errorResponse);
                $scope.isAddRequestInProgress = false;
            });
        };

        $scope.onAddEntryEdit = function() {
            // verify input values, enable submit button only if everything's ok
            $scope.isAddNewEntryValid = (($scope.addNewEntryForm.name.length > 0) && (/^\d+$/.test($scope.addNewEntryForm.calories)));
        };

        /************ FILTER ************/
        $scope.filter = {
            toDate : new Date(),
            toTime : new Date(),
            fromDate : new Date(),
            fromTime : new Date()
        };

        $scope.isFilterValid = false;
        $scope.isFilterApplied = false;

        $scope.filterApply = function() {
            $scope.isFilterApplied = true;
            $scope.isFilterRequestInProgress = true;
            MealFilter.save({page: $scope.page, size: $scope.daysOnPage}, $scope.filter, function(response) {
                $scope.meals = response;
                $scope.totalPages = response.totalPages;
                // save filter between states
                $rootScope.dashboard = {};
                $rootScope.dashboard.isFilterApplied = true;
                $rootScope.dashboard.filter = $scope.filter;
                $scope.isFilterRequestInProgress = false;
            }, function(errorResponse) {
                console.log(errorResponse);
                $scope.isFilterRequestInProgress = false;
            });
        };

        $scope.filterDiscard = function() {
            // discard global state
            if ($rootScope.dashboard) {
                $rootScope.dashboard.isFilterApplied = null;
                $rootScope.dashboard.filter = null;
            }
            // reload list
            $scope.isFilterApplied = false;
            $scope.loadDashboardMealList();
        };

        /************ TABLE ************/
        $scope.selectedId = null;
        $scope.inEditMode = null;

        $scope.selectRow = function(id) {
            $scope.selectedId = id;
            $scope.inEditMode = null;
        };

        $scope.hasPrevious = function() {
            return ($scope.page < ($scope.totalPages - 1));
        };

        $scope.hasNext = function() {
            return ($scope.page > 0);
        };

        $scope.onEditClick = function(e, meal) {
            e.stopPropagation();
            $scope.inEditMode = meal.id;
            meal.newName = meal.name;
            meal.newCalories = meal.calories;
        };

        $scope.onDeleteClick = function(e,mealId) {
            e.stopPropagation();
            Meal.delete({id : mealId}, function(response) {
                console.log("meal deleted");
                $scope.loadDashboardMealList();
            }, function(errorResponse) {
                console.log("error: " + errorResponse);
            });
        };

        $scope.onEditInputClick = function(e) {
            e.stopPropagation();
        };

        $scope.onSubmitEditClick = function(meal) {
            meal.name = meal.newName;
            meal.calories = meal.newCalories;
            Meal.save({id : meal.id}, meal, function(response) {
                $scope.inEditMode = null;
            }, function(errorResponse) {
                console.log(errorResponse);
            });
        };

        $scope.prev = function(e) {
            e.preventDefault();
            $state.go("dashboard",{page : parseInt($scope.page) + 1});
        };

        $scope.next= function(e) {
            e.preventDefault();
            $state.go("dashboard",{page : parseInt($scope.page) - 1});
        };

        /*************** INIT PAGE **************/
        if ($rootScope.dashboard && $rootScope.dashboard.isFilterApplied) {
            // if filter is applied
            $scope.isFilterApplied = true;
            $scope.filter = $rootScope.dashboard.filter;
            // show filter bar
            $scope.onFilterClick();
            $scope.filterApply();
        } else {
            $scope.loadDashboardMealList();
        }

    });
