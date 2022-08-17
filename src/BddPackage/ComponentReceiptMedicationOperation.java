package BddPackage;

import Models.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComponentReceiptMedicationOperation extends BDD<ComponentReceipt> {

    @Override
    public boolean insert(ComponentReceipt o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO مشتريات_الدواء (معرف_الفاتورة,معرف_الدواء,الكمية,سعر_الوحدة) VALUES (?,?,?,?);";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdReceipt());
            preparedStmt.setInt(2,o.getIdComponent());
            preparedStmt.setInt(3,o.getQte());
            preparedStmt.setDouble(4,o.getPrice());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(ComponentReceipt o1, ComponentReceipt o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE مشتريات_الدواء SET الكمية = ? , سعر_الوحدة = ? WHERE معرف_الفاتورة = ? AND معرف_الدواء = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getQte());
            preparedStmt.setDouble(2,o1.getPrice());
            preparedStmt.setInt(3,o1.getIdReceipt());
            preparedStmt.setInt(4,o2.getIdComponent());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    @Override
    public boolean delete(ComponentReceipt o) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM مشتريات_الدواء WHERE معرف_الفاتورة = ? AND معرف_الدواء = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdReceipt());
            preparedStmt.setInt(2,o.getIdComponent());

            int update = preparedStmt.executeUpdate();
            if(update != -1) del = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return del;
    }

    @Override
    public boolean isExist(ComponentReceipt o) {
        return false;
    }

    @Override
    public ArrayList<ComponentReceipt> getAll() {
        connectDatabase();
        ArrayList<ComponentReceipt> list = new ArrayList<>();
        String query = "SELECT * FROM مشتريات_الدواء ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                ComponentReceipt componentReceipt = new ComponentReceipt();
                componentReceipt.setIdReceipt(resultSet.getInt("معرف_الفاتورة"));
                componentReceipt.setIdComponent(resultSet.getInt("معرف_الدواء"));
                componentReceipt.setQte(resultSet.getInt("الكمية"));
                componentReceipt.setPrice(resultSet.getDouble("سعر_الوحدة"));

                list.add(componentReceipt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public ArrayList<ComponentReceipt> getAllByReceipt(int idReceipt) {
        connectDatabase();
        ArrayList<ComponentReceipt> list = new ArrayList<>();
        String query = "SELECT * FROM مشتريات_الدواء WHERE  معرف_الفاتورة = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idReceipt);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                ComponentReceipt componentReceipt = new ComponentReceipt();
                componentReceipt.setIdReceipt(resultSet.getInt("معرف_الفاتورة"));
                componentReceipt.setIdComponent(resultSet.getInt("معرف_الدواء"));
                componentReceipt.setQte(resultSet.getInt("الكمية"));
                componentReceipt.setPrice(resultSet.getDouble("سعر_الوحدة"));

                list.add(componentReceipt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
}
