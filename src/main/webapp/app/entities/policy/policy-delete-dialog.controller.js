(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('PolicyDeleteController',PolicyDeleteController);

    PolicyDeleteController.$inject = ['$uibModalInstance', 'entity', 'Policy'];

    function PolicyDeleteController($uibModalInstance, entity, Policy) {
        var vm = this;
        vm.policy = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Policy.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
