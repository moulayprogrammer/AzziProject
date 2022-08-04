package Controllers.SpendControllers;

import BddPackage.SpendOperation;
import Models.Spend;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdateController implements Initializable {

    @FXML
    TextField tfPrice;
    @FXML
    DatePicker dpDate;
    @FXML
    TextArea taRaison;
    @FXML
    Button btnUpdate;

    private Spend SpendUpdate;
    private final SpendOperation operation = new SpendOperation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void InitUpdate(Spend upd){

        tfPrice.setText(String.valueOf(upd.getPrice()));
        dpDate.setValue(upd.getDate());
        taRaison.setText(upd.getRaison());

        this.SpendUpdate = upd;
    }

    @FXML
    private void ActionUpdateSave(){

        String stPrice = tfPrice.getText().trim();
        LocalDate date = dpDate.getValue();
        String raison = taRaison.getText().trim();

        if (!stPrice.isEmpty() && !raison.isEmpty() && date != null){

            Spend Spend =  new Spend();
            Spend.setPrice(Double.parseDouble(stPrice));
            Spend.setDate(date);
            Spend.setRaison(raison);


            boolean upd = update(Spend);
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

    private boolean update(Spend Spend) {
        boolean upd = false;
        try {
            upd = operation.update(Spend,this.SpendUpdate);
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
