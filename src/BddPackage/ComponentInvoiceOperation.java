package BddPackage;

import Models.ComponentInvoice;
import Models.ComponentReceipt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComponentInvoiceOperation extends BDD<ComponentInvoice> {

    @Override
    public boolean insert(ComponentInvoice o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO بيع_منتج (معرف_الفاتورة, معرف_المنتج, الكمية, سعر_الوحدة, سعر_الطريق) VALUES (?,?,?,?,?);\n" ;
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdInvoice());
            preparedStmt.setInt(2,o.getIdComponent());
            preparedStmt.setInt(3,o.getQte());
            preparedStmt.setDouble(4,o.getPrice());
            preparedStmt.setDouble(5,o.getPriceRoad());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(ComponentInvoice o1, ComponentInvoice o2) {

        connectDatabase();
        boolean upd = false;
        String query = "UPDATE بيع_منتج SET الكمية = ?, سعر_الوحدة = ?, سعر_الطريق = ? WHERE معرف_الفاتورة = ?  AND معرف_المنتج = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getQte());
            preparedStmt.setDouble(2,o1.getPrice());
            preparedStmt.setDouble(3,o1.getPriceRoad());
            preparedStmt.setInt(4,o2.getIdInvoice());
            preparedStmt.setInt(5,o2.getIdComponent());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    @Override
    public boolean delete(ComponentInvoice o) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM بيع_منتج WHERE معرف_الفاتورة = ? AND معرف_المنتج = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdInvoice());
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
    public boolean isExist(ComponentInvoice o) {
        return false;
    }

    @Override
    public ArrayList<ComponentInvoice> getAll() {
        return null;
    }

    public ArrayList<ComponentInvoice> getAllByInvoice(int idInvoice) {
        connectDatabase();
        ArrayList<ComponentInvoice> list = new ArrayList<>();
        String query = "SELECT * FROM بيع_منتج WHERE  معرف_الفاتورة = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idInvoice);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                ComponentInvoice componentInvoice = new ComponentInvoice();
                componentInvoice.setIdInvoice(resultSet.getInt("معرف_الفاتورة"));
                componentInvoice.setIdComponent(resultSet.getInt("معرف_المنتج"));
                componentInvoice.setQte(resultSet.getInt("الكمية"));
                componentInvoice.setPrice(resultSet.getDouble("سعر_الوحدة"));
                componentInvoice.setPriceRoad(resultSet.getDouble("سعر_الطريق"));

                list.add(componentInvoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
}
