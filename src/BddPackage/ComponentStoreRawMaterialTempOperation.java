package BddPackage;

import Models.ComponentStoreTemp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComponentStoreRawMaterialTempOperation extends BDD<ComponentStoreTemp> {

    @Override
    public boolean insert(ComponentStoreTemp o) {
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
    public boolean update(ComponentStoreTemp o1, ComponentStoreTemp o2) {
        return false;
    }

    @Override
    public boolean delete(ComponentStoreTemp o) {
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
    public boolean isExist(ComponentStoreTemp o) {
        return false;
    }

    @Override
    public ArrayList<ComponentStoreTemp> getAll() {
        return null;
    }

    public ArrayList<ComponentStoreTemp> getAllByProduction(int idProduction) {
        connectDatabase();
        ArrayList<ComponentStoreTemp> list = new ArrayList<>();
        String query = "SELECT * FROM تخزين_مواد_خام_مؤقت_للانتاج WHERE معرف_الانتاج = ?";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idProduction);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                ComponentStoreTemp componentStoreTemp = new ComponentStoreTemp();

                componentStoreTemp.setId(resultSet.getInt("المعرف"));
                componentStoreTemp.setIdComponent(resultSet.getInt("معرف_المادة_الخام"));
                componentStoreTemp.setIdDeliveryArrival(resultSet.getInt("معرف_وصل_التوصيل"));
                componentStoreTemp.setIdProduction(resultSet.getInt("معرف_الانتاج"));
                componentStoreTemp.setQte(resultSet.getInt("الكمية"));

                list.add(componentStoreTemp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
}
