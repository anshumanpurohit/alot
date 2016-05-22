(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CarrierDetailController', CarrierDetailController);

    CarrierDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Carrier'];

    function CarrierDetailController($scope, $rootScope, $stateParams, entity, Carrier) {
        var vm = this;
        vm.carrier = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:carrierUpdate', function(event, result) {
            vm.carrier = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
