package service;
import dao.Reviewdao;
import model.Review;
import java.util.*;

public class ReviewService {
    Reviewdao dao=new Reviewdao();
    public void add(Review r)
    {
        if (dao.isAlreadyReviewed(r.getUserId(), r.getShopId()))
        {
            System.out.println("Loi: Ban da danh gia cua hang nay roi!");
            return;
        }
        if(r.getRating()<1 || r.getRating()>5)
        {
            System.out.println("Rating phai tu 1 den 5");
            return;
        }
        if(r.getComment()==null || r.getComment().trim().isEmpty())
        {
            System.out.println("Vui long nhap danh gia");
            return;
        }
        dao.add(r);
    }
    public List<Review> getbyShopId(int shopId)
    {
        return dao.getAll(shopId);
    }
    public void update(Review r)
    {
        dao.upadte(r);
    }
    public void deleteR(int id)
    {
        dao.delete(id);
    }
    public boolean isReviewOwner(int reviewId, int userId)
    {
        return dao.isReviewOwner(reviewId, userId);
    }
}