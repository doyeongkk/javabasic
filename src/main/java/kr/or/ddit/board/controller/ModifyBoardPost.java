package kr.or.ddit.board.controller;

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
@WebServlet("/modifyBoardPost")
public class ModifyBoardPost extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ModifyBoardPost.class);

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		int boardNo = Integer.parseInt(request.getParameter("cBoardNo"));
		int postNo = Integer.parseInt(request.getParameter("cPostNo"));
		String userId = request.getParameter("cUserId");
		
		BoardVo vo = new BoardVo();
		vo.setBor_no(boardNo);
		vo.setPost_no(postNo);
		vo.setUser_id(userId);
		
		BoardServiceI service = new BoardService();
		
		BoardVo boardVo = service.selectBoardPost(vo);
		
		logger.debug("boardVo : {}" ,boardVo);
		
		List<BoardInfoVo> boardInfoList = service.selectAllBoardInfo();
		
		FileVo fileVo = new FileVo();
		fileVo.setBor_no(boardNo);
		fileVo.setPost_no(postNo);
		fileVo.setUser_id(userId);
		
		List<FileVo> fileList = service.selectFileList(fileVo);
		
		request.setAttribute("fileList", fileList);
		
		request.setAttribute("boardInfoList", boardInfoList);
		
		request.setAttribute("boardVo", boardVo);
		request.setAttribute("boardNo", boardNo);
		
		request.getRequestDispatcher("/board/modifyBoard.jsp").forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
	
		
		int boardNo = Integer.parseInt(request.getParameter("boardNo"));
		int postNo = Integer.parseInt(request.getParameter("postNo"));
		
		String userId = request.getParameter("userId");
		String title = request.getParameter("title");
		String cont = request.getParameter("cont");
		
		
		
		
		BoardVo boardVo = new BoardVo();
		boardVo.setPost_no(postNo);
		boardVo.setBor_no(boardNo);
		boardVo.setUser_id(userId);
		boardVo.setTitle(title);
		boardVo.setCont(cont);
		
		logger.debug("포스트의 보드VO {}",boardVo);
		
		String fileName = "";
		String realFileName = "";
		
		BoardServiceI service = new BoardService();
		
		int updateCnt = 0;
		
		try {
			updateCnt = service.modifyBoard(boardVo);
		} catch (Exception e) {
			e.printStackTrace();
			updateCnt = 0;
		}
		
		if(updateCnt == 1) {
			FileVo fileVo = new FileVo();
			fileVo.setBor_no(boardNo);
			fileVo.setPost_no(postNo);
			fileVo.setUser_id(userId);
			
			
			Collection<Part> parts = request.getParts();
			for(Part p : parts) {
				if(p.getSize()>0) {
					if(p.getHeader("Content-Disposition").contains("name=\"fileName\";")) {
						fileName = FileUtil.getFileName(p.getHeader("Content-Disposition"));
	
						String fileExtension = FileUtil.getFileExtension(fileName);
						realFileName = UUID.randomUUID().toString()+fileExtension;
						
						fileVo.setFile_nm(fileName);
						fileVo.setRead_file_name(realFileName);
						
						
						p.write("d:/uploadFile/"+realFileName);
						service.insertFile(fileVo);
					}
				}
			}

			response.sendRedirect(request.getContextPath()+"/boardPostView?boardNo="+boardNo+"&postNo="+postNo+"&userId="+userId);
		
		}else {
			doGet(request, response);
		}
	}

}
