<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../include/head.jsp"%>
<script>
		window.onload =  function () {
			  fetch("${root}/apts/ck",{
					method: 'GET',
					headers: {
					    "Content-Type": "application/json",
					}
				})
				.then((response) => response.json())
		        .then((data) => showCkList(data));
		};
		
		
		function showCkList(data){
			const table = document.getElementById("interest-table");
			for(var ck of data){
				const newRow = table.insertRow();
				const aptName = newRow.insertCell(0);
				const buildYear = newRow.insertCell(1);
				const dong = newRow.insertCell(2);
				const jibun = newRow.insertCell(3);
				const deleteBtn = newRow.insertCell(4);
				console.log(ck);
				aptName.innerText = ck.apartmentName;
				buildYear.innerText = ck.buildYear;
				dong.innerText = ck.dong;
				jibun.innerText = ck.jibun;
				deleteBtn.className = "text-center";
				deleteBtn.id = ck.aptCode;
				deleteBtn.innerHTML = `<a onClick="deleteRow(this.parentNode.id)"><img src="${root}/assets/img/icon/delete.png"></a>`;
			}
		}
		
		function deleteRow(aptCode){
			if (window.confirm("관심 매물에서 삭제하시겠습니까?")) {
				fetch("${root}/apts/ck/"+aptCode, {
					method: 'DELETE',
					headers: {
					    "Content-Type": "application/json",
					}
				}).then(res => {
					if(res.status == 200){
						location.href = "${root}/interest";
					}else{
						alert('비정상적 접근입니다!');
					}
				})
			}
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
							<!--관심지역 등록-->
							<div class="Interest-area-container page_item">
								<div>
									<div class="portfolio-info">
										<h3>관심매물 조회</h3>
										<table class="table table-hover" id="interest-table">
											<thead>
												<tr>
													<th>아파트 이름</th>
													<th>건축 년도</th>
													<th>지번</th>
													<th>법정동</th>
													<th style="display: none">aptNo</th>
													<th class="text-center">삭제</th>
												</tr>
											</thead>
											<tbody>
												<!-- 
												<c:forEach items="${requestScope.lists}" var="interest">
													<tr>
														<td>${interest.name}</td>
														<td>${interest.floor}</td>
														<td>${interest.area}</td>
														<td>${interest.dong}</td>
														<td>${interest.amount}</td>
														<th style="display: none">${interest.no}</th>
														<td class="text-center"><a
															href="${root}/interest?action=delete&aptNo=${interest.no}">
																<img alt="#" src="${root}/assets/img/icon/delete.png">
														</a></td>
													</tr>
												</c:forEach>
												-->
											</tbody>
										</table>
									</div>
								</div>
							</div>
							<!-- 
						<script type="text/javascript"
							src="//dapi.kakao.com/v2/maps/sdk.js?appkey=d272d2594ec93c9a9fde3053b9523c4c">
				      	</script>
				      	<script>
					   	  // 카카오 맵
					      let map_main_container = document.querySelector(".map_main_container");
					      map_main_container.setAttribute("style", "display: block;");
				
					      var mapContainer = document.getElementById("map"), // 지도를 표시할
					                                                            // div
					        mapOption = {
					          center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의
					                                                                // 중심좌표
					          level: 3, // 지도의 확대 레벨
					        };
					      // 지도를 표시할 div와 지도 옵션으로 지도를 생성합니다
					      var map = new kakao.maps.Map(mapContainer, mapOption);
				      	</script>
						<script src="${root}/assets/js/aptInfo.js"></script>
						 -->
						</div>
					</div>
			</section>
		</main>

		<!-- End Breadcrumbs -->
		<%@ include file="../include/footer.jsp"%>
</body>
</html>