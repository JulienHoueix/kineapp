'use strict';

angular.module('kineappApp')
    .controller('PhysiotherapistController', function ($scope, Physiotherapist) {
        $scope.physiotherapists = [];
        $scope.loadAll = function() {
            Physiotherapist.query(function(result) {
               $scope.physiotherapists = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Physiotherapist.get({id: id}, function(result) {
                $scope.physiotherapist = result;
                $('#deletePhysiotherapistConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Physiotherapist.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePhysiotherapistConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.physiotherapist = {firstName: null, lastName: null, street: null, postalCode: null, city: null, country: null, id: null};
        };
    });
