package BddPackage;


import Models.ComponentStore;
import Models.ComponentStoreProduct;
import Models.ComponentStoreProductTemp;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public ComponentStoreProduct get(int idProduct , int idProduction) {
        connectDatabase();
        ComponentStoreProduct storeProduct = new ComponentStoreProduct();
        String query = "SELECT * FROM تخزين_منتج WHERE معرف_الانتاج = ? AND معرف_المنتج = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idProduction);
            preparedStmt.setInt(2,idProduct);
            ResultSet resultSet = preparedStmt.executeQuery();
            if (resultSet.next()){

                storeProduct.setIdProduction(resultSet.getInt("معرف_الانتاج"));
                storeProduct.setIdComponent(resultSet.getInt("معرف_المنتج"));
                storeProduct.setDateStore(resultSet.getDate("تاريخ_التخزين").toLocalDate());
                storeProduct.setQteStored(resultSet.getInt("كمية_مخزنة"));
                storeProduct.setQteConsumed(resultSet.getInt("كمية_مستهلكة"));
                storeProduct.setPriceHt(resultSet.getDouble("سعر_البيع"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return storeProduct;
    }

    public ArrayList<ComponentStoreProduct> getAllByProductOrderByDate(int idProduct) {
        connectDatabase();
        ArrayList<ComponentStoreProduct> list = new ArrayList<>();
        String query = "SELECT * FROM تخزين_منتج WHERE معرف_المنتج = ?  AND (كمية_مخزنة - كمية_مستهلكة) > 0  ORDER BY تاريخ_التخزين DESC;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idProduct);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                ComponentStoreProduct storeProduct = new ComponentStoreProduct();
                storeProduct.setIdProduction(resultSet.getInt("معرف_الانتاج"));
                storeProduct.setIdComponent(resultSet.getInt("معرف_المنتج"));
                storeProduct.setDateStore(resultSet.getDate("تاريخ_التخزين").toLocalDate());
                storeProduct.setQteStored(resultSet.getInt("كمية_مخزنة"));
                storeProduct.setQteConsumed(resultSet.getInt("كمية_مستهلكة"));
                storeProduct.setPriceHt(resultSet.getDouble("سعر_البيع"));

                list.add(storeProduct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
}
