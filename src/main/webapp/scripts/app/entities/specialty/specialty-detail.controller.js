'use strict';

angular.module('kineappApp')
    .controller('SpecialtyDetailController', function ($scope, $rootScope, $stateParams, entity, Specialty) {
        $scope.specialty = entity;
        $scope.load = function (id) {
            Specialty.get({id: id}, function(result) {
                $scope.specialty = result;
            });
        };
        $rootScope.$on('kineappApp:specialtyUpdate', function(event, result) {
            $scope.specialty = result;
        });
    });
