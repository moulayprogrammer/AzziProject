package Controllers.DamageProductControllers;

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
    private ComboBox<String> cbProduct;
    @FXML
    private DatePicker dpDate;
    @FXML
    private TextArea taRaison;
    @FXML
    Button btnInsert;

    private final DamageProductOperation operation = new DamageProductOperation();
    private final ProductOperation productOperation = new ProductOperation();
    private final ComponentStoreProductOperation componentStoreProductOperation = new ComponentStoreProductOperation();
    private final ComponentDamageProductOperation componentDamageProductOperation = new ComponentDamageProductOperation();
    private final ObservableList<String> comboProductData = FXCollections.observableArrayList();
    private List<Product> Products = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        refreshComboProduct();

        // set Date
        dpDate.setValue(LocalDate.now());

        cbProduct.setEditable(true);
        cbProduct.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        TextFields.bindAutoCompletion(cbProduct.getEditor(), cbProduct.getItems());
        cbProduct.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            tfQte.setText(String.valueOf(Products.get(newValue.intValue()).getQte()));
        });
    }

    private void refreshComboProduct() {
        clearCombo();
        try {
            Products = productOperation.getAll();
            Products.forEach(Product -> {
                comboProductData.add(Product.getName());
            });
            cbProduct.setItems(comboProductData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void clearCombo(){
        cbProduct.getSelectionModel().clearSelection();
        comboProductData.clear();
        Products.clear();
    }

    @FXML
    private void ActionAnnulledAdd(){
        closeDialog(btnInsert);
    }

    @FXML
    void ActionInsert(ActionEvent event) {

        int selectIndexProduct = cbProduct.getSelectionModel().getSelectedIndex();
        LocalDate date = dpDate.getValue();
        String stQte = tfQte.getText().trim();
        String stRaison = taRaison.getText().trim();


        if (selectIndexProduct != -1 && date != null && !stQte.isEmpty() & !stRaison.isEmpty()){

            int idProduct = Products.get(selectIndexProduct).getId();
            int qte = Integer.parseInt(stQte);
            if (qte != 0 ) {
                if (Products.get(selectIndexProduct).getQte() >= qte) {

                    Damage damage = new Damage();
                    damage.setIdProduct(idProduct);
                    damage.setDate(date);
                    damage.setQte(qte);
                    damage.setRaison(stRaison);

                    int ins = insert(damage);
                    if (ins != -1) {
                        insertComponent(qte, ins, idProduct);
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

    private void insertComponent(int qte, int idDamage, int idProduct){
        try {
            ArrayList<ComponentStoreProduct> CSM = componentStoreProductOperation.getAllByProductOrderByDate(idProduct);

            for (ComponentStoreProduct store : CSM) {
                double qteStored = store.getQteStored();
                double qteConsumed = store.getQteConsumed();

                ComponentDamage damage = new ComponentDamage();
                damage.setIdDamage(idDamage);
                damage.setIdComponent(store.getIdComponent());
                damage.setIdReference(store.getIdProduction());

                if ((qteStored - qteConsumed) >= qte) {

                    damage.setQte(qte);
                    insertComponentDamage(damage);
                    store.setQteConsumed(qteConsumed + qte);
                    updateQteComponentStoreProduct(store);
                    break;

                } else {

                    double qteRest = qteStored - qteConsumed;
                    qte -= qteRest;
                    damage.setQte(qteRest);
                    insertComponentDamage(damage);
                    store.setQteConsumed(qteConsumed + qteRest);
                    updateQteComponentStoreProduct(store);
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
            insert = componentDamageProductOperation.insert(componentDamage);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private boolean updateQteComponentStoreProduct( ComponentStoreProduct store){
        boolean update = false;
        try {
            update = componentStoreProductOperation.updateQte(store);
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
