package kr.or.ddit.board.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.ddit.board.model.CommentVo;
import kr.or.ddit.board.service.BoardService;
import kr.or.ddit.board.service.BoardServiceI;

@WebServlet("/deleteComment")
public class DeleteComment extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		
		int comNo = Integer.parseInt(request.getParameter("comNo"));
		
		int boardNo = Integer.parseInt(request.getParameter("boardNo"));
		int postNo = Integer.parseInt(request.getParameter("postNo"));
		String rUserId = request.getParameter("rUserId");
		String comment = request.getParameter("comment");
		
		
		CommentVo commentVo = new CommentVo();
		
		commentVo.setCom_no(comNo);
		
		BoardServiceI service = new BoardService();
		
		service.deleteComment(commentVo);
		
		response.sendRedirect(request.getContextPath()+"/boardPostView?boardNo="+boardNo+"&postNo="+postNo+"&userId="+rUserId);
		
		
	}

}
