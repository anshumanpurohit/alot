(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ActivityPatternDeleteController',ActivityPatternDeleteController);

    ActivityPatternDeleteController.$inject = ['$uibModalInstance', 'entity', 'ActivityPattern'];

    function ActivityPatternDeleteController($uibModalInstance, entity, ActivityPattern) {
        var vm = this;
        vm.activityPattern = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ActivityPattern.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
