package BddPackage;

import Models.ComponentStoreProduct;
import Models.ComponentStoreProductTemp;

import java.sql.PreparedStatement;
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
        return false;
    }

    @Override
    public boolean isExist(ComponentStoreProductTemp o) {
        return false;
    }

    @Override
    public ArrayList<ComponentStoreProductTemp> getAll() {
        return null;
    }
}
