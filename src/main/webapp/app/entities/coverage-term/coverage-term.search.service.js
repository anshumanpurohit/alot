(function() {
    'use strict';

    angular
        .module('alotApp')
        .factory('CoverageTermSearch', CoverageTermSearch);

    CoverageTermSearch.$inject = ['$resource'];

    function CoverageTermSearch($resource) {
        var resourceUrl =  'api/_search/coverage-terms/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
