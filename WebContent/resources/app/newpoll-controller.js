myPollApp.controller('NewPollController', function($scope, $uibModal, PollService, contactLists) {
    $scope.contactLists = contactLists;
    $scope.newPoll = {options: [{},{}],
                      contactLists: []};

    $scope.addOption = function(){
        $scope.newPoll.options.push({});
    };

    $scope.removeOption = function(option){
        var index = $scope.newPoll.options.indexOf(option);
        $scope.newPoll.options.splice(index,1);
    };

    $scope.addToSelectedContactLists = function(){
        var contactListToSelect = $scope.contactListToSelect;
        console.log(contactListToSelect);
        var alreadyAdded = false;
        var len = $scope.newPoll.contactLists.length;
        for (var i=0; i < len; i++){
            if (contactListToSelect.contactListId === $scope.newPoll.contactLists[i].contactListId){
                alreadyAdded = true;
            }
        }
        if (alreadyAdded) {
            alert("This ContactList has already been selected.");
        }else{
            $scope.newPoll.contactLists.push(contactListToSelect);
        }
    };

    $scope.removeFromSelectedContactLists = function(selectedContactList){
        var index = $scope.newPoll.contactLists.indexOf(selectedContactList);
        $scope.newPoll.contactLists.splice(index,1);
    };

    function validateNewPollForm(newPoll){
        if (newPoll.contactLists.length == 0){
            $scope.errorMessage = {message: "Please select atleast one contact list"};
            return false;
        } else if (newPoll.options.length < 2){
            $scope.errorMessage = {message: "Please fill in atleast two options"};
            return false;
        } else {
            $scope.errorMessage = null;
            return true;
        }
    }

    function processAlternateValuesString(newPoll){
        var len = newPoll.options.length;
        for (var i=0;i<len;i++){
            var option = newPoll.options[i];
            var alternateValuesString = option.alternateValuesString;
            if (alternateValuesString){
                option.alternateValues = alternateValuesString.split(',');
                delete option.alternateValuesString;
            }
        }
    }

    $scope.sendPoll = function(newPoll){
        if (validateNewPollForm(newPoll)){
            processAlternateValuesString(newPoll);
            PollService.sendPoll(newPoll).then(function successCallback(response) {
                $scope.successMessage = {
                    message:  "Poll has been created and sent.",
                    redirectLink: "poll",
                    redirectText: "Go to Polls "
                };
                $scope.hideNewPollForm = true;
            }, function errorCallback(response) {
                $scope.errorMessage = {
                    message: "Poll creation failed."
                };
            });
        }
    };

});
