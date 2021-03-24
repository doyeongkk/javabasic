//package kr.or.ddit.board.controller;
//
//import java.io.IOException;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import kr.or.ddit.board.model.FileVo;
//import kr.or.ddit.board.service.BoardService;
//import kr.or.ddit.board.service.BoardServiceI;
//
//@WebServlet("/deleteFile")
//public class DeleteFile extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//       
// 
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		request.setCharacterEncoding("UTF-8");
//		
//		int attNo = Integer.parseInt(request.getParameter("attno"));
//		
//		FileVo fileVo = new FileVo();
//		fileVo.setAtt_no(attNo);
//		
//		BoardServiceI service = new BoardService();
//		
//		int deleteCnt = service.deleteFile(fileVo);
//		
//		request.setAttribute("deleteCnt", deleteCnt);
//		
//		request.getRequestDispatcher("/board/result.jsp").forward(request, response);
//		
//	}
//
//}
