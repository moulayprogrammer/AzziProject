package Controllers.ProductControllers;

import BddPackage.*;
import Models.Medication;
import Models.Product;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddController implements Initializable {

    @FXML
    TableView<List<StringProperty>> table;
    @FXML
    TableColumn<List<StringProperty>,String> clType,clId,clName,clQte;
    @FXML
    TextField tfName,tfReference,tfLimiteQte;
    @FXML
    Button btnInsert;

    private final ProductOperation operation = new ProductOperation();
    private final RawMaterialOperation materialOperation = new RawMaterialOperation();
    private final MedicationOperation medicationOperation = new MedicationOperation();
    private final ComponentRawMaterialOperation componentMaterialOperation = new ComponentRawMaterialOperation();
    private final ComponentMedicationOperation componentMedicationOperation = new ComponentMedicationOperation();

    private final ObservableList<List<StringProperty>> dataTable = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        clType.setCellValueFactory(data -> data.getValue().get(0));
        clId.setCellValueFactory(data -> data.getValue().get(1));
        clName.setCellValueFactory(data -> data.getValue().get(2));
        clQte.setCellValueFactory(data -> data.getValue().get(3));
        clQte.setCellFactory(TextFieldTableCell.forTableColumn());
        clQte.setOnEditCommit((TableColumn.CellEditEvent<List<StringProperty>, String> t) -> {
            t.getTableView()
                    .getItems()
                    .get(t.getTablePosition().getRow())
                    .set(3,new SimpleStringProperty(t.getNewValue()));
        });
    }

    @FXML
    private void SelectComponent(){

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
            medication.setLimiteQte(Integer.parseInt(limiteQte));

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
