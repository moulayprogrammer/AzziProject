package BddPackage;

import Models.ComponentReceipt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComponentReceiptRawMaterialOperation extends BDD<ComponentReceipt> {

    @Override
    public boolean insert(ComponentReceipt o) {
        boolean ins = false;
        String query = "INSERT INTO مشتريات_مواد_خام (معرف_الفاتورة,معرف_المادة_الخام,الكمية,سعر_الوحدة) VALUES (?,?,?,?);";
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
        boolean upd = false;
        String query = "UPDATE مشتريات_مواد_خام SET الكمية = ? , سعر_الوحدة = ? WHERE معرف_الفاتورة = ? AND معرف_المادة_الخام = ?;";
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
        return upd;
    }

    @Override
    public boolean delete(ComponentReceipt o) {
        boolean del = false;
        String query = "DELETE FROM مشتريات_مواد_خام WHERE معرف_الفاتورة = ? AND معرف_المادة_الخام = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdReceipt());
            preparedStmt.setInt(2,o.getIdComponent());

            int update = preparedStmt.executeUpdate();
            if(update != -1) del = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return del;
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
        String query = "SELECT * FROM مشتريات_مواد_خام WHERE  معرف_الفاتورة = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idReceipt);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                ComponentReceipt componentReceipt = new ComponentReceipt();
                componentReceipt.setIdReceipt(resultSet.getInt("معرف_الفاتورة"));
                componentReceipt.setIdComponent(resultSet.getInt("معرف_المادة_الخام"));
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
