(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('DiscountController', DiscountController);

    DiscountController.$inject = ['$scope', '$state', 'Discount', 'DiscountSearch'];

    function DiscountController ($scope, $state, Discount, DiscountSearch) {
        var vm = this;
        vm.discounts = [];
        vm.loadAll = function() {
            Discount.query(function(result) {
                vm.discounts = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            DiscountSearch.query({query: vm.searchQuery}, function(result) {
                vm.discounts = result;
            });
        };
        vm.loadAll();
        
    }
})();
