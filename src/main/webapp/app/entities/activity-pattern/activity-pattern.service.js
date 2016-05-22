(function() {
    'use strict';
    angular
        .module('alotApp')
        .factory('ActivityPattern', ActivityPattern);

    ActivityPattern.$inject = ['$resource'];

    function ActivityPattern ($resource) {
        var resourceUrl =  'api/activity-patterns/:id';

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
