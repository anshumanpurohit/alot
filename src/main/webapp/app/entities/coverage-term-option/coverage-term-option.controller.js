(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageTermOptionController', CoverageTermOptionController);

    CoverageTermOptionController.$inject = ['$scope', '$state', 'CoverageTermOption', 'CoverageTermOptionSearch'];

    function CoverageTermOptionController ($scope, $state, CoverageTermOption, CoverageTermOptionSearch) {
        var vm = this;
        vm.coverageTermOptions = [];
        vm.loadAll = function() {
            CoverageTermOption.query(function(result) {
                vm.coverageTermOptions = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CoverageTermOptionSearch.query({query: vm.searchQuery}, function(result) {
                vm.coverageTermOptions = result;
            });
        };
        vm.loadAll();
        
    }
})();
