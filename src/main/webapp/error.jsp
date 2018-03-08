<%--
  Created by IntelliJ IDEA.
  User: iy
  Date: 08.03.18
  Time: 15:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>

    <%-- Popup window--%>
    <style>
        #shadow {
            background: rgba(102, 102, 102, 0.5);
            width: 100%;
            height: 100%;
            position: absolute;
            top: 0;
            left: 0;
            display: none; /* show popup window on downloading page*/
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
        #shadow:target {display: block;} /*hide window if #shadow is target*/
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
            <h4 style="color: red;">Error!</h4><br>
            <br>${message}<br>
            <a href="?id=0#shadow" class="close">OK</a> <%-- set #shadow as target--%>
        </div>
    </div>
</body>
</html>
