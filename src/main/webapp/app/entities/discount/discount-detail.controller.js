(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('DiscountDetailController', DiscountDetailController);

    DiscountDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Discount', 'ProductLine', 'DiscountDef'];

    function DiscountDetailController($scope, $rootScope, $stateParams, entity, Discount, ProductLine, DiscountDef) {
        var vm = this;
        vm.discount = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:discountUpdate', function(event, result) {
            vm.discount = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
