(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ProducerController', ProducerController);

    ProducerController.$inject = ['$scope', '$state', 'Producer', 'ProducerSearch'];

    function ProducerController ($scope, $state, Producer, ProducerSearch) {
        var vm = this;
        vm.producers = [];
        vm.loadAll = function() {
            Producer.query(function(result) {
                vm.producers = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ProducerSearch.query({query: vm.searchQuery}, function(result) {
                vm.producers = result;
            });
        };
        vm.loadAll();
        
    }
})();
