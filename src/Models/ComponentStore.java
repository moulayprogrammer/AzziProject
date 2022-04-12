package Models;

public class ComponentStore {

    private int idStore;
    private int idComponent;
    private double price;
    private int qteStored;
    private int qteConsumed;

    public ComponentStore() {
    }

    public ComponentStore(int idStore, int idComponent, double price, int qteStored, int qteConsumed) {
        this.idStore = idStore;
        this.idComponent = idComponent;
        this.price = price;
        this.qteStored = qteStored;
        this.qteConsumed = qteConsumed;
    }

    public int getIdStore() {
        return idStore;
    }

    public void setIdStore(int idStore) {
        this.idStore = idStore;
    }

    public int getIdComponent() {
        return idComponent;
    }

    public void setIdComponent(int idComponent) {
        this.idComponent = idComponent;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQteStored() {
        return qteStored;
    }

    public void setQteStored(int qteStored) {
        this.qteStored = qteStored;
    }

    public int getQteConsumed() {
        return qteConsumed;
    }

    public void setQteConsumed(int qteConsumed) {
        this.qteConsumed = qteConsumed;
    }
}
