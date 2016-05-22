(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('AccordMappingDialogController', AccordMappingDialogController);

    AccordMappingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AccordMapping'];

    function AccordMappingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AccordMapping) {
        var vm = this;
        vm.accordMapping = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:accordMappingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.accordMapping.id !== null) {
                AccordMapping.update(vm.accordMapping, onSaveSuccess, onSaveError);
            } else {
                AccordMapping.save(vm.accordMapping, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
