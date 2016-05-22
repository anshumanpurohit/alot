(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('CoverageTermOptionDefDetailController', CoverageTermOptionDefDetailController);

    CoverageTermOptionDefDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'CoverageTermOptionDef', 'CoverageTermDef'];

    function CoverageTermOptionDefDetailController($scope, $rootScope, $stateParams, entity, CoverageTermOptionDef, CoverageTermDef) {
        var vm = this;
        vm.coverageTermOptionDef = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:coverageTermOptionDefUpdate', function(event, result) {
            vm.coverageTermOptionDef = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
