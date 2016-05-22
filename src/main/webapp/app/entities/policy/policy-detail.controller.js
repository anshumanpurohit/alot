(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('PolicyDetailController', PolicyDetailController);

    PolicyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Policy', 'ProductLine', 'Job'];

    function PolicyDetailController($scope, $rootScope, $stateParams, entity, Policy, ProductLine, Job) {
        var vm = this;
        vm.policy = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:policyUpdate', function(event, result) {
            vm.policy = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
