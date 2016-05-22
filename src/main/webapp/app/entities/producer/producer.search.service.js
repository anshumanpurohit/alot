(function() {
    'use strict';

    angular
        .module('alotApp')
        .factory('ProducerSearch', ProducerSearch);

    ProducerSearch.$inject = ['$resource'];

    function ProducerSearch($resource) {
        var resourceUrl =  'api/_search/producers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
