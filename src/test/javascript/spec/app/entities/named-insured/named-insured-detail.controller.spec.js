'use strict';

describe('Controller Tests', function() {

    describe('NamedInsured Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockNamedInsured, MockPolicyContact, MockProductLine;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockNamedInsured = jasmine.createSpy('MockNamedInsured');
            MockPolicyContact = jasmine.createSpy('MockPolicyContact');
            MockProductLine = jasmine.createSpy('MockProductLine');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'NamedInsured': MockNamedInsured,
                'PolicyContact': MockPolicyContact,
                'ProductLine': MockProductLine
            };
            createController = function() {
                $injector.get('$controller')("NamedInsuredDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'alotApp:namedInsuredUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
