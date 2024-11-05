package com.itwillbs.gittest_kjm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.gittest_kjm.VO.BoardVO;

@Mapper
public interface BoardMapper {

	int insertBoard(BoardVO board);

	//( 복수개 파라미터 전달 시 @Param 어노테이션을 통해 파라미터명 지정 필수)
	List<BoardVO> selectBoardList(
			@Param("searchType") String searchType,
			@Param("searchKeyword")String searchKeyword,
			@Param("startRow")int startRow, 
			@Param("listLimit")int listLimit);

	// 전체 게시물 조회
//	int selectBoardListCount(String searchType, String searchKeyword);
	// 전체 게시물 조회( 검색어 기능에 따른 검색어에 대한 게시물 수 조회로 변경)
	int selectBoardListCount(
			@Param("searchType") String searchType,@Param("searchKeyword") String searchKeyword);

	BoardVO selectBoard(int board_num);

	// 게시물 조회수 증가 
	void updateReadcount(BoardVO board);

	int deleteBoard(int board_num);

	int updateBoard(BoardVO board);

}
