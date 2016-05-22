(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ContactDeleteController',ContactDeleteController);

    ContactDeleteController.$inject = ['$uibModalInstance', 'entity', 'Contact'];

    function ContactDeleteController($uibModalInstance, entity, Contact) {
        var vm = this;
        vm.contact = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Contact.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
