'use strict';

describe('Controller Tests', function() {

    describe('PersonalAutoVehicle Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPersonalAutoVehicle, MockPolicyDriver, MockProductLine, MockAddress, MockLoss, MockCoverage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPersonalAutoVehicle = jasmine.createSpy('MockPersonalAutoVehicle');
            MockPolicyDriver = jasmine.createSpy('MockPolicyDriver');
            MockProductLine = jasmine.createSpy('MockProductLine');
            MockAddress = jasmine.createSpy('MockAddress');
            MockLoss = jasmine.createSpy('MockLoss');
            MockCoverage = jasmine.createSpy('MockCoverage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PersonalAutoVehicle': MockPersonalAutoVehicle,
                'PolicyDriver': MockPolicyDriver,
                'ProductLine': MockProductLine,
                'Address': MockAddress,
                'Loss': MockLoss,
                'Coverage': MockCoverage
            };
            createController = function() {
                $injector.get('$controller')("PersonalAutoVehicleDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'alotApp:personalAutoVehicleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
