package ControllerWeb;

import entity.Account;
import entity.WishList;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Services.WishListServices;
import ServicesImpl.WishListServicesImpl;

@WebServlet(name = "AddWishListControl", urlPatterns = { "/addWishList" })
public class AddWishListControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

		String pidString = request.getParameter("pid");
		int productID;
		try {
			productID = Integer.parseInt(pidString);
		} catch (Exception e) {
			// xử lí ngoại lệ khi đầu vào không hợp lệ
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		// kiểm tra giới hạn đầu vào
		if (productID < 0 || productID > 1000) {
			// xử lí ngoại lệ khi giá trị đầu vào không hợp lệ
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		HttpSession session = request.getSession();
		Account a = (Account) session.getAttribute("acc");
		if (a == null) {
			response.sendRedirect("login");
			return;
		}
		int accountID = a.getId();

		WishListServices wishListServices = new WishListServicesImpl();
		WishList wishExisted = wishListServices.checkCartExist(accountID, productID);

		if (wishExisted != null) {
			request.setAttribute("mess", "Sản phẩm đã có trong danh sách!");
			request.getRequestDispatcher("wishlist").forward(request, response);
		} else {
			wishListServices.insertCart(accountID, productID);
			request.setAttribute("mess", "Đã thêm sản phẩm vào danh sách!");
			request.getRequestDispatcher("wishlist").forward(request, response);
		}

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
