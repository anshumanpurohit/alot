(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('PolicyContactController', PolicyContactController);

    PolicyContactController.$inject = ['$scope', '$state', 'PolicyContact', 'PolicyContactSearch'];

    function PolicyContactController ($scope, $state, PolicyContact, PolicyContactSearch) {
        var vm = this;
        vm.policyContacts = [];
        vm.loadAll = function() {
            PolicyContact.query(function(result) {
                vm.policyContacts = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PolicyContactSearch.query({query: vm.searchQuery}, function(result) {
                vm.policyContacts = result;
            });
        };
        vm.loadAll();
        
    }
})();
