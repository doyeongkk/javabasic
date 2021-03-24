package kr.or.ddit.board.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.ddit.board.model.BoardVo;
import kr.or.ddit.board.service.BoardService;
import kr.or.ddit.board.service.BoardServiceI;

@WebServlet("/deleteBoardPost")
public class DeleteBoardPost extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		int cBoardNo = Integer.parseInt(request.getParameter("cBoardNo"));
		int cPostNo = Integer.parseInt(request.getParameter("cPostNo"));
		String cUserId = request.getParameter("cUserId");
		
		BoardVo boardVo = new BoardVo();
		
		boardVo.setBor_no(cBoardNo);
		boardVo.setPost_no(cPostNo);
		boardVo.setUser_id(cUserId);
		
		BoardServiceI service = new BoardService();
		
		service.deleteBoardPost(boardVo);
		
		response.sendRedirect(request.getContextPath()+"/boardView?boardNo="+cBoardNo);
		
		
	}

}
