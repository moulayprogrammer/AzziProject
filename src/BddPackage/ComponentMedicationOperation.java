package BddPackage;

import Models.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComponentMedicationOperation extends BDD<Component> {

    @Override
    public boolean insert(Component o) {
        boolean ins = false;
        String query = "INSERT INTO `مركب_الادوية`(`معرف_المنتج`, `معرف_الدواء`, `الكمية`) VALUES  (?,?,?)";
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
    public boolean update(Component o1, Component o2) {
        boolean upd = false;
        String query = "UPDATE `مركب_الادوية` SET `الكمية`= ? WHERE `معرف_المنتج` = ? AND `معرف_الدواء` = ?";
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
    public boolean delete(Component o) {
        return false;
    }

    @Override
    public boolean isExist(Component o) {
        return false;
    }

    @Override
    public ArrayList<Component> getAll() {
        return null;
    }
}
