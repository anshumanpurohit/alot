(function() {
    'use strict';

    angular
        .module('alotApp')
        .factory('LossSearch', LossSearch);

    LossSearch.$inject = ['$resource'];

    function LossSearch($resource) {
        var resourceUrl =  'api/_search/losses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
