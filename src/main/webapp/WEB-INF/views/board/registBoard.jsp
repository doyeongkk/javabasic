<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="UTF-8">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>Dashboard - SB Admin</title>
        
		
	 	<!-- <link href="https://netdna.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.css" rel="stylesheet"> -->
		<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.0/jquery.js"></script> 
		<script src="https://netdna.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.js"></script> 
		<link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.2/summernote.css" rel="stylesheet">
		<script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.2/summernote.js" defer></script>

        
        <link href="${cp}/css/styles.css" rel="stylesheet" />
        <link href="https://cdn.datatables.net/1.10.20/css/dataTables.bootstrap4.min.css" rel="stylesheet" crossorigin="anonymous" />
        <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/js/all.min.js" crossorigin="anonymous"></script>
        
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        
          <link rel="stylesheet" href="${cp}/resources/bootstrap/plugins/fontawesome-free/css/all.min.css">
		<!-- Theme style -->
		<link rel="stylesheet" href="${cp}/resources/bootstrap/dist/css/adminlte.min.css">
        
		<style type="text/css">
			.note-editable panel-body{
				height: 700px;
			}
		</style>

        
        <script>
      	$(function() {
      		var fileCnt = 1;
        	$('#summernote').summernote();	
        	
        	$("#addfile").on("click", function () {
        		fileCnt++;
        		if(fileCnt <= 5){
					$("#filediv").append(
						'<br> <input type="file" name="fileName">'		
					)
        		}else{
        			alert("??????????????? 5????????? ???????????????.");
        		}
			});
        	$("#registBoard").on("click",function(){
        		var title = $("#title").val();
        		if(title==""){
        			alert("????????? ???????????? ???????????????.");
        			return false;
        		}
        		$("#frm").submit();
        	});
		});

        </script>
    </head>
    <body class="sb-nav-fixed">
        <nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark">
            <a class="navbar-brand" href="${cp}/board/mainController">????????? ?????????</a>
            <button class="btn btn-link btn-sm order-1 order-lg-0" id="sidebarToggle" href="#"><i class="fas fa-bars"></i></button>
            <!-- Navbar Search-->
            <form class="d-none d-md-inline-block form-inline ml-auto mr-0 mr-md-3 my-2 my-md-0">
                <div class="input-group">
                    <input class="form-control" type="text" placeholder="Search for..." aria-label="Search" aria-describedby="basic-addon2" />
                    <div class="input-group-append">
                        <button class="btn btn-primary" type="button"><i class="fas fa-search"></i></button>
                    </div>
                </div>
            </form>
            <!-- Navbar-->
            <ul class="navbar-nav ml-auto ml-md-0">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" id="userDropdown" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><i class="fas fa-user fa-fw"></i></a>
                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="userDropdown">
                        <a class="dropdown-item" href="#">Settings</a>
                        <a class="dropdown-item" href="#">Activity Log</a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item" href="${cp}/board/logout">Logout</a>
                    </div>
                </li>
            </ul>
        </nav>
        <div id="layoutSidenav">
            <div id="layoutSidenav_nav">
                <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
                    <div class="sb-sidenav-menu">
                        <div class="nav">
                        	<div class="sb-sidenav-menu-heading">?????????</div>
                            <a class="nav-link" href="${cp}/board/registBoardInfo">
                                <div class="sb-nav-link-icon">
                                	<img src="${cp}/board/profile?userid=${S_USER.userid}" style="width: 40px; height: 40px;" class="img-circle elevation-2" alt="User Image">
                                </div> 
                                ${S_USER.usernm}
                            </a>
                        
                            <div class="sb-sidenav-menu-heading">???????????????</div>
                            <a class="nav-link" href="${cp}/board/registBoardInfo">
                                <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
                                ????????? ??????
                            </a>
                            <div class="sb-sidenav-menu-heading">????????? ??????</div>
                            <c:forEach items="${boardInfoList}" var="boardinfo">
                            	<c:if test="${boardinfo.bor_act==1}">
                            		<a class="nav-link collapsed" href="${cp}/board/boardView?boardNo=${boardinfo.bor_no}" >
		                                <div class="sb-nav-link-icon"><i class="fas fa-columns"></i></div>
		                                ${boardinfo.bor_name}
		                            </a>
                            	</c:if>
                            </c:forEach>
                            
                        </div>
                    </div>
                    <div class="sb-sidenav-footer">
                        <div class="small">Logged in as:</div>
                        Start Bootstrap
                    </div>
                </nav>
            </div>
            <div id="layoutSidenav_content">
                <main>
                    <div class="container-fluid">
                        <h1 class="mt-4">????????? ?????? ?????????</h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item active">???????????? ??????????????????!</li>
                        </ol>
                    
                        <div class="card mb-4">
                            <div class="card-header">
                                <i class="fas fa-table mr-1"></i>
                                ????????? ??????
                            </div>
                            <div class="card-body">
                            	<form id="frm" action="${cp}/board/registBoard"  method="post" enctype="multipart/form-data">
                            		<input type="hidden" name="boardNo" value="${boardNo}">
                            		<input type="hidden" name="userId" value="${S_USER.userid}">
	                                <div class="table-responsive">
	                                	?????? : <input type="text" id="title" name="title"><br><br>
	                                	?????????<br>
	                                	<textarea id="summernote" name="cont"></textarea>
	                             		
										<br>
										????????????<br>
										<div style="float: left;" id="filediv">
											<input type="file" name="fileName">
	                                	</div>
	                                	<input type="button" id="addfile" value="???????????? ????????????"><br><br>
	                                	<div>
	                                		<input type="button" id="registBoard" style="float: right;" value="????????????"><br>
	                                	</div>
	                                </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </main>
                <footer class="py-4 bg-light mt-auto">
                    <div class="container-fluid">
                        <div class="d-flex align-items-center justify-content-between small">
                            <div class="text-muted">Copyright &copy; Your Website 2020</div>
                            <div>
                                <a href="#">Privacy Policy</a>
                                &middot;
                                <a href="#">Terms &amp; Conditions</a>
                            </div>
                        </div>
                    </div>
                </footer>
            </div>
        </div>
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="${cp}/js/scripts.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.js" crossorigin="anonymous"></script>
        <script src="${cp}/assets/demo/chart-area-demo.js"></script>
        <script src="${cp}/assets/demo/chart-bar-demo.js"></script>
        <script src="https://cdn.datatables.net/1.10.20/js/jquery.dataTables.min.js" crossorigin="anonymous"></script>
        <script src="https://cdn.datatables.net/1.10.20/js/dataTables.bootstrap4.min.js" crossorigin="anonymous"></script>
        <script src="${cp}/assets/demo/datatables-demo.js"></script>
    </body>
</html>
