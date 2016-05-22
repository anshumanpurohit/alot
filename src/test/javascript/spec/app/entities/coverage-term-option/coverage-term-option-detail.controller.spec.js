'use strict';

describe('Controller Tests', function() {

    describe('CoverageTermOption Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCoverageTermOption, MockCoverageTerm, MockCoverageTermOptionDef;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCoverageTermOption = jasmine.createSpy('MockCoverageTermOption');
            MockCoverageTerm = jasmine.createSpy('MockCoverageTerm');
            MockCoverageTermOptionDef = jasmine.createSpy('MockCoverageTermOptionDef');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CoverageTermOption': MockCoverageTermOption,
                'CoverageTerm': MockCoverageTerm,
                'CoverageTermOptionDef': MockCoverageTermOptionDef
            };
            createController = function() {
                $injector.get('$controller')("CoverageTermOptionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'alotApp:coverageTermOptionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
