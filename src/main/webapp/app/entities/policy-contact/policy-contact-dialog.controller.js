(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('PolicyContactDialogController', PolicyContactDialogController);

    PolicyContactDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PolicyContact'];

    function PolicyContactDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PolicyContact) {
        var vm = this;
        vm.policyContact = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:policyContactUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.policyContact.id !== null) {
                PolicyContact.update(vm.policyContact, onSaveSuccess, onSaveError);
            } else {
                PolicyContact.save(vm.policyContact, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dob = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
