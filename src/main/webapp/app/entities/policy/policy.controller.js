(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('PolicyController', PolicyController);

    PolicyController.$inject = ['$scope', '$state', 'Policy', 'PolicySearch'];

    function PolicyController ($scope, $state, Policy, PolicySearch) {
        var vm = this;
        vm.policies = [];
        vm.loadAll = function() {
            Policy.query(function(result) {
                vm.policies = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PolicySearch.query({query: vm.searchQuery}, function(result) {
                vm.policies = result;
            });
        };
        vm.loadAll();
        
    }
})();
