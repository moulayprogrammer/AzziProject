package Models;

import java.time.LocalDate;

public class ComponentStoreProduct {

    private int idComponent;
    private int idProduction;
    private LocalDate dateStore;
    private double priceHt;
    private double qteStored;
    private double qteConsumed;

    public ComponentStoreProduct() {
    }

    public ComponentStoreProduct(int idComponent, int idProduction, double priceHt, int qteStored, int qteConsumed) {
        this.idComponent = idComponent;
        this.idProduction = idProduction;
        this.priceHt = priceHt;
        this.qteStored = qteStored;
        this.qteConsumed = qteConsumed;
    }

    public ComponentStoreProduct(int idComponent, int idProduction, LocalDate dateStore, double priceHt, int qteStored, int qteConsumed) {
        this.idComponent = idComponent;
        this.idProduction = idProduction;
        this.dateStore = dateStore;
        this.priceHt = priceHt;
        this.qteStored = qteStored;
        this.qteConsumed = qteConsumed;
    }

    public int getIdComponent() {
        return idComponent;
    }

    public void setIdComponent(int idComponent) {
        this.idComponent = idComponent;
    }

    public LocalDate getDateStore() {
        return dateStore;
    }

    public void setDateStore(LocalDate dateStore) {
        this.dateStore = dateStore;
    }

    public int getIdProduction() {
        return idProduction;
    }

    public void setIdProduction(int idProduction) {
        this.idProduction = idProduction;
    }

    public double getPriceHt() {
        return priceHt;
    }

    public void setPriceHt(double priceHt) {
        this.priceHt = priceHt;
    }

    public double getQteStored() {
        return qteStored;
    }

    public void setQteStored(double qteStored) {
        this.qteStored = qteStored;
    }

    public double getQteConsumed() {
        return qteConsumed;
    }

    public void setQteConsumed(double qteConsumed) {
        this.qteConsumed = qteConsumed;
    }
}
