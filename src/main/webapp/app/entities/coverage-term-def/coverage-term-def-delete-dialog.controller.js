(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageTermDefDeleteController',CoverageTermDefDeleteController);

    CoverageTermDefDeleteController.$inject = ['$uibModalInstance', 'entity', 'CoverageTermDef'];

    function CoverageTermDefDeleteController($uibModalInstance, entity, CoverageTermDef) {
        var vm = this;
        vm.coverageTermDef = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            CoverageTermDef.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
