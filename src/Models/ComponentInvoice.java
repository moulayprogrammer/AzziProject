package Models;

public class ComponentInvoice {

    private int idInvoice;
    private int idProduct;
    private int qte;
    private double price;
    private double priceRoad;

    public ComponentInvoice() {
    }

    public ComponentInvoice(int idInvoice, int idProduct, int qte, double price, double priceRoad) {
        this.idInvoice = idInvoice;
        this.idProduct = idProduct;
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

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
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
