package kr.or.ddit.board.controller;

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.or.ddit.board.model.FileVo;
import kr.or.ddit.board.service.BoardService;
import kr.or.ddit.board.service.BoardServiceI;
import kr.or.ddit.user.model.UserVo;
import kr.or.ddit.user.service.UserService;
import kr.or.ddit.user.service.UserServiceI;

@WebServlet("/fileDownload")
public class FileDownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(FileDownloadServlet.class);

	private BoardServiceI service = new BoardService();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		
		
		// userid 파라미터를 이용하여
		// userService 객체를 동해 사용자의 사진 파일 이름을 획득
		// 파일 입출력을 통해 사진을 읽어들여 resp객체의 outputStream으로 응답 생성
		
		int attNo = Integer.parseInt(req.getParameter("attNo"));
		int boardNo = Integer.parseInt(req.getParameter("boardNo"));
		int postNo = Integer.parseInt(req.getParameter("postNo"));
		String userId = req.getParameter("userId");
		
		FileVo vo = new FileVo();
		
		vo.setAtt_no(attNo);
		vo.setBor_no(boardNo);
		vo.setPost_no(postNo);
		vo.setUser_id(userId);
		
		FileVo fileVo = service.selectFile(vo);
		
		String fileName ="";
		
		
		String path = "";
		
		
		path = "d:\\uploadFile\\" + fileVo.getRead_file_name();
		fileName = fileVo.getFile_nm();
		
		logger.debug("path : {} ", path);
		
		logger.debug("fileName : {} ", fileName);
		
		
		resp.setHeader("Content-Disposition", "attachment; filename="+fileName);
		
		FileInputStream fis = new FileInputStream(path);
		ServletOutputStream sos = resp.getOutputStream();
		
		byte[] buff = new byte[512];
		while(fis.read(buff)!=-1) {
			
			sos.write(buff);
			
		}
		
		
		fis.close();
		sos.flush();
		sos.close();
		
	}
}









