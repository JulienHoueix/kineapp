'use strict';

angular.module('kineappApp').controller('PhysiotherapistDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Physiotherapist', 'Specialty',
        function($scope, $stateParams, $modalInstance, entity, Physiotherapist, Specialty) {

        $scope.physiotherapist = entity;
        $scope.specialties = Specialty.query();
        $scope.load = function(id) {
            Physiotherapist.get({id : id}, function(result) {
                $scope.physiotherapist = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('kineappApp:physiotherapistUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.physiotherapist.id != null) {
                Physiotherapist.update($scope.physiotherapist, onSaveFinished);
            } else {
                Physiotherapist.save($scope.physiotherapist, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
