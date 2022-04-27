package BddPackage;

import Models.Store;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StoreOperation extends BDD<Store> {

    @Override
    public boolean insert(Store o) {
        boolean ins = false;
        String query = "INSERT INTO المخزن (الاسم, العنوان) VALUES (?,?)";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,o.getName());
            preparedStmt.setString(2,o.getAddress());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ins;
    }

    @Override
    public boolean update(Store o1, Store o2) {
        boolean upd = false;
        String query = "UPDATE المخزن SET الاسم = ?, العنوان = ? WHERE المعرف = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,o1.getName());
            preparedStmt.setString(2,o1.getAddress());
            preparedStmt.setInt(3,o2.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return upd;
    }

    @Override
    public boolean delete(Store o) {
        return false;
    }

    @Override
    public boolean isExist(Store o) {
        return false;
    }

    @Override
    public ArrayList<Store> getAll() {
        ArrayList<Store> list = new ArrayList<>();
        String query = "SELECT * FROM  المخزن WHERE ارشيف = 0;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Store store = new Store();
                store.setId(resultSet.getInt("المعرف"));
                store.setName(resultSet.getString("الاسم"));
                store.setAddress(resultSet.getString("العنوان"));

                list.add(store);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean AddToArchive(Store store){
        boolean upd = false;
        String query = "UPDATE المخزن SET ارشيف = 1 WHERE المعرف = ?; ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,store.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return upd;
    }

    public boolean DeleteFromArchive(Store store){
        boolean upd = false;
        String query = "UPDATE المخزن SET ارشيف = 0 WHERE المعرف = ?; ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,store.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return upd;
    }

    public ArrayList<Store> getAllArchive() {
        ArrayList<Store> list = new ArrayList<>();
        String query = "SELECT * FROM  المخزن WHERE ارشيف = 1;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Store store = new Store();
                store.setId(resultSet.getInt("المعرف"));
                store.setName(resultSet.getString("الاسم"));
                store.setAddress(resultSet.getString("العنوان"));

                list.add(store);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
