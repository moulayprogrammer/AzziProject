package Controllers.ProductControllers;

import BddPackage.RawMaterialOperation;
import Models.RawMaterial;
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

public class SelectRawMaterialController implements Initializable {

    @FXML
    TextField tfRecherche;
    @FXML
    TableView<RawMaterial> table;
    @FXML
    TableColumn<RawMaterial,String> clName,clReference;
    @FXML
    TableColumn<RawMaterial,Integer> clId,clLimiteQte;

    private final ObservableList<RawMaterial> dataTable = FXCollections.observableArrayList();
    private final RawMaterialOperation operation = new RawMaterialOperation();
    private RawMaterial rawMaterial;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        clId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clReference.setCellValueFactory(new PropertyValueFactory<>("reference"));
        clLimiteQte.setCellValueFactory(new PropertyValueFactory<>("limitQte"));

        refresh();
    }
    public void Init(RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
    }
    @FXML
    private void selectRawMaterial(){
        RawMaterial rawMaterial = table.getSelectionModel().getSelectedItem();

        if (rawMaterial != null){
            try {

                this.rawMaterial.setId(rawMaterial.getId());
                this.rawMaterial.setName(rawMaterial.getName());
                this.rawMaterial.setReference(rawMaterial.getReference());
                this.rawMaterial.setLimitQte(rawMaterial.getLimitQte());
                ActionAnnuler();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء اختيار مادة خام ");
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
        ArrayList<RawMaterial> rawMaterials = operation.getAll();
        dataTable.setAll(rawMaterials);
        table.setItems(dataTable);
    }

    @FXML
    void ActionSearch() {
        // filtrer les données
        ObservableList<RawMaterial> dataRaw = table.getItems();
        FilteredList<RawMaterial> filteredData = new FilteredList<>(dataRaw, e -> true);
        String txtRecherche = tfRecherche.getText().trim();

        filteredData.setPredicate((Predicate<? super RawMaterial>) rawMaterial -> {
            if (txtRecherche.isEmpty()) {
                //loadDataInTable();
                return true;
            } else if (rawMaterial.getName().contains(txtRecherche)) {
                return true;
            } else if (rawMaterial.getReference().contains(txtRecherche)) {
                return true;
            } else return String.valueOf(rawMaterial.getLimitQte()).contains(txtRecherche);
        });

        SortedList<RawMaterial> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);

    }


}
