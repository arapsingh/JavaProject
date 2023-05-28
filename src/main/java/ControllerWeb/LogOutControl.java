package ControllerWeb;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LogOutControl", urlPatterns = { "/logout" })
public class LogOutControl extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		HttpSession session = request.getSession();
		session.removeAttribute("acc");
		// Chỉnh sửa cookie để logout userC và passC reset
		// response.sendRedirect("home");
		// Xóa cookie "userC" và "passC"
		Cookie userCookie = new Cookie("userC", "");
		userCookie.setMaxAge(0);
		response.addCookie(userCookie);

		Cookie passCookie = new Cookie("passC", "");
		passCookie.setMaxAge(0);
		response.addCookie(passCookie);

		response.sendRedirect("home");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	public String getServletInfo() {
		return "Short description";
	}

}
