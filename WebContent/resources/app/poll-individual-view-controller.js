myPollApp.controller('PollIndividualViewController', function($scope, $uibModal, $filter, PollService, poll) {

    var repliedMessagesFilter = $filter('repliedMessagesFilter');
    var timestampFilter = $filter('timestampFilter');

    $scope.poll = poll;
    $scope.poll.stats = {};
    $scope.poll.stats.repliedMessages = repliedMessagesFilter($scope.poll.messages).length || 0;

    $scope.result = getSimpleResultArray(poll);
    $scope.itemsByPage = 10;
    $scope.tableData = getTableRowArray(poll.messages);
    $scope.tableHeader = ["First Name","Last Name","Mobile","Option","Last Reply","Received"];

    $scope.chartLabels = Object.keys($scope.result.validOptions);
    $scope.chartData = Object.values($scope.result.validOptions);

    function getSimpleResultArray(poll){
        var resultObject = {notResponded: 0,
                      invalidOption: 0,
                      validOptions: []};
        var validOptionsDic = {};
        for (i = 0, len = poll.messages.length; i < len; i++){
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

    function getTableRowArray(messages){
        var rowArray = [];
        for (i = 0, len= messages.length; i < len; i++){
            var msg = messages[i];
            var newItem = {
                           firstName: msg.firstName,
                           lastName: msg.lastName,
                           mobileNumber: msg.mobileNumber,
                           chosenOption: msg.chosenOption.value || "",
                           lastMessage: msg.smsResponses[0].content || "",
                           lastMessageTime: timestampFilter(msg.smsResponses[0].acknowledgedTimestamp)
            };
            rowArray.push(newItem);
        }
        return rowArray;
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
