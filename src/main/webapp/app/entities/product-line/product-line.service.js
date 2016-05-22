(function() {
    'use strict';
    angular
        .module('alotApp')
        .factory('ProductLine', ProductLine);

    ProductLine.$inject = ['$resource', 'DateUtils'];

    function ProductLine ($resource, DateUtils) {
        var resourceUrl =  'api/product-lines/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.startEffectiveDate = DateUtils.convertLocalDateFromServer(data.startEffectiveDate);
                    data.endEffectiveDate = DateUtils.convertLocalDateFromServer(data.endEffectiveDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.startEffectiveDate = DateUtils.convertLocalDateToServer(data.startEffectiveDate);
                    data.endEffectiveDate = DateUtils.convertLocalDateToServer(data.endEffectiveDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.startEffectiveDate = DateUtils.convertLocalDateToServer(data.startEffectiveDate);
                    data.endEffectiveDate = DateUtils.convertLocalDateToServer(data.endEffectiveDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
