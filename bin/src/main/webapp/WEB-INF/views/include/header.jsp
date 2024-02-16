<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!-- ======= Header ======= -->
<!-- <header id="header" class="fixed-top">  -->
<div class="container d-flex align-items-center">
	<h1 class="logo me-auto">
		<a href="${root}/main">Live</a>
	</h1>
	<!-- Uncomment below if you prefer to use an image logo -->
	<!-- <a href="index.html" class="logo me-auto"><img src="assets/img/logo.png" alt="" class="img-fluid"></a>-->

	<nav id="navbar" class="navbar">
		<c:choose>
			<c:when test="${empty sessionScope.userInfo}">
				<ul>
					<li><a class="nav-link scrollto active" href="${root}/main">Home</a></li>
					<li><a class="nav-link scrollto" href="${root}/intro">소개</a></li>
					<li><a class="nav-link scrollto" href="${root}/board/" id="href-board">공지사항</a></li>
					<li class="dropdown"><a href="#" class="getstarted scrollto"><span>Log
								In</span> <i class="bi bi-chevron-down"></i></a>
						<ul>
							<li><a href="${root}/login">로그인</a></li>
							<li><a href="${root}/regist">회원가입</a></li>
							<li><a href="${root}/findId">아이디 찾기</a></li>
							<li><a href="${root}/findPwd">비밀번호 찾기</a></li>
						</ul></li>
				</ul>
			</c:when>
			<c:otherwise>
				<ul>
					<li><a class="nav-link scrollto active" href="${root}/main">Home</a></li>
					<li><a class="nav-link scrollto" href="${root}/aptInfo">아파트
							매매 정보</a></li>
					<li><a class="nav-link scrollto" href="${root}/intro">소개</a></li>
					<li><a class="nav-link scrollto" href="${root}/board/" id="href-board">공지사항</a></li>
					<li class="dropdown"><a href="#" class="getstarted scrollto"><span>Log
								Out</span> <i class="bi bi-chevron-down"></i></a> <c:choose>
							<c:when test="${userInfo.manager == 'T'}">
								<ul>
									<li><a href="${root}/users/logout">로그아웃</a></li>
									<li><a href="${root}/users/confirm" id="href-confirm">회원정보
											확인</a></li>
									<li><a href="${root}/adminPage" id="href-admin">회원정보 관리</a></li>
									<li><a href="${root}/interest" id="href-interest">관심매물
											조회</a></li>
								</ul>
							</c:when>
							<c:otherwise>
								<ul>
									<li><a href="${root}/users/logout">로그아웃</a></li>
									<li><a href="${root}/users/confirm" id="href-confirm">회원정보
											확인</a></li>
									<li><a href="${root}/interest" id="href-interest">관심매물
											조회</a></li>
								</ul>
							</c:otherwise>
						</c:choose></li>
				</ul>
			</c:otherwise>
		</c:choose>
		<!-- 
		<ul>
			<li><a class="nav-link scrollto active" href="${root}/main">Home</a></li>
			<li><a class="nav-link scrollto" href="${root}/intro">소개</a></li>
			<li><a class="nav-link scrollto" href="${root}/board">공지사항</a></li>
			<li class="dropdown"><a href="#" class="getstarted scrollto"><span>Log
						In</span> <i class="bi bi-chevron-down"></i></a>
				<ul>
					<c:choose>
						<c:when test="${empty sessionScope.userInfo}">
							<li><a href="${root}/login">로그인</a></li>
							<li><a href="${root}/regist">회원가입</a></li>
							<li><a href="${root}/findId">ID PWD 찾기</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="${root}/user/logout">로그아웃</a></li>
							<li><a href="${root}/user/confirm" id="href-confirm">회원정보
									확인</a></li>
							<li><a href="${root}/interest" id="href-interest">관심매물 조회</a></li>
						</c:otherwise>
					</c:choose>
				</ul></li>
		</ul>
		 -->
		<i class="bi bi-list mobile-nav-toggle"></i>
	</nav>
	<!-- .navbar -->
</div>
</header>
<script>
document.getElementById("href-confirm").addEventListener("click", function () {
	  fetch("${root}/user/confirm",{
			method: 'GET',
			headers: {
			    "Content-Type": "application/json",
			}
		})
		.then(res => {
			if(res.status == 200){
				location.href = "${root}/confirm";
			}else{
				alert('비정상적인 접근입니다!');
			}
		})
});


</script>
<!-- End Header -->