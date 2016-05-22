(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageDefController', CoverageDefController);

    CoverageDefController.$inject = ['$scope', '$state', 'CoverageDef', 'CoverageDefSearch'];

    function CoverageDefController ($scope, $state, CoverageDef, CoverageDefSearch) {
        var vm = this;
        vm.coverageDefs = [];
        vm.loadAll = function() {
            CoverageDef.query(function(result) {
                vm.coverageDefs = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CoverageDefSearch.query({query: vm.searchQuery}, function(result) {
                vm.coverageDefs = result;
            });
        };
        vm.loadAll();
        
    }
})();
