'use strict';

angular.module('kineappApp').controller('PhysiotherapistDialogController',
        function($scope, $stateParams, $modalInstance, entity, Physiotherapist, Specialty, $http) {

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
        
        $scope.updateLocalization = function() {
        	$http.post('api/physiotherapists/updatelocalization', $scope.physiotherapist).then(function(response) {
        		$scope.physiotherapist = response.data;
        		if ($scope.physiotherapist.latitude && $scope.physiotherapist.longitude) {
            		$scope.map = { center: { latitude: $scope.physiotherapist.latitude, longitude: $scope.physiotherapist.longitude }, zoom: 15 };
        		} else {
        			$scope.map = undefined;
        		}
        	});
        }
        
});
