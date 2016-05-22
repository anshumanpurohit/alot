(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('PolicyDialogController', PolicyDialogController);

    PolicyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Policy', 'ProductLine', 'Job'];

    function PolicyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Policy, ProductLine, Job) {
        var vm = this;
        vm.policy = entity;
        vm.productlines = ProductLine.query({filter: 'policy-is-null'});
        $q.all([vm.policy.$promise, vm.productlines.$promise]).then(function() {
            if (!vm.policy.productLine || !vm.policy.productLine.id) {
                return $q.reject();
            }
            return ProductLine.get({id : vm.policy.productLine.id}).$promise;
        }).then(function(productLine) {
            vm.productlines.push(productLine);
        });
        vm.jobs = Job.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:policyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.policy.id !== null) {
                Policy.update(vm.policy, onSaveSuccess, onSaveError);
            } else {
                Policy.save(vm.policy, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
