package Controllers.ProductionsController;

import BddPackage.*;
import Models.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

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
    private final ComponentProductionMedicationOperation componentProductionMedicationOperation = new ComponentProductionMedicationOperation();
    private final ComponentProductionRawMaterialOperation componentProductionMedicationOperation1 = new ComponentProductionRawMaterialOperation();

    private final RawMaterialOperation materialOperation = new RawMaterialOperation();
    private final MedicationOperation medicationOperation = new MedicationOperation();
    private final ComponentProductionRawMaterialOperation componentMaterialOperation = new ComponentProductionRawMaterialOperation();
    private final ComponentProductionMedicationOperation componentMedicationOperation = new ComponentProductionMedicationOperation();

    private final ObservableList<List<StringProperty>> dataTable = FXCollections.observableArrayList();
    private final ObservableList<String> dataComboProduct = FXCollections.observableArrayList();
    private final ArrayList<Integer> listIdProduct = new ArrayList<>();
    private final ArrayList<ArrayList<ComponentStore>> componentStores = new ArrayList<>();
    ArrayList<ComponentProduction> componentProductionsMedication = new ArrayList<>();
    ArrayList<ComponentProduction> componentProductionsMaterial = new ArrayList<>();
    private Product productSelected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();

        refreshComboProduct();


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
            componentProductionsMedication = componentMedicationOperation.getAllByProduct(productSelected.getId());
            componentProductionsMaterial = componentMaterialOperation.getAllByProduct(productSelected.getId());


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void countPrice(){
        try {
            int qteProduct = Integer.parseInt(tfQte.getText().trim());
            boolean check = checkQte(qteProduct);

            System.out.println("ex = " + check);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean checkQte(int qte){
        boolean ex = true;
        try {
            for (int i = 0; i < componentProductionsMedication.size(); i++) {
                ComponentProduction componentProduction = componentProductionsMedication.get(i);
                try {
                    String query = "SELECT sum(كمية_مخزنة - كمية_مستهلكة) as كمية FROM تخزين_الادوية WHERE معرف_الدواء = ? ;";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1,componentProduction.getIdComponent());
                    ResultSet resultSet = preparedStmt.executeQuery();
                    int qteComponent = 0 ;
                    if (resultSet.next()){
                        qteComponent = resultSet.getInt("كمية");
                    }
                    if (qteComponent < qte) {
                        ex = false;
                        break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < componentProductionsMaterial.size(); i++) {
                ComponentProduction componentProduction = componentProductionsMaterial.get(i);
                try {
                    String query = "SELECT sum(كمية_مخزنة - كمية_مستهلكة) as كمية FROM تخزين_المواد_الخام WHERE معرف_المادة_الخام = ? ;";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1,componentProduction.getIdComponent());
                    ResultSet resultSet = preparedStmt.executeQuery();
                    int qteComponent = 0 ;
                    if (resultSet.next()){
                        qteComponent = resultSet.getInt("كمية");
                    }
                    if (qteComponent < qte) {
                        ex = false;
                        break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return ex;
    }

    private void refreshComponent(){
        ObservableList<List<StringProperty>> componentDataTable = FXCollections.observableArrayList();

        try {
            ArrayList<Medication> medications = medicationOperation.getAll();

            medications.forEach(medication -> {
                List<StringProperty> data = new ArrayList<>();
                data.add(new SimpleStringProperty("med"));
                data.add( new SimpleStringProperty(String.valueOf(medication.getId())));
                data.add( new SimpleStringProperty(medication.getName()));
                data.add(new SimpleStringProperty(medication.getReference()));
                componentDataTable.add(data);
            });

        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            ArrayList<RawMaterial> rawMaterials =  materialOperation.getAll();

            rawMaterials.forEach(rawMaterial -> {
                List<StringProperty> data = new ArrayList<>();
                data.add(0, new SimpleStringProperty("raw"));
                data.add(1, new SimpleStringProperty(String.valueOf(rawMaterial.getId())));
                data.add(2, new SimpleStringProperty(rawMaterial.getName()));
                data.add(3, new SimpleStringProperty(rawMaterial.getReference()));
                componentDataTable.add(data);
            });
        }catch (Exception e){
            e.printStackTrace();
        }

//        rawMedTable.setItems(componentDataTable);

    }


    @FXML
    private void ActionDeleteFromComposition(){
        /*int compoSelectedIndex = tableComposition.getSelectionModel().getSelectedIndex();
        if (compoSelectedIndex != -1){
            dataTable.remove(compoSelectedIndex);
            tableComposition.setItems(dataTable);
        }*/
    }
    @FXML
    private void ActionAnnulledAdd(){
        closeDialog(btnInsert);
    }

    @FXML
    void ActionInsert(ActionEvent event) {

        /*String name = tfName.getText().trim();
        String reference = tfReference.getText().trim();
        String limitQte = tfLimiteQte.getText().trim();

        if (!name.isEmpty() && !reference.isEmpty() && !limitQte.isEmpty() && dataTable.size() != 0){

            Product product = new Product();
            product.setName(name);
            product.setReference(reference);
            product.setLimitQte(Integer.parseInt(limitQte));

            int ins = insert(product);
            if (ins != -1 ) {
                insertComponent(dataTable, ins);
                ActionAnnulledAdd();
            }
            else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("تحذير ");
                alertWarning.setContentText("خطأ غير معروف");
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");
                alertWarning.showAndWait();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء ملأ جميع الحقول");
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }*/
    }

    private void insertComponent(ObservableList<List<StringProperty>> dataTable , int idProduct) {

        dataTable.forEach(stringProperties -> {
            String type =  stringProperties.get(0).getValue();
            int id = Integer.parseInt(stringProperties.get(1).getValue());
            int qte = Integer.parseInt(stringProperties.get(4).getValue());

            ComponentProduction componentProduction = new ComponentProduction();
            componentProduction.setIdComponent(id);
            componentProduction.setIdProduct(idProduct);
            componentProduction.setQte(qte);

            switch (type){
                case "med":
                    insertComponentMedication(componentProduction);
                    break;
                case "raw":
                    insertComponentRawMaterial(componentProduction);
                    break;
            }
        });
    }

    private int insert(Product product) {
        int insert = 0;
        try {
//            insert = operation.insertId(product);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private boolean insertComponentMedication(ComponentProduction componentProduction){
        boolean insert = false;
        try {
            insert = componentMedicationOperation.insert(componentProduction);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private boolean insertComponentRawMaterial(ComponentProduction componentProduction){
        boolean insert = false;
        try {
            insert = componentMaterialOperation.insert(componentProduction);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }


    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }

}
