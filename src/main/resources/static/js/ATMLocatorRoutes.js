angular.module('ATMLocatorApp').config(['$routeProvider', function($routeProvider){

  $routeProvider
    .when('/list',{
      templateUrl: '/view/list.html',
      controller: 'ListCtrl'
    })
	.when('/map',{
      templateUrl: '/view/map.html',
	  controller: 'MapCtrl'
    })
    .otherwise(
    { redirectTo: '/list'}
  );
}]);