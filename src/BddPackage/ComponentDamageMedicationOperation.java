package BddPackage;

import Models.ComponentDamage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComponentDamageMedicationOperation extends BDD<ComponentDamage> {

    @Override
    public boolean insert(ComponentDamage o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO الادوية_التالفة (معرف_التلف, معرف_الدواء, معرف_التوصيل, الكمية) VALUES (?,?,?,?);";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdDamage());
            preparedStmt.setInt(2,o.getIdComponent());
            preparedStmt.setInt(3,o.getIdReference());
            preparedStmt.setInt(4,o.getQte());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(ComponentDamage o1, ComponentDamage o2) {
        return false;
    }

    public boolean updateQte(ComponentDamage o){
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE الادوية_التالفة SET   الكمية = ? WHERE معرف_التلف = ? AND معرف_الدواء = ? AND معرف_التوصيل = ? ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getQte());
            preparedStmt.setInt(2,o.getIdDamage());
            preparedStmt.setInt(3,o.getIdComponent());
            preparedStmt.setInt(4,o.getIdReference());

            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    @Override
    public boolean delete(ComponentDamage o) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM الادوية_التالفة WHERE معرف_التلف = ? AND معرف_الدواء = ? AND معرف_التوصيل = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdDamage());
            preparedStmt.setInt(2,o.getIdComponent());
            preparedStmt.setInt(3,o.getIdReference());

            int update = preparedStmt.executeUpdate();
            if(update != -1) del = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return del;
    }

    @Override
    public boolean isExist(ComponentDamage o) {
        return false;
    }

    @Override
    public ArrayList<ComponentDamage> getAll() {
        connectDatabase();
        ArrayList<ComponentDamage> list = new ArrayList<>();
        String query = "SELECT * FROM الادوية_التالفة";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                ComponentDamage componentDamage = new ComponentDamage();
                componentDamage.setIdDamage(resultSet.getInt("معرف_التلف"));
                componentDamage.setIdComponent(resultSet.getInt("معرف_الدواء"));
                componentDamage.setIdReference(resultSet.getInt("معرف_التوصيل"));
                componentDamage.setQte(resultSet.getInt("الكمية"));

                list.add(componentDamage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public ArrayList<ComponentDamage> getAllByDamage(int idDamage) {
        connectDatabase();
        ArrayList<ComponentDamage> list = new ArrayList<>();
        String query = "SELECT * FROM الادوية_التالفة WHERE معرف_التلف = ?";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idDamage);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                ComponentDamage componentDamage = new ComponentDamage();
                componentDamage.setIdDamage(resultSet.getInt("معرف_التلف"));
                componentDamage.setIdComponent(resultSet.getInt("معرف_الدواء"));
                componentDamage.setIdReference(resultSet.getInt("معرف_التوصيل"));
                componentDamage.setQte(resultSet.getInt("الكمية"));

                list.add(componentDamage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
}
