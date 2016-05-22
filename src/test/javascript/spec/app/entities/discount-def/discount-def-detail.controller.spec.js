'use strict';

describe('Controller Tests', function() {

    describe('DiscountDef Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDiscountDef;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDiscountDef = jasmine.createSpy('MockDiscountDef');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'DiscountDef': MockDiscountDef
            };
            createController = function() {
                $injector.get('$controller')("DiscountDefDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'alotApp:discountDefUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
