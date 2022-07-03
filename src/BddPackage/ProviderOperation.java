package BddPackage;

import Models.Provider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProviderOperation extends BDD<Provider> {

    @Override
    public boolean insert(Provider o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO المورد (الاسم, العنوان) VALUES (?,?)";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,o.getName());
            preparedStmt.setString(2,o.getAddress());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(Provider o1, Provider o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE المورد SET الاسم = ?, العنوان = ? WHERE المعرف = ?;";
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
        closeDatabase();
        return upd;
    }

    @Override
    public boolean delete(Provider o) {
        return false;
    }

    @Override
    public boolean isExist(Provider o) {
        return false;
    }

    @Override
    public ArrayList<Provider> getAll() {
        connectDatabase();
        ArrayList<Provider> list = new ArrayList<>();
        String query = "SELECT * FROM المورد  WHERE ارشيف = 0;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Provider provider = new Provider();
                provider.setId(resultSet.getInt("المعرف"));
                provider.setName(resultSet.getString("الاسم"));
                provider.setAddress(resultSet.getString("العنوان"));

                list.add(provider);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public Provider get(int id) {
        connectDatabase();
        Provider provider = new Provider();
        String query = "SELECT * FROM المورد WHERE المعرف = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            if (resultSet.next()){

                provider.setId(resultSet.getInt("المعرف"));
                provider.setName(resultSet.getString("الاسم"));
                provider.setAddress(resultSet.getString("العنوان"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return provider;
    }

    public boolean AddToArchive(Provider provider){
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE المورد SET ارشيف = 1 WHERE المعرف = ?; ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,provider.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    public boolean DeleteFromArchive(Provider provider){
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE المورد SET ارشيف = 0 WHERE المعرف = ?; ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,provider.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    public ArrayList<Provider> getAllArchive() {
        connectDatabase();
        ArrayList<Provider> list = new ArrayList<>();
        String query = "SELECT * FROM المورد  WHERE ارشيف = 1;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Provider provider = new Provider();
                provider.setId(resultSet.getInt("المعرف"));
                provider.setName(resultSet.getString("الاسم"));
                provider.setAddress(resultSet.getString("العنوان"));

                list.add(provider);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
}
