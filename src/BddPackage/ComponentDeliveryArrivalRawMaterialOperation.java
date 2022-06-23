package BddPackage;

import Models.ComponentDeliveryArrival;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComponentDeliveryArrivalRawMaterialOperation extends BDD<ComponentDeliveryArrival>{

    @Override
    public boolean insert(ComponentDeliveryArrival o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO توصيل_مواد_خام (معرف_الوصل, معرف_المادة, الكمية_المفوترة , الكمية_الموصلة) VALUES  (?,?,?,?)";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdDeliveryArrival());
            preparedStmt.setInt(2,o.getIdComponent());
            preparedStmt.setInt(3,o.getQteReceipt());
            preparedStmt.setInt(4,o.getQteReal());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(ComponentDeliveryArrival o1, ComponentDeliveryArrival o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE توصيل_مواد_خام SET  الكمية_المفوترة = ? , الكمية_الموصلة = ? WHERE معرف_الوصل = ? AND معرف_المادة = ?";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getQteReceipt());
            preparedStmt.setInt(2,o1.getQteReal());
            preparedStmt.setInt(3,o2.getIdDeliveryArrival());
            preparedStmt.setInt(4,o2.getIdComponent());

            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    @Override
    public boolean delete(ComponentDeliveryArrival o) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM توصيل_مواد_خام WHERE معرف_الوصل = ? AND معرف_المادة = ? ;";
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
    public boolean isExist(ComponentDeliveryArrival o) {
        return false;
    }

    @Override
    public ArrayList<ComponentDeliveryArrival> getAll() {
        connectDatabase();
        ArrayList<ComponentDeliveryArrival> list = new ArrayList<>();
        String query = "SELECT * FROM توصيل_مواد_خام";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                ComponentDeliveryArrival componentDeliveryArrival = new ComponentDeliveryArrival();
                componentDeliveryArrival.setIdDeliveryArrival(resultSet.getInt("معرف_الوصل"));
                componentDeliveryArrival.setIdComponent(resultSet.getInt("معرف_المادة"));
                componentDeliveryArrival.setQteReceipt(resultSet.getInt("الكمية_المفوترة"));
                componentDeliveryArrival.setQteReal(resultSet.getInt("الكمية_الموصلة"));

                list.add(componentDeliveryArrival);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }


    public ArrayList<ComponentDeliveryArrival> getAllByDeliveryArrivalAndMedication(int idDeliveryArrival , int idMedication) {
        connectDatabase();
        ArrayList<ComponentDeliveryArrival> list = new ArrayList<>();
        String query = "SELECT * FROM توصيل_مواد_خام WHERE ارشيف = 0 AND معرف_الوصل = ? AND  معرف_المادة = ?";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idDeliveryArrival);
            preparedStmt.setInt(2,idMedication);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                ComponentDeliveryArrival componentDeliveryArrival = new ComponentDeliveryArrival();
                componentDeliveryArrival.setIdDeliveryArrival(resultSet.getInt("معرف_الوصل"));
                componentDeliveryArrival.setIdComponent(resultSet.getInt("معرف_المادة"));
                componentDeliveryArrival.setQteReceipt(resultSet.getInt("الكمية_المفوترة"));
                componentDeliveryArrival.setQteReal(resultSet.getInt("الكمية_الموصلة"));

                list.add(componentDeliveryArrival);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

}
