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

		<div class="container">
			<form action="">
				<input type="hidden" name="action" value="modify" />
				<input type="hidden" name="isbn" id="articleno" value="${articleNo}"/>
				<div class="form-group">
					<label for="title">Title</label> 
					<input type="text" name="subject"
						class="form-control" placeholder="Enter title" id="title" value="${subject}">
				</div>
				<br>

				<div class="form-group">
					<label for="comment">Comments:</label>
					<textarea class="form-control" rows="5" id="comment" name="content">${content}</textarea>
				</div>
				<button type="submit" class="btn btn-secondary" id="btn-modify"
					style="float: right;">수정하기</button>
			</form>
			<br>
		</div>	
		<script>
		
		document.getElementById("btn-modify").addEventListener("click", function () {
			const title = document.getElementById('title').value;
			const comment = document.getElementById('comment').value;
			const articleno = document.getElementById('articleno').value;
			
			  fetch("${root}/board/modify/"+articleno, {
					method: 'PUT',
					headers: {
					    "Content-Type": "application/json",
					},
					body: JSON.stringify({
						subject: title,
						content: comment
					})
				})
				.then(res => {
					if(res.status == 200){
						location.href = "${root}/board";
					}else{
						alert('수정에 실패하였습니다!');
					}
				})
		});
		</script>
		</main>
</body>
</html>