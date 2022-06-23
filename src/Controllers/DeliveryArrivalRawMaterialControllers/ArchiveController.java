package Controllers.DeliveryArrivalRawMaterialControllers;

import BddPackage.ConnectBD;
import BddPackage.DeliveryArrivalMedicationOperation;
import BddPackage.DeliveryArrivalRawMaterialOperation;
import Models.DeliveryArrival;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import java.util.function.Predicate;

public class ArchiveController implements Initializable {

    @FXML
    TextField tfRecherche;
    @FXML
    TableView<List<StringProperty>> table;
    @FXML
    TableColumn<List<StringProperty>,String> clId, clDelivery,clDate, clFacture, clPrice;


    private final DeliveryArrivalRawMaterialOperation operation = new DeliveryArrivalRawMaterialOperation();
    private final ObservableList<List<StringProperty>> dataTable = FXCollections.observableArrayList();

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        conn = connectBD.connect();

        clId.setCellValueFactory(data -> data.getValue().get(0));
        clDelivery.setCellValueFactory(data -> data.getValue().get(1));
        clDate.setCellValueFactory(data -> data.getValue().get(2));
        clFacture.setCellValueFactory(data -> data.getValue().get(3));
        clPrice.setCellValueFactory(data -> data.getValue().get(4));

        refresh();
    }

    @FXML
    private void ActionDeleteFromArchive(){
        List<StringProperty> data  = table.getSelectionModel().getSelectedItem();
        if (data != null){
            try {
                DeliveryArrival deliveryArrival = operation.getArchive(Integer.parseInt(data.get(0).getValue()));

                Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                alertConfirmation.setHeaderText("تاكيد الارشفة");
                alertConfirmation.setContentText("هل انت متاكد من الغاء ارشفة الوصل " );
                Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");

                Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
                cancel.setText("الغاء");

                alertConfirmation.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.CANCEL) {
                        alertConfirmation.close();
                    } else if (response == ButtonType.OK) {
                        operation.DeleteFromArchive(deliveryArrival);

                        ActionAnnuler();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء اختيار الوصل لالغاء أرشفته ");
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
            dataTable.clear();

            String query = "SELECT * FROM وصل_توصيل_الدواء , الموصل WHERE وصل_توصيل_الدواء.ارشيف = 1 AND وصل_توصيل_الدواء.معرف_الموصل = الموصل.المعرف ;";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){
                List<StringProperty> data = new ArrayList<>();
                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("المعرف"))));
                data.add( new SimpleStringProperty(resultSet.getString("الاسم")));
                data.add( new SimpleStringProperty(resultSet.getDate("التاريخ").toLocalDate().toString()));
                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("معرف_الفاتورة"))));
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (resultSet.getDouble("السعر")))));

                dataTable.add(data);
            }

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
            }  else return stringProperties.get(4).toString().contains(txtRecherche);
        });

        SortedList<List<StringProperty>> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);

    }
}
