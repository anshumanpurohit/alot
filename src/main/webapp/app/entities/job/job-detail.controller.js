(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('JobDetailController', JobDetailController);

    JobDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Job', 'Lead', 'Policy'];

    function JobDetailController($scope, $rootScope, $stateParams, entity, Job, Lead, Policy) {
        var vm = this;
        vm.job = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:jobUpdate', function(event, result) {
            vm.job = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
