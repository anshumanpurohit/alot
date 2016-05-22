(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('PolicyContactDeleteController',PolicyContactDeleteController);

    PolicyContactDeleteController.$inject = ['$uibModalInstance', 'entity', 'PolicyContact'];

    function PolicyContactDeleteController($uibModalInstance, entity, PolicyContact) {
        var vm = this;
        vm.policyContact = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            PolicyContact.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
