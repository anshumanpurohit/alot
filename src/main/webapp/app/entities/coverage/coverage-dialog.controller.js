(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageDialogController', CoverageDialogController);

    CoverageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Coverage', 'ProductLine', 'CoverageDef', 'CoverageTerm', 'PersonalAutoVehicle'];

    function CoverageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Coverage, ProductLine, CoverageDef, CoverageTerm, PersonalAutoVehicle) {
        var vm = this;
        vm.coverage = entity;
        vm.productlines = ProductLine.query();
        vm.definitions = CoverageDef.query({filter: 'coverage-is-null'});
        $q.all([vm.coverage.$promise, vm.definitions.$promise]).then(function() {
            if (!vm.coverage.definition || !vm.coverage.definition.id) {
                return $q.reject();
            }
            return CoverageDef.get({id : vm.coverage.definition.id}).$promise;
        }).then(function(definition) {
            vm.definitions.push(definition);
        });
        vm.coverageterms = CoverageTerm.query();
        vm.personalautovehicles = PersonalAutoVehicle.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:coverageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.coverage.id !== null) {
                Coverage.update(vm.coverage, onSaveSuccess, onSaveError);
            } else {
                Coverage.save(vm.coverage, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
