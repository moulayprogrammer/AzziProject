package BddPackage;

import Models.Client;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientOperation extends BDD<Client> {

    @Override
    public boolean insert(Client o) {
        boolean ins = false;
        String query = "INSERT INTO `زبون`( `الاسم`, `العنوان`) VALUES  (?,?,?)";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,o.getName());
            preparedStmt.setString(2,o.getAddress());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ins;
    }

    @Override
    public boolean update(Client o1, Client o2) {
        return false;
    }

    @Override
    public boolean delete(Client o) {
        return false;
    }

    @Override
    public boolean isExist(Client o) {
        return false;
    }

    @Override
    public ArrayList<Client> getAll() {
        return null;
    }
}
