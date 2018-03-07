<%--
  Created by IntelliJ IDEA.
  User: iy
  Date: 07.03.18
  Time: 22:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Editing</title>
    <%-- Popup window--%>
    <style>
        #shadow {
            background: rgba(102, 102, 102, 0.5);
            width: 100%;
            height: 100%;
            position: absolute;
            top: 0;
            left: 0;
            display: none;
        }
        #window {
            width: 300px;
            height: 200px;
            text-align: center;
            padding: 15px;
            border: 3px solid #0000cc;
            border-radius: 10px;
            color: #0000cc;
            position: absolute;
            top: 0;
            right: 0;
            bottom: 0;
            left: 0;
            margin: auto;
            background: #fff;
        }
        #shadow:target {display: block;}
        .close {
            display: inline-block;
            border: 1px solid #0000cc;
            color: #0000cc;
            padding: 0 12px;
            margin: 10px;
            text-decoration: none;
            background: #f2f2f2;
            font-size: 14pt;
            cursor:pointer;
        }
        .close:hover {background: #e6e6ff;}
    </style>
</head>

<body>
<%-- popup window --%>
<div id="shadow">
    <div id="window">
        Meal editing<br>

        <form action="meals" method="post">
            <table>
                <tr>
                    <td>Date</td>
                    <td><input type="text" name="date"></td>
                </tr>
                <tr>
                    <td>Description</td>
                    <td><input type="text" name="description"></td>
                </tr>
                <tr>
                    <td>Calories</td>
                    <td><input type="text" name="calories"></td>
                </tr>
            </table>
            <input type="submit" value="OK" class="close">
            <a href="#" class="close">Cancel</a>
        </form>
    </div>
</div>

<a href="?id=3#shadow">Meal editing</a>
</body>
</html>
