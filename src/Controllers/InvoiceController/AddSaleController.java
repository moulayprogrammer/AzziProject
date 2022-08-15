package Controllers.InvoiceController;

import BddPackage.ConnectBD;
import Models.ComponentInvoice;
import Models.ComponentStoreProduct;
import Models.ComponentStoreProductTemp;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddSaleController implements Initializable {

    @FXML
    TextField tfQte,tfPriceProductionUnit,tfPriceProduction,tfPriceUnit,tfPrice,tfNetProfit;
    @FXML
    private Button btnSale;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;

    private List<ComponentStoreProduct> storeProducts = new ArrayList<>();
    private List<ComponentStoreProductTemp> storeProductTemps = new ArrayList<>();
    private ComponentInvoice componentInvoice;
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

    public void Init(Product product, ComponentInvoice componentInvoice, List<ComponentStoreProduct> storeProducts, List<ComponentStoreProductTemp> storeProductTemps){

        this.storeProducts = storeProducts;
        this.storeProductTemps = storeProductTemps;
        this.componentInvoice = componentInvoice;
        this.selectedProduct = product;
        tfQte.setText(String.valueOf(selectedProduct.getQte()));
    }

    private void Count(){
        String stQte = tfQte.getText().trim();
        if (!stQte.isEmpty() && !stQte.equals("0")){
            qte = Integer.parseInt(stQte);
            if (qte <= this.selectedProduct.getQte() ) {
                try {
                    if (conn.isClosed()) conn = connectBD.connect();
                    this.storeProducts.clear();
                    this.storeProductTemps.clear();
                    price = 0.0;
                    cost = 0.0;

                    try {

                        String query = "SELECT تخزين_منتج.معرف_الانتاج, تخزين_منتج.معرف_المنتج, كمية_مخزنة, كمية_مستهلكة, سعر_البيع, التكلفة, الكمية_المنتجة FROM تخزين_منتج, الانتاج  " +
                                "WHERE  تخزين_منتج.معرف_المنتج = ? AND  كمية_مخزنة - كمية_مستهلكة > 0 AND الانتاج.المعرف = تخزين_منتج.معرف_الانتاج ORDER BY (تاريخ_التخزين) ASC;";
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setInt(1,this.selectedProduct.getId());
                        ResultSet resultSet = preparedStmt.executeQuery();

                        while (resultSet.next()){
                            int idProduction = resultSet.getInt("معرف_الانتاج");
                            int idProduct = resultSet.getInt("معرف_المنتج");
                            int qteProConsumed = resultSet.getInt("كمية_مستهلكة");
                            int qtePro = resultSet.getInt("كمية_مخزنة") - qteProConsumed;
                            double pricePro = resultSet.getDouble("سعر_البيع");
                            double costPro = resultSet.getDouble("التكلفة") / resultSet.getInt("الكمية_المنتجة");

                            ComponentStoreProduct componentStoreProduct = new ComponentStoreProduct();
                            componentStoreProduct.setIdComponent(idProduct);
                            componentStoreProduct.setIdProduction(idProduction);
                            componentStoreProduct.setPriceHt(pricePro);

                            ComponentStoreProductTemp componentStoreProductTemp = new ComponentStoreProductTemp();
                            componentStoreProductTemp.setIdProduct(idProduct);
                            componentStoreProductTemp.setIdProduction(idProduction);

                            if (qtePro >= qte){
                                price += (pricePro * qte);
                                cost += (costPro * qte );

                                componentStoreProduct.setQteConsumed(qteProConsumed  + qte);
                                componentStoreProductTemp.setQte(qte);

                                this.storeProducts.add(componentStoreProduct);
                                this.storeProductTemps.add(componentStoreProductTemp);
                                break;
                            }else {
                                price += (pricePro * qtePro);
                                cost += (costPro * qtePro);
                                qte -= qtePro;

                                componentStoreProduct.setQteConsumed(qteProConsumed + qtePro);
                                componentStoreProductTemp.setQte(qtePro);

                                this.storeProducts.add(componentStoreProduct);
                                this.storeProductTemps.add(componentStoreProductTemp);
                            }
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    qte = Integer.parseInt(stQte);
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
                alertWarning.initOwner(this.tfNetProfit.getScene().getWindow());
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
        if (!stQte.isEmpty() && !stQte.equals("0")) {

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

        String stQte = tfQte.getText().trim();
        String stPriceU = tfPriceUnit.getText().trim();

        if (!stQte.isEmpty() && !stQte.equals("0") && !stPriceU.isEmpty() && !stPriceU.equals("0")) {
            int qte = Integer.parseInt(stQte);
            double PriceU = Double.parseDouble(stPriceU);

            componentInvoice.setIdProduct(this.selectedProduct.getId());
            componentInvoice.setPrice(PriceU);
            componentInvoice.setQte(qte);

            ActionAnnul();
        }
    }

    @FXML
    private void ActionAnnul(){
        closeDialog(btnSale);
    }

    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }
}
