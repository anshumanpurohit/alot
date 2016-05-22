(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ProducerDetailController', ProducerDetailController);

    ProducerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Producer', 'AddressBook', 'Activity'];

    function ProducerDetailController($scope, $rootScope, $stateParams, entity, Producer, AddressBook, Activity) {
        var vm = this;
        vm.producer = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:producerUpdate', function(event, result) {
            vm.producer = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
