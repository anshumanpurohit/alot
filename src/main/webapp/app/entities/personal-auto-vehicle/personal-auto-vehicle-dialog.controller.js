(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('PersonalAutoVehicleDialogController', PersonalAutoVehicleDialogController);

    PersonalAutoVehicleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'PersonalAutoVehicle', 'PolicyDriver', 'ProductLine', 'Address', 'Loss', 'Coverage'];

    function PersonalAutoVehicleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, PersonalAutoVehicle, PolicyDriver, ProductLine, Address, Loss, Coverage) {
        var vm = this;
        vm.personalAutoVehicle = entity;
        vm.policydrivers = PolicyDriver.query();
        vm.productlines = ProductLine.query();
        vm.garagelocations = Address.query({filter: 'personalautovehicle-is-null'});
        $q.all([vm.personalAutoVehicle.$promise, vm.garagelocations.$promise]).then(function() {
            if (!vm.personalAutoVehicle.garageLocation || !vm.personalAutoVehicle.garageLocation.id) {
                return $q.reject();
            }
            return Address.get({id : vm.personalAutoVehicle.garageLocation.id}).$promise;
        }).then(function(garageLocation) {
            vm.garagelocations.push(garageLocation);
        });
        vm.losses = Loss.query();
        vm.coverages = Coverage.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:personalAutoVehicleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.personalAutoVehicle.id !== null) {
                PersonalAutoVehicle.update(vm.personalAutoVehicle, onSaveSuccess, onSaveError);
            } else {
                PersonalAutoVehicle.save(vm.personalAutoVehicle, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
