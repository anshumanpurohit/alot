(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('JobController', JobController);

    JobController.$inject = ['$scope', '$state', 'Job', 'JobSearch'];

    function JobController ($scope, $state, Job, JobSearch) {
        var vm = this;
        vm.jobs = [];
        vm.loadAll = function() {
            Job.query(function(result) {
                vm.jobs = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            JobSearch.query({query: vm.searchQuery}, function(result) {
                vm.jobs = result;
            });
        };
        vm.loadAll();
        
    }
})();
