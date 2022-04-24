package Controllers.ProviderControllers;

import BddPackage.ClientOperation;
import BddPackage.ProviderOperation;
import Models.Client;
import Models.Provider;
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
    TextField tfName,tfAddress,tfActivity,tfNationalNbr;
    @FXML
    Button btnUpdate;

    private Provider providerUpdate;
    private final ProviderOperation operation = new ProviderOperation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void InitUpdate(Provider upd){

        tfName.setText(upd.getName());
        tfAddress.setText(upd.getAddress());
        tfActivity.setText(upd.getActivity());
        tfNationalNbr.setText(upd.getNationalNumber());

        this.providerUpdate = upd;
    }

    @FXML
    private void ActionUpdateSave(){

        String name = tfName.getText().trim();
        String address = tfAddress.getText().trim();
        String activity = tfActivity.getText().trim();
        String nationalNbr = tfNationalNbr.getText().trim();


        if (!name.isEmpty() && !address.isEmpty()){

            Provider provider = new Provider();
            provider.setName(name);
            provider.setAddress(address);
            provider.setActivity(activity);
            provider.setNationalNumber(nationalNbr);



            boolean upd = update(provider);
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

    private boolean update(Provider provider) {
        boolean upd = false;
        try {
            upd = operation.update(provider,this.providerUpdate);
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
