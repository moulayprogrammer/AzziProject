package BddPackage;

import Models.Receipt;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReceiptOperation extends BDD<Receipt> {

    @Override
    public boolean insert(Receipt o) {
        return false;
    }

    public int insertId(Receipt receipt) {
        int ins = 0;
        String query = "INSERT INTO فاتورة_شراء_الدواء (معرف_المورد,تاريخ_الشراء, رقم_الفاتورة, الدفع)  VALUES (?,?,?,?);";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,receipt.getIdProvider());
            preparedStmt.setDate(2, Date.valueOf(receipt.getDate()));
            preparedStmt.setInt(3,receipt.getNumber());
            preparedStmt.setDouble(3,receipt.getPaying());
            int insert = preparedStmt.executeUpdate();
            if(insert != -1) ins = preparedStmt.getGeneratedKeys().getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ins;
    }

    @Override
    public boolean update(Receipt o1, Receipt o2) {
        return false;
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
        return null;
    }

    public int getLastNumber(){
        int nbr = 0;
        try {
            String query = "SELECT فاتورة_شراء_الدواء.رقم_الفاتورة , فاتورة_شراء_الدواء.المعرف  FROM فاتورة_شراء_الدواء WHERE ارشيف = 0 ORDER BY(المعرف) DESC LIMIT 1 ;";
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
        return nbr;
    }
}
