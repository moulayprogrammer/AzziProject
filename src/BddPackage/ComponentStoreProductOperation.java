package BddPackage;


import Models.ComponentStoreProduct;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComponentStoreProductOperation extends BDD<ComponentStoreProduct> {

    @Override
    public boolean insert(ComponentStoreProduct o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO تخزين_منتج ( معرف_الانتاج, معرف_المنتج , تاريخ_التخزين , سعر_البيع, كمية_مخزنة, كمية_مستهلكة ) VALUES (?,?,?,?,?,?) ; ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdProduction());
            preparedStmt.setInt(2,o.getIdComponent());
            preparedStmt.setDate(3, Date.valueOf(o.getDateStore()));
            preparedStmt.setDouble(4,o.getPriceHt());
            preparedStmt.setInt(5,o.getQteStored());
            preparedStmt.setInt(6,o.getQteConsumed());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(ComponentStoreProduct o1, ComponentStoreProduct o2) {
        return false;
    }

    public boolean updateQte(ComponentStoreProduct o) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE تخزين_منتج SET كمية_مستهلكة = ? WHERE معرف_الانتاج = ? AND معرف_المنتج = ?";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getQteConsumed());
            preparedStmt.setInt(2,o.getIdProduction());
            preparedStmt.setInt(3,o.getIdComponent());

            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    @Override
    public boolean delete(ComponentStoreProduct o) {
        return false;
    }

    @Override
    public boolean isExist(ComponentStoreProduct o) {
        return false;
    }

    @Override
    public ArrayList<ComponentStoreProduct> getAll() {
        return null;
    }
}
