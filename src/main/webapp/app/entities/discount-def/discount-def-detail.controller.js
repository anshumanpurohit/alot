(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('DiscountDefDetailController', DiscountDefDetailController);

    DiscountDefDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'DiscountDef'];

    function DiscountDefDetailController($scope, $rootScope, $stateParams, entity, DiscountDef) {
        var vm = this;
        vm.discountDef = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:discountDefUpdate', function(event, result) {
            vm.discountDef = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
