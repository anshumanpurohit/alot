(function() {
    'use strict';

    angular
        .module('alotApp')
        .factory('ProductLineSearch', ProductLineSearch);

    ProductLineSearch.$inject = ['$resource'];

    function ProductLineSearch($resource) {
        var resourceUrl =  'api/_search/product-lines/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
