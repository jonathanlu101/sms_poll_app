myPollApp.controller('PollIndividualViewController', function($scope, $uibModal,$filter, PollService, poll) {

    $scope.poll = poll;
    var repliedMessagesFilter = $filter('repliedMessagesFilter');
    $scope.poll.stats = {};
    $scope.poll.stats.repliedMessages = repliedMessagesFilter($scope.poll.messages).length || 0;

    $scope.result = getSimpleResultArray(poll);
    $scope.chartLabels = Object.keys($scope.result.validOptions);
    $scope.chartData = Object.values($scope.result.validOptions);

    function getSimpleResultArray(poll){
        var resultObject = {notResponded: 0,
                      invalidOption: 0,
                      validOptions: []};
        var validOptionsDic = {};
        var len = poll.messages.length;
        for (var i=0;i<len;i++){
            var responded = poll.messages[i].smsResponses.length > 0? true : false;
            var hasChosenOption = poll.messages[i].chosenOption ? true: false;
            if (!responded){
                resultObject["notResponded"]++;
            }else if (!hasChosenOption){
                resultObject["invalidOption"]++;
            }else {
                var chosenOptionValue = poll.messages[i].chosenOption.value;
                resultObject.validOptions[chosenOptionValue] = (resultObject.validOptions[chosenOptionValue] || 0) + 1 ;
            }
        }
        return resultObject;
    }

    $scope.updateResponses = function(poll){
        PollService.updateResponses(poll.pollId).then(function(response){
            $scope.poll = response.data;
            console.log(response.data);
            $scope.successMessage = {message: "Poll data has been updated."}
        },function(){
            $scope.errorMessage = {message: "Something went wrong with updating the responses. Try again later."};
        });
    }

    $scope.graphTypeChange = function(){
        if ($scope.graphType == "bar"){
            $scope.chartOptions = {scales: { yAxes : [{ticks: {beginAtZero: true}}]}}
        } else {
            $scope.chartOptions = {};
        }
    }

    $scope.openDeletePollModal = function(poll) {
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: 'resources/templates/deletePollModal.html',
            controller: 'deletePollModalInstanceCtrl',
            controllerAs: '$ctrl',
            resolve: {
                poll: function() {
                    return poll;
                }
            }
        });
        modalInstance.result.then(function(poll) {
            PollService.deletePoll(poll.pollId).then(function successCallback(response) {
                $scope.successMessage = {
                    message:  "Poll " + poll.name + " has been deleted.",
                    redirectLink: "/poll",
                    redirectText: "Return to Poll"
                };
                $scope.poll = null;
            }, function errorCallback(response) {
                $scope.errorMessage = {
                    message: "Deletion failed, please try again later."
                };
            });
        });
    };
});

myPollApp.controller('deletePollModalInstanceCtrl', function($uibModalInstance, poll) {
    $ctrl = this;
    $ctrl.pollName = poll.name;
    $ctrl.delete = function() {
        $uibModalInstance.close(poll);
    };
    $ctrl.cancel = function() {
        $uibModalInstance.dismiss();
    };
});
