package Controllers.DeliveryArrivalMedicationControllers;

import BddPackage.*;
import Models.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class AddController implements Initializable {

    @FXML
    DatePicker dpDate;
    @FXML
    Label lbFactureNbr;
    @FXML
    ComboBox<String> cbDelivery;
    @FXML
    TextField tfRecherchePurchases, tfRechercheDeliveryComponent,tfPrice;
    @FXML
    TableView<List<StringProperty>> tablePurchases, tableDeliveryComponent;
    @FXML
    TableColumn<List<StringProperty>,String> tcIdPurchases, tcNamePurchases, tcQteRested, tcQteFactored;
    @FXML
    TableColumn<List<StringProperty>,String> tcId,tcName,tcQteDelivery,tcQteFacture,tcQteRestedFact;
    @FXML
    Button btnInsert;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;
    private final DeliveryArrivalMedicationOperation operation = new DeliveryArrivalMedicationOperation();
    private final DeliveryOperation deliveryOperation = new DeliveryOperation();
    private final ComponentDeliveryArrivalMedicationOperation componentDeliveryArrivalMedicationOperation = new ComponentDeliveryArrivalMedicationOperation();
    private final ObservableList<List<StringProperty>> dataTable = FXCollections.observableArrayList();
    private final ObservableList<String> comboDeliveryData = FXCollections.observableArrayList();
    private final List<Integer> idDeliveryCombo = new ArrayList<>();
    private Receipt receiptSelected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();

        tcIdPurchases.setCellValueFactory(data -> data.getValue().get(0));
        tcNamePurchases.setCellValueFactory(data -> data.getValue().get(1));
        tcQteRested.setCellValueFactory(data -> data.getValue().get(2));
        tcQteFactored.setCellValueFactory(data -> data.getValue().get(3));

        tcId.setCellValueFactory(data -> data.getValue().get(0));
        tcName.setCellValueFactory(data -> data.getValue().get(1));
        tcQteDelivery.setCellValueFactory(data -> data.getValue().get(3));
        tcQteFacture.setCellValueFactory(data -> data.getValue().get(2));
        tcQteRestedFact.setCellValueFactory(data -> data.getValue().get(4));

        refreshComboDelivery();

        // set Date
        dpDate.setValue(LocalDate.now());

        //set Combo Search
        cbDelivery.setEditable(true);
        cbDelivery.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        TextFields.bindAutoCompletion(cbDelivery.getEditor(), cbDelivery.getItems());

        tfRecherchePurchases.textProperty().addListener((observable, oldValue, newValue) -> {

            if (!newValue.isEmpty()) {

                ObservableList<List<StringProperty>> items = tablePurchases.getItems();
                FilteredList<List<StringProperty>> filteredData = new FilteredList<>(items, e -> true);

                filteredData.setPredicate((Predicate<? super List<StringProperty>>) stringProperties -> {

                    if (stringProperties.get(1).toString().contains(newValue)) {
                        return true;
                    } else if (stringProperties.get(2).toString().contains(newValue)) {
                        return true;
                    }else return stringProperties.get(3).toString().contains(newValue);
                });

                SortedList<List<StringProperty>> sortedList = new SortedList<>(filteredData);
                sortedList.comparatorProperty().bind(tablePurchases.comparatorProperty());
                tablePurchases.setItems(sortedList);
            }else {
                refreshPurchases();
            }
        });
    }

    public void Init(Receipt receipt){
        this.receiptSelected = receipt;

        refreshPurchases();
        setFactureNumber();
    }

    private void setFactureNumber() {
        int nbr = receiptSelected.getNumber();
        int year = LocalDate.now().getYear();
        lbFactureNbr.setText((nbr) + "/" + year );
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

    private void refreshPurchases(){
        ObservableList<List<StringProperty>> purchasesDataTable = FXCollections.observableArrayList();

        try {
            if (conn.isClosed()) conn = connectBD.connect();

            String query = "SELECT * FROM مشتريات_الدواء , الادوية WHERE  معرف_الفاتورة = ? AND معرف_الدواء = المعرف ;";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,receiptSelected.getId());
            ResultSet resultSetComponent = preparedStmt.executeQuery();

            while (resultSetComponent.next()) {

                query = "SELECT sum( توصيل_الدواء.الكمية_المفوترة ) as الكمية_المفوترة\n" +
                        "FROM وصل_توصيل_الدواء , توصيل_الدواء \n" +
                        "WHERE وصل_توصيل_الدواء.ارشيف = 0\n" +
                        "AND وصل_توصيل_الدواء.معرف_الفاتورة = ? \n" +
                        "AND توصيل_الدواء.معرف_الدواء = ? \n" +
                        "AND توصيل_الدواء.معرف_الوصل = وصل_توصيل_الدواء.المعرف ;";

                preparedStmt = conn.prepareStatement(query);
                preparedStmt.setInt(1, this.receiptSelected.getId());
                preparedStmt.setInt(2, resultSetComponent.getInt("معرف_الدواء"));
                ResultSet resultSet = preparedStmt.executeQuery();
                if (resultSet.next()) {

                    int qteFacture = resultSetComponent.getInt("الكمية");
                    int qteDelivered = resultSet.getInt("الكمية_المفوترة");
                    int qteRested = qteFacture - qteDelivered;

                    if (qteRested > 0) {
                        List<StringProperty> data = new ArrayList<>();
                        data.add(new SimpleStringProperty(String.valueOf(resultSetComponent.getInt("معرف_الدواء"))));
                        data.add(new SimpleStringProperty(resultSetComponent.getString("الاسم")));
                        data.add(new SimpleStringProperty(String.valueOf(qteRested)));
                        data.add(new SimpleStringProperty(String.valueOf(qteFacture)));

                        purchasesDataTable.add(data);
                    } else {
                        List<StringProperty> data = new ArrayList<>();
                        data.add(new SimpleStringProperty(String.valueOf(resultSetComponent.getInt("معرف_الدواء"))));
                        data.add(new SimpleStringProperty(resultSetComponent.getString("الاسم")));
                        data.add(new SimpleStringProperty(String.valueOf(0)));
                        data.add(new SimpleStringProperty(String.valueOf(qteFacture)));

                        purchasesDataTable.add(data);
                    }
                }
            }

            conn.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        tablePurchases.setItems(purchasesDataTable);
    }

    @FXML
    private void ActionAddDelivery(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/DeliveryViews/AddView.fxml"));
            DialogPane temp = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setVisible(false);
            dialog.showAndWait();

            refreshComboDelivery();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionAddToCompositionDefault(){
        List<StringProperty> dataSelected = tablePurchases.getSelectionModel().getSelectedItem();
        if (dataSelected != null) {
            int ex = exist(dataSelected);
            if (ex == -1) {
                try {
                    ArrayList<String> qteList = new ArrayList<>();
                    qteList.add(dataSelected.get(2).getValue());

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/DeliveryArrivalMedicationViews/QteDelivredDialog.fxml"));
                    DialogPane temp = loader.load();
                    QteDeliveredController controller = loader.getController();
                    controller.Init(qteList);
                    Dialog<ButtonType> dialog = new Dialog<>();
                    dialog.setDialogPane(temp);
                    dialog.resizableProperty().setValue(false);
                    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                    Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                    closeButton.setVisible(false);
                    dialog.showAndWait();

                    if (qteList.size() > 1){
                        List<StringProperty> data = new ArrayList<>();

                        data.add(0, new SimpleStringProperty(dataSelected.get(0).getValue()));
                        data.add(1, new SimpleStringProperty(dataSelected.get(1).getValue()));
                        data.add(2, new SimpleStringProperty(qteList.get(0)));
                        data.add(3, new SimpleStringProperty(qteList.get(1)));
                        data.add(4, new SimpleStringProperty(dataSelected.get(2).getValue()));

                        dataTable.add(data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            tableDeliveryComponent.setItems(dataTable);
        }
    }

    private int exist(List<StringProperty> dataSelected){
        AtomicInteger ex = new AtomicInteger(-1);
            for (int i = 0 ; i < dataTable.size() ; i++) {
                if (dataTable.get(i).get(0).getValue().equals(dataSelected.get(0).getValue()) ){
                    ex.set(i);
                    break;
                }
            }
        return ex.get();
    }

    @FXML
    private void ActionModifiedQte(){
        try {
            int compoSelectedIndex = tableDeliveryComponent.getSelectionModel().getSelectedIndex();
            if (compoSelectedIndex != -1) {
                ArrayList<String> qteList = new ArrayList<>();

                qteList.add(tableDeliveryComponent.getItems().get(compoSelectedIndex).get(2).getValue());
                qteList.add(tableDeliveryComponent.getItems().get(compoSelectedIndex).get(3).getValue());
                qteList.add(tableDeliveryComponent.getItems().get(compoSelectedIndex).get(4).getValue());

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/DeliveryArrivalMedicationViews/QteDelivredDialog.fxml"));
                DialogPane temp = loader.load();
                QteDeliveredController controller = loader.getController();
                controller.Init(qteList);
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(temp);
                dialog.resizableProperty().setValue(false);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                closeButton.setVisible(false);
                dialog.showAndWait();

                if (qteList.size() > 1){

                    dataTable.get(compoSelectedIndex).get(2).setValue(qteList.get(0));
                    dataTable.get(compoSelectedIndex).get(3).setValue(qteList.get(1));

                    tableDeliveryComponent.setItems(dataTable);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionDeleteFromComposition(){
        int compoSelectedIndex = tableDeliveryComponent.getSelectionModel().getSelectedIndex();

        if (compoSelectedIndex != -1){
            dataTable.remove(compoSelectedIndex);
            tableDeliveryComponent.setItems(dataTable);

        }
    }

    @FXML
    private void ActionAnnulledAdd(){
        closeDialog(btnInsert);
    }

    @FXML
    void ActionInsert(ActionEvent event) {

        int selectedIndexDelivery = cbDelivery.getSelectionModel().getSelectedIndex();
        LocalDate date = dpDate.getValue();
        String price = tfPrice.getText().trim();

        if (selectedIndexDelivery != -1 && date != null && !price.isEmpty() && dataTable.size() != 0 ){

            int idDelivery = idDeliveryCombo.get(selectedIndexDelivery);
            double paying = Double.parseDouble(price);

            DeliveryArrival deliveryArrival = new DeliveryArrival();
            deliveryArrival.setIdReceipt(this.receiptSelected.getId());
            deliveryArrival.setIdDelivery(idDelivery);
            deliveryArrival.setDate(date);
            deliveryArrival.setPrice(paying);

            int ins = insert(deliveryArrival);
            if (ins != -1){
                insertComponent(ins);
                ActionAnnulledAdd();
            }else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("تحذير ");
                alertWarning.setContentText("خطأ غير معروف");
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");
                alertWarning.showAndWait();
            }

        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء ملأ جميع الحقول");
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    private void insertComponent(int idDeliveryArrival) {

        for (int i = 0; i < dataTable.size(); i++) {

            List<StringProperty> stringProperties = dataTable.get(i);

            int idComponent = Integer.parseInt(stringProperties.get(0).getValue());
            int qteDel = Integer.parseInt(stringProperties.get(2).getValue());
            int qteFact = Integer.parseInt(stringProperties.get(3).getValue());

            ComponentDeliveryArrival componentDeliveryArrival = new ComponentDeliveryArrival();
            componentDeliveryArrival.setIdDeliveryArrival(idDeliveryArrival);
            componentDeliveryArrival.setIdComponent(idComponent);
            componentDeliveryArrival.setQteReal(qteDel);
            componentDeliveryArrival.setQteReceipt(qteFact);

            insertComponentDeliveryArrivalMedication(componentDeliveryArrival);
        }
    }

    private int insert(DeliveryArrival deliveryArrival) {
        int insert = 0;
        try {
            insert = operation.insertId(deliveryArrival);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private boolean insertComponentDeliveryArrivalMedication(ComponentDeliveryArrival componentDeliveryArrival){
        boolean insert = false;
        try {
            insert = componentDeliveryArrivalMedicationOperation.insert(componentDeliveryArrival);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }

    @FXML
    private void ActionRefresh(){
        clearRecherche();
        tableDeliveryComponent.setItems(dataTable);
    }

    private void clearRecherche(){
        tfRechercheDeliveryComponent.clear();
    }

    @FXML
    void ActionSearch() {
        // filtrer les données
        ObservableList<List<StringProperty>> items = tableDeliveryComponent.getItems();
        FilteredList<List<StringProperty>> filteredData = new FilteredList<>(items, e -> true);
        String txtRecherche = tfRechercheDeliveryComponent.getText().trim();

        filteredData.setPredicate((Predicate<? super List<StringProperty>>) stringProperties -> {
            if (txtRecherche.isEmpty()) {
                //loadDataInTable();
                return true;
            } else if (stringProperties.get(0).toString().contains(txtRecherche)) {
                return true;
            } else if (stringProperties.get(1).toString().contains(txtRecherche)) {
                return true;
            }else if (stringProperties.get(2).toString().contains(txtRecherche)) {
                return true;
            }  else return stringProperties.get(3).toString().contains(txtRecherche);
        });

        SortedList<List<StringProperty>> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(tableDeliveryComponent.comparatorProperty());
        tableDeliveryComponent.setItems(sortedList);
    }
}
