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
    <div style="display: flex">
        <div style="flex: 1">
            <s:form action="search_art" method="post">
                <s:textfield label="Insert a Search String" name="input" /><br>
                <s:submit />
            </s:form>
            <c:if test="${search_result_art == true}">
                <h1><c:out value = "${'All the dank Artists you searched for fam:'}"/></h1><br>
                <s:form action="search_art_info" method="post">
                    <select name="art_selected" onchange="this.form.submit()">
                        <c:forEach items="${artists}" var="artist">
                            <option value="${artist}" >${artist}</option>
                        </c:forEach>
                    </select>
                    <s:submit />
                </s:form>
            </c:if>
        </div>
        <div style="flex: 1">
            <c:if test="${search_result_art_info == true}">
                <h1>Info on this <c:out value = "${artist['name']}"/> fella:</h1><br>
                <h3><c:out value="${artist['activity_start']}" /></h3><br>
                <h3><c:out value="${artist['activity_end']}" /></h3><br>
                <h3><c:out value="${artist['description']}" /></h3><br>
                <c:forEach items="${artist_albs}" var="alb">
                    <h3><c:out value="${alb['album_name']}" /></h3><br>
                    <h3><c:out value="${alb['album_release']}" /></h3><br>
                </c:forEach>
            </c:if>
        </div>
    </div>
</body>
</html>
