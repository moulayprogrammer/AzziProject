package Controllers.ProductionsController;

import BddPackage.ComponentStoreProductOperation;
import BddPackage.ComponentStoreRawMaterialTempOperation;
import BddPackage.ComponentStoreMedicationTempOperation;
import BddPackage.ProductOperation;
import Models.ComponentStoreProduct;
import Models.ComponentStoreTemp;
import Models.Production;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

public class ConfirmationController implements Initializable {

    @FXML
    TextField tfProduct,tfQte,tfPriceProduction,tfPrice;
    @FXML
    DatePicker dpDateStore;
    @FXML
    Button btnInsert;

    private final ProductOperation productOperation = new ProductOperation();
    private final ComponentStoreProductOperation operation = new ComponentStoreProductOperation();
    private Production productionSelected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void Init(Production production){
        this.productionSelected = production;

        tfProduct.setText(productOperation.get(productionSelected.getIdProduct()).getName());
        tfQte.setText(String.valueOf(productionSelected.getQteProduct()));
        tfPriceProduction.setText(String.format(Locale.FRANCE, "%,.2f", productionSelected.getPrice() ));
        dpDateStore.setValue(LocalDate.now());
    }

    @FXML
    void ActionConfirm(ActionEvent event) {


        String stQte = tfQte.getText().trim();
        String stPrice = tfPrice.getText().trim();
        LocalDate date = dpDateStore.getValue();

        if (date != null && !stQte.isEmpty() && !stPrice.isEmpty()){

            ComponentStoreProduct componentStoreProduct = new ComponentStoreProduct();
            componentStoreProduct.setIdProduction(this.productionSelected.getId());
            componentStoreProduct.setIdComponent(this.productionSelected.getIdProduct());
            componentStoreProduct.setDateStore(date);
            componentStoreProduct.setPriceHt(Double.parseDouble(stPrice));
            componentStoreProduct.setQteStored(Integer.parseInt(stQte));

            boolean ins = insert(componentStoreProduct);
            if (ins) {
                deleteTempStore();
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

    private void deleteTempStore() {
        ComponentStoreRawMaterialTempOperation componentStoreRawMaterialTempOperation = new ComponentStoreRawMaterialTempOperation();
        componentStoreRawMaterialTempOperation.delete(new ComponentStoreTemp(productionSelected.getId()));

        ComponentStoreMedicationTempOperation componentStoreMedicationTempOperation = new ComponentStoreMedicationTempOperation();
        componentStoreMedicationTempOperation.delete(new ComponentStoreTemp(productionSelected.getId()));
    }

    private boolean insert(ComponentStoreProduct csp)  {
        boolean insert = false;
        try {
            insert = operation.insert(csp);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    @FXML
    private void ActionAnnulledAdd(){
        closeDialog(btnInsert);
    }

    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }
}
