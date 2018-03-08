<%--
  Created by IntelliJ IDEA.
  User: iy
  Date: 05.03.18
  Time: 19:22
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Meals</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
</head>
<body>
<table>
    <tr>
        <td width="100"><h3><a href="index.html">Home</a></h3></td>
        <td width="100"><h3><a href="?add=true#shadow">Add meal</a></h3></td>
    </tr>
</table>
<hr>
<c:if test="${!empty meals}">
    <table class="tg">
            <%-- table head--%>
        <tr align="left">
            <th width="300">Date</th>
            <th width="300">Description</th>
            <th width="300">Calories</th>
            <th width="100">Edit</th>
            <th width="100">Delete</th>
        </tr>

            <%-- table data--%>
        <c:forEach items="${meals}" var="meal">
            <%--set color--%>
            <c:choose>
                <c:when test="${!meal.exceed}">
                    <c:set var="color" value="green"/>
                </c:when>
                <c:otherwise>
                    <c:set var="color" value="red"/>
                </c:otherwise>
            </c:choose>
            <%-- set horizontal line--%>
            <tr>
                <td colspan="5">
                    <hr>
                </td>
            </tr>
            <tr style="color: ${color}">
                <td><%--deleting 'T' from date--%>
                    <c:forTokens items="${meal.getDateTime()}" delims="T" var="time">
                        ${time}
                    </c:forTokens>
                </td>
                <td>${meal.getDescription()}</td>
                <td>${meal.getCalories()}</td>
                <%-- editing. Add id to url for getting it in method doGet()--%>
                <td><a href="?id=${meal.getId()}#shadow">edit</a></td>
                <%-- deleting. Add id to url for getting it in method doGet()--%>
                <td><a href="?id=${meal.getId()}&del=true">delete</a></td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<%@ include file="form.jsp"%>

</body>
</html>
