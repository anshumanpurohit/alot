(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('PersonalAutoVehicleDeleteController',PersonalAutoVehicleDeleteController);

    PersonalAutoVehicleDeleteController.$inject = ['$uibModalInstance', 'entity', 'PersonalAutoVehicle'];

    function PersonalAutoVehicleDeleteController($uibModalInstance, entity, PersonalAutoVehicle) {
        var vm = this;
        vm.personalAutoVehicle = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            PersonalAutoVehicle.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
