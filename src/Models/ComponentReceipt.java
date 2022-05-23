package Models;

public class ComponentReceipt {

    private int idReceipt;
    private int idComponent;
    private int qte;
    private double price;

    public ComponentReceipt() {
    }

    public ComponentReceipt(int idReceipt, int idComponent, int qte, double price) {
        this.idReceipt = idReceipt;
        this.idComponent = idComponent;
        this.qte = qte;
        this.price = price;
    }

    public int getIdReceipt() {
        return idReceipt;
    }

    public void setIdReceipt(int idReceipt) {
        this.idReceipt = idReceipt;
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
}
