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
							<!--아이디/비밀번호 찾기-->
							<div class="user-profile-container page_item">
								<div>
									<div class="portfolio-info">
										<h3>아이디 찾기</h3>
										<form id="form-join" action="">
											<ul>
												<div class="mb-3 mt-3">
													<label for="uname" class="form-label">이름:</label> <input
														type="text" class="form-control" id="userName"
														name="userName" required />
													<div class="valid-feedback">Valid.</div>
													<div class="invalid-feedback">필수 항목입니다.</div>
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
											</ul>
											<button type="button" class="btn btn-secondary" id="find-btn">아이디
												찾기</button>
										</form>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				</div>
			</section>
		</main>
		<script>
		document.querySelector("#find-btn").addEventListener("click", function findId(){
				if (!document.querySelector("#userName").value) {
					  alert("이름 입력!!");
					  return;
					} else if (!document.querySelector("#emailId").value) {
					  alert("이메일 입력!!");
					  return;
					  
					} else if (!document.querySelector("#emailDomain").value) {
						alert("이메일 도메인 입력!!");
						return;
					}else {
						fetch("${root}/users/find-id",{
							method: 'POST',
							headers: {
							    "Content-Type": "application/json",
							},
							body: JSON.stringify({
								userName: userName.value,
								emailId: emailId.value,
								emailDomain: emailDomain.value
							})
						})
						.then(res => res.json())
						.then(data => {
							alert(data.userId)
							location.href = "${root}/login"
						}).catch(error => {alert("매칭된 이름과 이메일이 없습니다.")})
					}
			});
		</script>
		<!-- End Breadcrumbs -->
		<%@ include file="../include/footer.jsp"%>
</body>
</html>