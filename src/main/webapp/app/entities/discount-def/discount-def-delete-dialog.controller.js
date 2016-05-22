(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('DiscountDefDeleteController',DiscountDefDeleteController);

    DiscountDefDeleteController.$inject = ['$uibModalInstance', 'entity', 'DiscountDef'];

    function DiscountDefDeleteController($uibModalInstance, entity, DiscountDef) {
        var vm = this;
        vm.discountDef = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            DiscountDef.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
