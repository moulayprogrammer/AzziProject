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

public class UpdateSaleController implements Initializable {

    @FXML
    TextField tfQte,tfPriceProductionUnit,tfPriceProduction,tfPriceUnit,tfPrice,tfNetProfit;
    @FXML
    private Button btnSale;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;

    private List<ComponentStoreProduct> storeProducts = new ArrayList<>();
    private List<ComponentStoreProductTemp> storeProductTemps = new ArrayList<>();
    private List<ComponentStoreProduct> storeProducts_ = new ArrayList<>();
    private List<ComponentStoreProductTemp> storeProductTemps_ = new ArrayList<>();
    private List<ComponentStoreProduct> storeProducts_add = new ArrayList<>();
    private List<ComponentStoreProductTemp> storeProductTemps_add = new ArrayList<>();
    private ComponentInvoice selectedComponentInvoice;
    private Product selectedProduct;
    private double price;
    private double cost;
    private int qte;
    private int qteInit;
    private boolean update;

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

    public void Init(Product product, ComponentInvoice componentInvoice , List<ComponentStoreProduct> storeProducts, List<ComponentStoreProductTemp> storeProductTemps, boolean update) {

        this.storeProducts = storeProducts;
        this.storeProductTemps = storeProductTemps;
        this.selectedComponentInvoice = componentInvoice;
        this.selectedProduct = product;
        this.update = update;

        this.qte = selectedComponentInvoice.getQte();
        this.qteInit = this.qte;
        tfQte.setText(String.valueOf(qte));

    }

    private void Count(){
        String stQte = tfQte.getText().trim();
        if (!stQte.isEmpty() && !stQte.equals("0")){
            int qte = Integer.parseInt(stQte);
                if (qte <= this.qteInit){

                    storeProducts_.clear();
                    storeProductTemps_.clear();

                    for (int i = 0; i <storeProducts.size(); i++) {
                        ComponentStoreProduct storeProduct = storeProducts.get(i);
                        ComponentStoreProductTemp storeProductTemp = storeProductTemps.get( i);

                        ComponentStoreProduct componentStoreProduct = new ComponentStoreProduct();
                        componentStoreProduct.setIdProduction(storeProduct.getIdProduction());
                        componentStoreProduct.setIdComponent(storeProduct.getIdComponent());
                        componentStoreProduct.setQteStored(storeProduct.getQteStored());
                        componentStoreProduct.setQteConsumed(storeProduct.getQteConsumed());
                        componentStoreProduct.setPriceHt(storeProduct.getPriceHt());
                        
                        ComponentStoreProductTemp componentStoreProductTemp = new ComponentStoreProductTemp();
                        componentStoreProductTemp.setId(storeProductTemp.getId());
                        componentStoreProductTemp.setIdProduct(storeProductTemp.getIdProduct());
                        componentStoreProductTemp.setIdProduction(storeProductTemp.getIdProduction());
                        componentStoreProductTemp.setIdInvoice(storeProductTemp.getIdInvoice());
                        componentStoreProductTemp.setQte(storeProductTemp.getQte());
                        
                        if (storeProductTemp.getQte() >= qte){
                            componentStoreProduct.setQteConsumed(storeProduct.getQteConsumed() - storeProductTemp.getQte() + qte);
                            componentStoreProductTemp.setQte(qte);
                            
                            storeProducts_.add(componentStoreProduct);
                            storeProductTemps_.add(componentStoreProductTemp);
                            
                            break;
                        }else {
                            qte -= storeProductTemp.getQte();

                            storeProducts_.add(componentStoreProduct);
                            storeProductTemps_.add(componentStoreProductTemp);
                        }
                    }

                    UpdateFields(Integer.parseInt(stQte));

                }else{
                    int qteRest =  qte - this.qteInit;
                    int qteComp;
                    if (update)  qteComp = this.selectedProduct.getQte();
                    else qteComp = this.selectedProduct.getQte() - this.qteInit;
                    if (qteRest <= qteComp ) {
                        try {
                            if (conn.isClosed()) conn = connectBD.connect();
                            storeProducts_add.clear();
                            storeProductTemps_add.clear();
                            storeProducts_.clear();
                            storeProductTemps_.clear();
                            storeProducts_.addAll(storeProducts);
                            storeProductTemps_.addAll(storeProductTemps);

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

                                    ComponentStoreProduct componentStoreProduct = new ComponentStoreProduct();
                                    componentStoreProduct.setIdProduction(idProduction);
                                    componentStoreProduct.setIdComponent(idProduct);
                                    componentStoreProduct.setQteConsumed(qteProConsumed);
                                    componentStoreProduct.setPriceHt(pricePro);

                                    ComponentStoreProductTemp componentStoreProductTemp = new ComponentStoreProductTemp();
                                    componentStoreProductTemp.setIdProduct(idProduct);
                                    componentStoreProductTemp.setIdProduction(idProduction);

                                    boolean qteComplete = true ;
                                    boolean notEx = true;
                                    for (ComponentStoreProductTemp productTemp : storeProductTemps_) {
                                        if (productTemp.getIdProduct() == idProduct && productTemp.getIdProduction() == idProduction){
                                            notEx = false;
                                            if (productTemp.getQte() < qtePro){
                                                qtePro -= productTemp.getQte();
                                                qteComplete = false;
                                            }
                                            break;
                                        }
                                    }
                                    if (notEx) {
                                        if (qtePro >= qteRest) {
                                            componentStoreProduct.setQteConsumed(qteProConsumed + qteRest);
                                            componentStoreProductTemp.setQte(qteRest);

                                            storeProducts_add.add(componentStoreProduct);
                                            storeProductTemps_add.add(componentStoreProductTemp);
                                            break;
                                        } else {
                                            componentStoreProduct.setQteConsumed(qteProConsumed + qtePro);
                                            componentStoreProductTemp.setQte(qtePro);

                                            storeProducts_add.add(componentStoreProduct);
                                            storeProductTemps_add.add(componentStoreProductTemp);
                                            qteRest -= qtePro;
                                        }
                                    }else if (!qteComplete){
                                        if (qtePro >= qteRest) {
                                            componentStoreProduct.setQteConsumed(qteProConsumed + qteRest);
                                            componentStoreProductTemp.setQte(qteRest);

                                            storeProducts_add.add(componentStoreProduct);
                                            storeProductTemps_add.add(componentStoreProductTemp);
                                            break;
                                        } else {
                                            componentStoreProduct.setQteConsumed(qteProConsumed + qtePro);
                                            componentStoreProductTemp.setQte(qtePro);

                                            storeProducts_add.add(componentStoreProduct);
                                            storeProductTemps_add.add(componentStoreProductTemp);
                                            qteRest -= qtePro;
                                        }
                                    }
                                }

                                if (storeProducts_add.size() != 0 ){
                                    storeProducts_.addAll(storeProducts_add);
                                    storeProductTemps_.addAll(storeProductTemps_add);

                                    UpdateFields(Integer.parseInt(stQte));
                                }

                            }catch (Exception e){
                                e.printStackTrace();
                            }

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
            }

        }else {
            tfPriceProductionUnit.setText("0");
            tfPriceProduction.setText("0");
            tfPriceUnit.setText("0");
            tfPrice.setText("0");
            tfNetProfit.setText("0");
        }
    }

    private void UpdateFields(int qte){
        try {
            double cost = 0.0;
            double price = 0.0;
            for (int i = 0; i < storeProductTemps_.size(); i++) {
                ComponentStoreProduct storeProduct = storeProducts_.get(i);
                ComponentStoreProductTemp storeProductTemp = storeProductTemps_.get(i);

                if (storeProduct.getIdProduction() == storeProductTemp.getIdProduction() && storeProduct.getIdComponent() == storeProductTemp.getIdProduct()) {
                    if (conn.isClosed()) conn = connectBD.connect();
                    try {
                        String query = "SELECT * FROM الانتاج WHERE المعرف = ?;";
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setInt(1, storeProductTemp.getIdProduction());
                        ResultSet resultSet = preparedStmt.executeQuery();

                        if (resultSet.next()) {
                            double costPro = resultSet.getDouble("التكلفة") / resultSet.getInt("الكمية_المنتجة");
                            cost += costPro * storeProductTemp.getQte();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    conn.close();
                    price += storeProduct.getPriceHt() * storeProductTemp.getQte();

                }else {
                    System.out.println();
                }

            }

            this.qte = qte;
            this.price = price;
            this.cost = cost;

            tfPriceProductionUnit.setText(String.format(Locale.FRANCE, "%,.2f",cost/qte ));
            tfPriceProduction.setText(String.format(Locale.FRANCE, "%,.2f", cost ));
            tfPriceUnit.setText(String.valueOf(price/qte));
            tfPrice.setText(String.format(Locale.FRANCE, "%,.2f", price ));
            tfNetProfit.setText(String.format(Locale.FRANCE, "%,.2f", (price - cost) ));

        }catch (Exception e){
            e.printStackTrace();
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

        selectedComponentInvoice.setIdProduct(this.selectedProduct.getId());
        selectedComponentInvoice.setPrice(PriceU);
        selectedComponentInvoice.setQte(qte);

        storeProducts.clear();
        storeProducts.addAll(storeProducts_);
        storeProductTemps.clear();
        storeProductTemps.addAll(storeProductTemps_);

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
