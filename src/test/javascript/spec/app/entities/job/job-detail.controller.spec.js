'use strict';

describe('Controller Tests', function() {

    describe('Job Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockJob, MockLead, MockPolicy;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockJob = jasmine.createSpy('MockJob');
            MockLead = jasmine.createSpy('MockLead');
            MockPolicy = jasmine.createSpy('MockPolicy');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Job': MockJob,
                'Lead': MockLead,
                'Policy': MockPolicy
            };
            createController = function() {
                $injector.get('$controller')("JobDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'alotApp:jobUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
