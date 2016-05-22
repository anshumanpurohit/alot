(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageDetailController', CoverageDetailController);

    CoverageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Coverage', 'ProductLine', 'CoverageDef', 'CoverageTerm', 'PersonalAutoVehicle'];

    function CoverageDetailController($scope, $rootScope, $stateParams, entity, Coverage, ProductLine, CoverageDef, CoverageTerm, PersonalAutoVehicle) {
        var vm = this;
        vm.coverage = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:coverageUpdate', function(event, result) {
            vm.coverage = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
