package Controllers.InvoiceController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class PayingController implements Initializable {

    @FXML
    TextField tfPaying,tfTotFacture,tfDebt,tfRest;
    @FXML
    private Button btnPay;
    @FXML
    CheckBox cbFastPrint,cbDebtPrint;

    private double pay, debt, tot, rest;
    private boolean fastPrint, debtPrint;

    ArrayList<Double> doubles = new ArrayList<>();
    ArrayList<Boolean> booleans = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfPaying.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) count(Double.parseDouble(newValue));
        });
    }

    public void Init(ArrayList<Double> doubles, ArrayList<Boolean> booleans){
        this.doubles = doubles;
        this.booleans = booleans;

        this.pay = doubles.get(0);
        this.tot =  doubles.get(1);
        this.debt =  doubles.get(2);

        tfTotFacture.setText(String.format(Locale.FRANCE, "%,.2f", doubles.get(1) ));
        tfDebt.setText(String.format(Locale.FRANCE, "%,.2f", doubles.get(2) ));
        tfRest.setText(String.format(Locale.FRANCE, "%,.2f", (tot + debt) ));
    }

    private void count(double pay){
        double totComp = tot + debt;
        if (pay <= totComp){
            rest = totComp - pay;
            doubles.set(3,rest);
            tfRest.setText(String.format(Locale.FRANCE, "%,.2f", rest ));
        }
    }

    @FXML
    private void ActionPay(ActionEvent actionEvent) {
        String stPay = tfPaying.getText().trim();
        fastPrint = cbFastPrint.isSelected();
        debtPrint = cbDebtPrint.isSelected();
        doubles.set(0,Double.parseDouble(stPay));
        booleans.set(0,fastPrint);
        booleans.set(1,debtPrint);
        booleans.set(2,true);

        if (!stPay.isEmpty()) closeDialog(this.btnPay);
    }

    @FXML
    private void ActionAnnul(ActionEvent actionEvent) {
        closeDialog(this.btnPay);
    }

    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }
}
