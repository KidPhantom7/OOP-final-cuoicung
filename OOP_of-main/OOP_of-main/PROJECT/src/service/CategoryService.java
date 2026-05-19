package service;

import dao.Categorydao;
import model.Category;
import java.util.List;

public class CategoryService {
    Categorydao dao = new Categorydao();

    public void createCategory(Category c) {
        if (c.getName() == null || c.getName().isEmpty()) {
            System.out.println("Tên danh mục không được để trống!");
            return;
        }
        dao.add(c);
        System.out.println("Đã thêm danh mục thành công.");
    }

    public void editCategory(Category c) {
        // 1. Kiểm tra ID cơ bản
        if (c.getId() <= 0) {
            System.out.println("ID không hợp lệ!");
            return;
        }

        // 2. Chặn ngay nếu ID không tồn tại trong hệ thống
        if (!dao.findById(c.getId())) {
            System.out.println("Lỗi: Không tìm thấy danh mục có ID = " + c.getId() + " để cập nhật!");
            return; // Dừng chương trình luôn tại đây, không chạy lệnh dưới
        }

        // 3. Kiểm tra tính hợp lệ của dữ liệu mới
        if (c.getName() == null || c.getName().isEmpty()) {
            System.out.println("Tên không được để trống!");
            return;
        }

        // 4. Mọi thứ OK mới gọi xuống DB để sửa
        dao.update(c);
        System.out.println("Đã cập nhật danh mục thành công.");
    }

    public void removeCategory(int categoryId) {
        // 1. Kiểm tra ID cơ bản
        if (categoryId <= 0) {
            System.out.println("ID không hợp lệ!");
            return;
        }

        // 2. Chặn ngay nếu ID không tồn tại
        if (!dao.findById(categoryId)) {
            System.out.println("Lỗi: Không tìm thấy danh mục có categoryId = " + categoryId + " để xóa!");
            return; // Dừng chương trình luôn, không chạy xuống lệnh xóa
        }

        // 3. Xác nhận tồn tại xong mới tiến hành xóa
        dao.delete(categoryId);
        System.out.println("Đã xóa danh mục thành công.");
    }

    public void displayAll() {
        List<Category> list = dao.getAll();
        if (list.isEmpty()) {
            System.out.println("Danh sách trống.");
        } else {
            for (Category c : list) {
                System.out.println(c.getId() + ". " + c.getName() + " - " + c.getDescription());
            }
        }
    }

    public boolean checkIdExists(int categoryId) {
        if (categoryId <= 0) return false;
        return dao.findById(categoryId);
    }

}
