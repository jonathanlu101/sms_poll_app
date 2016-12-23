myPollApp.controller('LoginController', function($scope, $location, $http, $routeParams, $rootScope) {

    if ($routeParams.error) {
        $scope.errorMessage = "Login failed.";
    } else if ($routeParams.success) {
        $rootScope.loggedIn = true;
        $location.path("/home").search({
            welcome: 'true'
        });
    } else if ($routeParams.logout) {
        $http.get("/auth/logout").then(function(){
            $scope.infoMessage = "You have been logged out.";
            $rootScope.loggedIn = false;
        },function(){
            $scope.errorMessage = "Server error.";
        });
    }
});
