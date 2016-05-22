(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('AddressBookDialogController', AddressBookDialogController);

    AddressBookDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AddressBook', 'Producer'];

    function AddressBookDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AddressBook, Producer) {
        var vm = this;
        vm.addressBook = entity;
        vm.producers = Producer.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:addressBookUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.addressBook.id !== null) {
                AddressBook.update(vm.addressBook, onSaveSuccess, onSaveError);
            } else {
                AddressBook.save(vm.addressBook, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
