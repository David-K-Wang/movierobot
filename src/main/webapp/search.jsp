<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<%@ page isELIgnored="false" %>
	<meta name="description" content="">
	<meta name="author"      content="WangKe">
	
	<title>选电影</title>

	<link rel="shortcut icon" href="../assets/images/logo.png">
	
	<!-- Bootstrap itself -->
	<link href="http://netdna.bootstrapcdn.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet" type="text/css">

	<!-- Custom styles -->
	

	<!-- Fonts -->
	<link href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" rel="stylesheet" type="text/css">
	<link href='http://fonts.googleapis.com/css?family=Wire+One' rel='stylesheet' type='text/css'>
	
	<link href="../assets/css/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
</head>
<body>
	<nav class="navbar navbar-default" role="navigation">
	   <div class="navbar-header">
	      <a class="navbar-brand" href="/">MovieTime</a>
	   </div>
	   <div>
	      <form class="navbar-form navbar-left" role="search" action="/api/search">
	        <div class="form-group">
	           <input type="text" class="form-control" placeholder="电影名称" name="movieName" size="10" value="${result.searchForm.movieName}">
	        </div>
	        <div class="form-group">
	           <input type="text" class="form-control" placeholder="影院名称" name="cinemaName" size = "12" value="${result.searchForm.cinemaName}">
	        </div>
	        <div class="form-group">
               <input type="text" class="form-control" id="datetimepicker1" placeholder="搜索起始时间" name="fromDate" size="16" value="${result.searchForm.fromDateStr}">
           	</div>
           	<div class="form-group">
               <input type="text" class="form-control" id="datetimepicker2" placeholder="搜索结束时间" name="toDate" size="16" value="${result.searchForm.toDateStr}">
           	</div>
         	<button type="submit" class="btn">搜索</button>
	      </form>    
	   </div>
	</nav>
	
	<div class="container">
		<c:forEach items="${result.movies}" var="m">
		
			<div class="col-xs-3 col-md-3 col-md-3 img">
				<img src="${m.moviePicUrl}">
			</div>
					
			<div class="col-xs-9 col-md-9 col-md-9">
				<h2 class="">${m.movieName} </h2>
				<p>
					<small>
						主演：${m.movieDesc}
					</small> 
				</p>
			</div>
			
			<table class="table table-hover">
				<thead>
			      <tr>
			         <th>影院</th>
			         <th>放映时间</th>
			         <th>放映厅</th>
			         <th>价格</th>
			         <th>原价</th>
			         <th>票源</th>
			      </tr>
			    </thead>
				<tbody>
					<c:forEach items="${m.ticketList}" var="t">
					<tr>
					    <td>${t.cinemaName}</td>
					    <td>${t.movieTime}</td>
					    <td>${t.cinemaHall}</td>
					    <td>¥${t.price}</td>
					    <td>¥${t.originalPrice}</td>
					    <td><a href="${t.sourceUrl}" target="_blank">${t.sourceType}</a></td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
			<br/>
			<br/>
		</c:forEach>
	</div>
	
	
	<div class="container">
		
	</div>
	
	

	<script src="../assets/js/jquery.min.js"></script>
	<script src="../assets/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="../assets/js/bootstrap-datetimepicker.min.js" charset="UTF-8"></script>
	<script type="text/javascript" src="../assets/js/locales/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
	<script type="text/javascript">
	    $('#datetimepicker1').datetimepicker({
	        language:  'zh-CN',
	        weekStart: 1,
	        todayBtn:  1,
			autoclose: 1
	    });
	    $('#datetimepicker2').datetimepicker({
	        language:  'zh-CN',
	        weekStart: 1,
	        todayBtn:  1,
			autoclose: 1
	    });
    </script>
</body>
</html>