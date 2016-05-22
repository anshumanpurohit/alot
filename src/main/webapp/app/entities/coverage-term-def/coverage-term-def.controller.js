(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageTermDefController', CoverageTermDefController);

    CoverageTermDefController.$inject = ['$scope', '$state', 'CoverageTermDef', 'CoverageTermDefSearch'];

    function CoverageTermDefController ($scope, $state, CoverageTermDef, CoverageTermDefSearch) {
        var vm = this;
        vm.coverageTermDefs = [];
        vm.loadAll = function() {
            CoverageTermDef.query(function(result) {
                vm.coverageTermDefs = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CoverageTermDefSearch.query({query: vm.searchQuery}, function(result) {
                vm.coverageTermDefs = result;
            });
        };
        vm.loadAll();
        
    }
})();
