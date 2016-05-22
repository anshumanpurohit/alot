'use strict';

describe('Controller Tests', function() {

    describe('Activity Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockActivity, MockActivityPattern, MockProducer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockActivity = jasmine.createSpy('MockActivity');
            MockActivityPattern = jasmine.createSpy('MockActivityPattern');
            MockProducer = jasmine.createSpy('MockProducer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Activity': MockActivity,
                'ActivityPattern': MockActivityPattern,
                'Producer': MockProducer
            };
            createController = function() {
                $injector.get('$controller')("ActivityDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'alotApp:activityUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
