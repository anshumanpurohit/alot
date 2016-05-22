(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ActivityPatternDialogController', ActivityPatternDialogController);

    ActivityPatternDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ActivityPattern'];

    function ActivityPatternDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ActivityPattern) {
        var vm = this;
        vm.activityPattern = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:activityPatternUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.activityPattern.id !== null) {
                ActivityPattern.update(vm.activityPattern, onSaveSuccess, onSaveError);
            } else {
                ActivityPattern.save(vm.activityPattern, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
