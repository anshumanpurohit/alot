(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('AddressDeleteController',AddressDeleteController);

    AddressDeleteController.$inject = ['$uibModalInstance', 'entity', 'Address'];

    function AddressDeleteController($uibModalInstance, entity, Address) {
        var vm = this;
        vm.address = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Address.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
