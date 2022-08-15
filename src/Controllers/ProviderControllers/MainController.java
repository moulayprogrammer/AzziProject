package Controllers.ProviderControllers;

import BddPackage.*;
import Models.Client;
import Models.ComponentReceipt;
import Models.Provider;
import Models.Receipt;
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
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class MainController implements Initializable {

    @FXML
    TextField tfRecherche;
    @FXML
    TableView<List<StringProperty>> table;
    @FXML
    TableColumn<List<StringProperty>,String> clId,clName,clAddress,clTransaction,clPaying,clDebt;


    private final ObservableList<List<StringProperty>> dataTable = FXCollections.observableArrayList();
    private final ProviderOperation operation = new ProviderOperation();
    private final ReceiptRawMaterialOperation receiptRawMaterialOperation = new ReceiptRawMaterialOperation();
    private final ReceiptMedicationOperation receiptMedicationOperation = new ReceiptMedicationOperation();
    private final ComponentReceiptMedicationOperation componentMedicationOperation = new ComponentReceiptMedicationOperation();
    private final ComponentReceiptRawMaterialOperation componentReceiptRawMaterialOperation = new ComponentReceiptRawMaterialOperation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        clId.setCellValueFactory(data -> data.getValue().get(0));
        clName.setCellValueFactory(data -> data.getValue().get(1));
        clAddress.setCellValueFactory(data -> data.getValue().get(2));
        clTransaction.setCellValueFactory(data -> data.getValue().get(3));
        clPaying.setCellValueFactory(data -> data.getValue().get(4));
        clDebt.setCellValueFactory(data -> data.getValue().get(5));

        refresh();
    }

    @FXML
    private void ActionAdd(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ProviderViews/AddView.fxml"));
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionUpdate(){
        List<StringProperty> data = table.getSelectionModel().getSelectedItem();

        if (data != null){
            try {
                Provider provider = operation.get(Integer.parseInt(data.get(0).getValue()));

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ProviderViews/UpdateView.fxml"));
                DialogPane temp = loader.load();
                UpdateController controller = loader.getController();
                controller.InitUpdate(provider);
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
            alertWarning.setContentText("الرجاء اختيار مورد من اجل التعديل");
            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    @FXML
    private void ActionAddToArchive(){
        List<StringProperty> data = table.getSelectionModel().getSelectedItem();

        if (data != null){
            if (data.get(5).getValue().equals("0,00")) {
                try {
                    Provider provider = operation.get(Integer.parseInt(data.get(0).getValue()));

                    Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                    alertConfirmation.setHeaderText("تاكيد الارشفة");
                    alertConfirmation.setContentText("هل انت متاكد من ارشفة المورد");
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
                            operation.AddToArchive(provider);
                            refresh();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                Alert alertInformation = new Alert(Alert.AlertType.INFORMATION);
                alertInformation.setHeaderText("لا تستطيع الارشفة ");
                alertInformation.setContentText("لا تستطيع ارشفة المورد الحالي لا يزال يملك دين");
                alertInformation.initOwner(this.tfRecherche.getScene().getWindow());
                alertInformation.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                Button okButton = (Button) alertInformation.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");
                alertInformation.showAndWait();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء اختيار مورد لارشفته");
            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }


    @FXML
    private void ActionDeleteFromArchive(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ProviderViews/ArchiveView.fxml"));
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionPayDebt(){
        List<StringProperty> data = table.getSelectionModel().getSelectedItem();

        if (data != null){
            try {
                Provider provider = operation.get(Integer.parseInt(data.get(0).getValue()));
                if (!Objects.equals(data.get(5).getValue(), "0,00")){
                    TextInputDialog dialog = new TextInputDialog();

                    dialog.setTitle("التسديد");
                    dialog.initOwner(this.tfRecherche.getScene().getWindow());
                    dialog.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    dialog.setHeaderText("ادخل السعر المسدد ");
                    dialog.setContentText("السعر :");

                    Optional<String> result = dialog.showAndWait();

                    result.ifPresent(price -> {

                        if (!price.isEmpty() ) {
                            double pr = Double.parseDouble(price);

                            ArrayList<Receipt> receipts = receiptMedicationOperation.getAllByProvider(provider.getId());
                            for (int i = 0; i < receipts.size(); i++) {
                                Receipt receipt = receipts.get(i);
                                ArrayList<ComponentReceipt> componentReceipts = componentMedicationOperation.getAllByReceipt(receipt.getId());
                                AtomicReference<Double> sumR = new AtomicReference<>(0.0);
                                componentReceipts.forEach(componentReceipt -> {
                                    double pre = componentReceipt.getPrice() * componentReceipt.getQte();
                                    sumR.updateAndGet(v -> (double) (v + pre));
                                });
                                double debt = sumR.get() - receipt.getPaying();
                                if (debt > 0){
                                    if (pr <= debt){
                                        double newPaying = pr + receipt.getPaying();
                                        pr = 0;
                                        PayDebtReceiptMedication(receipt.getId(),newPaying);
                                        break;
                                    }else {
                                        double newPaying = debt + receipt.getPaying();
                                        pr = pr - debt;
                                        PayDebtReceiptMedication(receipt.getId(),newPaying);
                                    }
                                }
                            }
                            if (pr > 0 ){
                                ArrayList<Receipt> receiptsMaterial = receiptRawMaterialOperation.getAllByProvider(provider.getId());
                                for (int i = 0; i < receiptsMaterial.size(); i++) {
                                    Receipt receipt = receiptsMaterial.get(i);
                                    ArrayList<ComponentReceipt> componentReceipts = componentReceiptRawMaterialOperation.getAllByReceipt(receipt.getId());
                                    AtomicReference<Double> sumR = new AtomicReference<>(0.0);
                                    componentReceipts.forEach(componentReceipt -> {
                                        double pre = componentReceipt.getPrice() * componentReceipt.getQte();
                                        sumR.updateAndGet(v -> (double) (v + pre));
                                    });
                                    double debt = sumR.get() - receipt.getPaying();
                                    if (debt > 0){
                                        if (pr <= debt){
                                            double newPaying = pr + receipt.getPaying();
                                            pr = 0;
                                            PayDebtReceiptMaterial(receipt.getId(),newPaying);
                                            break;
                                        }else {
                                            double newPaying = debt + receipt.getPaying();
                                            pr = pr - debt;
                                            PayDebtReceiptMaterial(receipt.getId(),newPaying);
                                        }
                                    }
                                }
                            }
                            if (pr > 0 ){
                                Alert alertInformation = new Alert(Alert.AlertType.INFORMATION);
                                alertInformation.setHeaderText("المبلغ المتبقي ");
                                alertInformation.setContentText("المبلغ المتبقي من التسديد هو   " + String.format(Locale.FRANCE, "%,.2f", (pr)));
                                alertInformation.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                                alertInformation.initOwner(this.tfRecherche.getScene().getWindow());
                                Button okButton = (Button) alertInformation.getDialogPane().lookupButton(ButtonType.OK);
                                okButton.setText("موافق");
                                alertInformation.showAndWait();
                            }
                            refresh();
                        }
                    });
                }else {
                    Alert alertWarning = new Alert(Alert.AlertType.INFORMATION);
                    alertWarning.setHeaderText("لا يوجد دين ");
                    alertWarning.setContentText("المورد المختار يملك 0.0 دج دين ");
                    alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                    alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.setText("موافق");
                    alertWarning.showAndWait();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء اختيار مورد ");
            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    private void PayDebtReceiptMedication(int receiptId , double price) {
        try {
            Receipt receipt1 = new Receipt();
            receipt1.setPaying(price);

            Receipt receipt2 =  new Receipt();
            receipt2.setId(receiptId);

            receiptMedicationOperation.updatePaying(receipt1,receipt2);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void PayDebtReceiptMaterial(int receiptId , double price) {
        try {
            Receipt receipt1 = new Receipt();
            receipt1.setPaying(price);

            Receipt receipt2 =  new Receipt();
            receipt2.setId(receiptId);

            receiptRawMaterialOperation.updatePaying(receipt1,receipt2);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refresh(){
        dataTable.clear();
        try {
            ArrayList<Provider> providers = operation.getAll();
            providers.forEach(provider -> {
                double debt = 0.0;
                AtomicReference<Double> trans = new AtomicReference<>(0.0);
                AtomicReference<Double> pay = new AtomicReference<>(0.0);

                // Medication
                ArrayList<Receipt> receipts = receiptMedicationOperation.getAllByProvider(provider.getId());
                receipts.forEach(receipt -> {
                    ArrayList<ComponentReceipt> componentReceipts = componentMedicationOperation.getAllByReceipt(receipt.getId());
                    AtomicReference<Double> sumR = new AtomicReference<>(0.0);
                    componentReceipts.forEach(componentReceipt -> {
                        double pr = componentReceipt.getPrice() * componentReceipt.getQte();
                        sumR.updateAndGet(v -> (double) (v + pr));
                    });
                    pay.updateAndGet(v -> (double) (v + receipt.getPaying()));
                    trans.updateAndGet(v -> (double) (v + sumR.get()));
                });

                // Raw Material
                ArrayList<Receipt> receiptsMaterial = receiptRawMaterialOperation.getAllByProvider(provider.getId());
                receiptsMaterial.forEach(receipt -> {
                    ArrayList<ComponentReceipt> componentReceipts = componentReceiptRawMaterialOperation.getAllByReceipt(receipt.getId());
                    AtomicReference<Double> sumR = new AtomicReference<>(0.0);
                    componentReceipts.forEach(componentReceipt -> {
                        double pr = componentReceipt.getPrice() * componentReceipt.getQte();
                        sumR.updateAndGet(v -> (double) (v + pr));
                    });
                    pay.updateAndGet(v -> (double) (v + receipt.getPaying()));
                    trans.updateAndGet(v -> (double) (v + sumR.get()));
                });

                debt = trans.get() - pay.get();

                List<StringProperty> data = new ArrayList<>();
                data.add(0, new SimpleStringProperty(String.valueOf(provider.getId())));
                data.add(1, new SimpleStringProperty(provider.getName()));
                data.add(2, new SimpleStringProperty(provider.getAddress()));
                data.add(3, new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", trans.get())));
                data.add(4, new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", pay.get())));
                data.add(5, new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", debt)));

                dataTable.add(data);
            });

            table.setItems(dataTable);
        }catch (Exception e){
            e.printStackTrace();
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
            }  else return stringProperties.get(5).toString().contains(txtRecherche);
        });

        SortedList<List<StringProperty>> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }
}
