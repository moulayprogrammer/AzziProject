package BddPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

abstract class BDD<Object> {

    Connection conn;

    BDD() {
        connect();
    }

    public Connection connect(){
        // db parameters
        String url = "jdbc:mysql://localhost:3306/مزرعة_الجنوب?useSSL=false";
        String user = "root";
        String password = "";
        String unicode= "?useUnicode=yes&characterEncoding=UTF-8";

        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected to the database");
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred. Maybe user/password is invalid");
            ex.printStackTrace();
        }
        return conn;
    }

    void close(){
        try {
            if (!conn.isClosed()){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    abstract public boolean insert(Object o);
    //o1 new value
    //o2 id

    abstract public boolean update(Object o1,Object o2);

    abstract public boolean delete(Object o);

    abstract public boolean isExist(Object o);

    abstract public ArrayList<Object> getAll();
}
