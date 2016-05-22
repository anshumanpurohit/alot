(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageDeleteController',CoverageDeleteController);

    CoverageDeleteController.$inject = ['$uibModalInstance', 'entity', 'Coverage'];

    function CoverageDeleteController($uibModalInstance, entity, Coverage) {
        var vm = this;
        vm.coverage = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Coverage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
