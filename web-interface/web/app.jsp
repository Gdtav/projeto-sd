<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>DropMusic</title>
</head>
<body>

	<c:choose>
		<c:when test="${session.loggedin == true}">
			<p>Welcome, ${session.username}.</p>
		</c:when>
		<c:otherwise>
			<p>Welcome, anonymous user. Tell someone to fuck off!</p>
		</c:otherwise>
	</c:choose>

    <h1>Main Menu</h1>
    <h2><a href='${pageContext.request.contextPath}/search.jsp'>Search Artist or Album</a></h2>

	<p><a href="<s:url action="index" />">Back to login</a></p>

</body>
</html>