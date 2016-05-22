(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('PolicyDriverDeleteController',PolicyDriverDeleteController);

    PolicyDriverDeleteController.$inject = ['$uibModalInstance', 'entity', 'PolicyDriver'];

    function PolicyDriverDeleteController($uibModalInstance, entity, PolicyDriver) {
        var vm = this;
        vm.policyDriver = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            PolicyDriver.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
