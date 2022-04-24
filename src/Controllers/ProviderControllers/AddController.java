package Controllers.ProviderControllers;

import BddPackage.ProviderOperation;
import Models.Provider;
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
    TextField tfName,tfAddress,tfActivity,tfNationalNbr;
    @FXML
    Button btnInsert;



    private final ProviderOperation operation = new ProviderOperation();

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
        String address = tfAddress.getText().trim();
        String activity = tfActivity.getText().trim();
        String nationalNbr = tfNationalNbr.getText().trim();


        if (!name.isEmpty() && !address.isEmpty()){

            Provider provider = new Provider();
            provider.setName(name);
            provider.setAddress(address);
            provider.setActivity(activity);
            provider.setNationalNumber(nationalNbr);

            boolean ins = insert(provider);
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

    private boolean insert(Provider provider) {
        boolean insert = false;
        try {
            insert = operation.insert(provider);
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
