'use strict';

angular.module('kineappApp').controller('SpecialtyDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Specialty',
        function($scope, $stateParams, $modalInstance, entity, Specialty) {

        $scope.specialty = entity;
        $scope.load = function(id) {
            Specialty.get({id : id}, function(result) {
                $scope.specialty = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('kineappApp:specialtyUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.specialty.id != null) {
                Specialty.update($scope.specialty, onSaveFinished);
            } else {
                Specialty.save($scope.specialty, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
