(function() {
    'use strict';

    angular
        .module('alotApp')
        .factory('PolicySearch', PolicySearch);

    PolicySearch.$inject = ['$resource'];

    function PolicySearch($resource) {
        var resourceUrl =  'api/_search/policies/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
