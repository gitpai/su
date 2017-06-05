<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c_rt"%>   
<!DOCTYPE HTML>
<html>
  <head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
	<meta name="renderer" content="webkit|ie-comp|ie-stand">
	<meta name="keywords" content="scclui框架">
	<meta name="description" content="scclui为轻量级的网站后台管理系统模版。">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta http-equiv="Cache-Control" content="no-siteapp" />
    <title>首页</title>
    
  </head>
  
  <body>
  <c_rt:choose>
  <c_rt:when test="${status}">
  <div align="center"><font  color="green" > <h1>共享雨伞</h1></font></div>
  
	<img  src="resources/image/home.jpg" width="100%" height="99%">	
		
  </c_rt:when>
  
  <c_rt:otherwise>
  		<% response.sendRedirect("login"); %>
  		<%-- <jsp:forward page="home.jsp"></jsp:forward>  --%>
  </c_rt:otherwise>
  </c_rt:choose>
  </body>
</html>