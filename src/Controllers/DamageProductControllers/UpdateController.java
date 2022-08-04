package Controllers.DamageProductControllers;

import BddPackage.ComponentDamageMedicationOperation;
import BddPackage.ComponentStoreMedicationOperation;
import BddPackage.DamageMedicationOperation;
import BddPackage.MedicationOperation;
import Models.ComponentDamage;
import Models.ComponentStore;
import Models.Damage;
import Models.Medication;
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

public class UpdateController implements Initializable {

    @FXML
    private TextField tfQte;
    @FXML
    private ComboBox<String> cbMedication;
    @FXML
    private DatePicker dpDate;
    @FXML
    private TextArea taRaison;
    @FXML
    Button btnUpdate;


    private final DamageMedicationOperation operation = new DamageMedicationOperation();
    private final MedicationOperation medicationOperation = new MedicationOperation();
    private final ComponentStoreMedicationOperation componentStoreMedicationOperation = new ComponentStoreMedicationOperation();
    private final ComponentDamageMedicationOperation componentDamageMedicationOperation = new ComponentDamageMedicationOperation();
    private final ObservableList<String> comboMedicationData = FXCollections.observableArrayList();
    private List<Medication> medications = new ArrayList<>();
    private List<Integer> idMedications = new ArrayList<>();
    private ArrayList<ComponentDamage> initComponentDamages = new ArrayList<>();
    private Damage selectDamage;
    private Medication selectMedication;

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

    public void InitUpdate(Damage damage){

        this.selectDamage = damage;
        initComponentDamages = componentDamageMedicationOperation.getAllByDamage(damage.getId());

        selectMedication = medications.get(idMedications.indexOf(damage.getIdProduct()));
        cbMedication.getSelectionModel().select(idMedications.indexOf(damage.getIdProduct()));
        dpDate.setValue(damage.getDate());
        tfQte.setText(String.valueOf(damage.getQte()));
        taRaison.setText(damage.getRaison());
    }

    private void refreshComboMedication() {
        clearCombo();
        try {
            medications = medicationOperation.getAll();
            medications.forEach(Medication -> {
                comboMedicationData.add(Medication.getName());
                idMedications.add(Medication.getId());
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
        closeDialog(btnUpdate);
    }

    @FXML
    void ActionUpdate(ActionEvent event) {

        LocalDate date = dpDate.getValue();
        String stQte = tfQte.getText().trim();
        String stRaison = taRaison.getText().trim();


        if ( date != null && !stQte.isEmpty() & !stRaison.isEmpty()){

            int idMedication = selectMedication.getId();
            int qte = Integer.parseInt(stQte);
            if (qte != 0 ) {
                if ((selectMedication.getQte() + selectDamage.getQte()) >= qte) {

                    Damage damage = new Damage();
                    damage.setDate(date);
                    damage.setQte(qte);
                    damage.setRaison(stRaison);

                    boolean upd = update(damage);
                    if (upd) {
                        updateComponent(qte, idMedication);
                        closeDialog(btnUpdate);
                    } else {
                        Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                        alertWarning.setHeaderText("تحذير ");
                        alertWarning.setContentText("خطأ غير معروف");
                        alertWarning.initOwner(this.btnUpdate.getScene().getWindow());
                        alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                        Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                        okButton.setText("موافق");
                        alertWarning.showAndWait();
                    }
                } else {
                    Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                    alertWarning.setHeaderText("الكمية ");
                    alertWarning.setContentText("الكمية غير متوفرة");
                    alertWarning.initOwner(this.btnUpdate.getScene().getWindow());
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
            alertWarning.initOwner(this.btnUpdate.getScene().getWindow());
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    private void updateComponent(int qte , int idMedication){
        try {
            if (qte <= this.selectDamage.getQte()){
                ArrayList<ComponentDamage> componentDamages = new ArrayList<>();

                for (ComponentDamage damage : this.initComponentDamages){

                    ComponentDamage componentDamage = new ComponentDamage();
                    componentDamage.setIdDamage(damage.getIdDamage());
                    componentDamage.setIdComponent(damage.getIdComponent());
                    componentDamage.setIdReference(damage.getIdReference());

                    if (damage.getQte() >= qte) {

                        componentDamage.setQte(qte);
                        componentDamages.add(componentDamage);
                        break;

                    } else {
                        qte -= damage.getQte();
                        componentDamage.setQte(damage.getQte());
                        componentDamages.add(damage);
                    }
                }
                deleteInitComponentDamage();
                insertUpdatedComponent(componentDamages);
            }else {
                int qteRest = qte - this.selectDamage.getQte();
                if (qteRest <= this.selectMedication.getQte()){
                    ArrayList<ComponentStore> CSM = componentStoreMedicationOperation.getAllByMedicationOrderByDate(idMedication);

                    for (ComponentStore store : CSM) {
                        int qteStored = store.getQteStored();
                        int qteConsumed = store.getQteConsumed();

                        ComponentDamage damage = new ComponentDamage();
                        damage.setIdDamage(this.selectDamage.getId());
                        damage.setIdComponent(store.getIdComponent());
                        damage.setIdReference(store.getIdDeliveryArrival());

                        if ((qteStored - qteConsumed) >= qteRest) {

                            damage.setQte(qteRest);
                            insertComponentDamage(damage);
                            store.setQteConsumed(qteConsumed + qteRest);
                            updateQteComponentStoreMedication(store);
                            break;

                        } else {

                            int qteRestPr = qteStored - qteConsumed;
                            qteRest -= qteRestPr;
                            damage.setQte(qteRestPr);
                            insertComponentDamage(damage);
                            store.setQteConsumed(qteConsumed + qteRestPr);
                            updateQteComponentStoreMedication(store);
                        }
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean update(Damage damage) {
        boolean update = false;
        try {
           update = operation.update(damage,selectDamage);
            return update;
        }catch (Exception e){
            e.printStackTrace();
            return update;
        }
    }

    private void deleteInitComponentDamage(){
        for (ComponentDamage selectDamage : this.initComponentDamages){
            ComponentStore store = componentStoreMedicationOperation.get(selectDamage.getIdComponent(),selectDamage.getIdReference());
            store.setQteConsumed(store.getQteConsumed() - selectDamage.getQte());
            updateQteComponentStoreMedication(store);
            componentDamageMedicationOperation.delete(selectDamage);
        }
    }

    private void insertUpdatedComponent(ArrayList<ComponentDamage> componentDamages){
        for (ComponentDamage componentDamage : componentDamages){
            ComponentStore store = componentStoreMedicationOperation.get(componentDamage.getIdComponent(),componentDamage.getIdReference());
            store.setQteConsumed(store.getQteConsumed() + componentDamage.getQte());
            updateQteComponentStoreMedication(store);
            insertComponentDamage(componentDamage);
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
    private boolean updateQteComponentDamageMedication( ComponentDamage damage){
        boolean update = false;
        try {
            update = componentDamageMedicationOperation.updateQte(damage);
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
