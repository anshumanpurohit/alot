(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageTermOptionDefDeleteController',CoverageTermOptionDefDeleteController);

    CoverageTermOptionDefDeleteController.$inject = ['$uibModalInstance', 'entity', 'CoverageTermOptionDef'];

    function CoverageTermOptionDefDeleteController($uibModalInstance, entity, CoverageTermOptionDef) {
        var vm = this;
        vm.coverageTermOptionDef = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            CoverageTermOptionDef.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
