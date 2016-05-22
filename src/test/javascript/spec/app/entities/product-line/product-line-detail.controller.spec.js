'use strict';

describe('Controller Tests', function() {

    describe('ProductLine Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProductLine, MockProductLineDef, MockDiscount, MockPolicyDriver, MockNamedInsured, MockPersonalAutoVehicle, MockCoverage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProductLine = jasmine.createSpy('MockProductLine');
            MockProductLineDef = jasmine.createSpy('MockProductLineDef');
            MockDiscount = jasmine.createSpy('MockDiscount');
            MockPolicyDriver = jasmine.createSpy('MockPolicyDriver');
            MockNamedInsured = jasmine.createSpy('MockNamedInsured');
            MockPersonalAutoVehicle = jasmine.createSpy('MockPersonalAutoVehicle');
            MockCoverage = jasmine.createSpy('MockCoverage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ProductLine': MockProductLine,
                'ProductLineDef': MockProductLineDef,
                'Discount': MockDiscount,
                'PolicyDriver': MockPolicyDriver,
                'NamedInsured': MockNamedInsured,
                'PersonalAutoVehicle': MockPersonalAutoVehicle,
                'Coverage': MockCoverage
            };
            createController = function() {
                $injector.get('$controller')("ProductLineDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'alotApp:productLineUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
