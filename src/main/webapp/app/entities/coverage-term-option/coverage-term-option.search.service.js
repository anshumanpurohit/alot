(function() {
    'use strict';

    angular
        .module('alotApp')
        .factory('CoverageTermOptionSearch', CoverageTermOptionSearch);

    CoverageTermOptionSearch.$inject = ['$resource'];

    function CoverageTermOptionSearch($resource) {
        var resourceUrl =  'api/_search/coverage-term-options/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
