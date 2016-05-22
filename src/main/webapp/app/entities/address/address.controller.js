(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('AddressController', AddressController);

    AddressController.$inject = ['$scope', '$state', 'Address', 'AddressSearch'];

    function AddressController ($scope, $state, Address, AddressSearch) {
        var vm = this;
        vm.addresses = [];
        vm.loadAll = function() {
            Address.query(function(result) {
                vm.addresses = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            AddressSearch.query({query: vm.searchQuery}, function(result) {
                vm.addresses = result;
            });
        };
        vm.loadAll();
        
    }
})();
