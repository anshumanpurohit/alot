(function() {
    'use strict';
    angular
        .module('alotApp')
        .factory('CoverageDef', CoverageDef);

    CoverageDef.$inject = ['$resource', 'DateUtils'];

    function CoverageDef ($resource, DateUtils) {
        var resourceUrl =  'api/coverage-defs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.beginEffectiveDate = DateUtils.convertLocalDateFromServer(data.beginEffectiveDate);
                    data.endEffectiveDate = DateUtils.convertLocalDateFromServer(data.endEffectiveDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.beginEffectiveDate = DateUtils.convertLocalDateToServer(data.beginEffectiveDate);
                    data.endEffectiveDate = DateUtils.convertLocalDateToServer(data.endEffectiveDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.beginEffectiveDate = DateUtils.convertLocalDateToServer(data.beginEffectiveDate);
                    data.endEffectiveDate = DateUtils.convertLocalDateToServer(data.endEffectiveDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
