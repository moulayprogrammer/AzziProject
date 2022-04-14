package Controllers.ProductControllers;

import BddPackage.MedicationOperation;
import Models.Medication;
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
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class SelectMedicationController implements Initializable {

    @FXML
    TextField tfRecherche;
    @FXML
    TableView<Medication> table;
    @FXML
    TableColumn<Medication,String> clName,clReference;
    @FXML
    TableColumn<Medication,Integer> clId,clLimiteQte;

    private Medication medication;
    private final ObservableList<Medication> dataTable = FXCollections.observableArrayList();
    private final MedicationOperation operation = new MedicationOperation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        clId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clReference.setCellValueFactory(new PropertyValueFactory<>("reference"));
        clLimiteQte.setCellValueFactory(new PropertyValueFactory<>("limitQte"));

        refresh();
    }

    public void Init(Medication medication){
        this.medication = medication;
    }

    @FXML
    private void selectMedication(){
        Medication medication = table.getSelectionModel().getSelectedItem();

        if (medication != null){

            this.medication.setId(medication.getId());
            this.medication.setName(medication.getName());
            this.medication.setReference(medication.getReference());
            this.medication.setLimitQte(medication.getLimitQte());
            ActionAnnuler();

        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء اختيار دواء لتحديده");
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
        ArrayList<Medication> medications = operation.getAll();
        dataTable.setAll(medications);
        table.setItems(dataTable);
    }

    @FXML
    void ActionSearch() {
        // filtrer les données
        ObservableList<Medication> dataMedication = table.getItems();
        FilteredList<Medication> filteredData = new FilteredList<>(dataMedication, e -> true);
        String txtRecherche = tfRecherche.getText().trim();

        filteredData.setPredicate((Predicate<? super Medication>) medication -> {
            if (txtRecherche.isEmpty()) {
                //loadDataInTable();
                return true;
            } else if (medication.getName().contains(txtRecherche)) {
                return true;
            } else if (medication.getReference().contains(txtRecherche)) {
                return true;
            } else return String.valueOf(medication.getLimitQte()).contains(txtRecherche);
        });

        SortedList<Medication> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);

    }
}
