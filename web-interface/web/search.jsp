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
        <div style="flex: 1;margin-right:2em">
            <div style="display:table;width: auto;border: 2px solid #666666;border-spacing: 5px; padding: 5px">
                <s:form action="search_art" method="post">
                    <s:textfield label="Search Artist" name="input" />
                    <s:submit />
                </s:form>
            </div>
            <c:if test="${insert_artist == true}">
                <div style="display:table;width: auto; margin-top: 2em; border: 2px solid #666666;border-spacing: 5px; padding: 5px">
                    <h1><c:out value = "${'Create New Artist:'}"/></h1>
                    <form action="new_artist" method="post">
                        <table>
                            <tr>
                                <td>
                                    Artist Name:
                                </td>
                                <td>
                                    <input type="text" name="new_art_name">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Activity Start:
                                </td>
                                <td>
                                    <input type="date" name="new_art_start" value="2018-12-21" min="1950-01-01" max="2099-12-31">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Activity End:
                                </td>
                                <td>
                                    <input type="date" name="new_art_end" value="2018-12-21" min="1950-01-01" max="2099-12-31">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Description:
                                </td>
                                <td>
                                    <textarea rows="4" cols="50" name="new_artist_desc"></textarea>
                                </td>
                            </tr>
                        </table>
                        <input type="submit" value="Create Artist">
                    </form>
                </div>
            </c:if>
            <c:if test="${insert_album == true}">
                <div style="display:table;width: auto; margin-top: 2em; border: 2px solid #666666;border-spacing: 5px; padding: 5px">
                    <h1><c:out value = "${'Create New Album:'}"/></h1>
                    <form action="new_album" method="post">
                        <table>
                            <tr>
                                <td>
                                    Album Name:
                                </td>
                                <td>
                                    <input type="text" name="new_alb_name">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Release Date:
                                </td>
                                <td>
                                    <input type="date" name="new_alb_release" value="2018-12-21" min="1950-01-01" max="2099-12-31">
                                </td>
                            </tr>
                        </table>
                        <input type="submit" value="Create Album">
                    </form>
                </div>
            </c:if>
            <c:if test="${insert_song == true}">
                <div style="display:table;width: auto; margin-top: 2em; border: 2px solid #666666;border-spacing: 5px; padding: 5px">
                    <h1><c:out value = "${'Create New Song:'}"/></h1>
                    <form action="new_song" method="post">
                        <table>
                            <tr>
                                <td>
                                    Song Name:
                                </td>
                                <td>
                                    <input type="text" name="new_song_name">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Lyrics:
                                </td>
                                <td>
                                    <textarea rows="4" cols="50" name="new_song_lyrics"></textarea>
                                </td>
                            </tr>
                        </table>
                        <input type="submit" value="Create Song">
                    </form>
                </div>
            </c:if>
        </div>
        <c:if test="${search_result_art == true}">
            <div style="flex: 1">
                <div style="display:table;width: auto;border: 2px solid #666666;border-spacing: 5px; padding: 5px">
                        <h1><c:out value = "${'Search Results:'}"/></h1>
                        <form action="search_art_info" method="post">
                            <select name="art_selected" style="overflow-y: auto; padding-left: 1em; padding-right: 1em;" size="7" onchange="this.form.submit()">
                                <option disabled selected value style="display:none"> -- select an option -- </option>
                                <c:forEach items="${artists}" var="artist">
                                    <option value="${artist}" >${artist}</option>
                                </c:forEach>
                            </select>
                        </form>

                </div>
                <div style="margin-top:1em;display:table;width: auto;border: 2px solid #666666;border-spacing: 5px; padding: 5px">
                    <form action="show_insert_artist" method="post">
                        <input type="submit" value="New Artist">
                    </form>
                </div>
            </div>
        </c:if>
        <c:if test="${search_result_art_info == true}">
            <div style="flex: 1">
                <div style="display:table;width: auto;border: 2px solid #666666;border-spacing: 5px; padding: 5px">

                        <h1><c:out value = "${artist['name']}"/>:</h1>
                        <h4>Start date: <c:out value="${artist['activity_start']}" /></h4>
                        <h4>End date:<c:out value="${artist['activity_end']}" /></h4>
                        <h4><c:out value="${artist['description']}" /></h4>
                        <form action="search_alb_info" method="post">
                            <select name="alb_selected" style="overflow-y: auto; padding-left: 1em; padding-right: 1em;" size="7" onchange="this.form.submit()">
                                <option disabled selected value style="display:none"> -- select an option -- </option>
                                <c:forEach items="${artist_albs}" var="alb">
                                    <option value="${alb['album_name']}" >${alb['album_name']} | ${alb['album_release']}</option>
                                </c:forEach>
                            </select>
                        </form>

                </div>
                <div style=";display:table;width: auto; margin-top:2em; border: 2px solid #666666;border-spacing: 5px; padding: 5px">
                    <form action="show_insert_album" method="post">
                        <input type="submit" value="New Album">
                    </form>
                </div>
            </div>
        </c:if>
        <c:if test="${search_result_alb_info == true}">
            <div style="flex: 1">
                <div style="display:table;width: auto;border: 2px solid #666666;border-spacing: 5px; padding: 5px">
                    <h1><c:out value = "${album['album_name']}"/> album:</h1>
                    <h4><c:out value="${album['release_date']}" /></h4>
                    <select name="song_selected" readonly="true" style="overflow-y: auto; padding-left: 1em; padding-right: 1em;" size="7" >
                        <option disabled selected value style="display:none"> -- select an option -- </option>
                        <c:forEach items="${album_songs}" var="song">
                            <option value="${song}" >${song}</option>
                        </c:forEach>
                    </select>
                    <div style="display:table;margin-top:1em;width: auto;border: 1px solid #666666;border-spacing: 5px;">
                        <s:form action="review" method="post">
                            Leave a review:
                            <div style="display:table-row;width: auto;clear: both; margin-top:5px;">
                                <select name="review_score">
                                    <option disabled selected value style="display:none"> -- select an option -- </option>
                                    <option value="0">0</option>
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                    <option value="5">5</option>
                                </select>
                            </div>
                            <div style="display:table-row;width: auto;clear: both;">
                                <textarea rows="4" cols="50" name="review_desc"></textarea>
                            </div>
                            <div>
                                <input type="submit" value="Save Review">
                            </div>
                        </s:form>
                    </div>
                    <div style="display:table;margin-top:1em;width: auto;border: 1px solid #666666;border-spacing: 5px;">
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
                </div>
                <div style="margin-top:1em;display:table;width: auto;border: 2px solid #666666;border-spacing: 5px; padding: 5px">
                    <form action="show_insert_song" method="post">
                        <input type="submit" value="New Song">
                    </form>
                </div>
            </div>
        </c:if>
    </div>
</body>
</html>

<c:if test="${review_result == true}">
    <script>alert("Review Successful")</script>
    <c:remove var="review_result" scope="session" />
</c:if>

<c:if test="${review_result == false}">
    <script>alert("There was an error. Please try again")</script>
    <c:remove var="review_result" scope="session" />
</c:if>
