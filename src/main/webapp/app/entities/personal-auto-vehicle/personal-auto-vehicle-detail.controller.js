(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('PersonalAutoVehicleDetailController', PersonalAutoVehicleDetailController);

    PersonalAutoVehicleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'PersonalAutoVehicle', 'PolicyDriver', 'ProductLine', 'Address', 'Loss', 'Coverage'];

    function PersonalAutoVehicleDetailController($scope, $rootScope, $stateParams, entity, PersonalAutoVehicle, PolicyDriver, ProductLine, Address, Loss, Coverage) {
        var vm = this;
        vm.personalAutoVehicle = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:personalAutoVehicleUpdate', function(event, result) {
            vm.personalAutoVehicle = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
