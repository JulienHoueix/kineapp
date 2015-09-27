'use strict';

angular.module('kineappApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
