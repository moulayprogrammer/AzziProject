package Controllers.ProductControllers;

import BddPackage.*;
import Models.Medication;
import Models.RawMaterial;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class AddController implements Initializable {


    @FXML
    TextField tfName,tfReference,tfLimiteQte;
    @FXML
    TableView<List<StringProperty>> rawMedTable,tableComposition;
    @FXML
    TableColumn<List<StringProperty>,String>  tcIdComponent,tcNameComponent,tcTypeComponent,tcReferenceComponent;
    @FXML
    TableColumn<List<StringProperty>,String> tcType,tcId,tcName,tcReference,tcQte;
    @FXML
    Button btnInsert;

    private final ObservableList<List<StringProperty>> componentDataTable = FXCollections.observableArrayList();
    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;

    private final ProductOperation operation = new ProductOperation();
    private final RawMaterialOperation materialOperation = new RawMaterialOperation();
    private final MedicationOperation medicationOperation = new MedicationOperation();
    private final ComponentRawMaterialOperation componentMaterialOperation = new ComponentRawMaterialOperation();
    private final ComponentMedicationOperation componentMedicationOperation = new ComponentMedicationOperation();

    private final ObservableList<List<StringProperty>> dataTable = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();

        tcTypeComponent.setCellValueFactory(data -> data.getValue().get(0));
        tcIdComponent.setCellValueFactory(data -> data.getValue().get(1));
        tcNameComponent.setCellValueFactory(data -> data.getValue().get(2));
        tcReferenceComponent.setCellValueFactory(data -> data.getValue().get(3));

        tcType.setCellValueFactory(data -> data.getValue().get(0));
        tcId.setCellValueFactory(data -> data.getValue().get(1));
        tcName.setCellValueFactory(data -> data.getValue().get(2));
        tcReference.setCellValueFactory(data -> data.getValue().get(3));
        tcQte.setCellValueFactory(data -> data.getValue().get(4));

        refreshComponent();
    }
    private void refreshComponent(){
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

        rawMedTable.setItems(componentDataTable);

    }

    @FXML
    private void ActionAddToCompositionDefault(){
        List<StringProperty> dataSelected = rawMedTable.getSelectionModel().getSelectedItem();
        if (dataSelected != null) {
            int ex = exist(dataSelected);
            if (ex != -1){
                try {
                    int val = Integer.parseInt(dataTable.get(ex).get(4).getValue());
                    dataTable.get(ex).get(4).setValue(String.valueOf(val+1));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                try {
                    List<StringProperty> data = new ArrayList<>();
                    data.add(0, new SimpleStringProperty(dataSelected.get(0).getValue()));
                    data.add(1, new SimpleStringProperty(dataSelected.get(1).getValue()));
                    data.add(2, new SimpleStringProperty(dataSelected.get(2).getValue()));
                    data.add(3, new SimpleStringProperty(dataSelected.get(3).getValue()));
                    data.add(4, new SimpleStringProperty(String.valueOf(1)));

                    dataTable.add(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            tableComposition.setItems(dataTable);
        }
    }

    private int exist(List<StringProperty> dataSelected){
        AtomicInteger ex = new AtomicInteger(-1);
        if (dataTable.size() != 0){
            for (int i = 0; i < dataTable.size() - 1; i++) {
                System.out.println("zz");
                if (dataTable.get(i).get(0).equals(dataSelected.get(0)) && dataTable.get(i).get(1).equals(dataSelected.get(1))) ex.set(i);
            }
        }
        return ex.get();
    }
    @FXML
    private void ActionAnnulledAdd(){
        closeDialog(btnInsert);
    }

    @FXML
    void ActionInsert(ActionEvent event) {

        String name = tfName.getText().trim();
        String reference = tfReference.getText().trim();
        String limiteQte = tfLimiteQte.getText().trim();

        if (!name.isEmpty() && !reference.isEmpty() && !limiteQte.isEmpty()){

            Medication medication = new Medication();
            medication.setName(name);
            medication.setReference(reference);
            medication.setLimitQte(Integer.parseInt(limiteQte));

            boolean ins = insert(medication);
            if (ins) closeDialog(btnInsert);
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
        }
    }

    private boolean insert(Medication medication) {
        boolean insert = false;
        try {
//            insert = operation.insert(medication);
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
