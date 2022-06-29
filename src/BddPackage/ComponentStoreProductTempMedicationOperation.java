package BddPackage;

import Models.ComponentStoreProductTemp;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ComponentStoreProductTempMedicationOperation extends BDD<ComponentStoreProductTemp> {

    @Override
    public boolean insert(ComponentStoreProductTemp o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO  تخزين_الادوية_مؤقت_للانتاج (معرف_الدواء, معرف_وصل_التوصيل, معرف_الانتاج, الكمية) VALUES (?,?,?,?);";
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
