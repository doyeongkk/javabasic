package kr.or.ddit.user.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.or.ddit.user.model.UserVo;
import kr.or.ddit.user.service.UserService;
import kr.or.ddit.user.service.UserServiceI;

@WebServlet("/login.do")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	private UserServiceI userService = new UserService();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String userid = request.getParameter("userid");
		String pass = request.getParameter("pass");
		
		UserVo user = userService.selectUser(userid);
		
		if( user !=null && pass.equals(user.getPass())) {
			HttpSession session = request.getSession();
			session.setAttribute("S_USER", user); 
			response.sendRedirect(request.getContextPath()+"/mainController");
		}else {
			response.sendRedirect(request.getContextPath()+"/board/login.jsp");
		}
		
		
	}

}
