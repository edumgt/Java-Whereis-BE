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
						<li><a href="index.html">LIVE</a></li>
						<li>정보</li>
					</ol>
					<h2>공지사항</h2>
				</div>
			</section>
			<div class="col-md-5 offset-5">
				<form class="d-flex" id="form-search" action="">
					<div class="input-group input-group-sm">
						<input type="text" class="form-control" id="word" name="word"
							placeholder="글제목 입력" />
						<button id="btn-search" class="btn btn-dark" type="button">검색</button>
					</div>
				</form>
			</div>
			<section>
				<div class="container mt-3">
					<form action="${root}/board/write">
						<table class="table" id="board-table">
							<thead>
								<tr>
									<th>no</th>
									<th>분류</th>
									<th>제목</th>
									<th>작성일</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
						<button type="submit" class="btn btn-secondary"
							style="float: right;">글쓰기</button>
					</form>
				</div>
			</section>
		</main>
</body>
    <script>
    window.onload = function(){
  	  fetch("${root}/board/board")
  	  	.then((response) => response.json())
		.then((data) => {
			console.log(data);
			const table = document.getElementById("board-table");
			for(var board of data){
				const row = table.insertRow();
				//row.setAttribute("onclick","showDetail(\"board.articleNo\")");
				const articleNo = row.insertCell(0);
				const bullet = row.insertCell(1);
				const subject = row.insertCell(2);
				const registerTime = row.insertCell(3);
				articleNo.innerText = board.articleNo;
				bullet.innerText = board.bullet;
				//subject.innerText = board.subject;
				subject.id = board.articleNo;
				subject.name = board.subject;
				if(board.bullet == "공지"){
				subject.innerHTML = "<a style=\"font-weight: bold\" onclick=\"showDetail(this.parentNode.id)\">"+board.subject+"</a>";
				} else{
					subject.innerHTML = "<a onclick=\"showDetail(this.parentNode.id)\">"+board.subject+"</a>";
				}
				registerTime.innerText = board.registerTime;
				//console.log(board.subject);
				
			}
		});
    };
    
    document.getElementById("btn-search").addEventListener("click", function () {
    	const word = document.getElementById('word').value;
      		
      		fetch("${root}/board/search/"+word,{
    			method: 'GET',
    			headers: {
    			    "Content-Type": "application/json",
    			}
    		})
    		.then(res => {
    			if(res.status == 200){
    				location.href = "${root}/search/"+word;
    			}else{
    				alert('비정상적인 접근입니다!');
    			}
    		})
    	});
      	
      	function showDetail(articleNo){
      		var url = "${root}/board/"+articleNo;
      		console.log(url);
      		console.log(typeof articleNo);
      		fetch(url,{
    			method: 'GET',
    			headers: {
    			    "Content-Type": "application/json",
    			}
    		})
    		.then(res => {
    			if(res.status == 200){
    				location.href = "${root}/"+articleNo;
    			}else{
    				alert('비정상적인 접근입니다!');
    			}
    		})
      	}
      
    </script>
</html>