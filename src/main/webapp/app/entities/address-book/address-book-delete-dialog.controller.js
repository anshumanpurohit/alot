(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('AddressBookDeleteController',AddressBookDeleteController);

    AddressBookDeleteController.$inject = ['$uibModalInstance', 'entity', 'AddressBook'];

    function AddressBookDeleteController($uibModalInstance, entity, AddressBook) {
        var vm = this;
        vm.addressBook = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            AddressBook.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
