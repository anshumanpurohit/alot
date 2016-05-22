(function() {
    'use strict';

    angular
        .module('alotApp')
        .factory('DiscountSearch', DiscountSearch);

    DiscountSearch.$inject = ['$resource'];

    function DiscountSearch($resource) {
        var resourceUrl =  'api/_search/discounts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
