package Controllers.DamageMaterialControllers;

import BddPackage.ComponentDamageRawMaterialOperation;
import BddPackage.ComponentStoreRawMaterialOperation;
import BddPackage.DamageRawMaterialOperation;
import BddPackage.RawMaterialOperation;
import Models.ComponentDamage;
import Models.ComponentStore;
import Models.Damage;
import Models.RawMaterial;
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
    private ComboBox<String> cbMaterial;
    @FXML
    private DatePicker dpDate;
    @FXML
    private TextArea taRaison;
    @FXML
    Button btnUpdate;


    private final DamageRawMaterialOperation operation = new DamageRawMaterialOperation();
    private final RawMaterialOperation rawMaterialOperation = new RawMaterialOperation();
    private final ComponentStoreRawMaterialOperation componentStoreRawMaterialOperation = new ComponentStoreRawMaterialOperation();
    private final ComponentDamageRawMaterialOperation componentDamageRawMaterialOperation = new ComponentDamageRawMaterialOperation();
    private final ObservableList<String> comboMaterialData = FXCollections.observableArrayList();
    private List<RawMaterial> materials = new ArrayList<>();
    private List<Integer> idMaterials = new ArrayList<>();
    private ArrayList<ComponentDamage> initComponentDamages = new ArrayList<>();
    private Damage selectDamage;
    private RawMaterial selectMaterial;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        refreshComboMaterial();

        // set Date
        dpDate.setValue(LocalDate.now());

        cbMaterial.setEditable(true);
        cbMaterial.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        TextFields.bindAutoCompletion(cbMaterial.getEditor(), cbMaterial.getItems());
        cbMaterial.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            tfQte.setText(String.valueOf(materials.get(newValue.intValue()).getQte()));
        });
    }

    public void InitUpdate(Damage damage){

        this.selectDamage = damage;
        initComponentDamages = componentDamageRawMaterialOperation.getAllByDamage(damage.getId());

        selectMaterial = materials.get(idMaterials.indexOf(damage.getIdProduct()));
        cbMaterial.getSelectionModel().select(idMaterials.indexOf(damage.getIdProduct()));
        dpDate.setValue(damage.getDate());
        tfQte.setText(String.valueOf(damage.getQte()));
        taRaison.setText(damage.getRaison());
    }

    private void refreshComboMaterial() {
        clearCombo();
        try {
            materials = rawMaterialOperation.getAll();
            materials.forEach(material -> {
                comboMaterialData.add(material.getName());
                idMaterials.add(material.getId());
            });
            cbMaterial.setItems(comboMaterialData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void clearCombo(){
        cbMaterial.getSelectionModel().clearSelection();
        comboMaterialData.clear();
        materials.clear();
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

            int idMaterial = selectMaterial.getId();
            int qte = Integer.parseInt(stQte);
            if (qte != 0 ) {
                if ((selectMaterial.getQte() + selectDamage.getQte()) >= qte) {

                    Damage damage = new Damage();
                    damage.setDate(date);
                    damage.setQte(qte);
                    damage.setRaison(stRaison);

                    boolean upd = update(damage);
                    if (upd) {
                        updateComponent(qte, idMaterial);
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

    private void updateComponent(int qte , int idMaterial){
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
                deleteAddComponentDamage(componentDamages);
            }else {
                int qteRest = qte - this.selectDamage.getQte();
                if (qteRest <= this.selectMaterial.getQte()){
                    ArrayList<ComponentStore> CSM = componentStoreRawMaterialOperation.getAllByMaterialOrderByDate(idMaterial);

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
                            updateQteComponentStoreMaterial(store);
                            break;

                        } else {

                            int qteRestPr = qteStored - qteConsumed;
                            qteRest -= qteRestPr;
                            damage.setQte(qteRestPr);
                            insertComponentDamage(damage);
                            store.setQteConsumed(qteConsumed + qteRestPr);
                            updateQteComponentStoreMaterial(store);
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

    private boolean insertComponentDamage(ComponentDamage componentDamage){
        boolean insert = false;
        try {
            insert = componentDamageRawMaterialOperation.insert(componentDamage);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private boolean updateQteComponentStoreMaterial( ComponentStore store){
        boolean update = false;
        try {
            update = componentStoreRawMaterialOperation.updateQte(store);
            return update;
        }catch (Exception e){
            e.printStackTrace();
            return update;
        }
    }
    private boolean updateQteComponentDamageMaterial( ComponentDamage damage){
        boolean update = false;
        try {
            update = componentDamageRawMaterialOperation.updateQte(damage);
            return update;
        }catch (Exception e){
            e.printStackTrace();
            return update;
        }
    }

    private void deleteAddComponentDamage(ArrayList<ComponentDamage> componentDamages){
        for (ComponentDamage damage : componentDamages){
            for (ComponentDamage selectDamage : this.initComponentDamages){
                if (damage.getIdComponent() != selectDamage.getIdComponent() && damage.getIdReference() != selectDamage.getIdReference()){
                    ComponentStore store = componentStoreRawMaterialOperation.get(selectDamage.getIdComponent(),selectDamage.getIdReference());
                    store.setQteConsumed(store.getQteConsumed() - selectDamage.getQte());
                    updateQteComponentStoreMaterial(store);
                    componentDamageRawMaterialOperation.delete(selectDamage);
                }
            }
        }
    }


    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }
}
