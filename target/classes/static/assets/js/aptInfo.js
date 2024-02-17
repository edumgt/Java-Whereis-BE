/**
 * 
 */
// /////////////////////// select box 설정 (지역, 매매기간)
// /////////////////////////
let date = new Date();
let apartList;
let ckList;

window.onload = function () {
  let yearEl = document.querySelector("#year");
  let yearOpt = `<option value="">매매년도선택</option>`;
  let year = date.getFullYear();
  for (let i = year; i > year - 20; i--) {
    yearOpt += `<option value="${i}">${i}년</option>`;
  }
  yearEl.innerHTML = yearOpt;

  // 브라우저가 열리면 시도정보 얻기.
  sendRequest("sido", "*00000000", "sido");
};

document.querySelector("#year").addEventListener("change", function () {
  let month = date.getMonth() + 1;
  let monthEl = document.querySelector("#month");
  let monthOpt = `<option value="">매매월선택</option>`;
  let yearSel = document.querySelector("#year");
  let m = yearSel[yearSel.selectedIndex].value == date.getFullYear() ? month : 13;
  for (let i = 1; i < m; i++) {
     monthOpt += `<option value="${i < 10 ? "0" + i : i}">${i}월</option>`;
  }
  monthEl.innerHTML = monthOpt;
});

// 시도가 바뀌면 구군정보 얻기.
document.querySelector("#sido").addEventListener("change", function () {
  if (this[this.selectedIndex].value) {
    let regcode = this[this.selectedIndex].value.substr(0, 2) + "*00000";
    sendRequest("gugun", regcode, "gugun");
  } else {
    initOption("gugun");
    initOption("dong");
  }
});

// 구군이 바뀌면 동정보 얻기.
document.querySelector("#gugun").addEventListener("change", function () {
  if (this[this.selectedIndex].value) {
    let regcode = this[this.selectedIndex].value.substr(0, 5) + "*";
    sendRequest("dong", regcode, "dong");
  } else {
    initOption("dong");
  }
});

function sendRequest(selid, regcode, action) {
 
      let url = "/live/dong-code/"+selid;
      let params = "regcode=" + regcode;
      if(selid != "sido"){
    	  
    	  url += "/"+params;
      }
//      console.log(selid + " " + regcode + " " + action + " " + `${url}?${params}`);
      fetch(`${url}`)
        .then((response) => response.json())
        .then((data) => addOption(selid, data));
    }

function addOption(selid, data) {
  let opt = ``;
  initOption(selid);
  switch (selid) {
    case "sido":
      opt += `<option value="">시도선택</option>`;
      data.regcodes.forEach(function (regcode) {
        opt += `<option value="${regcode.dongCode}">${regcode.sidoName}</option>`;
      });
      break;
    case "gugun":
      opt += `<option value="">구군선택</option>`;
      let name = "";
      data.regcodes.forEach(function (regcode) {
        opt += `<option value="${regcode.dongCode}">${regcode.gugunName}</option>`;
      });
      break;
    case "dong":
      opt += `<option value="">동선택</option>`;
      data.regcodes.forEach(function (regcode) {
        opt += `<option value="${regcode.dongCode}">${regcode.dongName}</option>`;
      });
  }
  document.querySelector(`#${selid}`).innerHTML = opt;
}

function initOption(selid) {
  let options = document.querySelector(`#${selid}`);
  options.length = 0;
}
//뒤로가기 버튼 이벤트 핸들링
document.querySelector("#back-btn").addEventListener("click",function(){
	document.querySelector("#apart").setAttribute("style", "display:;");
	document.querySelector("#apart-title").setAttribute("style", "display:;");
	//관심
	document.querySelector("#ck-btn").setAttribute("style", "display:;");
	document.querySelector("#detail").setAttribute("style", "display: none;");
	document.querySelector("#detail-title").setAttribute("style", "display: none;");
	document.querySelector("#back-btn").setAttribute("style", "display: none;");
	setMarkers();
})

//관심 확정 등록 버튼 이벤트 핸들링
document.querySelector("#ck-btn").addEventListener("click",function(){
	const url = "apts/ck";
	fetch(url,{
		method:"post",
		headers:{
			"Content-Type":"application/json"
				},
				body:JSON.stringify(ckList)
	})
})
// /////////////////////// 아파트 매매 정보 /////////////////////////
document.querySelector("#list-btn").addEventListener("click", function () {
  let url = "/live/apts";

  let dongSel = document.querySelector("#dong");
  let regCode = dongSel[dongSel.selectedIndex].value.substr(0,8);
  let yearSel = document.querySelector("#year");
  let year = yearSel[yearSel.selectedIndex].value;
  let monthSel = document.querySelector("#month");
  let month = monthSel[monthSel.selectedIndex].value;
  let queryParams = 
      "regcode=" + regCode 
      + "&year=" + year
      + "&month=" + month; 
 
  fetch(`${url}?${queryParams}`)
    .then((response) => response.json())
    .then((data) => {
        makeList(data);
//        registMarker(data);
    });

});

//지도 그리기
var container = document.getElementById("map");
var options = {
    center: new kakao.maps.LatLng(33.450701, 126.570667),
    level: 3,
};
var map = new kakao.maps.Map(container, options);

function setCenter(lat,lng) {            
    // 이동할 위도 경도 위치를 생성합니다
    var moveLatLon = new kakao.maps.LatLng(parseFloat(lat), parseFloat(lng));
    
    // 지도 중심을 이동 시킵니다
    map.setCenter(moveLatLon);
}

function registMarker(data){

    var positions = [];
    data.regcodes.forEach(function (regcode) {
        var position = {
                title: regcode.apartmentName,
                latlng: new kakao.maps.LatLng(regcode.lat, regcode.lng)
        };
        positions.push(position);
    });

    console.log(positions);

	// 마커 이미지의 이미지 주소입니다
	var imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png"; 
	    
	for (var i = 0; i < positions.length; i ++) {
	    
	    // 마커 이미지의 이미지 크기 입니다
	    var imageSize = new kakao.maps.Size(24, 35); 
	    
	    // 마커 이미지를 생성합니다
	    var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize); 
	    
	    // 마커를 생성합니다
	    var marker = new kakao.maps.Marker({
	        map: map, // 마커를 표시할 지도
	        position: positions[i].latlng, // 마커를 표시할 위치
	        title : positions[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
	        image : markerImage // 마커 이미지
	    });
	}
	
	// 이동할 위도 경도 위치를 생성합니다
	var moveLatLon = positions[0].latlng;
	
	// 지도 중심을 부드럽게 이동시킵니다
	// 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
	map.panTo(moveLatLon);    
}


function makeList(data) {
  document.querySelector("table").setAttribute("style", "display: ;");
//  let tbody = document.querySelector("#aptlist");
  apartList = []
  //관심
  ckList = data.aptcode;
  for(let home of data.homes){
  	let aptCode = home.aptCode;
  	let apartName = home.apartmentName; // 아파트 이름
  	let apartBuildYear = home.buildYear;
  	let apartDong = home.dong; // 법적동
  	let apartJibun = home.jibun; // 지번
  	let apartLat = home.lat; // 위도
  	let apartLng = home.lng; // 경도
  	for(let hdeal of home.houseDeals){
  		let apartSize = hdeal.area; // 면적
  		let apartFloor = hdeal.floor; // 층
  		let apartValue = hdeal.dealAmount; // 가격
  	  	// 아파트 / 법적동 / 지번 / 면적/ 층 / 건축년도 / 가격 / 위도 / 경도
  		apartList.push(apartName+"/"+apartDong + "/" + apartJibun + "/" + apartSize + "/" + apartFloor + "/" + apartBuildYear + "/" +apartValue + "/"+apartLat+"/"+apartLng+"/"+aptCode)
  	}
  }
  //관심
  console.log(ckList)
  // 매물 데이터가 없으면 알람 처리
  if(data.empty == 'F'){
  	setMarkers();
  }else{
  	alert("해당 기간의 매물 정보가 없습니다!")
  }
//  initTable();
//  data.regcodes.forEach(function (regcode) {
//    let tr = document.createElement("tr");
//    
//    let interestTd = document.createElement("td");
//    let interestImg = document.createElement("img");
//	interestImg.setAttribute("src", "/live/assets/img/icon/star_off.png");
//	interestTd.appendChild(interestImg);
//	tr.appendChild(interestTd);
//	
//	interestImg.addEventListener("click", function() {
//		const url = "/live/interest";
//	    let params = "no=" + regcode.no
//	    			 + "&action=register";
//
//		fetch(`${url}?${params}`)
//	    .then((response) => response.json())
//	    .then((data) => {
//	        console.log("성공");
//	    });
//		
//		interestImg.setAttribute("src", "/live/assets/img/icon/star_on.png");
//	});
//	
//	let nameTd = document.createElement("td");
//	nameTd.appendChild(document.createTextNode(regcode.apartmentName));
//	tr.appendChild(nameTd);
//	
//	let floorTd = document.createElement("td");
//	floorTd.appendChild(document.createTextNode(regcode.floor));
//	tr.appendChild(floorTd);
//	
//	let areaTd = document.createElement("td");
//	areaTd.appendChild(document.createTextNode(regcode.area));
//	tr.appendChild(areaTd);
//	
//	let dongTd = document.createElement("td");
//	dongTd.appendChild(document.createTextNode(regcode.dong));;
//	tr.appendChild(dongTd);
//	
//	let priceTd = document.createElement("td");
//	priceTd.appendChild(document.createTextNode(regcode.dealAmount));
//	priceTd.classList.add("text-end");
//	tr.appendChild(priceTd);
//	
//	let noTd = document.createElement("td");
//	noTd.appendChild(document.createTextNode(regcode.no));
//	noTd.setAttribute("style", "display: none;");
//	tr.appendChild(noTd);
//
//	tbody.appendChild(tr);
//  });
}

//function initTable() {
//  let tbody = document.querySelector("#aptlist");
//  let len = tbody.rows.length;
//  for (let i = len - 1; i >= 0; i--) {
//    tbody.deleteRow(i);
//  }
//}
var markers = [];
let apartDict;
function setMarkers() { // 주소를 찾아 마커와 가격 표시하는 함수
	apartDict={};
    document.querySelector("table").setAttribute("style", "display: ;");
    document.querySelector("#apart-title").setAttribute("style", "display: ;");
    //관심
    document.querySelector("#ck-btn").setAttribute("style", "display: ;");
    let aptTbody = document.querySelector("#aptlist");
    let detailTbody = document.querySelector("#detail-list");
    initTable1();
    removeMarker();
    let fragment1 = document.createDocumentFragment();
		let fragment2 = document.createDocumentFragment();
    let bounds = new kakao.maps.LatLngBounds();
    var infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });

    for (let i = 0; i < apartList.length; i++) {
    	// 아파트 / 법적동 / 지번 / 면적/ 층 / 건축년도 / 가격 / 위도 / 경도 /아파트코드
    	
        let name = apartList[i].split("/")[0]; // 아파트 이름
        let dong = apartList[i].split("/")[1]; // 법적동
        let jibun = apartList[i].split("/")[2]; // 지번
        let size = apartList[i].split("/")[3]; // 면적
        let floor = apartList[i].split("/")[4]; // 층
        let buildYear = apartList[i].split("/")[5]; // 건축 년도
        let value = apartList[i].split("/")[6]; // 가격
        let lat = apartList[i].split("/")[7]; // 위도
        let lng = apartList[i].split("/")[8]; // 경도
        let aptCode = apartList[i].split("/")[9]; // 아파트 코드
        if(name in apartDict){
    		apartDict[name].push({'dong':dong,'jibun':jibun,'size' : size,'floor':floor,'buildYear':buildYear,'value': value,'latlng':[lat,lng]})
    	}else{
    		apartDict[name] = [{'dong':dong,'jibun':jibun,'size' : size,'floor':floor,'buildYear':buildYear,'value': value,'latlng':[lat,lng]}]
    		let itemE1 = getListItem1(name, dong, jibun,aptCode);
            	var coords = new kakao.maps.LatLng(parseFloat(lat),parseFloat(lng));
            	marker = new kakao.maps.Marker({
            		map: map,
            		position: coords
            	});
            	markers.push(marker);
            	bounds.extend(coords);
            	
            	(function (marker){
            		kakao.maps.event.addListener(marker, 'mouseover', function () {
                        var content = '<div style="padding:5px;z-index:1;">' + value + '</div>';
                        infowindow.setContent(content);
                        infowindow.open(map, marker);
                    });

                    // 마커에 mouseout 이벤트를 등록하고 마우스 아웃 시 인포윈도우를 닫습니다
                    kakao.maps.event.addListener(marker, 'mouseout', function () {
                        infowindow.close();
                    });

                    itemE1.onmouseover = function () { // 테이블 열에
														// 마우스 올렸을때
														// 가격 출력
                        var content = '<div style="padding:5px;z-index:1;">' + value + '</div>';
                        infowindow.setContent(content);
                        infowindow.open(map, marker);
                    };
                    // 테이블 열에 마우스 때면 가격 출력표 없애기
                    itemE1.onmouseout = function () {
                        infowindow.close();
                    };
                    // 테이블열 클릭시 부드럽게 이동
                    itemE1.firstChild.onclick = function () {
                    	// 컬럼 클릭시 없애고 상세 정보 출력
                    	document.querySelector("#apart").setAttribute("style", "display:none;");
                    	document.querySelector("#apart-title").setAttribute("style", "display:none;");
                    	//관심 지역
                    	document.querySelector("#ck-btn").setAttribute("style", "display:none;");
                    	document.querySelector("#detail").setAttribute("style", "display:;");
                    	document.querySelector("#detail-title").setAttribute("style", "display: ;");
                    	document.querySelector("#back-btn").setAttribute("style", "display: ;");
                    	detailMarkers(name);
                    }
                    itemE1.lastChild.onclick = function(){
                    	if(itemE1.lastChild.innerHTML == '<img src="/live/assets/img/star-filled.svg">'){ // 관심 아파트 해제
                    		ckList = ckList.filter(function(item){
                    			return item !== aptCode;
                    		});
                        	itemE1.lastChild.innerHTML = '<img src="/live/assets/img/star.svg"/>'                                		
                    	}else{ // 관심 아파트 등록
                    		ckList.push(aptCode);
                        	itemE1.lastChild.innerHTML = '<img src="/live/assets/img/star-filled.svg"/>'                                		
                    	}
                    	console.log(ckList);
                    }
            	})(marker);
            	fragment1.appendChild(itemE1);
            }
            aptTbody.appendChild(fragment1);
            map.setBounds(bounds);
    	}       
}

function detailMarkers(name){
    let detailTbody = document.querySelector("#detail-list");
    removeMarker();
    initTable2();
		let fragment2 = document.createDocumentFragment();
    let bounds = new kakao.maps.LatLngBounds();
    var infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });
    for (let aptName in apartDict){
    	if( name !== aptName) continue;
    	for(let dealInfo of apartDict[aptName]){
    		let itemE2 = getListItem2(aptName,dealInfo['buildYear'],dealInfo['floor'],dealInfo['size'],dealInfo['value']);
    		let value = dealInfo['value'];
    		let lat = dealInfo['latlng'][0];
    		let lng = dealInfo['latlng'][1];
    		var coords = new kakao.maps.LatLng(parseFloat(lat),parseFloat(lng));
        	marker = new kakao.maps.Marker({
        		map: map,
        		position: coords
        	});
        	markers.push(marker);
        	bounds.extend(coords);
        	
        	(function (marker,value){
        		kakao.maps.event.addListener(marker, 'mouseover', function () {
                    var content = '<div style="padding:5px;z-index:1;">' + value + '</div>';
                    infowindow.setContent(content);
                    infowindow.open(map, marker);
                });

                // 마커에 mouseout 이벤트를 등록하고 마우스 아웃 시 인포윈도우를 닫습니다
                kakao.maps.event.addListener(marker, 'mouseout', function () {
                    infowindow.close();
                });

                itemE2.onmouseover = function () { // 테이블 열에
													// 마우스 올렸을때
													// 가격 출력
                    var content = '<div style="padding:5px;z-index:1;">' + value + '</div>';
                    infowindow.setContent(content);
                    infowindow.open(map, marker);
                };
                // 테이블 열에 마우스 때면 가격 출력표 없애기
                itemE2.onmouseout = function () {
                    infowindow.close();
                };
                // 테이블열 클릭시 부드럽게 이동
                itemE2.onclick = function () {
                    map.panTo(coords);
                }
        	})(marker,value);
        	fragment2.appendChild(itemE2);
        }
        detailTbody.appendChild(fragment2);
        map.setBounds(bounds);
	}                     	
}

function initTable1() {
    let tbody = document.querySelector("#aptlist");
    let len = tbody.rows.length;
    for (let i = len - 1; i >= 0; i--) {
        tbody.deleteRow(i);
    }
}
function initTable2() {
    let tbody = document.querySelector("#detail-list");
    let len = tbody.rows.length;
    for (let i = len - 1; i >= 0; i--) {
        tbody.deleteRow(i);
    }
}
function getListItem1(name, dong, jibun,aptCode) {
    var tr = document.createElement("tr");
    itemStr = '<td class="item1Name">' + name + "</td>" +
        "<td>" + dong + "</td>" +
        "<td>" + jibun + "</td>"
    console.log(aptCode,ckList);
    //관심
    if(ckList.includes(aptCode)){
    	itemCk = '<td class ="text-end">'+ '<img src="/live/assets/img/star-filled.svg"/>'+"</td>"
    }else{
    	itemCk = '<td class ="text-end">'+ '<img src="/live/assets/img/star.svg"/>'+"</td>"
    }
    tr.innerHTML = itemStr+itemCk;
    tr.className = 'item1';

    return tr;

}
function getListItem2(name, buildYear, floor, size, value) {
    var tr = document.createElement("tr");
    itemStr = "<td>" + name + "</td>" +
    	"<td>" + buildYear + "</td>" +
        "<td>" + floor + "</td>" +
        "<td>" + size + "</td>" +
        '<td class ="text-end">' + value + "</td>"
    tr.innerHTML = itemStr;
    tr.className = 'item2';
    return tr;

}
// 지도 위에 표시되고 있는 마커를 모두 제거합니다
function removeMarker() {
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(null);
    }
    markers = [];
}
// 인포윈도우를 표시하는 클로저를 만드는 함수입니다
function makeOverListener(map, marker, infowindow) {
    return function () {
        infowindow.open(map, marker);
    };
}

// 인포윈도우를 닫는 클로저를 만드는 함수입니다
function makeOutListener(infowindow) {
    return function () {
        infowindow.close();
    };
}