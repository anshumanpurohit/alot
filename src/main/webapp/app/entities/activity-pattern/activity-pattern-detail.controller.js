(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ActivityPatternDetailController', ActivityPatternDetailController);

    ActivityPatternDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ActivityPattern'];

    function ActivityPatternDetailController($scope, $rootScope, $stateParams, entity, ActivityPattern) {
        var vm = this;
        vm.activityPattern = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:activityPatternUpdate', function(event, result) {
            vm.activityPattern = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
