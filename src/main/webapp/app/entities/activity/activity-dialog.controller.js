(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ActivityDialogController', ActivityDialogController);

    ActivityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Activity', 'ActivityPattern', 'Producer'];

    function ActivityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Activity, ActivityPattern, Producer) {
        var vm = this;
        vm.activity = entity;
        vm.activitypatterns = ActivityPattern.query({filter: 'activity-is-null'});
        $q.all([vm.activity.$promise, vm.activitypatterns.$promise]).then(function() {
            if (!vm.activity.activityPattern || !vm.activity.activityPattern.id) {
                return $q.reject();
            }
            return ActivityPattern.get({id : vm.activity.activityPattern.id}).$promise;
        }).then(function(activityPattern) {
            vm.activitypatterns.push(activityPattern);
        });
        vm.producers = Producer.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:activityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.activity.id !== null) {
                Activity.update(vm.activity, onSaveSuccess, onSaveError);
            } else {
                Activity.save(vm.activity, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
