(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ViolationDialogController', ViolationDialogController);

    ViolationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Violation'];

    function ViolationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Violation) {
        var vm = this;
        vm.violation = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:violationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.violation.id !== null) {
                Violation.update(vm.violation, onSaveSuccess, onSaveError);
            } else {
                Violation.save(vm.violation, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.violationOccurredDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
