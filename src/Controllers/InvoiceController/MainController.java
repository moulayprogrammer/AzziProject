package Controllers.InvoiceController;

import BddPackage.*;
import Models.*;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class MainController implements Initializable {

    @FXML
    TextField tfRecherche;
    @FXML
    TableView<List<StringProperty>> table;
    @FXML
    TableColumn<List<StringProperty>,String> clId, clClient,clDate,clAmount,clPaying,clDebt,clConfirmation;
    @FXML
    Label lbSumAmount,lbSumPaying,lbSumDebt;
    @FXML
    ComboBox<String> cbClient;
    @FXML
    DatePicker dpFirst,dpSecond;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;
    private final ObservableList<List<StringProperty>> dataTable = FXCollections.observableArrayList();
    private final ObservableList<String> comboClientData = FXCollections.observableArrayList();
    private final List<Integer> idClientCombo = new ArrayList<>();
    private final InvoiceOperation operation = new InvoiceOperation();
    private final ClientOperation clientOperation = new ClientOperation();
    private final ComponentInvoiceOperation componentInvoiceOperation = new ComponentInvoiceOperation();
    private final ComponentStoreProductTempOperation componentStoreProductTempOperation = new ComponentStoreProductTempOperation();
    private final ComponentStoreProductOperation componentStoreProductOperation = new ComponentStoreProductOperation();
    private Client selectedClient;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();
        selectedClient = new Client();

        clId.setCellValueFactory(data -> data.getValue().get(0));
        clClient.setCellValueFactory(data -> data.getValue().get(1));
        clDate.setCellValueFactory(data -> data.getValue().get(2));
        clAmount.setCellValueFactory(data -> data.getValue().get(3));
        clPaying.setCellValueFactory(data -> data.getValue().get(4));
/*        clDebt.setCellValueFactory(data -> data.getValue().get(5));
        clConfirmation.setCellValueFactory(data -> data.getValue().get(6));*/

        refresh();
        refreshComboClient();

        //set Combo Search
        cbClient.setEditable(true);
        cbClient.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        TextFields.bindAutoCompletion(cbClient.getEditor(), cbClient.getItems());
    }

    @FXML
    private void ActionSelectComboProvider(){
        int index = cbClient.getSelectionModel().getSelectedIndex();
        LocalDate dateFirst = dpFirst.getValue();
        LocalDate dateSecond = dpSecond.getValue();
        if (index >= 0) {
            selectedClient = clientOperation.get(idClientCombo.get(index));
            if (dateFirst != null && dateSecond != null) setClientDateInvoice(selectedClient.getId(),dateFirst,dateSecond);
            else setClientInvoice(selectedClient.getId());
        }
    }
    @FXML
    private void ActionSelectDate(){
        LocalDate dateFirst = dpFirst.getValue();
        LocalDate dateSecond = dpSecond.getValue();
        if (dateFirst != null && dateSecond != null){
            if (selectedClient.getId() > 0) setClientDateInvoice(selectedClient.getId(),dateFirst,dateSecond);
            else setDateInvoice(dateFirst,dateSecond);
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
    private void ActionAdd(){
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/InvoiceViews/AddView.fxml"));
            BorderPane temp = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(temp));
            stage.setMaximized(true);
            stage.getIcons().add(new Image("Images/logo.png"));
            stage.setTitle("مزرعة الجنوب");
            stage.initOwner(this.tfRecherche.getScene().getWindow());
            stage.showAndWait();

            refresh();
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
            if (data.get(6).getValue().equals("غير مأكد")) {
                try {
                    Invoice invoice = operation.get(Integer.parseInt(data.get(0).getValue()));

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/InvoiceViews/UpdateView.fxml"));
                    BorderPane temp = loader.load();
                    UpdateController controller = loader.getController();
                    controller.Init(invoice);
                    Stage stage = new Stage();
                    stage.setScene(new Scene(temp));
                    stage.setMaximized(true);
                    stage.getIcons().add(new Image("Images/logo.png"));
                    stage.setTitle("مزرعة الجنوب");
                    stage.setOnCloseRequest(event -> controller.insertOldComponent());
                    stage.initOwner(this.tfRecherche.getScene().getWindow());
                    stage.showAndWait();

                    refresh();



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("تحذير ");
                alertWarning.setContentText("الفاتورة مأكدة بالفعل لا يمكن تعديلها");
                alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");
                alertWarning.showAndWait();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء اختيار فاتورة للتعديل");
            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    @FXML
    private void ActionConfirm(){
        List<StringProperty> data  = table.getSelectionModel().getSelectedItem();
        if (data != null){
            if (data.get(6).getValue().equals("غير مأكد")) {
                try {
                    Invoice invoice = operation.get(Integer.parseInt(data.get(0).getValue()));

                    Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                    alertConfirmation.setHeaderText("تاكيد");
                    alertConfirmation.setContentText("هل انت متاكد من تاكيد الفاتورة");
                    alertConfirmation.initOwner(this.tfRecherche.getScene().getWindow());
                    alertConfirmation.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.setText("موافق");

                    Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
                    cancel.setText("الغاء");

                    alertConfirmation.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.CANCEL) {
                            alertConfirmation.close();
                        } else if (response == ButtonType.OK) {
                            componentStoreProductTempOperation.deleteByInvoice(invoice.getId());
                            refresh();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("تحذير ");
                alertWarning.setContentText("الفاتورة مأكدة بالفعل");
                alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");
                alertWarning.showAndWait();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء اختيار فاتورة لتأكيدها");
            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    @FXML
    private void ActionDelete(){

        List<StringProperty> data  = table.getSelectionModel().getSelectedItem();
        if (data != null){
            if (data.get(6).getValue().equals("غير مأكد")) {
                try {
                    Invoice invoice = operation.get(Integer.parseInt(data.get(0).getValue()));

                    Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                    alertConfirmation.setHeaderText("حذف فاتورة");
                    alertConfirmation.setContentText("هل انت متاكد من حذف الفاتورة");
                    alertConfirmation.initOwner(this.tfRecherche.getScene().getWindow());
                    alertConfirmation.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.setText("موافق");

                    Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
                    cancel.setText("الغاء");

                    alertConfirmation.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.CANCEL) {
                            alertConfirmation.close();
                        } else if (response == ButtonType.OK) {

                            ArrayList<ComponentStoreProductTemp> storeProductTemps = componentStoreProductTempOperation.getAllByInvoice(invoice.getId());
                            for (ComponentStoreProductTemp storeProductTemp : storeProductTemps){

                                ComponentStoreProduct storeProduct = componentStoreProductOperation.get(storeProductTemp.getIdProduct(),storeProductTemp.getIdProduction());
                                storeProduct.setQteConsumed(storeProduct.getQteConsumed() - storeProductTemp.getQte());
                                componentStoreProductOperation.updateQte(storeProduct);

                                deleteComponentInvoice(new ComponentInvoice(invoice.getId(), storeProductTemp.getIdProduct()));
                            }
                            componentStoreProductTempOperation.deleteByInvoice(invoice.getId());
                            operation.delete(invoice);

                            refresh();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("تحذير ");
                alertWarning.setContentText("الفاتورة مأكدة بالفعل لا يمكن حذفها");
                alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");
                alertWarning.showAndWait();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء اختيار فاتورة للحذف");
            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    private boolean deleteComponentInvoice(ComponentInvoice componentInvoice){
        boolean insert = false;
        try {
            insert = componentInvoiceOperation.delete(componentInvoice);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private void refresh(){
        try {
            ArrayList<Invoice> invoices = operation.getAll();

            dataTable.clear();
            AtomicReference<Double> sumAmount = new AtomicReference<>(0.0);
            AtomicReference<Double> sumPaying = new AtomicReference<>(0.0);
            AtomicReference<Double> sumDebt = new AtomicReference<>(0.0);
            invoices.forEach(invoice -> {
                Client client = clientOperation.get(invoice.getIdClient());
                ArrayList<ComponentInvoice> componentInvoices = componentInvoiceOperation.getAllByInvoice(invoice.getId());
                AtomicReference<Double> sumR = new AtomicReference<>(0.0);
                componentInvoices.forEach(componentInvoice -> {
                    double pr = componentInvoice.getPrice() * componentInvoice.getQte();
                    sumR.updateAndGet(v -> (double) (v + pr));
                });

                List<StringProperty> data = new ArrayList<>();
                data.add( new SimpleStringProperty(String.valueOf(invoice.getId())));//0
                data.add( new SimpleStringProperty(client.getName()));//1
                data.add( new SimpleStringProperty(String.valueOf(invoice.getDate())));//2

                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (sumR.get()))));//3
//                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (invoice.getPaying()))));//4
//                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (sumR.get() - invoice.getPaying()))));//5
                data.add( new SimpleStringProperty(invoice.getConfirmation()));//6

                sumAmount.updateAndGet(v -> v + sumR.get());
//                sumPaying.updateAndGet(v -> v + invoice.getPaying());
//                sumDebt.updateAndGet(v -> v + (sumR.get() - invoice.getPaying()));

                dataTable.add(data);
            });
            lbSumAmount.setText(String.format(Locale.FRANCE, "%,.2f", (sumAmount.get())));
            lbSumPaying.setText(String.format(Locale.FRANCE, "%,.2f", (sumPaying.get())));
            lbSumDebt.setText(String.format(Locale.FRANCE, "%,.2f", (sumDebt.get())));
            table.setItems(dataTable);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void refreshComboClient() {
        comboClientData.clear();
        idClientCombo.clear();
        try {
            ArrayList<Client> clients = clientOperation.getAll();
            clients.forEach(client -> {
                comboClientData.add(client.getName()  + " " + client.getReference());
                idClientCombo.add(client.getId());
            });
            cbClient.setItems(comboClientData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setClientInvoice(int selectedClient) {
        try {
            ArrayList<Invoice> invoices = operation.getAllByClient(selectedClient);
            dataTable.clear();
            AtomicReference<Double> sumAmount = new AtomicReference<>(0.0);
            AtomicReference<Double> sumPaying = new AtomicReference<>(0.0);
            AtomicReference<Double> sumDebt = new AtomicReference<>(0.0);
            invoices.forEach(invoice -> {
                Client client = clientOperation.get(invoice.getIdClient());
                ArrayList<ComponentInvoice> componentInvoices = componentInvoiceOperation.getAllByInvoice(invoice.getId());
                AtomicReference<Double> sumR = new AtomicReference<>(0.0);
                componentInvoices.forEach(componentReceipt -> {
                    double pr = componentReceipt.getPrice() * componentReceipt.getQte();
                    sumR.updateAndGet(v -> (double) (v + pr));
                });

                List<StringProperty> data = new ArrayList<>();
                data.add( new SimpleStringProperty(String.valueOf(invoice.getId())));//0
                data.add( new SimpleStringProperty(client.getName()));//1
                data.add( new SimpleStringProperty(String.valueOf(invoice.getDate())));//2
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (sumR.get()))));//3
//                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (invoice.getPaying()))));//4
//                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (sumR.get() - invoice.getPaying()))));//5
                data.add( new SimpleStringProperty(invoice.getConfirmation()));//6

                sumAmount.updateAndGet(v -> (double) (v + sumR.get()));
//                sumPaying.updateAndGet(v -> (double) (v + invoice.getPaying()));
//                sumDebt.updateAndGet(v -> (double) (v + (sumR.get() - invoice.getPaying())));

                dataTable.add(data);
            });
            lbSumAmount.setText(String.format(Locale.FRANCE, "%,.2f", (sumAmount.get())));
            lbSumPaying.setText(String.format(Locale.FRANCE, "%,.2f", (sumPaying.get())));
            lbSumDebt.setText(String.format(Locale.FRANCE, "%,.2f", (sumDebt.get())));
            table.setItems(dataTable);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setDateInvoice(LocalDate dateFirst, LocalDate dateSecond) {
        try {
            ArrayList<Invoice> invoices = operation.getAllByDate(dateFirst,dateSecond);
            dataTable.clear();
            AtomicReference<Double> sumAmount = new AtomicReference<>(0.0);
            AtomicReference<Double> sumPaying = new AtomicReference<>(0.0);
            AtomicReference<Double> sumDebt = new AtomicReference<>(0.0);
            invoices.forEach(invoice -> {
                Client client = clientOperation.get(invoice.getIdClient());
                ArrayList<ComponentInvoice> componentInvoices = componentInvoiceOperation.getAllByInvoice(invoice.getId());
                AtomicReference<Double> sumR = new AtomicReference<>(0.0);
                componentInvoices.forEach(componentInvoice -> {
                    double pr = componentInvoice.getPrice() * componentInvoice.getQte();
                    sumR.updateAndGet(v -> (double) (v + pr));
                });

                List<StringProperty> data = new ArrayList<>();
                data.add( new SimpleStringProperty(String.valueOf(invoice.getId())));//0
                data.add( new SimpleStringProperty(client.getName()));//1
                data.add( new SimpleStringProperty(String.valueOf(invoice.getDate())));//2
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (sumR.get()))));//3
//                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (invoice.getPaying()))));//4
//                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (sumR.get() - invoice.getPaying()))));//5
                data.add( new SimpleStringProperty(invoice.getConfirmation()));//6

                sumAmount.updateAndGet(v -> (double) (v + sumR.get()));
//                sumPaying.updateAndGet(v -> (double) (v + invoice.getPaying()));
//                sumDebt.updateAndGet(v -> (double) (v + (sumR.get() - invoice.getPaying())));

                dataTable.add(data);
            });
            lbSumAmount.setText(String.format(Locale.FRANCE, "%,.2f", (sumAmount.get())));
            lbSumPaying.setText(String.format(Locale.FRANCE, "%,.2f", (sumPaying.get())));
            lbSumDebt.setText(String.format(Locale.FRANCE, "%,.2f", (sumDebt.get())));
            table.setItems(dataTable);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setClientDateInvoice(int selectedProvider , LocalDate dateFirst, LocalDate dateSecond ){
        try {
            ArrayList<Invoice> invoices = operation.getAllByDateClient(selectedProvider,dateFirst,dateSecond);
            dataTable.clear();
            AtomicReference<Double> sumAmount = new AtomicReference<>(0.0);
            AtomicReference<Double> sumPaying = new AtomicReference<>(0.0);
            AtomicReference<Double> sumDebt = new AtomicReference<>(0.0);
            invoices.forEach(invoice -> {
                Client client = clientOperation.get(invoice.getIdClient());
                ArrayList<ComponentInvoice> componentInvoices = componentInvoiceOperation.getAllByInvoice(invoice.getId());
                AtomicReference<Double> sumR = new AtomicReference<>(0.0);
                componentInvoices.forEach(componentInvoice -> {
                    double pr = componentInvoice.getPrice() * componentInvoice.getQte();
                    sumR.updateAndGet(v -> (double) (v + pr));
                });

                List<StringProperty> data = new ArrayList<>();
                data.add( new SimpleStringProperty(String.valueOf(invoice.getId())));//0
                data.add( new SimpleStringProperty(client.getName()));//1
                data.add( new SimpleStringProperty(String.valueOf(invoice.getDate())));//2
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (sumR.get()))));//3
//                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (invoice.getPaying()))));//4
//                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (sumR.get() - invoice.getPaying()))));//5
                data.add( new SimpleStringProperty(invoice.getConfirmation()));//6

                sumAmount.updateAndGet(v -> (double) (v + sumR.get()));
//                sumPaying.updateAndGet(v -> (double) (v + invoice.getPaying()));
//                sumDebt.updateAndGet(v -> (double) (v + (sumR.get() - invoice.getPaying())));

                dataTable.add(data);
            });
            lbSumAmount.setText(String.format(Locale.FRANCE, "%,.2f", (sumAmount.get())));
            lbSumPaying.setText(String.format(Locale.FRANCE, "%,.2f", (sumPaying.get())));
            lbSumDebt.setText(String.format(Locale.FRANCE, "%,.2f", (sumDebt.get())));
            table.setItems(dataTable);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionRefreshCombo(){
        cbClient.getSelectionModel().clearSelection();
        selectedClient = new Client();
        LocalDate dateFirst = dpFirst.getValue();
        LocalDate dateSecond = dpSecond.getValue();
        if (dateFirst != null && dateSecond != null) setDateInvoice(dateFirst , dateSecond);
        else refresh();
    }
    @FXML
    private void ActionRefreshDate(){
        dpFirst.setValue(null);
        dpSecond.setValue(null);
        if (selectedClient.getId() > 0 ) setClientInvoice(selectedClient.getId());
        else refresh();
    }
    @FXML
    private void ActionRefresh(){
        clearRecherche();

        refresh();
    }

    private void clearRecherche(){
        tfRecherche.clear();
        cbClient.getSelectionModel().clearSelection();
        selectedClient = new Client();
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
                //loadDataInTable();
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
