'use strict';

describe('Controller Tests', function() {

    describe('PolicyDriver Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPolicyDriver, MockViolation, MockPolicyContact, MockPersonalAutoVehicle, MockProductLine;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPolicyDriver = jasmine.createSpy('MockPolicyDriver');
            MockViolation = jasmine.createSpy('MockViolation');
            MockPolicyContact = jasmine.createSpy('MockPolicyContact');
            MockPersonalAutoVehicle = jasmine.createSpy('MockPersonalAutoVehicle');
            MockProductLine = jasmine.createSpy('MockProductLine');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PolicyDriver': MockPolicyDriver,
                'Violation': MockViolation,
                'PolicyContact': MockPolicyContact,
                'PersonalAutoVehicle': MockPersonalAutoVehicle,
                'ProductLine': MockProductLine
            };
            createController = function() {
                $injector.get('$controller')("PolicyDriverDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'alotApp:policyDriverUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
