(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ProductLineDefDialogController', ProductLineDefDialogController);

    ProductLineDefDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProductLineDef'];

    function ProductLineDefDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProductLineDef) {
        var vm = this;
        vm.productLineDef = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:productLineDefUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.productLineDef.id !== null) {
                ProductLineDef.update(vm.productLineDef, onSaveSuccess, onSaveError);
            } else {
                ProductLineDef.save(vm.productLineDef, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
