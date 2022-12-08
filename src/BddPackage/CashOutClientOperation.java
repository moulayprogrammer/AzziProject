package BddPackage;

import Models.CashOut;
import Models.Payments;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class CashOutClientOperation extends BDD<CashOut> {

    @Override
    public boolean insert(CashOut o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO CASH_OUT (ID_CLIENT, ID_INVOICE, DATE, RAISON, OUT) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            preparedStmt.setInt(1,o.getIdClient());
            preparedStmt.setInt(2,o.getIdInvoice());
            preparedStmt.setTimestamp(3, Timestamp.valueOf(o.getDate()));
            preparedStmt.setString(4,o.getRaison());
            preparedStmt.setDouble(5,o.getOut());

            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(CashOut o1, CashOut o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE CASH_OUT SET ID_CLIENT = ?, ID_INVOICE = ?, DATE = ?, RAISON = ?, OUT = ? WHERE ID = ?; ";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getIdClient());
            preparedStmt.setInt(2,o1.getIdInvoice());
            preparedStmt.setTimestamp(3, Timestamp.valueOf(o1.getDate()));
            preparedStmt.setString(4,o1.getRaison());
            preparedStmt.setDouble(5,o1.getOut());

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
    public boolean delete(CashOut o) {
        return false;
    }

    @Override
    public boolean isExist(CashOut o) {
        return false;
    }

    @Override
    public ArrayList<CashOut> getAll() {
        connectDatabase();
        ArrayList<CashOut> list = new ArrayList<>();
        String query = "SELECT ID, ID_CLIENT, ID_INVOICE, DATE, RAISON, OUT FROM CASH_OUT;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                CashOut CashOut = new CashOut();
                CashOut.setId(resultSet.getInt("ID"));
                CashOut.setIdClient(resultSet.getInt("ID_CLIENT"));
                CashOut.setIdInvoice(resultSet.getInt("ID_INVOICE"));
                CashOut.setDate(resultSet.getTimestamp("DATE").toLocalDateTime());
                CashOut.setRaison(resultSet.getString("RAISON"));
                CashOut.setOut(resultSet.getDouble("OUT"));

                list.add(CashOut);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public ArrayList<CashOut> getAllByClient(int idClient) {
        connectDatabase();
        ArrayList<CashOut> list = new ArrayList<>();
        String query = "SELECT ID, ID_CLIENT, ID_INVOICE, DATE, RAISON, OUT FROM CASH_OUT WHERE ID_CLIENT = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idClient);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                CashOut CashOut = new CashOut();
                CashOut.setId(resultSet.getInt("ID"));
                CashOut.setIdInvoice(resultSet.getInt("ID_CLIENT"));
                CashOut.setIdInvoice(resultSet.getInt("ID_INVOICE"));
                CashOut.setDate(resultSet.getTimestamp("DATE").toLocalDateTime());
                CashOut.setRaison(resultSet.getString("RAISON"));
                CashOut.setOut(resultSet.getDouble("OUT"));

                list.add(CashOut);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
}
