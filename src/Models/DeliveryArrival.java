package Models;

import java.time.LocalDate;

public class DeliveryArrival {

    private int id;
    private int idDelivery;
    private int idReceipt;
    private LocalDate date;
    private double price;

    public DeliveryArrival() {
    }

    public DeliveryArrival(int id, int idDelivery, int idReceipt, LocalDate date, double price) {
        this.id = id;
        this.idDelivery = idDelivery;
        this.idReceipt = idReceipt;
        this.date = date;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdDelivery() {
        return idDelivery;
    }

    public void setIdDelivery(int idDelivery) {
        this.idDelivery = idDelivery;
    }

    public int getIdReceipt() {
        return idReceipt;
    }

    public void setIdReceipt(int idReceipt) {
        this.idReceipt = idReceipt;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
