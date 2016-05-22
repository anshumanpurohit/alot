(function() {
    'use strict';

    angular
        .module('alotApp')
        .factory('ActivityPatternSearch', ActivityPatternSearch);

    ActivityPatternSearch.$inject = ['$resource'];

    function ActivityPatternSearch($resource) {
        var resourceUrl =  'api/_search/activity-patterns/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
