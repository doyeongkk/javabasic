<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	int deleteCnt = (Integer)request.getAttribute("deleteCnt");
	if(deleteCnt==1){

%>
	{
		"cnt" : "1"
	}
<%		
	}else{
%>
	{
		"cnt" : "0"
	}
<%		
	}
%>