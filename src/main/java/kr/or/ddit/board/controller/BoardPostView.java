package kr.or.ddit.board.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.ddit.board.model.BoardInfoVo;
import kr.or.ddit.board.model.BoardVo;
import kr.or.ddit.board.model.CommentVo;
import kr.or.ddit.board.model.FileVo;
import kr.or.ddit.board.service.BoardService;
import kr.or.ddit.board.service.BoardServiceI;

@WebServlet("/boardPostView")
public class BoardPostView extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		int boardNo = Integer.parseInt(request.getParameter("boardNo"));
		int postNo = Integer.parseInt(request.getParameter("postNo"));
		String userId = request.getParameter("userId");
		
		BoardVo boardVo = new BoardVo();
		boardVo.setBor_no(boardNo);
		boardVo.setPost_no(postNo);
		boardVo.setUser_id(userId);
	
		FileVo fileVo = new FileVo();
		fileVo.setBor_no(boardNo);
		fileVo.setPost_no(postNo);
		fileVo.setUser_id(userId);
		
		CommentVo commentVo = new CommentVo();
		commentVo.setBor_no(boardNo);
		commentVo.setPost_no(postNo);
		commentVo.setUser_id(userId);
		
		BoardServiceI service = new BoardService();
		
		BoardVo postVo = service.selectBoardPost(boardVo);
	
		List<BoardInfoVo> boardInfoList = service.selectAllBoardInfo();
		
		List<FileVo> fileList = service.selectFileList(fileVo);
		
		List<CommentVo> commentList = service.selectBoardComment(commentVo);
		
		request.setAttribute("commentList", commentList);
		
		request.setAttribute("boardInfoList", boardInfoList);
		
		request.setAttribute("postVo", postVo);
		
		request.setAttribute("fileList", fileList);
		
		request.getRequestDispatcher("/board/boardPostView.jsp").forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
