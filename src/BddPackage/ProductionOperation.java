package BddPackage;

import Models.Product;
import Models.Production;

import java.sql.*;
import java.util.ArrayList;

public class ProductionOperation extends BDD<Production> {

    @Override
    public boolean insert(Production o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO الانتاج (معرف_المنتج,التاريخ,الكمية_المنتجة,التكلفة) VALUES (?,?,?,?);";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdProduct());
            preparedStmt.setDate(2, Date.valueOf(o.getDate()));
            preparedStmt.setInt(3,o.getQteProduct());
            preparedStmt.setDouble(4,o.getPrice());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    public int insertId(Production o) {
        connectDatabase();
        int ins = 0;
        String query = "INSERT INTO الانتاج (معرف_المنتج,التاريخ,الكمية_المنتجة,التكلفة) VALUES (?,?,?,?);";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getIdProduct());
            preparedStmt.setDate(2, Date.valueOf(o.getDate()));
            preparedStmt.setInt(3,o.getQteProduct());
            preparedStmt.setDouble(4,o.getPrice());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = preparedStmt.getGeneratedKeys().getInt(1);;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }


    @Override
    public boolean update(Production o1, Production o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE الانتاج SET الكمية_المنتجة = ?, التكلفة= ? WHERE المعرف = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getQteProduct());
            preparedStmt.setDouble(2,o1.getPrice());
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
    public boolean delete(Production o) {
        connectDatabase();
        boolean del = false;

        try {
            String query = "DELETE FROM الانتاج WHERE المعرف = ?;";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getId());
            int delete = preparedStmt.executeUpdate();
            if(delete != -1) del = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            String query = "DELETE FROM تخزين_مواد_خام_مؤقت_للانتاج WHERE معرف_الانتاج = ?;";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o.getId());
            int delete = preparedStmt.executeUpdate();
            if(delete != -1) del = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            String query = "DELETE FROM تخزين_الادوية_مؤقت_للانتاج WHERE معرف_الانتاج = ?;";
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
    public boolean isExist(Production o) {
        return false;
    }

    @Override
    public ArrayList<Production> getAll() {
        ArrayList<Production> list = new ArrayList<>();
        connectDatabase();
        String query = "SELECT * FROM الانتاج";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Production production = new Production();

                production.setId(resultSet.getInt("المعرف"));
                production.setIdProduct(resultSet.getInt("معرف_المنتج"));
                production.setDate(resultSet.getDate("التاريخ").toLocalDate());
                production.setQteProduct(resultSet.getInt("الكمية_المنتجة"));
                production.setPrice(resultSet.getDouble("التكلفة"));

                list.add(production);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public Production get(int id) {
        connectDatabase();
        Production production = new Production();
        String query = "SELECT * FROM الانتاج WHERE المعرف = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                production.setId(resultSet.getInt("المعرف"));
                production.setIdProduct(resultSet.getInt("معرف_المنتج"));
                production.setDate(resultSet.getDate("التاريخ").toLocalDate());
                production.setQteProduct(resultSet.getInt("الكمية_المنتجة"));
                production.setPrice(resultSet.getDouble("التكلفة"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return production;
    }
}
