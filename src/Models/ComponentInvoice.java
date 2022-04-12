package Models;

public class ComponentInvoice {

    private int idInvoice;
    private int idComponent;
    private int qte;
    private double price;
    private double priceRoad;

    public ComponentInvoice() {
    }

    public ComponentInvoice(int idInvoice, int idComponent, int qte, double price, double priceRoad) {
        this.idInvoice = idInvoice;
        this.idComponent = idComponent;
        this.qte = qte;
        this.price = price;
        this.priceRoad = priceRoad;
    }

    public int getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(int idInvoice) {
        this.idInvoice = idInvoice;
    }

    public int getIdComponent() {
        return idComponent;
    }

    public void setIdComponent(int idComponent) {
        this.idComponent = idComponent;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPriceRoad() {
        return priceRoad;
    }

    public void setPriceRoad(double priceRoad) {
        this.priceRoad = priceRoad;
    }
}
