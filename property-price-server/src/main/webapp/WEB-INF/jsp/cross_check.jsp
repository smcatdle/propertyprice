<!DOCTYPE html>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>

<html>

<head>
<script src= "http://ajax.googleapis.com/ajax/libs/angularjs/1.2.26/angular.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>

<body>

	<c:if test="${pageContext.request.userPrincipal.name != null}">
	   <h2>Welcome : ${pageContext.request.userPrincipal.name} 
           | <a href="<c:url value="/j_spring_security_logout" />" > Logout</a></h2>  
	</c:if>
	
<div ng-app="" ng-controller="customersController"> 

	    <ul>
	    <li ng-repeat="x in crossChecks">
	    <table>
	    <th>{{ x.originalAddress}}</th>
	   	<tr><td>{{ x.currentAddress}}</td></tr>
	  	<tr><td class="match">{{ x.suggestedAddress}}</td></tr>
	  	<tr><td>{{ x.crossCheckCode}}</td></tr>
	    </table>
	    </li>
	    </ul>

<script>
function customersController($scope,$http) {
  $http.get("http://localhost:49823/property-price-server/RetrieveLastCrossCheckServlet")
  .success(function(response) {$scope.crossChecks = response;});
}
</script>

</body>
</html>