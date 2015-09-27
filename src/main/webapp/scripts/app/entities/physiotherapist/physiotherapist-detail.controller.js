'use strict';

angular.module('kineappApp')

    .controller('PhysiotherapistDetailController', function ($scope, $rootScope, $stateParams, entity, Physiotherapist, Specialty) {
    	
        $scope.physiotherapist = entity;
        $scope.map = { center: { latitude: $scope.physiotherapist.latitude, longitude: $scope.physiotherapist.longitude }, zoom: 15 };
        
        $scope.load = function (id) {
            Physiotherapist.get({id: id}, function(result) {
                $scope.physiotherapist = result;
            });
        };
        
        $rootScope.$on('kineappApp:physiotherapistUpdate', function(event, result) {
            $scope.physiotherapist = result;
        });

    });
