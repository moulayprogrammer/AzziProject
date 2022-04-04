package Controllers.RawMaterialControllers;

import BddPackage.RawMaterialOperation;
import Models.RawMaterial;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddController implements Initializable {

    @FXML
    TextField tfName,tfReference,tfLimiteQte;
    @FXML
    Button btnInsert;



    private final RawMaterialOperation operation = new RawMaterialOperation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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

            RawMaterial rawMaterial = new RawMaterial();
            rawMaterial.setName(name);
            rawMaterial.setReference(reference);
            rawMaterial.setLimiteQte(Integer.parseInt(limiteQte));

            boolean ins = insert(rawMaterial);
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

    private boolean insert(RawMaterial rawMaterial) {
        boolean insert = false;
        try {
            insert = operation.insert(rawMaterial);
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
