(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ActivityDetailController', ActivityDetailController);

    ActivityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Activity', 'ActivityPattern', 'Producer'];

    function ActivityDetailController($scope, $rootScope, $stateParams, entity, Activity, ActivityPattern, Producer) {
        var vm = this;
        vm.activity = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:activityUpdate', function(event, result) {
            vm.activity = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
