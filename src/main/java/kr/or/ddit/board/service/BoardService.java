package kr.or.ddit.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.or.ddit.board.model.BoardInfoVo;
import kr.or.ddit.board.model.BoardVo;
import kr.or.ddit.board.model.CommentVo;
import kr.or.ddit.board.model.FileVo;
import kr.or.ddit.board.repository.BoardDao;
import kr.or.ddit.board.repository.BoardDaoI;
import kr.or.ddit.common.model.PageVo;
import kr.or.ddit.user.repository.UserDao;

@Service("boardService")
public class BoardService implements BoardServiceI {

	@Resource(name="boardDao")
	private BoardDao boardDao;
	
	public BoardService() {}
	
	public BoardService(BoardDao boardDao) {
		this.boardDao = boardDao;
	}
	
	@Override
	public List<BoardInfoVo> selectAllBoardInfo() {
		// TODO Auto-generated method stub
		return boardDao.selectAllBoardInfo();
	}

	@Override
	public int insertBoardInfo(BoardInfoVo vo) {
		// TODO Auto-generated method stub
		return boardDao.insertBoardInfo(vo);
	}

	@Override
	public int modifyBoardInfo(BoardInfoVo vo) {
		// TODO Auto-generated method stub
		return boardDao.modifyBoardInfo(vo);
	}

	@Override
	public Map<String,Object> searchPagingBoard(PageVo vo) {
		
		Map<String,Object> map = new HashMap<>();
		
		List<BoardVo> boardList = boardDao.searchPagingBoard(vo);
		int boardCnt = boardDao.allBoardCnt(vo);
		
		map.put("boardList", boardList);
		map.put("boardCnt", boardCnt);
		
		return map;
	}

	@Override
	public int registBoard(BoardVo vo) {
		// TODO Auto-generated method stub
		return boardDao.registBoard(vo);
	}

	@Override
	public BoardVo selectBoardPost(BoardVo vo) {
		// TODO Auto-generated method stub
		return boardDao.selectBoardPost(vo);
	}

	@Override
	public int registComentBoard(BoardVo vo) {
		// TODO Auto-generated method stub
		return boardDao.registComentBoard(vo);
	}

	@Override
	public int insertFile(FileVo vo) {
		// TODO Auto-generated method stub
		return boardDao.insertFile(vo);
	}

	@Override
	public int selectMaxPostNo() {
		// TODO Auto-generated method stub
		return boardDao.selectMaxPostNo();
	}

	@Override
	public List<FileVo> selectFileList(FileVo vo) {
		// TODO Auto-generated method stub
		return boardDao.selectFileList(vo);
	}

	@Override
	public FileVo selectFile(FileVo vo) {
		// TODO Auto-generated method stub
		return boardDao.selectFile(vo);
	}

	@Override
	public int deleteBoardPost(BoardVo vo) {
		// TODO Auto-generated method stub
		return boardDao.deleteBoardPost(vo);
	}

	@Override
	public int registComment(CommentVo vo) {
		// TODO Auto-generated method stub
		return boardDao.registComment(vo);
	}

	@Override
	public List<CommentVo> selectBoardComment(CommentVo vo) {
		// TODO Auto-generated method stub
		return boardDao.selectBoardComment(vo);
	}

	@Override
	public int deleteComment(CommentVo vo) {
		// TODO Auto-generated method stub
		return boardDao.deleteComment(vo);
	}

	@Override
	public int deleteFile(int att_no) {
		// TODO Auto-generated method stub
		return boardDao.deleteFile(att_no);
	}

	@Override
	public int modifyBoard(BoardVo vo) {
		// TODO Auto-generated method stub
		return boardDao.modifyBoard(vo);
	}

	@Override
	public BoardInfoVo selectBoardInfo(int bor_no) {
		// TODO Auto-generated method stub
		return boardDao.selectBoardInfo(bor_no);
	}

}
