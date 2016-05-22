'use strict';

describe('Controller Tests', function() {

    describe('PolicyContact Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPolicyContact;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPolicyContact = jasmine.createSpy('MockPolicyContact');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PolicyContact': MockPolicyContact
            };
            createController = function() {
                $injector.get('$controller')("PolicyContactDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'alotApp:policyContactUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
