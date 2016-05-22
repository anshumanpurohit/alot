(function() {
    'use strict';

    angular
        .module('alotApp')
        .factory('CoverageTermOptionDefSearch', CoverageTermOptionDefSearch);

    CoverageTermOptionDefSearch.$inject = ['$resource'];

    function CoverageTermOptionDefSearch($resource) {
        var resourceUrl =  'api/_search/coverage-term-option-defs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
