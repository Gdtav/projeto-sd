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
                <h4>Start date: <c:out value="${artist['activity_start']}" /></h4>
                <h4>    End date:<c:out value="${artist['activity_end']}" /></h4><br>
                <h4><c:out value="${artist['description']}" /></h4><br>
                <s:form action="search_alb_info" method="post">
                    <select name="alb_selected" onchange="this.form.submit()">
                        <c:forEach items="${artist_albs}" var="alb">
                            <option value="${alb['album_name']}" >${alb['album_name']} - ${alb['album_release']}</option>
                        </c:forEach>
                    </select>
                    <s:submit />
                </s:form>
            </c:if>
        </div>
        <div style="flex: 1">
            <c:if test="${search_result_alb_info == true}">
                <h1>Info on this <c:out value = "${album['album_name']}"/> album:</h1><br>
                <h4><c:out value="${album['album_release']}" /></h4><br>
                <s:form action="search_alb_info" method="post">
                    <select name="alb_selected" onchange="this.form.submit()">
                        <c:forEach items="${album_songs}" var="song">
                            <option value="${song}" >${song}</option>
                        </c:forEach>
                    </select>
                </s:form>
                <s:form action="search_alb_info" method="post">
                    <select name="alb_selected" onchange="this.form.submit()">
                        <c:forEach items="${album_reviews}" var="review">
                            <option>${review['review_score']} - ${review['review_desc']}</option>
                        </c:forEach>
                    </select>
                </s:form>
            </c:if>
        </div>
    </div>
</body>
</html>
