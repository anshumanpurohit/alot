(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ActivityDeleteController',ActivityDeleteController);

    ActivityDeleteController.$inject = ['$uibModalInstance', 'entity', 'Activity'];

    function ActivityDeleteController($uibModalInstance, entity, Activity) {
        var vm = this;
        vm.activity = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Activity.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
