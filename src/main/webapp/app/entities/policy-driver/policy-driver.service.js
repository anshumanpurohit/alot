(function() {
    'use strict';
    angular
        .module('alotApp')
        .factory('PolicyDriver', PolicyDriver);

    PolicyDriver.$inject = ['$resource'];

    function PolicyDriver ($resource) {
        var resourceUrl =  'api/policy-drivers/:id';

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
