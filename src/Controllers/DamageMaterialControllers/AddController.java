package Controllers.DamageMaterialControllers;

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
    private ComboBox<String> cbMaterial;
    @FXML
    private DatePicker dpDate;
    @FXML
    private TextArea taRaison;
    @FXML
    Button btnInsert;

    private final DamageRawMaterialOperation operation = new DamageRawMaterialOperation();
    private final RawMaterialOperation rawMaterialOperation = new RawMaterialOperation();
    private final ComponentStoreRawMaterialOperation componentStoreRawMaterialOperation = new ComponentStoreRawMaterialOperation();
    private final ComponentDamageRawMaterialOperation componentDamageRawMaterialOperation = new ComponentDamageRawMaterialOperation();
    private final ObservableList<String> comboMaterialData = FXCollections.observableArrayList();
    private List<RawMaterial> materials = new ArrayList<>();

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

    private void refreshComboMaterial() {
        clearCombo();
        try {
            materials = rawMaterialOperation.getAll();
            materials.forEach(material -> {
                comboMaterialData.add(material.getName());
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
        closeDialog(btnInsert);
    }

    @FXML
    void ActionInsert(ActionEvent event) {

        int selectIndexMaterial = cbMaterial.getSelectionModel().getSelectedIndex();
        LocalDate date = dpDate.getValue();
        String stQte = tfQte.getText().trim();
        String stRaison = taRaison.getText().trim();


        if (selectIndexMaterial != -1 && date != null && !stQte.isEmpty() & !stRaison.isEmpty()){

            int idMaterial = materials.get(selectIndexMaterial).getId();
            int qte = Integer.parseInt(stQte);
            if (qte != 0 ) {
                if (materials.get(selectIndexMaterial).getQte() >= qte) {

                    Damage damage = new Damage();
                    damage.setIdProduct(idMaterial);
                    damage.setDate(date);
                    damage.setQte(qte);
                    damage.setRaison(stRaison);

                    int ins = insert(damage);
                    if (ins != -1) {
                        insertComponent(qte, ins, idMaterial);
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

    private void insertComponent(int qte, int idDamage, int idMaterial){
        try {
            ArrayList<ComponentStore> CSM = componentStoreRawMaterialOperation.getAllByMaterialOrderByDate(idMaterial);

            for (ComponentStore store : CSM) {
                int qteStored = store.getQteStored();
                int qteConsumed = store.getQteConsumed();

                ComponentDamage damage = new ComponentDamage();
                damage.setIdDamage(idDamage);
                damage.setIdComponent(store.getIdComponent());
                damage.setIdReference(store.getIdDeliveryArrival());

                if ((qteStored - qteConsumed) >= qte) {

                    damage.setQte(qte);
                    insertComponentDamage(damage);
                    store.setQteConsumed(qteConsumed + qte);
                    updateQteComponentStoreMaterial(store);
                    break;

                } else {

                    int qteRest = qteStored - qteConsumed;
                    qte -= qteRest;
                    damage.setQte(qteRest);
                    insertComponentDamage(damage);
                    store.setQteConsumed(qteConsumed + qteRest);
                    updateQteComponentStoreMaterial(store);
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


    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }
}
