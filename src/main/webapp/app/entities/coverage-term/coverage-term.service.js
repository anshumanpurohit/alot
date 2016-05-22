(function() {
    'use strict';
    angular
        .module('alotApp')
        .factory('CoverageTerm', CoverageTerm);

    CoverageTerm.$inject = ['$resource'];

    function CoverageTerm ($resource) {
        var resourceUrl =  'api/coverage-terms/:id';

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
