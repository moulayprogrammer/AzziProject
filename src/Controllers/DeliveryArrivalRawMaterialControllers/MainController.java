package Controllers.DeliveryArrivalRawMaterialControllers;

import BddPackage.*;
import Models.Delivery;
import Models.DeliveryArrival;
import Models.Receipt;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class MainController implements Initializable {

    @FXML
    TextField tfRecherche;
    @FXML
    TableView<List<StringProperty>> table;
    @FXML
    TableColumn<List<StringProperty>,String> clId, clDelivery,clDate, clFacture, clPrice, clValidation;
    @FXML
    ComboBox<String> cbDelivery;
    @FXML
    DatePicker dpFirst,dpSecond;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;
    private final ObservableList<List<StringProperty>> dataTable = FXCollections.observableArrayList();
    private final DeliveryOperation deliveryOperation = new DeliveryOperation();
    private final ReceiptRawMaterialOperation receiptRawMaterialOperation = new ReceiptRawMaterialOperation();
    private final ObservableList<String> comboDeliveryData = FXCollections.observableArrayList();
    private final List<Integer> idDeliveryCombo = new ArrayList<>();
    private final DeliveryArrivalRawMaterialOperation operation = new DeliveryArrivalRawMaterialOperation();

    private int selectedDelivery = 0;
    private Receipt receipt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();
        receipt = new Receipt();

        clId.setCellValueFactory(data -> data.getValue().get(0));
        clDelivery.setCellValueFactory(data -> data.getValue().get(1));
        clDate.setCellValueFactory(data -> data.getValue().get(2));
        clFacture.setCellValueFactory(data -> data.getValue().get(3));
        clPrice.setCellValueFactory(data -> data.getValue().get(4));
        clValidation.setCellValueFactory(data -> data.getValue().get(5));

        refresh();
        refreshComboDelivery();

        //set Combo Search
        cbDelivery.setEditable(true);
        cbDelivery.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        TextFields.bindAutoCompletion(cbDelivery.getEditor(), cbDelivery.getItems());
    }

    @FXML
    private void ActionSelectComboDelivery(){
        int index = cbDelivery.getSelectionModel().getSelectedIndex();
        LocalDate dateFirst = dpFirst.getValue();
        LocalDate dateSecond = dpSecond.getValue();
        if (index >= 0) {
            selectedDelivery = idDeliveryCombo.get(index);
            if (dateFirst != null && dateSecond != null) setDeliveryDateDeliveryArrival(selectedDelivery,dateFirst,dateSecond);
            else setDeliveryToDeliveryArrival(selectedDelivery);
        }
    }
    @FXML
    private void ActionSelectDate(){
        LocalDate dateFirst = dpFirst.getValue();
        LocalDate dateSecond = dpSecond.getValue();
        if (dateFirst != null && dateSecond != null){
            if (selectedDelivery > 0) setDeliveryDateDeliveryArrival(selectedDelivery,dateFirst,dateSecond);
            else setDateDeliveryArrival(dateFirst,dateSecond);
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير");
            alertWarning.setContentText("الرجاء تحديد تاريخ");
            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    @FXML
    public void ActionAdd(){
        try {
            selectReceipt();

            if (this.receipt.getDate() != null) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/DeliveryArrivalRawMaterialViews/AddView.fxml"));
                BorderPane temp = loader.load();
                AddController controller = loader.getController();
                controller.Init(this.receipt);
                Stage stage = new Stage();
                stage.setScene(new Scene(temp));
                stage.setMaximized(true);
                stage.initOwner(this.tfRecherche.getScene().getWindow());
                stage.getIcons().add(new Image("Images/logo.png"));
                stage.setTitle("مزرعة الجنوب");
                stage.showAndWait();

                ActionRefresh();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selectReceipt(){
        try {
            this.receipt = new Receipt();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/DeliveryArrivalRawMaterialViews/SelectedReceiptRawMaterialView.fxml"));
            DialogPane temp = loader.load();
            SelectedReceiptRawMaterialController controller = loader.getController();
            controller.Init(this.receipt);
            Dialog<Boolean> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.initOwner(this.tfRecherche.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setVisible(false);
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void tableClick(MouseEvent mouseEvent) {
        if ( mouseEvent.getClickCount() == 2 && mouseEvent.getButton().equals(MouseButton.PRIMARY) ){

            ActionUpdate();
        }
    }
    @FXML
    private void ActionUpdate(){
        List<StringProperty> data  = table.getSelectionModel().getSelectedItem();
        if (data != null){
            if (data.get(5).getValue().equals("غير مأكد")) {

                try {
                    Receipt receipt = receiptRawMaterialOperation.get(Integer.parseInt(data.get(3).getValue()));
                    DeliveryArrival deliveryArrival = operation.get(Integer.parseInt(data.get(0).getValue()));

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/DeliveryArrivalRawMaterialViews/UpdateView.fxml"));
                    BorderPane temp = loader.load();
                    UpdateController controller = loader.getController();
                    controller.Init(receipt,deliveryArrival);
                    Stage stage = new Stage();
                    stage.setScene(new Scene(temp));
                    stage.setMaximized(true);
                    stage.initOwner(this.tfRecherche.getScene().getWindow());
                    stage.getIcons().add(new Image("Images/logo.png"));
                    stage.setTitle("مزرعة الجنوب");
                    stage.showAndWait();

                    refresh();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير");
            alertWarning.setContentText("الرجاء اختيار وصل من اجل التعديل");
            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    @FXML
    private void ActionAddToStore(){
        List<StringProperty> data  = table.getSelectionModel().getSelectedItem();
        if (data != null){
            if (data.get(5).getValue().equals("غير مأكد")) {
                try {
                    DeliveryArrival deliveryArrival = operation.get(Integer.parseInt(data.get(0).getValue()));

                    Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                    alertConfirmation.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    alertConfirmation.initOwner(this.tfRecherche.getScene().getWindow());
                    alertConfirmation.setHeaderText("تاكيد الوصل");
                    alertConfirmation.setContentText("هل انت متاكد من تأكيد الوصل \n" +
                            "\" لا يمكن تعديل أو حذف الوصل بعد التاكيد  \"");
                    Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.setText("موافق");

                    Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
                    cancel.setText("الغاء");

                    alertConfirmation.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.CANCEL) {
                            alertConfirmation.close();
                        } else if (response == ButtonType.OK) {
                            operation.StoreDeliveryArrival(deliveryArrival);

                            ActionRefresh();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                Alert alertWarning = new Alert(Alert.AlertType.INFORMATION);
                alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                alertWarning.setHeaderText("تأكيد ");
                alertWarning.setContentText("الوصل مؤكد بالفعل");
                alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");
                alertWarning.showAndWait();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء اختيار وصل لتأكيده");
            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    @FXML
    private void ActionDelete(){
        List<StringProperty> data  = table.getSelectionModel().getSelectedItem();
        if (data != null){
            if (data.get(5).getValue().equals("غير مأكد")) {
                try {
                    DeliveryArrival deliveryArrival = operation.get(Integer.parseInt(data.get(0).getValue()));

                    Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                    alertConfirmation.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    alertConfirmation.initOwner(this.tfRecherche.getScene().getWindow());
                    alertConfirmation.setHeaderText("حذف الوصل");
                    alertConfirmation.setContentText("هل انت متاكد من حذف الوصل ");
                    Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.setText("موافق");

                    Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
                    cancel.setText("الغاء");

                    alertConfirmation.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.CANCEL) {
                            alertConfirmation.close();
                        } else if (response == ButtonType.OK) {
                            operation.delete(deliveryArrival);

                            ActionRefresh();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                Alert alertWarning = new Alert(Alert.AlertType.INFORMATION);
                alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                alertWarning.setHeaderText("الحذف ");
                alertWarning.setContentText("الوصل مؤكد بالفعل لا يمكنك حذفه");
                alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");
                alertWarning.showAndWait();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء اختيار وصل لحذفه");
            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    private void refresh(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();
            dataTable.clear();

            String query = "SELECT وصل_توصيل_مواد_خام.المعرف, وصل_توصيل_مواد_خام.معرف_الفاتورة, وصل_توصيل_مواد_خام.معرف_الموصل, وصل_توصيل_مواد_خام.التاريخ, وصل_توصيل_مواد_خام.السعر, الموصل.الاسم\n" +
                    " , (SELECT معرف_وصل_التوصيل FROM تخزين_المواد_الخام WHERE معرف_وصل_التوصيل = وصل_توصيل_مواد_خام.المعرف) AS التاكيد\n" +
                    "FROM وصل_توصيل_مواد_خام , الموصل WHERE وصل_توصيل_مواد_خام.ارشيف = 0 AND وصل_توصيل_مواد_خام.معرف_الموصل = الموصل.المعرف ;";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                List<StringProperty> data = new ArrayList<>();
                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("المعرف"))));
                data.add( new SimpleStringProperty(resultSet.getString("الاسم")));
                data.add( new SimpleStringProperty(resultSet.getDate("التاريخ").toLocalDate().toString()));
                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("معرف_الفاتورة"))));
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (resultSet.getDouble("السعر")))));
                int confirmation = resultSet.getInt("التاكيد");
                if (confirmation != 0 ) data.add( new SimpleStringProperty("مأكد"));
                else data.add( new SimpleStringProperty("غير مأكد"));

                dataTable.add(data);
            }

            conn.close();

            table.setItems(dataTable);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void refreshComboDelivery() {
        clearCombo();
        try {
            ArrayList<Delivery> deliveries = deliveryOperation.getAll();

            deliveries.forEach(delivery -> {
                comboDeliveryData.add(delivery.getName());
                idDeliveryCombo.add(delivery.getId());
            });
            cbDelivery.setItems(comboDeliveryData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void clearCombo(){
        cbDelivery.getSelectionModel().clearSelection();
        comboDeliveryData.clear();
        idDeliveryCombo.clear();
    }

    private void setDeliveryToDeliveryArrival(int selectedDelivery) {
        try {
            if (conn.isClosed()) conn = connectBD.connect();
            dataTable.clear();

            String query = "SELECT وصل_توصيل_مواد_خام.المعرف, وصل_توصيل_مواد_خام.معرف_الفاتورة, وصل_توصيل_مواد_خام.معرف_الموصل, وصل_توصيل_مواد_خام.التاريخ, وصل_توصيل_مواد_خام.السعر, الموصل.الاسم\n" +
                    " , (SELECT معرف_وصل_التوصيل FROM تخزين_المواد_الخام WHERE معرف_وصل_التوصيل = وصل_توصيل_مواد_خام.المعرف) AS التاكيد\n" +
                    "FROM وصل_توصيل_مواد_خام , الموصل WHERE وصل_توصيل_مواد_خام.ارشيف = 0 AND وصل_توصيل_مواد_خام.معرف_الموصل = ? AND وصل_توصيل_مواد_خام.معرف_الموصل = الموصل.المعرف ;";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,selectedDelivery);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                List<StringProperty> data = new ArrayList<>();
                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("المعرف"))));
                data.add( new SimpleStringProperty(resultSet.getString("الاسم")));
                data.add( new SimpleStringProperty(resultSet.getDate("التاريخ").toLocalDate().toString()));
                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("معرف_الفاتورة"))));
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (resultSet.getDouble("السعر")))));
                int confirmation = resultSet.getInt("التاكيد");
                if (confirmation != 0 ) data.add( new SimpleStringProperty("مأكد"));
                else data.add( new SimpleStringProperty("غير مأكد"));

                dataTable.add(data);
            }
            conn.close();

            table.setItems(dataTable);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setDateDeliveryArrival(LocalDate dateFirst, LocalDate dateSecond) {
        try {
            if (conn.isClosed()) conn = connectBD.connect();
            dataTable.clear();

            String query = "SELECT وصل_توصيل_مواد_خام.المعرف, وصل_توصيل_مواد_خام.معرف_الفاتورة, وصل_توصيل_مواد_خام.معرف_الموصل, وصل_توصيل_مواد_خام.التاريخ, وصل_توصيل_مواد_خام.السعر, الموصل.الاسم\n" +
                    " , (SELECT معرف_وصل_التوصيل FROM تخزين_المواد_الخام WHERE معرف_وصل_التوصيل = وصل_توصيل_مواد_خام.المعرف) AS التاكيد\n" +
                    "FROM وصل_توصيل_مواد_خام , الموصل WHERE وصل_توصيل_مواد_خام.ارشيف = 0 AND   التاريخ BETWEEN ? AND ?  AND وصل_توصيل_مواد_خام.معرف_الموصل = الموصل.المعرف ;";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDate(1,Date.valueOf(dateFirst));
            preparedStmt.setDate(2,Date.valueOf(dateSecond));
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                List<StringProperty> data = new ArrayList<>();
                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("المعرف"))));
                data.add( new SimpleStringProperty(resultSet.getString("الاسم")));
                data.add( new SimpleStringProperty(resultSet.getDate("التاريخ").toLocalDate().toString()));
                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("معرف_الفاتورة"))));
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (resultSet.getDouble("السعر")))));
                int confirmation = resultSet.getInt("التاكيد");
                if (confirmation != 0 ) data.add( new SimpleStringProperty("مأكد"));
                else data.add( new SimpleStringProperty("غير مأكد"));

                dataTable.add(data);
            }
            conn.close();

            table.setItems(dataTable);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setDeliveryDateDeliveryArrival(int selectedDelivery , LocalDate dateFirst, LocalDate dateSecond ){
        try {
            if (conn.isClosed()) conn = connectBD.connect();
            dataTable.clear();

            String query = "SELECT وصل_توصيل_مواد_خام.المعرف, وصل_توصيل_مواد_خام.معرف_الفاتورة, وصل_توصيل_مواد_خام.معرف_الموصل, وصل_توصيل_مواد_خام.التاريخ, وصل_توصيل_مواد_خام.السعر, الموصل.الاسم\n" +
                    " , (SELECT معرف_وصل_التوصيل FROM تخزين_المواد_الخام WHERE معرف_وصل_التوصيل = وصل_توصيل_مواد_خام.المعرف) AS التاكيد\n" +
                    "FROM وصل_توصيل_مواد_خام , الموصل WHERE وصل_توصيل_مواد_خام.ارشيف = 0 AND وصل_توصيل_مواد_خام.معرف_الموصل = ? AND  التاريخ BETWEEN ? AND ?  AND وصل_توصيل_مواد_خام.معرف_الموصل = الموصل.المعرف ;";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,selectedDelivery);
            preparedStmt.setDate(2,Date.valueOf(dateFirst));
            preparedStmt.setDate(3,Date.valueOf(dateSecond));
            ResultSet resultSet = preparedStmt.executeQuery();

            while (resultSet.next()){

                List<StringProperty> data = new ArrayList<>();
                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("المعرف"))));
                data.add( new SimpleStringProperty(resultSet.getString("الاسم")));
                data.add( new SimpleStringProperty(resultSet.getDate("التاريخ").toLocalDate().toString()));
                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("معرف_الفاتورة"))));
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (resultSet.getDouble("السعر")))));
                int confirmation = resultSet.getInt("التاكيد");
                if (confirmation != 0 ) data.add( new SimpleStringProperty("مأكد"));
                else data.add( new SimpleStringProperty("غير مأكد"));

                dataTable.add(data);
            }
            conn.close();

            table.setItems(dataTable);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionRefreshCombo(){
        cbDelivery.getSelectionModel().clearSelection();
        selectedDelivery = 0;
        LocalDate dateFirst = dpFirst.getValue();
        LocalDate dateSecond = dpSecond.getValue();
        if (dateFirst != null && dateSecond != null) setDateDeliveryArrival(dateFirst , dateSecond);
        else refresh();
    }
    @FXML
    private void ActionRefreshDate(){
        dpFirst.setValue(null);
        dpSecond.setValue(null);
        if (selectedDelivery > 0 ) setDeliveryToDeliveryArrival(selectedDelivery);
        else refresh();
    }
    @FXML
    private void ActionRefresh(){
        clearRecherche();

        refresh();
    }

    private void clearRecherche(){
        tfRecherche.clear();
        cbDelivery.getSelectionModel().clearSelection();
        dpFirst.setValue(null);
        dpSecond.setValue(null);
    }

    @FXML
    void ActionSearch() {
        // filtrer les données
        ObservableList<List<StringProperty>> items = table.getItems();
        FilteredList<List<StringProperty>> filteredData = new FilteredList<>(items, e -> true);
        String txtRecherche = tfRecherche.getText().trim();

        filteredData.setPredicate((Predicate<? super List<StringProperty>>) stringProperties -> {
            if (txtRecherche.isEmpty()) {
                ActionRefresh();
                return true;
            } else if (stringProperties.get(1).toString().contains(txtRecherche)) {
                return true;
            } else if (stringProperties.get(2).toString().contains(txtRecherche)) {
                return true;
            }else if (stringProperties.get(3).toString().contains(txtRecherche)) {
                return true;
            } else if (stringProperties.get(4).toString().contains(txtRecherche)) {
                return true;
            }  else return stringProperties.get(5).toString().contains(txtRecherche);
        });

        SortedList<List<StringProperty>> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }
}