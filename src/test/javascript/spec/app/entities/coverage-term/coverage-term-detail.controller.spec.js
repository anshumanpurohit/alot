'use strict';

describe('Controller Tests', function() {

    describe('CoverageTerm Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCoverageTerm, MockCoverage, MockCoverageTermDef, MockCoverageTermOption;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCoverageTerm = jasmine.createSpy('MockCoverageTerm');
            MockCoverage = jasmine.createSpy('MockCoverage');
            MockCoverageTermDef = jasmine.createSpy('MockCoverageTermDef');
            MockCoverageTermOption = jasmine.createSpy('MockCoverageTermOption');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CoverageTerm': MockCoverageTerm,
                'Coverage': MockCoverage,
                'CoverageTermDef': MockCoverageTermDef,
                'CoverageTermOption': MockCoverageTermOption
            };
            createController = function() {
                $injector.get('$controller')("CoverageTermDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'alotApp:coverageTermUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
