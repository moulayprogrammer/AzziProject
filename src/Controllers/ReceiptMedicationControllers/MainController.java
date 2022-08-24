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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
    TableColumn<List<StringProperty>,String> clId,clProvider,clDate,clAmount,clPaying,clDebt;
    @FXML
    Label lbSumAmount,lbSumPaying,lbSumDebt;
    @FXML
    ComboBox<String> cbProvider;
    @FXML
    DatePicker dpFirst,dpSecond;

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
    }

    @FXML
    private void ActionSelectComboProvider(){
        int index = cbProvider.getSelectionModel().getSelectedIndex();
        LocalDate dateFirst = dpFirst.getValue();
        LocalDate dateSecond = dpSecond.getValue();
        if (index >= 0) {
            selectedProvider = idProviderCombo.get(index);
            if (dateFirst != null && dateSecond != null) setProviderDateReceipt(selectedProvider,dateFirst,dateSecond);
            else setProviderReceipt(selectedProvider);
        }
    }
    @FXML
    private void ActionSelectDate(){
        LocalDate dateFirst = dpFirst.getValue();
        LocalDate dateSecond = dpSecond.getValue();
        if (dateFirst != null && dateSecond != null){
            if (selectedProvider > 0) setProviderDateReceipt(selectedProvider,dateFirst,dateSecond);
            else setDateReceipt(dateFirst,dateSecond);
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير");
            alertWarning.setContentText("الرجاء تحديد تاريخ");
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    @FXML
    private void ActionAdd(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ReceiptMedicationViews/AddView.fxml"));
            BorderPane temp = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(temp));
            stage.setMaximized(true);
            stage.getIcons().add(new Image("Images/logo.png"));
            stage.setTitle("مزرعة الجنوب");
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
            try {
                Receipt receipt = operation.get(Integer.parseInt(data.get(0).getValue()));
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ReceiptMedicationViews/UpdateView.fxml"));
                BorderPane temp = loader.load();
                UpdateController controller = loader.getController();
                controller.Init(receipt);
                Stage stage = new Stage();
                stage.setScene(new Scene(temp));
                stage.setMaximized(true);
                stage.getIcons().add(new Image("Images/logo.png"));
                stage.setTitle("مزرعة الجنوب");
                stage.showAndWait();

                refresh();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير");
            alertWarning.setContentText("الرجاء اختيار زبون من اجل التعديل");
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
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

    private void setDateReceipt(LocalDate dateFirst, LocalDate dateSecond) {
        try {
            ArrayList<Receipt> receipts = operation.getAllByDate(dateFirst,dateSecond);
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

    private void setProviderDateReceipt(int selectedProvider ,LocalDate dateFirst, LocalDate dateSecond ){
        try {
            ArrayList<Receipt> receipts = operation.getAllByDateProvider(selectedProvider,dateFirst,dateSecond);
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
        cbProvider.getSelectionModel().clearSelection();
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
