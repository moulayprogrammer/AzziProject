package Controllers.ClientControllers;

import BddPackage.ClientOperation;
import BddPackage.ComponentInvoiceOperation;
import BddPackage.InvoiceOperation;
import Models.Client;
import Models.ComponentInvoice;
import Models.Invoice;
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

import javax.swing.text.rtf.RTFEditorKit;
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
    TableColumn<List<StringProperty>,String> clId,clReference,clName,clAddress,clTransaction,clPaying,clDebt,clActivity,clNationalNumber;


    private final ObservableList<List<StringProperty>> dataTable = FXCollections.observableArrayList();
    private final ClientOperation operation = new ClientOperation();
    private final InvoiceOperation invoiceOperation = new InvoiceOperation();
    private final ComponentInvoiceOperation componentInvoiceOperation = new ComponentInvoiceOperation();



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        clId.setCellValueFactory(data -> data.getValue().get(0));
        clReference.setCellValueFactory(data -> data.getValue().get(1));
        clName.setCellValueFactory(data -> data.getValue().get(2));
        clAddress.setCellValueFactory(data -> data.getValue().get(3));
        clTransaction.setCellValueFactory(data -> data.getValue().get(4));
        clPaying.setCellValueFactory(data -> data.getValue().get(5));
        clDebt.setCellValueFactory(data -> data.getValue().get(6));
        clActivity.setCellValueFactory(data -> data.getValue().get(7));
        clNationalNumber.setCellValueFactory(data -> data.getValue().get(8));

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
            dialog.initOwner(this.tfRecherche.getScene().getWindow());
            dialog.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
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
        List<StringProperty> data  = table.getSelectionModel().getSelectedItem();

        if (data != null){
            try {
                Client client = operation.get(Integer.parseInt(data.get(0).getValue()));

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ClientViews/UpdateView.fxml"));
                DialogPane temp = loader.load();
                UpdateController controller = loader.getController();
                controller.InitUpdate(client);
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(temp);
                dialog.resizableProperty().setValue(false);
                dialog.initOwner(this.tfRecherche.getScene().getWindow());
                dialog.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
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
    private void ActionAddToArchive(){
        List<StringProperty> data  = table.getSelectionModel().getSelectedItem();

        if (data != null){
            if(data.get(6).getValue().equals("0,00")) {
                try {
                    Client client = operation.get(Integer.parseInt(data.get(0).getValue()));

                    Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                    alertConfirmation.setHeaderText("تاكيد الارشفة");
                    alertConfirmation.setContentText("هل انت متاكد من ارشفة الزبون");
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
                alertWarning.setContentText("الزبون مدين بـ : "+ data.get(6).getValue() + "  لا يمكن ارشفته حتى يسدد الدين ");
                alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");
                alertWarning.showAndWait();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء اختيار زبون لارشفته");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ClientViews/ArchiveView.fxml"));
            DialogPane temp = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.initOwner(this.tfRecherche.getScene().getWindow());
            dialog.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
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
    private void ActionPayDebtClient(){
        List<StringProperty> data  = table.getSelectionModel().getSelectedItem();
        if (data != null) {
            try {
                Client client = operation.get(Integer.parseInt(data.get(0).getValue()));

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
                        ArrayList<Invoice> invoices = invoiceOperation.getAllByClient(client.getId());
                        for (Invoice invoice : invoices) {
                            ArrayList<ComponentInvoice> componentInvoices = componentInvoiceOperation.getAllByInvoice(invoice.getId());
                            AtomicReference<Double> sumR = new AtomicReference<>(0.0);
                            componentInvoices.forEach(componentInvoice -> {
                                double pre = componentInvoice.getPrice() * componentInvoice.getQte();
                                sumR.updateAndGet(v -> v + pre);
                            });
                            double debt = sumR.get() - invoice.getPaying();
                            if (debt > 0) {
                                if (pr <= debt) {
                                    double newPaying = pr + invoice.getPaying();
                                    pr = 0;
                                    PayDebtInvoice(invoice.getId(), newPaying);
                                    break;
                                } else {
                                    double newPaying = debt + invoice.getPaying();
                                    pr = pr - debt;
                                    PayDebtInvoice(invoice.getId(), newPaying);
                                }
                            }
                        }
                        if (pr > 0 ){
                            Alert alertInformation = new Alert(Alert.AlertType.INFORMATION);
                            alertInformation.setHeaderText("المبلغ المتبقي ");
                            alertInformation.setContentText("المبلغ المتبقي من التسديد هو   " + String.format(Locale.FRANCE, "%,.2f", (pr)));
                            alertInformation.initOwner(this.tfRecherche.getScene().getWindow());
                            alertInformation.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                            Button okButton = (Button) alertInformation.getDialogPane().lookupButton(ButtonType.OK);
                            okButton.setText("موافق");
                            alertInformation.showAndWait();
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء اختيار زبون لدفع الدين");
            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }



    }

    private void PayDebtInvoice(int invoiceId , double price) {
        try {
            Invoice invoice1 = new Invoice();
            invoice1.setPaying(price);

            Invoice invoice2 =  new Invoice();
            invoice2.setId(invoiceId);

            invoiceOperation.updatePaying(invoice1,invoice2);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refresh(){
        dataTable.clear();
        try {
            ArrayList<Client> clients = operation.getAll();
            clients.forEach(client -> {
                double debt = 0.0;
                AtomicReference<Double> trans = new AtomicReference<>(0.0);
                AtomicReference<Double> pay = new AtomicReference<>(0.0);

                ArrayList<Invoice> invoices = invoiceOperation.getAllByClient(client.getId());
                invoices.forEach(invoice -> {
                    ArrayList<ComponentInvoice> componentInvoices = componentInvoiceOperation.getAllByInvoice(invoice.getId());
                    AtomicReference<Double> sumR = new AtomicReference<>(0.0);
                    componentInvoices.forEach(componentInvoice -> {
                        double pr = componentInvoice.getPrice() * componentInvoice.getQte();
                        sumR.updateAndGet(v -> v + pr);
                    });
                    pay.updateAndGet(v -> v + invoice.getPaying());
                    trans.updateAndGet(v -> v + sumR.get());
                });
                debt = trans.get() - pay.get();

                List<StringProperty> data = new ArrayList<>();
                data.add(0, new SimpleStringProperty(String.valueOf(client.getId())));
                data.add(1, new SimpleStringProperty(client.getReference()));
                data.add(2, new SimpleStringProperty(client.getName()));
                data.add(3, new SimpleStringProperty(client.getAddress()));
                data.add(4, new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", trans.get())));
                data.add(5, new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", pay.get())));
                data.add(6, new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", debt)));
                data.add(7, new SimpleStringProperty(client.getActivity()));
                data.add(8, new SimpleStringProperty(client.getNationalNumber()));

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
            } else if (stringProperties.get(5).toString().contains(txtRecherche)) {
                return true;
            }else if (stringProperties.get(6).toString().contains(txtRecherche)) {
                return true;
            } else if (stringProperties.get(7).toString().contains(txtRecherche)) {
                return true;
            } else return stringProperties.get(8).toString().contains(txtRecherche);
        });

        SortedList<List<StringProperty>> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }
}
