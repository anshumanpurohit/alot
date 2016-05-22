(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageTermDeleteController',CoverageTermDeleteController);

    CoverageTermDeleteController.$inject = ['$uibModalInstance', 'entity', 'CoverageTerm'];

    function CoverageTermDeleteController($uibModalInstance, entity, CoverageTerm) {
        var vm = this;
        vm.coverageTerm = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            CoverageTerm.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
