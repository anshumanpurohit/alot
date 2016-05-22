(function() {
    'use strict';
    angular
        .module('alotApp')
        .factory('Loss', Loss);

    Loss.$inject = ['$resource', 'DateUtils'];

    function Loss ($resource, DateUtils) {
        var resourceUrl =  'api/losses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.lossOccurredDate = DateUtils.convertLocalDateFromServer(data.lossOccurredDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.lossOccurredDate = DateUtils.convertLocalDateToServer(data.lossOccurredDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.lossOccurredDate = DateUtils.convertLocalDateToServer(data.lossOccurredDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
