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
    <div style="display: flex;">
        <div style="flex: 1">
            <div style="display:table;width: auto;border: 2px solid #666666;border-spacing: 5px; padding: 5px">
                <s:form action="search_art" method="post">
                    <s:textfield label="Search Artist" name="input" /><br>
                    <s:submit />
                </s:form>
            </div>
        </div>
        <div style="flex: 1">
            <div style="display:table;width: auto;border: 2px solid #666666;border-spacing: 5px; padding: 5px">
                <c:if test="${search_result_art == true}">
                    <h1><c:out value = "${'Search Results:'}"/></h1><br>
                    <form action="search_art_info" method="post">
                        <select name="art_selected" style="overflow-y: auto; padding-left: 1em; padding-right: 1em;" size="7" onchange="this.form.submit()">
                            <option disabled selected value style="display:none"> -- select an option -- </option>
                            <c:forEach items="${artists}" var="artist">
                                <option value="${artist}" >${artist}</option>
                            </c:forEach>
                        </select>
                    </form>
                </c:if>
            </div>
            <div style="margin-top:2em;display:table;width: auto;border: 2px solid #666666;border-spacing: 5px; padding: 5px">
                <button>New Artist</button>
            </div>
        </div>
        <div style="flex: 1">
            <div style="display:table;width: auto;border: 2px solid #666666;border-spacing: 5px; padding: 5px">
                <c:if test="${search_result_art_info == true}">
                    <h1><c:out value = "${artist['name']}"/>:</h1><br>
                    <h4>Start date: <c:out value="${artist['activity_start']}" /></h4>
                    <h4>End date:<c:out value="${artist['activity_end']}" /></h4>
                    <h4><c:out value="${artist['description']}" /></h4><br>
                    <form action="search_alb_info" method="post">
                        <select name="alb_selected" style="overflow-y: auto; padding-left: 1em; padding-right: 1em;" size="7" onchange="this.form.submit()">
                            <option disabled selected value style="display:none"> -- select an option -- </option>
                            <c:forEach items="${artist_albs}" var="alb">
                                <option value="${alb['album_name']}" >${alb['album_name']} | ${alb['album_release']}</option>
                            </c:forEach>
                        </select>
                    </form>
                </c:if>
            </div>
            <div style="margin-top:2em;display:table;width: auto;border: 2px solid #666666;border-spacing: 5px; padding: 5px">
                <button>New Album</button>
            </div>
        </div>
        <div style="flex: 1">
            <div style="display:table;width: auto;border: 2px solid #666666;border-spacing: 5px; padding: 5px">
                <c:if test="${search_result_alb_info == true}">
                    <h1>Info on this <c:out value = "${album['album_name']}"/> album:</h1><br>
                    <h4><c:out value="${album['album_release']}" /></h4><br>
                    <select name="song_selected" readonly="true" style="overflow-y: auto; padding-left: 1em; padding-right: 1em;" size="7" >
                        <option disabled selected value style="display:none"> -- select an option -- </option>
                        <c:forEach items="${album_songs}" var="song">
                            <option value="${song}" >${song}</option>
                        </c:forEach>
                    </select>
                    <div style="display:table;width: auto;border: 1px solid #666666;border-spacing: 5px;">
                        <c:forEach items="${album_reviews}" var="review">
                            <div style="display:table-row;width: auto;clear: both;">
                                <div style="display:table-column;float: left; text-align: center;width: 25px; background-color: #ccc; border: 1px solid #666666;border-spacing: 5px;">
                                        ${review['review_score']}
                                </div>
                                <div style="display:table-column;float: left; padding-left: 10px;width: 300px; border: 1px solid #666666;border-spacing: 5px;">
                                        ${review['review_desc']}
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <s:form action="search_alb_info" method="post">
                    </s:form>
                </c:if>
            </div>
            <div style="margin-top:2em;display:table;width: auto;border: 2px solid #666666;border-spacing: 5px; padding: 5px">
                <button>New Song</button>
            </div>
        </div>
    </div>
</body>
</html>
