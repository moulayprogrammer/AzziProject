package BddPackage;

import Models.ComponentStore;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ComponentStoreMedicationOperation extends BDD<ComponentStore> {

    @Override
    public boolean insert(ComponentStore o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO تخزين_الادوية ( معرف_الدواء, معرف_وصل_التوصيل , تاريخ_التخزين , سعر_الوحدة, كمية_مخزنة, كمية_مستهلكة ) VALUES (?,?,?,?,?) ; ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdComponent());
            preparedStmt.setInt(2,o.getIdDeliveryArrival());
            preparedStmt.setDate(3, Date.valueOf(LocalDate.now()));
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
        String query = "UPDATE تخزين_الادوية SET   سعر_الوحدة = ? , كمية_مخزنة = ? , كمية_مستهلكة = ? WHERE معرف_الدواء = ? AND معرف_وصل_التوصيل = ?";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDouble(1,o1.getPrice());
            preparedStmt.setInt(2,o1.getQteStored());
            preparedStmt.setInt(3,o1.getQteConsumed());
            preparedStmt.setInt(4,o2.getIdComponent());
            preparedStmt.setInt(5,o2.getIdDeliveryArrival());

            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    public boolean updateQte(ComponentStore o) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE تخزين_الادوية SET كمية_مستهلكة = ? WHERE معرف_الدواء = ? AND معرف_وصل_التوصيل = ?";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getQteConsumed());
            preparedStmt.setInt(2,o.getIdComponent());
            preparedStmt.setInt(3,o.getIdDeliveryArrival());

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
                componentStore.setIdComponent(resultSet.getInt("معرف_الدواء"));
                componentStore.setIdDeliveryArrival(resultSet.getInt("معرف_وصل_التوصيل"));
                componentStore.setDateStore(resultSet.getDate("تاريخ_التخزين").toLocalDate());
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

    public ArrayList<ComponentStore> getAllByMedicationOrderByDate(int idMedication) {
        connectDatabase();
        ArrayList<ComponentStore> list = new ArrayList<>();
        String query = "SELECT * FROM تخزين_الادوية WHERE معرف_الدواء = ?  AND (كمية_مخزنة - كمية_مستهلكة) > 0  ORDER BY تاريخ_التخزين DESC;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idMedication);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                ComponentStore componentStore = new ComponentStore();
                componentStore.setIdComponent(resultSet.getInt("معرف_الدواء"));
                componentStore.setIdDeliveryArrival(resultSet.getInt("معرف_وصل_التوصيل"));
                componentStore.setDateStore(resultSet.getDate("تاريخ_التخزين").toLocalDate());
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
