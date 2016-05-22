(function() {
    'use strict';

    angular
        .module('alotApp')
        .factory('ContactSearch', ContactSearch);

    ContactSearch.$inject = ['$resource'];

    function ContactSearch($resource) {
        var resourceUrl =  'api/_search/contacts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
