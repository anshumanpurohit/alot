'use strict';

describe('Controller Tests', function() {

    describe('Loss Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLoss, MockPersonalAutoVehicle;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLoss = jasmine.createSpy('MockLoss');
            MockPersonalAutoVehicle = jasmine.createSpy('MockPersonalAutoVehicle');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Loss': MockLoss,
                'PersonalAutoVehicle': MockPersonalAutoVehicle
            };
            createController = function() {
                $injector.get('$controller')("LossDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'alotApp:lossUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
