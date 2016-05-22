(function() {
    'use strict';

    angular
        .module('alotApp')
        .factory('CoverageTermDefSearch', CoverageTermDefSearch);

    CoverageTermDefSearch.$inject = ['$resource'];

    function CoverageTermDefSearch($resource) {
        var resourceUrl =  'api/_search/coverage-term-defs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
