package Controllers.ProductControllers;

import BddPackage.*;
import Models.ComponentProduction;
import Models.Medication;
import Models.Product;
import Models.RawMaterial;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class UpdateController implements Initializable {


    @FXML
    TextField tfName,tfReference, tfLimitQte,tfRechercheRawMad,tfRecherche;
    @FXML
    TableView<List<StringProperty>> rawMedTable,tableComposition;
    @FXML
    TableColumn<List<StringProperty>,String>  tcIdComponent,tcNameComponent,tcTypeComponent,tcReferenceComponent;
    @FXML
    TableColumn<List<StringProperty>,String> tcType,tcId,tcName,tcReference,tcQte;
    @FXML
    Button btnUpdate;
    @FXML
    Label lbSumQte;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;

    private final ProductOperation operation = new ProductOperation();
    private final RawMaterialOperation materialOperation = new RawMaterialOperation();
    private final MedicationOperation medicationOperation = new MedicationOperation();
    private final ComponentProductionRawMaterialOperation componentMaterialOperation = new ComponentProductionRawMaterialOperation();
    private final ComponentProductionMedicationOperation componentMedicationOperation = new ComponentProductionMedicationOperation();
    private final ObservableList<List<StringProperty>> dataTable = FXCollections.observableArrayList();
    private Product productUpdated;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();

        tcTypeComponent.setCellValueFactory(data -> data.getValue().get(0));
        tcIdComponent.setCellValueFactory(data -> data.getValue().get(1));
        tcNameComponent.setCellValueFactory(data -> data.getValue().get(2));
        tcReferenceComponent.setCellValueFactory(data -> data.getValue().get(3));

        tcType.setCellValueFactory(data -> data.getValue().get(0));
        tcId.setCellValueFactory(data -> data.getValue().get(1));
        tcName.setCellValueFactory(data -> data.getValue().get(2));
        tcReference.setCellValueFactory(data -> data.getValue().get(3));
        tcQte.setCellValueFactory(data -> data.getValue().get(4));

        refreshComponent();

        tfRechercheRawMad.textProperty().addListener((observable, oldValue, newValue) -> {

            if (!newValue.isEmpty()) {
                ObservableList<List<StringProperty>> items = rawMedTable.getItems();
                FilteredList<List<StringProperty>> filteredData = new FilteredList<>(items, e -> true);

                filteredData.setPredicate((Predicate<? super List<StringProperty>>) stringProperties -> {

                    if (stringProperties.get(1).toString().contains(newValue)) {
                        return true;
                    } else if (stringProperties.get(2).toString().contains(newValue)) {
                        return true;
                    } else return stringProperties.get(3).toString().contains(newValue);
                });

                SortedList<List<StringProperty>> sortedList = new SortedList<>(filteredData);
                sortedList.comparatorProperty().bind(rawMedTable.comparatorProperty());
                rawMedTable.setItems(sortedList);
            }else {
                refreshComponent();
            }
        });
    }

    public void Init(Product product){
        this.productUpdated = product;

        tfName.setText(product.getName());
        tfReference.setText(product.getReference());
        tfLimitQte.setText(String.valueOf(product.getLimitQte()));

        refreshComposition();
    }

    private void refreshComponent(){
        ObservableList<List<StringProperty>> componentDataTable = FXCollections.observableArrayList();

        try {
            ArrayList<Medication> medications = medicationOperation.getAll();

            medications.forEach(medication -> {
                List<StringProperty> data = new ArrayList<>();
                data.add(new SimpleStringProperty("med"));
                data.add( new SimpleStringProperty(String.valueOf(medication.getId())));
                data.add( new SimpleStringProperty(medication.getName()));
                data.add(new SimpleStringProperty(medication.getReference()));
                componentDataTable.add(data);
            });

        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            ArrayList<RawMaterial> rawMaterials =  materialOperation.getAll();

            rawMaterials.forEach(rawMaterial -> {
                List<StringProperty> data = new ArrayList<>();
                data.add(0, new SimpleStringProperty("raw"));
                data.add(1, new SimpleStringProperty(String.valueOf(rawMaterial.getId())));
                data.add(2, new SimpleStringProperty(rawMaterial.getName()));
                data.add(3, new SimpleStringProperty(rawMaterial.getReference()));
                componentDataTable.add(data);
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        rawMedTable.setItems(componentDataTable);

    }

    private void refreshComposition(){
        dataTable.clear();

        try {
            String query = "SELECT خلطة_الادوية.معرف_الدواء , خلطة_الادوية.معرف_المنتج , خلطة_الادوية.الكمية , الادوية.المعرف , الادوية.الاسم , الادوية.المرجع , الادوية.ارشيف " +
                    "FROM خلطة_الادوية , الادوية WHERE الادوية.ارشيف = 0 AND خلطة_الادوية.معرف_المنتج = ? AND خلطة_الادوية.معرف_الدواء = الادوية.المعرف ; ";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,productUpdated.getId());
            ResultSet resultSet = preparedStmt.executeQuery();

            while (resultSet.next()){
                List<StringProperty> data = new ArrayList<>();
                data.add(new SimpleStringProperty("med"));
                data.add(new SimpleStringProperty(String.valueOf(resultSet.getInt("المعرف"))));
                data.add(new SimpleStringProperty(resultSet.getString("الاسم")));
                data.add(new SimpleStringProperty(resultSet.getString("المرجع")));
                data.add(new SimpleStringProperty(String.valueOf(resultSet.getDouble("الكمية"))));

                dataTable.add(data);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            String query = "SELECT خلطة_المواد_الخام.معرف_المادة_الخام , خلطة_المواد_الخام.معرف_المنتج , خلطة_المواد_الخام.الكمية , المواد_الخام.المعرف , المواد_الخام.الاسم , المواد_الخام.المرجع , المواد_الخام.ارشيف FROM" +
                    " خلطة_المواد_الخام , المواد_الخام WHERE خلطة_المواد_الخام.معرف_المادة_الخام = المواد_الخام.المعرف  AND خلطة_المواد_الخام.معرف_المنتج = ?; ";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,productUpdated.getId());
            ResultSet resultSet = preparedStmt.executeQuery();

            while (resultSet.next()){
                List<StringProperty> data = new ArrayList<>();
                data.add(new SimpleStringProperty("raw"));
                data.add(new SimpleStringProperty(String.valueOf(resultSet.getInt("المعرف"))));
                data.add(new SimpleStringProperty(resultSet.getString("الاسم")));
                data.add(new SimpleStringProperty(resultSet.getString("المرجع")));
                data.add(new SimpleStringProperty(String.valueOf(resultSet.getDouble("الكمية"))));

                dataTable.add(data);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        tableComposition.setItems(dataTable);
        sumQte();
    }


    @FXML
    private void tableMaterialMedClick(MouseEvent mouseEvent) {
        if ( mouseEvent.getClickCount() == 2 && mouseEvent.getButton().equals(MouseButton.PRIMARY) ){

            ActionAddToComposition();
        }
    }
    @FXML
    private void ActionAddToComposition(){
        List<StringProperty> dataSelected = rawMedTable.getSelectionModel().getSelectedItem();
        if (dataSelected != null) {
            int ex = exist(dataSelected);
            if ( ex != -1 ){
                try {
                    TextInputDialog dialog = new TextInputDialog();

                    dialog.setTitle("الكمية");
                    dialog.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    dialog.initOwner(this.tfRecherche.getScene().getWindow());
                    dialog.setHeaderText("ادخل الكمية ");
                    dialog.setContentText("الكمية :");

                    Optional<String> result = dialog.showAndWait();

                    result.ifPresent(qte -> {
//                        dataTable.get(ex).get(4).setValue(qte);

                        ComponentProduction componentProduction = new ComponentProduction();
                        componentProduction.setIdComponent(Integer.parseInt(dataTable.get(ex).get(1).getValue()));
                        componentProduction.setIdProduct(productUpdated.getId());
                        componentProduction.setQte(Double.parseDouble(qte));
                        switch (dataTable.get(ex).get(0).getValue()) {
                            case "med":
                                updateComponentMedication(componentProduction);
                                break;
                            case "raw":
                                updateComponentRawMaterial(componentProduction);
                        }
                        refreshComposition();
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                try {
                    TextInputDialog dialog = new TextInputDialog();

                    dialog.setTitle("الكمية");
                    dialog.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    dialog.initOwner(this.tfRecherche.getScene().getWindow());
                    dialog.setHeaderText("ادخل الكمية ");
                    dialog.setContentText("الكمية :");

                    Optional<String> result = dialog.showAndWait();

                    result.ifPresent(qte -> {
                        List<StringProperty> data = new ArrayList<>();
                        data.add(0, new SimpleStringProperty(dataSelected.get(0).getValue()));
                        data.add(1, new SimpleStringProperty(dataSelected.get(1).getValue()));
                        data.add(2, new SimpleStringProperty(dataSelected.get(2).getValue()));
                        data.add(3, new SimpleStringProperty(dataSelected.get(3).getValue()));
                        data.add(4, new SimpleStringProperty(qte));

                        ComponentProduction componentProduction = new ComponentProduction();
                        componentProduction.setIdComponent(Integer.parseInt(dataSelected.get(1).getValue()));
                        componentProduction.setIdProduct(productUpdated.getId());
                        componentProduction.setQte(Double.parseDouble(qte));
                        switch (dataSelected.get(0).getValue()){
                            case "med":
                                insertComponentMedication(componentProduction);
                                break;
                            case "raw":
                                insertComponentRawMaterial(componentProduction);
                        }
                        refreshComposition();

//                        dataTable.add(data);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            tableComposition.setItems(dataTable);
        }
    }

    private int exist(List<StringProperty> dataSelected){
        AtomicInteger ex = new AtomicInteger(-1);
        for (int i = 0 ; i < dataTable.size() ; i++) {
            if (dataTable.get(i).get(0).getValue().equals(dataSelected.get(0).getValue()) && dataTable.get(i).get(1).getValue().equals(dataSelected.get(1).getValue()) ){
                ex.set(i);
                break;
            }
        }
        return ex.get();
    }

    @FXML
    private void tableProductClick(MouseEvent mouseEvent) {
        if ( mouseEvent.getClickCount() == 2 && mouseEvent.getButton().equals(MouseButton.PRIMARY) ){

            ActionModifiedQte();
        }
    }
    @FXML
    private void ActionModifiedQte(){
        int compoSelectedIndex = tableComposition.getSelectionModel().getSelectedIndex();
        if (compoSelectedIndex != -1){
            TextInputDialog dialog = new TextInputDialog();

            dialog.setTitle("الكمية");
            dialog.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            dialog.initOwner(this.tfRecherche.getScene().getWindow());
            dialog.setHeaderText("تعديل الكمية ");
            dialog.setContentText("الكمية :");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(qte -> {
//                dataTable.get(compoSelectedIndex).get(4).setValue(qte);

                ComponentProduction componentProduction = new ComponentProduction();
                componentProduction.setIdComponent(Integer.parseInt(dataTable.get(compoSelectedIndex).get(1).getValue()));
                componentProduction.setIdProduct(productUpdated.getId());
                componentProduction.setQte(Double.parseDouble(qte));
                switch (dataTable.get(compoSelectedIndex).get(0).getValue()) {
                    case "med":
                        updateComponentMedication(componentProduction);
                        break;
                    case "raw":
                        updateComponentRawMaterial(componentProduction);
                }
                refreshComposition();

//                tableComposition.setItems(dataTable);
            });
        }
    }
    @FXML
    private void ActionDeleteFromComposition(){
        int compoSelectedIndex = tableComposition.getSelectionModel().getSelectedIndex();
        if (compoSelectedIndex != -1){

            ComponentProduction componentProduction = new ComponentProduction();
            componentProduction.setIdComponent(Integer.parseInt(dataTable.get(compoSelectedIndex).get(1).getValue()));
            componentProduction.setIdProduct(productUpdated.getId());

            switch (dataTable.get(compoSelectedIndex).get(0).getValue()) {
                case "med":
                    deleteComponentMedication(componentProduction);
                    break;
                case "raw":
                    deleteComponentRawMaterial(componentProduction);
            }
            refreshComposition();

//            dataTable.remove(compoSelectedIndex);
//            tableComposition.setItems(dataTable);
        }
    }

    private void sumQte(){
        double sum = 0.0;
        for (List<StringProperty> data : dataTable){
            sum += Double.parseDouble(data.get(4).getValue());
        }
        lbSumQte.setText(String.valueOf(sum));
    }
    @FXML
    private void ActionAnnulledUpdate(){
        closeDialog(btnUpdate);
    }
    @FXML
    void ActionUpdate(ActionEvent event) {

        String name = tfName.getText().trim();
        String reference = tfReference.getText().trim();
        String limitQte = tfLimitQte.getText().trim();

        if (!name.isEmpty() && !reference.isEmpty() && !limitQte.isEmpty() && dataTable.size() != 0){

            Product product = new Product();
            product.setName(name);
            product.setReference(reference);
            product.setLimitQte(Integer.parseInt(limitQte));

            boolean upd = update(product,productUpdated);
            if (upd ) {
                ActionAnnulledUpdate();
            }
            else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("تحذير ");
                alertWarning.setContentText("خطأ غير معروف");
                alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");
                alertWarning.showAndWait();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء ملأ جميع الحقول");
            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    private boolean update(Product product,Product productUpdated) {
        boolean update = false;
        try {
            update = operation.update(product,productUpdated);
            return update;
        }catch (Exception e){
            e.printStackTrace();
            return update;
        }
    }

    private boolean insertComponentMedication(ComponentProduction componentProduction){
        boolean insert = false;
        try {
            insert = componentMedicationOperation.insert(componentProduction);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean updateComponentMedication(ComponentProduction componentProduction){
        boolean update = false;
        try {
            update = componentMedicationOperation.update(componentProduction, componentProduction);
            return update;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean deleteComponentMedication(ComponentProduction componentProduction){
        boolean delete = false;
        try {
            delete = componentMedicationOperation.delete(componentProduction);
            return delete;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean insertComponentRawMaterial(ComponentProduction componentProduction){
        boolean update = false;
        try {
            update = componentMaterialOperation.insert(componentProduction);
            return update;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean updateComponentRawMaterial(ComponentProduction componentProduction){
        boolean update = false;
        try {
            update = componentMaterialOperation.update(componentProduction, componentProduction);
            return update;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean deleteComponentRawMaterial(ComponentProduction componentProduction){
        boolean del = false;
        try {
            del = componentMaterialOperation.delete(componentProduction);
            return del;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }

    @FXML
    private void ActionRefresh(){
        clearRecherche();
        refreshComposition();
    }

    private void clearRecherche(){
        tfRecherche.clear();
    }

    @FXML
    void ActionSearch() {
        // filtrer les données
        ObservableList<List<StringProperty>> items = tableComposition.getItems();
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
            }  else return stringProperties.get(4).toString().contains(txtRecherche);
        });

        SortedList<List<StringProperty>> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(tableComposition.comparatorProperty());
        tableComposition.setItems(sortedList);
        sumQte();
    }
}
