(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('PersonalAutoVehicleController', PersonalAutoVehicleController);

    PersonalAutoVehicleController.$inject = ['$scope', '$state', 'PersonalAutoVehicle', 'PersonalAutoVehicleSearch'];

    function PersonalAutoVehicleController ($scope, $state, PersonalAutoVehicle, PersonalAutoVehicleSearch) {
        var vm = this;
        vm.personalAutoVehicles = [];
        vm.loadAll = function() {
            PersonalAutoVehicle.query(function(result) {
                vm.personalAutoVehicles = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PersonalAutoVehicleSearch.query({query: vm.searchQuery}, function(result) {
                vm.personalAutoVehicles = result;
            });
        };
        vm.loadAll();
        
    }
})();
