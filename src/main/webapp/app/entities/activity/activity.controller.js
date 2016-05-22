(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ActivityController', ActivityController);

    ActivityController.$inject = ['$scope', '$state', 'Activity', 'ActivitySearch'];

    function ActivityController ($scope, $state, Activity, ActivitySearch) {
        var vm = this;
        vm.activities = [];
        vm.loadAll = function() {
            Activity.query(function(result) {
                vm.activities = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ActivitySearch.query({query: vm.searchQuery}, function(result) {
                vm.activities = result;
            });
        };
        vm.loadAll();
        
    }
})();
