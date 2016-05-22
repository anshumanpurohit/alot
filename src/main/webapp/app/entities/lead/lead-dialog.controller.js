(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('LeadDialogController', LeadDialogController);

    LeadDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Lead', 'Job', 'Carrier'];

    function LeadDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Lead, Job, Carrier) {
        var vm = this;
        vm.lead = entity;
        vm.jobs = Job.query();
        vm.carriers = Carrier.query({filter: 'lead-is-null'});
        $q.all([vm.lead.$promise, vm.carriers.$promise]).then(function() {
            if (!vm.lead.carrier || !vm.lead.carrier.id) {
                return $q.reject();
            }
            return Carrier.get({id : vm.lead.carrier.id}).$promise;
        }).then(function(carrier) {
            vm.carriers.push(carrier);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:leadUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.lead.id !== null) {
                Lead.update(vm.lead, onSaveSuccess, onSaveError);
            } else {
                Lead.save(vm.lead, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.requestedTime = false;
        vm.datePickerOpenStatus.responseTime = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
