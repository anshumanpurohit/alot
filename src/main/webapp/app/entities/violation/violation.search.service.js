(function() {
    'use strict';

    angular
        .module('alotApp')
        .factory('ViolationSearch', ViolationSearch);

    ViolationSearch.$inject = ['$resource'];

    function ViolationSearch($resource) {
        var resourceUrl =  'api/_search/violations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
