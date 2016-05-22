(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CarrierController', CarrierController);

    CarrierController.$inject = ['$scope', '$state', 'Carrier', 'CarrierSearch'];

    function CarrierController ($scope, $state, Carrier, CarrierSearch) {
        var vm = this;
        vm.carriers = [];
        vm.loadAll = function() {
            Carrier.query(function(result) {
                vm.carriers = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CarrierSearch.query({query: vm.searchQuery}, function(result) {
                vm.carriers = result;
            });
        };
        vm.loadAll();
        
    }
})();
