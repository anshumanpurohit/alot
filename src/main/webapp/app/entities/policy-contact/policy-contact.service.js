(function() {
    'use strict';
    angular
        .module('alotApp')
        .factory('PolicyContact', PolicyContact);

    PolicyContact.$inject = ['$resource', 'DateUtils'];

    function PolicyContact ($resource, DateUtils) {
        var resourceUrl =  'api/policy-contacts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dob = DateUtils.convertLocalDateFromServer(data.dob);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dob = DateUtils.convertLocalDateToServer(data.dob);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dob = DateUtils.convertLocalDateToServer(data.dob);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
