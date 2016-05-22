(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ContactController', ContactController);

    ContactController.$inject = ['$scope', '$state', 'Contact', 'ContactSearch'];

    function ContactController ($scope, $state, Contact, ContactSearch) {
        var vm = this;
        vm.contacts = [];
        vm.loadAll = function() {
            Contact.query(function(result) {
                vm.contacts = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ContactSearch.query({query: vm.searchQuery}, function(result) {
                vm.contacts = result;
            });
        };
        vm.loadAll();
        
    }
})();
