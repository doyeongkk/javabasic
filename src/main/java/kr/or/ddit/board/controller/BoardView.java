package kr.or.ddit.board.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.ddit.board.model.BoardInfoVo;
import kr.or.ddit.board.model.BoardVo;
import kr.or.ddit.board.service.BoardService;
import kr.or.ddit.board.service.BoardServiceI;
import kr.or.ddit.common.model.PageVo;

/**
 * Servlet implementation class BoardView
 */
@WebServlet("/boardView")
public class BoardView extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		
		String boardNoStr = request.getParameter("boardNo");
		String pageStr = request.getParameter("page");
		String pageSizeStr = request.getParameter("pageSize");
		
		int page = pageStr == null ?  1 : Integer.parseInt(pageStr);
		int pageSize = pageSizeStr==null ? 10:Integer.parseInt(pageSizeStr); 
		
		
		int boardNo = 0;
		
		if(boardNoStr!=null) {
			boardNo = Integer.parseInt(boardNoStr);
		}
		
		PageVo pageVo = new PageVo(page, pageSize, boardNo);
		
		BoardServiceI service = new BoardService();
		
		Map<String, Object> map = service.searchPagingBoard(pageVo);
		
		List<BoardVo> boardList = (List<BoardVo>)map.get("boardList");
		int boardCnt = (int)map.get("boardCnt");
		int pagination = (int)Math.ceil((double)boardCnt/pageSize);
		
		List<BoardInfoVo> boardInfoList = service.selectAllBoardInfo();
		
		BoardInfoVo boardInfo = service.selectBoardInfo(boardNo);
		
		
		request.setAttribute("boardList", boardList);		
		request.setAttribute("boardInfo", boardInfo);	
		request.setAttribute("boardInfoList", boardInfoList);
		request.setAttribute("pagination", pagination);
		request.setAttribute("pageVo", pageVo);
		request.setAttribute("boardNo", boardNo);
	
		request.getRequestDispatcher("board/boardListView.jsp").forward(request, response);
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
