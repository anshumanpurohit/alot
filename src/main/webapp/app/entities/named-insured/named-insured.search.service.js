(function() {
    'use strict';

    angular
        .module('alotApp')
        .factory('NamedInsuredSearch', NamedInsuredSearch);

    NamedInsuredSearch.$inject = ['$resource'];

    function NamedInsuredSearch($resource) {
        var resourceUrl =  'api/_search/named-insureds/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
