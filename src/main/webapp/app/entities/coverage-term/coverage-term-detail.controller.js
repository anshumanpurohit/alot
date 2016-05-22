(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageTermDetailController', CoverageTermDetailController);

    CoverageTermDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'CoverageTerm', 'Coverage', 'CoverageTermDef', 'CoverageTermOption'];

    function CoverageTermDetailController($scope, $rootScope, $stateParams, entity, CoverageTerm, Coverage, CoverageTermDef, CoverageTermOption) {
        var vm = this;
        vm.coverageTerm = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:coverageTermUpdate', function(event, result) {
            vm.coverageTerm = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
