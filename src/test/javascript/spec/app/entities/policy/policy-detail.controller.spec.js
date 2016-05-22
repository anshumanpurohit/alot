'use strict';

describe('Controller Tests', function() {

    describe('Policy Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPolicy, MockProductLine, MockJob;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPolicy = jasmine.createSpy('MockPolicy');
            MockProductLine = jasmine.createSpy('MockProductLine');
            MockJob = jasmine.createSpy('MockJob');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Policy': MockPolicy,
                'ProductLine': MockProductLine,
                'Job': MockJob
            };
            createController = function() {
                $injector.get('$controller')("PolicyDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'alotApp:policyUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
