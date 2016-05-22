(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('LossDeleteController',LossDeleteController);

    LossDeleteController.$inject = ['$uibModalInstance', 'entity', 'Loss'];

    function LossDeleteController($uibModalInstance, entity, Loss) {
        var vm = this;
        vm.loss = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Loss.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
