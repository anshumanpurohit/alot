(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ProductLineDefDeleteController',ProductLineDefDeleteController);

    ProductLineDefDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProductLineDef'];

    function ProductLineDefDeleteController($uibModalInstance, entity, ProductLineDef) {
        var vm = this;
        vm.productLineDef = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ProductLineDef.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
