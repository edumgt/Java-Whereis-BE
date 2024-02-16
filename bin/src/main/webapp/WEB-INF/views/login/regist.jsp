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
							<c:choose>
								<c:when
									test="${requestScope.success != 1 or empty requestScope.success}">
									<!--회원가입-->
									<div class="col-lg-4 page_item">
										<div>
											<div class="portfolio-info">
												<h3>회원가입</h3>
												<form id="form-join" method="POST"
													action="${root}/user/join">
													<input type="hidden" name="action" value="regist">
													<div class="mb-3">
														<label for="username" class="form-label">이름 : </label> <input
															type="text" class="form-control" id="userName"
															name="userName" placeholder="이름..." />
													</div>
													<div class="mb-3">
														<label for="userid" class="form-label">아이디 : </label> <input
															type="text" class="form-control" id="userId"
															name="userId" placeholder="아이디..." />
													</div>
													<div id="idcheck-result"></div>
													<div class="mb-3">
														<label for="userpwd" class="form-label">비밀번호 : </label> <input
															type="password" class="form-control" id="userPwd"
															name="userPwd" placeholder="비밀번호..." />
													</div>
													<div class="mb-3">
														<label for="pwdcheck" class="form-label">비밀번호확인 :
														</label> <input type="password" class="form-control" id="pwdcheck"
															placeholder="비밀번호확인..." />
													</div>
													<div class="mb-3">
														<label for="emailid" class="form-label">이메일 : </label>
														<div class="input-group">
															<input type="text" class="form-control" id="emailId"
																name="emailId" placeholder="이메일아이디" /> <span
																class="input-group-text">@</span> <select
																class="form-select" id="emailDomain" name="emailDomain"
																aria-label="이메일 도메인 선택">
																<option selected>선택</option>
																<option value="ssafy.com">싸피</option>
																<option value="google.com">구글</option>
																<option value="naver.com">네이버</option>
																<option value="kakao.com">카카오</option>
															</select>
														</div>
													</div>
													<div class="col-auto text-center">
														<button type="button" id="btn-join"
															class="btn btn-outline-primary mb-3">회원가입</button>
													</div>
												</form>
											</div>
										</div>
									</div>
									<script>
								document.querySelector("#btn-join").addEventListener("click", function () {
									if (!document.querySelector("#userName").value) {
									  alert("이름 입력!!");
									  return;
									} else if (!document.querySelector("#userId").value) {
									  alert("아이디 입력!!");
									  return;
									} else if (!document.querySelector("#userPwd").value) {
									  alert("비밀번호 입력!!");
									  return;
									} else if (document.querySelector("#userPwd").value != document.querySelector("#pwdcheck").value) {
									  alert("비밀번호 확인!!");
									  return;
									} else {
										fetch("${root}/users/join",{
											method: 'POST',
											headers: {
											    "Content-Type": "application/json",
											},
											body: JSON.stringify({
												userName: userName.value,
												userId: userId.value,
												userPwd: userPwd.value,
												emailId: emailId.value,
												emailDomain: emailDomain.value
											})
										})
										.then(res => {
											if(res.status == 201){
												location.href = "${root}/login";
											}else{
												alert('아이디 혹은 비밀번호가 틀렸습니다!');
											}
										})
									}
								});
								</script>
								</c:when>
								<c:otherwise>
									<h1>회원 가입 성공</h1>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</section>
		</main>
		<%@ include file="../include/footer.jsp"%>
</body>
</html>