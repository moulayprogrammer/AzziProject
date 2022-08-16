package Controllers.ProductionsController;

import BddPackage.*;
import Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddController implements Initializable {


    @FXML
    TextField tfQte,tfPrice;
    @FXML
    ComboBox<String> cbProduct;
    @FXML
    DatePicker dpDate;
    @FXML
    Button btnInsert;


    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;

    private final ProductionOperation operation = new ProductionOperation();
    private final ProductOperation productOperation = new ProductOperation();
    private final ComponentProductionRawMaterialOperation componentProductionRawMaterialOperation = new ComponentProductionRawMaterialOperation();
    private final ComponentProductionMedicationOperation componentProductionMedicationOperation = new ComponentProductionMedicationOperation();
    private final ComponentStoreMedicationOperation componentStoreMedicationOperation = new ComponentStoreMedicationOperation();
    private final ComponentStoreRawMaterialOperation componentStoreMaterialOperation = new ComponentStoreRawMaterialOperation();
    private final ComponentStoreMedicationTempOperation componentStoreMedicationTempOperation = new ComponentStoreMedicationTempOperation();
    private final ComponentStoreRawMaterialTempOperation componentStoreRawMaterialTempOperation = new ComponentStoreRawMaterialTempOperation();

    private final ObservableList<String> dataComboProduct = FXCollections.observableArrayList();
    private final ArrayList<Integer> listIdProduct = new ArrayList<>();
    private ArrayList<ComponentProduction> componentProductionsMedication = new ArrayList<>();
    private ArrayList<ComponentProduction> componentProductionsMaterial = new ArrayList<>();
    private final ArrayList<ComponentStoreTemp> componentStoreMedicationTemp = new ArrayList<>();
    private final ArrayList<ComponentStoreTemp> componentStoreMaterialTemp = new ArrayList<>();
    private final ArrayList<ComponentStore> componentStoreMedication = new ArrayList<>();
    private final ArrayList<ComponentStore> componentStoreMaterial = new ArrayList<>();

    private Product productSelected;
    private double priceProduction;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();

        refreshComboProduct();
        dpDate.setValue(LocalDate.now());

//        refreshComponent();

        cbProduct.setEditable(true);
        cbProduct.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        TextFields.bindAutoCompletion(cbProduct.getEditor(), cbProduct.getItems());
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

    @FXML
    private void comboSelect(){
        try {
            int index = cbProduct.getSelectionModel().getSelectedIndex();
            if (index != -1 ) {
                productSelected = productOperation.get(listIdProduct.get(index));
                getComponentProduction();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getComponentProduction(){
        try {
            componentProductionsMedication = componentProductionMedicationOperation.getAllByProduct(productSelected.getId());
            componentProductionsMaterial = componentProductionRawMaterialOperation.getAllByProduct(productSelected.getId());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void countPrice(){
        try {
            String stQte = tfQte.getText().trim();
            if (productSelected.getName() != null && !stQte.isEmpty()) {
                int qteProduction = Integer.parseInt(stQte);
                if (checkQte(qteProduction)){
                    priceProduction = 0;
                    componentStoreMaterialTemp.clear();
                    componentStoreMedicationTemp.clear();
                    componentStoreMedication.clear();
                    componentStoreMaterial.clear();

                    for (ComponentProduction production : componentProductionsMedication) {
                        ArrayList<ComponentStore> CSM = componentStoreMedicationOperation.getAllByMedicationOrderByDate(production.getIdComponent());

                        int qteNeed = production.getQte() * qteProduction;
                        for (ComponentStore store : CSM) {

                            int qteStored = store.getQteStored();
                            int qteConsumed = store.getQteConsumed();
                            double price = store.getPrice();

                            ComponentStoreTemp componentStoreTemp = new ComponentStoreTemp();
                            componentStoreTemp.setIdComponent(store.getIdComponent());
                            componentStoreTemp.setIdDeliveryArrival(store.getIdDeliveryArrival());

                            if ((qteStored - qteConsumed) >= qteNeed) {

                                priceProduction += qteNeed * price;
                                componentStoreTemp.setQte(qteNeed);
                                componentStoreMedicationTemp.add(componentStoreTemp);
                                store.setQteConsumed(qteConsumed + qteNeed);
                                componentStoreMedication.add(store);
                                break;

                            } else {

                                int qteRest = qteStored - qteConsumed;
                                qteNeed -= qteRest;
                                priceProduction += qteRest * price;
                                componentStoreTemp.setQte(qteRest);
                                componentStoreMedicationTemp.add(componentStoreTemp);
                                store.setQteConsumed(qteConsumed + qteRest);
                                componentStoreMedication.add(store);
                            }
                        }
                    }
                    for (ComponentProduction production : componentProductionsMaterial) {
                        ArrayList<ComponentStore> CSM = componentStoreMaterialOperation.getAllByMaterialOrderByDate(production.getIdComponent());

                        int qteNeed = production.getQte() * qteProduction;
                        for (ComponentStore store : CSM) {

                            int qteStored = store.getQteStored();
                            int qteConsumed = store.getQteConsumed();
                            double price = store.getPrice();

                            ComponentStoreTemp componentStoreTemp = new ComponentStoreTemp();
                            componentStoreTemp.setIdComponent(store.getIdComponent());
                            componentStoreTemp.setIdDeliveryArrival(store.getIdDeliveryArrival());

                            if ((qteStored - qteConsumed) >= qteNeed) {

                                priceProduction += qteNeed * price;
                                componentStoreTemp.setQte(qteNeed);
                                componentStoreMaterialTemp.add(componentStoreTemp);
                                store.setQteConsumed(qteConsumed + qteNeed);
                                componentStoreMaterial.add(store);
                                break;

                            } else {

                                int qteRest = qteStored - qteConsumed;
                                qteNeed -= qteRest;
                                priceProduction += qteRest * price;
                                componentStoreTemp.setQte(qteRest);
                                componentStoreMaterialTemp.add(componentStoreTemp);
                                store.setQteConsumed(qteConsumed + qteRest);
                                componentStoreMaterial.add(store);
                            }
                        }
                    }
                    tfPrice.setText(String.format(Locale.FRANCE, "%,.2f", priceProduction));
                }else {
                    Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                    alertWarning.setHeaderText("الكمية كبيرة ");
                    alertWarning.setContentText("كمية المواد الاولية المتوفرة لا تكفي لانتاج هذه الكمية");
                    alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.setText("موافق");
                    alertWarning.showAndWait();
                }
            }else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("تحذير ");
                alertWarning.setContentText("الرجاء ملأ جميع الخانات و اختيار المنتج لحساب سعر الانتاج");
                alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");
                alertWarning.showAndWait();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean checkQte(int qte){
        boolean ex = true;
        try {
            if (conn.isClosed()) conn = connectBD.connect();
            for (ComponentProduction componentProduction : componentProductionsMedication) {
                try {
                    String query = "SELECT sum(كمية_مخزنة - كمية_مستهلكة) as كمية FROM تخزين_الادوية WHERE معرف_الدواء = ? ;";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1, componentProduction.getIdComponent());
                    ResultSet resultSet = preparedStmt.executeQuery();
                    int qteComponent = 0;
                    if (resultSet.next()) {
                        qteComponent = resultSet.getInt("كمية");
                    }
                    if (qteComponent < (qte * componentProduction.getQte())) {
                        ex = false;
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (ComponentProduction componentProduction : componentProductionsMaterial) {
                try {
                    String query = "SELECT sum(كمية_مخزنة - كمية_مستهلكة) as كمية FROM تخزين_المواد_الخام WHERE معرف_المادة_الخام = ? ;";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1, componentProduction.getIdComponent());
                    ResultSet resultSet = preparedStmt.executeQuery();
                    int qteComponent = 0;
                    if (resultSet.next()) {
                        qteComponent = resultSet.getInt("كمية");
                    }
                    if (qteComponent < (qte * componentProduction.getQte())) {
                        ex = false;
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
    void ActionInsert(ActionEvent event) {

        LocalDate date = dpDate.getValue();
        String stQte = tfQte.getText().trim();
        String stPrice = tfPrice.getText().trim();
        int indexPr = cbProduct.getSelectionModel().getSelectedIndex();

        if (date != null && !stQte.isEmpty() && !stPrice.isEmpty() && indexPr != -1){

            Production production = new Production();
            production.setDate(date);
            production.setIdProduct(listIdProduct.get(indexPr));
            production.setQteProduct(Integer.parseInt(stQte));
            production.setPrice(priceProduction / Integer.parseInt(stQte) );

            int ins = insert(production);
            if (ins != -1 ) {
                insertComponent( ins);
                ActionAnnulledAdd();
            }else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("تحذير ");
                alertWarning.setContentText("خطأ غير معروف");
                alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");
                alertWarning.showAndWait();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء ملأ جميع الحقول");
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    private void insertComponent(int idProduction) {

        for (int i = 0; i < componentStoreMedicationTemp.size(); i++) {
            ComponentStoreTemp componentStoreTemp = componentStoreMedicationTemp.get(i);

            componentStoreTemp.setIdProduction(idProduction);
            insertComponentStoreTempMedication(componentStoreTemp);
            updateQteComponentStoreMedication(componentStoreMedication.get(i));

        }

        for (int i = 0; i < componentStoreMaterialTemp.size(); i++) {
            ComponentStoreTemp componentStoreTemp = componentStoreMaterialTemp.get(i);

            componentStoreTemp.setIdProduction(idProduction);
            insertComponentStoreTempMaterial(componentStoreTemp);
            updateQteComponentStoreMaterial(componentStoreMaterial.get(i));
        }
    }

    private int insert(Production production)  {
        int insert = 0;
        try {
            insert = operation.insertId(production);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private boolean insertComponentStoreTempMedication(ComponentStoreTemp storeProductTemp){
        boolean insert = false;
        try {
            insert = componentStoreMedicationTempOperation.insert(storeProductTemp);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private boolean insertComponentStoreTempMaterial(ComponentStoreTemp storeProductTemp){
        boolean insert = false;
        try {
            insert = componentStoreRawMaterialTempOperation.insert(storeProductTemp);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private boolean updateQteComponentStoreMedication(ComponentStore store){
        boolean update = false;
        try {
            update = componentStoreMedicationOperation.updateQte(store);
            return update;
        }catch (Exception e){
            e.printStackTrace();
            return update;
        }
    }

    private boolean updateQteComponentStoreMaterial( ComponentStore store){
        boolean update = false;
        try {
            update = componentStoreMaterialOperation.updateQte(store);
            return update;
        }catch (Exception e){
            e.printStackTrace();
            return update;
        }
    }


    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }

}
