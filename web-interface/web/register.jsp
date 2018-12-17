<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Guilherme
  Date: 17/12/2018
  Time: 18:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
    <s:form action="register" method="post">
        <s:textfield label="Username" name="username" /><br>
        <s:password label="Password" name="password" /><br>
        <s:submit></s:submit>
    </s:form>
</body>
</html>
