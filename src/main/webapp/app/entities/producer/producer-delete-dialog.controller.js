(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ProducerDeleteController',ProducerDeleteController);

    ProducerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Producer'];

    function ProducerDeleteController($uibModalInstance, entity, Producer) {
        var vm = this;
        vm.producer = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Producer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
