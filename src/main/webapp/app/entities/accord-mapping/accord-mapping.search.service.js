(function() {
    'use strict';

    angular
        .module('alotApp')
        .factory('AccordMappingSearch', AccordMappingSearch);

    AccordMappingSearch.$inject = ['$resource'];

    function AccordMappingSearch($resource) {
        var resourceUrl =  'api/_search/accord-mappings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
