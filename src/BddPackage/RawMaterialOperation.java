package BddPackage;

import Models.RawMaterial;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RawMaterialOperation extends BDD<RawMaterial> {

    @Override
    public boolean insert(RawMaterial o) {
        boolean ins = false;
        String query = "INSERT INTO المواد_الخام (الاسم, المرجع, \"اقل كمية\") VALUES (?,?,?)";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,o.getName());
            preparedStmt.setString(2,o.getReference());
            preparedStmt.setInt(3,o.getLimitQte());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ins;
    }

    @Override
    public boolean update(RawMaterial o1, RawMaterial o2) {
        boolean upd = false;
        String query = "UPDATE المواد_الخام SET الاسم = ?, المرجع = ?, \"اقل كمية\" = ? WHERE المعرف = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,o1.getName());
            preparedStmt.setString(2,o1.getReference());
            preparedStmt.setInt(3,o1.getLimitQte());
            preparedStmt.setInt(4,o2.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return upd;
    }

    @Override
    public boolean delete(RawMaterial o) {
        return false;
    }

    @Override
    public boolean isExist(RawMaterial o) {
        return false;
    }

    @Override
    public ArrayList<RawMaterial> getAll() {
        ArrayList<RawMaterial> list = new ArrayList<>();
        String query = "SELECT *,(SELECT SUM(تخزين_المواد_الخام.كمية_مخزنة) FROM تخزين_المواد_الخام WHERE تخزين_المواد_الخام.معرف_المادة_الخام = المواد_الخام.المعرف) AS الكمية_المخزنة\n" +
                "      ,(SELECT SUM(تخزين_المواد_الخام.كمية_مستهلكة) FROM تخزين_المواد_الخام WHERE تخزين_المواد_الخام.معرف_المادة_الخام = المواد_الخام.المعرف) AS الكمية_المستهلكة\n" +
                "       FROM  المواد_الخام  WHERE ارشيف = 0;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                RawMaterial rawMaterial = new RawMaterial();
                rawMaterial.setId(resultSet.getInt("المعرف"));
                rawMaterial.setName(resultSet.getString("الاسم"));
                rawMaterial.setReference(resultSet.getString("المرجع"));
                rawMaterial.setLimitQte(resultSet.getInt("اقل كمية"));
                rawMaterial.setQte(resultSet.getInt("الكمية_المخزنة") - resultSet.getInt("الكمية_المستهلكة"));

                list.add(rawMaterial);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public RawMaterial get(int id) {
        RawMaterial rawMaterial = new RawMaterial();
        String query = "SELECT *,(SELECT SUM(تخزين_المواد_الخام.كمية_مخزنة) FROM تخزين_المواد_الخام WHERE تخزين_المواد_الخام.معرف_المادة_الخام = المواد_الخام.المعرف) AS الكمية_المخزنة\n" +
                "      ,(SELECT SUM(تخزين_المواد_الخام.كمية_مستهلكة) FROM تخزين_المواد_الخام WHERE تخزين_المواد_الخام.معرف_المادة_الخام = المواد_الخام.المعرف) AS الكمية_المستهلكة\n" +
                "       FROM  المواد_الخام  WHERE ارشيف = 0 AND المعرف = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            if (resultSet.next()){


                rawMaterial.setId(resultSet.getInt("المعرف"));
                rawMaterial.setName(resultSet.getString("الاسم"));
                rawMaterial.setReference(resultSet.getString("المرجع"));
                rawMaterial.setLimitQte(resultSet.getInt("اقل كمية"));
                rawMaterial.setQte(resultSet.getInt("الكمية_المخزنة") - resultSet.getInt("الكمية_المستهلكة"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rawMaterial;
    }

    public boolean AddToArchive(RawMaterial rawMaterial){
        boolean upd = false;
        String query = "UPDATE المواد_الخام SET ارشيف = 1 WHERE المعرف = ?; ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,rawMaterial.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return upd;
    }

    public boolean DeleteFromArchive(RawMaterial rawMaterial){
        boolean upd = false;
        String query = "UPDATE المواد_الخام SET ارشيف = 0 WHERE المعرف = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,rawMaterial.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return upd;
    }

    public ArrayList<RawMaterial> getAllArchive() {
        ArrayList<RawMaterial> list = new ArrayList<>();
        String query = "SELECT * FROM `المواد_الخام` WHERE `ارشيف` = 1";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                RawMaterial rawMaterial = new RawMaterial();
                rawMaterial.setId(resultSet.getInt("المعرف"));
                rawMaterial.setName(resultSet.getString("الاسم"));
                rawMaterial.setReference(resultSet.getString("المرجع"));
                rawMaterial.setLimitQte(resultSet.getInt("اقل كمية"));

                list.add(rawMaterial);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
