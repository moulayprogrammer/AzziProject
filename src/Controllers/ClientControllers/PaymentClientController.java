package Controllers.ClientControllers;


import BddPackage.PaymentsClientOperation;
import Models.Client;
import Models.Payments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class PaymentClientController implements Initializable {

    @FXML
    TextField tfRecherche;
    @FXML
    TableView<Payments> table;
    @FXML
    TableColumn<Payments, LocalDate> clDate;
    @FXML
    TableColumn<Payments, Double> clPay, clRest;
    @FXML
    TableColumn<Payments,Integer> clId;
    @FXML
    Label lbClient;


    private final ObservableList<Payments> dataTable = FXCollections.observableArrayList();
    private final PaymentsClientOperation operation = new PaymentsClientOperation();
    private Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        clId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        clPay.setCellValueFactory(new PropertyValueFactory<>("pay"));
        clRest.setCellValueFactory(new PropertyValueFactory<>("rest"));


    }

    public void Init(Client client){
        this.client = client;

        lbClient.setText(client.getName() + client.getReference());
        refresh();
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
        ArrayList<Payments> payments = operation.getAllByClient(this.client.getId());
        dataTable.setAll(payments);
        table.setItems(dataTable);
    }

    @FXML
    void ActionSearch() {
        // filtrer les données
        ObservableList<Payments> dataPayments = table.getItems();
        FilteredList<Payments> filteredData = new FilteredList<>(dataPayments, e -> true);
        String txtRecherche = tfRecherche.getText().trim();

        filteredData.setPredicate((Predicate<? super Payments>) payment -> {
            if (txtRecherche.isEmpty()) {
                //loadDataInTable();
                return true;
            } else if (String.valueOf(payment.getId()).contains(txtRecherche)) {
                return true;
            }else if (payment.getDate().toString().contains(txtRecherche)) {
                return true;
            }else if (String.valueOf(payment.getPay()).contains(txtRecherche)) {
                return true;
            } else return  (String.valueOf(payment.getRest()).contains(txtRecherche)) ;
        });

        SortedList<Payments> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);

    }
}
