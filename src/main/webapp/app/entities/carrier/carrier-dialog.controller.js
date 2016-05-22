(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CarrierDialogController', CarrierDialogController);

    CarrierDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Carrier'];

    function CarrierDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Carrier) {
        var vm = this;
        vm.carrier = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:carrierUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.carrier.id !== null) {
                Carrier.update(vm.carrier, onSaveSuccess, onSaveError);
            } else {
                Carrier.save(vm.carrier, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
