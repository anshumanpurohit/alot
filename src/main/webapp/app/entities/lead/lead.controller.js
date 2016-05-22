(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('LeadController', LeadController);

    LeadController.$inject = ['$scope', '$state', 'Lead', 'LeadSearch'];

    function LeadController ($scope, $state, Lead, LeadSearch) {
        var vm = this;
        vm.leads = [];
        vm.loadAll = function() {
            Lead.query(function(result) {
                vm.leads = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            LeadSearch.query({query: vm.searchQuery}, function(result) {
                vm.leads = result;
            });
        };
        vm.loadAll();
        
    }
})();
