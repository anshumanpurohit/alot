'use strict';

describe('Controller Tests', function() {

    describe('ActivityPattern Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockActivityPattern;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockActivityPattern = jasmine.createSpy('MockActivityPattern');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ActivityPattern': MockActivityPattern
            };
            createController = function() {
                $injector.get('$controller')("ActivityPatternDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'alotApp:activityPatternUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
