(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ProductLineDetailController', ProductLineDetailController);

    ProductLineDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ProductLine', 'ProductLineDef', 'Discount', 'PolicyDriver', 'NamedInsured', 'PersonalAutoVehicle', 'Coverage'];

    function ProductLineDetailController($scope, $rootScope, $stateParams, entity, ProductLine, ProductLineDef, Discount, PolicyDriver, NamedInsured, PersonalAutoVehicle, Coverage) {
        var vm = this;
        vm.productLine = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:productLineUpdate', function(event, result) {
            vm.productLine = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
