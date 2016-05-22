(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ViolationController', ViolationController);

    ViolationController.$inject = ['$scope', '$state', 'Violation', 'ViolationSearch'];

    function ViolationController ($scope, $state, Violation, ViolationSearch) {
        var vm = this;
        vm.violations = [];
        vm.loadAll = function() {
            Violation.query(function(result) {
                vm.violations = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ViolationSearch.query({query: vm.searchQuery}, function(result) {
                vm.violations = result;
            });
        };
        vm.loadAll();
        
    }
})();
