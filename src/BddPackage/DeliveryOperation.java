package BddPackage;

import Models.Delivery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DeliveryOperation extends BDD<Delivery> {

    @Override
    public boolean insert(Delivery o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO الموصل (الاسم, رقم_رخصة_السياقة, ترقيم_الشاحنة, ترقيم_الشاحنة_2) VALUES (?,?,?,?)";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,o.getName());
            preparedStmt.setString(2,o.getDriverLicence());
            preparedStmt.setString(3,o.getTrackNumber());
            preparedStmt.setString(4,o.getTrackNumber2());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(Delivery o1, Delivery o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE  الموصل SET  الاسم = ?, رقم_رخصة_السياقة = ?, ترقيم_الشاحنة = ?, ترقيم_الشاحنة_2 = ? WHERE المعرف = ?;;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,o1.getName());
            preparedStmt.setString(2,o1.getDriverLicence());
            preparedStmt.setString(3,o1.getTrackNumber());
            preparedStmt.setString(4,o1.getTrackNumber2());
            preparedStmt.setInt(5,o2.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    @Override
    public boolean delete(Delivery o) {
        return false;
    }

    @Override
    public boolean isExist(Delivery o) {
        return false;
    }

    @Override
    public ArrayList<Delivery> getAll() {
        connectDatabase();
        ArrayList<Delivery> list = new ArrayList<>();
        String query = "SELECT * FROM الموصل WHERE ارشيف = 0;;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Delivery delivery = new Delivery();
                delivery.setId(resultSet.getInt("المعرف"));
                delivery.setName(resultSet.getString("الاسم"));
                delivery.setDriverLicence(resultSet.getString("رقم_رخصة_السياقة"));
                delivery.setTrackNumber(resultSet.getString("ترقيم_الشاحنة"));
                delivery.setTrackNumber2(resultSet.getString("ترقيم_الشاحنة_2"));

                list.add(delivery);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public Delivery get(int id) {
        connectDatabase();
        Delivery delivery = new Delivery();
        String query = "SELECT * FROM الموصل WHERE ارشيف = 0 AND المعرف = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                delivery.setId(resultSet.getInt("المعرف"));
                delivery.setName(resultSet.getString("الاسم"));
                delivery.setDriverLicence(resultSet.getString("رقم_رخصة_السياقة"));
                delivery.setTrackNumber(resultSet.getString("ترقيم_الشاحنة"));
                delivery.setTrackNumber2(resultSet.getString("ترقيم_الشاحنة_2"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return delivery;
    }

    public boolean AddToArchive(Delivery delivery){
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE الموصل SET ارشيف = 1 WHERE المعرف = ?; ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,delivery.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    public boolean DeleteFromArchive(Delivery delivery){
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE الموصل SET ارشيف = 0 WHERE المعرف = ?; ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,delivery.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    public ArrayList<Delivery> getAllArchive() {
        connectDatabase();
        ArrayList<Delivery> list = new ArrayList<>();
        String query = "SELECT * FROM الموصل WHERE ارشيف = 1;;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Delivery delivery = new Delivery();
                delivery.setId(resultSet.getInt("المعرف"));
                delivery.setName(resultSet.getString("الاسم"));
                delivery.setDriverLicence(resultSet.getString("رقم_رخصة_السياقة"));
                delivery.setTrackNumber(resultSet.getString("ترقيم_الشاحنة"));
                delivery.setTrackNumber2(resultSet.getString("ترقيم_الشاحنة_2"));

                list.add(delivery);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
}
