(function() {
    'use strict';

    angular
        .module('alotApp')
        .factory('AddressBookSearch', AddressBookSearch);

    AddressBookSearch.$inject = ['$resource'];

    function AddressBookSearch($resource) {
        var resourceUrl =  'api/_search/address-books/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
