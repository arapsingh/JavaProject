package Services;

import java.util.List;

import entity.Seller;

public interface SellerServices {
	void singup(String name, String phone, String email, int uID);

	Seller getSellerByUID(int uid);

	Seller getSellerBySellID(int sellID);

	List<Seller> getAllSeller();
	
	void deleteSellerByAccountID(String uid);
}
