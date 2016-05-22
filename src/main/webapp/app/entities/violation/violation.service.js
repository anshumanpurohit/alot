(function() {
    'use strict';
    angular
        .module('alotApp')
        .factory('Violation', Violation);

    Violation.$inject = ['$resource', 'DateUtils'];

    function Violation ($resource, DateUtils) {
        var resourceUrl =  'api/violations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.violationOccurredDate = DateUtils.convertLocalDateFromServer(data.violationOccurredDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.violationOccurredDate = DateUtils.convertLocalDateToServer(data.violationOccurredDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.violationOccurredDate = DateUtils.convertLocalDateToServer(data.violationOccurredDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
