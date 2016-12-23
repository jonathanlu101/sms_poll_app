myPollApp.controller('ContactListViewController', function($scope, $uibModal, ContactListService) {
    ContactListService.getAllContactlists().then(function successCallback(response) {
        $scope.contactLists = response.data;
    }, function errorCallback(response) {});

    $scope.openCreateContactListModal = function() {
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: 'resources/templates/createContactListModal.html',
            controller: 'createContactListModalInstanceCtrl',
            controllerAs: '$ctrl'
        });

        modalInstance.result.then(function(newContactList) {
            ContactListService.createContactList(newContactList).then(function successCallback(response) {
                newContactList.contactListId = response.data;
                newContactList.contacts = [];
                $scope.contactLists.push(newContactList);
                $scope.successMessage = {
                    message: "Contact List '" + newContactList.name + "' has been created."
                };
            }, function errorCallback(response) {
                $scope.errorMessage = {
                    message: "Failed to create contact list, Please try again Later"
                };
            });
        });
    };

    $scope.openDeleteContactListModal = function(contactList) {
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: 'resources/templates/deleteContactListModal.html',
            controller: 'deleteContactListModalInstanceCtrl',
            controllerAs: '$ctrl',
            resolve: {
                contactList: function() {
                    return contactList;
                }
            }
        });

        modalInstance.result.then(function(contactList) {
            ContactListService.deleteContactList(contactList).then(function successCallback(response) {
                var index = $scope.contactLists.indexOf(contactList);
                $scope.contactLists.splice(index, 1);
                $scope.successMessage = {
                    message: "Contact List '" + contactList.name + "' has been deleted."
                };
            }, function errorCallback(response) {
                $scope.errorMessage = "Deletion failed, please try again later";
            });
        });
    };

});

myPollApp.controller('createContactListModalInstanceCtrl', function($uibModalInstance) {
    $ctrl = this;
    $ctrl.create = function(newContactList) {
        $uibModalInstance.close(newContactList);
    };
    $ctrl.cancel = function() {
        $uibModalInstance.dismiss();
    };
});

myPollApp.controller('deleteContactListModalInstanceCtrl', function($uibModalInstance, contactList) {
    $ctrl = this;
    $ctrl.contactListName = contactList.name;
    $ctrl.ok = function() {
        $uibModalInstance.close(contactList);
    };
    $ctrl.cancel = function() {
        $uibModalInstance.dismiss();
    };
});

myPollApp.controller('editContactListModalInstanceCtrl', function($uibModalInstance, editedContactList) {
    $ctrl = this;
    $ctrl.editedContactList = editedContactList;
    $ctrl.ok = function() {
        $uibModalInstance.close(editedContactList);
    };
    $ctrl.cancel = function() {
        $uibModalInstance.dismiss();
    };
});
