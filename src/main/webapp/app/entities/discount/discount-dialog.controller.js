(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('DiscountDialogController', DiscountDialogController);

    DiscountDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Discount', 'ProductLine', 'DiscountDef'];

    function DiscountDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Discount, ProductLine, DiscountDef) {
        var vm = this;
        vm.discount = entity;
        vm.productlines = ProductLine.query();
        vm.definitions = DiscountDef.query({filter: 'discount-is-null'});
        $q.all([vm.discount.$promise, vm.definitions.$promise]).then(function() {
            if (!vm.discount.definition || !vm.discount.definition.id) {
                return $q.reject();
            }
            return DiscountDef.get({id : vm.discount.definition.id}).$promise;
        }).then(function(definition) {
            vm.definitions.push(definition);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:discountUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.discount.id !== null) {
                Discount.update(vm.discount, onSaveSuccess, onSaveError);
            } else {
                Discount.save(vm.discount, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
