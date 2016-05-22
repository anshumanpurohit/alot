'use strict';

describe('Controller Tests', function() {

    describe('AddressBook Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAddressBook, MockProducer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAddressBook = jasmine.createSpy('MockAddressBook');
            MockProducer = jasmine.createSpy('MockProducer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'AddressBook': MockAddressBook,
                'Producer': MockProducer
            };
            createController = function() {
                $injector.get('$controller')("AddressBookDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'alotApp:addressBookUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
