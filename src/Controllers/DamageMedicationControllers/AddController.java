package Controllers.DamageMedicationControllers;

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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddController implements Initializable {

    @FXML
    private TextField tfQte;
    @FXML
    private ComboBox<String> cbMedication;
    @FXML
    private DatePicker dpDate;
    @FXML
    private TextArea taRaison;
    @FXML
    Button btnInsert;

    private final DamageMedicationOperation operation = new DamageMedicationOperation();
    private final MedicationOperation medicationOperation = new MedicationOperation();
    private final ComponentStoreMedicationOperation componentStoreMedicationOperation = new ComponentStoreMedicationOperation();
    private final ComponentDamageMedicationOperation componentDamageMedicationOperation = new ComponentDamageMedicationOperation();
    private final ObservableList<String> comboMedicationData = FXCollections.observableArrayList();
    private List<Medication> medications = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        refreshComboMedication();

        // set Date
        dpDate.setValue(LocalDate.now());

        cbMedication.setEditable(true);
        cbMedication.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        TextFields.bindAutoCompletion(cbMedication.getEditor(), cbMedication.getItems());
        cbMedication.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            tfQte.setText(String.valueOf(medications.get(newValue.intValue()).getQte()));
        });
    }

    private void refreshComboMedication() {
        clearCombo();
        try {
            medications = medicationOperation.getAll();
            medications.forEach(Medication -> {
                comboMedicationData.add(Medication.getName());
            });
            cbMedication.setItems(comboMedicationData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void clearCombo(){
        cbMedication.getSelectionModel().clearSelection();
        comboMedicationData.clear();
        medications.clear();
    }

    @FXML
    private void ActionAnnulledAdd(){
        closeDialog(btnInsert);
    }

    @FXML
    void ActionInsert(ActionEvent event) {

        int selectIndexMedication = cbMedication.getSelectionModel().getSelectedIndex();
        LocalDate date = dpDate.getValue();
        String stQte = tfQte.getText().trim();
        String stRaison = taRaison.getText().trim();


        if (selectIndexMedication != -1 && date != null && !stQte.isEmpty() & !stRaison.isEmpty()){

            int idMedication = medications.get(selectIndexMedication).getId();
            int qte = Integer.parseInt(stQte);
            if (qte != 0 ) {
                if (medications.get(selectIndexMedication).getQte() >= qte) {

                    Damage damage = new Damage();
                    damage.setIdProduct(idMedication);
                    damage.setDate(date);
                    damage.setQte(qte);
                    damage.setRaison(stRaison);

                    int ins = insert(damage);
                    if (ins != -1) {
                        insertComponent(qte, ins, idMedication);
                        closeDialog(btnInsert);
                    } else {
                        Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                        alertWarning.setHeaderText("تحذير ");
                        alertWarning.setContentText("خطأ غير معروف");
                        alertWarning.initOwner(this.btnInsert.getScene().getWindow());
                        alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                        Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                        okButton.setText("موافق");
                        alertWarning.showAndWait();
                    }
                } else {
                    Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                    alertWarning.setHeaderText("الكمية ");
                    alertWarning.setContentText("الكمية غير متوفرة");
                    alertWarning.initOwner(this.btnInsert.getScene().getWindow());
                    alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.setText("موافق");
                    alertWarning.showAndWait();
                }
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء ملأ جميع الحقول");
            alertWarning.initOwner(this.btnInsert.getScene().getWindow());
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    private void insertComponent(int qte, int idDamage, int idMedication){
        try {
            ArrayList<ComponentStore> CSM = componentStoreMedicationOperation.getAllByMedicationOrderByDate(idMedication);

            for (ComponentStore store : CSM) {
                double qteStored = store.getQteStored();
                double qteConsumed = store.getQteConsumed();

                ComponentDamage damage = new ComponentDamage();
                damage.setIdDamage(idDamage);
                damage.setIdComponent(store.getIdComponent());
                damage.setIdReference(store.getIdDeliveryArrival());

                if ((qteStored - qteConsumed) >= qte) {

                    damage.setQte(qte);
                    insertComponentDamage(damage);
                    store.setQteConsumed(qteConsumed + qte);
                    updateQteComponentStoreMedication(store);
                    break;

                } else {

                    double qteRest = qteStored - qteConsumed;
                    qte -= qteRest;
                    damage.setQte(qteRest);
                    insertComponentDamage(damage);
                    store.setQteConsumed(qteConsumed + qteRest);
                    updateQteComponentStoreMedication(store);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private int insert(Damage damage) {
        int insert = 0;
        try {
           insert = operation.insertId(damage);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private boolean insertComponentDamage(ComponentDamage componentDamage){
        boolean insert = false;
        try {
            insert = componentDamageMedicationOperation.insert(componentDamage);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private boolean updateQteComponentStoreMedication( ComponentStore store){
        boolean update = false;
        try {
            update = componentStoreMedicationOperation.updateQte(store);
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
