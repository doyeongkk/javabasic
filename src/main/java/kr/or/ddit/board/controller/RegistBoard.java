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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.or.ddit.board.model.BoardInfoVo;
import kr.or.ddit.board.model.BoardVo;
import kr.or.ddit.board.model.FileVo;
import kr.or.ddit.board.service.BoardService;
import kr.or.ddit.board.service.BoardServiceI;
import kr.or.ddit.user.util.FileUtil;

@MultipartConfig
@WebServlet("/registBoard")
public class RegistBoard extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	private static final Logger logger = LoggerFactory.getLogger(RegistBoard.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		String boardNo = request.getParameter("boardNo");
		
		BoardServiceI service = new BoardService();
		
		List<BoardInfoVo> boardInfoList = service.selectAllBoardInfo();
		
		request.setAttribute("boardInfoList", boardInfoList);
		
		request.setAttribute("boardNo", boardNo);
		
		request.getRequestDispatcher("/board/registBoard.jsp").forward(request, response);
		
		
	}

	protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		int boardNo = Integer.parseInt(request.getParameter("boardNo"));
		String userId = request.getParameter("userId");
		String title = request.getParameter("title");
		String cont = request.getParameter("cont");
		
		BoardVo boardVo = new BoardVo();
		boardVo.setBor_no(boardNo);
		boardVo.setUser_id(userId);
		boardVo.setTitle(title);
		boardVo.setCont(cont);
		
		String fileName = "";
		String realFileName = "";
		
		BoardServiceI service = new BoardService();
		
		int insertCnt = 0;
		
		try {
			insertCnt = service.registBoard(boardVo);
		} catch (Exception e) {
			e.printStackTrace();
			insertCnt = 0;
		}
		
		if(insertCnt == 1) {
			int maxPostNo = service.selectMaxPostNo();
			FileVo fileVo = new FileVo();
			fileVo.setBor_no(boardNo);
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

			response.sendRedirect(request.getContextPath()+"/boardPostView?boardNo="+boardNo+"&postNo="+maxPostNo+"&userId="+userId);
		
		}else {
			doGet(request, response);
		}
		
		
	}

}
