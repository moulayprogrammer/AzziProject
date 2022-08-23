package Models;

import java.time.LocalDate;

public class Payments {

    private int id;
    private int idPayer;
    private LocalDate date;
    private double pay;
    private double rest;

    public Payments() {
    }

    public Payments(int id, LocalDate date, double pay, double rest) {
        this.id = id;
        this.date = date;
        this.pay = pay;
        this.rest = rest;
    }

    public Payments(int id, int idPayer, LocalDate date, double pay, double rest) {
        this.id = id;
        this.idPayer = idPayer;
        this.date = date;
        this.pay = pay;
        this.rest = rest;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPayer() {
        return idPayer;
    }

    public void setIdPayer(int idPayer) {
        this.idPayer = idPayer;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getPay() {
        return pay;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }

    public double getRest() {
        return rest;
    }

    public void setRest(double rest) {
        this.rest = rest;
    }
}
