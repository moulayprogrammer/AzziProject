package Controllers.InvoiceController;

import BddPackage.ConnectBD;
import Models.ComponentInvoice;
import Models.Product;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Locale;
import java.util.ResourceBundle;

public class UpdateSaleController implements Initializable {

    @FXML
    TextField tfQte,tfPriceProductionUnit,tfPriceProduction,tfPriceUnit,tfPrice,tfNetProfit;
    @FXML
    private Button btnSale;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;

    private ComponentInvoice selectedComponentInvoice;
    private Product selectedProduct;
    private double price;
    private double cost;
    private int qte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();

        tfQte.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) tfQte.setText(String.valueOf(qte));
            else Count();
        });

        tfPriceUnit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                tfPriceUnit.setText(String.valueOf(price / qte));
                tfPrice.setText(String.format(Locale.FRANCE, "%,.2f", price ));
                tfNetProfit.setText(String.format(Locale.FRANCE, "%,.2f", (price - cost) ));
            }
            else CountProfit();
        });
    }

    public void Init(Product product, ComponentInvoice componentInvoice){

        this.selectedComponentInvoice = componentInvoice;
        this.selectedProduct = product;
        tfQte.setText(String.valueOf(selectedComponentInvoice.getQte()));
        Count();

    }

    private void Count(){
        String stQte = tfQte.getText().trim();
        if (!stQte.isEmpty() && !stQte.equals("0")){
            qte = Integer.parseInt(stQte);
            if (qte <= this.selectedProduct.getQte() ) {
                try {
                    if (conn.isClosed()) conn = connectBD.connect();
                    price = 0.0;
                    cost = 0.0;

                    try {
                        String query = "SELECT كمية_مخزنة - كمية_مستهلكة AS الكمية, سعر_البيع, التكلفة, الكمية_المنتجة FROM تخزين_منتج, الانتاج" +
                                " WHERE  تخزين_منتج.معرف_المنتج = ? AND  كمية_مخزنة - كمية_مستهلكة > 0 AND الانتاج.المعرف = تخزين_منتج.معرف_الانتاج ORDER BY (تاريخ_التخزين) ASC;";
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setInt(1,this.selectedProduct.getId());
                        ResultSet resultSet = preparedStmt.executeQuery();

                        while (resultSet.next()){
                            int qtePro = resultSet.getInt("الكمية");
                            double pricePro = resultSet.getDouble("سعر_البيع");
                            double costPro = resultSet.getDouble("التكلفة") / resultSet.getInt("الكمية_المنتجة");

                            if (qtePro >= qte){
                                price += (pricePro * qte);
                                cost += (costPro * qte );
                                break;
                            }else {
                                price += (pricePro * qtePro);
                                cost += (costPro * qtePro);
                                qte -= qtePro;
                            }
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    tfPriceProductionUnit.setText(String.format(Locale.FRANCE, "%,.2f",cost/qte ));
                    tfPriceProduction.setText(String.format(Locale.FRANCE, "%,.2f", cost ));
                    tfPriceUnit.setText(String.valueOf(price/qte));
                    tfPrice.setText(String.format(Locale.FRANCE, "%,.2f", price ));
                    tfNetProfit.setText(String.format(Locale.FRANCE, "%,.2f", (price - cost) ));

                    conn.close();
                }catch (Exception e){
                    e.printStackTrace();
                }


            }else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("الكمية ");
                alertWarning.setContentText("الكمية غير متوفرة");
                alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");
                alertWarning.showAndWait();

                tfQte.setText(String.valueOf(this.selectedProduct.getQte()));
            }

        }else {
            tfPriceProductionUnit.setText("0");
            tfPriceProduction.setText("0");
            tfPriceUnit.setText("0");
            tfPrice.setText("0");
            tfNetProfit.setText("0");
        }
    }

    private void CountProfit(){
        String stQte = tfQte.getText().trim();
        if (!stQte.equals("0")) {

            try {
                String stPriceNew = tfPriceUnit.getText().trim();
                double priceNew = Double.parseDouble(stPriceNew);


                tfPrice.setText(String.format(Locale.FRANCE, "%,.2f", priceNew * qte));
                tfNetProfit.setText(String.format(Locale.FRANCE, "%,.2f", ((priceNew * qte) - cost)));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void ActionAdd(){
        int qte = Integer.parseInt(tfQte.getText().trim());
        double PriceU = Double.parseDouble(tfPriceUnit.getText().trim());

        selectedComponentInvoice.setIdComponent(this.selectedProduct.getId());
        selectedComponentInvoice.setPrice(PriceU);
        selectedComponentInvoice.setQte(qte);

        ActionAnnul();
    }

    @FXML
    private void ActionAnnul(){
        closeDialog(btnSale);
    }

    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }
}
