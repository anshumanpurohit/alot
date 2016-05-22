'use strict';

describe('Controller Tests', function() {

    describe('Producer Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProducer, MockAddressBook, MockActivity;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProducer = jasmine.createSpy('MockProducer');
            MockAddressBook = jasmine.createSpy('MockAddressBook');
            MockActivity = jasmine.createSpy('MockActivity');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Producer': MockProducer,
                'AddressBook': MockAddressBook,
                'Activity': MockActivity
            };
            createController = function() {
                $injector.get('$controller')("ProducerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'alotApp:producerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
