(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('LossDetailController', LossDetailController);

    LossDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Loss', 'PersonalAutoVehicle'];

    function LossDetailController($scope, $rootScope, $stateParams, entity, Loss, PersonalAutoVehicle) {
        var vm = this;
        vm.loss = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:lossUpdate', function(event, result) {
            vm.loss = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
