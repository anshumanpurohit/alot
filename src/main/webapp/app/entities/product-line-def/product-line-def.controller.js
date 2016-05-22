(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ProductLineDefController', ProductLineDefController);

    ProductLineDefController.$inject = ['$scope', '$state', 'ProductLineDef', 'ProductLineDefSearch'];

    function ProductLineDefController ($scope, $state, ProductLineDef, ProductLineDefSearch) {
        var vm = this;
        vm.productLineDefs = [];
        vm.loadAll = function() {
            ProductLineDef.query(function(result) {
                vm.productLineDefs = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ProductLineDefSearch.query({query: vm.searchQuery}, function(result) {
                vm.productLineDefs = result;
            });
        };
        vm.loadAll();
        
    }
})();
