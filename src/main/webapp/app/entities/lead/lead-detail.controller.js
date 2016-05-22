(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('LeadDetailController', LeadDetailController);

    LeadDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Lead', 'Job', 'Carrier'];

    function LeadDetailController($scope, $rootScope, $stateParams, entity, Lead, Job, Carrier) {
        var vm = this;
        vm.lead = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:leadUpdate', function(event, result) {
            vm.lead = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
