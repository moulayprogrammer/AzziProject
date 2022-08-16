package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class  MainController implements Initializable {

    @FXML
    private Label lbWindowName;
    @FXML
    private BorderPane mainPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainPane.setPadding(new Insets(0,10,5,10));
        ShowPrincipalScreen();
    }

    @FXML
    private void close(){
        ((Stage)mainPane.getScene().getWindow()).close();
    }

    @FXML
    private void ShowPrincipalScreen(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/PrincipalView.fxml"));
            HBox temp = loader.load();
            PrincipalController controller = loader.getController();
            controller.Init(mainPane,lbWindowName);
            lbWindowName.setText("الرئيسية" );
            mainPane.setCenter(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowRawMaterialScreen(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/RawMaterialViews/MainView.fxml"));
            BorderPane temp = loader.load();
            lbWindowName.setText(" قائمة المواد الخام");
            mainPane.setCenter(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowMedicationScreen(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/MedicationViews/MainView.fxml"));
            BorderPane temp = loader.load();
            lbWindowName.setText(" قائمة الادوية");
            mainPane.setCenter(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowProductScreen(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ProductViews/MainView.fxml"));
            BorderPane temp = loader.load();
            lbWindowName.setText(" قائمة المنتجات");
            mainPane.setCenter(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowClientScreen(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ClientViews/MainView.fxml"));
            BorderPane temp = loader.load();
            lbWindowName.setText(" قائمة الزبائن");
            mainPane.setCenter(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowProviderScreen(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ProviderViews/MainView.fxml"));
            BorderPane temp = loader.load();
            lbWindowName.setText(" قائمة الموردين");
            mainPane.setCenter(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowDeliveryScreen(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/DeliveryViews/MainView.fxml"));
            BorderPane temp = loader.load();
            lbWindowName.setText(" قائمة الموصلين");
            mainPane.setCenter(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowStoreScreen(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/StoreViews/MainView.fxml"));
            BorderPane temp = loader.load();
            mainPane.setCenter(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowReceiptMedicationScreen(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ReceiptMedicationViews/MainView.fxml"));
            BorderPane temp = loader.load();
            lbWindowName.setText(" فواتير شراء الدواء ");
            mainPane.setCenter(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowReceiptRawMaterialScreen(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ReceiptRawMaterialViews/MainView.fxml"));
            BorderPane temp = loader.load();
            lbWindowName.setText("فواتير شراء المواد الخام");
            mainPane.setCenter(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowAddReceiptRawMaterialScreen(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ReceiptRawMaterialViews/AddView.fxml"));
            BorderPane temp = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(temp));
            stage.setMaximized(true);
            stage.getIcons().add(new Image("Images/logo.png"));
            stage.setTitle("مزرعة الجنوب");
            stage.initOwner(this.mainPane.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowDeliveryArrivalMedicationScreen(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/DeliveryArrivalMedicationViews/MainView.fxml"));
            BorderPane temp = loader.load();
            lbWindowName.setText("وصول توصيل الادوية");
            mainPane.setCenter(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowDeliveryArrivalRawMaterialScreen(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/DeliveryArrivalRawMaterialViews/MainView.fxml"));
            BorderPane temp = loader.load();
            lbWindowName.setText("وصول توصيل المواد الخام");
            mainPane.setCenter(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowProductionScreen(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ProductionsViews/MainView.fxml"));
            BorderPane temp = loader.load();
            lbWindowName.setText("الانتاج ");
            mainPane.setCenter(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowInvoiceScreen(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/InvoiceViews/MainView.fxml"));
            BorderPane temp = loader.load();
            lbWindowName.setText("فواتير المبيعات");
            mainPane.setCenter(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowDamageMaterialScreen(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/DamageMaterialViews/MainView.fxml"));
            BorderPane temp = loader.load();
            lbWindowName.setText("تلف المواد الخام");
            mainPane.setCenter(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowDamageMedicationScreen(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/DamageMedicationViews/MainView.fxml"));
            BorderPane temp = loader.load();
            lbWindowName.setText("تلف الادوية");
            mainPane.setCenter(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowDamageProductScreen(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/DamageProductViews/MainView.fxml"));
            BorderPane temp = loader.load();
            lbWindowName.setText("تلف المنتجات");
            mainPane.setCenter(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowSpendScreen(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/SpendViews/MainView.fxml"));
            BorderPane temp = loader.load();
            lbWindowName.setText("المصاريف");
            mainPane.setCenter(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowStatisticScreen(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/StatisticViews/MainView.fxml"));
            BorderPane temp = loader.load();
            lbWindowName.setText("المصاريف");
            mainPane.setCenter(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Logout(){
        try {
            ((Stage)this.mainPane.getScene().getWindow()).close();
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/LoginView.fxml")));
            primaryStage.setTitle("مزرعة الجنوب");
            primaryStage.setScene(new Scene(root));
            primaryStage.getIcons().add(new Image("Images/logo.png"));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
