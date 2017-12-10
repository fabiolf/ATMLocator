//var atmLocatorApp = angular.module('ATMLocatorApp',[]);

angular.module('ATMLocatorApp').controller('MainCtrl', ['$scope', '$location', '$http', function($scope, $location, $http) {
 
  $scope.cityNameDisabled = true;
  $scope.cityNameTextBoxPlaceholder = "Loading data..."

  // first thing, get a list of cities and store in a variable
  $http.get('http://localhost:8080/api/list').
		then(function(response) {
			$scope.cities = response.data;
			$scope.cityNameDisabled = false;
			$scope.cityNameTextBoxPlaceholder = "Type a city name";
		});
  
//  $scope.cityTextBoxValue = "";
   
  var getATMList = function(cityName) {
	   $http.get('http://localhost:8080/api/list/' + cityName).
        then(function(response) {
			if (response.data.length == 0) {
				console.log('no results');
				alert("City not found!");
			} else {
				$scope.ATMList = response.data;
				// adding lat & lng to a list of values on each address to prepare data for map visualization
				//i = 0;
				console.log($scope.ATMList.length);
				angular.forEach($scope.ATMList, function(ATMList){
					ATMList.latlng = [ATMList.address.geoLocation.lat, ATMList.address.geoLocation.lng];
					//$scope.cmVisibilityControl[i] = false;
					//i++;
				});
				$scope.firstViewVisualization = false;
			}
        });
  }
  
  $scope.selectedCityName = "";
  
  $scope.firstViewVisualization = true;
  
  $scope.citySelected = function() {
		if ($scope.selectedCityName != $scope.cityTextBoxValue) {
			$scope.selectedCityName = $scope.cityTextBoxValue;
			getATMList($scope.cityTextBoxValue);
		}
  }
  
  var viewMode = 0; // 0 - list; 1 - map
  
  $scope.changeView = function(view) {
	  if (view == "list") {
		  if (viewMode != 0) {
			  viewMode = 0;
			  $location.path(view);
		  }
	  } else {
		  if (viewMode != 1) {
			  viewMode = 1;
			  $location.path(view);
		  }
	  }
  }
  
  
}]);