package BddPackage;

import Models.ComponentProduction;
import Models.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComponentMedicationOperation extends BDD<ComponentProduction> {

    @Override
    public boolean insert(ComponentProduction o) {
        boolean ins = false;
        String query = "INSERT INTO خلطة_الادوية (معرف_المنتج, معرف_الدواء, الكمية) VALUES  (?,?,?)";
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
        String query = "UPDATE خلطة_الادوية SET  الكمية = ? WHERE معرف_المنتج = ? AND معرف_الدواء = ?";
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
        String query = "DELETE FROM خلطة_الادوية WHERE خلطة_الادوية.معرف_الدواء = ? AND خلطة_الادوية.معرف_المنتج = ? ;";
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
        ArrayList<ComponentProduction> list = new ArrayList<>();
        String query = "SELECT * FROM خلطة_الادوية WHERE ارشيف = 0";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                ComponentProduction component = new ComponentProduction();
                component.setIdComponent(resultSet.getInt("معرف_المنتج"));
                component.setIdProduct(resultSet.getInt("معرف_الدواء"));
                component.setQte(resultSet.getInt("الكمية"));

                list.add(component);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
