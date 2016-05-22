(function() {
    'use strict';
    angular
        .module('alotApp')
        .factory('PersonalAutoVehicle', PersonalAutoVehicle);

    PersonalAutoVehicle.$inject = ['$resource'];

    function PersonalAutoVehicle ($resource) {
        var resourceUrl =  'api/personal-auto-vehicles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
