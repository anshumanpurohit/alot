(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ProductLineDeleteController',ProductLineDeleteController);

    ProductLineDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProductLine'];

    function ProductLineDeleteController($uibModalInstance, entity, ProductLine) {
        var vm = this;
        vm.productLine = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ProductLine.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
