(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ViolationDeleteController',ViolationDeleteController);

    ViolationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Violation'];

    function ViolationDeleteController($uibModalInstance, entity, Violation) {
        var vm = this;
        vm.violation = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Violation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
