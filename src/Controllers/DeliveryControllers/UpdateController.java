package Controllers.DeliveryControllers;

import BddPackage.ClientOperation;
import BddPackage.DeliveryOperation;
import Models.Client;
import Models.Delivery;
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
    TextField tfName, tfDriverLic,tfTrackNbr1,tfTrackNbr2;
    @FXML
    Button btnUpdate;

    private Delivery deliveryUpdate;
    private final DeliveryOperation operation = new DeliveryOperation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void InitUpdate(Delivery upd){

        tfName.setText(upd.getName());
         tfDriverLic.setText(upd.getDriverLicence());
         tfTrackNbr1.setText(upd.getTrackNumber());
         tfTrackNbr2.setText(upd.getTrackNumber2());

        this.deliveryUpdate = upd;
    }

    @FXML
    private void ActionUpdateSave(){

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


            boolean upd = update(delivery);
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

    private boolean update(Delivery delivery) {
        boolean upd = false;
        try {
            upd = operation.update(delivery,this.deliveryUpdate);
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
