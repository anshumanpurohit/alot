(function() {
    'use strict';
    angular
        .module('alotApp')
        .factory('Lead', Lead);

    Lead.$inject = ['$resource', 'DateUtils'];

    function Lead ($resource, DateUtils) {
        var resourceUrl =  'api/leads/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.requestedTime = DateUtils.convertLocalDateFromServer(data.requestedTime);
                    data.responseTime = DateUtils.convertLocalDateFromServer(data.responseTime);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.requestedTime = DateUtils.convertLocalDateToServer(data.requestedTime);
                    data.responseTime = DateUtils.convertLocalDateToServer(data.responseTime);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.requestedTime = DateUtils.convertLocalDateToServer(data.requestedTime);
                    data.responseTime = DateUtils.convertLocalDateToServer(data.responseTime);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
