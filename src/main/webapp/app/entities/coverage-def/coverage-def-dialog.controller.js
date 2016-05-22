(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageDefDialogController', CoverageDefDialogController);

    CoverageDefDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CoverageDef', 'CoverageTermDef'];

    function CoverageDefDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CoverageDef, CoverageTermDef) {
        var vm = this;
        vm.coverageDef = entity;
        vm.coveragetermdefs = CoverageTermDef.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:coverageDefUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.coverageDef.id !== null) {
                CoverageDef.update(vm.coverageDef, onSaveSuccess, onSaveError);
            } else {
                CoverageDef.save(vm.coverageDef, onSaveSuccess, onSaveError);
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
