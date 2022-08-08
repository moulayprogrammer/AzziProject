package BddPackage;

import Models.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsersOperation extends BDD<Users> {

    @Override
    public boolean insert(Users o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO user (username, password) VALUES (?,?)";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,o.getUsername());
            preparedStmt.setString(2,o.getPassword());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(Users o1, Users o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE user SET username = ?, password  = ? WHERE id = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,o1.getUsername());
            preparedStmt.setString(2,o1.getPassword());
            preparedStmt.setInt(3,o2.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    @Override
    public boolean delete(Users o) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM user WHERE id = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) del = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return del;
    }

    @Override
    public boolean isExist(Users o) {
        connectDatabase();
        boolean ex = false;
        String query = "SELECT * FROM user WHERE username = ? AND password = ?";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,o.getUsername());
            preparedStmt.setString(2,o.getPassword());
            ResultSet resultSet = preparedStmt.executeQuery();
            if (resultSet.next()){
                ex = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ex;
    }


    public ArrayList<Users> getAll() {
        connectDatabase();
        ArrayList<Users> list = new ArrayList<>();
        String query = "SELECT * FROM  user;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Users users = new Users();
                users.setId(resultSet.getInt("id"));
                users.setUsername(resultSet.getString("username"));
                users.setPassword(resultSet.getString("password"));

                list.add(users);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public Users get(String username, String password) {
        connectDatabase();
        Users users = new Users();
        String query = "SELECT * FROM user WHERE username = ? AND password = ?";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1,username);
            preparedStmt.setString(2,password);
            ResultSet resultSet = preparedStmt.executeQuery();
            if (resultSet.next()){

                users.setId(resultSet.getInt("id"));
                users.setUsername(resultSet.getString("username"));
                users.setPassword(resultSet.getString("password"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return users;
    }
}
