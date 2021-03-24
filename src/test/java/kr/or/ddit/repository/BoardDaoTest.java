package kr.or.ddit.repository;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.or.ddit.board.model.BoardInfoVo;
import kr.or.ddit.board.model.BoardVo;
import kr.or.ddit.board.repository.BoardDao;
import kr.or.ddit.board.repository.BoardDaoI;
import kr.or.ddit.common.model.PageVo;

public class BoardDaoTest {

	private static final Logger logger = LoggerFactory.getLogger(BoardDaoTest.class);


	@Test
	public void TestBoardDaoTest() {
		/***Given***/
		BoardDaoI dao = new BoardDao();

		/***When***/
		List<BoardInfoVo> boardInfo = dao.selectAllBoardInfo();

		
		/***Then***/
		
		assertEquals(2, boardInfo.size());
	}
	
	@Test
	public void TestBoardListTest() {
		/***Given***/
		BoardDaoI dao = new BoardDao();

		PageVo vo = new PageVo(1,5,1);
		
		/***When***/
		
		
		List<BoardVo> boardlist = dao.searchPagingBoard(vo);

		
		/***Then***/
		
		assertEquals(5, boardlist.size());
	}
	
	@Test
	public void BoardCntTest() {
		/***Given***/
		BoardDaoI dao = new BoardDao();

		PageVo vo = new PageVo(1,5,1);
		
		/***When***/
		
		
		int boardCnt = dao.allBoardCnt(vo);

		
		/***Then***/
		
		assertEquals(9, boardCnt);
	}

}
