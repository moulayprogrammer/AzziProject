package Models;

public class ComponentStoreProduct {

    private int idComponent;
    private int idProduction;
    private double priceHt;
    private int qteStored;
    private int qteConsumed;

    public ComponentStoreProduct() {
    }

    public ComponentStoreProduct(int idComponent, int idProduction, double priceHt, int qteStored, int qteConsumed) {
        this.idComponent = idComponent;
        this.idProduction = idProduction;
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
