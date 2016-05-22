(function() {
    'use strict';
    angular
        .module('alotApp')
        .factory('ProductLineDef', ProductLineDef);

    ProductLineDef.$inject = ['$resource'];

    function ProductLineDef ($resource) {
        var resourceUrl =  'api/product-line-defs/:id';

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
