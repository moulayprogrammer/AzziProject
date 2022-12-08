package BddPackage;

import Models.Client;
import Models.Payments;

import java.sql.*;
import java.util.ArrayList;

public class PaymentsClientOperation extends BDD<Payments> {

    @Override
    public boolean insert(Payments o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO PAYMENTS (ID_CLIENT, ID_INVOICE, DATE, RAISON, PAY) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            preparedStmt.setInt(1,o.getIdPayer());
            preparedStmt.setInt(2,o.getIdInvoice());
            preparedStmt.setTimestamp(3, Timestamp.valueOf(o.getDate()));
            preparedStmt.setString(4,o.getRaison());
            preparedStmt.setDouble(5,o.getPay());

            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(Payments o1, Payments o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE PAYMENTS SET ID_CLIENT = ?, ID_INVOICE = ?, DATE = ?, RAISON = ?, PAY = ? WHERE ID = ?; ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getIdPayer());
            preparedStmt.setInt(2,o1.getIdInvoice());
            preparedStmt.setTimestamp(3, Timestamp.valueOf(o1.getDate()));
            preparedStmt.setString(4,o1.getRaison());
            preparedStmt.setDouble(5,o1.getPay());

            preparedStmt.setInt(6,o2.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    @Override
    public boolean delete(Payments o) {
        return false;
    }

    @Override
    public boolean isExist(Payments o) {
        return false;
    }

    @Override
    public ArrayList<Payments> getAll() {
        connectDatabase();
        ArrayList<Payments> list = new ArrayList<>();
        String query = "SELECT ID, ID_CLIENT, ID_INVOICE, DATE, RAISON, PAY FROM PAYMENTS;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Payments payments = new Payments();
                payments.setId(resultSet.getInt("ID"));
                payments.setIdPayer(resultSet.getInt("ID_CLIENT"));
                payments.setIdInvoice(resultSet.getInt("ID_INVOICE"));
                payments.setDate(resultSet.getTimestamp("DATE").toLocalDateTime());
                payments.setRaison(resultSet.getString("RAISON"));
                payments.setPay(resultSet.getDouble("PAY"));

                list.add(payments);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public ArrayList<Payments> getAllByClient(int idClient) {
        connectDatabase();
        ArrayList<Payments> list = new ArrayList<>();
        String query = "SELECT ID, ID_CLIENT, ID_INVOICE, DATE, RAISON, PAY FROM PAYMENTS WHERE ID_CLIENT = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idClient);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Payments payments = new Payments();
                payments.setId(resultSet.getInt("ID"));
                payments.setIdPayer(resultSet.getInt("ID_CLIENT"));
                payments.setIdInvoice(resultSet.getInt("ID_INVOICE"));
                payments.setDate(resultSet.getTimestamp("DATE").toLocalDateTime());
                payments.setRaison(resultSet.getString("RAISON"));
                payments.setPay(resultSet.getDouble("PAY"));

                list.add(payments);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
}
