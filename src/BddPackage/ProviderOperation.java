package BddPackage;

import Models.Client;
import Models.Provider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProviderOperation extends BDD<Provider> {

    @Override
    public boolean insert(Provider o) {
        boolean ins = false;
        String query = "INSERT INTO المورد (الاسم, العنوان, النشاط, الرقم_الوطني) VALUES (?,?,?,?)";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,o.getName());
            preparedStmt.setString(2,o.getAddress());
            preparedStmt.setString(3,o.getActivity());
            preparedStmt.setString(4,o.getNationalNumber());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ins;
    }

    @Override
    public boolean update(Provider o1, Provider o2) {
        boolean upd = false;
        String query = "UPDATE المورد SET الاسم = ?, العنوان = ?, النشاط = ?, الرقم_الوطني = ? WHERE المعرف = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,o1.getName());
            preparedStmt.setString(2,o1.getAddress());
            preparedStmt.setString(3,o1.getActivity());
            preparedStmt.setString(4,o1.getNationalNumber());
            preparedStmt.setInt(5,o2.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                provider.setActivity(resultSet.getString("النشاط"));
                provider.setNationalNumber(resultSet.getString("الرقم_الوطني"));

                list.add(provider);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Provider get(int id) {
        Provider provider = new Provider();
        String query = "SELECT * FROM المورد WHERE المعرف = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            if (resultSet.next()){

                provider.setId(resultSet.getInt("المعرف"));
                provider.setName(resultSet.getString("الاسم"));
                System.out.println("p  =  " + resultSet.getString("الاسم") );
                provider.setAddress(resultSet.getString("العنوان"));
                provider.setActivity(resultSet.getString("النشاط"));
                provider.setNationalNumber(resultSet.getString("الرقم_الوطني"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return provider;
    }

    public boolean AddToArchive(Provider provider){
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
        return upd;
    }

    public boolean DeleteFromArchive(Provider provider){
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
        return upd;
    }

    public ArrayList<Provider> getAllArchive() {
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
                provider.setActivity(resultSet.getString("النشاط"));
                provider.setNationalNumber(resultSet.getString("الرقم_الوطني"));

                list.add(provider);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
