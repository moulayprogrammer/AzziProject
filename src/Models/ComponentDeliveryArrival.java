package Models;

public class ComponentDeliveryArrival {

    private int idDeliveryArrival;
    private int idComponent;
    private int qteReceipt;
    private int qteReal;

    public ComponentDeliveryArrival() {
    }

    public ComponentDeliveryArrival(int idDeliveryArrival, int idComponent, int qteReceipt, int qteReal) {
        this.idDeliveryArrival = idDeliveryArrival;
        this.idComponent = idComponent;
        this.qteReceipt = qteReceipt;
        this.qteReal = qteReal;
    }

    public int getIdDeliveryArrival() {
        return idDeliveryArrival;
    }

    public void setIdDeliveryArrival(int idDeliveryArrival) {
        this.idDeliveryArrival = idDeliveryArrival;
    }

    public int getIdComponent() {
        return idComponent;
    }

    public void setIdComponent(int idComponent) {
        this.idComponent = idComponent;
    }

    public int getQteReceipt() {
        return qteReceipt;
    }

    public void setQteReceipt(int qteReceipt) {
        this.qteReceipt = qteReceipt;
    }

    public int getQteReal() {
        return qteReal;
    }

    public void setQteReal(int qteReal) {
        this.qteReal = qteReal;
    }
}
