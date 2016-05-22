(function() {
    'use strict';
    angular
        .module('alotApp')
        .factory('Discount', Discount);

    Discount.$inject = ['$resource'];

    function Discount ($resource) {
        var resourceUrl =  'api/discounts/:id';

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
