(function() {
    'use strict';

    angular
        .module('alotApp')
        .factory('PersonalAutoVehicleSearch', PersonalAutoVehicleSearch);

    PersonalAutoVehicleSearch.$inject = ['$resource'];

    function PersonalAutoVehicleSearch($resource) {
        var resourceUrl =  'api/_search/personal-auto-vehicles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
