package Models;

public class ComponentProduction {

    private int idProduct;
    private int idComponent;
    private double qte;

    public ComponentProduction() {
    }

    public ComponentProduction(int idProduct, int idComponent, double qte) {
        this.idProduct = idProduct;
        this.idComponent = idComponent;
        this.qte = qte;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getIdComponent() {
        return idComponent;
    }

    public void setIdComponent(int idComponent) {
        this.idComponent = idComponent;
    }

    public double getQte() {
        return qte;
    }

    public void setQte(double qte) {
        this.qte = qte;
    }
}
