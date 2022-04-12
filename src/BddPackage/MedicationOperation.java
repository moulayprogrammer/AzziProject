package BddPackage;

import Models.Medication;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MedicationOperation extends BDD<Medication> {

    @Override
    public boolean insert(Medication o) {
        boolean ins = false;
        String query = "INSERT INTO `الادوية`( `الاسم`, `المرجع`, `اقل_كمية`) VALUES  (?,?,?)";
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
    public boolean update(Medication o1, Medication o2) {
        boolean upd = false;
        String query = "UPDATE `الادوية` SET `الاسم`= ?,`المرجع`= ?,`اقل_كمية`= ? " +
                "WHERE `المعرف` = ? ";
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
    public boolean delete(Medication o) {
        return false;
    }

    @Override
    public boolean isExist(Medication o) {
        return false;
    }

    @Override
    public ArrayList<Medication> getAll() {
        ArrayList<Medication> list = new ArrayList<>();
        String query = "SELECT * FROM `الادوية` WHERE `ارشيف` = 0";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Medication medication = new Medication();
                medication.setId(resultSet.getInt("المعرف"));
                medication.setName(resultSet.getString("الاسم"));
                medication.setReference(resultSet.getString("المرجع"));
                medication.setLimitQte(resultSet.getInt("اقل_كمية"));

                list.add(medication);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean AddToArchive(Medication rawMaterial){
        boolean upd = false;
        String query = "UPDATE `الادوية` SET `ارشيف`= 1 WHERE `المعرف` = ? ";
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

    public boolean DeleteFromArchive(Medication rawMaterial){
        boolean upd = false;
        String query = "UPDATE `الادوية` SET `ارشيف`= 0 WHERE `المعرف` = ? ";
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

    public ArrayList<Medication> getAllArchive() {
        ArrayList<Medication> list = new ArrayList<>();
        String query = "SELECT * FROM `الادوية` WHERE `ارشيف` = 1";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Medication medication = new Medication();
                medication.setId(resultSet.getInt("المعرف"));
                medication.setName(resultSet.getString("الاسم"));
                medication.setReference(resultSet.getString("المرجع"));
                medication.setLimitQte(resultSet.getInt("اقل_كمية"));

                list.add(medication);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
