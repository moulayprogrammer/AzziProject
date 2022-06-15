package BddPackage;

import Models.ComponentStore;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComponentStoreMedicationOperation extends BDD<ComponentStore> {

    @Override
    public boolean insert(ComponentStore o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO تخزين_الادوية (معرف_المخزن, معرف_الدواء, معرف_وصل_التوصيل , سعر_الوحدة, كمية_مخزنة, كمية_مستهلكة ) VALUES (?,?,?,?,?,?) ; ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdStore());
            preparedStmt.setInt(2,o.getIdComponent());
            preparedStmt.setInt(3,o.getIdDeliveryArrival());
            preparedStmt.setDouble(4,o.getPrice());
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
    public boolean update(ComponentStore o1, ComponentStore o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE تخزين_الادوية SET  معرف_المخزن = ? , سعر_الوحدة = ? , كمية_مخزنة = ? , كمية_مستهلكة = ? WHERE معرف_الدواء = ? AND معرف_وصل_التوصيل = ?";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getIdStore());
            preparedStmt.setDouble(2,o1.getPrice());
            preparedStmt.setInt(3,o2.getQteStored());
            preparedStmt.setInt(4,o2.getQteConsumed());
            preparedStmt.setInt(4,o2.getIdComponent());
            preparedStmt.setInt(4,o2.getIdDeliveryArrival());

            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    @Override
    public boolean delete(ComponentStore o) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM تخزين_الادوية WHERE معرف_وصل_التوصيل = ? AND معرف_الدواء = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdDeliveryArrival());
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
    public boolean isExist(ComponentStore o) {
        return false;
    }

    @Override
    public ArrayList<ComponentStore> getAll() {
        connectDatabase();
        ArrayList<ComponentStore> list = new ArrayList<>();
        String query = "SELECT * FROM تخزين_الادوية ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                ComponentStore componentStore = new ComponentStore();
                componentStore.setIdComponent(resultSet.getInt("معرف_المادة_الخام"));
                componentStore.setIdStore(resultSet.getInt("معرف_المخزن"));
                componentStore.setIdDeliveryArrival(resultSet.getInt("معرف_وصل_التوصيل"));
                componentStore.setPrice(resultSet.getDouble("سعر_الوحدة"));
                componentStore.setQteStored(resultSet.getInt("كمية_مخزنة"));
                componentStore.setQteConsumed(resultSet.getInt("كمية_مستهلكة"));

                list.add(componentStore);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
}
