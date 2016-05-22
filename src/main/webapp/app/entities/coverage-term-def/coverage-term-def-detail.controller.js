(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageTermDefDetailController', CoverageTermDefDetailController);

    CoverageTermDefDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'CoverageTermDef', 'CoverageDef', 'CoverageTermOptionDef'];

    function CoverageTermDefDetailController($scope, $rootScope, $stateParams, entity, CoverageTermDef, CoverageDef, CoverageTermOptionDef) {
        var vm = this;
        vm.coverageTermDef = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:coverageTermDefUpdate', function(event, result) {
            vm.coverageTermDef = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
