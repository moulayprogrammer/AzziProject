package Controllers.RawMaterialControllers;

import BddPackage.RawMaterialOperation;
import Models.RawMaterial;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateController implements Initializable {

    @FXML
    TextField tfName,tfReference,tfLimiteQte;
    @FXML
    Button btnUpdate;

    private RawMaterial rawMaterialUpdated;
    private final RawMaterialOperation operation = new RawMaterialOperation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void InitUpdate(RawMaterial upd){

        tfName.setText(upd.getName());
        tfReference.setText(upd.getReference());
        tfLimiteQte.setText(upd.getLimitQte() + "");

        this.rawMaterialUpdated = upd;
    }

    @FXML
    private void ActionUpdateSave(){

        String name = tfName.getText().trim();
        String reference = tfReference.getText().trim();
        String limiteQte = tfLimiteQte.getText().trim();

        if (!name.isEmpty() && !reference.isEmpty() && !limiteQte.isEmpty()){

            RawMaterial rawMaterial = new RawMaterial();
            rawMaterial.setName(name);
            rawMaterial.setReference(reference);
            rawMaterial.setLimitQte(Integer.parseInt(limiteQte));

            boolean upd = update(rawMaterial);
            if (upd) closeDialog(btnUpdate);
            else {
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
            alertWarning.setContentText("الرجاء ملأ جميع الخانات");
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق" );
            alertWarning.showAndWait();
        }
    }

    private boolean update(RawMaterial rawMaterial) {
        boolean upd = false;
        try {
            upd = operation.update(rawMaterial,this.rawMaterialUpdated);
            return upd;
        }catch (Exception e){
            e.printStackTrace();
            return upd;
        }
    }

    @FXML
    private void ActionAnnullerUpd(){
        closeDialog(btnUpdate);
    }

    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }

}
