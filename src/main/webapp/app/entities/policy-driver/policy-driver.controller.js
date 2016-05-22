(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('PolicyDriverController', PolicyDriverController);

    PolicyDriverController.$inject = ['$scope', '$state', 'PolicyDriver', 'PolicyDriverSearch'];

    function PolicyDriverController ($scope, $state, PolicyDriver, PolicyDriverSearch) {
        var vm = this;
        vm.policyDrivers = [];
        vm.loadAll = function() {
            PolicyDriver.query(function(result) {
                vm.policyDrivers = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PolicyDriverSearch.query({query: vm.searchQuery}, function(result) {
                vm.policyDrivers = result;
            });
        };
        vm.loadAll();
        
    }
})();
