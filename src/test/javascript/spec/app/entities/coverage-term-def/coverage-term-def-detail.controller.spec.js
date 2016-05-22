'use strict';

describe('Controller Tests', function() {

    describe('CoverageTermDef Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCoverageTermDef, MockCoverageDef, MockCoverageTermOptionDef;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCoverageTermDef = jasmine.createSpy('MockCoverageTermDef');
            MockCoverageDef = jasmine.createSpy('MockCoverageDef');
            MockCoverageTermOptionDef = jasmine.createSpy('MockCoverageTermOptionDef');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CoverageTermDef': MockCoverageTermDef,
                'CoverageDef': MockCoverageDef,
                'CoverageTermOptionDef': MockCoverageTermOptionDef
            };
            createController = function() {
                $injector.get('$controller')("CoverageTermDefDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'alotApp:coverageTermDefUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
