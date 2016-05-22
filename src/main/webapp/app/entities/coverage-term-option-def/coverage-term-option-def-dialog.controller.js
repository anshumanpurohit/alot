(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageTermOptionDefDialogController', CoverageTermOptionDefDialogController);

    CoverageTermOptionDefDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CoverageTermOptionDef', 'CoverageTermDef'];

    function CoverageTermOptionDefDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CoverageTermOptionDef, CoverageTermDef) {
        var vm = this;
        vm.coverageTermOptionDef = entity;
        vm.coveragetermdefs = CoverageTermDef.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:coverageTermOptionDefUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.coverageTermOptionDef.id !== null) {
                CoverageTermOptionDef.update(vm.coverageTermOptionDef, onSaveSuccess, onSaveError);
            } else {
                CoverageTermOptionDef.save(vm.coverageTermOptionDef, onSaveSuccess, onSaveError);
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
