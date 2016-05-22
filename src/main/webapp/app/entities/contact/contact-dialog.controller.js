(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ContactDialogController', ContactDialogController);

    ContactDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Contact', 'Address', 'AddressBook'];

    function ContactDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Contact, Address, AddressBook) {
        var vm = this;
        vm.contact = entity;
        vm.addresss = Address.query({filter: 'contact-is-null'});
        $q.all([vm.contact.$promise, vm.addresss.$promise]).then(function() {
            if (!vm.contact.address || !vm.contact.address.id) {
                return $q.reject();
            }
            return Address.get({id : vm.contact.address.id}).$promise;
        }).then(function(address) {
            vm.addresses.push(address);
        });
        vm.addressbooks = AddressBook.query({filter: 'contact-is-null'});
        $q.all([vm.contact.$promise, vm.addressbooks.$promise]).then(function() {
            if (!vm.contact.addressBook || !vm.contact.addressBook.id) {
                return $q.reject();
            }
            return AddressBook.get({id : vm.contact.addressBook.id}).$promise;
        }).then(function(addressBook) {
            vm.addressbooks.push(addressBook);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:contactUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.contact.id !== null) {
                Contact.update(vm.contact, onSaveSuccess, onSaveError);
            } else {
                Contact.save(vm.contact, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dob = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
