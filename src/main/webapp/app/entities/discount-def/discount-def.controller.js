(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('DiscountDefController', DiscountDefController);

    DiscountDefController.$inject = ['$scope', '$state', 'DiscountDef', 'DiscountDefSearch'];

    function DiscountDefController ($scope, $state, DiscountDef, DiscountDefSearch) {
        var vm = this;
        vm.discountDefs = [];
        vm.loadAll = function() {
            DiscountDef.query(function(result) {
                vm.discountDefs = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            DiscountDefSearch.query({query: vm.searchQuery}, function(result) {
                vm.discountDefs = result;
            });
        };
        vm.loadAll();
        
    }
})();
