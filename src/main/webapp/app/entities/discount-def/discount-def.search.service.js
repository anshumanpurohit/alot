(function() {
    'use strict';

    angular
        .module('alotApp')
        .factory('DiscountDefSearch', DiscountDefSearch);

    DiscountDefSearch.$inject = ['$resource'];

    function DiscountDefSearch($resource) {
        var resourceUrl =  'api/_search/discount-defs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
