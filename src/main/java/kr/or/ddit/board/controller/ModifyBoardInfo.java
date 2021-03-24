package kr.or.ddit.board.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.ddit.board.model.BoardInfoVo;
import kr.or.ddit.board.service.BoardService;
import kr.or.ddit.board.service.BoardServiceI;

@WebServlet("/modifyBoardInfo")
public class ModifyBoardInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		String boardNoStr = request.getParameter("boardNo");
		String checkStr = request.getParameter("check");
		
		int boardNo = 0;
		int check = 0;
		
		if(!"".equals(boardNoStr) && boardNoStr != null) {
			boardNo = Integer.parseInt(boardNoStr);
		}
		
		if(checkStr.equals("t")) {
			check = 1;
		}
		
		BoardInfoVo boardInfo = new BoardInfoVo();
		boardInfo.setBor_no(boardNo);
		boardInfo.setBor_act(check);
		
		
		BoardServiceI service = new BoardService();
		
		int updateCnt = 0;
		
		try {
			updateCnt = service.modifyBoardInfo(boardInfo);
		} catch (Exception e) {
			updateCnt = 0;
		}
		
		response.sendRedirect(request.getContextPath()+"/registBoardInfo");
		
		
		
	}

}
