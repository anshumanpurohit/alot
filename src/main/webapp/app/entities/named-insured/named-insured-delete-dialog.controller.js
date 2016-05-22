(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('NamedInsuredDeleteController',NamedInsuredDeleteController);

    NamedInsuredDeleteController.$inject = ['$uibModalInstance', 'entity', 'NamedInsured'];

    function NamedInsuredDeleteController($uibModalInstance, entity, NamedInsured) {
        var vm = this;
        vm.namedInsured = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            NamedInsured.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
