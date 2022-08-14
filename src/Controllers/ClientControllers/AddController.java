package Controllers.ClientControllers;

import BddPackage.ClientOperation;
import BddPackage.MedicationOperation;
import Models.Client;
import Models.Medication;
import javafx.event.ActionEvent;
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

public class AddController implements Initializable {

    @FXML
    TextField tfReference,tfName,tfAddress,tfActivity,tfNationalNumber;
    @FXML
    Button btnInsert;



    private final ClientOperation operation = new ClientOperation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void ActionAnnulledAdd(){
        closeDialog(btnInsert);
    }

    @FXML
    void ActionInsert(ActionEvent event) {

        String reference = tfReference.getText().trim();
        String name = tfName.getText().trim();
        String address = tfAddress.getText().trim();
        String activity = tfActivity.getText().trim();
        String nationalNumber = tfNationalNumber.getText().trim();


        if (!reference.isEmpty() && !name.isEmpty() && !address.isEmpty() & !activity.isEmpty() & !nationalNumber.isEmpty()){

            Client client =  new Client();
            client.setReference(reference);
            client.setName(name);
            client.setAddress(address);
            client.setActivity(activity);
            client.setNationalNumber(nationalNumber);

            boolean ins = insert(client);
            if (ins) closeDialog(btnInsert);
            else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("تحذير ");
                alertWarning.setContentText("خطأ غير معروف");
                alertWarning.initOwner(this.btnInsert.getScene().getWindow());
                alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");
                alertWarning.showAndWait();
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

    private boolean insert(Client client) {
        boolean insert = false;
        try {
            insert = operation.insert(client);
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
