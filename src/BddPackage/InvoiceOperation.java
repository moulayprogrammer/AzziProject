package BddPackage;

import Models.Invoice;
import Models.Receipt;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class InvoiceOperation extends BDD<Invoice> {

    @Override
    public boolean insert(Invoice o) {
        return false;
    }

    public int insertId(Invoice invoice) {
        connectDatabase();
        int ins = 0;
        String query = "INSERT INTO فاتورة_بيع (رقم_الفاتورة, معرف_الزبون, تاريخ_البيع, الدفع) VALUES (?,?,?);";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,invoice.getIdClient());
            preparedStmt.setInt(2,invoice.getNumber());
            preparedStmt.setDate(3, Date.valueOf(invoice.getDate()));
            preparedStmt.setDouble(4,invoice.getPaying());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = preparedStmt.getGeneratedKeys().getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(Invoice o1, Invoice o2) { //
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE فاتورة_بيع SET معرف_الزبون = ?, تاريخ_البيع = ?, الدفع = ? WHERE المعرف = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getIdClient());
            preparedStmt.setDate(2,Date.valueOf(o1.getDate()));
            preparedStmt.setDouble(3,o1.getPaying());
            preparedStmt.setInt(4,o2.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    public boolean updatePaying(Invoice o1, Invoice o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE فاتورة_بيع SET الدفع = ? WHERE المعرف = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDouble(1,o1.getPaying());
            preparedStmt.setInt(2,o2.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1) upd = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    @Override
    public boolean delete(Invoice o) {
        return false;
    }

    @Override
    public boolean isExist(Invoice o) {
        return false;
    }

    @Override
    public ArrayList<Invoice> getAll() {
        connectDatabase();
        ArrayList<Invoice> list = new ArrayList<>();
        String query = "SELECT * FROM فاتورة_بيع WHERE ارشيف = 0";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Invoice invoice = new Invoice();
                invoice.setId(resultSet.getInt("المعرف"));
                invoice.setNumber(resultSet.getInt("رقم_الفاتورة"));
                invoice.setIdClient(resultSet.getInt("معرف_الزبون"));
                invoice.setDate(resultSet.getDate("تاريخ_البيع").toLocalDate());
                invoice.setPaying(resultSet.getDouble("الدفع"));

                list.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
    public Invoice get(int id) {
        connectDatabase();
        Invoice invoice = new Invoice();
        String query = "SELECT * FROM فاتورة_بيع WHERE ارشيف = 0 AND المعرف = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){


                invoice.setId(resultSet.getInt("المعرف"));
                invoice.setNumber(resultSet.getInt("رقم_الفاتورة"));
                invoice.setIdClient(resultSet.getInt("معرف_الزبون"));
                invoice.setDate(resultSet.getDate("تاريخ_البيع").toLocalDate());
                invoice.setPaying(resultSet.getDouble("الدفع"));


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return invoice;
    }
    public int getLastNumber(){
        connectDatabase();
        int nbr = 0;
        try {
            String query = "SELECT المعرف, رقم_الفاتورة  FROM فاتورة_بيع ORDER BY(المعرف) DESC LIMIT 1 ;";
            try {
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                ResultSet resultSet = preparedStmt.executeQuery();
                while (resultSet.next()){
                    nbr = resultSet.getInt("رقم_الفاتورة");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        closeDatabase();
        return nbr;
    }

    public ArrayList<Invoice> getAllByClient(int idClient) {
        connectDatabase();
        ArrayList<Invoice> list = new ArrayList<>();
        String query = "SELECT * FROM فاتورة_بيع WHERE ارشيف = 0 AND معرف_الزبون = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idClient);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Invoice invoice = new Invoice();
                invoice.setId(resultSet.getInt("المعرف"));
                invoice.setNumber(resultSet.getInt("رقم_الفاتورة"));
                invoice.setIdClient(resultSet.getInt("معرف_الزبون"));
                invoice.setDate(resultSet.getDate("تاريخ_البيع").toLocalDate());
                invoice.setPaying(resultSet.getDouble("الدفع"));

                list.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public ArrayList<Invoice> getAllByDate(LocalDate dateFirst, LocalDate dateSecond) {
        connectDatabase();
        ArrayList<Invoice> list = new ArrayList<>();
        String query = "SELECT * FROM فاتورة_بيع WHERE ارشيف = 0 AND تاريخ_الشراء BETWEEN ? AND ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDate(1,Date.valueOf(dateFirst));
            preparedStmt.setDate(2,Date.valueOf(dateSecond));
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Invoice invoice = new Invoice();
                invoice.setId(resultSet.getInt("المعرف"));
                invoice.setNumber(resultSet.getInt("رقم_الفاتورة"));
                invoice.setIdClient(resultSet.getInt("معرف_الزبون"));
                invoice.setDate(resultSet.getDate("تاريخ_البيع").toLocalDate());
                invoice.setPaying(resultSet.getDouble("الدفع"));

                list.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public ArrayList<Invoice> getAllByDateClient(int idClient, LocalDate dateFirst, LocalDate dateSecond) {
        connectDatabase();
        ArrayList<Invoice> list = new ArrayList<>();
        String query = "SELECT * FROM فاتورة_بيع WHERE ارشيف = 0 AND معرف_الزبون = ? AND تاريخ_الشراء BETWEEN ? AND ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idClient);
            preparedStmt.setDate(2,Date.valueOf(dateFirst));
            preparedStmt.setDate(3,Date.valueOf(dateSecond));
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Invoice invoice = new Invoice();
                invoice.setId(resultSet.getInt("المعرف"));
                invoice.setNumber(resultSet.getInt("رقم_الفاتورة"));
                invoice.setIdClient(resultSet.getInt("معرف_الزبون"));
                invoice.setDate(resultSet.getDate("تاريخ_البيع").toLocalDate());
                invoice.setPaying(resultSet.getDouble("الدفع"));

                list.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
}
