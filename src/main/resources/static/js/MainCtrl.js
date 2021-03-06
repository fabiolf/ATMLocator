angular.module('ATMLocatorApp').controller('MainCtrl', ['$scope', '$rootScope', '$location', '$http', 'LocationService', function($scope, $rootScope, $location, $http, LocationService) {
	/*
	 * Initialization
	 */
  if ($rootScope.user == undefined) {
	  $rootScope.login();
  }
	
  $scope.cityNameDisabled = true;
  $scope.cityNameTextBoxPlaceholder = "Loading data..."
  $scope.selectedCityName = "";
  $scope.firstViewVisualization = true;

  // get a list of cities and store in a variable
  LocationService.query().$promise.then(function(response) {
	$scope.cities = response;
	$scope.cityNameDisabled = false;
	$scope.cityNameTextBoxPlaceholder = "Type a city name";
  }).catch(function (data) {
	// Handle error here
	msg = 'ERROR! Could not connect to backend at http://localhost:' + serverPort + '/atmlocator-0.1.0/api/list. Please verify.';
	alert(msg);
  });

  // get a list of ATM locations given a city name
  var getATMList = function(cityName) {
	  LocationService.query({name: cityName}).$promise
        .then(function(response) {
			if (response.length == 0) {
				alert("City not found!");
			} else {
				$scope.ATMList = response;
				// adding lat & lng to a list of values on each address to
				// prepare data for map visualization
				angular.forEach($scope.ATMList, function(ATMList){
					ATMList.latlng = [ATMList.address.geoLocation.lat, ATMList.address.geoLocation.lng];
				});
				$scope.firstViewVisualization = false;
			}
        }).catch(function (data) {
        // Handle error here
			msg = 'ERROR! Could not obtain the list of ATM at ' + cityName + ' (the call to http://localhost:' + serverPort + '/api/list/' + cityName + ' failed). Please verify.';
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
			  $location.path("atmlocator-0.1.0/" + view);
		  }
	  } else {
		  if (viewMode != 1) {
			  viewMode = 1;
			  $location.path("atmlocator-0.1.0/" + view);
		  }
	  }
  }
  
	$scope.markerClick = function(evt, objIndex) {
		msg = "Address: " + $scope.ATMList[objIndex].address.street + ", " + $scope.ATMList[objIndex].address.housenumber +
			", " + $scope.ATMList[objIndex].address.city + ".";
		alert(msg);
	}
    
}]);