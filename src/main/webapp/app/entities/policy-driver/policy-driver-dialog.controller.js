(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('PolicyDriverDialogController', PolicyDriverDialogController);

    PolicyDriverDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'PolicyDriver', 'Violation', 'PolicyContact', 'PersonalAutoVehicle', 'ProductLine'];

    function PolicyDriverDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, PolicyDriver, Violation, PolicyContact, PersonalAutoVehicle, ProductLine) {
        var vm = this;
        vm.policyDriver = entity;
        vm.violationss = Violation.query({filter: 'policydriver-is-null'});
        $q.all([vm.policyDriver.$promise, vm.violationss.$promise]).then(function() {
            if (!vm.policyDriver.violations || !vm.policyDriver.violations.id) {
                return $q.reject();
            }
            return Violation.get({id : vm.policyDriver.violations.id}).$promise;
        }).then(function(violations) {
            vm.violations.push(violations);
        });
        vm.policycontacts = PolicyContact.query({filter: 'policydriver-is-null'});
        $q.all([vm.policyDriver.$promise, vm.policycontacts.$promise]).then(function() {
            if (!vm.policyDriver.policyContact || !vm.policyDriver.policyContact.id) {
                return $q.reject();
            }
            return PolicyContact.get({id : vm.policyDriver.policyContact.id}).$promise;
        }).then(function(policyContact) {
            vm.policycontacts.push(policyContact);
        });
        vm.personalautovehicles = PersonalAutoVehicle.query();
        vm.productlines = ProductLine.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:policyDriverUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.policyDriver.id !== null) {
                PolicyDriver.update(vm.policyDriver, onSaveSuccess, onSaveError);
            } else {
                PolicyDriver.save(vm.policyDriver, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
