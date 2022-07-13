package Controllers.InvoiceController;

import Models.Product;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddSaleController implements Initializable {

    @FXML
    TextField tfPriceProduction,tfPriceSale,tfQte,tfPriceGlobal,tfNetProfit;
    @FXML
    private Button btnSale;

    private Product selectedProduct;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void Init(Product product){

        this.selectedProduct = product;

    }

    @FXML
    private void ActionCount(){
        String stQte = tfQte.getText().trim();
        if (!stQte.isEmpty()){
            int qte = Integer.parseInt(stQte);

            try {
                String query = "";


            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    @FXML
    private void ActionAdd(){

    }

    @FXML
    private void ActionAnnul(){
        closeDialog(btnSale);
    }

    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }
}
