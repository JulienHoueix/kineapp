'use strict';

angular.module('kineappApp')
    .factory('Specialty', function ($resource, DateUtils) {
        return $resource('api/specialties/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
