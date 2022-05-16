package BddPackage;

import Models.ComponentProduction;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComponentRawMaterialOperation extends BDD<ComponentProduction> {

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
        String query = "UPDATE مركب_المواد_الخام SET الكمية= ? WHERE معرف_المنتج = ? AND مركب_المواد_الخام = ?";
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
        return false;
    }

    @Override
    public boolean isExist(ComponentProduction o) {
        return false;
    }

    @Override
    public ArrayList<ComponentProduction> getAll() {
        return null;
    }
}
