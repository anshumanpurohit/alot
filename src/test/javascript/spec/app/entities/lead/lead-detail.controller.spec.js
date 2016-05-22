'use strict';

describe('Controller Tests', function() {

    describe('Lead Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLead, MockJob, MockCarrier;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLead = jasmine.createSpy('MockLead');
            MockJob = jasmine.createSpy('MockJob');
            MockCarrier = jasmine.createSpy('MockCarrier');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Lead': MockLead,
                'Job': MockJob,
                'Carrier': MockCarrier
            };
            createController = function() {
                $injector.get('$controller')("LeadDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'alotApp:leadUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
