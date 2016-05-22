(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('ContactDetailController', ContactDetailController);

    ContactDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Contact', 'Address', 'AddressBook'];

    function ContactDetailController($scope, $rootScope, $stateParams, entity, Contact, Address, AddressBook) {
        var vm = this;
        vm.contact = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:contactUpdate', function(event, result) {
            vm.contact = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
