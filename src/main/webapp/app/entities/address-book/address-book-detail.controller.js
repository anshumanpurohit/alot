(function() {
    'use strict';

    angular
        .module('alotApp')
        .controller('AddressBookDetailController', AddressBookDetailController);

    AddressBookDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'AddressBook', 'Producer'];

    function AddressBookDetailController($scope, $rootScope, $stateParams, entity, AddressBook, Producer) {
        var vm = this;
        vm.addressBook = entity;
        
        var unsubscribe = $rootScope.$on('alotApp:addressBookUpdate', function(event, result) {
            vm.addressBook = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
