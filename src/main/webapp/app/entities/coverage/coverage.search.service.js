(function() {
    'use strict';

    angular
        .module('alotApp')
        .factory('CoverageSearch', CoverageSearch);

    CoverageSearch.$inject = ['$resource'];

    function CoverageSearch($resource) {
        var resourceUrl =  'api/_search/coverages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
