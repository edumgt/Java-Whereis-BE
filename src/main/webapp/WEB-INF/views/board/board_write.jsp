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
			<form action="" style="margin: 20px 0">
				<input type="hidden" name="action" value="write" />
				
				<select style="display: inline; width: auto;" name="bullet" id="bullet"
					class="form-select form-select-sm" aria-label=".form-select-sm example">
				  <option selected>글머리</option>
				  <option value="일반">일반</option>
				  <c:if test="${userInfo.manager eq 'T'}">
				  	<option value="공지">공지사항</option>
				  </c:if>
				</select>
								
				
				<div class="form-group">
					<label for="title">Title</label> <input type="text" name="subject"
						class="form-control" placeholder="Enter title" id="title">
				</div>
				<br>

				<div class="form-group">
					<label for="comment">Comments:</label>
					<textarea class="form-control" rows="5" id="comment" name="content"></textarea>
				</div>
				<button type="submit" class="btn btn-secondary"
					style="float: right; margin: 15px 0" id="btn-write">글쓰기</button>
			</form>
			<br>
			<script>
			document.querySelector("#btn-write").addEventListener("click", function() {
				
				fetch("${root}/board/write",{
					method: 'POST',
					headers: {
					    "Content-Type": "application/json",
					},
					body: JSON.stringify({
						bullet: bullet.value,
						subject: title.value,
						content: comment.value
					})
				})
				.then(res => {
					if(res.status == 200){
						location.href = "${root}/board";
					}else{
						alert('잘못된 접근입니다!');
					}
				})
			});
			</script>
		</div>	
		</main>
</body>
</html>