'use strict';

describe('Controller Tests', function() {

    describe('AccordMapping Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAccordMapping;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAccordMapping = jasmine.createSpy('MockAccordMapping');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'AccordMapping': MockAccordMapping
            };
            createController = function() {
                $injector.get('$controller')("AccordMappingDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'alotApp:accordMappingUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
