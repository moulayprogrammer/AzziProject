package Models;

import java.time.LocalDate;

public class Invoice {

    private int id;
    private int number;
    private int idClient;
    private LocalDate date;
    private double paying;
    private String confirmation;

    public Invoice() {
    }

    public Invoice(int id, int idClient, LocalDate date, double paying) {
        this.id = id;
        this.idClient = idClient;
        this.date = date;
        this.paying = paying;
    }

    public Invoice(int id, int number, int idClient, LocalDate date, double paying) {
        this.id = id;
        this.number = number;
        this.idClient = idClient;
        this.date = date;
        this.paying = paying;
    }

    public Invoice(int id, int number, int idClient, LocalDate date, double paying, String confirmation) {
        this.id = id;
        this.number = number;
        this.idClient = idClient;
        this.date = date;
        this.paying = paying;
        this.confirmation = confirmation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
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

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }
}
