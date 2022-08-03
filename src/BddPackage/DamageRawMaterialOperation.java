package BddPackage;

import Models.Damage;
import Models.DeliveryArrival;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DamageRawMaterialOperation extends BDD<Damage> {

    @Override
    public boolean insert(Damage o) {
        return false;
    }

    public int insertId(Damage damage) {
        connectDatabase();
        int ins = 0;
        String query = "INSERT INTO اتلاف_المواد_الخام (معرف_المادة, تاريخ_التلف, الكمية, السبب) VALUES (?,?,?,?);";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,damage.getIdProduct());
            preparedStmt.setDate(2, Date.valueOf(damage.getDate()));
            preparedStmt.setInt(3,damage.getQte());
            preparedStmt.setString(4,damage.getRaison());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) {
                ins = preparedStmt.getGeneratedKeys().getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(Damage o1, Damage o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE اتلاف_المواد_الخام SET تاريخ_التلف = ?, الكمية = ?, السبب = ? WHERE المعرف = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDate(1,Date.valueOf(o1.getDate()));
            preparedStmt.setInt(2,o1.getQte());
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
    public boolean delete(Damage o) {
        connectDatabase();
        boolean del = false;
        String query = "DELETE FROM اتلاف_المواد_الخام WHERE المعرف = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getId());

            int delete = preparedStmt.executeUpdate();
            if(delete != -1) del = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return del;
    }

    @Override
    public boolean isExist(Damage o) {
        return false;
    }

    @Override
    public ArrayList<Damage> getAll() {
        connectDatabase();
        ArrayList<Damage> list = new ArrayList<>();
        String query = "SELECT * FROM اتلاف_المواد_الخام ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Damage damage = new Damage();
                damage.setId(resultSet.getInt("المعرف"));
                damage.setIdProduct(resultSet.getInt("معرف_المادة"));
                damage.setDate(resultSet.getDate("تاريخ_التلف").toLocalDate());
                damage.setQte(resultSet.getInt("الكمية"));
                damage.setRaison(resultSet.getString("السبب"));

                list.add(damage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
}
