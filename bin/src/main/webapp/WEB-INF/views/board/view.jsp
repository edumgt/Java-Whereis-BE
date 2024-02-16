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
			<section id="breadcrumbs" class="breadcrumbs">
				<div class="container">
					<ol>
						<li><a href="index.html">LIVE</a></li>
						<li>정보</li>
					</ol>
					<h2>공지사항</h2>
				</div>
			</section>

			<div class="row justify-content-center">
				<div class="col-lg-8 col-md-10 col-sm-12">
					<h2 class="my-3 py-3 shadow-sm bg-light text-center">
						<mark class="sky">글보기</mark>
					</h2>
				</div>
				<div class="col-lg-8 col-md-10 col-sm-12">
					<div class="row my-2">
						<h2 class="text-secondary px-5">${articleNo}.${subject}</h2>
					</div>
					<div class="row">
						<div class="col-md-8">
							<div class="clearfix align-content-center">
								<img class="avatar me-2 float-md-start bg-light p-2"
									src="https://raw.githubusercontent.com/twbs/icons/main/icons/person-fill.svg" />
								<p>
									<span class="fw-bold">${userId}</span> <br /> <span
										class="text-secondary fw-light"> ${registerTime} 조회 :
										${hit +1}</span>
								</p>
							</div>
						</div>
						<div class="divider mb-3"></div>
						<div class="text-secondary">${content}</div>
						<div class="divider mt-3 mb-3"></div>
						<div class="d-flex justify-content-end">
							<button type="button" id="btn-list"
								class="btn btn-outline-primary mb-3">글목록</button>
							<c:if test="${userId eq userInfo.userId}">
								<button type="button" id="btn-mv-modify" onclick="modify('${articleNo}', '${content}', '${subject}')"
									class="btn btn-outline-success mb-3 ms-1">글수정</button>
								<button type="button" id="btn-delete"
									class="btn btn-outline-danger mb-3 ms-1">글삭제</button>
							</c:if>
						</div>
					</div>
				</div>
			</div>
			<form id="form-param" method="get" action="">
				<input type="hidden" id="act" name="action" value="">
			</form>
			<form id="form-no-param" method="get" action="${root}/board">
				<input type="hidden" id="nact" name="action" value=""> 
				<input type="hidden" id="articleno" name="articleno" value="${articleNo}">
			</form>
			<script>
			document.querySelector("#btn-list").addEventListener("click", function() {
				let form = document.querySelector("#form-param");
				document.querySelector("#act").value = "list";
				form.setAttribute("action", "${root}/board");
				form.submit();

			});
			
			function modify(articleNo, content, subject){
				console.log(articleNo);
				console.log(content);
				console.log(subject);
				
				  fetch("${root}/board/modifyPage/"+articleNo,{
						method: 'POST',
						headers: {
						    "Content-Type": "application/json",
						},
						body: JSON.stringify({
							articleNo: articleNo,
							subject: subject,
							content: content
						})
					})
					.then(res => {
		    			if(res.status == 200){
		    				location.href = "${root}/board/modify/"+articleNo;
		    			}else{
		    				alert('비정상적인 접근입니다!');
		    			}
		    		})
			}

			document
					.querySelector("#btn-delete")
					.addEventListener(
							"click",
							function() {
								if (confirm("정말 삭제하시겠습니까?")) {
									const articleno = document.getElementById('articleno').value;
						      		
						      		fetch("${root}/board/delete/"+articleno,{
						    			method: 'DELETE',
						    			headers: {
						    			    "Content-Type": "application/json",
						    			}
						    		})
						    		.then(res => {
						    			if(res.status == 200){
						    				location.href = "${root}/board";
						    			}else{
						    				alert('비정상적인 접근입니다!');
						    			}
						    		})
								}
							});

			
		</script>
		</main>
		<%@ include file="../include/footer.jsp"%>
</body>
</html>
