package Controllers.DeliveryArrivalMedicationControllers;

import BddPackage.ComponentReceiptRawMaterialOperation;
import BddPackage.ConnectBD;
import BddPackage.ProviderOperation;
import BddPackage.ReceiptRawMaterialOperation;
import Models.ComponentReceipt;
import Models.Provider;
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
import javafx.scene.control.*;
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
    TableColumn<List<StringProperty>,String> clId, clDelivery,clDate, clFacture, clPrice,clDebt;
    @FXML
    Label lbSumAmount,lbSumPaying,lbSumDebt;
    @FXML
    ComboBox<String> cbDelivery;
    @FXML
    DatePicker dpFirst,dpSecond;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;
    private final ObservableList<List<StringProperty>> dataTable = FXCollections.observableArrayList();
    private final ObservableList<String> comboProviderData = FXCollections.observableArrayList();
    private final List<Integer> idProviderCombo = new ArrayList<>();
    private final ReceiptRawMaterialOperation operation = new ReceiptRawMaterialOperation();
    private final ProviderOperation providerOperation = new ProviderOperation();
    private final ComponentReceiptRawMaterialOperation componentReceiptRawMaterialOperation = new ComponentReceiptRawMaterialOperation();
    private int selectedProvider = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();

        /*clId.setCellValueFactory(data -> data.getValue().get(0));
        clDelivery.setCellValueFactory(data -> data.getValue().get(1));
        clDate.setCellValueFactory(data -> data.getValue().get(2));
        clFacture.setCellValueFactory(data -> data.getValue().get(3));
        clPrice.setCellValueFactory(data -> data.getValue().get(4));
        clDebt.setCellValueFactory(data -> data.getValue().get(5));

        refresh();
        refreshComboProvider();

        //set Combo Search
        cbDelivery.setEditable(true);
        cbDelivery.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        TextFields.bindAutoCompletion(cbDelivery.getEditor(), cbDelivery.getItems());*/
    }

    @FXML
    private void ActionSelectComboProvider(){
        /*int index = cbDelivery.getSelectionModel().getSelectedIndex();
        LocalDate dateFirst = dpFirst.getValue();
        LocalDate dateSecond = dpSecond.getValue();
        if (index >= 0) {
            selectedProvider = idProviderCombo.get(index);
            if (dateFirst != null && dateSecond != null) setProviderDateReceipt(selectedProvider,dateFirst,dateSecond);
            else setProviderReceipt(selectedProvider);
        }*/
    }
    @FXML
    private void ActionSelectDate(){
        /*LocalDate dateFirst = dpFirst.getValue();
        LocalDate dateSecond = dpSecond.getValue();
        if (dateFirst != null && dateSecond != null){
            if (selectedProvider > 0) setProviderDateReceipt(selectedProvider,dateFirst,dateSecond);
            else setDateReceipt(dateFirst,dateSecond);
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير");
            alertWarning.setContentText("الرجاء تحديد تاريخ");
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }*/
    }

    @FXML
    private void ActionAdd(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/DeliveryArrivalMedicationViews/AddView.fxml"));
            DialogPane temp = loader.load();
            Dialog<Boolean> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setVisible(false);
            dialog.showAndWait();

            refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionUpdate(){
       /* List<StringProperty> data  = table.getSelectionModel().getSelectedItem();
        if (data != null){
            try {
                Receipt receipt = operation.get(Integer.parseInt(data.get(0).getValue()));
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ReceiptRawMaterialViews/UpdateView.fxml"));
                DialogPane temp = loader.load();
                UpdateController controller = loader.getController();
                controller.Init(receipt);
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(temp);
                dialog.resizableProperty().setValue(false);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                closeButton.setVisible(false);
                dialog.showAndWait();
                refresh();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير");
            alertWarning.setContentText("الرجاء اختيار فاتورة من اجل التعديل");
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }*/
    }

    @FXML
    private void ActionAddToArchive(){
        /*List<StringProperty> data  = table.getSelectionModel().getSelectedItem();
        if (data != null){
            try {
                Receipt receipt = operation.get(Integer.parseInt(data.get(0).getValue()));
                Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                alertConfirmation.setHeaderText("تاكيد الارشفة");
                alertConfirmation.setContentText("هل انت متاكد من ارشفة الفاتورة" );
                Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");

                Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
                cancel.setText("الغاء");

                alertConfirmation.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.CANCEL) {
                        alertConfirmation.close();
                    } else if (response == ButtonType.OK) {
                        operation.AddToArchive(receipt);
                        refresh();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء اختيار فاتورة لارشفتها");
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }*/
    }


    @FXML
    private void ActionDeleteFromArchive(){
       /* try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ReceiptRawMaterialViews/ArchiveView.fxml"));
            DialogPane temp = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setVisible(false);
            dialog.showAndWait();

            refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    private void refresh(){
        /*try {
            ArrayList<Receipt> receipts = operation.getAll();
            dataTable.clear();
            AtomicReference<Double> sumAmount = new AtomicReference<>(0.0);
            AtomicReference<Double> sumPaying = new AtomicReference<>(0.0);
            AtomicReference<Double> sumDebt = new AtomicReference<>(0.0);
            receipts.forEach(receipt -> {
                Provider provider = providerOperation.get(receipt.getIdProvider());
                ArrayList<ComponentReceipt> componentReceipts = componentReceiptRawMaterialOperation.getAllByReceipt(receipt.getId());
                AtomicReference<Double> sumR = new AtomicReference<>(0.0);
                componentReceipts.forEach(componentReceipt -> {
                    double pr = componentReceipt.getPrice() * componentReceipt.getQte();
                    sumR.updateAndGet(v -> (double) (v + pr));
                });

                List<StringProperty> data = new ArrayList<>();
                data.add( new SimpleStringProperty(String.valueOf(receipt.getId())));//0
                data.add( new SimpleStringProperty(provider.getName()));//1
                data.add( new SimpleStringProperty(String.valueOf(receipt.getDate())));//2
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (sumR.get()))));//3
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (receipt.getPaying()))));//4
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (sumR.get() - receipt.getPaying()))));//5

                sumAmount.updateAndGet(v -> (double) (v + sumR.get()));
                sumPaying.updateAndGet(v -> (double) (v + receipt.getPaying()));
                sumDebt.updateAndGet(v -> (double) (v + (sumR.get() - receipt.getPaying())));

                dataTable.add(data);
            });
            lbSumAmount.setText(String.format(Locale.FRANCE, "%,.2f", (sumAmount.get())));
            lbSumPaying.setText(String.format(Locale.FRANCE, "%,.2f", (sumPaying.get())));
            lbSumDebt.setText(String.format(Locale.FRANCE, "%,.2f", (sumDebt.get())));
            table.setItems(dataTable);
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }
    private void refreshComboProvider() {
        /*comboProviderData.clear();
        idProviderCombo.clear();
        try {
            String query = "SELECT * FROM المورد  WHERE ارشيف = 0;";
            try {
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                ResultSet resultSet = preparedStmt.executeQuery();
                while (resultSet.next()){
                    comboProviderData.add(resultSet.getString("الاسم"));
                    idProviderCombo.add(resultSet.getInt("المعرف"));
                }
                cbDelivery.setItems(comboProviderData);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

    private void setProviderReceipt(int selectedProvider) {
        /*try {
            ArrayList<Receipt> receipts = operation.getAllByProvider(selectedProvider);
            dataTable.clear();
            AtomicReference<Double> sumAmount = new AtomicReference<>(0.0);
            AtomicReference<Double> sumPaying = new AtomicReference<>(0.0);
            AtomicReference<Double> sumDebt = new AtomicReference<>(0.0);
            receipts.forEach(receipt -> {
                Provider provider = providerOperation.get(receipt.getIdProvider());
                ArrayList<ComponentReceipt> componentReceipts = componentReceiptRawMaterialOperation.getAllByReceipt(receipt.getId());
                AtomicReference<Double> sumR = new AtomicReference<>(0.0);
                componentReceipts.forEach(componentReceipt -> {
                    double pr = componentReceipt.getPrice() * componentReceipt.getQte();
                    sumR.updateAndGet(v -> (double) (v + pr));
                });

                List<StringProperty> data = new ArrayList<>();
                data.add( new SimpleStringProperty(String.valueOf(receipt.getId())));//0
                data.add( new SimpleStringProperty(provider.getName()));//1
                data.add( new SimpleStringProperty(String.valueOf(receipt.getDate())));//2
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (sumR.get()))));//3
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (receipt.getPaying()))));//4
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (sumR.get() - receipt.getPaying()))));//5

                sumAmount.updateAndGet(v -> (double) (v + sumR.get()));
                sumPaying.updateAndGet(v -> (double) (v + receipt.getPaying()));
                sumDebt.updateAndGet(v -> (double) (v + (sumR.get() - receipt.getPaying())));

                dataTable.add(data);
            });
            lbSumAmount.setText(String.format(Locale.FRANCE, "%,.2f", (sumAmount.get())));
            lbSumPaying.setText(String.format(Locale.FRANCE, "%,.2f", (sumPaying.get())));
            lbSumDebt.setText(String.format(Locale.FRANCE, "%,.2f", (sumDebt.get())));
            table.setItems(dataTable);
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

    private void setDateReceipt(LocalDate dateFirst, LocalDate dateSecond) {
        /*try {
            ArrayList<Receipt> receipts = operation.getAllByDate(dateFirst,dateSecond);
            dataTable.clear();
            AtomicReference<Double> sumAmount = new AtomicReference<>(0.0);
            AtomicReference<Double> sumPaying = new AtomicReference<>(0.0);
            AtomicReference<Double> sumDebt = new AtomicReference<>(0.0);
            receipts.forEach(receipt -> {
                Provider provider = providerOperation.get(receipt.getIdProvider());
                ArrayList<ComponentReceipt> componentReceipts = componentReceiptRawMaterialOperation.getAllByReceipt(receipt.getId());
                AtomicReference<Double> sumR = new AtomicReference<>(0.0);
                componentReceipts.forEach(componentReceipt -> {
                    double pr = componentReceipt.getPrice() * componentReceipt.getQte();
                    sumR.updateAndGet(v -> (double) (v + pr));
                });

                List<StringProperty> data = new ArrayList<>();
                data.add( new SimpleStringProperty(String.valueOf(receipt.getId())));//0
                data.add( new SimpleStringProperty(provider.getName()));//1
                data.add( new SimpleStringProperty(String.valueOf(receipt.getDate())));//2
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (sumR.get()))));//3
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (receipt.getPaying()))));//4
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (sumR.get() - receipt.getPaying()))));//5

                sumAmount.updateAndGet(v -> (double) (v + sumR.get()));
                sumPaying.updateAndGet(v -> (double) (v + receipt.getPaying()));
                sumDebt.updateAndGet(v -> (double) (v + (sumR.get() - receipt.getPaying())));

                dataTable.add(data);
            });
            lbSumAmount.setText(String.format(Locale.FRANCE, "%,.2f", (sumAmount.get())));
            lbSumPaying.setText(String.format(Locale.FRANCE, "%,.2f", (sumPaying.get())));
            lbSumDebt.setText(String.format(Locale.FRANCE, "%,.2f", (sumDebt.get())));
            table.setItems(dataTable);
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

    private void setProviderDateReceipt(int selectedProvider ,LocalDate dateFirst, LocalDate dateSecond ){
        /*try {
            ArrayList<Receipt> receipts = operation.getAllByDateProvider(selectedProvider,dateFirst,dateSecond);
            dataTable.clear();
            AtomicReference<Double> sumAmount = new AtomicReference<>(0.0);
            AtomicReference<Double> sumPaying = new AtomicReference<>(0.0);
            AtomicReference<Double> sumDebt = new AtomicReference<>(0.0);
            receipts.forEach(receipt -> {
                Provider provider = providerOperation.get(receipt.getIdProvider());
                ArrayList<ComponentReceipt> componentReceipts = componentReceiptRawMaterialOperation.getAllByReceipt(receipt.getId());
                AtomicReference<Double> sumR = new AtomicReference<>(0.0);
                componentReceipts.forEach(componentReceipt -> {
                    double pr = componentReceipt.getPrice() * componentReceipt.getQte();
                    sumR.updateAndGet(v -> (double) (v + pr));
                });

                List<StringProperty> data = new ArrayList<>();
                data.add( new SimpleStringProperty(String.valueOf(receipt.getId())));//0
                data.add( new SimpleStringProperty(provider.getName()));//1
                data.add( new SimpleStringProperty(String.valueOf(receipt.getDate())));//2
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (sumR.get()))));//3
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (receipt.getPaying()))));//4
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (sumR.get() - receipt.getPaying()))));//5

                sumAmount.updateAndGet(v -> (double) (v + sumR.get()));
                sumPaying.updateAndGet(v -> (double) (v + receipt.getPaying()));
                sumDebt.updateAndGet(v -> (double) (v + (sumR.get() - receipt.getPaying())));

                dataTable.add(data);
            });
            lbSumAmount.setText(String.format(Locale.FRANCE, "%,.2f", (sumAmount.get())));
            lbSumPaying.setText(String.format(Locale.FRANCE, "%,.2f", (sumPaying.get())));
            lbSumDebt.setText(String.format(Locale.FRANCE, "%,.2f", (sumDebt.get())));
            table.setItems(dataTable);
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

    @FXML
    private void ActionRefreshCombo(){
        cbDelivery.getSelectionModel().clearSelection();
        LocalDate dateFirst = dpFirst.getValue();
        LocalDate dateSecond = dpSecond.getValue();
        if (dateFirst != null && dateSecond != null) setDateReceipt(dateFirst , dateSecond);
        else refresh();
    }
    @FXML
    private void ActionRefreshDate(){
        dpFirst.setValue(null);
        dpSecond.setValue(null);
        if (selectedProvider > 0 ) setProviderReceipt(selectedProvider);
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
