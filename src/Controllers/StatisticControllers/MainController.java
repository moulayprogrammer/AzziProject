package Controllers.StatisticControllers;

import BddPackage.*;
import Models.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    Label lbSumDebtPorchesRM,lbSumPayPorchesRM,lbSumPorchesRM;
    @FXML
    Label lbSumDebtPorchesM,lbSumPayPorchesM,lbSumPorchesM;
    @FXML
    Label lbSumDebtSales,lbSumPaySales,lbSumSales;
    @FXML
    Label lbQteStoreRM,lbSumStoreRM;
    @FXML
    Label lbQteStoreRMND,lbSumStoreRMND;
    @FXML
    Label lbQteStoreM,lbSumStoreM;
    @FXML
    Label lbQteStoreMND,lbSumStoreMND;
    @FXML
    Label lbQteStorePr,lbSumStorePr;
    @FXML
    Label lbQteDamageRM,lbSumDamageRM;
    @FXML
    Label lbQteDamageM,lbSumDamageM;
    @FXML
    Label lbQteDamagePr,lbSumDamagePr;
    @FXML
    Label lbSumSpend;
    @FXML
    Label lbFund;
    @FXML
    Label lbCapital;

    private final InvoiceOperation invoiceOperation = new InvoiceOperation();
    private final ReceiptRawMaterialOperation receiptRawMaterialOperation = new ReceiptRawMaterialOperation();
    private final ReceiptMedicationOperation receiptMedicationOperation = new ReceiptMedicationOperation();
    private final DeliveryArrivalRawMaterialOperation deliveryArrivalRawMaterialOperation = new DeliveryArrivalRawMaterialOperation();
    private final DeliveryArrivalMedicationOperation deliveryArrivalMedicationOperation = new DeliveryArrivalMedicationOperation();

    private final DamageRawMaterialOperation damageRawMaterialOperation = new DamageRawMaterialOperation();
    private final DamageMedicationOperation damageMedicationOperation = new DamageMedicationOperation();
    private final DamageProductOperation damageProductOperation = new DamageProductOperation();
    private final SpendOperation spendOperation = new SpendOperation();
    private final ProductionOperation productionOperation = new ProductionOperation();

    private final ComponentInvoiceOperation componentInvoiceOperation = new ComponentInvoiceOperation();
    private final ComponentReceiptRawMaterialOperation componentReceiptRawMaterialOperation = new ComponentReceiptRawMaterialOperation();
    private final ComponentReceiptMedicationOperation componentReceiptMedicationOperation = new ComponentReceiptMedicationOperation();
    private final ComponentDeliveryArrivalRawMaterialOperation componentDeliveryArrivalRawMaterialOperation = new ComponentDeliveryArrivalRawMaterialOperation();
    private final ComponentDeliveryArrivalMedicationOperation componentDeliveryArrivalMedicationOperation = new ComponentDeliveryArrivalMedicationOperation();
    private final ComponentDamageRawMaterialOperation componentDamageRawMaterialOperation = new ComponentDamageRawMaterialOperation();
    private final ComponentDamageMedicationOperation componentDamageMedicationOperation = new ComponentDamageMedicationOperation();
    private final ComponentDamageProductOperation componentDamageProductOperation = new ComponentDamageProductOperation();
    private final ComponentStoreRawMaterialOperation componentStoreRawMaterialOperation = new ComponentStoreRawMaterialOperation();
    private final ComponentStoreRawMaterialTempOperation componentStoreRawMaterialTempOperation = new ComponentStoreRawMaterialTempOperation();
    private final ComponentStoreMedicationOperation componentStoreMedicationOperation = new ComponentStoreMedicationOperation();
    private final ComponentStoreMedicationTempOperation componentStoreMedicationTempOperation = new ComponentStoreMedicationTempOperation();
    private final ComponentStoreProductOperation componentStoreProductOperation = new ComponentStoreProductOperation();
    private final ComponentStoreProductTempOperation componentStoreProductTempOperation = new ComponentStoreProductTempOperation();


    private double sumDebtPorchesRM = 0.0, sumPayPorchesRM = 0.0, sumPorchesRM = 0.0;
    private double sumDebtPorchesM = 0.0, sumPayPorchesM = 0.0, sumPorchesM = 0.0;
    private double sumDebtSales, sumPaySales = 0.0, sumSales = 0.0;
    private double sumQteStoreRM = 0.0, sumStoreRM = 0.0;
    private double sumQteStoreRMND = 0.0, sumStoreRMND = 0.0;
    private double sumQteStoreM = 0.0, sumStoreM = 0.0;
    private double sumQteStoreMND = 0.0, sumStoreMND = 0.0;
    private double sumQteStorePr = 0.0, sumStorePr = 0.0;

    private double sumQteDamageRM = 0.0, sumDamageRM = 0.0;
    private double sumQteDamageM = 0.0, sumDamageM = 0.0;
    private double sumQteDamagePr = 0.0, sumDamagePr = 0.0;
    private double sumSpend;

    private double capitalPrincipal = 48870000.0;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        porchesRM();
        porchesM();
        Sales();
        StoreM();
        StoreRM();
        StorePr();
        DamageRM();
        DamageM();
        DamagePr();
        Spend();

        sumFund();
        sumCapital();
    }

    private void porchesRM(){
        try {
            ArrayList<Receipt> receipts = receiptRawMaterialOperation.getAll();
            for (Receipt receipt : receipts){
                ArrayList<ComponentReceipt> componentReceipts = componentReceiptRawMaterialOperation.getAllByReceipt(receipt.getId());
                double sumR = 0.0;
                for (ComponentReceipt componentReceipt : componentReceipts){
                    sumR += (componentReceipt.getQte() * componentReceipt.getPrice());
                }
                this.sumPorchesRM += sumR;
                this.sumPayPorchesRM += receipt.getPaying();
            }
            this.sumDebtPorchesRM = this.sumPorchesRM - this.sumPayPorchesRM;

            lbSumPorchesRM.setText(String.format(Locale.FRANCE, "%,.2f", sumPorchesRM));
            lbSumPayPorchesRM.setText(String.format(Locale.FRANCE, "%,.2f", sumPayPorchesRM));
            lbSumDebtPorchesRM.setText(String.format(Locale.FRANCE, "%,.2f", sumDebtPorchesRM));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void porchesM(){
        try {
            ArrayList<Receipt> receipts = receiptMedicationOperation.getAll();
            for (Receipt receipt : receipts){
                ArrayList<ComponentReceipt> componentReceipts = componentReceiptMedicationOperation.getAllByReceipt(receipt.getId());
                double sumR = 0.0;
                for (ComponentReceipt componentReceipt : componentReceipts){
                    sumR += (componentReceipt.getQte() * componentReceipt.getPrice());
                }
                this.sumPorchesM += sumR;
                this.sumPayPorchesM += receipt.getPaying();
            }
            this.sumDebtPorchesM = this.sumPorchesM - this.sumPayPorchesM;

            lbSumPorchesM.setText(String.format(Locale.FRANCE, "%,.2f", sumPorchesM));
            lbSumPayPorchesM.setText(String.format(Locale.FRANCE, "%,.2f", sumPayPorchesM));
            lbSumDebtPorchesM.setText(String.format(Locale.FRANCE, "%,.2f", sumDebtPorchesM));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void Sales(){
        try {
            ArrayList<Invoice> invoices = invoiceOperation.getAll();

            for (Invoice invoice : invoices){
                if (invoice.getConfirmation().equals("مأكد")) {
                    ArrayList<ComponentInvoice> componentInvoices = componentInvoiceOperation.getAllByInvoice(invoice.getId());
                    double sumR = 0.0;
                    for (ComponentInvoice componentInvoice : componentInvoices) {
                        sumR += (componentInvoice.getPrice() * componentInvoice.getQte());
                    }
                    this.sumSales += sumR;
                    this.sumPaySales += invoice.getPaying();
                }
            }

            this.sumDebtSales = this.sumSales - this.sumPaySales;

            lbSumSales.setText(String.format(Locale.FRANCE, "%,.2f", sumSales));
            lbSumDebtSales.setText(String.format(Locale.FRANCE, "%,.2f", sumDebtSales));
            lbSumPaySales.setText(String.format(Locale.FRANCE, "%,.2f", sumPaySales));


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void StoreRM(){
        try {
            ArrayList<ComponentStore> componentStores = componentStoreRawMaterialOperation.getAll();
            for (ComponentStore componentStore : componentStores){
                int qte = (componentStore.getQteStored() - componentStore.getQteConsumed());
                this.sumStoreRM += (qte * componentStore.getPrice());
                this.sumQteStoreRM += qte;
            }

            ArrayList<ComponentStoreTemp> componentStoreTemps = componentStoreRawMaterialTempOperation.getAll();
            for (ComponentStoreTemp componentStoreTemp : componentStoreTemps){
                ComponentStore componentStore = componentStoreRawMaterialOperation.get(componentStoreTemp.getIdComponent(), componentStoreTemp.getIdDeliveryArrival());

                this.sumStoreRM += (componentStoreTemp.getQte() * componentStore.getPrice());
                this.sumQteStoreRM += componentStoreTemp.getQte();
            }

            lbSumStoreRM.setText(String.format(Locale.FRANCE, "%,.2f", sumStoreRM));
            lbQteStoreRM.setText(String.valueOf(sumQteStoreRM));

            ArrayList<ComponentReceipt> componentReceipts = componentReceiptRawMaterialOperation.getAll();
            for (ComponentReceipt componentReceipt : componentReceipts) {
                int qteF = 0;

                ArrayList<DeliveryArrival> deliveryArrivals = deliveryArrivalRawMaterialOperation.getAllByReceipt(componentReceipt.getIdReceipt());
                for (DeliveryArrival deliveryArrival : deliveryArrivals){
                    ComponentStore store = componentStoreRawMaterialOperation.get(componentReceipt.getIdComponent() , deliveryArrival.getId());
                    ComponentDeliveryArrival componentDeliveryArrival = componentDeliveryArrivalRawMaterialOperation.getByDeliveryArrivalAndRawMaterial(deliveryArrival.getId(),componentReceipt.getIdComponent());

                    if (store.getIdDeliveryArrival() != 0){
                        qteF += componentDeliveryArrival.getQteReceipt();
                    }
                }

                int qte = componentReceipt.getQte() - qteF;
                sumQteStoreRMND += qte;
                sumStoreRMND += (qte * componentReceipt.getPrice());
            }

            lbSumStoreRMND.setText(String.format(Locale.FRANCE, "%,.2f", sumStoreRMND));
            lbQteStoreRMND.setText(String.valueOf(sumQteStoreRMND));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void StoreM(){
        try {
            ArrayList<ComponentStore> componentStores = componentStoreMedicationOperation.getAll();
            for (ComponentStore componentStore : componentStores){
                int qte = (componentStore.getQteStored() - componentStore.getQteConsumed());
                this.sumStoreM += (qte * componentStore.getPrice());
                this.sumQteStoreM += qte;
            }

            ArrayList<ComponentStoreTemp> componentStoreTemps = componentStoreMedicationTempOperation.getAll();
            for (ComponentStoreTemp componentStoreTemp : componentStoreTemps){
                ComponentStore componentStore = componentStoreMedicationOperation.get(componentStoreTemp.getIdComponent(), componentStoreTemp.getIdDeliveryArrival());

                this.sumStoreM += (componentStoreTemp.getQte() * componentStore.getPrice());
                this.sumQteStoreM += componentStoreTemp.getQte();
            }

            lbSumStoreM.setText(String.format(Locale.FRANCE, "%,.2f", sumStoreM));
            lbQteStoreM.setText(String.valueOf(sumQteStoreM));

            ArrayList<ComponentReceipt> componentReceipts = componentReceiptMedicationOperation.getAll();
            for (ComponentReceipt componentReceipt : componentReceipts) {
                int qteF = 0;

                ArrayList<DeliveryArrival> deliveryArrivals = deliveryArrivalMedicationOperation.getAllByReceipt(componentReceipt.getIdReceipt());
                for (DeliveryArrival deliveryArrival : deliveryArrivals){
                    ComponentStore store = componentStoreMedicationOperation.get(componentReceipt.getIdComponent() , deliveryArrival.getId());
                    ComponentDeliveryArrival componentDeliveryArrival = componentDeliveryArrivalMedicationOperation.getByDeliveryArrivalAndMedication(deliveryArrival.getId(),componentReceipt.getIdComponent());

                    if (store.getIdDeliveryArrival() != 0){
                        qteF += componentDeliveryArrival.getQteReceipt();
                    }
                }

                int qte = componentReceipt.getQte() - qteF;
                sumQteStoreMND += qte;
                sumStoreMND += (qte * componentReceipt.getPrice());
            }

            lbSumStoreMND.setText(String.format(Locale.FRANCE, "%,.2f", sumStoreMND));
            lbQteStoreMND.setText(String.valueOf(sumQteStoreMND));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void StorePr(){
        try {
            ArrayList<ComponentStoreProduct> componentStoreProducts = componentStoreProductOperation.getAll();
            for (ComponentStoreProduct componentStoreProduct : componentStoreProducts){
                Production production = productionOperation.get(componentStoreProduct.getIdProduction());

                this.sumStorePr += ((componentStoreProduct.getQteStored() - componentStoreProduct.getQteConsumed()) * production.getPrice());
                this.sumQteStorePr += (componentStoreProduct.getQteStored() - componentStoreProduct.getQteConsumed());
            }

            ArrayList<ComponentStoreProductTemp> componentStoreProductTemps = componentStoreProductTempOperation.getAll();
            for (ComponentStoreProductTemp componentStoreProductTemp : componentStoreProductTemps){
                Production production = productionOperation.get(componentStoreProductTemp.getIdProduction());

                this.sumStorePr += (componentStoreProductTemp.getQte()  * production.getPrice());
                this.sumQteStorePr += componentStoreProductTemp.getQte();
            }

            lbSumStorePr.setText(String.format(Locale.FRANCE, "%,.2f", sumStorePr));
            lbQteStorePr.setText(String.valueOf(sumQteStorePr));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void DamageRM(){
        try {
            ArrayList<Damage> damages = damageRawMaterialOperation.getAll();

            for (Damage damage : damages){
                ArrayList<ComponentDamage> componentDamages = componentDamageRawMaterialOperation.getAllByDamage(damage.getId());

                for (ComponentDamage componentDamage : componentDamages){
                    ComponentStore store = componentStoreRawMaterialOperation.get(componentDamage.getIdComponent(), componentDamage.getIdReference());
                    this.sumDamageRM += (componentDamage.getQte() * store.getPrice());
                    this.sumQteDamageRM += componentDamage.getQte();
                }
            }

            lbSumDamageRM.setText(String.format(Locale.FRANCE, "%,.2f", sumDamageRM));
            lbQteDamageRM.setText(String.valueOf(sumQteDamageRM));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void DamageM(){
        try {
            ArrayList<Damage> damages = damageMedicationOperation.getAll();

            for (Damage damage : damages){
                ArrayList<ComponentDamage> componentDamages = componentDamageMedicationOperation.getAllByDamage(damage.getId());

                for (ComponentDamage componentDamage : componentDamages){
                    ComponentStore store = componentStoreMedicationOperation.get(componentDamage.getIdComponent(), componentDamage.getIdReference());
                    this.sumDamageM += (componentDamage.getQte() * store.getPrice());
                    this.sumQteDamageM += componentDamage.getQte();
                }
            }

            lbSumDamageM.setText(String.format(Locale.FRANCE, "%,.2f", sumDamageM));
            lbQteDamageM.setText(String.valueOf(sumQteDamageM));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void DamagePr(){
        try {
            ArrayList<Damage> damages = damageProductOperation.getAll();

            for (Damage damage : damages){
                ArrayList<ComponentDamage> componentDamages = componentDamageProductOperation.getAllByDamage(damage.getId());

                for (ComponentDamage componentDamage : componentDamages){
                    Production production = productionOperation.get(componentDamage.getIdReference());

                    this.sumDamagePr += (componentDamage.getQte() * production.getPrice());
                    this.sumQteDamagePr += componentDamage.getQte();
                }
            }

            lbSumDamagePr.setText(String.format(Locale.FRANCE, "%,.2f", sumDamagePr));
            lbQteDamagePr.setText(String.valueOf(sumQteDamagePr));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void Spend(){
        try {
            ArrayList<Spend> spends = spendOperation.getAll();

            for (Spend spend : spends){
                this.sumSpend += spend.getPrice();
            }

            lbSumSpend.setText(String.format(Locale.FRANCE, "%,.2f", sumSpend));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sumFund(){
//        double stores = sumStoreRM + sumStoreM + sumStorePr + sumStoreRMND + sumStoreMND ;
        double sales = sumSales;
        double debtSales = sumDebtSales;
        double payPorches = sumPayPorchesRM + sumPayPorchesM;
        double debtPorches = sumDebtPorchesRM + sumDebtPorchesM;

//        double spends = sumDamageRM + sumDamageM + sumDamagePr + sumSpend;

        double fund = capitalPrincipal + 19599900.0 + sumPaySales - payPorches - sumSpend ;
        lbFund.setText(String.format(Locale.FRANCE, "%,.2f", fund));
    }

    private void sumCapital(){
        double stores = sumStoreRM + sumStoreM + sumStorePr + sumStoreRMND + sumStoreMND ;
        double sales = sumSales;
        double porches = sumPorchesRM + sumPorchesM;
        double spends = sumDamageRM + sumDamageM + sumDamagePr + sumSpend;

        double capital = capitalPrincipal +  stores + sales - porches - spends;
        lbCapital.setText(String.format(Locale.FRANCE, "%,.2f", capital));
    }

}
