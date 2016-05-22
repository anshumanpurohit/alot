(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ActivityPatternController', ActivityPatternController);

    ActivityPatternController.$inject = ['$scope', '$state', 'ActivityPattern', 'ActivityPatternSearch'];

    function ActivityPatternController ($scope, $state, ActivityPattern, ActivityPatternSearch) {
        var vm = this;
        vm.activityPatterns = [];
        vm.loadAll = function() {
            ActivityPattern.query(function(result) {
                vm.activityPatterns = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ActivityPatternSearch.query({query: vm.searchQuery}, function(result) {
                vm.activityPatterns = result;
            });
        };
        vm.loadAll();
        
    }
})();
