(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ProductLineDialogController', ProductLineDialogController);

    ProductLineDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'ProductLine', 'ProductLineDef', 'Discount', 'PolicyDriver', 'NamedInsured', 'PersonalAutoVehicle', 'Coverage'];

    function ProductLineDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, ProductLine, ProductLineDef, Discount, PolicyDriver, NamedInsured, PersonalAutoVehicle, Coverage) {
        var vm = this;
        vm.productLine = entity;
        vm.productlinedefs = ProductLineDef.query({filter: 'productline-is-null'});
        $q.all([vm.productLine.$promise, vm.productlinedefs.$promise]).then(function() {
            if (!vm.productLine.productLineDef || !vm.productLine.productLineDef.id) {
                return $q.reject();
            }
            return ProductLineDef.get({id : vm.productLine.productLineDef.id}).$promise;
        }).then(function(productLineDef) {
            vm.productlinedefs.push(productLineDef);
        });
        vm.discounts = Discount.query();
        vm.policydrivers = PolicyDriver.query();
        vm.namedinsureds = NamedInsured.query();
        vm.personalautovehicles = PersonalAutoVehicle.query();
        vm.coverages = Coverage.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:productLineUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.productLine.id !== null) {
                ProductLine.update(vm.productLine, onSaveSuccess, onSaveError);
            } else {
                ProductLine.save(vm.productLine, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.startEffectiveDate = false;
        vm.datePickerOpenStatus.endEffectiveDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
