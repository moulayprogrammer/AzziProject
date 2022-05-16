package Controllers.DeliveryControllers;

import BddPackage.DeliveryOperation;
import Models.Delivery;
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
    TextField tfName, tfDriverLic,tfTrackNbr1,tfTrackNbr2;
    @FXML
    Button btnInsert;


    private final DeliveryOperation operation = new DeliveryOperation();

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
        String driverL = tfDriverLic.getText().trim();
        String trackNbr01 = tfTrackNbr1.getText().trim();
        String trackNbr02 = tfTrackNbr2.getText().trim();


        if (!name.isEmpty() && !driverL.isEmpty() && !trackNbr01.isEmpty() ){

            Delivery delivery = new Delivery();
            delivery.setName(name);
            delivery.setDriverLicence(driverL);
            delivery.setTrackNumber(trackNbr01);
            delivery.setTrackNumber2(trackNbr02);

            boolean ins = insert(delivery);
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

    private boolean insert(Delivery delivery) {
        boolean insert = false;
        try {
            insert = operation.insert(delivery);
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