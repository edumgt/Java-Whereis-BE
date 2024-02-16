<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

<%@ include file="../include/head.jsp"%>

</head>
<body>
	<header id="header" class="fixed-top header-inner-pages">
		<%@ include file="../include/header.jsp"%>
		<main id="main"> <!-- ======= Breadcrumbs ======= -->
		<section id="breadcrumbs" class="breadcrumbs">
			<div class="container">
				<ol>
					<li><a href="index.html">Profile</a></li>
					<li>Info</li>
				</ol>
				<h2>Profile</h2>
			</div>
		</section>
		<section id="portfolio-details" class="portfolio-details">
			<div class="container">
				<div class="page_item">
					<div>
						<!--회원 정보 확인-->
						<c:choose>
							<c:when test="${not requestScope.deleteFail eq true}">
								<h1>회원 탈퇴 완료</h1>	
								<div>
									<strong style="margin: 20px 0">그동안 Live를 이용해 주셔서 감사합니다.</strong> 
								</div>
							</c:when>
							<c:otherwise>
								<h1>회원 탈퇴 실패</h1>
								<div>
									<strong style="margin: 20px 0">회원 탈퇴가 실패하였습니다.</strong> 
								</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</section>
		</main>
		<!-- End Breadcrumbs -->
		<%@ include file="../include/footer.jsp"%>
</body>
</html>