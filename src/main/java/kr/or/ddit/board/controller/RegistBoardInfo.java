package kr.or.ddit.board.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.ddit.board.model.BoardInfoVo;
import kr.or.ddit.board.repository.BoardDao;
import kr.or.ddit.board.repository.BoardDaoI;
import kr.or.ddit.board.service.BoardService;
import kr.or.ddit.board.service.BoardServiceI;

@WebServlet("/registBoardInfo")
public class RegistBoardInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		
		BoardServiceI service = new BoardService();
		
		List<BoardInfoVo> boardInfoList = service.selectAllBoardInfo();
		
		request.setAttribute("boardInfoList", boardInfoList);
		
		request.getRequestDispatcher("/board/boardInfoView.jsp").forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		String boardName = request.getParameter("boardName");
		String check = request.getParameter("check");
		
		int flag = 0;
		
		if(check.equals("t")) {
			flag = 1;
		}
		
		BoardInfoVo boardInfo = new BoardInfoVo();
		
		boardInfo.setBor_act(flag);
		boardInfo.setBor_name(boardName);
		
		BoardServiceI service = new BoardService();
		
		int insertCnt = 0;
		
		try {
			insertCnt = service.insertBoardInfo(boardInfo);
		} catch (Exception e) {
			insertCnt = 0;
		}
		
		
		response.sendRedirect(request.getContextPath()+"/registBoardInfo");
		
//		request.getRequestDispatcher("/mainController").forward(request, response);
		
		
		
		
	}

}
