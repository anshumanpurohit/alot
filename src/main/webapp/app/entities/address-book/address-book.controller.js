(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('AddressBookController', AddressBookController);

    AddressBookController.$inject = ['$scope', '$state', 'AddressBook', 'AddressBookSearch'];

    function AddressBookController ($scope, $state, AddressBook, AddressBookSearch) {
        var vm = this;
        vm.addressBooks = [];
        vm.loadAll = function() {
            AddressBook.query(function(result) {
                vm.addressBooks = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            AddressBookSearch.query({query: vm.searchQuery}, function(result) {
                vm.addressBooks = result;
            });
        };
        vm.loadAll();
        
    }
})();
