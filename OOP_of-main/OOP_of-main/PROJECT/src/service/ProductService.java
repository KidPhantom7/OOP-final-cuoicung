package service;

import dao.Productdao;
import dao.Categorydao;
import model.Product;
import java.util.List;

public class ProductService {
    Productdao dao = new Productdao();
    Categorydao catDao = new Categorydao();

    public boolean checkIdExists(int productId) {
        if (productId <= 0) return false;
        return dao.findById(productId);
    }

    public boolean isProductOwner(int productId, int userId) {
        if (productId <= 0 || userId <= 0) return false;
        return dao.isProductOwner(productId, userId);
    }

    public void createProduct(Product p) {
        // 1. Kiểm tra tên, giá, số lượng
        if (p.getName() == null || p.getName().isEmpty()) {
            System.out.println("Lỗi: Tên sản phẩm không được để trống!");
            return;
        }
        if (p.getPrice() < 0) {
            System.out.println("Lỗi: Giá sản phẩm không được âm!");
            return;
        }

        // 2. KIỂM TRA ID DANH MỤC CÓ TỒN TẠI KHÔNG
        if (!catDao.findById(p.getCategoryId())) {
            System.out.println("Lỗi: Mã danh mục (ID = " + p.getCategoryId() + ") không tồn tại trong hệ thống!");
            return;
        }

        // 3. Mọi thứ hợp lệ mới thêm vào DB
        dao.add(p);
        System.out.println("Đã thêm sản phẩm thành công.");
    }

    public void editProduct(Product p) {
        // Kiểm tra dữ liệu đầu vào cơ bản
        if (p.getName() == null || p.getName().isEmpty()) {
            System.out.println("Lỗi: Tên sản phẩm không được để trống!");
            return;
        }
        if (p.getPrice() < 0 || p.getStock() < 0) {
            System.out.println("Lỗi: Giá hoặc số lượng sản phẩm không được âm!");
            return;
        }

        // Kiểm tra lại khóa ngoại danh mục một lần nữa cho chắc chắn
        if (!catDao.findById(p.getCategoryId())) {
            System.out.println("Lỗi nghiệp vụ: Mã danh mục " + p.getCategoryId() + " không tồn tại!");
            return;
        }

        // Thực thi cập nhật xuống Database
        dao.update(p);
        System.out.println("Đã cập nhật sản phẩm thành công.");
    }

    public void removeProduct(int productId) {
        if (productId <= 0) {
            System.out.println("ID không hợp lệ!");
            return;
        }
        if (!dao.findById(productId)) {
            System.out.println("Lỗi: Không tìm thấy sản phẩm có ID = " + productId + " để xóa!");
            return;
        }
        dao.delete(productId);
        System.out.println("Đã xóa sản phẩm thành công.");
    }

    public void displayAll() {
        List<Product> list = dao.getAll();
        if (list.isEmpty()) {
            System.out.println("Gian hàng hiện tại trống.");
        } else {
            System.out.println("\n--- DANH SÁCH SẢN PHẨM ---");
            for (Product p : list) {
                System.out.println("ID: " + p.getId() + " | Tên: " + p.getName() +
                        " | Giá: " + p.getPrice() + " | SL: " + p.getStock() +
                        " | Mã DM: " + p.getCategoryId() + " | Mã Shop: " + p.getShopId());
            }
        }
    }

    public void displayAllBySeller(int sellerId) {
        List<Product> list = dao.getBySellerId(sellerId);
        if (list.isEmpty()) {
            System.out.println("Gian hàng của bạn hiện tại trống.");
        } else {
            System.out.println("\n--- SẢN PHẨM CỦA BẠN ---");
            for (Product p : list) {
                System.out.println("ID: " + p.getId() + " | Tên: " + p.getName() +
                        " | Giá: " + p.getPrice() + " | SL: " + p.getStock() +
                        " | Mã DM: " + p.getCategoryId() + " | Mã Shop: " + p.getShopId());
            }
        }
    }

    public List<Product> getBySellerId(int sellerId) {
        return dao.getBySellerId(sellerId);
    }


    // Hàm phụ trợ dùng chung để in danh sách sản phẩm khi tìm kiếm/lọc
    private void printSearchResult(List<Product> list) {
        if (list.isEmpty()) {
            System.out.println("Không tìm thấy sản phẩm nào khớp với điều kiện lọc.");
        } else {
            System.out.println("\n--- KẾT QUẢ TÌM KIẾM/LỌC ---");
            for (Product p : list) {
                System.out.println("ID: " + p.getId() + " | Tên: " + p.getName() +
                        " | Giá: " + p.getPrice() + " | SL: " + p.getStock() +
                        " | Mã DM: " + p.getCategoryId() + " | Mã Shop: " + p.getShopId());
            }
        }
    }

    public void searchProductsByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Từ khóa tìm kiếm không hợp lệ!");
            return;
        }
        List<Product> list = dao.searchByName(keyword.trim());
        printSearchResult(list);
    }

    public void filterProductsByPrice(double min, double max) {
        if (min < 0 || max < min) {
            System.out.println("Khoảng giá lọc không hợp lệ (Giá tối thiểu không được âm, giá tối đa phải lớn hơn giá tối thiểu)!");
            return;
        }
        List<Product> list = dao.filterByPrice(min, max);
        printSearchResult(list);
    }

    public void filterProductsByCategory(int cartId) {
        if (cartId <= 0) {
            System.out.println("Mã danh mục không hợp lệ!");
            return;
        }
        List<Product> list = dao.filterByCategoryId(cartId);
        printSearchResult(list);
    }

    public List<Product> getAll() {
        return dao.getAll();
    }
}
