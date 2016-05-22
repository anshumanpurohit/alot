'use strict';

describe('Controller Tests', function() {

    describe('ProductLineDef Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProductLineDef;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProductLineDef = jasmine.createSpy('MockProductLineDef');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ProductLineDef': MockProductLineDef
            };
            createController = function() {
                $injector.get('$controller')("ProductLineDefDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'alotApp:productLineDefUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
