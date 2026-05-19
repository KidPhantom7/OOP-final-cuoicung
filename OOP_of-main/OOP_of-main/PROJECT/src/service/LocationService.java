package service;

import java.util.*;
import model.Location;
import dao.Locationdao;

public class LocationService
{
    Locationdao locatedao = new Locationdao();

    public void add(Location locate)
    {
        if (locatedao.isLocationExists(locate.getUserId(), locate.getDetail().trim()))
            {
                System.out.println("Loi: Dia chi nay ban da luu truoc do roi!");
                return;
            }
        if (locate.getDetail() == null || locate.getDetail().trim().isEmpty())
        {
            System.out.println("Dia chi khong hop le");
            return;
        }
        // Dinh dang SDT: bat dau bang 0, theo sau la chu so, khong phai toan so 0
        String phone = locate.getPhone() == null ? "" : locate.getPhone().trim();
        boolean validPhone = phone.matches("0\\d{8,11}") && !phone.matches("0+");
        if (!validPhone)
        {
            System.out.println("So dien thoai khong hop le!");
            System.out.println("  Yeu cau: Bat dau bang 0, theo sau la chu so (VD: 0912345678)");
            return;
        }
        locatedao.AddLocate(locate);
    }
    public List<Location> getAll(int userId)
    {
        return locatedao.getAllLocate(userId);
    }
    public void updateLocate(Location locate)
    {
        locatedao.updateLocate(locate);
    }
    public void delteLocate(int id)
    {
        locatedao.deleteLocate(id);
    }
    public boolean isLocationOwner(int locationId, int userId)
    {
        return locatedao.isLocationOwner(locationId, userId);
    }
}