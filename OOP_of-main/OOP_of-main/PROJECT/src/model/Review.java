package model;

public class Review {
    int reviewId;
    int userId;
    int shopId;
    int rating;
    String comment;
    public Review() {}
    public Review(int userId, int shopId, int rating, String comment)
    {
        this.userId = userId;
        this.shopId = shopId;
        this.rating = rating;
        this.comment = comment;
    }
    public int getReviewId()
    {
        return reviewId;
    }
    public void setReviewId(int reviewId)
    {
        this.reviewId = reviewId;
    }
    public int getUserId()
    {
        return userId;
    }
    public void setUserId(int userId)
    {
        this.userId = userId;
    }
    public int getShopId()
    {
        return shopId;
    }
    public void setShopId(int shopId)
    {
        this.shopId = shopId;
    }
    public int getRating()
    {
        return rating;
    }
    public void setRating(int rating)
    {
        this.rating = rating;
    }
    public String getComment()
    {
        return comment;
    }
    public void setComment(String comment)
    {
        this.comment = comment;
    }
}