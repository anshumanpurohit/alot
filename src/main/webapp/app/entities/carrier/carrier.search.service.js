(function() {
    'use strict';

    angular
        .module('alotApp')
        .factory('CarrierSearch', CarrierSearch);

    CarrierSearch.$inject = ['$resource'];

    function CarrierSearch($resource) {
        var resourceUrl =  'api/_search/carriers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
