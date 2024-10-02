<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
this is error page

<h3>Error Page</h3>
<p><%= session.getAttribute("error") %></p>

</body>
</html>