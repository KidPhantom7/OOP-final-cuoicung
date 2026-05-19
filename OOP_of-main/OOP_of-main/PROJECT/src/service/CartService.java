package service;

import dao.Cartdao;
import model.Cart;
import java.util.List;

public class CartService {
    private Cartdao dao = new Cartdao();

    public void addToCart(Cart cart) {
        if (cart.getQuantity() <= 0) {
            System.out.println("Số lượng không hợp lệ!");
            return;
        }

        // Lấy số lượng tồn kho và số lượng đã có trong giỏ
        int availableStock = dao.getAvailableStock(cart.getProductId());
        int currentQtyInCart = dao.getQuantityInCart(cart.getUserId(), cart.getProductId());

        // Kiểm tra xem tổng số lượng (đã có + thêm mới) có vượt quá tồn kho không
        if (currentQtyInCart + cart.getQuantity() > availableStock) {
            System.out.println("That bai! Vuot qua so luong trong kho");
            return;
        }

        dao.addToCart(cart);
    }

    public void updateQuantity(int userId, int productId, int newQty) {
        if (newQty <= 0) {
            System.out.println("So luong phai lon hon 0!");
            return;
        }

        int availableStock = dao.getAvailableStock(productId);
        if (newQty > availableStock) {
            System.out.println("That bai! So luong vuot qua ton kho. Kho hien con: " + availableStock);
            return;
        }

        dao.updateQuantity(userId, productId, newQty);
    }

    public void viewCart(int userId) {
        dao.viewCart(userId);
    }

    public void removeFromCart(int userId, int productId) {
        dao.removeFromCart(userId, productId);
    }

    public double calculateTotalAmount(int userId) {
        return dao.calculateTotalAmount(userId);
    }

    public List<Cart> getCartItems(int userId) {
        return dao.getCartItems(userId);
    }

    public void clearCart(int userId) {
        dao.clearCart(userId);
    }

    public int getAvailableStock(int productId) {
        return dao.getAvailableStock(productId);
    }

    public int getQuantityInCart(int userId, int productId) {
        return dao.getQuantityInCart(userId, productId);
    }
}
