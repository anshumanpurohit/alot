(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ViolationDetailController', ViolationDetailController);

    ViolationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Violation'];

    function ViolationDetailController($scope, $rootScope, $stateParams, entity, Violation) {
        var vm = this;
        vm.violation = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:violationUpdate', function(event, result) {
            vm.violation = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
