(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('NamedInsuredController', NamedInsuredController);

    NamedInsuredController.$inject = ['$scope', '$state', 'NamedInsured', 'NamedInsuredSearch'];

    function NamedInsuredController ($scope, $state, NamedInsured, NamedInsuredSearch) {
        var vm = this;
        vm.namedInsureds = [];
        vm.loadAll = function() {
            NamedInsured.query(function(result) {
                vm.namedInsureds = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            NamedInsuredSearch.query({query: vm.searchQuery}, function(result) {
                vm.namedInsureds = result;
            });
        };
        vm.loadAll();
        
    }
})();
