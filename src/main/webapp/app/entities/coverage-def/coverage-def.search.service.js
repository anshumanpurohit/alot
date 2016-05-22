(function() {
    'use strict';

    angular
        .module('alotApp')
        .factory('CoverageDefSearch', CoverageDefSearch);

    CoverageDefSearch.$inject = ['$resource'];

    function CoverageDefSearch($resource) {
        var resourceUrl =  'api/_search/coverage-defs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
