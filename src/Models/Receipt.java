package Models;

import java.time.LocalDate;

public class Receipt {

    private int id;
    private int number;
    private int idProvider;
    private LocalDate date;
    private double paying;

    public Receipt() {
    }

    public Receipt(int id, int idProvider, LocalDate date, double paying) {
        this.id = id;
        this.idProvider = idProvider;
        this.date = date;
        this.paying = paying;
    }

    public Receipt(int id, int number, int idProvider, LocalDate date, double paying) {
        this.id = id;
        this.number = number;
        this.idProvider = idProvider;
        this.date = date;
        this.paying = paying;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProvider() {
        return idProvider;
    }

    public void setIdProvider(int idProvider) {
        this.idProvider = idProvider;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getPaying() {
        return paying;
    }

    public void setPaying(double paying) {
        this.paying = paying;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
