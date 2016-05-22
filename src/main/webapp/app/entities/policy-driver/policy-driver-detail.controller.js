(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('PolicyDriverDetailController', PolicyDriverDetailController);

    PolicyDriverDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'PolicyDriver', 'Violation', 'PolicyContact', 'PersonalAutoVehicle', 'ProductLine'];

    function PolicyDriverDetailController($scope, $rootScope, $stateParams, entity, PolicyDriver, Violation, PolicyContact, PersonalAutoVehicle, ProductLine) {
        var vm = this;
        vm.policyDriver = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:policyDriverUpdate', function(event, result) {
            vm.policyDriver = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
