(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageTermOptionDialogController', CoverageTermOptionDialogController);

    CoverageTermOptionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'CoverageTermOption', 'CoverageTerm', 'CoverageTermOptionDef'];

    function CoverageTermOptionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, CoverageTermOption, CoverageTerm, CoverageTermOptionDef) {
        var vm = this;
        vm.coverageTermOption = entity;
        vm.coverageterms = CoverageTerm.query();
        vm.definitions = CoverageTermOptionDef.query({filter: 'coveragetermoption-is-null'});
        $q.all([vm.coverageTermOption.$promise, vm.definitions.$promise]).then(function() {
            if (!vm.coverageTermOption.definition || !vm.coverageTermOption.definition.id) {
                return $q.reject();
            }
            return CoverageTermOptionDef.get({id : vm.coverageTermOption.definition.id}).$promise;
        }).then(function(definition) {
            vm.definitions.push(definition);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:coverageTermOptionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.coverageTermOption.id !== null) {
                CoverageTermOption.update(vm.coverageTermOption, onSaveSuccess, onSaveError);
            } else {
                CoverageTermOption.save(vm.coverageTermOption, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
