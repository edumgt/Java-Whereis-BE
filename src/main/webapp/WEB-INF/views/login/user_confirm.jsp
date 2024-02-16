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
		<main id="main">
			<!-- ======= Breadcrumbs ======= -->
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
								<c:when test="${requestScope.confirmFail eq true}">
									<h1>회원 정보 조회 실패</h1>
								</c:when>
								<c:otherwise>
									<div class="user-profile-container page_item">
										<div>
											<div class="portfolio-info">
												<h3>회원 정보 확인</h3>
												<form action="${root}/user" method="post">
													<input type="hidden" name="action" value="delete">
													<div>
														<strong>아이디</strong> <label for="id-verification"
															class="profile-item">${userid}</label>
													</div>
													<div>
														<strong>이름</strong> <label for="id-verification">${username}</label>
													</div>
													<div>
														<strong>email</strong> <label for="id-verification"
															class="profile-item">${emailid}@${emaildomain}</label>
													</div>
													<button type="button" onclick="update()"
														class="btn btn-secondary" id="portfolio-info-modify-btn"
														style="margin-right: 10px">회원정보 수정</button>
													<script>
														function update() {
															location.href = "${root}/update";
														}
													</script>
													<button type="button" onclick="withdrawal()"
														class="btn btn-secondary" id="portfolio-info-delete-btn">회원탈퇴</button>
													<script>
														function withdrawal() {
															if (window.confirm("정말 회원 탈퇴 하시겠습니까?")) {
																fetch("${root}/users", {
																	method: 'DELETE',
																	headers: {
																	    "Content-Type": "application/json",
																	}
																}).then(res => {
																	if(res.status == 200){
																		location.href = "${root}/users/logout";
																	}else{
																		alert('비정상적 접근입니다!');
																	}
																})
															}	
														}
													</script>
												</form>
											</div>
										</div>
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