(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('AccordMappingDeleteController',AccordMappingDeleteController);

    AccordMappingDeleteController.$inject = ['$uibModalInstance', 'entity', 'AccordMapping'];

    function AccordMappingDeleteController($uibModalInstance, entity, AccordMapping) {
        var vm = this;
        vm.accordMapping = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            AccordMapping.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
