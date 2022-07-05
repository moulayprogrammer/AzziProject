package Controllers.ClientControllers;

import BddPackage.ClientOperation;
import Models.Client;
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
    TextField tfName,tfAddress,tfActivity,tfNationalNumber;
    @FXML
    Button btnUpdate;

    private Client clientUpdate;
    private final ClientOperation operation = new ClientOperation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void InitUpdate(Client upd){

        tfName.setText(upd.getName());
        tfAddress.setText(upd.getAddress());
        tfActivity.setText(upd.getActivity());
        tfNationalNumber.setText(upd.getNationalNumber());

        this.clientUpdate = upd;
    }

    @FXML
    private void ActionUpdateSave(){

        String name = tfName.getText().trim();
        String address = tfAddress.getText().trim();
        String activity = tfActivity.getText().trim();
        String nationalNumber = tfNationalNumber.getText().trim();


        if (!name.isEmpty() && !address.isEmpty() && !activity.isEmpty() && !nationalNumber.isEmpty()){

            Client client =  new Client();
            client.setName(name);
            client.setAddress(address);
            client.setActivity(activity);
            client.setNationalNumber(nationalNumber);


            boolean upd = update(client);
            if (upd) closeDialog(btnUpdate);
            else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("تحذير ");
                alertWarning.setContentText("خطأ غير معرروف");
                alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");
                alertWarning.showAndWait();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء ملأ جميع الحفول الفارغة");
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    private boolean update(Client client) {
        boolean upd = false;
        try {
            upd = operation.update(client,this.clientUpdate);
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
