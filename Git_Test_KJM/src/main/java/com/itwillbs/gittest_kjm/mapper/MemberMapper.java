package com.itwillbs.gittest_kjm.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.gittest_kjm.VO.MemberVO;

@Mapper
public interface MemberMapper {

	int insertMember(MemberVO member);

	String selectMemberPasswd(String id);

	MemberVO selectMember(MemberVO member);
	
	int updateMember(Map<String, String> map);
	
	// 회원탈퇴(파라미터가 2개이므로 @Param 어노테이션 필요)
	int updateMemberStatus(@Param("id") String id,@Param("member_status") int member_status);


}
