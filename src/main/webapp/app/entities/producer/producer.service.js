(function() {
    'use strict';
    angular
        .module('alotApp')
        .factory('Producer', Producer);

    Producer.$inject = ['$resource'];

    function Producer ($resource) {
        var resourceUrl =  'api/producers/:id';

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
