(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('PolicyContactDetailController', PolicyContactDetailController);

    PolicyContactDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'PolicyContact'];

    function PolicyContactDetailController($scope, $rootScope, $stateParams, entity, PolicyContact) {
        var vm = this;
        vm.policyContact = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:policyContactUpdate', function(event, result) {
            vm.policyContact = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
