package com.itwillbs.gittest_kjm.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.gittest_kjm.VO.MemberVO;
import com.itwillbs.gittest_kjm.mapper.MemberMapper;

@Service
public class MemberService {
	@Autowired
	private MemberMapper mapper;

	public int registMember(MemberVO member) {
		return mapper.insertMember(member);
	}

	// 회원 패스워드 조회 요청
	public String getMemberPasswd(String id) {
		// MemberMapper - selectMemberPasswd()
		return mapper.selectMemberPasswd(id);
	}

	public MemberVO getMember(MemberVO member) {
		return mapper.selectMember(member);
	}
	
	public int modifyMember(Map<String, String> map) {
		return mapper.updateMember(map);
	}
	
	public int withdrawMember(String id) {
		// MemberMapper - updateMemberStatus()
		// => 파라미터 : 회원 상태값(정수 1:정상, 2:휴면, 3ㅣ탈퇴)
		return mapper.updateMemberStatus(id, 3);
	}

}
