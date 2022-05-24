package BddPackage;

import Models.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComponentReceiptMedicationOperation extends BDD<ComponentReceipt> {

    @Override
    public boolean insert(ComponentReceipt o) {
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
        return ins;
    }

    @Override
    public boolean update(ComponentReceipt o1, ComponentReceipt o2) {
        return false;
    }

    @Override
    public boolean delete(ComponentReceipt o) {
        return false;
    }

    @Override
    public boolean isExist(ComponentReceipt o) {
        return false;
    }

    @Override
    public ArrayList<ComponentReceipt> getAll() {
        return null;
    }

    public ArrayList<ComponentReceipt> getAllByReceipt(int idReceipt) {
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
        return list;
    }
}