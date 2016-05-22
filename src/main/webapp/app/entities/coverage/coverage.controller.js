(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageController', CoverageController);

    CoverageController.$inject = ['$scope', '$state', 'Coverage', 'CoverageSearch'];

    function CoverageController ($scope, $state, Coverage, CoverageSearch) {
        var vm = this;
        vm.coverages = [];
        vm.loadAll = function() {
            Coverage.query(function(result) {
                vm.coverages = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CoverageSearch.query({query: vm.searchQuery}, function(result) {
                vm.coverages = result;
            });
        };
        vm.loadAll();
        
    }
})();
