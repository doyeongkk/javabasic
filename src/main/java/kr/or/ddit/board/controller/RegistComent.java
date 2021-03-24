package kr.or.ddit.board.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.or.ddit.board.model.CommentVo;
import kr.or.ddit.board.service.BoardService;
import kr.or.ddit.board.service.BoardServiceI;

@WebServlet("/registComent")
public class RegistComent extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(RegistComent.class);

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		int boardNo = Integer.parseInt(request.getParameter("boardNo"));
		int postNo = Integer.parseInt(request.getParameter("postNo"));
		String userId = request.getParameter("userId");
		String rUserId = request.getParameter("rUserId");
		String comment = request.getParameter("comment");
		comment = comment.replaceAll("\n","<br>");
		
		CommentVo commentVo = new CommentVo();
		
		commentVo.setBor_no(boardNo);
		commentVo.setPost_no(postNo);
		commentVo.setCom_con(comment);
		commentVo.setCom_user_id(userId);
		commentVo.setUser_id(rUserId);
		
		logger.debug(commentVo.toString());
		
		BoardServiceI service = new BoardService();
		
		int insertCnt = service.registComment(commentVo);
		
		response.sendRedirect(request.getContextPath()+"/boardPostView?boardNo="+boardNo+"&postNo="+postNo+"&userId="+rUserId);
		
		
		
	}

}
