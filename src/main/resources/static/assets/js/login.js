
//let isUseId = false;
//document.querySelector("#userId").addEventListener("keyup", function () {
//	 let userid = this.value;
//	 let resultDiv = document.querySelector("#idcheck-result");
//	 if(userid.length < 5 || userid.length > 16) {
//	     resultDiv.setAttribute("class", "mb-3 text-dark");
//	     resultDiv.textContent = "아이디는 6자 이상 16자 이하 입니다.";
//	     isUseId = false;
//	 }
//	 } else {
//	    // $는jsp꺼..
//	    fetch("${root}/user?act=idcheck&userid=" + userid)
//	        .then(response => response.text())
//	        .then(data =>{
//		            console.log(data);
//		         if(data == 0) { // data는 DB에 현재 아이디가 몇개 있는지 받은거임
//		           resultDiv.setAttribute("class", "mb-3 text-primary");
//		           resultDiv.textContent = userid + "는 사용할 수 있습니다.";
//		           isUseId = true;
//		         } else {
//		           resultDiv.setAttribute("class", "mb-3 text-danger");
//		             resultDiv.textContent = userid + "는 사용할 수 없습니다.";
//		             isUseId = false;
//		         }
//	        });
//	 }
//});
  
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
		fetch("${root}/users/regist",{
			method: 'POST',
			headers: {
			    "Content-Type": "application/json",
			},
			body: JSON.stringify({
				userName: userName.value,
				userId: userId.value,
				userPwd: userPwd.value,
				emailId: emailId.value,
				emailDomain: emailDomain
			})
		})
		.then(res => {
			if(res.status == 200){
				location.href = "${root}/login";
			}else{
				alert('아이디 혹은 비밀번호가 틀렸습니다!');
			}
		})
	}
});
