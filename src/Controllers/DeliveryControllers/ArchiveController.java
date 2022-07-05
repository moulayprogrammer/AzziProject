package Controllers.DeliveryControllers;

import BddPackage.ClientOperation;
import BddPackage.DeliveryOperation;
import Models.Client;
import Models.Delivery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class ArchiveController implements Initializable {

    @FXML
    TextField tfRecherche;
    @FXML
    TableView<Delivery> table;
    @FXML
    TableColumn<Delivery,String> clName,clDriverL,clTrackNbr01,clTrackNbr02;
    @FXML
    TableColumn<Delivery,Integer> clId;


    private final ObservableList<Delivery> dataTable = FXCollections.observableArrayList();
    private final DeliveryOperation operation = new DeliveryOperation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        clId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clDriverL.setCellValueFactory(new PropertyValueFactory<>("driverLicence"));
        clTrackNbr01.setCellValueFactory(new PropertyValueFactory<>("trackNumber"));
        clTrackNbr02.setCellValueFactory(new PropertyValueFactory<>("trackNumber2"));

        refresh();
    }

    @FXML
    private void ActionDeleteFromArchive(){
        Delivery delivery = table.getSelectionModel().getSelectedItem();

        if (delivery != null){
            try {

                Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                alertConfirmation.setHeaderText("تاكيد الارشفة");
                alertConfirmation.setContentText("هل انت متاكد من الغاء ارشفة الموصل" );
                alertConfirmation.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");

                Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
                cancel.setText("الغاء");

                alertConfirmation.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.CANCEL) {
                        alertConfirmation.close();
                    } else if (response == ButtonType.OK) {
                        operation.DeleteFromArchive(delivery);
                        ActionAnnuler();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء اختيار موصل لالغاء أرشفته");
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
        ArrayList<Delivery> deliveries = operation.getAllArchive();
        dataTable.setAll(deliveries);
        table.setItems(dataTable);
    }

    @FXML
    void ActionSearch() {
        // filtrer les données
        ObservableList<Delivery> dataDeliveries = table.getItems();
        FilteredList<Delivery> filteredData = new FilteredList<>(dataDeliveries, e -> true);
        String txtRecherche = tfRecherche.getText().trim();

        filteredData.setPredicate((Predicate<? super Delivery>) delivery -> {
            if (txtRecherche.isEmpty()) {
                //loadDataInTable();
                return true;
            } else if (delivery.getName().contains(txtRecherche)) {
                return true;
            } else if (delivery.getDriverLicence().contains(txtRecherche)) {
                return true;
            }else if (delivery.getTrackNumber().contains(txtRecherche)) {
                return true;
            }else return  (delivery.getTrackNumber2().contains(txtRecherche)) ;
        });

        SortedList<Delivery> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);

    }
}
