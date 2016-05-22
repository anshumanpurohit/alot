(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('NamedInsuredDetailController', NamedInsuredDetailController);

    NamedInsuredDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'NamedInsured', 'PolicyContact', 'ProductLine'];

    function NamedInsuredDetailController($scope, $rootScope, $stateParams, entity, NamedInsured, PolicyContact, ProductLine) {
        var vm = this;
        vm.namedInsured = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:namedInsuredUpdate', function(event, result) {
            vm.namedInsured = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
