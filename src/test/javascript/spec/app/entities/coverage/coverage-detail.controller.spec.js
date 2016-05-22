'use strict';

describe('Controller Tests', function() {

    describe('Coverage Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCoverage, MockProductLine, MockCoverageDef, MockCoverageTerm, MockPersonalAutoVehicle;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCoverage = jasmine.createSpy('MockCoverage');
            MockProductLine = jasmine.createSpy('MockProductLine');
            MockCoverageDef = jasmine.createSpy('MockCoverageDef');
            MockCoverageTerm = jasmine.createSpy('MockCoverageTerm');
            MockPersonalAutoVehicle = jasmine.createSpy('MockPersonalAutoVehicle');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Coverage': MockCoverage,
                'ProductLine': MockProductLine,
                'CoverageDef': MockCoverageDef,
                'CoverageTerm': MockCoverageTerm,
                'PersonalAutoVehicle': MockPersonalAutoVehicle
            };
            createController = function() {
                $injector.get('$controller')("CoverageDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'alotApp:coverageUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
