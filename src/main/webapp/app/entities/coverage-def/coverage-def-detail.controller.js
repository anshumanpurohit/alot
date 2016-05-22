(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageDefDetailController', CoverageDefDetailController);

    CoverageDefDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'CoverageDef', 'CoverageTermDef'];

    function CoverageDefDetailController($scope, $rootScope, $stateParams, entity, CoverageDef, CoverageTermDef) {
        var vm = this;
        vm.coverageDef = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:coverageDefUpdate', function(event, result) {
            vm.coverageDef = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
