package Controllers.SpendControllers;

import BddPackage.SpendOperation;
import Models.Spend;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddController implements Initializable {

    @FXML
    TextField tfPrice;
    @FXML
    DatePicker dpDate;
    @FXML
    TextArea taRaison;
    @FXML
    Button btnInsert;



    private final SpendOperation operation = new SpendOperation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        dpDate.setValue(LocalDate.now());
    }

    @FXML
    private void ActionAnnulledAdd(){
        closeDialog(btnInsert);
    }

    @FXML
    void ActionInsert(ActionEvent event) {

        String stPrice = tfPrice.getText().trim();
        LocalDate date = dpDate.getValue();
        String raison = taRaison.getText().trim();

        if (!stPrice.isEmpty() && !raison.isEmpty() && date != null){

            Spend Spend =  new Spend();
            Spend.setPrice(Double.parseDouble(stPrice));
            Spend.setDate(date);
            Spend.setRaison(raison);

            boolean ins = insert(Spend);
            if (ins) closeDialog(btnInsert);
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
            alertWarning.setContentText("الرجاء ملأ جميع الحقول");
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    private boolean insert(Spend Spend) {
        boolean insert = false;
        try {
            insert = operation.insert(Spend);
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
