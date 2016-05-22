(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ProductLineDefDetailController', ProductLineDefDetailController);

    ProductLineDefDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ProductLineDef'];

    function ProductLineDefDetailController($scope, $rootScope, $stateParams, entity, ProductLineDef) {
        var vm = this;
        vm.productLineDef = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:productLineDefUpdate', function(event, result) {
            vm.productLineDef = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
