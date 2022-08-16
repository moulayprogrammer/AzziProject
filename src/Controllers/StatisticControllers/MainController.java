package Controllers.StatisticControllers;

import BddPackage.ConnectBD;
import Models.ComponentInvoice;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    Label lbSumDebtPorchesRM,lbSumPayPorchesRM,lbSumPorchesRM;
    @FXML
    Label lbSumDebtPorchesM,lbSumPayPorchesM,lbSumPorchesM;
    @FXML
    Label lbSumDeliveryMed,lbSumDeliveryMat;
    @FXML
    Label lbSumDebtSales,lbSumPaySales,lbSumSales;
    @FXML
    Label lbQteStoreRM,lbSumStoreRM;
    @FXML
    Label lbQteStoreM,lbSumStoreM;
    @FXML
    Label lbQteStorePr,lbSumStorePr;
    @FXML
    Label lbQteDamageRM,lbSumDamageRM;
    @FXML
    Label lbQteDamageM,lbSumDamageM;
    @FXML
    Label lbQteDamagePr,lbSumDamagePr;


    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;

    private double sumDebtPorchesRM = 0.0, sumPayPorchesRM = 0.0, sumPorchesRM = 0.0;
    private double sumDebtPorchesM = 0.0, sumPayPorchesM = 0.0, sumPorchesM = 0.0;
    private double sumDeliveryMed = 0.0, sumDeliveryMat = 0.0;
    private double sumDebtSales = 0.0, sumPaySales = 0.0, sumSales = 0.0;
    private double sumQteStoreRM = 0.0, sumStoreRM = 0.0;
    private double sumQteStoreM = 0.0, sumStoreM = 0.0;
    private double sumQteStorePr = 0.0, sumStorePr = 0.0;

    private double sumQteDamageRM = 0.0, sumDamageRM = 0.0;
    private double sumQteDamageM = 0.0, sumDamageM = 0.0;
    private double sumQteDamagePr = 0.0, sumDamagePr = 0.0;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();

        porchesRM();
        porchesM();
        delivery();
        Sales();
        StoreM();
        StoreRM();
        StorePr();
        DamageRM();
        DamageM();
        DamagePr();
    }

    private void porchesRM(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();

            String query = "SELECT  sum(فاتورة_شراء_المواد_الخام.الدفع)  AS payment, (SELECT sum(مشتريات_مواد_خام.سعر_الوحدة * مشتريات_مواد_خام.الكمية) FROM مشتريات_مواد_خام)  AS totalSum FROM فاتورة_شراء_المواد_الخام; ";
            try {
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                ResultSet resultSet = preparedStmt.executeQuery();
                if (resultSet.next()){
                    sumPayPorchesRM = resultSet.getDouble("payment");
                    sumPorchesRM = resultSet.getDouble("totalSum");
                    sumDebtPorchesRM = sumPorchesRM - sumPayPorchesRM;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            lbSumPorchesRM.setText(String.format(Locale.FRANCE, "%,.2f", sumPorchesRM));
            lbSumPayPorchesRM.setText(String.format(Locale.FRANCE, "%,.2f", sumPayPorchesRM));
            lbSumDebtPorchesRM.setText(String.format(Locale.FRANCE, "%,.2f", sumDebtPorchesRM));
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void porchesM(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();

            String query = "SELECT  sum(فاتورة_شراء_الدواء.الدفع)  AS payment, (SELECT sum(مشتريات_الدواء.سعر_الوحدة * مشتريات_الدواء.الكمية) FROM مشتريات_الدواء)  AS totalSum FROM فاتورة_شراء_الدواء; ";
            try {
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                ResultSet resultSet = preparedStmt.executeQuery();
                if (resultSet.next()){
                    sumPayPorchesM = resultSet.getDouble("payment");
                    sumPorchesM = resultSet.getDouble("totalSum");
                    sumDebtPorchesM = sumPorchesM - sumPayPorchesM;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            lbSumPorchesM.setText(String.format(Locale.FRANCE, "%,.2f", sumPorchesM));
            lbSumPayPorchesM.setText(String.format(Locale.FRANCE, "%,.2f", sumPayPorchesM));
            lbSumDebtPorchesM.setText(String.format(Locale.FRANCE, "%,.2f", sumDebtPorchesM));
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void delivery(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();

            String query = "SELECT sum(وصل_توصيل_الدواء.السعر) AS deliveryMed ,  sum(وصل_توصيل_مواد_خام.السعر) AS deliveryMat  FROM وصل_توصيل_الدواء , وصل_توصيل_مواد_خام ;";
            try {
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                ResultSet resultSet = preparedStmt.executeQuery();
                if (resultSet.next()){
                    sumDeliveryMed = resultSet.getDouble("deliveryMed");
                    sumDeliveryMat = resultSet.getDouble("deliveryMat");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            lbSumDeliveryMed.setText(String.format(Locale.FRANCE, "%,.2f", sumDeliveryMed));
            lbSumDeliveryMat.setText(String.format(Locale.FRANCE, "%,.2f", sumDeliveryMat));

            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void Sales(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();

            String query = "SELECT  sum(فاتورة_بيع.الدفع)  AS payment, (SELECT sum(بيع_منتج.سعر_الوحدة * بيع_منتج.الكمية) FROM بيع_منتج)  AS totalSum FROM فاتورة_بيع; ";
            try {
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                ResultSet resultSet = preparedStmt.executeQuery();
                if (resultSet.next()){
                    sumPaySales = resultSet.getDouble("payment");
                    sumSales = resultSet.getDouble("totalSum");
                    sumDebtSales = sumSales - sumPaySales;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            lbSumSales.setText(String.format(Locale.FRANCE, "%,.2f", sumSales));
            lbSumPaySales.setText(String.format(Locale.FRANCE, "%,.2f", sumPaySales));
            lbSumDebtSales.setText(String.format(Locale.FRANCE, "%,.2f", sumDebtSales));
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void StoreRM(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();

            String query = "SELECT sum((تخزين_المواد_الخام.كمية_مخزنة - تخزين_المواد_الخام.كمية_مستهلكة) *  تخزين_المواد_الخام.سعر_الوحدة ) AS  sumTotal , sum(تخزين_المواد_الخام.كمية_مخزنة - تخزين_المواد_الخام.كمية_مستهلكة) AS qteReste  FROM تخزين_المواد_الخام; ";
            try {
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                ResultSet resultSet = preparedStmt.executeQuery();
                if (resultSet.next()){
                    sumStoreRM = resultSet.getDouble("sumTotal");
                    sumQteStoreRM = resultSet.getDouble("qteReste");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            lbSumStoreRM.setText(String.format(Locale.FRANCE, "%,.2f", sumStoreRM));
            lbQteStoreRM.setText(String.valueOf(sumQteStoreRM));
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void StoreM(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();

            String query = "SELECT sum((تخزين_الادوية.كمية_مخزنة - تخزين_الادوية.كمية_مستهلكة) *  تخزين_الادوية.سعر_الوحدة ) AS  sumTotal , sum(تخزين_الادوية.كمية_مخزنة - تخزين_الادوية.كمية_مستهلكة) AS qteReste  FROM تخزين_الادوية; ";
            try {
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                ResultSet resultSet = preparedStmt.executeQuery();
                if (resultSet.next()){
                    sumStoreM = resultSet.getDouble("sumTotal");
                    sumQteStoreM = resultSet.getDouble("qteReste");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            lbSumStoreM.setText(String.format(Locale.FRANCE, "%,.2f", sumStoreM));
            lbQteStoreM.setText(String.valueOf(sumQteStoreM));
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void StorePr(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();

            String query = "SELECT sum((كمية_مخزنة - كمية_مستهلكة) * التكلفة) AS sumTotal , sum(كمية_مخزنة - كمية_مستهلكة) AS qteReste FROM تخزين_منتج, الانتاج WHERE تخزين_منتج.معرف_الانتاج = الانتاج.المعرف ; ";
            try {
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                ResultSet resultSet = preparedStmt.executeQuery();
                if (resultSet.next()){
                    sumQteStorePr = resultSet.getDouble("sumTotal");
                    sumStorePr = resultSet.getDouble("qteReste");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            lbSumStorePr.setText(String.format(Locale.FRANCE, "%,.2f", sumQteStorePr));
            lbQteStorePr.setText(String.valueOf(sumStorePr));
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void DamageRM(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();

            String query = "SELECT sum(الكمية * سعر_الوحدة) AS sumTotal , الكمية  FROM المواد_الخام_التالفة, تخزين_المواد_الخام WHERE المواد_الخام_التالفة.معرف_المادة = تخزين_المواد_الخام.معرف_المادة_الخام AND المواد_الخام_التالفة.معرف_التوصيل = تخزين_المواد_الخام.معرف_وصل_التوصيل";
            try {
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                ResultSet resultSet = preparedStmt.executeQuery();
                if (resultSet.next()){
                    sumDamageRM = resultSet.getDouble("sumTotal");
                    sumQteDamageRM = resultSet.getDouble("الكمية");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            lbSumDamageRM.setText(String.format(Locale.FRANCE, "%,.2f", sumDamageRM));
            lbQteDamageRM.setText(String.valueOf(sumQteDamageRM));
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void DamageM(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();

            String query = "SELECT sum(الكمية * سعر_الوحدة) AS sumTotal , الكمية  FROM الادوية_التالفة, تخزين_الادوية WHERE الادوية_التالفة.معرف_الدواء = تخزين_الادوية.معرف_الدواء AND الادوية_التالفة.معرف_التوصيل = تخزين_الادوية.معرف_وصل_التوصيل ";
            try {
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                ResultSet resultSet = preparedStmt.executeQuery();
                if (resultSet.next()){
                    sumDamageM = resultSet.getDouble("sumTotal");
                    sumQteDamageM = resultSet.getDouble("الكمية");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            lbSumDamageM.setText(String.format(Locale.FRANCE, "%,.2f", sumDamageM));
            lbQteDamageM.setText(String.valueOf(sumQteDamageM));
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void DamagePr(){
        /*try {
            if (conn.isClosed()) conn = connectBD.connect();

            String query = "SELECT sum((كمية_مخزنة - كمية_مستهلكة) * التكلفة) AS sumTotal , sum(كمية_مخزنة - كمية_مستهلكة) AS qteReste FROM تخزين_منتج, الانتاج WHERE تخزين_منتج.معرف_الانتاج = الانتاج.المعرف ; ";
            try {
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                ResultSet resultSet = preparedStmt.executeQuery();
                if (resultSet.next()){
                    sumQteStorePr = resultSet.getDouble("sumTotal");
                    sumStorePr = resultSet.getDouble("qteReste");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            lbSumStorePr.setText(String.format(Locale.FRANCE, "%,.2f", sumQteStorePr));
            lbQteStorePr.setText(String.valueOf(sumStorePr));
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

}
