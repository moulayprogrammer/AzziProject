package Controllers.ProductionsController;

import BddPackage.*;
import Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class UpdateController implements Initializable {

    @FXML
    TextField tfQte,tfPrice,tfQteMaterial,tfQteNew,tfPriceNew;
    @FXML
    ComboBox<String> cbProduct,cbMaterial;
    @FXML
    Button btnInsert;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;

    private final ProductionOperation operation = new ProductionOperation();
    private final ProductOperation productOperation = new ProductOperation();
    private final ComponentStoreRawMaterialOperation componentStoreMaterialOperation = new ComponentStoreRawMaterialOperation();
    private final ComponentStoreRawMaterialTempOperation componentStoreRawMaterialTempOperation = new ComponentStoreRawMaterialTempOperation();

    private final ObservableList<String> dataComboProduct = FXCollections.observableArrayList();
    private final ObservableList<String> dataComboMaterial = FXCollections.observableArrayList();
    private final ArrayList<Integer> listIdProduct = new ArrayList<>();
    private final ArrayList<Integer> listIdMaterial = new ArrayList<>();
    private final ArrayList<ComponentStoreTemp> componentStoreMaterialTemp = new ArrayList<>();
    private final ArrayList<ComponentStore> componentStoreMaterial = new ArrayList<>();

    private Production productionSelected;
    private double priceProduction;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();

        refreshComboProduct();


//        refreshComponent();

        cbProduct.setEditable(true);
        cbProduct.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        TextFields.bindAutoCompletion(cbProduct.getEditor(), cbProduct.getItems());

        cbMaterial.setEditable(true);
        cbMaterial.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        TextFields.bindAutoCompletion(cbMaterial.getEditor(), cbMaterial.getItems());
    }

    public void Init(Production production){
        this.productionSelected = production;

        cbProduct.getSelectionModel().select(listIdProduct.indexOf(productionSelected.getIdProduct()));
        tfQte.setText(String.valueOf(productionSelected.getQteProduct()));
        tfPrice.setText(String.format(Locale.FRANCE, "%,.2f", productionSelected.getPrice() * productionSelected.getQteProduct() ));

//        this.priceProduction = productionSelected.getPrice() * productionSelected.getQteProduct();

        refreshComboMaterial();
//        getComponentProduction();
    }

    private void refreshComboProduct(){
        dataComboProduct.clear();
        listIdProduct.clear();
        try {
            ArrayList<Product> products = productOperation.getAll();

            products.forEach(product -> {
                dataComboProduct.add(product.getName());
                listIdProduct.add(product.getId());
            });

            cbProduct.setItems(dataComboProduct);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshComboMaterial(){
        dataComboMaterial.clear();
        listIdMaterial.clear();
        try {
            if (conn.isClosed()) conn = connectBD.connect();

            String query = "SELECT خلطة_المواد_الخام.معرف_المادة_الخام, المواد_الخام.الاسم  FROM خلطة_المواد_الخام, المواد_الخام  WHERE معرف_المنتج = ? AND معرف_المادة_الخام = المعرف";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, this.productionSelected.getIdProduct());
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){
                dataComboMaterial.add(resultSet.getString("الاسم"));
                listIdMaterial.add(resultSet.getInt("معرف_المادة_الخام"));
            }

            cbMaterial.setItems(dataComboMaterial);
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void countPrice(){
        try {
            String stQte = tfQteMaterial.getText().trim();
            int indexMat = cbMaterial.getSelectionModel().getSelectedIndex();
            String stNewQte = tfQteNew.getText().trim();

            if (indexMat != -1 && !stQte.isEmpty() && !stNewQte.isEmpty()){
                int idMat = listIdMaterial.get(indexMat);
                int qte = Integer.parseInt(stQte);
                if (checkQte(idMat,qte)) {
                    priceProduction = 0;
                    componentStoreMaterialTemp.clear();
                    componentStoreMaterial.clear();

                    ArrayList<ComponentStore> CSM = componentStoreMaterialOperation.getAllByMaterialOrderByDate(listIdMaterial.get(indexMat));
                    int qteNeed = qte;

                    for (ComponentStore componentStore : CSM) {

                        double qteStored = componentStore.getQteStored();
                        double qteConsumed = componentStore.getQteConsumed();
                        double price = componentStore.getPrice();

                        ComponentStoreTemp componentStoreTemp = new ComponentStoreTemp();
                        componentStoreTemp.setIdComponent(componentStore.getIdComponent());
                        componentStoreTemp.setIdDeliveryArrival(componentStore.getIdDeliveryArrival());

                        if ((qteStored - qteConsumed) >= qteNeed) {

                            priceProduction += qteNeed * price;
                            componentStoreTemp.setQte(qteNeed);
                            componentStoreMaterialTemp.add(componentStoreTemp);
                            componentStore.setQteConsumed(qteConsumed + qteNeed);
                            componentStoreMaterial.add(componentStore);
                            break;

                        } else {

                            double qteRest = qteStored - qteConsumed;
                            qteNeed -= qteRest;
                            priceProduction += qteRest * price;
                            componentStoreTemp.setQte(qteRest);
                            componentStoreMaterialTemp.add(componentStoreTemp);
                            componentStore.setQteConsumed(qteConsumed + qteRest);
                            componentStoreMaterial.add(componentStore);
                        }
                    }

                    priceProduction += (productionSelected.getPrice() * productionSelected.getQteProduct()) ;

                    tfPriceNew.setText(String.format(Locale.FRANCE, "%,.2f", priceProduction));
                }else {
                    Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                    alertWarning.setHeaderText("الكمية كبيرة ");
                    alertWarning.setContentText("كمية المواد الاولية المتوفرة لا تكفي لانتاج هذه الكمية");
                    alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    alertWarning.initOwner(this.btnInsert.getScene().getWindow());
                    Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.setText("موافق");
                    alertWarning.showAndWait();
                }
            }else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("تحذير ");
                alertWarning.setContentText("الرجاء ملأ جميع الخانات و اختيار المنتج لحساب سعر الانتاج");
                alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                alertWarning.initOwner(this.btnInsert.getScene().getWindow());
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");
                alertWarning.showAndWait();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean checkQte(int idMat, int qte){
        boolean ex = true;
        try {
            if (conn.isClosed()) conn = connectBD.connect();

            try {
                String query = "SELECT sum(كمية_مخزنة - كمية_مستهلكة) as كمية FROM تخزين_المواد_الخام WHERE معرف_المادة_الخام = ? ;";
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setInt(1,idMat);
                ResultSet resultSet = preparedStmt.executeQuery();
                int qteComponent = 0 ;
                if (resultSet.next()){
                    qteComponent = resultSet.getInt("كمية");
                }
                if (qteComponent < qte) {
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

    @FXML
    private void ActionAnnulledAdd(){
        closeDialog(btnInsert);
    }

    @FXML
    void ActionUpdate() {

        String stQte = tfQteNew.getText().trim();
        String stPrice = tfPriceNew.getText().trim();

        if ( !stQte.isEmpty() && !stPrice.isEmpty() ){

            Production production = new Production();

            int qte =  Integer.parseInt(stQte);
            production.setQteProduct(qte);
            production.setPrice(priceProduction / qte);

            boolean update = update(production);
            if (update) {
                insertComponent();
                ActionAnnulledAdd();
            }else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("تحذير ");
                alertWarning.setContentText("خطأ غير معروف");
                alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                alertWarning.initOwner(this.btnInsert.getScene().getWindow());
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");
                alertWarning.showAndWait();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء ملأ جميع الحقول");
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            alertWarning.initOwner(this.btnInsert.getScene().getWindow());
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    private void insertComponent() {


        for (int i = 0; i < componentStoreMaterialTemp.size(); i++) {
            ComponentStoreTemp componentStoreTemp = componentStoreMaterialTemp.get(i);

            componentStoreTemp.setIdProduction(productionSelected.getId());
            insertComponentStoreTempMaterial(componentStoreTemp);
            updateQteComponentStoreMaterial(componentStoreMaterial.get(i));
        }
    }

    private boolean update(Production production)  {
        boolean update = false;
        try {
            update = operation.update(production,productionSelected);
            return update;
        }catch (Exception e){
            e.printStackTrace();
            return update;
        }
    }

    private void insertComponentStoreTempMaterial(ComponentStoreTemp storeProductTemp){
        boolean insert = false;
        try {
            insert = componentStoreRawMaterialTempOperation.insert(storeProductTemp);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateQteComponentStoreMaterial(ComponentStore store){
        boolean update = false;
        try {
            update = componentStoreMaterialOperation.updateQte(store);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }

}
