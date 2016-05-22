(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('JobDialogController', JobDialogController);

    JobDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Job', 'Lead', 'Policy'];

    function JobDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Job, Lead, Policy) {
        var vm = this;
        vm.job = entity;
        vm.leads = Lead.query();
        vm.policies = Policy.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:jobUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.job.id !== null) {
                Job.update(vm.job, onSaveSuccess, onSaveError);
            } else {
                Job.save(vm.job, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
