(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ProductLineController', ProductLineController);

    ProductLineController.$inject = ['$scope', '$state', 'ProductLine', 'ProductLineSearch'];

    function ProductLineController ($scope, $state, ProductLine, ProductLineSearch) {
        var vm = this;
        vm.productLines = [];
        vm.loadAll = function() {
            ProductLine.query(function(result) {
                vm.productLines = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ProductLineSearch.query({query: vm.searchQuery}, function(result) {
                vm.productLines = result;
            });
        };
        vm.loadAll();
        
    }
})();
