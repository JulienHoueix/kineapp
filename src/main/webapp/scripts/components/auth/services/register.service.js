'use strict';

angular.module('kineappApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


