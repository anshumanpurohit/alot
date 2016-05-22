'use strict';

describe('Controller Tests', function() {

    describe('CoverageDef Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCoverageDef, MockCoverageTermDef;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCoverageDef = jasmine.createSpy('MockCoverageDef');
            MockCoverageTermDef = jasmine.createSpy('MockCoverageTermDef');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CoverageDef': MockCoverageDef,
                'CoverageTermDef': MockCoverageTermDef
            };
            createController = function() {
                $injector.get('$controller')("CoverageDefDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'alotApp:coverageDefUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
