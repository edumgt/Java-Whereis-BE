<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../include/head.jsp"%>
<script>
		window.onload =  function () {
			  fetch("${root}/admin/",{
					method: 'GET',
					headers: {
					    "Content-Type": "application/json",
					}
				})
				.then((response) => response.json())
		        .then((data) => showUserList(data));
		};
		
		let user_auth_dict = {};
		function showUserList(data){
			const table = document.getElementById("interest-table");
			for(var user of data){
				const newRow = table.insertRow();
				const userName = newRow.insertCell(0);
				const userId = newRow.insertCell(1);
				const userPwd = newRow.insertCell(2);
				const email = newRow.insertCell(3);
				const manager = newRow.insertCell(4);
				const deleteBtn = newRow.insertCell(5);
				user_auth_dict[user.userId] = user.manager;
				userName.innerText = user.userName;
				userId.innerText = user.userId;
				userPwd.innerText = user.userPwd;
				email.innerText = user.emailId +"@"+ user.emailDomain;
				manager.id = user.userId;
				if(user.manager == "T"){
					manager.innerHTML = `<div class="form-check form-switch text-ceneter">
					  		<input class="form-check-input" type="checkbox" id="managerSwitch" onClick="grantManager(this.parentNode.parentNode.id)"checked>
					  		<label class="form-check-label" for="managerSwitch">권한 여부</label>
						</div>`;
				}else{
					manager.innerHTML = `<div class="form-check form-switch text-ceneter">
				  		<input class="form-check-input" type="checkbox" id="managerSwitch" onClick="grantManager(this.parentNode.parentNode.id)">
				  		<label class="form-check-label" for="managerSwitch">권한 여부</label>
					</div>`;
				}
				deleteBtn.className = "text-center";
				deleteBtn.id = user.userId;
				deleteBtn.innerHTML = `<a onClick="deleteRow(this.parentNode.id)"><img src="${root}/assets/img/icon/delete.png"></a>`;
			}
		}
		
		function deleteRow(userId){
			if (window.confirm("유저를 삭제하시겠습니까?")) {
				fetch("${root}/admin/"+userId, {
					method: 'DELETE',
					headers: {
					    "Content-Type": "application/json",
					}
				}).then(res => {
					if(res.status == 200){
						location.href = "${root}/adminPage";
					}else if(res.status == 401){
						alert('허용 되지 않은 권한입니다.')
					}else if(res.status == 400){
						alert('비정상적 접근입니다!');
					}
				})
			}
		}
		
		function grantManager(userId){
			fetch("${root}/admin/"+userId+"/"+user_auth_dict[userId], {
				method: 'PUT',
				headers: {
				    "Content-Type": "application/json",
				}
			}).then(res => {
				if(user_auth_dict[userId] == "F"){
					user_auth_dict[userId] = "T"
				}else{
					user_auth_dict[userId] = "F"
				}
				if(res.status == 401){
					alert('허용 되지 않은 권한입니다.')
				}else if(res.status == 400){
					alert('비정상적 접근입니다!');
				}
			})
			
		}
</script>

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
							<div class="Interest-area-container page_item">
								<div>
									<div class="portfolio-info">
										<h3>유저 조회</h3>
										<table class="table table-hover" id="interest-table">
											<thead>
												<tr>
													<th>이름</th>
													<th>아이디</th>
													<th>비밀 번호</th>
													<th>이메일</th>
													<th>관리자 권한</th>
													<th class="text-center">삭제</th>
												</tr>
											</thead>
											<tbody>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
			</section>
		</main>

		<!-- End Breadcrumbs -->
		<%@ include file="../include/footer.jsp"%>
</body>
</html>