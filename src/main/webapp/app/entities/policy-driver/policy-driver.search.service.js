(function() {
    'use strict';

    angular
        .module('alotApp')
        .factory('PolicyDriverSearch', PolicyDriverSearch);

    PolicyDriverSearch.$inject = ['$resource'];

    function PolicyDriverSearch($resource) {
        var resourceUrl =  'api/_search/policy-drivers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
