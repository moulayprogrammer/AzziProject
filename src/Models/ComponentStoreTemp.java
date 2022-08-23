package Models;

public class ComponentStoreTemp {

    private int id;
    private int idComponent;
    private int idDeliveryArrival;
    private int idProduction;
    private double qte;

    public ComponentStoreTemp() {
    }

    public ComponentStoreTemp(int idProduction) {
        this.idProduction = idProduction;
    }

    public ComponentStoreTemp(int id, int idComponent, int idDeliveryArrival, int idProduction, double qte) {
        this.id = id;
        this.idComponent = idComponent;
        this.idDeliveryArrival = idDeliveryArrival;
        this.idProduction = idProduction;
        this.qte = qte;
    }

    public ComponentStoreTemp(int idComponent, int idDeliveryArrival, int idProduction, double qte) {
        this.idComponent = idComponent;
        this.idDeliveryArrival = idDeliveryArrival;
        this.idProduction = idProduction;
        this.qte = qte;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdComponent() {
        return idComponent;
    }

    public void setIdComponent(int idComponent) {
        this.idComponent = idComponent;
    }

    public int getIdDeliveryArrival() {
        return idDeliveryArrival;
    }

    public void setIdDeliveryArrival(int idDeliveryArrival) {
        this.idDeliveryArrival = idDeliveryArrival;
    }

    public int getIdProduction() {
        return idProduction;
    }

    public void setIdProduction(int idProduction) {
        this.idProduction = idProduction;
    }

    public double getQte() {
        return qte;
    }

    public void setQte(double qte) {
        this.qte = qte;
    }
}
