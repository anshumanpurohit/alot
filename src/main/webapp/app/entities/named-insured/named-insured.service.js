(function() {
    'use strict';
    angular
        .module('alotApp')
        .factory('NamedInsured', NamedInsured);

    NamedInsured.$inject = ['$resource'];

    function NamedInsured ($resource) {
        var resourceUrl =  'api/named-insureds/:id';

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
