package BddPackage;

import Models.ComponentStoreProduct;
import Models.ComponentStoreProductTemp;
import Models.Delivery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComponentStoreProductTempOperation extends BDD<ComponentStoreProductTemp>{

    @Override
    public boolean insert(ComponentStoreProductTemp o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO تخزين_منتجات_مؤقت_للبيع (معرف_الانتاج, معرف_المنتج, معرف_فاتورة_البيع, الكمية) VALUES (?,?,?,?)";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdProduction());
            preparedStmt.setInt(2,o.getIdProduct());
            preparedStmt.setDouble(3,o.getIdInvoice());
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
    public boolean update(ComponentStoreProductTemp o1, ComponentStoreProductTemp o2) {
        return false;
    }

    @Override
    public boolean delete(ComponentStoreProductTemp o) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM تخزين_منتجات_مؤقت_للبيع WHERE المعرف = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getId());

            int delete = preparedStmt.executeUpdate();
            if(delete != -1) del = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return del;
    }

    public boolean deleteByInvoice(int idInvoice){
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM تخزين_منتجات_مؤقت_للبيع WHERE معرف_فاتورة_البيع = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idInvoice);

            int delete = preparedStmt.executeUpdate();
            if(delete != -1) del = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return del;
    }

    @Override
    public boolean isExist(ComponentStoreProductTemp o) {
        return false;
    }

    @Override
    public ArrayList<ComponentStoreProductTemp> getAll() {
        connectDatabase();
        ArrayList<ComponentStoreProductTemp> list = new ArrayList<>();
        String query = "SELECT * FROM تخزين_منتجات_مؤقت_للبيع  ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                ComponentStoreProductTemp storeProductTemp = new ComponentStoreProductTemp();
                storeProductTemp.setId(resultSet.getInt("المعرف"));
                storeProductTemp.setIdInvoice(resultSet.getInt("معرف_فاتورة_البيع"));
                storeProductTemp.setIdProduct(resultSet.getInt("معرف_المنتج"));
                storeProductTemp.setIdProduction(resultSet.getInt("معرف_الانتاج"));
                storeProductTemp.setQte(resultSet.getInt("الكمية"));

                list.add(storeProductTemp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public ArrayList<ComponentStoreProductTemp> getAllByInvoice(int idInvoice){
        connectDatabase();
        ArrayList<ComponentStoreProductTemp> list = new ArrayList<>();
        String query = "SELECT * FROM تخزين_منتجات_مؤقت_للبيع WHERE معرف_فاتورة_البيع = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idInvoice);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                ComponentStoreProductTemp storeProductTemp = new ComponentStoreProductTemp();
                storeProductTemp.setId(resultSet.getInt("المعرف"));
                storeProductTemp.setIdInvoice(resultSet.getInt("معرف_فاتورة_البيع"));
                storeProductTemp.setIdProduct(resultSet.getInt("معرف_المنتج"));
                storeProductTemp.setIdProduction(resultSet.getInt("معرف_الانتاج"));
                storeProductTemp.setQte(resultSet.getInt("الكمية"));

                list.add(storeProductTemp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;

    }
}
