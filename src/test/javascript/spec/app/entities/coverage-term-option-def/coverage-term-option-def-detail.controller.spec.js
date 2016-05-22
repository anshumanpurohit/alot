'use strict';

describe('Controller Tests', function() {

    describe('CoverageTermOptionDef Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCoverageTermOptionDef, MockCoverageTermDef;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCoverageTermOptionDef = jasmine.createSpy('MockCoverageTermOptionDef');
            MockCoverageTermDef = jasmine.createSpy('MockCoverageTermDef');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CoverageTermOptionDef': MockCoverageTermOptionDef,
                'CoverageTermDef': MockCoverageTermDef
            };
            createController = function() {
                $injector.get('$controller')("CoverageTermOptionDefDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'alotApp:coverageTermOptionDefUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
