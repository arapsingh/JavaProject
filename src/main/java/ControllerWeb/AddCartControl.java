package ControllerWeb;

import entity.Account;
import entity.Cart;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Services.CartServices;
import ServicesImpl.CartServicesImpl;

@WebServlet(name = "AddCartControl", urlPatterns = { "/addCart" })
public class AddCartControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

		String rawPID = request.getParameter("pid");
		int productID;
		try {
			productID = Integer.parseInt(request.getParameter("pid"));
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

		// Sửa lỗi số lượng âm từ url
		String rawquantity = request.getParameter("quantity");
		int quantity;
		String size = request.getParameter("size");
		try {
			quantity = Integer.parseInt(rawquantity);
		} catch (NumberFormatException e) {
			// Xử lí ngoại lệ khi đầu vào không hợp lệ
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		// Kiểm tra đầu vào của quantity xem nó có âm hay không, kiểm tra size có rỗng
		// hay null không
		if (quantity < 1 || size.isEmpty() || size == null) {
			// Xử lí ngoại lệ khi giá trị đầu vào không hợp lệ
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		} else {
			productID = Integer.parseInt(request.getParameter("pid"));
			HttpSession session = request.getSession();
			Account a = (Account) session.getAttribute("acc");
			if (a == null) {
				response.sendRedirect("login");
				return;
			}
			int accountID = a.getId();
			int amount = Integer.parseInt(request.getParameter("quantity"));

			CartServices cartServices = new CartServicesImpl();
			Cart cartExisted = cartServices.checkCartExist(accountID, productID, size);
			int amountExisted;

			if (cartExisted != null) {
				amountExisted = cartExisted.getAmount();
				cartServices.editAmountAndSizeCart(accountID, productID, (amountExisted + amount), size);
				request.setAttribute("mess", "Đã tăng số lượng sản phẩm!");
				request.getRequestDispatcher("managerCart").forward(request, response);
			} else {
				cartServices.insertCart(accountID, productID, amount, size);
				request.setAttribute("mess", "Đã thêm sản phẩm vào giỏ hàng!");
				request.getRequestDispatcher("managerCart").forward(request, response);
			}
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
