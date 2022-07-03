package Controllers.ClientControllers;

import BddPackage.ClientOperation;
import BddPackage.MedicationOperation;
import Models.Client;
import Models.Medication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class MainController implements Initializable {

    @FXML
    AnchorPane MainPanel;
    @FXML
    TextField tfRecherche;
    @FXML
    TableView<Client> table;
    @FXML
    TableColumn<Client,String> clName,clAddress,clActivity,clNationalNumber;
    @FXML
    TableColumn<Client,Integer> clId;


    private final ObservableList<Client> dataTable = FXCollections.observableArrayList();
    private final ClientOperation operation = new ClientOperation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        clAddress.setCellValueFactory(new PropertyValueFactory<>("activity"));
        clAddress.setCellValueFactory(new PropertyValueFactory<>("nationalNumber"));

        refresh();
    }

    @FXML
    private void ActionAdd(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ClientViews/AddView.fxml"));
            DialogPane temp = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.showAndWait();

            refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionUpdate(){


        Client client = table.getSelectionModel().getSelectedItem();
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
        }
    }

    @FXML
    private void ActionAddToArchive(){
        Client client = table.getSelectionModel().getSelectedItem();

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
        }
    }


    @FXML
    private void ActionDeleteFromArchive(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ClientViews/ArchiveView.fxml"));
            DialogPane temp = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.showAndWait();

            refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refresh(){
        ArrayList<Client> clients = operation.getAll();
        dataTable.setAll(clients);
        table.setItems(dataTable);
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
        ObservableList<Client> dataClient = table.getItems();
        FilteredList<Client> filteredData = new FilteredList<>(dataClient, e -> true);
        String txtRecherche = tfRecherche.getText().trim();

        filteredData.setPredicate((Predicate<? super Client>) client -> {
            if (txtRecherche.isEmpty()) {
                //loadDataInTable();
                return true;
            } else if (client.getName().contains(txtRecherche)) {
                return true;
            }else if (client.getAddress().contains(txtRecherche)) {
                return true;
            }else if (client.getActivity().contains(txtRecherche)) {
                return true;
            } else return  (client.getNationalNumber().contains(txtRecherche)) ;
        });

        SortedList<Client> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }
}
