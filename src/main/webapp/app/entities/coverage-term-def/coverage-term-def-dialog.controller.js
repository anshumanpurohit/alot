(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageTermDefDialogController', CoverageTermDefDialogController);

    CoverageTermDefDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CoverageTermDef', 'CoverageDef', 'CoverageTermOptionDef'];

    function CoverageTermDefDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CoverageTermDef, CoverageDef, CoverageTermOptionDef) {
        var vm = this;
        vm.coverageTermDef = entity;
        vm.coveragedefs = CoverageDef.query();
        vm.coveragetermoptiondefs = CoverageTermOptionDef.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:coverageTermDefUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.coverageTermDef.id !== null) {
                CoverageTermDef.update(vm.coverageTermDef, onSaveSuccess, onSaveError);
            } else {
                CoverageTermDef.save(vm.coverageTermDef, onSaveSuccess, onSaveError);
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
