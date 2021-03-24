package kr.or.ddit.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import kr.or.ddit.board.model.BoardInfoVo;
import kr.or.ddit.board.model.BoardVo;
import kr.or.ddit.board.model.FileVo;
import kr.or.ddit.board.service.BoardService;
import kr.or.ddit.board.service.BoardServiceI;
import kr.or.ddit.user.util.FileUtil;

@MultipartConfig
@WebServlet("/comentBoardPost")
public class ComentBoardPost extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		int cBoardNo = Integer.parseInt(request.getParameter("cBoardNo"));
		int cPostNo = Integer.parseInt(request.getParameter("cPostNo"));
		String cUserId = request.getParameter("cUserId");
		
		BoardServiceI service = new BoardService();
		List<BoardInfoVo> boardInfoList = service.selectAllBoardInfo();
		
		request.setAttribute("boardInfoList", boardInfoList);
		request.setAttribute("cBoardNo", cBoardNo);
		request.setAttribute("cPostNo", cPostNo);
		request.setAttribute("cUserId", cUserId);
		
		request.getRequestDispatcher("/board/registComentBoard.jsp").forward(request, response);
		
	}

	protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		String userId = request.getParameter("userId");
		String title = request.getParameter("title");
		String cont = request.getParameter("cont");
		int cBoardNo = Integer.parseInt(request.getParameter("cBoardNo"));
		int cPostNo = Integer.parseInt(request.getParameter("cPostNo"));
		String cUserId = request.getParameter("cUserId");
		
		BoardVo boardVo = new BoardVo();
		boardVo.setBor_no(cBoardNo);
		boardVo.setUser_id(userId);
		boardVo.setTitle(title);
		boardVo.setCont(cont);
		boardVo.setBor_c_nm(cBoardNo);
		boardVo.setPost_c_no(cPostNo);
		boardVo.setRep_user_id(cUserId);
		
	
		String fileName = "";
		String realFileName = "";
		
		BoardServiceI service = new BoardService();
		
		int insertCnt = 0;
		
		try {
			insertCnt = service.registComentBoard(boardVo);
		} catch (Exception e) {
			e.printStackTrace();
			insertCnt = 0;
		}
		
		if(insertCnt == 1) {
			
			int maxPostNo = service.selectMaxPostNo();
			FileVo fileVo = new FileVo();
			fileVo.setBor_no(cBoardNo);
			fileVo.setPost_no(maxPostNo);
			fileVo.setUser_id(userId);
		
			Collection<Part> parts = request.getParts();
			for(Part p : parts) {
				if(p.getSize()>0) {
					if(p.getHeader("Content-Disposition").contains("name=\"fileName\";")) {
						fileName = FileUtil.getFileName(p.getHeader("Content-Disposition"));
	
						String fileExtension = FileUtil.getFileExtension(fileName);
						realFileName = UUID.randomUUID().toString()+fileExtension;
						
						String uploadPath = "d:" + File.separator + "uploadFile";
						
						File uploadDir = new File(uploadPath);
						
						if(!uploadDir.exists()) {
							uploadDir.mkdirs();
						}
						
						
						fileVo.setFile_nm(fileName);
						fileVo.setRead_file_name(realFileName);
						
						
						p.write(uploadPath+ File.separator+realFileName);
						service.insertFile(fileVo);
					}
				}
			}
		
		
			response.sendRedirect(request.getContextPath()+"/boardPostView?boardNo="+cBoardNo+"&postNo="+maxPostNo+"&userId="+userId);
		}else {
			doGet(request, response);
		}
	}

}
