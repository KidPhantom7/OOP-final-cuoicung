import model.*;
import service.*;
import util.SessionManager;
import java.util.*;

public class Main
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        AuthService authService = new AuthService();
        ShopService shopservice = new ShopService();
        ReviewService reviewService = new ReviewService();
        LocationService locateService = new LocationService();
        CartService cartService = new CartService();
        OrderService orderService = new OrderService();
        PaymentService paymentService = new PaymentService();
        CategoryService categoryService = new CategoryService();
        ProductService productService = new ProductService();
        UserService userService = new UserService();

        // === BUOC 1: DANG NHAP / DANG KY ===
        if (!handleAuth(sc, authService)) {
            System.out.println("Thoat chuong trinh.");
            return;
        }

        int myUserId = SessionManager.getCurrentUserId();
        String role = SessionManager.getCurrentUser().getRole();

        // === BUOC 2: PHAN LUONG THEO ROLE ===
        if ("ADMIN".equalsIgnoreCase(role)) {
            runAdminMenu(sc, userService);
        } else if ("SELLER".equalsIgnoreCase(role)) {
            runSellerMenu(sc, shopservice, orderService, paymentService,
                          categoryService, productService, myUserId);
        } else {
            runCustomerMenu(sc, cartService, orderService, paymentService,
                            locateService, reviewService, productService,
                            categoryService, myUserId);
        }
    }

    // ================================================================
    //  MENU DANH RIENG CHO CUSTOMER (Nguoi mua)
    // ================================================================
    private static void runCustomerMenu(Scanner sc, CartService cartService,
                                        OrderService orderService, PaymentService paymentService,
                                        LocationService locateService, ReviewService reviewService,
                                        ProductService productService, CategoryService categoryService,
                                        int myUserId) {
        while (true) {
            System.out.println("\n===== GOC NGUOI MUA =====");
            System.out.println("Xin chao, " + SessionManager.getCurrentUser().getFullName() + "!");
            System.out.println("1. Quan ly gio hang & Chot don");
            System.out.println("2. Xem lich su mua hang & Bien lai");
            System.out.println("3. Quan ly danh gia (Review)");
            System.out.println("4. Quan ly dia chi nhan hang");
            System.out.println("5. Tim kiem & Loc san pham");
            System.out.println("0. Dang xuat & Thoat");
            System.out.print("Chon chuc nang: ");
            int choice = inputInt(sc);
            switch (choice) {
                case 1:
                    manageCartAndCheckout(sc, cartService, orderService, paymentService,
                                         locateService, productService, myUserId);
                    break;
                case 2:
                    viewBuyerHistory(orderService, paymentService, myUserId);
                    break;
                case 3:
                    manageReviews(sc, reviewService, myUserId);
                    break;
                case 4:
                    manageLocations(sc, locateService, myUserId);
                    break;
                case 5:
                    searchProducts(sc, productService, categoryService);
                    break;
                case 0:
                    System.out.println("Da dang xuat. Tam biet!");
                    return;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        }
    }

    // ================================================================
    //  MENU DANH RIENG CHO ADMIN (Quan tri nguoi dung)
    // ================================================================
    private static void runAdminMenu(Scanner sc, UserService userService) {
        while (true) {
            System.out.println("\n===== TRANG QUAN TRI (ADMIN) =====");
            System.out.println("Xin chao, " + SessionManager.getCurrentUser().getFullName() + "!");
            System.out.println("1. Xem danh sach nguoi ban (SELLER)");
            System.out.println("2. Xem danh sach nguoi mua (CUSTOMER)");
            System.out.println("3. Xoa tai khoan nguoi ban (SELLER)");
            System.out.println("4. Xoa tai khoan nguoi mua (CUSTOMER)");
            System.out.println("0. Dang xuat & Thoat");
            System.out.print("Chon chuc nang: ");
            int choice = inputInt(sc);
            switch (choice) {
                case 1: {
                    List<User> sellers = userService.getAllSellers();
                    if (sellers.isEmpty()) {
                        System.out.println("Khong co nguoi ban nao trong he thong.");
                    } else {
                        System.out.println("\n--- DANH SACH NGUOI BAN ---");
                        for (User u : sellers) {
                            System.out.printf("ID: %d | Username: %s | Email: %s | Ho ten: %s\n", 
                                    u.getId(), u.getUsername(), u.getEmail(), u.getFullName());
                        }
                    }
                    break;
                }
                case 2: {
                    List<User> customers = userService.getAllCustomers();
                    if (customers.isEmpty()) {
                        System.out.println("Khong co nguoi mua nao trong he thong.");
                    } else {
                        System.out.println("\n--- DANH SACH NGUOI MUA ---");
                        for (User u : customers) {
                            System.out.printf("ID: %d | Username: %s | Email: %s | Ho ten: %s\n", 
                                    u.getId(), u.getUsername(), u.getEmail(), u.getFullName());
                        }
                    }
                    break;
                }
                case 3: {
                    System.out.print("Nhap ID nguoi ban muon xoa: ");
                    int id = inputInt(sc);
                    userService.deleteUser(id);
                    break;
                }
                case 4: {
                    System.out.print("Nhap ID nguoi mua muon xoa: ");
                    int id = inputInt(sc);
                    userService.deleteUser(id);
                    break;
                }
                case 0:
                    System.out.println("Da dang xuat. Tam biet!");
                    return;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        }
    }

    // ================================================================
    //  MENU DANH RIENG CHO SELLER (Nguoi ban)
    // ================================================================
    private static void runSellerMenu(Scanner sc, ShopService shopservice,
                                      OrderService orderService, PaymentService paymentService,
                                      CategoryService categoryService, ProductService productService,
                                      int myUserId) {
        while (true) {
            System.out.println("\n===== GOC NGUOI BAN (SELLER) =====");
            System.out.println("Xin chao, " + SessionManager.getCurrentUser().getFullName() + "!");
            System.out.println("1. Quan ly gian hang (Shop)");
            System.out.println("2. Xem tat ca don hang");
            System.out.println("3. Cap nhat trang thai don hang");
            System.out.println("4. Xem bien lai thanh toan");
            System.out.println("5. Quan ly danh muc nganh hang");
            System.out.println("6. Quan ly san pham");
            System.out.println("0. Dang xuat & Thoat");
            System.out.print("Chon chuc nang: ");
            int choice = inputInt(sc);
            switch (choice) {
                case 1:
                    manageShops(sc, shopservice, myUserId);
                    break;
                case 2:
                    orderService.viewAllOrders();
                    break;
                case 3: {
                    orderService.viewAllOrders();
                    System.out.print("Nhap Ma don hang can cap nhat: ");
                    int orderId = inputInt(sc);
                    String currentStatus = orderService.getOrderStatus(orderId);
                    if (currentStatus == null) {
                        System.out.println("Loi: Khong tim thay don hang #" + orderId);
                        break;
                    }
                    System.out.println("=> Trang thai hien tai: " + currentStatus);

                    // Enforce flow: Cho xac nhan -> Dang giao -> Hoan thanh
                    if ("Cho xac nhan".equals(currentStatus)) {
                        System.out.println("1. Xac nhan don -> Dang giao hang");
                        System.out.println("2. Huy don hang");
                        System.out.print("Lua chon: ");
                        int sub = inputInt(sc);
                        if (sub == 1) {
                            orderService.updateStatus(orderId, "Dang giao hang");
                        } else if (sub == 2) {
                            orderService.updateStatus(orderId, "Da huy");
                        } else {
                            System.out.println("Lua chon khong hop le!");
                        }
                    } else if ("Dang giao hang".equals(currentStatus)) {
                        System.out.println("1. Xac nhan da giao -> Hoan thanh");
                        System.out.print("Lua chon (1 de xac nhan): ");
                        int sub = inputInt(sc);
                        if (sub == 1) {
                            orderService.updateStatus(orderId, "Hoan thanh");
                            // Neu COD -> tu dong cap nhat thanh toan
                            String payMethod = paymentService.getPaymentMethodByOrderId(orderId);
                            if ("COD".equals(payMethod)) {
                                paymentService.updatePaymentStatusByOrderId(orderId, "Da thanh toan");
                                System.out.println("=> Don COD - tu dong cap nhat thanh toan thanh: Da thanh toan");
                            }
                        } else {
                            System.out.println("Lua chon khong hop le!");
                        }
                    } else if ("Hoan thanh".equals(currentStatus)) {
                        System.out.println("Don hang nay da hoan thanh, khong the thay doi!");
                    } else if ("Da huy".equals(currentStatus)) {
                        System.out.println("Don hang nay da bi huy, khong the thay doi!");
                    } else {
                        System.out.println("Trang thai hien tai khong hop le de cap nhat!");
                    }
                    break;
                }
                case 4: {
                    paymentService.viewReceipts();
                    break;
                }
                case 5:
                    manageCategories(sc, categoryService);
                    break;
                case 6:
                    manageProducts(sc, productService, categoryService, shopservice, myUserId);
                    break;
                case 0:
                    System.out.println("Da dang xuat. Tam biet!");
                    return;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        }
    }


    private static void manageShops(Scanner sc, ShopService shopservice, int myUserId) {
        while (true)
        {
            System.out.println("\n=== Quan ly gian hang cua ban ===");
            System.out.println("1. Them 1 shop moi");
            System.out.println("2. Xem shop cua toi");
            System.out.println("3. Cap nhat shop");
            System.out.println("4. Dong cua shop");
            System.out.println("5. Xoa shop");
            System.out.println("6. Mo lai shop");
            System.out.println("0. Thoat");
            System.out.print("Lua chon: ");
            int option = sc.nextInt();
            sc.nextLine();
            if (option == 0)
                break;
            switch (option) {
                case 1:
                    if (!shopservice.getByUserId(myUserId).isEmpty()) {
                        System.out.println("Loi: Ban da co 1 gian hang. Moi nguoi ban chi duoc phep quan ly toi da 1 gian hang duy nhat!");
                        break;
                    }
                    System.out.print("Ten shop: ");
                    String name = sc.nextLine();
                    System.out.print("Nhap mo ta: ");
                    String mota = sc.nextLine();
                    System.out.print("Nhap dia chi: ");
                    String address = sc.nextLine();
                    Shop s = new Shop(myUserId, name, mota, address);
                    shopservice.createShop(s);
                    break;
                case 2:
                    System.out.println("Danh sach shop cua ban (ID=" + myUserId + "): ");
                    shopservice.getByUserId(myUserId)
                            .forEach(shop -> System.out
                                    .println(shop.getShopId() + " - " + shop.getName() + " - " + shop.getStatus()));
                    break;
                case 3: {
                    System.out.print("Nhap id Shop can sua: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    if (!shopservice.isOwner(id, myUserId)) {
                        System.out.println("Loi: Ban khong co quyen sua shop nay!");
                        break;
                    }
                    System.out.print("Mo ta moi: ");
                    String desc = sc.nextLine();
                    System.out.print("Dia chi moi: ");
                    String adr = sc.nextLine();
                    Shop update = new Shop();
                    update.setShopId(id);
                    update.setDescription(desc);
                    update.setAddress(adr);
                    shopservice.update(update);
                    break;
                }
                case 4: {
                    System.out.print("Nhap id Shop can dong: ");
                    int closed = sc.nextInt();
                    sc.nextLine();
                    if (!shopservice.isOwner(closed, myUserId)) {
                        System.out.println("Loi: Ban khong co quyen dong shop nay!");
                        break;
                    }
                    shopservice.close(closed);
                    break;
                }
                case 5: {
                    System.out.print("Nhap id Shop can xoa: ");
                    int idremove = sc.nextInt();
                    sc.nextLine();
                    if (!shopservice.isOwner(idremove, myUserId)) {
                        System.out.println("Loi: Ban khong co quyen xoa shop nay!");
                        break;
                    }
                    shopservice.remove(idremove);
                    break;
                }
                case 6: {
                    System.out.print("Nhap id shop can mo lai: ");
                    int reopenId = sc.nextInt();
                    sc.nextLine();
                    if (!shopservice.isOwner(reopenId, myUserId)) {
                        System.out.println("Loi: Ban khong co quyen mo lai shop nay!");
                        break;
                    }
                    shopservice.reopen(reopenId);
                    System.out.println("Mo lai shop thanh cong!");
                    break;
                }
            }
        }
    }

    private static void manageReviews(Scanner sc, ReviewService reviewService, int myUserId) {
        while (true) {
            System.out.println("\n=== Quan ly danh gia (ID cua ban: " + myUserId + ") ===");
            System.out.println("1. Them danh gia moi");
            System.out.println("2. Xem danh gia cua shop");
            System.out.println("3. Sua danh gia cua toi");
            System.out.println("4. Xoa danh gia cua toi");
            System.out.println("0. Thoat");
            System.out.print("Lua chon: ");
            int option2 = sc.nextInt();
            sc.nextLine();
            if (option2 == 0)
                break;
            switch (option2) {
                case 1: {
                    System.out.print("Shop ID: ");
                    int shopId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Danh gia (tu 1->5): ");
                    int rating = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Comment: ");
                    String com = sc.nextLine();
                    // Tu dong dung myUserId, khong cho nhap tay
                    Review r = new Review(myUserId, shopId, rating, com);
                    reviewService.add(r);
                    break;
                }
                case 2: {
                    System.out.print("Nhap id shop can xem danh gia: ");
                    int sid = sc.nextInt();
                    sc.nextLine();
                    reviewService.getbyShopId(sid).forEach(rv -> System.out
                            .println("ReviewID: " + rv.getReviewId() + " | UserID: " + rv.getUserId()
                                    + " | " + rv.getRating() + "* | " + rv.getComment()));
                    break;
                }
                case 3: {
                    System.out.print("Id review can sua: ");
                    int rid = sc.nextInt();
                    sc.nextLine();
                    if (!reviewService.isReviewOwner(rid, myUserId)) {
                        System.out.println("Loi: Ban chi co the sua danh gia cua chinh minh!");
                        break;
                    }
                    System.out.print("Rating moi: ");
                    int newrate = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Comment moi: ");
                    String newcom = sc.nextLine();
                    Review reviewmoi = new Review();
                    reviewmoi.setReviewId(rid);
                    reviewmoi.setUserId(myUserId);
                    reviewmoi.setRating(newrate);
                    reviewmoi.setComment(newcom);
                    reviewService.update(reviewmoi);
                    break;
                }
                case 4: {
                    System.out.print("Nhap Id review muon xoa: ");
                    int iddelete = sc.nextInt();
                    sc.nextLine();
                    if (!reviewService.isReviewOwner(iddelete, myUserId)) {
                        System.out.println("Loi: Ban chi co the xoa danh gia cua chinh minh!");
                        break;
                    }
                    reviewService.deleteR(iddelete);
                    break;
                }
            }
        }
    }

    private static void manageLocations(Scanner sc, LocationService locateService, int myUserId) {
        while (true)
        {
            System.out.println("\n=== Quan ly dia chi (ID cua ban: " + myUserId + ") ===");
            System.out.println("1. Them dia chi moi");
            System.out.println("2. Xem dia chi cua toi");
            System.out.println("3. Thay doi dia chi");
            System.out.println("4. Xoa dia chi");
            System.out.println("0. Thoat");
            System.out.print("Lua chon: ");
            int option3 = sc.nextInt();
            sc.nextLine();
            if (option3 == 0)
                break;
            switch (option3) {
                case 1: {
                    // Tu dong dung myUserId, khong cho nhap tay
                    System.out.print("Nhap mo ta chi tiet: ");
                    String motadiachi = sc.nextLine();
                    System.out.print("Dien so dien thoai: ");
                    String sdt = sc.nextLine();
                    Location lo = new Location(myUserId, motadiachi, sdt);
                    locateService.add(lo);
                    break;
                }
                case 2: {
                    // Chi hien thi dia chi cua chinh minh
                    locateService.getAll(myUserId).forEach(location -> System.out.println(
                            "LocationID: " + location.getLocationId()
                            + " | " + location.getDetail() + " | " + location.getPhone()));
                    break;
                }
                case 3: {
                    System.out.print("Nhap id dia chi can thay doi: ");
                    int idlo = sc.nextInt();
                    sc.nextLine();
                    if (!locateService.isLocationOwner(idlo, myUserId)) {
                        System.out.println("Loi: Ban chi co the sua dia chi cua chinh minh!");
                        break;
                    }
                    System.out.print("Nhap mo ta moi: ");
                    String newdetail = sc.nextLine();
                    System.out.print("Nhap sdt moi: ");
                    String newphone = sc.nextLine();
                    Location loca = new Location();
                    loca.setLocationId(idlo);
                    loca.setUserId(myUserId);
                    loca.setPhone(newphone);
                    loca.setDetail(newdetail);
                    locateService.updateLocate(loca);
                    break;
                }
                case 4: {
                    System.out.print("Nhap id dia chi can xoa: ");
                    int iddele = sc.nextInt();
                    sc.nextLine();
                    if (!locateService.isLocationOwner(iddele, myUserId)) {
                        System.out.println("Loi: Ban chi co the xoa dia chi cua chinh minh!");
                        break;
                    }
                    locateService.delteLocate(iddele);
                    break;
                }
            }
        }
    }

    private static void manageCartAndCheckout(Scanner scanner, CartService cartService, OrderService orderService,
                                               PaymentService paymentService, LocationService locateService,
                                               ProductService productService, int myUserId) {
        boolean cartMenu = true;
        while (cartMenu) {
            System.out.println("\n=== QUAN LY GIO HANG ===");
            System.out.println("1. Them san pham vao gio");
            System.out.println("2. Xem chi tiet gio hang");
            System.out.println("3. Xoa san pham khoi gio");
            System.out.println("4. Cap nhat so luong");
            System.out.println("5. CHOT DON (Checkout) & Thanh toan");
            System.out.println("0. Quay lai");
            System.out.print("Chon chuc nang: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    // Hien thi danh sach san pham voi so luong con lai thuc te
                    java.util.List<model.Product> products = productService.getAll();
                    if (products.isEmpty()) {
                        System.out.println("Hien tai chua co san pham nao.");
                        break;
                    }
                    System.out.println("\n--- DANH SACH SAN PHAM ---");
                    for (model.Product p : products) {
                        int remaining = p.getStock() - cartService.getQuantityInCart(myUserId, p.getId());
                        String slText = remaining <= 0 ? "Het hang" : String.valueOf(remaining);
                        System.out.println("ID: " + p.getId() + " | Ten: " + p.getName()
                                + " | Gia: " + p.getPrice() + " | Con lai: " + slText
                                + " | Ma DM: " + p.getCategoryId() + " | Ma Shop: " + p.getShopId());
                    }
                    System.out.print("Nhap ID san pham muon them vao gio (bam 0 de huy): ");
                    int spId = scanner.nextInt();
                    scanner.nextLine();
                    if (spId == 0) {
                        System.out.println("Da huy them san pham.");
                        break;
                    }
                    if (!productService.checkIdExists(spId)) {
                        System.out.println("Loi: Khong tim thay san pham co ID = " + spId + "!");
                        break;
                    }
                    int stockInDB = cartService.getAvailableStock(spId);
                    int inCart = cartService.getQuantityInCart(myUserId, spId);
                    int canAdd = stockInDB - inCart;
                    if (canAdd <= 0) {
                        System.out.println("San pham nay da het hang!");
                        break;
                    }
                    System.out.print("Nhap so luong (toi da " + canAdd + "): ");
                    int qty = scanner.nextInt();
                    scanner.nextLine();
                    if (qty <= 0) {
                        System.out.println("So luong phai lon hon 0!");
                        break;
                    }
                    cartService.addToCart(new Cart(myUserId, spId, qty));
                    break;
                case 2:
                    cartService.viewCart(myUserId);
                    break;
                case 3:
                    cartService.viewCart(myUserId);
                    System.out.print("Nhap ID san pham can xoa khoi gio: ");
                    int delId = scanner.nextInt();
                    scanner.nextLine();
                    cartService.removeFromCart(myUserId, delId);
                    break;
                case 4:
                    cartService.viewCart(myUserId);
                    System.out.print("Nhap ID san pham can cap nhat so luong: ");
                    int updateId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Nhap so luong moi: ");
                    int newQty = scanner.nextInt();
                    scanner.nextLine();
                    cartService.updateQuantity(myUserId, updateId, newQty);
                    break;
                case 5:
                    System.out.println("\n--- TIEN HANH CHOT DON (CHECKOUT) ---");
                    System.out.println("Danh sach dia chi cua ban:");
                    List<Location> locs = locateService.getAll(myUserId);
                    if (locs.isEmpty()) {
                        System.out.println(
                                "Ban chua co dia chi nao! Vui long vao Menu -> Quan ly dia chi de them truoc.");
                        break;
                    }
                    for (Location l : locs) {
                        System.out.println(l.getLocationId() + " - " + l.getDetail() + " - " + l.getPhone());
                    }
                    int locId = -1;
                    while (true) {
                        System.out.print("Chon ID dia chi de giao hang: ");
                        locId = scanner.nextInt();
                        scanner.nextLine();
                        boolean found = false;
                        for (Location l : locs) {
                            if (l.getLocationId() == locId) {
                                found = true;
                                break;
                            }
                        }
                        if (found)
                            break;
                        System.out.println("ID dia chi khong hop le!");
                    }

                    double totalMoney = cartService.calculateTotalAmount(myUserId);
                    if (totalMoney <= 0) {
                        System.out.println("Gio hang trong hoac khong tinh duoc tong tien!");
                        break;
                    }
                    Order newOrder = new Order(myUserId, totalMoney, "Cho xac nhan");
                    int generatedOrderId = orderService.createOrder(newOrder);

                    if (generatedOrderId > 0) {
                        System.out.println("=> Tao don hang thanh cong! Ma don: #" + generatedOrderId);
                        List<Cart> currentCartItems = cartService.getCartItems(myUserId);
                        orderService.saveOrderDetails(generatedOrderId, currentCartItems);
                        cartService.clearCart(myUserId);

                        System.out.println("\n--- CHON PHUONG THUC THANH TOAN ---");
                        System.out.println("1. Chuyen khoan (BANK_TRANSFER)");
                        System.out.println("2. Tra tien mat khi nhan hang (COD)");
                        System.out.print("Lua chon: ");
                        int payChoice = scanner.nextInt();
                        scanner.nextLine();
                        String method = (payChoice == 1) ? "BANK_TRANSFER" : "COD";

                        Payment pay = new Payment(generatedOrderId, method, totalMoney);
                        paymentService.createPayment(pay);
                        if ("BANK_TRANSFER".equals(method)) {
                            System.out.println("=> Thanh toan chuyen khoan thanh cong! Trang thai: Da thanh toan.");
                        } else {
                            System.out.println("=> Phuong thuc COD - Thanh toan khi nhan hang.");
                            System.out.println("   Trang thai thanh toan se tu dong cap nhat khi don hoan thanh.");
                        }
                        System.out.println("=> Dat hang thanh cong! Don hang dang cho nguoi ban xac nhan.");
                    }
                    cartMenu = false;
                    break;
                case 0:
                    cartMenu = false;
                    break;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        }
    }


    private static void viewBuyerHistory(OrderService orderService, PaymentService paymentService, int myUserId) {
        System.out.println("\n--- LICH SU DON HANG CUA USER " + myUserId + " ---");
        List<Order> history = orderService.getOrderHistory(myUserId);
        if (history != null && !history.isEmpty()) {
            for (Order o : history) {
                String dateStr = (o.getOrderDate() != null)
                        ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(o.getOrderDate())
                        : "N/A";
                System.out.printf("Don hang #%d | Tong tien: %,.0f | Trang thai: %s | Ngay: %s\n",
                        o.getId(), o.getTotalAmount(), o.getStatus(), dateStr);
            }
        } else {
            System.out.println("Chua co don hang nao.");
        }

        System.out.println("\n--- BIEN LAI THANH TOAN ---");
        paymentService.viewReceiptsByUser(myUserId);
    }


    private static void manageCategories(Scanner sc, CategoryService catManager) {
        int catChoice;
        do {
            System.out.println("\n--- [MENU] QUAN LY DANH MUC NGANH HANG ---");
            System.out.println("1. Xem danh sach danh muc");
            System.out.println("2. Them moi danh muc");
            System.out.println("3. Cap nhat danh muc");
            System.out.println("4. Xoa danh muc");
            System.out.println("0. Quay lai Menu chinh");
            System.out.print("Chon chuc nang (0-4): ");
            catChoice = inputInt(sc);

            switch (catChoice) {
                case 1 -> catManager.displayAll();
                case 2 -> {
                    System.out.print("Nhap ten danh muc: ");
                    String name = sc.nextLine();
                    System.out.print("Nhap mo ta: ");
                    String desc = sc.nextLine();
                    catManager.createCategory(new Category(0, name, desc));
                }
                case 3 -> {
                    System.out.print("Nhap ID danh muc can sua: ");
                    int categoryId = inputInt(sc);
                    if (!catManager.checkIdExists(categoryId)) {
                        System.out.println("Loi: Khong tim thay danh muc co ID = " + categoryId);
                        continue;
                    }
                    System.out.print("Ten moi: ");
                    String name = sc.nextLine();
                    System.out.print("Mo ta moi: ");
                    String desc = sc.nextLine();
                    catManager.editCategory(new Category(categoryId, name, desc));
                }
                case 4 -> {
                    System.out.print("Nhap ID danh muc can xoa: ");
                    int categoryId = inputInt(sc);
                    catManager.removeCategory(categoryId);
                }
            }
        } while (catChoice != 0);
    }

    private static void manageProducts(Scanner sc, ProductService prodManager, CategoryService catManager,
                                       ShopService shopservice, int myUserId) {
        int prodChoice;
        do {
            System.out.println("\n--- [MENU] QUAN LY SAN PHAM ---");
            System.out.println("1. Xem danh sach san pham");
            System.out.println("2. Them moi san pham");
            System.out.println("3. Cap nhat san pham (Gia, so luong...)");
            System.out.println("4. Xoa san pham");
            System.out.println("0. Quay lai Menu chinh");
            System.out.print("Chon chuc nang (0-4): ");
            prodChoice = inputInt(sc);
            switch (prodChoice) {
                case 1 -> prodManager.displayAllBySeller(myUserId);
                case 2 -> {
                    System.out.print("Ten san pham: ");
                    String name = sc.nextLine();
                    while (name.trim().isEmpty() || isAllNumbers(name)) {
                        if (name.trim().isEmpty()) {
                            System.out.print("Ten khong duoc de trong, nhap lai: ");
                        } else {
                            System.out.print("Ten san pham khong duoc chi chua so, nhap lai: ");
                        }
                        name = sc.nextLine();
                    }
                    double price;
                    while (true) {
                        System.out.print("Gia ban: ");
                        price = inputDouble(sc);
                        if (price > 0)
                            break;
                        else
                            System.out.println("Gia ban phai lon hon 0! Vui long nhap lai.");
                    }

                    int qty;
                    while (true) {
                        System.out.print("So luong kho: ");
                        qty = inputInt(sc);
                        if (qty > 0)
                            break;
                        else
                            System.out.println("So luong phai lon hon 0! Vui long nhap lai.");
                    }
                    int catId;
                    while (true) {
                        System.out.print("Nhap ma danh muc (Category ID): ");
                        catId = inputInt(sc);
                        if (catManager.checkIdExists(catId))
                            break;
                        else
                            System.out.println("Ma danh muc khong ton tai! Vui long nhap lai.");
                    }
                    int shopId;
                    while (true) {
                        System.out.print("Nhap ID Gian hang cua ban (Shop ID): ");
                        shopId = inputInt(sc);
                        if (shopservice.isOwner(shopId, myUserId)) {
                            break;
                        }
                        System.out.println("ID Gian hang khong hop le hoac khong thuoc quyen so huu cua ban!");
                    }
                    prodManager.createProduct(new Product(0, name, price, qty, catId, shopId));
                }
                case 3 -> {
                    System.out.print("Nhap ID san pham can sua: ");
                    int productId = inputInt(sc);
                    if (!prodManager.isProductOwner(productId, myUserId)) {
                        System.out.println("Loi: San pham khong ton tai hoac khong thuoc quyen so huu cua ban!");
                        continue;
                    }
                    System.out.print("Ten moi: ");
                    String name = sc.nextLine();
                    System.out.print("Gia moi: ");
                    double price = inputDouble(sc);
                    System.out.print("So luong kho moi: ");
                    int qty = inputInt(sc);
                    int catId;
                    while (true) {
                        System.out.print("Nhap ma danh muc moi (Category ID): ");
                        catId = inputInt(sc);
                        if (catManager.checkIdExists(catId))
                            break;
                        else
                            System.out.println("Ma danh muc khong ton tai! Vui long nhap lai.");
                    }
                    int shopId;
                    while (true) {
                        System.out.print("Nhap ID Gian hang moi (Shop ID): ");
                        shopId = inputInt(sc);
                        if (shopservice.isOwner(shopId, myUserId)) {
                            break;
                        }
                        System.out.println("ID Gian hang khong hop le hoac khong thuoc quyen so huu cua ban!");
                    }
                    prodManager.editProduct(new Product(productId, name, price, qty, catId, shopId));
                }
                case 4 -> {
                    System.out.print("Nhap ID san pham can xoa: ");
                    int id = inputInt(sc);
                    if (!prodManager.isProductOwner(id, myUserId)) {
                        System.out.println("Loi: San pham khong ton tai hoac khong thuoc quyen so huu cua ban!");
                        break;
                    }
                    prodManager.removeProduct(id);
                }
            }
        } while (prodChoice != 0);
    }

    // --- HĂ„â€Ă¢â€Â¬M TĂ„â€Ă‚ÂCH RIĂ„â€Ă‚ÂNG: T?M KI?M VĂ„â€Ă¢â€Â¬ L?C S?N
    // PH?M ---
    private static void searchProducts(Scanner sc, ProductService prodManager, CategoryService catManager) {
        int filterChoice;
        do {
            System.out.println("\n--- CHON CHE DO LOC SAN PHAM ---");
            System.out.println("1. Tim kiem theo ten san pham");
            System.out.println("2. Loc theo khoang gia");
            System.out.println("3. Loc theo danh muc san pham");
            System.out.println("0. Tro ve");
            System.out.print("Lua chon (0-3): ");
            filterChoice = inputInt(sc);

            switch (filterChoice) {
                case 1 -> {
                    System.out.print("Nhap tu khoa ten san pham: ");
                    prodManager.searchProductsByName(sc.nextLine());
                }
                case 2 -> {
                    System.out.print("Gia toi thieu: ");
                    double min = inputDouble(sc);
                    System.out.print("Gia toi da: ");
                    double max = inputDouble(sc);
                    prodManager.filterProductsByPrice(min, max);
                }
                case 3 -> {
                    int catId;
                    while (true) {
                        System.out.print("Nhap ID danh muc de loc: ");
                        catId = inputInt(sc);
                        if (catManager.checkIdExists(catId))
                            break;
                        else
                            System.out.println("ID danh muc khong ton tai! Nhap lai.");
                    }
                    prodManager.filterProductsByCategory(catId);
                }
            }
        } while (filterChoice != 0);
    }

    private static boolean isAllNumbers(String str) {
        return str.trim().matches("-?\\d+(\\.\\d+)?");
    }

    private static int inputInt(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.print("Loi! Vui long nhap so nguyen: ");
            }
        }
    }

    private static double inputDouble(Scanner sc) {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine());
            } catch (Exception e) {
                System.out.print("Loi! Vui long nhap so thuc (VD: 15.5): ");
            }
        }
    }

    // === XU LY DANG NHAP / DANG KY ===
    private static boolean handleAuth(Scanner sc, AuthService authService) {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("   HE THONG THUONG MAI DIEN TU (TMDT)  ");
            System.out.println("========================================");
            System.out.println("1. Dang nhap");
            System.out.println("2. Dang ky tai khoan Khach hang (Customer)");
            System.out.println("3. Dang ky tai khoan Nguoi ban (Seller)");
            System.out.println("0. Thoat");
            System.out.print("Lua chon: ");
            int choice = inputInt(sc);

            switch (choice) {
                case 1: {
                    System.out.print("Ten tai khoan: ");
                    String username = sc.nextLine().trim();
                    System.out.print("Mat khau: ");
                    String password = sc.nextLine().trim();
                    if (authService.login(username, password)) {
                        String role = util.SessionManager.getCurrentUser().getRole();
                        String fullName = util.SessionManager.getCurrentUser().getFullName();
                        int loginedId = util.SessionManager.getCurrentUserId();
                        System.out.println("--------------------------------------------");
                        System.out.println("Dang nhap thanh cong! Xin chao, " + fullName + "!");
                        System.out.println("ID cua ban: " + loginedId);
                        if ("ADMIN".equalsIgnoreCase(role)) {
                            System.out.println("Vai tro: ADMIN  -> Dang vao trang quan tri...");
                        } else if ("SELLER".equalsIgnoreCase(role)) {
                            System.out.println("Vai tro: SELLER -> Dang vao goc Nguoi ban...");
                        } else {
                            System.out.println("Vai tro: CUSTOMER -> Dang vao goc Nguoi mua...");
                        }
                        System.out.println("--------------------------------------------");
                        return true;
                    }
                    break;
                }
                case 2: {
                    System.out.println("\n--- DANG KY TAI KHOAN KHACH HANG ---");
                    System.out.print("Ten tai khoan (username): ");
                    String username = sc.nextLine().trim();
                    if (username.isEmpty()) {
                        System.out.println("Ten tai khoan khong duoc de trong!");
                        break;
                    }
                    System.out.print("Mat khau (it nhat 6 ky tu): ");
                    String password = sc.nextLine().trim();
                    if (password.length() < 6) {
                        System.out.println("Mat khau phai co it nhat 6 ky tu!");
                        break;
                    }
                    System.out.print("Email: ");
                    String email = sc.nextLine().trim();
                    if (!email.contains("@") || !email.contains(".")) {
                        System.out.println("Email khong hop le!");
                        break;
                    }
                    System.out.print("Ho va ten day du: ");
                    String fullName = sc.nextLine().trim();
                    if (fullName.isEmpty()) {
                        System.out.println("Ho va ten khong duoc de trong!");
                        break;
                    }
                    // Role la CUSTOMER
                    boolean ok = authService.register(username, password, email, fullName, "CUSTOMER");
                    if (ok) {
                        System.out.println("Dang ky tai khoan Customer thanh cong! Vui long dang nhap de tiep tuc.");
                    } else {
                        System.out.println("Dang ky that bai. Vui long thu lai.");
                    }
                    break;
                }
                case 3: {
                    System.out.println("\n--- DANG KY TAI KHOAN NGUOI BAN ---");
                    System.out.print("Ten tai khoan (username): ");
                    String username = sc.nextLine().trim();
                    if (username.isEmpty()) {
                        System.out.println("Ten tai khoan khong duoc de trong!");
                        break;
                    }
                    System.out.print("Mat khau (it nhat 6 ky tu): ");
                    String password = sc.nextLine().trim();
                    if (password.length() < 6) {
                        System.out.println("Mat khau phai co it nhat 6 ky tu!");
                        break;
                    }
                    System.out.print("Email: ");
                    String email = sc.nextLine().trim();
                    if (!email.contains("@") || !email.contains(".")) {
                        System.out.println("Email khong hop le!");
                        break;
                    }
                    System.out.print("Ho va ten day du: ");
                    String fullName = sc.nextLine().trim();
                    if (fullName.isEmpty()) {
                        System.out.println("Ho va ten khong duoc de trong!");
                        break;
                    }
                    // Role la SELLER
                    boolean ok = authService.register(username, password, email, fullName, "SELLER");
                    if (ok) {
                        System.out.println("Dang ky tai khoan Seller thanh cong! Vui long dang nhap de tiep tuc.");
                    } else {
                        System.out.println("Dang ky that bai. Vui long thu lai.");
                    }
                    break;
                }
                case 0:
                    return false;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        }
    }
}