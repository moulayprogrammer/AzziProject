package BddPackage;

import Models.Client;
import Models.Spend;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SpendOperation extends BDD<Spend> {

    @Override
    public boolean insert(Spend o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO المصاريف (المبلغ, التاريخ, السبب) VALUES (?,?,?)";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDouble(1,o.getPrice());
            preparedStmt.setDate(2, Date.valueOf(o.getDate()));
            preparedStmt.setString(3,o.getRaison());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(Spend o1, Spend o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE المصاريف SET المبلغ = ?, التاريخ = ?, السبب = ? WHERE المعرف = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDouble(1,o1.getPrice());
            preparedStmt.setDate(2, Date.valueOf(o1.getDate()));
            preparedStmt.setString(3,o1.getRaison());
            preparedStmt.setInt(4,o2.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    @Override
    public boolean delete(Spend o) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM المصاريف WHERE المعرف = ? ;";
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
    public boolean isExist(Spend o) {
        return false;
    }

    @Override
    public ArrayList<Spend> getAll() {
        connectDatabase();
        ArrayList<Spend> list = new ArrayList<>();
        String query = "SELECT * FROM  المصاريف ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Spend spend = new Spend();
                spend.setId(resultSet.getInt("المعرف"));
                spend.setPrice(resultSet.getDouble("المبلغ"));
                spend.setDate(resultSet.getDate("التاريخ").toLocalDate());
                spend.setRaison(resultSet.getString("السبب"));

                list.add(spend);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
}
