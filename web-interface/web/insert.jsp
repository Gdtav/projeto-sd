<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--
  Created by IntelliJ IDEA.
  User: Guilherme
  Date: 16/12/2018
  Time: 16:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Information</title>
</head>
<body>
    <s:select list="select">
        <s:if test="select == Artist">
            <s:form>
                <s:textfield value="name">Artist Name</s:textfield>
                <s:datetextfield format="yyyy">Artist start date</s:datetextfield>
                <s:number parseIntegerOnly="true" name="num_albums"/>
                <c:forEach begin="1" end="num_albums">
                    <s:textfield value="album">Artist Name</s:textfield>
                </c:forEach>
            </s:form>
        </s:if>
        <s:else>

        </s:else>
    </s:select>
</body>
</html>
