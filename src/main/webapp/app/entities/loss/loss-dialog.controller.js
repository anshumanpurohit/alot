(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('LossDialogController', LossDialogController);

    LossDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Loss', 'PersonalAutoVehicle'];

    function LossDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Loss, PersonalAutoVehicle) {
        var vm = this;
        vm.loss = entity;
        vm.personalautovehicles = PersonalAutoVehicle.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:lossUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.loss.id !== null) {
                Loss.update(vm.loss, onSaveSuccess, onSaveError);
            } else {
                Loss.save(vm.loss, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.lossOccurredDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
