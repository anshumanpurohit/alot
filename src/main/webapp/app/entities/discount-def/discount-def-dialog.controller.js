(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('DiscountDefDialogController', DiscountDefDialogController);

    DiscountDefDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DiscountDef'];

    function DiscountDefDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DiscountDef) {
        var vm = this;
        vm.discountDef = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:discountDefUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.discountDef.id !== null) {
                DiscountDef.update(vm.discountDef, onSaveSuccess, onSaveError);
            } else {
                DiscountDef.save(vm.discountDef, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.beginEffectiveDate = false;
        vm.datePickerOpenStatus.endEffectiveDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
