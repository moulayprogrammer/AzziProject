package Models;

import java.time.LocalDate;

public class ComponentStore {

    private int idComponent;
    private int idDeliveryArrival;
    private LocalDate dateStore;
    private double price;
    private double qteStored;
    private double qteConsumed;

    public ComponentStore() {
    }

    public ComponentStore(int idDeliveryArrival, LocalDate dateStore, double price, double qteStored, double qteConsumed) {
        this.idDeliveryArrival = idDeliveryArrival;
        this.dateStore = dateStore;
        this.price = price;
        this.qteStored = qteStored;
        this.qteConsumed = qteConsumed;
    }

    public ComponentStore(int idComponent, int idDeliveryArrival, LocalDate dateStore, double price, double qteStored, double qteConsumed) {
        this.idComponent = idComponent;
        this.idDeliveryArrival = idDeliveryArrival;
        this.dateStore = dateStore;
        this.price = price;
        this.qteStored = qteStored;
        this.qteConsumed = qteConsumed;
    }

    public ComponentStore(int idComponent, int idDeliveryArrival, double qteConsumed) {
        this.idComponent = idComponent;
        this.idDeliveryArrival = idDeliveryArrival;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public int getIdDeliveryArrival() {
        return idDeliveryArrival;
    }

    public void setIdDeliveryArrival(int idDeliveryArrival) {
        this.idDeliveryArrival = idDeliveryArrival;
    }
}
