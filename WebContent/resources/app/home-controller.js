myPollApp.controller('HomeController', function($scope, $rootScope, $routeParams) {
    if ($routeParams.welcome) {
        $scope.welcome = true;
    }
});
