package Controllers.MedicationControllers;

import BddPackage.MedicationOperation;
import Models.Medication;
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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class MainController implements Initializable {

    @FXML
    Label lbName;
    @FXML
    TextField tfRecherche;
    @FXML
    TableView<Medication> table;
    @FXML
    TableColumn<Medication,String> clName,clReference;
    @FXML
    TableColumn<Medication,Integer> clId,clLimiteQte,clQte;


    private final ObservableList<Medication> dataTable = FXCollections.observableArrayList();
    private final MedicationOperation operation = new MedicationOperation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lbName.setText("قائمة الادوية");

        clId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clReference.setCellValueFactory(new PropertyValueFactory<>("reference"));
        clLimiteQte.setCellValueFactory(new PropertyValueFactory<>("limitQte"));
        clQte.setCellValueFactory(new PropertyValueFactory<>("qte"));

        refresh();
    }

    @FXML
    private void ActionAdd(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/MedicationViews/AddView.fxml"));
            DialogPane temp = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
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

        Medication medication = table.getSelectionModel().getSelectedItem();
        if (medication != null){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/MedicationViews/UpdateView.fxml"));
                DialogPane temp = loader.load();
                UpdateController controller = loader.getController();
                controller.InitUpdate(medication);
                Dialog<ButtonType> dialog = new Dialog<>();
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
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير");
            alertWarning.setContentText("الرجاء اختيار دواء من اجل التعديل");
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    @FXML
    private void ActionAddToArchive(){
        Medication medication = table.getSelectionModel().getSelectedItem();

        if (medication != null ){
            if (medication.getQte() == 0) {
                try {

                    Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                    alertConfirmation.setHeaderText("تاكيد الارشفة");
                    alertConfirmation.setContentText("هل انت متاكد من ارشفة الدواء");
                    alertConfirmation.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.setText("موافق");

                    Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
                    cancel.setText("الغاء");

                    alertConfirmation.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.CANCEL) {
                            alertConfirmation.close();
                        } else if (response == ButtonType.OK) {
                            operation.AddToArchive(medication);
                            refresh();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                Alert alertInformation = new Alert(Alert.AlertType.INFORMATION);
                alertInformation.setHeaderText("لا تستطيع الارشفة ");
                alertInformation.setContentText("لا تستطيع ارشفة الدواء الحالي لانه متبقي في المخزن");
                alertInformation.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                Button okButton = (Button) alertInformation.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");
                alertInformation.showAndWait();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء اختيار دواء لارشفته");
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }


    @FXML
    private void ActionDeleteFromArchive(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/MedicationViews/ArchiveView.fxml"));
            DialogPane temp = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
            closeButton.setVisible(false);
            dialog.showAndWait();

            refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refresh(){

        ArrayList<Medication> medication = operation.getAll();
        dataTable.setAll(medication);
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
