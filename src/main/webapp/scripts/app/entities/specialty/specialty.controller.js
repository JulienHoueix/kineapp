'use strict';

angular.module('kineappApp')
    .controller('SpecialtyController', function ($scope, Specialty) {
        $scope.specialtys = [];
        $scope.loadAll = function() {
            Specialty.query(function(result) {
               $scope.specialtys = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Specialty.get({id: id}, function(result) {
                $scope.specialty = result;
                $('#deleteSpecialtyConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Specialty.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSpecialtyConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.specialty = {name: null, id: null};
        };
    });
