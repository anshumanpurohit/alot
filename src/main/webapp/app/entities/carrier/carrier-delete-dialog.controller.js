(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CarrierDeleteController',CarrierDeleteController);

    CarrierDeleteController.$inject = ['$uibModalInstance', 'entity', 'Carrier'];

    function CarrierDeleteController($uibModalInstance, entity, Carrier) {
        var vm = this;
        vm.carrier = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Carrier.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
