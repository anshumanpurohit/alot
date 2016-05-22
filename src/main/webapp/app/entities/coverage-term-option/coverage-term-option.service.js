(function() {
    'use strict';
    angular
        .module('alotApp')
        .factory('CoverageTermOption', CoverageTermOption);

    CoverageTermOption.$inject = ['$resource'];

    function CoverageTermOption ($resource) {
        var resourceUrl =  'api/coverage-term-options/:id';

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
