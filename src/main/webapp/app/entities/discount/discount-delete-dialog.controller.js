(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('DiscountDeleteController',DiscountDeleteController);

    DiscountDeleteController.$inject = ['$uibModalInstance', 'entity', 'Discount'];

    function DiscountDeleteController($uibModalInstance, entity, Discount) {
        var vm = this;
        vm.discount = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Discount.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
