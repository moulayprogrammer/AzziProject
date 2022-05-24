package Controllers.ReceiptMedicationControllers;

import BddPackage.ComponentReceiptMedicationOperation;
import BddPackage.ConnectBD;
import BddPackage.ReceiptMedicationOperation;
import BddPackage.ProviderOperation;
import Models.Provider;
import Models.Receipt;
import Models.ComponentReceipt;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class MainController implements Initializable {

    @FXML
    AnchorPane MainPanel;
    @FXML
    TextField tfRecherche;
    @FXML
    TableView<List<StringProperty>> table;
    @FXML
    TableColumn<List<StringProperty>,String> clId,clProvider,clDate,clAmount,clPaying,clDebt;
    @FXML
    Label lbSumAmount,lbSumPaying,lbSumDebt;
    @FXML
    ComboBox<String> cbProvider;


//    private final ObservableList<Receipt> dataTable = FXCollections.observableArrayList();
    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;
    private final ObservableList<List<StringProperty>> dataTable = FXCollections.observableArrayList();
    private final ObservableList<String> comboProviderData = FXCollections.observableArrayList();
    private final List<Integer> idProviderCombo = new ArrayList<>();
    private final ReceiptMedicationOperation operation = new ReceiptMedicationOperation();
    private final ProviderOperation providerOperation = new ProviderOperation();
    private final ComponentReceiptMedicationOperation componentReceiptMedicationOperation = new ComponentReceiptMedicationOperation();
    private int selectedProvider = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();

        clId.setCellValueFactory(data -> data.getValue().get(0));
        clProvider.setCellValueFactory(data -> data.getValue().get(1));
        clDate.setCellValueFactory(data -> data.getValue().get(2));
        clAmount.setCellValueFactory(data -> data.getValue().get(3));
        clPaying.setCellValueFactory(data -> data.getValue().get(4));
        clDebt.setCellValueFactory(data -> data.getValue().get(5));

        refresh();
        refreshComboProvider();

        //set Combo Search
        cbProvider.setEditable(true);
        cbProvider.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        TextFields.bindAutoCompletion(cbProvider.getEditor(), cbProvider.getItems());

        cbProvider.setOnAction(event -> {
            int index = cbProvider.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                selectedProvider = idProviderCombo.get(index);
                setProviderReceipt(selectedProvider);
            }
        });
    }

    @FXML
    private void ActionAdd(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ReceiptMedicationViews/AddView.fxml"));
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

     /*   Client client = table.getSelectionModel().getSelectedItem();
        if (client != null){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ClientViews/UpdateView.fxml"));
                DialogPane temp = loader.load();
                UpdateController controller = loader.getController();
                controller.InitUpdate(client);
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(temp);
                dialog.resizableProperty().setValue(false);
                dialog.showAndWait();
                refresh();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير");
            alertWarning.setContentText("الرجاء اختيار زبون من اجل التعديل");
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }*/
    }

    @FXML
    private void ActionAddToArchive(){
        /*Client client = table.getSelectionModel().getSelectedItem();

        if (client != null){
            try {

                Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                alertConfirmation.setHeaderText("تاكيد الارشفة");
                alertConfirmation.setContentText("هل انت متاكد من ارشفة الزبون" );
                Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");

                Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
                cancel.setText("الغاء");

                alertConfirmation.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.CANCEL) {
                        alertConfirmation.close();
                    } else if (response == ButtonType.OK) {
                        operation.AddToArchive(client);
                        refresh();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء اختيار زبون لارشفته");
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }*/
    }


    @FXML
    private void ActionDeleteFromArchive(){
        /*try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ClientViews/ArchiveView.fxml"));
            DialogPane temp = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.showAndWait();

            refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    private void refresh(){
        try {
            ArrayList<Receipt> receipts = operation.getAll();
            dataTable.clear();
            AtomicReference<Double> sumAmount = new AtomicReference<>(0.0);
            AtomicReference<Double> sumPaying = new AtomicReference<>(0.0);
            AtomicReference<Double> sumDebt = new AtomicReference<>(0.0);
            receipts.forEach(receipt -> {
                Provider provider = providerOperation.get(receipt.getIdProvider());
                ArrayList<ComponentReceipt> componentReceipts = componentReceiptMedicationOperation.getAllByReceipt(receipt.getId());
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
        }
    }
    private void refreshComboProvider() {
        comboProviderData.clear();
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
                cbProvider.setItems(comboProviderData);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setProviderReceipt(int selectedProvider) {
        try {
            ArrayList<Receipt> receipts = operation.getAllByProvider(selectedProvider);
            dataTable.clear();
            AtomicReference<Double> sumAmount = new AtomicReference<>(0.0);
            AtomicReference<Double> sumPaying = new AtomicReference<>(0.0);
            AtomicReference<Double> sumDebt = new AtomicReference<>(0.0);
            receipts.forEach(receipt -> {
                Provider provider = providerOperation.get(receipt.getIdProvider());
                ArrayList<ComponentReceipt> componentReceipts = componentReceiptMedicationOperation.getAllByReceipt(receipt.getId());
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
        }
    }

    @FXML
    private void ActionRefreshCombo(){
        cbProvider.getSelectionModel().clearSelection();
        refresh();
    }
    @FXML
    private void ActionRefresh(){
        clearRecherche();
        refresh();
    }

    private void clearRecherche(){
        tfRecherche.clear();
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
