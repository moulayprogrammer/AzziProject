package Controllers.InvoiceController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SaleController implements Initializable {

    @FXML
    TextField tfPriceProduction,tfPriceSale,tfQte,tfPriceGlobal,tfNetProfit,tfPaying;
    @FXML
    private Button btnSale;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void Init(){

    }


    @FXML
    private void ActionSale(){

    }
    @FXML
    private void ActionAnnul(){
        closeDialog(btnSale);
    }
    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }
}
