(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('NamedInsuredDialogController', NamedInsuredDialogController);

    NamedInsuredDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'NamedInsured', 'PolicyContact', 'ProductLine'];

    function NamedInsuredDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, NamedInsured, PolicyContact, ProductLine) {
        var vm = this;
        vm.namedInsured = entity;
        vm.policycontacts = PolicyContact.query({filter: 'namedinsured-is-null'});
        $q.all([vm.namedInsured.$promise, vm.policycontacts.$promise]).then(function() {
            if (!vm.namedInsured.policyContact || !vm.namedInsured.policyContact.id) {
                return $q.reject();
            }
            return PolicyContact.get({id : vm.namedInsured.policyContact.id}).$promise;
        }).then(function(policyContact) {
            vm.policycontacts.push(policyContact);
        });
        vm.productlines = ProductLine.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:namedInsuredUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.namedInsured.id !== null) {
                NamedInsured.update(vm.namedInsured, onSaveSuccess, onSaveError);
            } else {
                NamedInsured.save(vm.namedInsured, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
