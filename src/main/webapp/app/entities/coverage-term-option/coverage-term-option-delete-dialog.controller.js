(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageTermOptionDeleteController',CoverageTermOptionDeleteController);

    CoverageTermOptionDeleteController.$inject = ['$uibModalInstance', 'entity', 'CoverageTermOption'];

    function CoverageTermOptionDeleteController($uibModalInstance, entity, CoverageTermOption) {
        var vm = this;
        vm.coverageTermOption = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            CoverageTermOption.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
