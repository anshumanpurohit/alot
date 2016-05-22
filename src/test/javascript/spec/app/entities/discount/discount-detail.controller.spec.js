'use strict';

describe('Controller Tests', function() {

    describe('Discount Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDiscount, MockProductLine, MockDiscountDef;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDiscount = jasmine.createSpy('MockDiscount');
            MockProductLine = jasmine.createSpy('MockProductLine');
            MockDiscountDef = jasmine.createSpy('MockDiscountDef');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Discount': MockDiscount,
                'ProductLine': MockProductLine,
                'DiscountDef': MockDiscountDef
            };
            createController = function() {
                $injector.get('$controller')("DiscountDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'alotApp:discountUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
