package Controllers.StatisticControllers;

import BddPackage.ClientOperation;
import BddPackage.ComponentInvoiceOperation;
import BddPackage.ConnectBD;
import BddPackage.InvoiceOperation;
import Models.Client;
import Models.ComponentInvoice;
import Models.Invoice;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class MainController implements Initializable {

    @FXML
    Label lbSumDebtPorchesRM,lbSumPayPorchesRM,lbSumPorchesRM;
    @FXML
    Label lbSumDebtPorchesM,lbSumPayPorchesM,lbSumPorchesM;
    @FXML
    Label lbSumDebtSales,lbSumPaySales,lbSumSales;
    @FXML
    Label lbSumDebtSalesC,lbSumPaySalesC,lbSumSalesC;
    @FXML
    Label lbQteStoreRM,lbSumStoreRM;
    /*  @FXML
    Label lbQteStoreM,lbSumStoreM;
    @FXML
    Label lbQteStorePr,lbSumStorePr;*/
    @FXML
    Label lbQteDamageRM,lbSumDamageRM;
    @FXML
    Label lbQteDamageM,lbSumDamageM;
    @FXML
    Label lbQteDamagePr,lbSumDamagePr;


    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;

    private final InvoiceOperation invoiceOperation = new InvoiceOperation();
    private final ClientOperation clientOperation = new ClientOperation();
    private final ComponentInvoiceOperation componentInvoiceOperation = new ComponentInvoiceOperation();

    private double sumDebtPorchesRM = 0.0, sumPayPorchesRM = 0.0, sumPorchesRM = 0.0;
    private double sumDebtPorchesM = 0.0, sumPayPorchesM = 0.0, sumPorchesM = 0.0;
    private double sumDebtSales = 0.0, sumPaySales = 0.0, sumSales = 0.0;
    private double sumDebtSalesC = 0.0, sumPaySalesC = 0.0, sumSalesC = 0.0;
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

    private void Sales(){
        try {
            ArrayList<Invoice> invoices = invoiceOperation.getAll();

            AtomicReference<Double> sumAmount = new AtomicReference<>(0.0);
            AtomicReference<Double> sumPaying = new AtomicReference<>(0.0);
            AtomicReference<Double> sumDebt = new AtomicReference<>(0.0);

            AtomicReference<Double> sumAmountC = new AtomicReference<>(0.0);
            AtomicReference<Double> sumPayingC = new AtomicReference<>(0.0);
            AtomicReference<Double> sumDebtC = new AtomicReference<>(0.0);

            invoices.forEach(invoice -> {

                ArrayList<ComponentInvoice> componentInvoices = componentInvoiceOperation.getAllByInvoice(invoice.getId());
                AtomicReference<Double> sumR = new AtomicReference<>(0.0);
                componentInvoices.forEach(componentInvoice -> {
                    double pr = componentInvoice.getPrice() * componentInvoice.getQte();
                    sumR.updateAndGet(v -> (double) (v + pr));
                });

                if (invoice.getConfirmation().equals("مأكد")){
                    sumAmount.updateAndGet(v -> v + sumR.get());
                    sumPaying.updateAndGet(v -> v + invoice.getPaying());
                    sumDebt.updateAndGet(v -> v + (sumR.get() - invoice.getPaying()));
                }else {
                    sumAmountC.updateAndGet(v -> v + sumR.get());
                    sumPayingC.updateAndGet(v -> v + invoice.getPaying());
                    sumDebtC.updateAndGet(v -> v + (sumR.get() - invoice.getPaying()));
                }
            });

            this.sumSales = sumAmount.get();
            this.sumDebtSales = sumDebt.get();
            this.sumPaySales = sumPaying.get();

            this.sumSalesC = sumAmountC.get();
            this.sumDebtSalesC = sumDebtC.get();
            this.sumPaySalesC = sumPayingC.get();

            lbSumSales.setText(String.format(Locale.FRANCE, "%,.2f", sumSales));
            lbSumDebtSales.setText(String.format(Locale.FRANCE, "%,.2f", sumDebtSales));
            lbSumPaySales.setText(String.format(Locale.FRANCE, "%,.2f", sumPaySales));

            lbSumSalesC.setText(String.format(Locale.FRANCE, "%,.2f", sumSalesC));
            lbSumDebtSalesC.setText(String.format(Locale.FRANCE, "%,.2f", sumDebtSalesC));
            lbSumPaySalesC.setText(String.format(Locale.FRANCE, "%,.2f", sumPaySalesC));

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

            query = "SELECT sum((تخزين_المواد_الخام.كمية_مخزنة - تخزين_المواد_الخام.كمية_مستهلكة) *  تخزين_المواد_الخام.سعر_الوحدة ) AS  sumTotal , sum(تخزين_المواد_الخام.كمية_مخزنة - تخزين_المواد_الخام.كمية_مستهلكة) AS qteReste  FROM تخزين_المواد_الخام; ";
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

            conn.close();




            lbSumStoreRM.setText(String.format(Locale.FRANCE, "%,.2f", sumStoreRM));
            lbQteStoreRM.setText(String.valueOf(sumQteStoreRM));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void StoreM(){
        /*try {
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
        }*/
    }

    private void StorePr(){
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
