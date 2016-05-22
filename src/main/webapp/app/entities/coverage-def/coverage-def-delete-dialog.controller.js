(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageDefDeleteController',CoverageDefDeleteController);

    CoverageDefDeleteController.$inject = ['$uibModalInstance', 'entity', 'CoverageDef'];

    function CoverageDefDeleteController($uibModalInstance, entity, CoverageDef) {
        var vm = this;
        vm.coverageDef = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            CoverageDef.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
