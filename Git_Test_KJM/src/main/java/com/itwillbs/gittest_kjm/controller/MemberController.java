package com.itwillbs.gittest_kjm.controller;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.gittest_kjm.VO.MemberVO;
import com.itwillbs.gittest_kjm.service.MemberService;

@Controller
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	@GetMapping("MemberJoin")
	public String joinForm() {
		return "member/member_join_form";
	}
	
	// "MemberJoin" 매핑 - POST
	// => 전송되는 폼파라미터 데이터는 MemberVO 타입 객체에 저장하도록 파라미터 타입 MemberVO 선언
	@PostMapping("MemberJoin")
	public String join(MemberVO member, Model model, BCryptPasswordEncoder passwordEncoder) {
//		System.out.println(member);
		
		/*
		 * [ BCryptPasswordEncoder 클래스를 활용한 패스워드 단방향 암호화 ]
		 * - org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder 클래스 활용
		 *   => spring-security-web 또는 spring-security-crypto 라이브러리 필요
		 *   
		 */
		// 1. BCryptPasswordEncoder 클래스 인스턴스 생성(기본 생성자 활용) - DI 활용 - 한번만 쓸거니까 파라미터에 집어넣어
//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		// 2. BCryptPasswordEncoder 객체의 encode() 메서드 호출하여
		//    평문(원본) 패스워드에 대한 해싱 수행 후 결과값을 문자열로 리턴받아 저장
		//    => 파라미터 : 평문 암호(MemberVO 객체의 passwd 멤버변수값)
		//    => 리턴타입 : String(암호문)
		String securePasswd = passwordEncoder.encode(member.getPasswd());
		System.out.println("평문 : " + member.getPasswd()); // 1234
		System.out.println("암호문 : " + securePasswd);  
		// => 단, 매번 생성되는 암호문은 솔팅에 의해 (Salt 값)에 의해 매번 달라진다.
		// 3. 암호화 된 패스워드를 다시 MemberVo 객체의 passwd 값에 저장(덮어쓰기)
		member.setPasswd(securePasswd);
		
		
		// MemberService - registMember() 메서드 호출하여 회원가입 작업 요청
		// => 파라미터 : MemberVO 객체, 리턴타입 : int(insertCount)
		int insertCount = memberService.registMember(member);
		
		
		// MemberMapper - insertMember() 메서드 
		// --------------------------------------------------------------------
		// 회원가입 결과 판별하여 페이징 처리
		// 성공시 : MemberJoinSuccess 서블릿 주소 리다이렉트
		if (insertCount > 0) {
			return "redirect:/MemberJoinSuccess";
		} else {
			// 실패 시 : result/fail.jsp 페이지 포워딩("msg" 속성값으로 "회원가입 ㅅ ㅣㄹ패! 전달)
			model.addAttribute("msg", "회원가입 실패");
			return "result/fail";
		}
	}
	
	// "MemberJoinSuccess" 매핑 - GET
	@GetMapping("MemberJoinSuccess")
	public String joinSuccess() {
		return "member/member_join_success";
	}
	
	// ====================================================================
	// "MemberLogin" 매핑 - GET
	// => 로그인 폼 뷰페이지 포워딩
	@GetMapping("MemberLogin")
	public String memberLoginForm() {
		return "member/member_login_form";
	}
	// "MemberLogin" 매핑 - GET
	@PostMapping("MemberLogin")
	// => 로그인 폼 파라미터 저장용 MemberVO, 아이디기억하기 값을 저장할 파라미터 rememberId
	//	  암호화 처리를 위한 BCryptPasswordEncoder 타입 파라미터,
	//	  세션 정보를 관리하는 HttpSession, 응답 페이지로 데이터 전송할 Model 타입 파라미터 필요
	public String memberLoginForm(MemberVO member, String rememberId, 
						BCryptPasswordEncoder passwordEncoder,HttpSession session, Model model) { 
		System.out.println("memberVO: " + member + ", rememberId : " + rememberId);
		
		// dkdlel qlalfqjsgh akwsmswl qlrygkfuaus 아이디 비번이 기존거랑 맞는지 비교하려면 기존꺼에 덮어쓰면 안됨. 
		
		// [ BCryptPasswordEncoder 클래스를 활용하여 암호화 된 패스워드 비교 작업을 위한 패스워드 조회 ]
		// => 기존 MemberVO 객체에 조회결과 덮어써도 되지만, 기존 패스워드 유지위해 별도의 변수 사용.
		// MemberService - getMember() 메서드 호출하여 회원 상세정보 조회요청(회원상세정보 재사용) -> 얘는 retrun을 membverVO로 받을 것임.
		// => 파라미터 MemberVO 객체(아이디 필요), 리턴타입 : membverVO
		MemberVO dbMember = memberService.getMember(member);
		//-------------------------------------------------------- 또는
		// MemberService - getMemberPasswd() 메서드 호출하여 회원 아이디에 대한 패스워드 조회
		// => 파라미터 : MemberVO 객체(아이디 필요), 리턴타입 : (map or memverVO or String 중에 선택가능) String(dbPasswd)
		// -> 저장하는 과정에서 단방향 암호화 해놔서 평문으로 비교하는 거 의미가 없어서 MemberVO꺼내서 주자.
//		String dbPasswd = memberService.getMemberPasswd(member);
		String dbPasswd = memberService.getMemberPasswd(member.getId());
		System.out.println("DB에 저장된 패스워드 : " + dbPasswd);
		System.out.println("입력된 패스워드 : " + member.getPasswd());
		//--> 일단 db에서 비밀ㄹ번호를 가지고 오기만 함. 
		
		
		/*
		 * [ BCryptPasswordEncoder 객체를 활용한 패스워드 비교 ]
		 * - 입력받은 패스워드(= 평문)와 DB에 저장된 패스워드(= 암호문) 간의 
		 *   직접적인 문자열 비교 시 무조건 두 문자열은 다름
		 * - 일반적인 해싱의 경우 새 패스워드도 해싱을 통해 암호문으로 변환하여 비교하면 되지만
		 *   현재, BCryptPasswordEncoder 객체를 통해 기존 패스워드를 암호화했기 때문에
		 *   솔팅값에 의해 두 암호는 서로 다른 문자열이 되어 
		 *   DB 에서 WHERE 절로 두 패스워드 비교 또는 String 클래스의 equals() 로 비교가 불가능하다!
		 * - BCryptPasswordEncoder 객체의 matches() 메서드를 활용하여 비교 필수!
		 *   (내부적으로 암호문으로부터 솔팅값을 추출하여 평문을 암호화하여 비교)
		 * 
		 * < 기본 문법 >
		 * 객체명.matches(평문, 암호문) 호출 시 boolean 타입 결과 리턴
		 * ------------------------------------------------------------------------
		 * 검색 아이디가 존재하지 않을 경우 리턴되는 결과값 : null
		 * => null일 경우 (=아이디 없음) 또는 패스워드가 일치하지 않을 경우
		 * 		"result/fail.jsp" 펭지 포워딩(전달 메세지 : "로그인 실패")
		 */
		
		
		// 회원상세정보를조회했을 경우
//		if(dbMember.getPasswd() == null || !passwordEncoder.matches(member.getPasswd(), dbMember.getPasswd())) {
		if(dbMember == null || !passwordEncoder.matches(member.getPasswd(), dbMember.getPasswd())) {
			model.addAttribute("msg", "로그인 실패!");
			return "result/fail";
		} else if(dbMember.getMember_status() == 3 ) {// 로그인 성공이지만, 탙퇴 회원일 경우		
		
			model.addAttribute("msg", "탈퇴한 회원입니다. !");
			return "result/fail";
		
		} else { // 로그인 성공
			// 로그인 성공아이디를 세션에 저장(속성명 "sId":
			session.setAttribute("sId", member.getId());
			
			// 만약 이게 금융사이트라면 시간제한이 있음. 타이머를 걸어보자.
			// 세션 타이머 1시간으로 변경(클라이언트로 부터 1시간동안 요청-스크롤 같은거 제외-이 없을 경우 세션 제거됨)
			session.setMaxInactiveInterval(60 * 60); // (60초 * 60분 = 1시간)
			
			
			// ---------------------------------------------------------------
			// 임시) 메인페이지(컨텍스트 루트)로 리다이렉트
//			return "redirect:/"; // jsp에서 location.href="./" 와 같다.
			// [특정 페이지 로그인 필수 처리를 ㅜ이한 로그인 완료시 원래 페이지로 이동 처리 ]
			// - 세션 객체에 저장된 "prevURL" 속성이 null이 아닐 경우 (null은 이전페이지가 없다. - 주소창에 넣는경우. setAttribute는 값이 없으면 null 토해냄??????????)
			//	 해당 주소로 리다이렉트 하고
			//	 null 일경우 (로그인 링크 눌렀을때) 기존과 동일하게 메인 페이지로 리다이렉트
			System.out.println("prevURL : " + session.getAttribute("prevURL"));
			if(session.getAttribute("prevURL") == null) {
				return "redirect:/";
			} else {
//				return "redirect:/" + session.getAttribute("prevURL"); // 24.10.29. 아래코드로 변경
				// 24.10.29. 아래코드로 변경
				// request.getServletPath() 메서드를 통해 이전 요청 URL 을 저장할 경우
				// "/요청URL" 형식으로 저장되므로 redirect:/ 에서 / 제외하고 결합하여 사용
				return "redirect:" + session.getAttribute("prevURL");
			}
		}
	}
	
	@GetMapping("MemberLogout")
	public String logout(HttpSession session) {
		// 세션 초기화
		session.invalidate();
		
		// 메인페이지로 리다이렉트
		return "redirect:/";
	}
	
	// ======================================================================
		// 회원 상세정보 조회("MemberInfo" - GET)
		@GetMapping("MemberInfo")
		public String memberInfo(MemberVO member, HttpSession session, Model model, HttpServletRequest request) {
			// 세션 아이디 체크하여 세션 아이디가 없을 경우 "result/fail" 페이지 포워딩 처리
			// => "msg" 속성값 : "로그인 필수!"
			// => "targetURL" 속성값 : "MemberLogin" (로그인페이지 URL)
			String id = (String)session.getAttribute("sId");
			if(id == null) {
				model.addAttribute("msg", "로그인 필수!\\n로그인 페이지로 이동합니다.");
				model.addAttribute("targetURL", "MemberLogin");
				
				//--------------------------------------------------------------------
				// 로그인 완료 후 다시 회원 상세정보 조회 페이지로 이동할 수 있도록 
				// 세션 객체에 회원 상세정보 조회 페이지의 서블릿 주소를 저장 후 
				// 로그인 완료 시 해당 주소로 리다이렉트 수행할 수 있다. 
				session.setAttribute("prevURL", "MemberInfo");
				return "result/fail";
			}
			// -------------------------------------------------------------------------
			// MemberService - getMember() 메서드 호출하여 회원 상세정보 조회
			// => 파라미터 : MemberVO 객체(아이디만 전달해도 무관함)   리턴타입 : MemberVO
			// => 이 때, 아이디 파라미터가 없으므로 세션 아이디 값을 MemberVO 객체에 저장
			// => 조회 결과 저장 시 새로운 MemberVO 타입 변수 선언 대신 기존 변수 member 재사용 가능
			//    (기존 MemberVO 객체와 별개로 구별해야할 정보가 없기 때문에 덮어써도 무관함)
			member.setId(id);
			member = memberService.getMember(member);
			
			// Model 객체에 MemberVO 객체 저장 후 member/member_info.jsp 페이지 포워딩
			model.addAttribute("member", member);
			
			return "member/member_info";
		}
		/* 비밀번호 틀렸을 때 500 에러 남!!
		// 회원정보수정 ("MemberModify" - POST)
		// => 회원정보 수정폼 없이 회원 상세정보 페이지에서 바로 수정 페이지 요청함.
		@PostMapping("MemberModify")
		
		// Map<String, String> map 이렇게 하면 파라미터를 못받는디????????
		public String modify(@RequestParam Map<String, String> map, MemberVO member, BCryptPasswordEncoder passwordEncoder, HttpSession session, Model model) { // 다른 방법인 Map 사용해보자! MemberVO는 비교하려고 일단 넣음. 사용은 안할거임
		// public String modify(MemberVO member, String oldPasswd,  HttpSession session, Model model) { // Model은 비번 틀렸을때, session은 관리자일때는 쓰면안됨. 왜냐면 내정보만 바꾸기 때문에 그래서 파라미터(member)와 세션을 다 가져온 것이다.
			
			// 만약 파라미터 매핑용 Map타입 선언시 @RequestParam이 필수!
			// 만약, Map 타입 파라미터와 MemberVO 타입 파라미터를 동시에 선언 시 두 객체 모두 파라미터가 저장됨
			// => 단, Map 객체에는 기존 패스워드(oldPasswd) 파라미터도 저장됨(MemberVO 에는 없음)
//			System.out.println("map : " + map);
//			System.out.println("member : " + member);
			
			// 세션 아이디 체크하여 세션 아이디가 없을 경우 "result/fail" 페이지 포워딩 처리
			// => "msg" 속성값 : "로그인 필수!"
			// => "targetURL" 속성값 : "MemberLogin" (로그인페이지 URL)
			String id = (String)session.getAttribute("sId");
			if(id == null) {
				// 세션아이디 왜 또체크하노. 상세페이지 들어올때 로그인을 체크하는데... 
				// 세션 타이머 걸어놨는데 수정되면 안되니까 다시 한번 더 비교한다. 
				// CORS : 서버주소와 들어오는 주소가 다르면 튕겨낸다?????????????????????
				
				model.addAttribute("msg", "로그인 필수!\\n로그인 페이지로 이동합니다.");
				model.addAttribute("targetURL", "MemberLogin");
				return "result/fail";
			}
			//---------------------------------------------------------------------------------
			// MemberVo 객체에 세션 아이디 덮어쓰기
			member.setId(id);
			
			// 세션이 꼬여서 타인의 정보가 보일떄가 아주 가끔있다. 2차로 비밀번호를 확인해야함.
			// MemberService - getMemberPasswd(매서드 재사용하여 암호화 된 패스워드 조회 
			// => 파라미터: MemberVO 객체 , 리턴타입 : String(dbPasswd)
			String dbPasswd = memberService.getMemberPasswd(member);
			
			// 조회된 비밀번호와 입력받은 기존 비밀번호를 비교(Map 객체의 oldPasswd 키 사용)
			if(dbPasswd == null || !passwordEncoder.matches(map.get("oldPasswd"), dbPasswd)) { // 아이디가 불일치 또는  패스워드 불일치 
				model.addAttribute("msg", "수정권한이 없습니다!");
				return "result/fail";
			}
			
			
			// 수정된 정보 확인을 위해 회원 상세정보 페이지로 리다이렉트
			return "redirect:/MemberInfo";
		}
		*/
		
		// 회원 정보 수정("MemberModify" - POST)
		@PostMapping("MemberModify")
		public String modify(@RequestParam Map<String, String> map, BCryptPasswordEncoder passwordEncoder,  
				String[] hobby, HttpSession session, Model model) {
			String id = (String)session.getAttribute("sId");
			if(id == null) {
				model.addAttribute("msg", "로그인 필수!\\n로그인 페이지로 이동합니다.");
				model.addAttribute("targetURL", "MemberLogin");
				return "result/fail";
			}
			map.put("id", id);
			String dbPasswd = memberService.getMemberPasswd(id);
			if(dbPasswd == null || !passwordEncoder.matches(map.get("oldPasswd"), dbPasswd)) { // 아이디 불일치 또는 패스워드 불일치
				model.addAttribute("msg", "수정 권한이 없습니다!");
				return "result/fail";
			}
			
			//------------------------------
			//if(map.get("passwd") != "") { // 이렇게 하면 문자열 비교가 안됨!!!
			if(!map.get("passwd").equals("")){
				map.put("passwd", passwordEncoder.encode(map.get("passwd")));
				System.out.println("암호화 된 새 비밀번호 : " + map.get("passwd"));
			}
			
			// --------------------------------
			// Map 타입으로 파라미터 처리시 동일한 파라미터명이 존재할 경우(ex. 체크박스)
			// 첫번째 파라미터만 Map객체에 저장이 되는 문제가 발생함.
			// => MemberVO 객체 사용시 문자열로 자동으로 결합됨
			// => 또는 , String[] 타입 파라미터를 추가로 선언하면 배열로 저장됨
			System.out.println("취미 : " + Arrays.toString(hobby));
			// => Map 객체에 "hobby"라는 키로 "A,B,C" 형식으로 문자열 결합하여 취미 저장
			if(hobby != null) { // 츄ㅣ미를 하나라도 체크했을 경우
				String strHobby = "";
				for(int i = 0; i < hobby.length; i++) {
					// 0번 인덱스를 제외한 나머지는 취미 앞에 콤마(,)결합
					if(i > 0) {
						strHobby += ",";
					} 
					strHobby += hobby[i];
				}
				map.put("hobby", strHobby);
				System.out.println("취미 : " + map.get("hobby"));
			} // 콤마같은 여러값의 조합이 있을 때는 Map보다는 객체가 유리함.
			
			// --------------------------------
			// MemberService - modifyMember() 메서드 호출하여 회원정보 수정 요청
			// => 파라미터 : Map 객체, 리턴타입 : int(updateCount)
			int updateCount = memberService.modifyMember(map);
			
			// 수정 요청 결과 판별
			// => 성공 시 수정된 정보 확인을 위해 회원 상세정보 페이지("MemberInfo")로 리다이레ㅌ트
			// 	  위의 작업 대신, 실패와 마찬가지로 "회원정보 수정 성공" 메세지 풀력 및 
			//	  회원 상새정보 페이지로 이동하는 작업처리 
			// => 실패 시 "회원정보 수정ㅅ 실패" 메세지 저장 후 이전 페이지 처리(fail.jsp)
			// =>success.jsp와 fail.jsp는 하나로 통합해도 무방함.
			if(updateCount > 0) {
//				return "redirect:/MemberInfo";
				model.addAttribute("msg", "회원정보 수정 성공");
				model.addAttribute("targetURL", "MemberInfo");
				return "result/success";
			} else {
				model.addAttribute("msg", "회원정보 수정 실패");
				return "result/fail";
			}
		}
		
		// ===============================================================================
		// 회원탈퇴("MemberWithdraw" - GET)
		@GetMapping("MemberWithdraw")
		public String withdrawForm(HttpSession session, Model model) {
			// ----------------------------------------------------------------------------
			// 세션 아이디 체크하여 세션 아이디가 없을 경우 "result/fail" 페이지 포워딩 처리
			// => "msg" 속성값 : "로그인 필수!"
			// => "targetURL" 속성값 : "MemberLogin" (로그인페이지 URL)
			String id = (String)session.getAttribute("sId");
			if(id == null) {
				model.addAttribute("msg", "로그인 필수!\\n로그인 페이지로 이동합니다.");
				model.addAttribute("targetURL", "MemberLogin");
				return "result/fail";
			}
			// ----------------------------------------------------------------------------
			// member/member_withdraw_form.jsp 페이지 포워딩
			return "member/member_withdraw_form";
		}
				
		// 회원탈퇴("MemberWithdraw" - POST)
		@PostMapping("MemberWithdraw")
		public String withdraw(String passwd, BCryptPasswordEncoder passwordEncoder, 
				HttpSession session, Model model) {
			// 입력받은 패스워드 확인
			String id = (String)session.getAttribute("sId");
			
			// MemberService - getMemberPasswd() 메서드 재사용하여 암호화 된 패스워드 조회
			// => 파라미터 : 아이디(String)   리턴타입 : String(dbPasswd)
			String dbPasswd = memberService.getMemberPasswd(id);
			// 조회된 패스워드와 입력받은 기존 패스워드를 비교(Map 객체의 oldPasswd 키 사용)
			if(dbPasswd == null || !passwordEncoder.matches(passwd, dbPasswd)) { // 아이디 불일치 또는 패스워드 불일치
				model.addAttribute("msg", "권한이 없습니다!");
				return "result/fail";
			}
			// ----------------------------------------------------------------------------
			// MmberService - withdrawMember() 메서드 호출하여 회원 탈퇴 요청
			// => 파라미터 : 아이디 , 리턴타입 : int(widhrawResult)
			// ----------------------------------------------------------------------------
			int widhrawResult = memberService.withdrawMember(id);
			
			// 탈퇴 요청 결과 판별
			if (widhrawResult > 0) {
				session.invalidate();
				model.addAttribute("msg", "탈퇴 처리가 완료되었습니다.");
				model.addAttribute("targetURL", "./");
				return "result/success";
			} else {
				model.addAttribute("msg", "탈퇴 실패!\\n 관리자에게 문의 바랍니다.");
				return "result/fail";
			}
			
			// ----------------------------------------------------------------------------
		}
		
		
}