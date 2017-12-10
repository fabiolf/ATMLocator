angular.module('ATMLocatorApp').config(['$routeProvider', function($routeProvider) {
	/*
	 * When the application was deployed in Tomcat, it served the pages
	 * not in the / (root) context anymore. Tomcat added the application
	 * name to the path. So I had to add the application name to this
	 * routes too. I'm sure there is a better way to avoid this
	 * hardcode, but I won't have time to find out the optimum and
	 * cleaner way to do this. So, I left this //TODO here
	 */
	// TODO look for a better way to avoid this application name
	// hardcoded here
	$routeProvider.when('/atmlocator-0.1.0/list', {
		templateUrl : '/atmlocator-0.1.0/view/list.html',
	}).when('/atmlocator-0.1.0/map', {
		templateUrl : '/atmlocator-0.1.0/view/map.html',
	}).otherwise({
		redirectTo : '/atmlocator-0.1.0/list'
	});

} ]);