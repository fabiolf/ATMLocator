//var atmLocatorApp = angular.module('ATMLocatorApp',[]);

angular.module('ATMLocatorApp').controller('MapCtrl', ['$scope', 'NgMap', function($scope, NgMap) {
/*
	console.log($("#listMapBtnGroup"));
	var obj = $("#listMapBtnGroup");
	console.log(obj[0].children[0]);
	console.log(obj[0].children[1]);
	*/
	
	$scope.markerClick = function(evt, objIndex) {
		msg = "Address: " + $scope.ATMList[objIndex].address.street + ", " + $scope.ATMList[objIndex].address.housenumber +
			", " + $scope.ATMList[objIndex].address.city + ".";
		alert(msg);
	}


}]);