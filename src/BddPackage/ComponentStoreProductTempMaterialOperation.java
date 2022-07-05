package BddPackage;

import Models.ComponentStoreProductTemp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComponentStoreProductTempMaterialOperation extends BDD<ComponentStoreProductTemp> {

    @Override
    public boolean insert(ComponentStoreProductTemp o) {
        connectDatabase();
        boolean ins = false;
            String query = "INSERT INTO  تخزين_مواد_خام_مؤقت_للانتاج (معرف_المادة_الخام, معرف_وصل_التوصيل, معرف_الانتاج, الكمية) VALUES (?,?,?,?);";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdComponent());
            preparedStmt.setInt(2,o.getIdDeliveryArrival());
            preparedStmt.setInt(3, o.getIdProduction());
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
        try {
            String query = "DELETE FROM تخزين_مواد_خام_مؤقت_للانتاج WHERE معرف_الانتاج = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdProduction());
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
        return null;
    }

    public ArrayList<ComponentStoreProductTemp> getAllByProduction(int idProduction) {
        connectDatabase();
        ArrayList<ComponentStoreProductTemp> list = new ArrayList<>();
        String query = "SELECT * FROM تخزين_مواد_خام_مؤقت_للانتاج WHERE معرف_الانتاج = ?";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idProduction);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                ComponentStoreProductTemp componentStoreProductTemp = new ComponentStoreProductTemp();

                componentStoreProductTemp.setId(resultSet.getInt("المعرف"));
                componentStoreProductTemp.setIdComponent(resultSet.getInt("معرف_المادة_الخام"));
                componentStoreProductTemp.setIdDeliveryArrival(resultSet.getInt("معرف_وصل_التوصيل"));
                componentStoreProductTemp.setIdProduction(resultSet.getInt("معرف_الانتاج"));
                componentStoreProductTemp.setQte(resultSet.getInt("الكمية"));

                list.add(componentStoreProductTemp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
}
