package BddPackage;

import Models.Product;
import Models.Receipt;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ReceiptMedicationOperation extends BDD<Receipt> {

    @Override
    public boolean insert(Receipt o) {
        return false;
    }

    public int insertId(Receipt receipt) {
        connectDatabase();
        int ins = 0;
        String query = "INSERT INTO فاتورة_شراء_الدواء (معرف_المورد,تاريخ_الشراء, رقم_الفاتورة, الدفع)  VALUES (?,?,?,?);";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,receipt.getIdProvider());
            preparedStmt.setDate(2, Date.valueOf(receipt.getDate()));
            preparedStmt.setInt(3,receipt.getNumber());
            preparedStmt.setDouble(4,receipt.getPaying());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = preparedStmt.getGeneratedKeys().getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return ins;
    }

    @Override
    public boolean update(Receipt o1, Receipt o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE فاتورة_شراء_الدواء SET معرف_المورد = ?, تاريخ_الشراء = ?, الدفع = ? WHERE المعرف = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,o1.getIdProvider());
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
    public boolean updatePaying(Receipt o1, Receipt o2) {
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE فاتورة_شراء_الدواء SET الدفع = ? WHERE المعرف = ?;";
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
    public boolean delete(Receipt o) {
        return false;
    }

    @Override
    public boolean isExist(Receipt o) {
        return false;
    }

    @Override
    public ArrayList<Receipt> getAll() {
        connectDatabase();
        ArrayList<Receipt> list = new ArrayList<>();
        String query = "SELECT * FROM فاتورة_شراء_الدواء WHERE ارشيف = 0";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Receipt receipt = new Receipt();
                receipt.setId(resultSet.getInt("المعرف"));
                receipt.setIdProvider(resultSet.getInt("معرف_المورد"));
                receipt.setNumber(resultSet.getInt("رقم_الفاتورة"));
                receipt.setDate(resultSet.getDate("تاريخ_الشراء").toLocalDate());
                receipt.setPaying(resultSet.getDouble("الدفع"));

                list.add(receipt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public Receipt get(int id) {
        connectDatabase();
        ArrayList<Receipt> list = new ArrayList<>();
        String query = "SELECT * FROM فاتورة_شراء_الدواء WHERE ارشيف = 0 AND المعرف = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Receipt receipt = new Receipt();
                receipt.setId(resultSet.getInt("المعرف"));
                receipt.setIdProvider(resultSet.getInt("معرف_المورد"));
                receipt.setNumber(resultSet.getInt("رقم_الفاتورة"));
                receipt.setDate(resultSet.getDate("تاريخ_الشراء").toLocalDate());
                receipt.setPaying(resultSet.getDouble("الدفع"));

                list.add(receipt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list.get(0);
    }

    public Receipt getArchive(int id) {
        connectDatabase();
        ArrayList<Receipt> list = new ArrayList<>();
        String query = "SELECT * FROM فاتورة_شراء_الدواء WHERE ارشيف = 1 AND المعرف = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,id);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Receipt receipt = new Receipt();
                receipt.setId(resultSet.getInt("المعرف"));
                receipt.setIdProvider(resultSet.getInt("معرف_المورد"));
                receipt.setNumber(resultSet.getInt("رقم_الفاتورة"));
                receipt.setDate(resultSet.getDate("تاريخ_الشراء").toLocalDate());
                receipt.setPaying(resultSet.getDouble("الدفع"));

                list.add(receipt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list.get(0);
    }

    public ArrayList<Receipt> getAllByProvider(int idProvider) {
        connectDatabase();
        ArrayList<Receipt> list = new ArrayList<>();
        String query = "SELECT * FROM فاتورة_شراء_الدواء WHERE ارشيف = 0 AND معرف_المورد = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idProvider);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Receipt receipt = new Receipt();
                receipt.setId(resultSet.getInt("المعرف"));
                receipt.setIdProvider(resultSet.getInt("معرف_المورد"));
                receipt.setNumber(resultSet.getInt("رقم_الفاتورة"));
                receipt.setDate(resultSet.getDate("تاريخ_الشراء").toLocalDate());
                receipt.setPaying(resultSet.getDouble("الدفع"));

                list.add(receipt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public ArrayList<Receipt> getAllByDate(LocalDate dateFirst, LocalDate dateSecond) {
        connectDatabase();
        ArrayList<Receipt> list = new ArrayList<>();
        String query = "SELECT * FROM فاتورة_شراء_الدواء WHERE ارشيف = 0 AND تاريخ_الشراء BETWEEN ? AND ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDate(1,Date.valueOf(dateFirst));
            preparedStmt.setDate(2,Date.valueOf(dateSecond));
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Receipt receipt = new Receipt();
                receipt.setId(resultSet.getInt("المعرف"));
                receipt.setIdProvider(resultSet.getInt("معرف_المورد"));
                receipt.setNumber(resultSet.getInt("رقم_الفاتورة"));
                receipt.setDate(resultSet.getDate("تاريخ_الشراء").toLocalDate());
                receipt.setPaying(resultSet.getDouble("الدفع"));

                list.add(receipt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public ArrayList<Receipt> getAllByDateProvider(int idProvider, LocalDate dateFirst, LocalDate dateSecond) {
        connectDatabase();
        ArrayList<Receipt> list = new ArrayList<>();
        String query = "SELECT * FROM فاتورة_شراء_الدواء WHERE ارشيف = 0 AND معرف_المورد = ? AND تاريخ_الشراء BETWEEN ? AND ? ;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,idProvider);
            preparedStmt.setDate(2,Date.valueOf(dateFirst));
            preparedStmt.setDate(3,Date.valueOf(dateSecond));
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Receipt receipt = new Receipt();
                receipt.setId(resultSet.getInt("المعرف"));
                receipt.setIdProvider(resultSet.getInt("معرف_المورد"));
                receipt.setNumber(resultSet.getInt("رقم_الفاتورة"));
                receipt.setDate(resultSet.getDate("تاريخ_الشراء").toLocalDate());
                receipt.setPaying(resultSet.getDouble("الدفع"));

                list.add(receipt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }

    public int getLastNumber(){
        connectDatabase();
        int nbr = 0;
        try {
            String query = "SELECT فاتورة_شراء_الدواء.رقم_الفاتورة , فاتورة_شراء_الدواء.المعرف  FROM فاتورة_شراء_الدواء ORDER BY(المعرف) DESC LIMIT 1 ;";
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

    public boolean AddToArchive(Receipt receipt){
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE فاتورة_شراء_الدواء SET ارشيف = 1 WHERE المعرف = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,receipt.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1){
                query = "UPDATE وصل_توصيل_الدواء SET ارشيف = 1 WHERE معرف_الفاتورة = ?;";
                preparedStmt = conn.prepareStatement(query);
                preparedStmt.setInt(1,receipt.getId());
                preparedStmt.executeUpdate();
                upd = true;
            }
        } catch (SQLException e) {            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    public boolean DeleteFromArchive(Receipt receipt){
        connectDatabase();
        boolean upd = false;
        String query = "UPDATE فاتورة_شراء_الدواء SET ارشيف = 0 WHERE المعرف = ?;";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,receipt.getId());
            int update = preparedStmt.executeUpdate();
            if(update != -1){
                query = "UPDATE وصل_توصيل_الدواء SET ارشيف = 0 WHERE معرف_الفاتورة = ?;";
                preparedStmt = conn.prepareStatement(query);
                preparedStmt.setInt(1,receipt.getId());
                preparedStmt.executeUpdate();
                upd = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return upd;
    }

    public ArrayList<Receipt> getAllArchive() {
        connectDatabase();
        ArrayList<Receipt> list = new ArrayList<>();
        String query = "SELECT * FROM فاتورة_شراء_الدواء WHERE ارشيف = 1";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                Receipt receipt = new Receipt();
                receipt.setId(resultSet.getInt("المعرف"));
                receipt.setIdProvider(resultSet.getInt("معرف_المورد"));
                receipt.setNumber(resultSet.getInt("رقم_الفاتورة"));
                receipt.setDate(resultSet.getDate("تاريخ_الشراء").toLocalDate());
                receipt.setPaying(resultSet.getDouble("الدفع"));

                list.add(receipt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDatabase();
        return list;
    }
}
