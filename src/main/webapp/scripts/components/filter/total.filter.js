angular.module('calorieCounterApp')
    .filter('totalFilter', function () {
        return function (meals) {
            var total = 0;
            for (i = 0; i < meals.length; i++) {
                total = parseInt(total) + parseInt(meals[i].calories);
            }
            return total;
        };
    });
