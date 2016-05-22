(function() {
    'use strict';
    angular
        .module('alotApp')
        .factory('AccordMapping', AccordMapping);

    AccordMapping.$inject = ['$resource'];

    function AccordMapping ($resource) {
        var resourceUrl =  'api/accord-mappings/:id';

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
