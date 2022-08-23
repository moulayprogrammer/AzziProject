package BddPackage;

import Models.Client;
import Models.Payments;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentsClientOperation extends BDD<Payments> {

    @Override
    public boolean insert(Payments o) {
        connectDatabase();
        boolean ins = false;
        String query = "INSERT INTO مدفوعات_الزبائن ( معرف_الزبون, التاريخ, الدفع, المتبقي) VALUES (?,?,?,?)";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            preparedStmt.setInt(1,o.getIdPayer());
            preparedStmt.setDate(2, Date.valueOf(o.getDate()));
            preparedStmt.setDouble(3,o.getPay());
            preparedStmt.setDouble(4,o.getRest());

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
        String query = "UPDATE مدفوعات_الزبائن SET التاريخ = ?, الدفع = ?, المتبقي = ?  WHERE المعرف = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDate(1, Date.valueOf(o1.getDate()));
            preparedStmt.setDouble(2,o1.getPay());
            preparedStmt.setDouble(3,o1.getRest());

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
        String query = "SELECT * FROM  مدفوعات_الزبائن ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Payments payments = new Payments();
                payments.setId(resultSet.getInt("المعرف"));
                payments.setIdPayer(resultSet.getInt("معرف_الزبون"));
                payments.setDate(resultSet.getDate("التاريخ").toLocalDate());
                payments.setPay(resultSet.getDouble("الدفع"));
                payments.setRest(resultSet.getDouble("المتبقي"));

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
        String query = "SELECT * FROM  مدفوعات_الزبائن  where معرف_الزبون = ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idClient);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Payments payments = new Payments();
                payments.setId(resultSet.getInt("المعرف"));
                payments.setIdPayer(resultSet.getInt("معرف_الزبون"));
                payments.setDate(resultSet.getDate("التاريخ").toLocalDate());
                payments.setPay(resultSet.getDouble("الدفع"));
                payments.setRest(resultSet.getDouble("المتبقي"));

                list.add(payments);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
}
