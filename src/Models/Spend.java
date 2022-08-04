package Models;

import java.time.LocalDate;

public class Spend {

    private int id;
    private double price;
    private LocalDate date;
    private String Raison;

    public Spend() {
    }

    public Spend(int id, double price, LocalDate date, String raison) {
        this.id = id;
        this.price = price;
        this.date = date;
        Raison = raison;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getRaison() {
        return Raison;
    }

    public void setRaison(String raison) {
        Raison = raison;
    }
}
