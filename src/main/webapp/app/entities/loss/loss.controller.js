(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('LossController', LossController);

    LossController.$inject = ['$scope', '$state', 'Loss', 'LossSearch'];

    function LossController ($scope, $state, Loss, LossSearch) {
        var vm = this;
        vm.losses = [];
        vm.loadAll = function() {
            Loss.query(function(result) {
                vm.losses = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            LossSearch.query({query: vm.searchQuery}, function(result) {
                vm.losses = result;
            });
        };
        vm.loadAll();
        
    }
})();
