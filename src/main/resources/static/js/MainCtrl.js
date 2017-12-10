angular.module('ATMLocatorApp').controller('MainCtrl', ['$scope', '$location', '$http', function($scope, $location, $http) {
	/*
	 * Initialization
	 */
 
  $scope.cityNameDisabled = true;
  $scope.cityNameTextBoxPlaceholder = "Loading data..."
  $scope.selectedCityName = "";
  $scope.firstViewVisualization = true;

  // get a list of cities and store in a variable
  $http.get('http://localhost:8080/api/list').
		then(function(response) {
			$scope.cities = response.data;
			$scope.cityNameDisabled = false;
			$scope.cityNameTextBoxPlaceholder = "Type a city name";
		}).catch(function (data) {
        // Handle error here
			msg = 'ERROR! Could not connect to backend at http://localhost:8080/api/list. Please verify.';
			alert(msg);
		});
  
  // get a list of ATM locations given a city name
  var getATMList = function(cityName) {
	  console.log('getATMList');
	   $http.get('http://localhost:8080/api/list/' + cityName).
        then(function(response) {
			if (response.data.length == 0) {
				alert("City not found!");
			} else {
				$scope.ATMList = response.data;
				// adding lat & lng to a list of values on each address to prepare data for map visualization
				angular.forEach($scope.ATMList, function(ATMList){
					ATMList.latlng = [ATMList.address.geoLocation.lat, ATMList.address.geoLocation.lng];
				});
				$scope.firstViewVisualization = false;
			}
        }).catch(function (data) {
        // Handle error here
			msg = 'ERROR! Could not obtain the list of ATM at ' + cityName + ' (the call to http://localhost:8080/api/list/' + cityName + ' failed). Please verify.';
			alert(msg);
		});
		
  }
  
  // handle the 'Go' button submit
  $scope.citySelected = function() {
		if ($scope.selectedCityName != $scope.cityTextBoxValue) {
			$scope.selectedCityName = $scope.cityTextBoxValue;
		}
		if ($scope.cityTextBoxValue.length == 0) {
			$scope.ATMList = undefined;
			$scope.firstViewVisualization = true;
		} else {
			getATMList($scope.cityTextBoxValue);
		}
  }
  
  // handle the toogle button click
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