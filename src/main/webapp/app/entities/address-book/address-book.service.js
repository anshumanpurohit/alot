(function() {
    'use strict';
    angular
        .module('alotApp')
        .factory('AddressBook', AddressBook);

    AddressBook.$inject = ['$resource'];

    function AddressBook ($resource) {
        var resourceUrl =  'api/address-books/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
