(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('LeadDeleteController',LeadDeleteController);

    LeadDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lead'];

    function LeadDeleteController($uibModalInstance, entity, Lead) {
        var vm = this;
        vm.lead = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Lead.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
