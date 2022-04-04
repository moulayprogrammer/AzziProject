package BddPackage;

import Models.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductOperation extends BDD<Product> {

    @Override
    public boolean insert(Product o) {
        boolean ins = false;
        String query = "INSERT INTO `المنتجات`( `الاسم`, `المرجع`, `اقل_كمية`) VALUES  (?,?,?)";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,o.getName());
            preparedStmt.setString(2,o.getReference());
            preparedStmt.setInt(3,o.getLimiteQte());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ins;
    }

    @Override
    public boolean update(Product o1, Product o2) {
        boolean upd = false;
        String query = "UPDATE `المنتجات` SET `الاسم`= ?,`المرجع`= ?,`اقل_كمية`= ? " +
                "WHERE `المعرف` = ? ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,o1.getName());
            preparedStmt.setString(2,o1.getReference());
            preparedStmt.setInt(3,o1.getLimiteQte());
            preparedStmt.setInt(4,o2.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return upd;
    }

    @Override
    public boolean delete(Product o) {
        return false;
    }

    @Override
    public boolean isExist(Product o) {
        return false;
    }

    @Override
    public ArrayList<Product> getAll() {
        ArrayList<Product> list = new ArrayList<>();
        String query = "SELECT * FROM `المنتجات` WHERE `ارشيف` = 0";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Product product = new Product();

                product.setId(resultSet.getInt("المعرف"));
                product.setName(resultSet.getString("الاسم"));
                product.setReference(resultSet.getString("المرجع"));
                product.setLimiteQte(resultSet.getInt("اقل_كمية"));

                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
