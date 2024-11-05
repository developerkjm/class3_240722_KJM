<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="${pageContext.request.contextPath}/resources/css/default.css" rel="stylesheet" type="text/css">
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.7.1.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/member_join_form.js"></script>
</head>
<body>
	<!-- form 태그 실습과제 화면.png 실습 -->
	<header>
		<jsp:include page="/WEB-INF/views/inc/top.jsp"></jsp:include>
	</header>
	<article>
		<h1>회원가입</h1>
		<form action="MemberJoin" name="joinForm" method="post">
			<table border="1">
				<tr>
					<th>이름</th>
					<td><input type="text" name="name"></td>
				</tr>
				<tr>
					<th>ID</th>
					<td>
						<!-- ID중복확인 버튼 클릭 시 함수 호출하여 새 창으로 "check_id.jsp" 열기 -->
						<input type="text" name="id" id="id" placeholder="4 ~ 8글자 사이 입력" onblur="checkIdLength()">
						<input type="button" value="ID중복확인" onclick="checkId()">
						<div id="checkIdResult"></div>
					</td>
				</tr>
				<tr>
					<th>비밀번호</th>
					<td>
						<!-- 
						비밀번호 입력란에 "키를 눌렀다 뗄 때" 함수 호출하여 비밀번호 입력값 길이 체크
						=> 비밀번호 텍스트 길이가 8글자 미만이거나 16글자 초과하면
						   "8 ~ 16글자 사이 입력 필수!" 메세지를 div 영역에 출력(빨간색)
						=> 비밀번호 길이가 8 ~ 16글자 사이면
						   "사용 가능한 비밀번호!" 메세지를 div 영역에 출력(초록색)
						-->
						<input type="password" id="passwd" name="passwd" placeholder="8 ~ 16글자 사이 입력">
						<div id="checkPasswdResult"></div>
					</td>
				</tr>
				<tr>
					<th>비밀번호확인</th>
					<td>
						<!--
						비밀번호 확인란에서 "커서가 빠져나갈 때" 함수 호출하여
						비밀번호 란에서 입력한 비밀번호와 비밀번호 확인란의 비밀번호를 비교하여
						두 비밀번호가 같을 경우 "비밀번호 확인 완료!"(초록색) div 영역에 출력하고
						다를 경우 "비밀번호 다름!"(빨간색) div 영역에 출력
						-->
						<input type="password" id="passwd2">
						<div id="checkPasswd2Result"></div>
					</td>
				</tr>
				<tr>
					<th>주소</th>
					<td>
						<input type="text" id="postcode" name="post_code" size="6" readonly placeholder="우편번호">
						<input type="button" value="주소검색" onclick="search_address()"><br>
						<input type="text" id="address1" name="address1" size="25" readonly placeholder="기본주소"><br>
						<input type="text" id="address2" name="address2" size="25" placeholder="상세주소">
					</td>
				</tr>
				<tr>
					<th>E-Mail</th>
					<td>
						<input type="text" size="10" id="email1" name="email1">
							@<input type="text" size="10" id="email2" name="email2">
						<select id="emailDomain">
							<option value="">직접입력</option>
							<option value="naver.com">naver.com</option>
							<option value="nate.com">nate.com</option>
							<option value="gmail.com">gmail.com</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>직업</th>
					<td>
						<select name="job">
							<option value="">항목을 선택하세요</option>
							<option value="개발자">개발자</option>
							<option value="DB엔지니어">DB엔지니어</option>
							<option value="관리자">관리자</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>성별</th>
					<td>
						<input type="radio" name="gender" value="남">남
						<input type="radio" name="gender" value="여">여
					</td>
				</tr>
				<tr>
					<th>취미</th>
					<td>
						<input type="checkbox" id="hobby1" name="hobby" value="여행"><label for="hobby1">여행</label>
						<input type="checkbox" id="hobby2" name="hobby" value="독서"><label for="hobby2">독서</label>
						<input type="checkbox" id="hobby3" name="hobby" value="게임"><label for="hobby3">게임</label>
						<input type="checkbox" id="check_all"><label for="check_all">전체선택</label>
					</td>
				</tr>
				<tr>
					<th>가입동기</th>
					<td>
						<textarea rows="5" cols="40" id="motivation" name="motivation"></textarea>
					</td>
				</tr>
				<tr>
					<th>프로필 이미지</th>
					<td>
						<img src="${pageContext.request.contextPath}/resources/images/profile_default.png" id="prewview_profile"><br>
						<input type="file" name="profile_img" id="profile_img">
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<input type="submit" value="가입">
<!-- 						<input type="button" value="가입" id="btnSubmit"> -->
						<input type="reset" value="초기화">
						<!-- 11. 돌아가기 버튼 클릭 시 이벤트 처리를 통해 이전 페이지로 이동 처리 -->
						<input type="button" value="돌아가기" onclick="history.back()">
					</td>
				</tr>
			</table>
		</form>
	</article>
	<footer>
		<jsp:include page="/WEB-INF/views/inc/bottom.jsp"></jsp:include>
	</footer>
	
	
	<!-- ==================================================================== -->
	<!-- 카카오(다음) 우편번호 검색 API 서비스 활용하여 주소 검색하기 -->
	<!-- 웹사이트 주소 : https://postcode.map.daum.net/guide -->
	<!-- 카카오(다음) 에서 제공하는 우편번호 검색 스크립트 파일 로딩 필수! -->
	<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
	<script type="text/javascript">
		function search_address() {
		    new daum.Postcode({ 
		        oncomplete: function(data) {
		            console.log(data);
		            document.joinForm.postcode.value = data.zonecode;
		    		let address = data.address; // 기본주소 저장
		    		
		    		if(data.buildingName != "") { // 건물명 존재여부 판별
		    			address += " (" + data.buildingName + ")"; // 건물명 결합
		    		}
		    		
		    		// 기본주소 출력
		    		document.joinForm.address1.value = address;
		    		
		    		// 상세주소 입력 항목에 커서 요청
		    		document.joinForm.address2.focus(); 
		    		
		        }
		    }).open(); // 주소검색창 표시(새 창 열기)
		}
	</script>
</body>
</html>