package Models;

public class ComponentStoreProductTemp {

    private int id;
    private int idProduction;
    private int idProduct;
    private int idInvoice;
    private int qte;

    public ComponentStoreProductTemp() {
    }

    public ComponentStoreProductTemp(int idProduction) {
        this.idProduction = idProduction;
    }

    public ComponentStoreProductTemp(int id, int idProduct, int idInvoice, int qte) {
        this.id = id;
        this.idProduct = idProduct;
        this.idInvoice = idInvoice;
        this.qte = qte;
    }

    public ComponentStoreProductTemp(int id, int idProduct, int idInvoice, int idProduction, int qte) {
        this.id = id;
        this.idProduct = idProduct;
        this.idInvoice = idInvoice;
        this.idProduction = idProduction;
        this.qte = qte;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(int idInvoice) {
        this.idInvoice = idInvoice;
    }

    public int getIdProduction() {
        return idProduction;
    }

    public void setIdProduction(int idProduction) {
        this.idProduction = idProduction;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }
}
