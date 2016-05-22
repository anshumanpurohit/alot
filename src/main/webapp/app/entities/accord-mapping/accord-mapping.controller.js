(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('AccordMappingController', AccordMappingController);

    AccordMappingController.$inject = ['$scope', '$state', 'AccordMapping', 'AccordMappingSearch'];

    function AccordMappingController ($scope, $state, AccordMapping, AccordMappingSearch) {
        var vm = this;
        vm.accordMappings = [];
        vm.loadAll = function() {
            AccordMapping.query(function(result) {
                vm.accordMappings = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            AccordMappingSearch.query({query: vm.searchQuery}, function(result) {
                vm.accordMappings = result;
            });
        };
        vm.loadAll();
        
    }
})();
