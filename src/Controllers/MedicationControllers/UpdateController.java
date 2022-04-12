package Controllers.MedicationControllers;

import BddPackage.MedicationOperation;
import Models.Medication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    private Medication medicationUpdate;
    private final MedicationOperation operation = new MedicationOperation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void InitUpdate(Medication upd){

        tfName.setText(upd.getName());
        tfReference.setText(upd.getReference());
        tfLimiteQte.setText(upd.getLimitQte() + "");

        this.medicationUpdate = upd;
    }

    @FXML
    private void ActionUpdateSave(){

        String name = tfName.getText().trim();
        String reference = tfReference.getText().trim();
        String limiteQte = tfLimiteQte.getText().trim();

        if (!name.isEmpty() && !reference.isEmpty() && !limiteQte.isEmpty()){

            Medication medication = new Medication();
            medication.setName(name);
            medication.setReference(reference);
            medication.setLimitQte(Integer.parseInt(limiteQte));

            boolean upd = update(medication);
            if (upd) closeDialog(btnUpdate);
            else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("تحذير ");
                alertWarning.setContentText("خطأ غير معرروف");
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");
                alertWarning.showAndWait();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء ملأ جميع الحفول الفارغة");
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    private boolean update(Medication medication) {
        boolean upd = false;
        try {
            upd = operation.update(medication,this.medicationUpdate);
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
