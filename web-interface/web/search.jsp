<%--
  Created by IntelliJ IDEA.
  User: Pmsilva1 - Asus PC
  Date: 15/12/2018
  Time: 12:42 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Search</title>
</head>
<body>

<c:choose>
    <c:when test="${search_result_art == true}">
        <h1><c:out value = "${'All the dank Artists you searched for fam:'}"/></h1><br>
        <s:form action="search_art_info" method="post">
            <select name="art_selected" onchange="this.form.submit()">
                <c:forEach items="${artists}" var="value">
                    <option value="${artists}" >${artists}</option>
                </c:forEach>
            </select>
        </s:form>
    </c:when>
    <c:when test="${search_result_art_info == true}">
        <h1><c:out value = "${'Info on this ' + artist.name + ' fella:'}"/></h1><br>
        <h3><c:out value="${}" /></h3><br>
        <c:forEach items="${artists}" var="value">

        </c:forEach>
    </c:when>
    <c:otherwise>
        <s:form action="search_art" method="post">
            <s:textfield label="Insert a Search String" name="input" /><br>
            <s:submit />
        </s:form>
    </c:otherwise>
</c:choose>

</body>
</html>
