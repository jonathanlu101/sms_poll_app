<!DOCTYPE html>
<html lang="en" ng-app="myPollApp">
  <head>
    <title>Poll This</title>
    <base href="/" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Style Sheets -->
    <link href="resources/content/css/flat-ui/css/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <link href="resources/content/css/flat-ui/css/flat-ui.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="resources/content/css/main.css">
    <!-- BootStrap/FlatUi JS -->
    <script src="resources\app\lib\jquery\jquery-3.1.1.js"></script>
    <script src="resources\app\lib\bootstrap\bootstrap.js"></script>
    <script src="resources/app/lib/flat-ui/vendor/video.js"></script>
    <script src="resources/app/lib/flat-ui/flat-ui.min.js"></script>
    <!-- AngularJS Dependencies -->
    <script src="resources\app\lib\angular\angular.js"></script>
    <script src="resources\app\lib\angular\angular-route.js"></script>
    <script src="resources\app\lib\angular\angular-animate.js"></script>
    <!-- Other JS Files -->
    <script src="resources\app\lib\ui-bootstrap\ui-bootstrap-tpls-2.3.0.js"></script>
    <script src="resources\app\lib\chart\Chart.js"></script>
    <script src="resources\app\lib\chart\angular-chart.js"></script>
    <script src="resources\app\lib\smart-table\smart-table.js"></script>
    <!-- App JS -->
    <script src="resources\app\app-main.js"></script>
    <script src="resources\app\service\contact-list-service.js"></script>
    <script src="resources\app\service\poll-service.js"></script>
    <script src="resources\app\home-controller.js"></script>
    <script src="resources\app\login-controller.js"></script>
    <script src="resources\app\contact-list-edit-controller.js"></script>
    <script src="resources\app\contact-list-listview-controller.js"></script>
    <script src="resources\app\newpoll-controller.js"></script>
    <script src="resources\app\poll-listview-controller.js"></script>
    <script src="resources\app\poll-individual-view-controller.js"></script>
  </head>
  <body>
    <header ng-include="'resources/header.html'"></header>
    <main ng-view></main>
  </body>
</html>
