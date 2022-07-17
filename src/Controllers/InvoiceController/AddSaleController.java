package Controllers.InvoiceController;

import BddPackage.ConnectBD;
import Models.ComponentProduction;
import Models.Product;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class AddSaleController implements Initializable {

    @FXML
    TextField tfPriceProduction,tfPriceSale,tfQte,tfPriceGlobal,tfNetProfit;
    @FXML
    private Button btnSale;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;

    private Product selectedProduct;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();
    }

    public void Init(Product product){

        this.selectedProduct = product;
        tfQte.setText(String.valueOf(selectedProduct.getQte()));
        ActionCount();
    }

    @FXML
    private void ActionCount(){
        String stQte = tfQte.getText().trim();
        if (!stQte.isEmpty()){
            int qte = Integer.parseInt(stQte);
            if (checkQte(qte)) {
//                double cost = getCost();
                double price = getPrice(qte);

            }

        }
    }

    private boolean checkQte(int qte){
        boolean ex = true;
        try {
            if (conn.isClosed()) conn = connectBD.connect();

            try {
                String query = "SELECT sum(كمية_مخزنة - كمية_مستهلكة ) AS كمية FROM تخزين_منتج WHERE معرف_المنتج = ? AND كمية_مخزنة - كمية_مستهلكة > 0 ;";
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setInt(1,this.selectedProduct.getId());
                ResultSet resultSet = preparedStmt.executeQuery();
                if (resultSet.next()){
                    int qteProduct = resultSet.getInt("كمية");
                    if (qteProduct < qte ) ex = false;
                }else {
                    ex = false;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ex;
    }

    private double getPrice(int qte){
        double price = 0;
        try {
            if (conn.isClosed()) conn = connectBD.connect();
            String query = "SELECT كمية_مخزنة - كمية_مستهلكة AS الكمية, سعر_البيع FROM تخزين_منتج WHERE" +
                    " معرف_المنتج = ? AND  كمية_مخزنة - كمية_مستهلكة > 0 ORDER BY (تاريخ_التخزين) ASC;";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,this.selectedProduct.getId());
            ResultSet resultSet = preparedStmt.executeQuery();

            while (resultSet.next()){
                int qtePro = resultSet.getInt("الكمية");
                int pricePro = resultSet.getInt("سعر_البيع");

                if (qtePro >= qte){
                    price += (pricePro * qte);
                    break;
                }else {
                    price += (pricePro * qtePro);
                    qte -= qtePro;
                }
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }

    private double getCost(int qte){
        double cost = 0;

        return cost;
    }

    @FXML
    private void ActionAdd(){

    }

    @FXML
    private void ActionAnnul(){
        closeDialog(btnSale);
    }

    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }
}
