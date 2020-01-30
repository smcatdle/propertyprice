

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>

<body>


	   <h2>Welcome : <c:out value="${2+2}"/>
           | <a href="<c:url value="/j_spring_security_logout" />" > Logout</a></h2>  
	
</body>
</html>