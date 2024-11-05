let checkIdResult = false;
	let checkPasswdResult = false;
	let checkPasswd2Result = false;

	function checkId() {
		window.open("check_id.jsp", "id_check", "width=400, height=400");
	}
	
	function checkIdLength() {
		let id = $("#id").val();
		console.log(id + ", " + id.length);
		
		if(id.length >= 4 && id.length <= 8) { // 적합
// 			document.querySelector("#checkIdResult").innerText = "사용 가능";
// 			document.querySelector("#checkIdResult").style.color = "GREEN";
			$("#checkIdResult").text("사용 가능22");
			$("#checkIdResult").css("color", "green");
			
			// 체크 결과 저장을 위해 checkIdResult 변수에 true 저장
			checkIdResult = true;
		} else { // 부적합
// 			document.querySelector("#checkIdResult").innerText = "4~8글자만 사용 가능합니다";
// 			document.querySelector("#checkIdResult").style.color = "RED";
			$("#checkIdResult").text("4~8글자만 사용 가능합니다22");
			$("#checkIdResult").css("color", "RED");
			
			// 체크 결과 저장을 위해 checkIdResult 변수에 false 저장
			checkIdResult = false;
		}
	}
	// -----------------------------------------------------------------
	$(function() {
		$("#passwd").keyup(function() {
			let passwd = $("#passwd").val();
			
			if(passwd.length >= 8 && passwd.length <= 16) { // 적합
// 				document.querySelector("#checkPasswdResult").innerText = "사용 가능한 패스워드";
// 				document.querySelector("#checkPasswdResult").style.color = "BLUE";
				$("#checkPasswdResult").text("사용 가능한 패스워드");
				$("#checkPasswdResult").css("color", "BLUE");
				
				// 체크 결과 저장을 위해 checkPasswdResult 변수에 true 저장
				checkPasswdResult = true;
			} else { // 부적합
// 				document.querySelector("#checkPasswdResult").innerText = "사용 불가능한 패스워드";
// 				document.querySelector("#checkPasswdResult").style.color = "RED";
				$("#checkPasswdResult").text("사용 불가능한 패스워드");
				$("#checkPasswdResult").css("color", "RED");
				
				// 체크 결과 저장을 위해 checkPasswdResult 변수에 false 저장
				checkPasswdResult = false;
				
			}
			
			// 비밀번호가 변경되면 비밀번호확인 작업을 다시 수행해야한다!
			// 단, 비밀번호확인을 수행하는 함수가 익명함수일 때 호출이 불가능하므로
			// 익명함수 호출 대신 비밀번호확인 항목에 대한 keyup 이벤트를 강제로 발생시킬 수 있다!
			// => 이벤트 트리거(trigger)를 활용하여 특정 요소에 이벤트 발생을 강제로 제어 가능
			$("#passwd2").trigger("keyup");
			
		});
		// ----------------------------------------------
		$("#passwd2").keyup(function() {
			let passwd = $("#passwd").val();
			let passwd2 = $("#passwd2").val();
			
			if(passwd == passwd2) { // 적합
				$("#checkPasswd2Result").text("비밀번호 일치");
				$("#checkPasswd2Result").css("color", "BLUE");
				
				// 체크 결과 저장을 위해 checkPasswdResult 변수에 true 저장
				checkPasswdResult = true;
			} else { // 부적합
				$("#checkPasswd2Result").text("비밀번호 불일치");
				$("#checkPasswd2Result").css("color", "RED");
				
				// 체크 결과 저장을 위해 checkPasswdResult 변수에 false 저장
				checkPasswd2Result = false;
			}
		});
		//----------------------------------------------------------
		// 이메일 도메인 선택시 텍스트박스에 입력
		$("#emailDomain").change(function(){
			$("email2").val($("#emailDomain").val());
		});
		
		
		//----------------------------------------------------------
		$("#check_all").click(function () {
			/*
			each() 메서드
			- 지정한 대상(요소 또는 객체)에 대한 반복을 수행하는 메서드
			- 지정 가능한 대상 : 선택자, 객체(배열 등)
			- 기본 문법 : 1) $("선택자").each(function(index, item) {});
						  2) $.each(객체, function(index, item) {}); // 선택자가 없으니까 반복할 객체를 직접 줘야하는 것이다.
			  => index : 대상의 인덱스,  item(또는 elem) : 대상 요소(객체)
			*/
			// 전체선택을 제외한 나머지 체크박스에 대한 반복수행
			$("input[name=hobby]").each(function(index, item) { // 짝수일때 뭐하고 홀수일때 뭐하고 이런거 지정이 가능함.
// 				console.log(index);
// 				console.log(item); // 체크박스 태그가 뜬다.
				// item 파라미터에 해당 체크박스 요소 태그가 객체로 전달됨
				// 따라서,  jqery 문법으로 해당객체에 접근 가능
				// id 선택자 check_all의 체크상태에 따라 나머지 체크박스 체크상태변경
// 				if($("#check_all").prop("checked")){
// 					$(item).prop("checked", true);// 객체를 제이쿼리로 감싸서 ???
// 				} else {
// 					$(item).prop("checked", false);// 객체를 제이쿼리로 감싸서 ???
// 				}
				// 전체선택 체크박스 상태값을 각 체크박스 체크상태값으로 설정함.
				$(item).prop("checked", $("#check_all").prop("checked"));
			});
			// -----------------------
			// 만약, 선택자 요소 없이 자바스크립트 객체만으로 jQuery의 each() 메서드 호출 시
			let arr = ["홍길동", "이순신", "강감찬"]; // 배열생성
			for (let i = 0; i < arr.length; i++){
				console.log(arr[i]);
			}
			for (let i of arr){
				console.log(i);
			}
			
			
			// jquery의 each() 메서드
			//$뒤에 소괄호()가 붙은 이유는 요소를 지정하려고 했는데 없으면 요소가 없다는 말이다.
			// 향상된 for문이 더 코드가 짧기떄문에 사용할 그게 없지만 
			// 주 목적은 요소지정할때 쓰니까 그떄 쓰면 되고 이런것도 있다는 것만 아시오. 유지보수할때 쓰일수가 있으니까요. 
		
			$.each(arr, function(index, item) { // arr 객체(배열)에 대해 반복 수행
				console.log("제이쿼리 each 메서드"+index + ", " + item);
			})
		});
		
		$("#emailDomain").on("change", function() {
			$("#email2").val($("#emailDomain").val());
			if($("#emailDomain > option").eq(0).prop("selected")){
				$("#email2").focus();
				$("#email2").css("background", "white");
				$("#email2").prop("readonly", false);
			} else {
				$("#email2").css("background", "gray");
				$("#email2").prop("readonly", true);
			}
		});
		//--------------------------
		// 이미지 파일 업로드 시 해당 이미지 미리보기 표시
		// => change 이벤트 익명함수 파라미터에 이벤트 객체를 전달받도록 변수 event 선언
		$("#profile_img").on("change", function(event){ // 이벤트 라고 적어주면 자동으로 객체가 받아짐.
			// 업로드 되는 이미지 파일 정보 가져오기 
			let file = event.target.files[0]; // 우리는 파일 1개밖에 없으니 0번째만 가지고 와도됨. target안에 배열 형태로 들어있음
			
			// 자바스크립트의 FileReader 객체 생성(파일 읽기용)
			let reader = new FileReader();
			
			// FileReader 객체 로딩 완료 시 핸들링 - 익명함수 파라미터로 event 객체 받아오기
			reader.onload = function(event2) { // reader객체가 로딩이 완료되면 함수를 실행함.
				// 파일 로딩 완료되면 img 태그에 업로드 ㅏ일 이미지 미리보기로 표시(src 속성값 변경)
				// => 익명함수 파라미터로 전달받은 event2 변수에 해당 업로드 파일 정보가 전달되므로
				//	  해당 객체의 target.result 속성으로 파일에 접근 가능
				console.log("파일: " + event2.target.result);
				$("#prewview_profile").attr("src", event2.target.result);
			}
			// FileReader 객체를 사용하여 전달된 파일 정보를 통해 파일 읽어오기
			reader.readAsDataURL(file);
			
		});
		// form 태그의 submit 이벤트 핸들링
//		$("form").submit(function(){ // 이거 동작 안해서 버튼으로 바꿀거임.
//		$("#btnSubmit").click(function(){
		$("form").on("submit", function() {
			// 입력값 검증을 모두 수행했다고 가정
			// -----------------------------------------------------------
			// 단, email1과 email2 값을 결합하여 전송하기 위해 별도로 검증
			let email1 = $("#email1").val(); 
			let email2 = $("#email2").val();
			console.log(email1 + ", " + email2); 
			
			if(email1 =="") {
				alert("이메일 입력필수");
				$("#email1").focus();
				return false;
			} else if (email2 =="") {
				alert("이메일 입력필수");
				$("#email2").focus();
				return false;
			}
			
			// email1과 email2 값 입력 완료시
			// 폼내에 <input type="hidden" 태그를 활용하여 email 파라미터 설정
			// => 이때, 해당 value 속성값은 email1과 email2 값을 결합한 값 사용 (email1 + "@" + email2)
			// => form 태그 내부에 <input>태그 추가 => append, prepend 메서드 활용
			$("form").prepend("<input type='hidden' name='email' value='" + email1 + "@" + email2 + "'>");
			// true 값 리턴하거나생략 시 submit 동작 수행됨
//			$("form").submit(); 그래서 주석처리함
//			return false;
		});
//		document.querySelector("form").submit(function() {
//			console.log("확인");
//		});
				
	});