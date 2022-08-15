package Controllers.ProviderControllers;


import BddPackage.ProviderOperation;
import Models.Client;
import Models.Provider;
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
    TableView<Provider> table;
    @FXML
    TableColumn<Client,String> clName,clAddress;
    @FXML
    TableColumn<Client,Integer> clId;


    private final ObservableList<Provider> dataTable = FXCollections.observableArrayList();
    private final ProviderOperation operation = new ProviderOperation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        clId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        refresh();
    }

    @FXML
    private void ActionDeleteFromArchive(){
        Provider provider = table.getSelectionModel().getSelectedItem();

        if (provider != null){
            try {

                Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                alertConfirmation.setHeaderText("تاكيد الارشفة");
                alertConfirmation.setContentText("هل انت متاكد من الغاء ارشفة  المورد" );
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
                        operation.DeleteFromArchive(provider);
                        ActionAnnuler();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء اختيار مورد لالغاء أرشفته");
            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
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
        ArrayList<Provider> providers = operation.getAllArchive();
        dataTable.setAll(providers);
        table.setItems(dataTable);
    }

    @FXML
    void ActionSearch() {
        // filtrer les données
        ObservableList<Provider> dataProviders = table.getItems();
        FilteredList<Provider> filteredData = new FilteredList<>(dataProviders, e -> true);
        String txtRecherche = tfRecherche.getText().trim();

        filteredData.setPredicate((Predicate<? super Provider>) provider -> {
            if (txtRecherche.isEmpty()) {
                //loadDataInTable();
                return true;
            } else if (provider.getName().contains(txtRecherche)) {
                return true;
            }else return  (provider.getAddress().contains(txtRecherche)) ;
        });

        SortedList<Provider> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);

    }
}
