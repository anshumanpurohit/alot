(function() {
    'use strict';

    angular
        .module('alotApp')
        .factory('ProductLineDefSearch', ProductLineDefSearch);

    ProductLineDefSearch.$inject = ['$resource'];

    function ProductLineDefSearch($resource) {
        var resourceUrl =  'api/_search/product-line-defs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
