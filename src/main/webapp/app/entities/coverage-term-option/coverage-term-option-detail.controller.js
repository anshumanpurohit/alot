(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageTermOptionDetailController', CoverageTermOptionDetailController);

    CoverageTermOptionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'CoverageTermOption', 'CoverageTerm', 'CoverageTermOptionDef'];

    function CoverageTermOptionDetailController($scope, $rootScope, $stateParams, entity, CoverageTermOption, CoverageTerm, CoverageTermOptionDef) {
        var vm = this;
        vm.coverageTermOption = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:coverageTermOptionUpdate', function(event, result) {
            vm.coverageTermOption = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
