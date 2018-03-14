<%--
  Created by IntelliJ IDEA.
  User: iy
  Date: 14.03.18
  Time: 16:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Filter</title>
</head>
<body>
<%--<jsp:useBean id="filter" type="ru.javawebinar.topjava.model.SearchFilter" scope="request"/>--%>
<form action="meals" method="get" align="left">
    <table>
        <tr>
            <td width="80">From date</td>
            <td><input type="date" name="fromDate"></td>
            <td width="80"></td>
            <td width="50">From time</td>
            <td><input type="time" name="fromTime"></td>
        </tr>
        <tr>
            <td width="80">To date</td>
            <td><input type="date" name="toDate"></td>
            <td width="50"></td>
            <td width="80">To time</td>
            <td><input type="time" name="toTime"></td>
        </tr>
    </table>

    <br>

    <table>
        <tr>
            <td><input type="text" name="txt"></td>
        </tr>
    </table>

    <input type="submit" value="Apply filter">
</form>
</body>
</html>
