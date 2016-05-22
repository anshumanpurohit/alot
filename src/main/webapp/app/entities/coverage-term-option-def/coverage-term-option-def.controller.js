(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageTermOptionDefController', CoverageTermOptionDefController);

    CoverageTermOptionDefController.$inject = ['$scope', '$state', 'CoverageTermOptionDef', 'CoverageTermOptionDefSearch'];

    function CoverageTermOptionDefController ($scope, $state, CoverageTermOptionDef, CoverageTermOptionDefSearch) {
        var vm = this;
        vm.coverageTermOptionDefs = [];
        vm.loadAll = function() {
            CoverageTermOptionDef.query(function(result) {
                vm.coverageTermOptionDefs = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CoverageTermOptionDefSearch.query({query: vm.searchQuery}, function(result) {
                vm.coverageTermOptionDefs = result;
            });
        };
        vm.loadAll();
        
    }
})();
