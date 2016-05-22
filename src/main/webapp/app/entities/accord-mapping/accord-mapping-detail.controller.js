(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('AccordMappingDetailController', AccordMappingDetailController);

    AccordMappingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'AccordMapping'];

    function AccordMappingDetailController($scope, $rootScope, $stateParams, entity, AccordMapping) {
        var vm = this;
        vm.accordMapping = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:accordMappingUpdate', function(event, result) {
            vm.accordMapping = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
