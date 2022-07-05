package Controllers.DeliveryArrivalRawMaterialControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class QteDeliveredController implements Initializable {

    @FXML
    TextField tfQteFractured,tfQteDelivered;

    private ArrayList<String> list;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void Init(ArrayList<String> strings){
        this.list = strings;

        if (strings.size() > 1){
            tfQteFractured.setText(strings.get(0));
            tfQteDelivered.setText(strings.get(1));
        }else {
            tfQteFractured.setText(strings.get(0));
            tfQteDelivered.setText(strings.get(0));
        }
    }

    @FXML
    private void ActionOk(){
        String qteFact = tfQteFractured.getText().trim();
        String qteDel = tfQteDelivered.getText().trim();

        if (!qteFact.isEmpty() && !qteDel.isEmpty()) {
            int qteR = 0;

            if (this.list.size() > 1 ){
                 qteR = Integer.parseInt(this.list.get(2));
            }else {
                 qteR = Integer.parseInt(this.list.get(0));
            }
            int qteF = Integer.parseInt(qteFact);
            int qteD = Integer.parseInt(qteDel);

            if (qteR >= qteF && qteF >= qteD) {
                if (this.list.size() > 1){
                    this.list.set(0, qteFact);
                    this.list.set(1,qteDel);
                }else {
                    this.list.set(0, qteFact);
                    this.list.add(qteDel);
                }
            }else {
                closeDialog();

                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("تحذير ");
                alertWarning.setContentText("تاكد من الكمية المدخلة ");
                alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");
                alertWarning.showAndWait();
            }
        }
        closeDialog();
    }

    @FXML
    private void ActionCancel(){
        closeDialog();
    }

    private void closeDialog(){
        ((Stage) tfQteDelivered.getScene().getWindow()).close();
    }
}
