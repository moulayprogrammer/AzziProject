package BddPackage;

import Models.ComponentProduction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComponentProductionRawMaterialOperation extends BDD<ComponentProduction> {

    @Override
    public boolean insert(ComponentProduction o) {
        boolean ins = false;
        String query = "INSERT INTO خلطة_المواد_الخام (معرف_المنتج , معرف_المادة_الخام, الكمية) VALUES  (?,?,?)";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdProduct());
            preparedStmt.setInt(2,o.getIdComponent());
            preparedStmt.setInt(3,o.getQte());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ins;
    }

    @Override
    public boolean update(ComponentProduction o1, ComponentProduction o2) {
        boolean upd = false;
        String query = "UPDATE خلطة_المواد_الخام SET الكمية= ? WHERE معرف_المنتج = ? AND معرف_المادة_الخام = ?";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getQte());
            preparedStmt.setInt(2,o2.getIdProduct());
            preparedStmt.setInt(3,o2.getIdComponent());

            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return upd;
    }

    @Override
    public boolean delete(ComponentProduction o) {
        boolean del = false;
        String query = "DELETE FROM خلطة_المواد_الخام WHERE خلطة_المواد_الخام.معرف_المادة_الخام = ? AND خلطة_المواد_الخام.معرف_المنتج = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdComponent());
            preparedStmt.setInt(2,o.getIdProduct());

            int update = preparedStmt.executeUpdate();
            if(update != -1) del = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return del;
    }

    @Override
    public boolean isExist(ComponentProduction o) {
        return false;
    }

    @Override
    public ArrayList<ComponentProduction> getAll() {
        return null;
    }

    public ArrayList<ComponentProduction> getAllByProduct(int idProduct) {
        ArrayList<ComponentProduction> list = new ArrayList<>();
        String query = "SELECT * FROM خلطة_المواد_الخام WHERE معرف_المنتج = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idProduct);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                ComponentProduction component = new ComponentProduction();
                component.setIdComponent(resultSet.getInt("معرف_المادة_الخام"));
                component.setIdProduct(resultSet.getInt("معرف_المنتج"));
                component.setQte(resultSet.getInt("الكمية"));

                list.add(component);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
