package Controllers.DamageMaterialControllers;

import BddPackage.ComponentDamageRawMaterialOperation;
import BddPackage.ComponentStoreRawMaterialOperation;
import BddPackage.ConnectBD;
import BddPackage.DamageRawMaterialOperation;
import Models.ComponentDamage;
import Models.ComponentStore;
import Models.Damage;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class MainController implements Initializable {

    @FXML
    TextField tfRecherche;
    @FXML
    TableView<List<StringProperty>> table;
    @FXML
    TableColumn<List<StringProperty>,String> clId, clName, clIdMaterial, clDate,clQte,clRaison;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;
    private final DamageRawMaterialOperation operation = new DamageRawMaterialOperation();
    private final ComponentStoreRawMaterialOperation componentStoreRawMaterialOperation = new ComponentStoreRawMaterialOperation();
    private final ComponentDamageRawMaterialOperation componentDamageRawMaterialOperation = new ComponentDamageRawMaterialOperation();
    private final ObservableList<List<StringProperty>> dataTable = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();

        clId.setCellValueFactory(data -> data.getValue().get(0));
        clName.setCellValueFactory(data -> data.getValue().get(1));
        clIdMaterial.setCellValueFactory(data -> data.getValue().get(2));
        clDate.setCellValueFactory(data -> data.getValue().get(3));
        clQte.setCellValueFactory(data -> data.getValue().get(4));
        clRaison.setCellValueFactory(data -> data.getValue().get(5));

        refresh();
    }

    @FXML
    private void ActionAdd(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/DamageMaterialViews/AddView.fxml"));
            DialogPane temp = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.initOwner(this.tfRecherche.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setVisible(false);
            dialog.showAndWait();

            refresh();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void tableClick(MouseEvent mouseEvent) {
        if ( mouseEvent.getClickCount() == 2 && mouseEvent.getButton().equals(MouseButton.PRIMARY) ){

            ActionUpdate();
        }
    }
    @FXML
    private void ActionUpdate(){

        List<StringProperty> data = table.getSelectionModel().getSelectedItem();
        if (data != null){
            Damage damage = new Damage(
                    Integer.parseInt(data.get(0).getValue()),
                    Integer.parseInt(data.get(2).getValue()),
                    LocalDate.parse(data.get(3).getValue()),
                    Integer.parseInt(data.get(4).getValue()),
                    data.get(5).getValue()
            );
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/DamageMaterialViews/UpdateView.fxml"));
                DialogPane temp = loader.load();
                UpdateController controller = loader.getController();
                controller.InitUpdate(damage);
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(temp);
                dialog.resizableProperty().setValue(false);
                dialog.initOwner(this.tfRecherche.getScene().getWindow());
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
            alertWarning.setContentText("الرجاء اختيار زبون من اجل التعديل");
            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    @FXML
    private void ActionDelete(){
        List<StringProperty> data = table.getSelectionModel().getSelectedItem();
        if (data != null){
            Damage damage = new Damage(
                    Integer.parseInt(data.get(0).getValue()),
                    Integer.parseInt(data.get(2).getValue()),
                    LocalDate.parse(data.get(3).getValue()),
                    Integer.parseInt(data.get(4).getValue()),
                    data.get(5).getValue()
            );
            try {
                Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                alertConfirmation.setHeaderText("تاكيد الحذف");
                alertConfirmation.setContentText("هل انت متاكد من حذف التلف" );
                alertConfirmation.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                alertConfirmation.initOwner(this.tfRecherche.getScene().getWindow());
                Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");

                Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
                cancel.setText("الغاء");

                alertConfirmation.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.CANCEL) {
                        alertConfirmation.close();
                    } else if (response == ButtonType.OK) {
                        ArrayList<ComponentDamage> damages = componentDamageRawMaterialOperation.getAllByDamage(damage.getId());
                        operation.delete(damage);
                        deleteComponentDamage(damages);
                    }
                });

                refresh();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير");
            alertWarning.setContentText("الرجاء اختيار زبون من اجل التعديل");
            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    private void deleteComponentDamage(ArrayList<ComponentDamage> componentDamages){
        for (ComponentDamage selectDamage : componentDamages){
            ComponentStore store = componentStoreRawMaterialOperation.get(selectDamage.getIdComponent(),selectDamage.getIdReference());
            store.setQteConsumed(store.getQteConsumed() - selectDamage.getQte());
            updateQteComponentStoreMaterial(store);
            componentDamageRawMaterialOperation.delete(selectDamage);
        }
    }

    private boolean updateQteComponentStoreMaterial( ComponentStore store){
        boolean update = false;
        try {
            update = componentStoreRawMaterialOperation.updateQte(store);
            return update;
        }catch (Exception e){
            e.printStackTrace();
            return update;
        }
    }

    private void refresh(){
        try {
            dataTable.clear();
            if (conn.isClosed()) conn = connectBD.connect();
            ArrayList<Damage> list = new ArrayList<>();
            String query = "SELECT * FROM اتلاف_المواد_الخام, المواد_الخام WHERE اتلاف_المواد_الخام.معرف_المادة = المواد_الخام.المعرف;";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                List<StringProperty> data = new ArrayList<>();
                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("المعرف"))));
                data.add( new SimpleStringProperty(resultSet.getString("الاسم")));
                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("معرف_المادة"))));
                data.add( new SimpleStringProperty(String.valueOf(resultSet.getDate("تاريخ_التلف").toLocalDate())));
                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("الكمية"))));
                data.add( new SimpleStringProperty(resultSet.getString("السبب")));

                dataTable.add(data);
            }
            table.setItems(dataTable);
            conn.close();
        }catch (Exception e){

        }
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
        ObservableList<List<StringProperty>> items = table.getItems();
        FilteredList<List<StringProperty>> filteredData = new FilteredList<>(items, e -> true);
        String txtRecherche = tfRecherche.getText().trim();

        filteredData.setPredicate((Predicate<? super List<StringProperty>>) stringProperties -> {
            if (txtRecherche.isEmpty()) {
                ActionRefresh();
                return true;
            } else if (stringProperties.get(1).toString().contains(txtRecherche)) {
                return true;
            } else if (stringProperties.get(2).toString().contains(txtRecherche)) {
                return true;
            }else if (stringProperties.get(3).toString().contains(txtRecherche)) {
                return true;
            } else if (stringProperties.get(4).toString().contains(txtRecherche)) {
                return true;
            }  else return stringProperties.get(5).toString().contains(txtRecherche);
        });

        SortedList<List<StringProperty>> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }
}
