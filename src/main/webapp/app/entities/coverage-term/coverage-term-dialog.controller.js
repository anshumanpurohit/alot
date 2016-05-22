(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageTermDialogController', CoverageTermDialogController);

    CoverageTermDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'CoverageTerm', 'Coverage', 'CoverageTermDef', 'CoverageTermOption'];

    function CoverageTermDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, CoverageTerm, Coverage, CoverageTermDef, CoverageTermOption) {
        var vm = this;
        vm.coverageTerm = entity;
        vm.coverages = Coverage.query();
        vm.definitions = CoverageTermDef.query({filter: 'coverageterm-is-null'});
        $q.all([vm.coverageTerm.$promise, vm.definitions.$promise]).then(function() {
            if (!vm.coverageTerm.definition || !vm.coverageTerm.definition.id) {
                return $q.reject();
            }
            return CoverageTermDef.get({id : vm.coverageTerm.definition.id}).$promise;
        }).then(function(definition) {
            vm.definitions.push(definition);
        });
        vm.coveragetermoptions = CoverageTermOption.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:coverageTermUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.coverageTerm.id !== null) {
                CoverageTerm.update(vm.coverageTerm, onSaveSuccess, onSaveError);
            } else {
                CoverageTerm.save(vm.coverageTerm, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
