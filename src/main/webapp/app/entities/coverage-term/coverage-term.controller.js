(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageTermController', CoverageTermController);

    CoverageTermController.$inject = ['$scope', '$state', 'CoverageTerm', 'CoverageTermSearch'];

    function CoverageTermController ($scope, $state, CoverageTerm, CoverageTermSearch) {
        var vm = this;
        vm.coverageTerms = [];
        vm.loadAll = function() {
            CoverageTerm.query(function(result) {
                vm.coverageTerms = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CoverageTermSearch.query({query: vm.searchQuery}, function(result) {
                vm.coverageTerms = result;
            });
        };
        vm.loadAll();
        
    }
})();
