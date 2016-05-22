(function() {
    'use strict';
    angular
        .module('alotApp')
        .factory('Coverage', Coverage);

    Coverage.$inject = ['$resource'];

    function Coverage ($resource) {
        var resourceUrl =  'api/coverages/:id';

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
