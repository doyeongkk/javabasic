package kr.or.ddit.user.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.board.model.BoardInfoVo;
import kr.or.ddit.board.model.BoardVo;
import kr.or.ddit.board.model.CommentVo;
import kr.or.ddit.board.model.FileVo;
import kr.or.ddit.board.service.BoardServiceI;
import kr.or.ddit.common.model.PageVo;
import kr.or.ddit.user.model.UserVo;
import kr.or.ddit.user.service.UserServiceI;
import kr.or.ddit.user.util.FileUtil;

@RequestMapping("board")
@Controller
public class BoardController {
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Resource(name = "userService")
	private UserServiceI userService;
	
	@Resource(name = "boardService")
	private BoardServiceI boardService;
	
	@RequestMapping(path = "view", method = RequestMethod.GET)
	public String view() {
		logger.debug("test");
		return "board/login";
	}
	
	@RequestMapping(path = "loginController")
	public String loginController(String userid, String pass, HttpSession session) {
		
		UserVo user = userService.selectUser(userid);
		
		if(user !=null && pass.equals(user.getPass())) {
			session.setAttribute("S_USER", user);
			return "redirect:/board/mainController";
		}else {
			return "board/login";
		}
		
	}
	
	@RequestMapping(path = "mainController")
	public String mainController(@Valid Model model) {
		List<BoardInfoVo> boardInfoList = boardService.selectAllBoardInfo();
		
		model.addAttribute("boardInfoList", boardInfoList);
		
		return "board/main";
		
	}
	
	@RequestMapping(path = "registBoardInfo", method = RequestMethod.GET)
	public String registBoardInfo(@Valid Model model) {
		List<BoardInfoVo> boardInfoList = boardService.selectAllBoardInfo();
		model.addAttribute("boardInfoList", boardInfoList);
		return "board/boardInfoView";
	}
	
	@RequestMapping(path = "registBoardInfo", method = RequestMethod.POST)
	public String registBoardInfo(@Valid String boardNo,String boardName, String check,  Model model, BoardInfoVo boardInfo) {
		
		int flag = 0;
		
		if(check.equals("t")) {
			flag = 1;
		}
		
		boardInfo.setBor_act(flag);
		boardInfo.setBor_name(boardName);
		
		int insertCnt = 0;
		
		try {
			insertCnt = boardService.insertBoardInfo(boardInfo);
		} catch (Exception e) {
			insertCnt= 0;
		}
		
		return "redirect:/board/registBoardInfo";
		
	}
	
	@RequestMapping(path = "modifyBoardInfo", method = RequestMethod.POST)
	public String modifyBoardInfo(@Valid String boardNo,String boardName, String check,  Model model, BoardInfoVo boardInfo) {
		
		int boardNoStr = 0;
		int checkStr = 0;
		
		if(!"".equals(boardNo) && boardNo != null) {
			boardNoStr = Integer.parseInt(boardNo);
		}
		
		if(check.equals("t")) {
			checkStr = 1;
		}
		
		boardInfo.setBor_no(boardNoStr);
		boardInfo.setBor_act(checkStr);
		
		int updateCnt = 0;
		
		try {
			updateCnt = boardService.modifyBoardInfo(boardInfo);
		} catch (Exception e) {
			updateCnt= 0;
		}
		
		return "redirect:/board/registBoardInfo";
	}
	
	@RequestMapping(path = "boardView")
	public String boardView(@Valid Model model, String page, String pageSize, String boardNo) {
		
		int pageStr = page == null ?  1 : Integer.parseInt(page);
		int pageSizeStr = pageSize==null ? 10:Integer.parseInt(pageSize);
		
		int boardNoStr = 0;
		
		if(boardNo!=null) {
			boardNoStr = Integer.parseInt(boardNo);
		}
		
		PageVo pageVo = new PageVo(pageStr, pageSizeStr, boardNoStr);
		
		Map<String, Object> map = boardService.searchPagingBoard(pageVo);
		
		List<BoardVo> boardList = (List<BoardVo>)map.get("boardList");
		int boardCnt = (int)map.get("boardCnt");
		int pagination = (int)Math.ceil((double)boardCnt/pageSizeStr);
		
		List<BoardInfoVo> boardInfoList = boardService.selectAllBoardInfo();
		
		BoardInfoVo boardInfo = boardService.selectBoardInfo(boardNoStr);
		
		model.addAttribute("boardList", boardList);
		model.addAttribute("boardInfo", boardInfo);
		model.addAttribute("boardInfoList", boardInfoList);
		model.addAttribute("pagination", pagination);
		model.addAttribute("pageVo", pageVo);
		model.addAttribute("boardNo", boardNoStr);
		
		return "board/boardListView";
	}
	
	@RequestMapping(path = "registBoard", method = RequestMethod.GET)
	public String registBoard(Model model, String boardNo) {
		List<BoardInfoVo> boardInfoList = boardService.selectAllBoardInfo();
		model.addAttribute("boardInfoList", boardInfoList);
		model.addAttribute("boardNo", boardNo);
		
		return "board/registBoard";
	}
	
	
	// 게시글 등록완료
	   @RequestMapping(path = "registBoard", method = RequestMethod.POST)
	   public String registBoard(int boardNo, String userId, String title, String cont, Model model,
	         RedirectAttributes ra, MultipartHttpServletRequest fileName) {

	      List<MultipartFile> files = fileName.getFiles("fileName");

	      BoardVo boardVo = new BoardVo(boardNo, 0, userId, title, cont, null, 1, 0, 0, null);
	      logger.debug(" boardVo : {}", boardVo);
	      int insertCnt = boardService.registBoard(boardVo);
	      String filename = "";
	      if (insertCnt == 1) {
	         // 첨부파일
	         int maxPostNo = boardService.selectMaxPostNo();
	         logger.debug("maxPostNo: {}", maxPostNo);
	         FileVo fileVo = new FileVo();
	         fileVo.setBor_no(boardNo);
	         fileVo.setPost_no(maxPostNo);
	         fileVo.setUser_id(userId);
	         if (files != null) {
	            for (MultipartFile profile : files) {
	               if (!("".equals(profile.getOriginalFilename()))) {

	                  try {
	                     String uploadPath = " d:" + File.separator + "uploadFile";
	                     File uploadDir = new File(uploadPath);
	                     if (!uploadDir.exists()) {
	                        uploadDir.mkdirs();
	                     }
	                     String fileExtension = FileUtil.getFileExtension(profile.getOriginalFilename());
	                     String realfilename = "d:/uploadFile/" + UUID.randomUUID().toString() + fileExtension;

	                     filename = profile.getOriginalFilename();
	                     logger.debug("filename: {}", filename);

	                     profile.transferTo(new File(realfilename));

	                     fileVo.setFile_nm(filename);
	                     fileVo.setRead_file_name(realfilename);

	                     boardService.insertFile(fileVo);

	                  } catch (IllegalStateException | IOException e) {

	                     e.printStackTrace();
	                  }
	               }
	            }
	         }

	         ra.addAttribute("boardNo", boardNo);
	         ra.addAttribute("postNo", maxPostNo);
	         ra.addAttribute("userId", userId);
	         return "redirect:/board/boardPostView";

	      } else {

	         return "board/RegistBoard";
	      }
	   }
	
	@RequestMapping(path = "boardPostView")
	public String boardPostView(BoardVo boardVo, int boardNo, int postNo, String userId
			,Model model, FileVo fileVo, CommentVo commentVo) {
		boardVo.setBor_no(boardNo);
		boardVo.setPost_no(postNo);
		boardVo.setUser_id(userId);
		
		fileVo.setBor_no(boardNo);
		fileVo.setPost_no(postNo);
		fileVo.setUser_id(userId);
		
		commentVo.setBor_no(boardNo);
		commentVo.setPost_no(postNo);
		commentVo.setUser_id(userId);
		
		BoardVo postVo = boardService.selectBoardPost(boardVo);
		
		List<BoardInfoVo> boardInfoList = boardService.selectAllBoardInfo();
		
		List<FileVo> fileList = boardService.selectFileList(fileVo);
		
		List<CommentVo> commentList = boardService.selectBoardComment(commentVo);
		
		model.addAttribute("commentList", commentList);
		
		model.addAttribute("boardInfoList", boardInfoList);
		
		model.addAttribute("postVo", postVo);
		
		model.addAttribute("fileList", fileList);
		
		return "board/boardPostView";
	}
	
	@RequestMapping("deleteBoardPost")
	public String deleteBoardPost(BoardVo boardVo, int cBoardNo, int cPostNo, String cUserId) {
		boardVo.setBor_no(cBoardNo);
		boardVo.setPost_no(cPostNo);
		boardVo.setUser_id(cUserId);
		
		boardService.deleteBoardPost(boardVo);
		
		return "redirect:/board/boardView?boardNo=" + cBoardNo;
	}
	
	@RequestMapping(path = "modifyBoardPost", method = RequestMethod.GET)
	public String modifyBoardPost(Model model, BoardVo vo, int cBoardNo, int cPostNo, String cUserId) {
		vo.setBor_no(cBoardNo);
		vo.setPost_no(cPostNo);
		vo.setUser_id(cUserId);
		
		BoardVo boardVo = boardService.selectBoardPost(vo);
		
		List<BoardInfoVo> boardInfoList = boardService.selectAllBoardInfo();
		
		FileVo fileVo = new FileVo();
		fileVo.setBor_no(cBoardNo);
		fileVo.setPost_no(cPostNo);
		fileVo.setUser_id(cUserId);
		
		List<FileVo> fileList = boardService.selectFileList(fileVo);
		
		model.addAttribute("fileList", fileList);
		model.addAttribute("boardInfoList", boardInfoList);
		model.addAttribute("boardVo", boardVo);
		model.addAttribute("boardNo", cBoardNo);
		
		return "board/modifyBoard";
	}
	
//	@RequestMapping(path = "modifyBoardPost", method = RequestMethod.POST)
//	public String modifyBoardPost(Model model, int boardNo, int postNo,
//			String userId, String title,
//			String cont, BoardVo boardVo) {
//		boardVo.setPost_no(postNo);
//		boardVo.setBor_no(boardNo);
//		boardVo.setUser_id(userId);
//		boardVo.setTitle(title);
//		boardVo.setCont(cont);
//		
//		String fileName = "";
//		String realFileName = "";
//		
//		int updateCnt = 0;
//		
//		try {
//			updateCnt = boardService.modifyBoard(boardVo);
//		} catch (Exception e) {
//			e.printStackTrace();
//			updateCnt = 0;
//		}
//		
//		return "redirect:/board/boardPostView?boardNo="+boardNo+"&postNo="+postNo+"&userId="+userId;
//	}
	
	@RequestMapping(path = "modifyBoardPost", method = RequestMethod.POST)
	public String modifyBoardPost(int boardNo, int postNo, String userId, String title, String cont, Model model,
	         RedirectAttributes ra, MultipartHttpServletRequest fileName) {
		 List<MultipartFile> files = fileName.getFiles("fileName");

	      BoardVo boardVo = new BoardVo(boardNo, postNo, userId, title, cont, null, 1, 0, 0, null);
	      logger.debug(" boardVo : {}", boardVo);
	      int updateCnt = boardService.modifyBoard(boardVo);
	      String filename = "";
	      if (updateCnt == 1) {
	         // 첨부파일
	         int maxPostNo = boardService.selectMaxPostNo();
	         logger.debug("maxPostNo: {}", maxPostNo);
	         FileVo fileVo = new FileVo();
	         fileVo.setBor_no(boardNo);
	         fileVo.setPost_no(maxPostNo);
	         fileVo.setUser_id(userId);
	         if (files != null) {
	            for (MultipartFile profile : files) {
	               if (!("".equals(profile.getOriginalFilename()))) {

	                  try {
	                     String uploadPath = " d:" + File.separator + "uploadFile";
	                     File uploadDir = new File(uploadPath);
	                     if (!uploadDir.exists()) {
	                        uploadDir.mkdirs();
	                     }
	                     String fileExtension = FileUtil.getFileExtension(profile.getOriginalFilename());
	                     String realfilename = "d:/uploadFile/" + UUID.randomUUID().toString() + fileExtension;

	                     filename = profile.getOriginalFilename();
	                     logger.debug("filename: {}", filename);

	                     profile.transferTo(new File(realfilename));

	                     fileVo.setFile_nm(filename);
	                     fileVo.setRead_file_name(realfilename);

	                     boardService.insertFile(fileVo);

	                  } catch (IllegalStateException | IOException e) {

	                     e.printStackTrace();
	                  }
	               }
	            }
	         }

	         ra.addAttribute("boardNo", boardNo);
	         ra.addAttribute("postNo", maxPostNo);
	         ra.addAttribute("userId", userId);
	         return "redirect:/board/boardPostView";

	      } else {

	         return "board/modifyBoard";
	      }
	   }
	
	@RequestMapping(path = "registComent", method = RequestMethod.POST)
	public String registComent(int boardNo, int postNo,
			String userId, String rUserId,
			String comment, CommentVo commentVo) {
		comment = comment.replaceAll("\n", "<br>");
		
		commentVo.setBor_no(boardNo);
		commentVo.setPost_no(postNo);
		commentVo.setCom_con(comment);
//		commentVo.setCom_com_user_id(userId);
		commentVo.setCom_user_id(userId);
		commentVo.setUser_id(rUserId);
		logger.debug("commentVo : {}", commentVo);
		
		int insertCnt = boardService.registComment(commentVo);
		
		return "redirect:/board/boardPostView?boardNo="+boardNo+"&postNo="+postNo+"&userId="+rUserId;
	}
	
	@RequestMapping(path = "deleteComment")
	public String deleteComment(CommentVo commentVo, int comNo
			, int boardNo, int postNo, String rUserId, String comment) {
		
		commentVo.setCom_no(comNo);
		boardService.deleteComment(commentVo);
		
		return "redirect:/board/boardPostView?boardNo="+boardNo+"&postNo="+postNo+"&userId="+rUserId; 
		
	}
	
	@RequestMapping(path = "comentBoardPost", method = RequestMethod.GET)
	public String comentBoardPost(Model model, int cBoardNo
			, int cPostNo, String cUserId) {
		List<BoardInfoVo> boardInfoList = boardService.selectAllBoardInfo();
		
		model.addAttribute("boardInfoList", boardInfoList);
		model.addAttribute("cBoardNo", cBoardNo);
		model.addAttribute("cPostNo", cPostNo);
		model.addAttribute("cUserId", cUserId);
		
		return "board/registComentBoard";
	}
	
	@RequestMapping(path = "comentBoardPost", method = RequestMethod.POST)
	public String comentBoardPost(Model model, String userId,
			String title, String cont, int cBoardNo, int cPostNo
			,String cUserId, BoardVo boardVo) {
		boardVo.setBor_no(cBoardNo);
		boardVo.setUser_id(userId);
		boardVo.setTitle(title);
		boardVo.setCont(cont);
		boardVo.setBor_c_nm(cBoardNo);
		boardVo.setPost_c_no(cPostNo);
		boardVo.setRep_user_id(cUserId);
		
		String fileName = "";
		String realFileName = "";
		
		int insertCnt = 0;
		
		try {
			insertCnt = boardService.registComentBoard(boardVo);
		} catch (Exception e) {
			e.printStackTrace();
			insertCnt = 0;
		}
		
		return "redirect:/board/boardView?boardNo="+cBoardNo+"&userId="+userId;
	}
	
	 //파일 다운로드 
	   @RequestMapping("fileDownload")
	   public void fileDownload(int attNo, int boardNo, int postNo, String userId, HttpServletResponse resp , Model model) throws IOException {
	      
	      FileVo vo = new FileVo();
	      vo.setAtt_no(attNo);
	      vo.setPost_no(postNo);
	      vo.setBor_no(boardNo);
	      vo.setUser_id(userId);
	      
	      FileVo fileVo = boardService.selectFile(vo);
	      String fileName= "";
	      
	      String path ="";
	      
	      path = fileVo.getRead_file_name();
	      fileName = fileVo.getFile_nm();
	      
	      logger.debug("path : {}",path);
	      logger.debug("fileName : {}",fileName);
	      
	      resp.setHeader("Content-Disposition","attachment; filename="+fileName);
	      
	      FileInputStream fis = new FileInputStream(path);
	      ServletOutputStream sos = resp.getOutputStream();
	      
	      byte[] buff = new byte[512];
	      while (fis.read(buff) != -1) {
	         sos.write(buff);
	      }
	      
	      fis.close();
	      sos.flush();
	      sos.close();
	   }
	   //파일삭제
	   @RequestMapping(path = "deleteFile", method = RequestMethod.POST)
	   public String deleteFile(int att_no, Model model) {
	      
	      int deleteCnt = boardService.deleteFile(att_no);
	      
	      model.addAttribute("cnt" ,deleteCnt);
	      logger.debug("deleteCnt : {}", deleteCnt);
	      return "jsonView";
	   }
}



















