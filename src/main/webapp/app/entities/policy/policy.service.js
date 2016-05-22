(function() {
    'use strict';
    angular
        .module('alotApp')
        .factory('Policy', Policy);

    Policy.$inject = ['$resource'];

    function Policy ($resource) {
        var resourceUrl =  'api/policies/:id';

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
