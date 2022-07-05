package Controllers.DeliveryArrivalMedicationControllers;

import BddPackage.ComponentReceiptMedicationOperation;
import BddPackage.ConnectBD;
import BddPackage.ProviderOperation;
import BddPackage.ReceiptMedicationOperation;
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
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class SelectedReceiptMedicationController implements Initializable {

    @FXML
    TextField tfRecherche;
    @FXML
    TableView<List<StringProperty>> table;
    @FXML
    TableColumn<List<StringProperty>,String> clId,clProvider,clDate,clAmount,clPaying,clDebt;


    private final ReceiptMedicationOperation operation = new ReceiptMedicationOperation();
    private final ProviderOperation providerOperation = new ProviderOperation();
    private final ComponentReceiptMedicationOperation componentReceiptMedicationOperation = new ComponentReceiptMedicationOperation();
    private final ObservableList<List<StringProperty>> dataTable = FXCollections.observableArrayList();

    private Receipt receipt;
    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;

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
    }

    public void Init(Receipt receipt){
        this.receipt = receipt;
    }

    @FXML
    private void ActionSelectReceipt(){
        List<StringProperty> data  = table.getSelectionModel().getSelectedItem();
        if (data != null){
            try {
                Receipt receiptSelected = operation.get(Integer.parseInt(data.get(0).getValue()));

                this.receipt.setId(receiptSelected.getId());
                this.receipt.setIdProvider(receiptSelected.getIdProvider());
                this.receipt.setNumber(receiptSelected.getNumber());
                this.receipt.setDate(receiptSelected.getDate());
                this.receipt.setPaying(receiptSelected.getPaying());

                ActionAnnuler();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء اختيار فاتورة");
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    @FXML
    private void ActionAnnuler(){
        ((Stage)tfRecherche.getScene().getWindow()).close();
    }

    @FXML
    private void ActionRefresh(){
        clearRecherche();
        refresh();
    }

    private void clearRecherche(){
        tfRecherche.clear();
    }

    private void refresh(){
        try {
            ArrayList<Receipt> receipts = operation.getAll();
            dataTable.clear();

            if (conn.isClosed()) conn = connectBD.connect();
            receipts.forEach(receipt -> {
                try {

                    int qteRested = 0;

                    String query = "SELECT * FROM مشتريات_الدواء WHERE  معرف_الفاتورة = ? ;";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1,receipt.getId());
                    ResultSet resultSetComponent = preparedStmt.executeQuery();

                    while (resultSetComponent.next()) {

                        query = "SELECT sum( توصيل_الدواء.الكمية_المفوترة ) as الكمية_المفوترة\n" +
                                "FROM وصل_توصيل_الدواء , توصيل_الدواء \n" +
                                "WHERE وصل_توصيل_الدواء.ارشيف = 0\n" +
                                "AND وصل_توصيل_الدواء.معرف_الفاتورة = ? \n" +
                                "AND توصيل_الدواء.معرف_الدواء = ? \n" +
                                "AND توصيل_الدواء.معرف_الوصل = وصل_توصيل_الدواء.المعرف ;";

                        preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setInt(1, receipt.getId());
                        preparedStmt.setInt(2, resultSetComponent.getInt("معرف_الدواء"));
                        ResultSet resultSet = preparedStmt.executeQuery();
                        if (resultSet.next()) {

                            int qteFacture = resultSetComponent.getInt("الكمية");
                            int qteDelivered = resultSet.getInt("الكمية_المفوترة");
                            qteRested = qteRested + ( qteFacture - qteDelivered );
                        }
                    }
                    if (qteRested > 0) {
                        Provider provider = providerOperation.get(receipt.getIdProvider());
                        ArrayList<ComponentReceipt> componentReceipts = componentReceiptMedicationOperation.getAllByReceipt(receipt.getId());
                        AtomicReference<Double> sumR = new AtomicReference<>(0.0);
                        componentReceipts.forEach(componentReceipt -> {
                            double pr = componentReceipt.getPrice() * componentReceipt.getQte();
                            sumR.updateAndGet(v -> (double) (v + pr));
                        });

                        List<StringProperty> data = new ArrayList<>();
                        data.add(new SimpleStringProperty(String.valueOf(receipt.getId())));//0
                        data.add(new SimpleStringProperty(provider.getName()));//1
                        data.add(new SimpleStringProperty(String.valueOf(receipt.getDate())));//2
                        data.add(new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (sumR.get()))));//3
                        data.add(new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (receipt.getPaying()))));//4
                        data.add(new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (sumR.get() - receipt.getPaying()))));//5

                        dataTable.add(data);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            });
            conn.close();

            table.setItems(dataTable);

        }catch (Exception e){
            e.printStackTrace();
        }
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
