(function() {
    'use strict';

    angular
        .module('alotApp')
        .factory('PolicyContactSearch', PolicyContactSearch);

    PolicyContactSearch.$inject = ['$resource'];

    function PolicyContactSearch($resource) {
        var resourceUrl =  'api/_search/policy-contacts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
