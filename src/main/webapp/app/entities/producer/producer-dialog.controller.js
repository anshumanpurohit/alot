(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ProducerDialogController', ProducerDialogController);

    ProducerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Producer', 'AddressBook', 'Activity'];

    function ProducerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Producer, AddressBook, Activity) {
        var vm = this;
        vm.producer = entity;
        vm.addressbooks = AddressBook.query();
        vm.activities = Activity.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('alotApp:producerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.producer.id !== null) {
                Producer.update(vm.producer, onSaveSuccess, onSaveError);
            } else {
                Producer.save(vm.producer, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
